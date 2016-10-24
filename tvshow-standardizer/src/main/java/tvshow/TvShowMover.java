package tvshow;


import standardizer.Standardizer;

import java.io.File;

public class TvShowMover {


    private File destinationDirectory;

    public TvShowMover(File destinationDirectory) {
        this.destinationDirectory = destinationDirectory;
    }

    public void move(File sourceDirectory) {
        for (File file : sourceDirectory.listFiles()) {
            Standardizer standardizer = new Standardizer(destinationDirectory);
            file.renameTo(new File(standardizer.transform(file.getName())));
        }

    }
}
