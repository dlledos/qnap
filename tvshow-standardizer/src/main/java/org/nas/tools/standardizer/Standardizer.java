package org.nas.tools.standardizer;

import java.io.File;
import java.io.IOException;
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

    public File[] findMatchingFile(File sourceDirectory) {
        return sourceDirectory.listFiles((dir, name) -> name.matches(pattern.pattern()));
    }

    public int move(File sourceDirectory, File destinationDirectory) throws IOException {
        Mover mover = new Mover(destinationDirectory, this);
        int movedFileCount = 0;
        for (File file : findMatchingFile(sourceDirectory)) {
            mover.move(file);
            movedFileCount++;
        }
        return movedFileCount;
    }

    public int moveLoop(File sourceDirectory, File destinationDirectory) throws IOException {
        int movedFileCount = 0;
        for (Pattern pattern : getPatterns()) {
            this.pattern = pattern;
            movedFileCount += move(sourceDirectory, destinationDirectory);
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
