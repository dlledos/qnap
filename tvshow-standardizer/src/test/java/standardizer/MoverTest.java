package standardizer;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;

import static org.fest.assertions.Assertions.assertThat;

public class MoverTest {

    @Rule
    public TemporaryFolder temporaryFolder = new TemporaryFolder();
    private File destinationFolder;
    private File sourceFolder;
    private Standardizer standardizer;


    @Before
    public void before() throws IOException {
        destinationFolder = temporaryFolder.newFolder();
        sourceFolder = temporaryFolder.newFolder();
        standardizer = new Standardizer(Standardizer.TvShownPattern.S0XE0X);
    }

    @After
    public void after() {
        destinationFolder.delete();
        sourceFolder.delete();
    }

    @Test
    public void move() throws IOException {
        File sourceFile = Paths.get(sourceFolder.getPath(), "machin.S00E00.truc.avi").toFile();
        sourceFile.createNewFile();
        assertThat(sourceFile).exists();

        new Mover(destinationFolder, standardizer).move(sourceFile);

        File expectedFile = Paths.get(destinationFolder.getPath(), "machin", "machin.S00E00.truc.avi").toFile();
        assertThat(expectedFile).exists();
    }

    @Test
    public void moveAndStandardizeFile() throws IOException {
        File sourceFile = Paths.get(sourceFolder.getPath(), "..machin S00E00 truc....avi").toFile();
        sourceFile.createNewFile();
        assertThat(sourceFile).exists();

        new Mover(destinationFolder, standardizer).move(sourceFile);

        File expectedFile = Paths.get(destinationFolder.getPath(), "machin", "machin.S00E00.truc.avi").toFile();
        assertThat(expectedFile).exists();
    }

    @Test
    public void userDir() {
        Path currentPath = Paths.get(System.getProperty("user.dir"));
        Path filePath = Paths.get(currentPath.toString(), "data", "foo.txt");
        System.out.println(filePath.toString());
    }

    @Test
    public void currentDir() {
        Path filePath = Paths.get(".", "data", "foo.txt");
        System.out.println(filePath.toString());
    }
}
