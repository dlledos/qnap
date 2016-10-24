package standardizer;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import static java.nio.file.StandardCopyOption.REPLACE_EXISTING;

public class Mover {
    private final File destinationFolder;

    public Mover(File destinationFolder) {
        this.destinationFolder = destinationFolder;
    }

    public void move(File source) throws IOException {
        String newDir = Standardizer.getNewDir(source);
        new File(destinationFolder.getPath(), newDir).mkdir();
        File target = Paths.get(destinationFolder.getPath(), newDir, Standardizer.getNewFilename(source)).toFile();
        System.out.println("  moving " +source.getAbsolutePath() + " -> " + target.getAbsolutePath());
        Files.move(source.toPath(), target.toPath(), REPLACE_EXISTING);
    }
}
