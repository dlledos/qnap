package org.nas.tools.tvshow;
import org.apache.commons.cli.*;
import org.nas.tools.standardizer.FileManager;

import java.net.URISyntaxException;
import java.nio.file.Path;
import java.nio.file.Paths;

public class Main {

    public static void main(String[] args) throws Exception {
        Options options = new Options();
        addOption(options, "s", "sourceDirectory", "Source directory path");
        addOption(options, "d", "destinationDirectory", "Destination directory path");
        CommandLine cmd = parse(args, options);
        String sourceDirectory = cmd.getOptionValue("sourceDirectory");
        String destinationDirectory = cmd.getOptionValue("destinationDirectory");

        FileManager fileManager = new FileManager(destinationDirectory, new TvShowStandardizer());
        try {
            getName(fileManager);
            fileManager.move(sourceDirectory);
        }
        catch (Exception e){
            System.out.println("Can't move file " + e);

            String urlCourante = fileManager.getClass().getProtectionDomain().getCodeSource().getLocation().getFile();

            new HelpFormatter().printHelp("tvshowManager", options);
            System.exit(1);
        }
    }

    private static Path getName(FileManager fileManager) throws URISyntaxException {
        Path path = Paths.get(fileManager.getClass().getProtectionDomain().getCodeSource().getLocation().toURI());
        return path.getName(path.getNameCount()-1);
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
