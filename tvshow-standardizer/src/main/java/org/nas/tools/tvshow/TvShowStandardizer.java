package org.nas.tools.tvshow;

import org.nas.tools.standardizer.Standardizer;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

public class TvShowStandardizer extends Standardizer {

    public enum PatternEnum {
        S0XE0X("(.*?)" + DOT_SEPARATOR
                + SEASON_SEPARATOR + "([0-9])+(?:" + EPISODE_SEPARATOR + "|" + DOT_SEPARATOR + EPISODE_SEPARATOR + ")?([0-9])+"
                + DOT_SEPARATOR + "(.*)" + DOT_SEPARATOR + "(mp4|avi|mkv)"),
        _1x01("(.*?)" + DOT_SEPARATOR
                + "([0-9])+" + EPISODE_SEPARATOR_X + "([0-9])+"
                + DOT_SEPARATOR + "(.*)" + DOT_SEPARATOR + "(mp4|avi|mkv)"),
        _101("(.*?)" + DOT_SEPARATOR
                + "([0-9])+" + "([0-9][0-9])"
                + DOT_SEPARATOR + "(.*)" + DOT_SEPARATOR + "(mp4|avi|mkv)");

        private Pattern pattern;

        PatternEnum(String pattern) {
            this.pattern = Pattern.compile(pattern);
        }

        public Pattern getPattern() {
            return pattern;
        }

        public static List<Pattern> getPatterns() {
            List<Pattern> patternsList = new ArrayList<>();
            for (PatternEnum patternEnum1 : PatternEnum.values()) {
                patternsList.add(patternEnum1.getPattern());
            }
            return patternsList;
        }
    }

    public static final String SEASON_SEPARATOR = "S";
    public static final String EPISODE_SEPARATOR = "E";
    public static final String EPISODE_SEPARATOR_X = "(?:x|X)";
    public static final int EXTENSION_POSITION = 5;
    public static final int TITLE_POSITION = 4;
    public static final int EPISODE_POSITION = 3;
    public static final int SEASON_POSITION = 2;
    public static final int NAME_POSITION = 1;

    public TvShowStandardizer(Pattern pattern) {
        super(pattern);
    }

    public TvShowStandardizer() {
    }

    @Override
    public List<Pattern> getPatterns() {
        return TvShowStandardizer.PatternEnum.getPatterns();
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

    private String findExtension(String file) {
        return find(file, EXTENSION_POSITION);
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
}
