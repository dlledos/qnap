package tvshow;


import standardizer.Mover;
import standardizer.Standardizer;

import java.io.File;
import java.io.IOException;

public class TvShowManager {


    private File destinationDirectory;

    public TvShowManager(File destinationDirectory) {
        this.destinationDirectory = destinationDirectory;
    }

    public void move(File sourceDirectory) throws IOException {
        Mover mover = new Mover(destinationDirectory);
        int movedFileCount = 0;
        System.out.println("Starting scan of " + sourceDirectory.getAbsolutePath());
        for (File file : sourceDirectory.listFiles((dir, name) -> name.matches(Standardizer.TVSHOW.pattern()))) {
            mover.move(file);
        }
        System.out.println("Moved " + movedFileCount + " file from " + sourceDirectory.getAbsolutePath() + " to "  + destinationDirectory.getAbsolutePath());
    }
}
