import org.apache.commons.cli.*;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFFont;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.*;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

public class Patronage {
    public static void main(String[] args) {
        // Option definitions
        Options options = new Options();

        options.addOption("h", "help", false, "Print help message and quit");
        options.addOption("p", "source-path", true, "Path of source file (including file)");
        options.addOption("r", "result-path", true, "Path of result file (directory; source directory by default)");
        options.addOption("x", "xlsx", false, "save to .xlsx file");
        options.addOption("z", "zip", false, "zip result file(s)");

        CommandLineParser parser = new DefaultParser();
        CommandLine line;

        // Option parsing
        try {
            line = parser.parse(options, args);

            if (line.hasOption("help")) {
                HelpFormatter formatter = new HelpFormatter();
                formatter.printHelp("java -jar IntivePatronage_Project.jar [OPTIONS]", options);
                return;
            }
        } catch (ParseException exp) {
            System.out.println("Unexpected exception:" + exp.getMessage());
            return;
        }

        // List of created files during execution of this script - will be used later,
        // during optional zip creation.
        List<File> createdFiles = new ArrayList<>();

        try {
            // Source file checks
            if (!line.hasOption("source-path")) {
                System.out.println("Source path not set!");
                return;
            }

            String sourcePath = line.getOptionValue("source-path");
            File sourceFile = new File(sourcePath);
            if (!sourceFile.exists()) {
                System.out.println("No file found at source path!");
                return;
            }
            if (!WordCounter.checkSize(sourcePath, Settings.MAX_FILE_SIZE_BYTES)) {
                System.out.println("File at source path is larger than 5 MB");
                return;
            }

            // Init outputs
            // Txt writer
            File txtResultFile = new File(
                    String.valueOf(
                            Paths.get(line.getOptionValue("result-path", line.getOptionValue("source-path") + "/.."), "result.txt")
                    )
            );
            FileWriter resultFileWriter = new FileWriter(txtResultFile);
            createdFiles.add(txtResultFile);

            // Main procedure: counting words from file
            var wordsMap = WordCounter.countWords(sourcePath);
            System.out.printf("%15s %17s", "Word:", "Occurrences:");

            XSSFWorkbook workbook = null;
            Sheet sheet = null;
            Row header;
            CellStyle headerStyle;
            XSSFFont font;
            Cell headerCell;
            CellStyle style = null;
            CellStyle styleAlt = null;

            if (line.hasOption("xlsx")) {
                workbook = new XSSFWorkbook();
                sheet = workbook.createSheet("result_sheet");
                sheet.setColumnWidth(0, 4000);
                sheet.setColumnWidth(1, 4000);
                header = sheet.createRow(0);

                headerStyle = workbook.createCellStyle();
                headerStyle.setFillForegroundColor(IndexedColors.GREY_80_PERCENT.getIndex());
                headerStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);

                font = workbook.createFont();
                font.setFontHeightInPoints((short) 12);
                font.setBold(true);
                font.setColor(IndexedColors.WHITE.getIndex());

                headerStyle.setFont(font);
                headerCell = header.createCell(0);

                headerCell.setCellValue("Word");
                headerCell.setCellStyle(headerStyle);
                headerCell = header.createCell(1);
                headerCell.setCellValue("Occurences");
                headerCell.setCellStyle(headerStyle);

                style = workbook.createCellStyle();
                style.setWrapText(true);
                style.setFillForegroundColor(IndexedColors.GREY_25_PERCENT.getIndex());
                style.setFillPattern(FillPatternType.SOLID_FOREGROUND);

                styleAlt = workbook.createCellStyle();
                styleAlt.setWrapText(true);
                styleAlt.setFillForegroundColor(IndexedColors.GREY_40_PERCENT.getIndex());
                styleAlt.setFillPattern(FillPatternType.SOLID_FOREGROUND);
            }

            // Final definition of variables uses inside lambda expression
            Sheet finalSheet = sheet;
            CellStyle finalStyle = style;
            CellStyle finalStyleAlt = styleAlt;

            // Reading stream from previous method and printing out to chosen outputs
            AtomicInteger currentLine = new AtomicInteger();

            WordCounter.sortWords(wordsMap).forEach(entry -> {
                try {
                    // Print to standard output
                    System.out.printf("\n%15s %17d", entry.getKey(), entry.getValue());

                    // Write to txt file
                    resultFileWriter.write(entry.getKey() + "\t" + entry.getValue() + "\n");

                    // Optionally write to XLSX file
                    if (line.hasOption("xlsx")) {
                        Row row = finalSheet.createRow(currentLine.getAndIncrement() + 1);
                        Cell cell = row.createCell(0);
                        cell.setCellValue(entry.getKey());
                        cell.setCellStyle(currentLine.get() % 2 == 0 ? finalStyle : finalStyleAlt);

                        cell = row.createCell(1);
                        cell.setCellValue(entry.getValue());
                        cell.setCellStyle(currentLine.get() % 2 == 0 ? finalStyle : finalStyleAlt);
                    }

                } catch (IOException e) {
                    System.out.println("I/O Error while reading from file!");
                    e.printStackTrace();
                }
            });

            // Close inputs
            resultFileWriter.close();

            if (line.hasOption("xlsx")) {

                File xlsxResultFile = new File(
                        String.valueOf(
                                Paths.get(line.getOptionValue("result-path", line.getOptionValue("source-path") + "/.."), "result.xlsx")
                        )
                );
                FileOutputStream outputStream = new FileOutputStream(xlsxResultFile);
                workbook.write(outputStream);
                workbook.close();
                createdFiles.add(xlsxResultFile);
            }

            // (optional) create zip from files
            if (line.hasOption("zip")) {
                FileOutputStream fos = new FileOutputStream(
                        String.valueOf(
                                Paths.get(line.getOptionValue("result-path", line.getOptionValue("source-path") + "/.."), "result.zip")
                        )
                );
                ZipOutputStream zipOut = new ZipOutputStream(fos);
                for (File createdFile : createdFiles) {
                    FileInputStream fis = new FileInputStream(createdFile);
                    // Add zip entry
                    ZipEntry zipEntry = new ZipEntry(createdFile.getName());
                    zipOut.putNextEntry(zipEntry);

                    // Fill zip entry with file contents
                    byte[] bytes = new byte[1024];
                    int length;
                    while ((length = fis.read(bytes)) >= 0) {
                        zipOut.write(bytes, 0, length);
                    }
                    fis.close();
                }
                zipOut.close();
                fos.close();
            }

        } catch (IOException e) {
            System.out.println("I/O Error while reading from file!");
            e.printStackTrace();
        }
    }
}