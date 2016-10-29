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
        File sourceFile = newFile(sourceFolder, "machin.S00E00.truc.avi");
        assertThat(sourceFile).exists();

        new Mover(destinationFolder, standardizer).move(sourceFile);

        File expectedFile = Paths.get(destinationFolder.getPath(), "machin", "machin.S00E00.truc.avi").toFile();
        assertThat(expectedFile).exists();
    }

    @Test
    public void moveAndStandardizeFile() throws IOException {
        File sourceFile = newFile(sourceFolder, "..machin S00E00 truc....avi");
        assertThat(sourceFile).exists();

        new Mover(destinationFolder, standardizer).move(sourceFile);

        File expectedFile = Paths.get(destinationFolder.getPath(), "machin", "machin.S00E00.truc.avi").toFile();
        assertThat(expectedFile).exists();
    }

    @Test
    public void changeNameIfFileAlreadyExist() throws Exception {
        String filename = "machin.S00E00.truc.avi";
        File sourceFile = newFile(sourceFolder, filename);
        newFile(destinationFolder, Paths.get(standardizer.getNewDir(sourceFile), filename).toString());
        newFile(destinationFolder, Paths.get(standardizer.getNewDir(sourceFile), "machin.S00E00.truc.copy1.avi").toString());
        newFile(destinationFolder, Paths.get(standardizer.getNewDir(sourceFile), "machin.S00E00.truc.copy2.avi").toString());
        new Mover(destinationFolder, standardizer).move(sourceFile);

        assertThat(Paths.get(destinationFolder.getPath(), "machin", "machin.S00E00.truc.avi").toFile()).exists();
        assertThat(Paths.get(destinationFolder.getPath(), "machin", "machin.S00E00.truc.copy1.avi").toFile()).exists();
        assertThat(Paths.get(destinationFolder.getPath(), "machin", "machin.S00E00.truc.copy2.avi").toFile()).exists();
        assertThat(Paths.get(destinationFolder.getPath(), "machin", "machin.S00E00.truc.copy3.avi").toFile()).exists();
    }

    private File newFile(File folder, String filename) throws IOException {
        File sourceFile = Paths.get(folder.getPath(), filename).toFile();
        sourceFile.mkdirs();
        sourceFile.createNewFile();
        return sourceFile;
    }
}
