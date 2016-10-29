package standardizer;

import java.io.File;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Standardizer {

    private static final String SEASON_SEPARATOR = "S";
    private static final String EPISODE_SEPARATOR = "E";
    private static final String EPISODE_SEPARATOR_X = "(?:x|X)";
    private static final String DOT_SEPARATOR = ".";
    private static final int EXTENSION_POSITION = 5;
    private static final int TITLE_POSITION = 4;
    private static final int EPISODE_POSITION = 3;
    private static final int SEASON_POSITION = 2;
    private static final int NAME_POSITION = 1;

    public enum TvShownPattern {
        S0XE0X("(.*?)" + DOT_SEPARATOR
                + SEASON_SEPARATOR + "([0-9])+(?:" + EPISODE_SEPARATOR + "|" + DOT_SEPARATOR + EPISODE_SEPARATOR + ")?([0-9])+"
                + DOT_SEPARATOR + "(.*)" + DOT_SEPARATOR + "(mp4|avi|mkv)"),
        _1x01("(.*?)" + DOT_SEPARATOR
                + "([0-9])+" + EPISODE_SEPARATOR_X + "([0-9])+"
                + DOT_SEPARATOR + "(.*)" + DOT_SEPARATOR + "(mp4|avi|mkv)"),
        _101("(.*?)" + DOT_SEPARATOR
                + "([0-9])+" + "([0-9][0-9])"
                + DOT_SEPARATOR + "(.*)" + DOT_SEPARATOR + "(mp4|avi|mkv)");
        public Pattern pattern;

        TvShownPattern(String pattern) {
            this.pattern = Pattern.compile(pattern);
        }

    }

    ;
    private TvShownPattern tvShownPattern;

    public Standardizer(TvShownPattern tvShownPattern) {
        this.tvShownPattern = tvShownPattern;
    }

    public String getNewFilename(File file) {
        String newFilename = standardize(file.getName());
        newFilename = findTvshowName(newFilename) + DOT_SEPARATOR
                + SEASON_SEPARATOR + findSeasonNumber(newFilename)
                + EPISODE_SEPARATOR + findEpisodeNumber(newFilename)
                + DOT_SEPARATOR + findTitle(newFilename)
                + DOT_SEPARATOR + findExtension(newFilename);
        return newFilename;
    }

    public String getNewDir(File file) {
        return findTvshowName(file.getName());
    }

    private String findTvshowName(String file) {
        file = standardize(file);
        return find(file, NAME_POSITION);
    }

    private String standardize(String file) {
        file = replaceNonAlphanumCharWithDot(file);
        file = removeDuplicateDot(file);
        file = removeFirstCharNonAlphaNumeric(file);
        return file;
    }

    private String findExtension(String file) {
        return find(file, EXTENSION_POSITION);
    }

    private String find(String file, int position) {
        Matcher matcher = tvShownPattern.pattern.matcher(file);
        if (matcher.find()) {
            return matcher.group(position);
        }
        return "??";
    }

    private String findTitle(String file) {
        return find(file, TITLE_POSITION);
    }

    private String findEpisodeNumber(String file) {
        return String.format("%02d", Integer.valueOf(find(file, EPISODE_POSITION)));
    }

    private String findSeasonNumber(String file) {
        return String.format("%02d", Integer.valueOf(find(file, SEASON_POSITION)));
    }

    private String removeDuplicateDot(String file) {
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

    private String replaceNonAlphanumCharWithDot(String file) {
        return file.replaceAll("[^\\p{Alnum}]", DOT_SEPARATOR);
    }

    private String removeFirstCharNonAlphaNumeric(String file) {
        if (!file.substring(0, 1).matches("\\p{Alnum}")) {
            file = file.substring(1);
        }
        return file;
    }

    public File[] findMatchingFile(File sourceDirectory) {
        return sourceDirectory.listFiles((dir, name) -> name.matches(tvShownPattern.pattern.pattern()));
    }
}
