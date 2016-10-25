package tvshow;
import org.apache.commons.cli.*;

import java.io.File;

public class Main {

/*
 java -jar target/my-utility.jar -i asd
 */
    public static void main(String[] args) throws Exception {
        Options options = new Options();
        addOption(options, "s", "sourceDirectory", "Source directory path");
        addOption(options, "d", "destinationDirectory", "Destination directory path");
        CommandLine cmd = parse(args, options);
        String sourceDirectory = cmd.getOptionValue("sourceDirectory");
        String destinationDirectory = cmd.getOptionValue("destinationDirectory");

        try {
            new TvShowManager(new File(destinationDirectory)).move(new File(sourceDirectory));
        }
        catch (Exception e){
            System.out.println("Can't move file " + e);
            new HelpFormatter().printHelp("tvshowManager", options);
            System.exit(1);
        }
    }

    private static CommandLine parse(String[] args, Options options) {
        CommandLineParser parser = new DefaultParser();
        try {
            return parser.parse(options, args);
        } catch (ParseException e) {
            System.out.println(e.getMessage());
            new HelpFormatter().printHelp("tvshowManager", options);
            System.exit(1);
            return null;
        }
    }

    private static void addOption(Options options, String opt, String longOpt, String description) {
        Option srcDir = new Option(opt, longOpt, true, description);
        srcDir.setRequired(true);
        options.addOption(srcDir);
    }
}
