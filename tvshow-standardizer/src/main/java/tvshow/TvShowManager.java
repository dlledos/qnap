package tvshow;


import standardizer.Mover;
import java.io.File;
import java.io.IOException;

public class TvShowManager {


    private File destinationDirectory;

    public TvShowManager(File destinationDirectory) {
        this.destinationDirectory = destinationDirectory;
    }

    public void move(File sourceDirectory) throws IOException {
        Mover mover = new Mover(destinationDirectory);
        for (File file : sourceDirectory.listFiles()) {
            mover.move(file);
        }
    }
}
