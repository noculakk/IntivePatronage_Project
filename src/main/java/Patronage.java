import org.apache.commons.cli.*;

public class Patronage {
    public static void main(String[] args) {

        Options options = new Options();

        options.addOption("h", "help", false, "Print help message and quit");
        options.addOption("p", "source-path", true, "Path of source file");
        options.addOption("r", "result-path", true, "Path of result file");
        options.addOption("x", "xlsx", false, "save to .xlsx file");
        options.addOption("z", "zip", false, "zip result file(s)");

        CommandLineParser parser = new DefaultParser();

        try {
            CommandLine line = parser.parse(options, args);

            if (line.hasOption("help")) {
                HelpFormatter formatter = new HelpFormatter();
                formatter.printHelp("java Patronage [OPTIONS]", options);
                return;
            }
        } catch (ParseException exp) {
            System.out.println("Unexpected exception:" + exp.getMessage());
        }
    }
}