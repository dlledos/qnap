package org.nas.tools.standardizer;

import com.beust.jcommander.internal.Lists;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.attribute.BasicFileAttributes;
import java.text.Format;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public abstract class Standardizer {
    public static final String DOT_SEPARATOR = ".";

    protected Pattern pattern;

    public Standardizer(Pattern pattern) {
        this.pattern = pattern;
    }

    public Standardizer() {

    }

    public List<File> findMatchingFile(File sourceDirectory) {
        //return FileUtils.listFiles(sourceDirectory, new NameFileFilter(), TrueFileFilter.INSTANCE);
        List<File> files = Lists.newArrayList(sourceDirectory.listFiles((dir, name) -> name.toLowerCase().matches(pattern.pattern())));
        File[] directory = sourceDirectory.listFiles((pathname) -> pathname.isDirectory());
        for (File file : directory) {
            files.addAll(findMatchingFile(file));
        }
        return files;
    }

    public int move(File sourceDirectory, File destinationDirectory, boolean dryRun) throws IOException {
        Mover mover = new Mover(sourceDirectory, destinationDirectory, this, dryRun);
        int movedFileCount = 0;
        List<File> matchingFile = findMatchingFile(sourceDirectory);
        if (matchingFile != null)
            for (File file : matchingFile) {
                movedFileCount++;
                String format = "%0" + String.valueOf(matchingFile.size()).length() +"d";
                System.out.format(format, movedFileCount).print(" / " +matchingFile.size() + " ");
                try {
                    mover.move(file);
                } catch (Exception e) {
                    System.out.println("Fail to move file : " + file);
                    e.printStackTrace();
                }
            }
        return movedFileCount;
    }

    public int moveLoop(File sourceDirectory, File destinationDirectory, boolean dryRun) throws IOException {
        int movedFileCount = 0;
        for (Pattern pattern : getPatterns()) {
            this.pattern = pattern;
            try {
                movedFileCount += move(sourceDirectory, destinationDirectory, dryRun);
            } catch (Exception e) {
                System.out.println("Fail to use pattern : " + pattern);
                e.printStackTrace();
            }
        }
        return movedFileCount;
    }

    public abstract List<Pattern> getPatterns();

    public abstract String getNewFilename(File file);

    public abstract String getNewDir(File file);

    protected String find(String file, int position) {
        Matcher matcher = pattern.matcher(file);
        if (matcher.find()) {
            return matcher.group(position);
        }
        return "??";
    }

    protected String standardize(String file) {
        file = replaceNonAlphanumCharWithDot(file);
        file = removeDuplicateDot(file);
        file = removeFirstCharNonAlphaNumeric(file);
        return file;
    }

    protected String removeDuplicateDot(String file) {
        String result = new String();
        boolean dotAlreadyFound = false;
        for (int i = 0; i < file.length(); i++) {
            char charFound = file.charAt(i);
            if (charFound == DOT_SEPARATOR.charAt(0)) {
                if (!dotAlreadyFound) {
                    result += charFound;
                }
                dotAlreadyFound = true;
            } else {
                result += charFound;
                dotAlreadyFound = false;
            }
        }
        return result;
    }

    protected String replaceNonAlphanumCharWithDot(String file) {
        return file.replaceAll("[^\\p{Alnum}]", DOT_SEPARATOR);
    }

    protected String removeFirstCharNonAlphaNumeric(String file) {
        if (!file.substring(0, 1).matches("\\p{Alnum}")) {
            file = file.substring(1);
        }
        return file;
    }
}
