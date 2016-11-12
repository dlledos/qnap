package org.nas.tools.standardizer;
import org.apache.commons.cli.*;
import org.nas.tools.tvshow.TvShowStandardizer;

import java.net.URISyntaxException;
import java.nio.file.Path;
import java.nio.file.Paths;

public class Main {

    private static String binName;

    public static void main(String[] args) throws Exception {
        standardize(args, new TvShowStandardizer());
    }

    public static void standardize(String[] args, Standardizer standardizer) throws URISyntaxException {
        binName = getName().toString();
        Options options = new Options();
        addOption(options, "s", "sourceDirectory", "Source directory path");
        addOption(options, "d", "destinationDirectory", "Destination directory path");
        CommandLine cmd = parse(args, options);
        String sourceDirectory = cmd.getOptionValue("sourceDirectory");
        String destinationDirectory = cmd.getOptionValue("destinationDirectory");

        FileManager fileManager = new FileManager(destinationDirectory, standardizer);
        try {
            fileManager.move(sourceDirectory);
        }
        catch (Exception e){
            System.out.println("Can't move file " + e);
            new HelpFormatter().printHelp(binName, options);
            System.exit(1);
        }
    }

    private static Path getName() throws URISyntaxException {
        Path path = Paths.get(new Main().getClass().getProtectionDomain().getCodeSource().getLocation().toURI());
        return path.getName(path.getNameCount()-1);
    }

    private static CommandLine parse(String[] args, Options options) {
        CommandLineParser parser = new DefaultParser();
        try {
            return parser.parse(options, args);
        } catch (ParseException e) {
            System.out.println(e.getMessage());
            new HelpFormatter().printHelp(binName, options);
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
