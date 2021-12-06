import org.apache.commons.cli.*;

import java.io.File;
import java.io.IOException;

public class Patronage {
    public static void main(String[] args) {
        args = new String[]{"--source-path=C:\\Users\\nocul\\IdeaProjects\\IntivePatronage_Project\\test1.txt"};
        Options options = new Options();

        options.addOption("h", "help", false, "Print help message and quit");
        options.addOption("p", "source-path", true, "Path of source file");
        options.addOption("r", "result-path", true, "Path of result file");
        options.addOption("x", "xlsx", false, "save to .xlsx file");
        options.addOption("z", "zip", false, "zip result file(s)");

        CommandLineParser parser = new DefaultParser();
        CommandLine line;
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
            if (!line.hasOption("source-path")) {
                System.out.println("Nie podano ścieżki pliku źródłowego");
                return;
            }

            String sourcePath = line.getOptionValue("source-path");
            File sourceFile = new File(sourcePath);
            if (!sourceFile.exists()) {
                System.out.println("Dany plik nie istnieje!");
                return;
            }
            if (!WordCounter.checkSize(sourcePath, Settings.MAX_FILE_SIZE_BYTES)) {
                System.out.println("Podany plik ma rozmiar powyzej 5 MB!");
                return;
            }

            var wordsMap = WordCounter.countWords(sourcePath);
            System.out.printf("%15s %17s", "Słowo:", "Wystąpienia:");

            WordCounter.sortWords(wordsMap).forEach(entry -> {
                System.out.println("");
                System.out.printf("%15s %17d", entry.getKey(), entry.getValue());
            });
        } catch (IOException e) {
            e.printStackTrace();
        }


    }
}