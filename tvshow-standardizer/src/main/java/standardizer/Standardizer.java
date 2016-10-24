package standardizer;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Standardizer {

    public static final String SEASON_SEPARATOR = "S";
    public static final String EPISODE_SEPARATOR = "E";
    public static final String DOT_SEPARATOR = ".";
    private static final int EXTENSION_POSITION = 5;
    public static final int TITLE_POSITION = 4;
    public static final int EPISODE_POSITION = 3;
    public static final int SEASON_POSITION = 2;
    public static final int NAME_POSITION = 1;
    private final Pattern pattern = Pattern.compile("(.*?)" + DOT_SEPARATOR + SEASON_SEPARATOR+ "([0-9])+" + EPISODE_SEPARATOR + "([0-9])+" +DOT_SEPARATOR+"(.*)"+DOT_SEPARATOR+"(mp4|avi|mkv)");

    public String transform(String file) {
        String newFilename = findFilename(file);
        return findTvshowName(newFilename) + "/" + newFilename;
    }

    private String findFilename(String file) {
        file = removeFirstCharNonAlphaNumeric(file);
        file = replaceNonAlphanumCharWithDot(file);
        file = removeDuplicateDot(file);
        file = findTvshowName(file) + DOT_SEPARATOR
                + SEASON_SEPARATOR + findSeasonNumber(file)
                + EPISODE_SEPARATOR + findEpisodeNumber(file)
                + DOT_SEPARATOR + findTitle(file)
                + DOT_SEPARATOR + findExtension(file);
        return file;
    }

    private String findExtension(String file) {
        return find(file, EXTENSION_POSITION);
    }

    private String find(String file, int position) {
        Matcher matcher = pattern.matcher(file);
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

    private String findTvshowName(String file) {
        return find(file, NAME_POSITION);
    }

    private String removeDuplicateDot(String file) {
        String result = new String();
        boolean dotAlreadyFound = false;
        for (int i = 0; i < file.length(); i++){
            char charFound = file.charAt(i);
            if (charFound == DOT_SEPARATOR.charAt(0)){
                if (!dotAlreadyFound){
                    result+= charFound;
                }
                dotAlreadyFound = true;
            }
            else {
                result+= charFound;
                dotAlreadyFound = false;
            }
        }
        return result;
    }

    private String replaceNonAlphanumCharWithDot(String file) {
        return file.replaceAll("[^\\p{Alnum}]", DOT_SEPARATOR);
    }

    private String removeFirstCharNonAlphaNumeric(String file) {
        if (! file.substring(0,1).matches("\\p{Alnum}")){
            file = file.substring(1);
        }
        return file;
    }

    public String getNewDir(String file) {
        return findTvshowName(file);
    }
}
