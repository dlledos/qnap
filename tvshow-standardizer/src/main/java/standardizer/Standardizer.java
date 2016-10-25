package standardizer;

import java.io.File;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Standardizer {

    private static final String SEASON_SEPARATOR = "S";
    private static final String EPISODE_SEPARATOR = "E";
    private static final String DOT_SEPARATOR = ".";
    private static final int EXTENSION_POSITION = 5;
    private static final int TITLE_POSITION = 4;
    private static final int EPISODE_POSITION = 3;
    private static final int SEASON_POSITION = 2;
    private static final int NAME_POSITION = 1;
    public static final Pattern TVSHOW_S0XE0X = Pattern.compile("(.*?)" + DOT_SEPARATOR + SEASON_SEPARATOR+ "([0-9])+" + EPISODE_SEPARATOR + "([0-9])+" +DOT_SEPARATOR+"(.*)"+DOT_SEPARATOR+"(mp4|avi|mkv)");
    public static final Pattern TVSHOW_ = Pattern.compile("(.*?)" + DOT_SEPARATOR + SEASON_SEPARATOR+ "([0-9])+" + EPISODE_SEPARATOR + "([0-9])+" +DOT_SEPARATOR+"(.*)"+DOT_SEPARATOR+"(mp4|avi|mkv)");
/*
        anything_s01e02.ext (1)
                anything_s1e2.ext (1)
                anything_s01.e02.ext (1)
                anything_s01_e02.ext (1)
                anything_1x02.ext (5)
                anything_102.ext (6)
*/

/*
    Heroes - s02e05-e08.mkv
 */
    public static String getNewFilename(File file) {
        String newFilename = standardize(file.getName());
        newFilename = findTvshowName(newFilename) + DOT_SEPARATOR
                + SEASON_SEPARATOR + findSeasonNumber(newFilename)
                + EPISODE_SEPARATOR + findEpisodeNumber(newFilename)
                + DOT_SEPARATOR + findTitle(newFilename)
                + DOT_SEPARATOR + findExtension(newFilename);
        return newFilename;
    }

    public static String getNewDir(File file) {
        return findTvshowName(file.getName());
    }

    private static String findTvshowName(String file) {
        file = standardize(file);
        return find(file, NAME_POSITION);
    }

    private static String standardize(String file) {
        file = replaceNonAlphanumCharWithDot(file);
        file = removeDuplicateDot(file);
        file = removeFirstCharNonAlphaNumeric(file);
        return file;
    }

    private static String findExtension(String file) {
        return find(file, EXTENSION_POSITION);
    }

    private static String find(String file, int position) {
        Matcher matcher = TVSHOW_S0XE0X.matcher(file);
        if (matcher.find()) {
            return matcher.group(position);
        }
        return "??";
    }

    private static String findTitle(String file) {
        return find(file, TITLE_POSITION);
    }

    private static String findEpisodeNumber(String file) {
        return String.format("%02d", Integer.valueOf(find(file, EPISODE_POSITION)));
    }

    private static String findSeasonNumber(String file) {
        return String.format("%02d", Integer.valueOf(find(file, SEASON_POSITION)));
    }

    private static String removeDuplicateDot(String file) {
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

    private static String replaceNonAlphanumCharWithDot(String file) {
        return file.replaceAll("[^\\p{Alnum}]", DOT_SEPARATOR);
    }

    private static String removeFirstCharNonAlphaNumeric(String file) {
        if (! file.substring(0,1).matches("\\p{Alnum}")){
            file = file.substring(1);
        }
        return file;
    }
}
