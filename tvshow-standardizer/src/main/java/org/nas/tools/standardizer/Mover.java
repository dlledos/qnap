package org.nas.tools.standardizer;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Mover {
    private final File destinationFolder;
    private Standardizer standardizer;

    public Mover(File destinationFolder, Standardizer standardizer) {
        this.destinationFolder = destinationFolder;
        this.standardizer = standardizer;
    }

    public void move(File source) throws IOException {
        String newDir = standardizer.getNewDir(source);
        new File(destinationFolder.getPath(), newDir).mkdir();
        File target = chooseTarget(source, newDir);
        System.out.println("  moving " + source.getAbsolutePath() + " -> " + target.getAbsolutePath());
        Files.move(source.toPath(), target.toPath(), StandardCopyOption.REPLACE_EXISTING);
    }

    private File chooseTarget(File source, String newDir) {
        return chooseTarget(newDir, standardizer.getNewFilename(source));
    }

    private File chooseTarget(String newDir, String newFilename) {
        File file = Paths.get(destinationFolder.getPath(), newDir, newFilename).toFile();
        if (file.exists()){
            Pattern pattern = Pattern.compile("(.*)copy([0-9]+)\\..*");
            Matcher matcher = pattern.matcher(file.getName());
            int number =1;
            String filenameBegin;
            if (matcher.find()) {
                filenameBegin = matcher.group(1);
                number = Integer.valueOf(matcher.group(2)) + 1;
            }
            else {
                filenameBegin = newFilename.substring(0, newFilename.lastIndexOf("."))+".";
            }
            String newFilenameCopy = filenameBegin +"copy"+ String.valueOf(number)+newFilename.substring(newFilename.lastIndexOf("."));
            return chooseTarget(newDir, newFilenameCopy);
        }
        return file;
    }
}
