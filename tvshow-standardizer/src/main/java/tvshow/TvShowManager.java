package tvshow;


import standardizer.Mover;
import standardizer.Standardizer;

import java.io.File;
import java.io.IOException;
import java.util.regex.Pattern;

public class TvShowManager {


    private File destinationDirectory;

    public TvShowManager(File destinationDirectory) {
        this.destinationDirectory = destinationDirectory;
    }

    public void move(File sourceDirectory) throws IOException {
        System.out.println("Starting scan of " + sourceDirectory.getAbsolutePath());
        int movedFileCount = 0;
        movedFileCount += standardizeTvShow(sourceDirectory, Standardizer.TVSHOW_S0XE0X);
        movedFileCount += standardizeTvShow(sourceDirectory, Standardizer.TVSHOW_1x01);
        movedFileCount += standardizeTvShow(sourceDirectory, Standardizer.TVSHOW_101);
        System.out.println("Moved " + movedFileCount + " file from " + sourceDirectory.getAbsolutePath() + " to "  + destinationDirectory.getAbsolutePath());
    }

    private int standardizeTvShow(File sourceDirectory, Pattern pattern) throws IOException {
        Standardizer standardizer = new Standardizer(pattern);
        Mover mover = new Mover(destinationDirectory, standardizer);
        int movedFileCount = 0;
        for (File file : standardizer.findMatchingFile(sourceDirectory)) {
            mover.move(file);
            movedFileCount++;
        }
        return movedFileCount;
    }

}