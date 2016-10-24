package tvshow;


import standardizer.Mover;
import standardizer.Standardizer;

import java.io.File;
import java.io.IOException;

public class TvShowMover {


    private File destinationDirectory;

    public TvShowMover(File destinationDirectory) {
        this.destinationDirectory = destinationDirectory;
    }

    public void move(File sourceDirectory) throws IOException {
        for (File file : sourceDirectory.listFiles()) {
            new Mover(destinationDirectory).move(file);
        }

    }
}
