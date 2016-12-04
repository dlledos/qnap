package org.nas.tools.standardizer;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Mover {
    private File sourceFolder;
    private final File destinationFolder;
    private Standardizer standardizer;
    private boolean dryRun;

    public Mover(File sourceFolder, File destinationFolder, Standardizer standardizer, boolean dryRun) {
        this.sourceFolder = sourceFolder;
        this.destinationFolder = destinationFolder;
        this.standardizer = standardizer;
        this.dryRun = dryRun;
    }

    public void move(File source) throws IOException {
        String newDir = Paths.get(getParentDir(source), standardizer.getNewDir(source)).toString();
        File file = new File(destinationFolder.getPath(), newDir);
        file.mkdir();
        File target = chooseTarget(source, newDir);
        System.out.println("  moving " + source.getAbsolutePath() + " -> " + target.getAbsolutePath());
        if (!dryRun) {
            Files.move(source.toPath(), target.toPath(), StandardCopyOption.REPLACE_EXISTING);
        }
    }

    private String getParentDir(File source) {
        String parentDir = ".";
        if (!Paths.get(source.getAbsolutePath()).getParent().toString().equals(sourceFolder.toString())) {
            parentDir = Paths.get(source.getAbsolutePath().substring(sourceFolder.getAbsolutePath().length())).getParent().toString();
        }
        return parentDir;
    }

    private File chooseTarget(File source, String newDir) {
        return chooseTarget(newDir, standardizer.getNewFilename(source));
    }

    private File chooseTarget(String newDir, String newFilename) {
        File file = Paths.get(destinationFolder.getPath(), newDir, newFilename).toFile();
        if (file.exists()) {
            Pattern pattern = Pattern.compile("(.*)copy([0-9]+)\\..*");
            Matcher matcher = pattern.matcher(file.getName());
            int number = 1;
            String filenameBegin;
            if (matcher.find()) {
                filenameBegin = matcher.group(1);
                number = Integer.valueOf(matcher.group(2)) + 1;
            } else {
                filenameBegin = newFilename.substring(0, newFilename.lastIndexOf(".")) + ".";
            }
            String newFilenameCopy = filenameBegin + "copy" + String.valueOf(number) + newFilename.substring(newFilename.lastIndexOf("."));
            return chooseTarget(newDir, newFilenameCopy);
        }
        return file;
    }
}
