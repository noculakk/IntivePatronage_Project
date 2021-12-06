import org.apache.commons.cli.*;

import java.io.File;
import java.io.IOException;

public class Patronage {
    public static void main(String[] args) {
        args = new String[]{"--source-path=C:\\Users\\nocul\\IdeaProjects\\IntivePatronage_Project\\test1.txt"};

        // Option definitions
        Options options = new Options();

        options.addOption("h", "help", false, "Print help message and quit");
        options.addOption("p", "source-path", true, "Path of source file");
        options.addOption("r", "result-path", true, "Path of result file");
        options.addOption("x", "xlsx", false, "save to .xlsx file");
        options.addOption("z", "zip", false, "zip result file(s)");

        CommandLineParser parser = new DefaultParser();
        CommandLine line;

        // Option parsing
        try {
            line = parser.parse(options, args);

            if (line.hasOption("help")) {
                HelpFormatter formatter = new HelpFormatter();
                formatter.printHelp("java Patronage [OPTIONS]", options);
                return;
            }
        } catch (ParseException exp) {
            System.out.println("Unexpected exception:" + exp.getMessage());
            return;
        }

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

            // Main procedure: counting words from file
            var wordsMap = WordCounter.countWords(sourcePath);
            System.out.printf("%15s %17s", "Word:", "Occurrences:");

            // Reading stream from previous method and printing out to chosen outputs
            WordCounter.sortWords(wordsMap).forEach(entry -> {
                // Print to standard output
                System.out.printf("\n%15s %17d", entry.getKey(), entry.getValue());
            });
        } catch (IOException e) {
            System.out.println("I/O Error while reading from file!");
            e.printStackTrace();
        }
    }
}