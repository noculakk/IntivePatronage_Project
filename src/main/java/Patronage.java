import org.apache.commons.cli.*;

import java.io.File;
import java.io.IOException;

public class Patronage {
    public static void main(String[] args) {
        args = new String[] {"--source-path=C:\\Users\\Nela\\IdeaProjects\\IntivePatronage_Project\\src\\main\\java\\test1.txt"};
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
            if(line.hasOption("source-path")) {
                String sourcePath = line.getOptionValue("source-path");
                File f = new File(sourcePath);
                if(!f.exists()) {
                    System.out.println("Dany plik nie istnieje!");
                    return;
                }
                if (WordCounter.checkSize(sourcePath, 1024 * 1024 * 5)) {
                    var wordsMap = WordCounter.countWords(sourcePath);
                    WordCounter.sortWords(wordsMap).forEach(entry->{
                        System.out.println(entry.getKey() + " " + entry.getValue());
                    });
                }
            }
            else {
                System.out.println("Nie podano ścieżki pliku źródłowego");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }


    }
}