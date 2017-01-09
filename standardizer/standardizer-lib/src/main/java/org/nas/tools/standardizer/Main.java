package org.nas.tools.standardizer;

import org.apache.commons.cli.*;

import java.net.URISyntaxException;
import java.nio.file.Path;
import java.nio.file.Paths;

public class Main {

    private static String binName;

    public static void standardize(String[] args, Standardizer standardizer) throws URISyntaxException {
        binName = getName().toString();
        Options options = new Options();
        addOption(options, "s", "sourceDirectory", "Source directory path");
        addOption(options, "d", "destinationDirectory", "Destination directory path");
        addOptionnalOption(options, "t", "dryRun", false, "Print only, no move");
        CommandLine cmd = parse(args, options);
        String sourceDirectory = cmd.getOptionValue("sourceDirectory");
        String destinationDirectory = cmd.getOptionValue("destinationDirectory");
        FileManager fileManager = new FileManager(destinationDirectory, standardizer, cmd.hasOption("dryRun"));
        try {
            fileManager.move(sourceDirectory);
        } catch (Exception e) {
            System.out.println("Can't move file");
            e.printStackTrace();
            new HelpFormatter().printHelp(binName, options);
            System.exit(1);
        }
    }

    public static void addOptionnalOption(Options options, String opt, String longOpt, boolean hasArg, String description) {
        Option option = new Option(opt, longOpt, hasArg, description);
        option.setRequired(hasArg);
        options.addOption(option);
    }

    private static Path getName() throws URISyntaxException {
        Path path = Paths.get(new Main().getClass().getProtectionDomain().getCodeSource().getLocation().toURI());
        return path.getName(path.getNameCount() - 1);
    }

    public static CommandLine parse(String[] args, Options options) {
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

    public static void addOption(Options options, String opt, String longOpt, String description) {
        addOptionnalOption(options, opt, longOpt, true, description);
    }
}
