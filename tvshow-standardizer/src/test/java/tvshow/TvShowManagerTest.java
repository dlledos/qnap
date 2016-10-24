package tvshow;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;

import java.io.File;
import java.io.IOException;

import static org.fest.assertions.Assertions.assertThat;


public class TvShowManagerTest {


    private TemporaryFolder temporaryFolder;
    private File destinationDirectory;
    private File sourceDirectory;

    @Before
    public void setUp() throws Exception {
        temporaryFolder = new TemporaryFolder();
        temporaryFolder.create();
        destinationDirectory = temporaryFolder.newFolder();
        sourceDirectory = temporaryFolder.newFolder();
    }

    @After
    public void tearDown() throws Exception {
        temporaryFolder.delete();
    }

    @Test
    public void canMove() throws Exception {
        newFile("test.S01E01.truc.mkv");

        new TvShowManager(destinationDirectory).move(sourceDirectory);

        assertThat(destinationDirectory.listFiles()).hasSize(1);
        assertThat(destinationDirectory.listFiles()[0].getName()).isEqualTo("test");
        assertThat(destinationDirectory.listFiles()[0].listFiles()).hasSize(1);
        assertThat(destinationDirectory.listFiles()[0].listFiles()[0].getName()).isEqualTo("test.S01E01.truc.mkv");
    }

    @Test
    public void canMoveVideoFileOnly() throws Exception {
        newFile("test.S01E01.truc.mkv");
        newFile("rien.a.voir.boby");

        new TvShowManager(destinationDirectory).move(sourceDirectory);

        assertThat(destinationDirectory.listFiles()).hasSize(1);
        assertThat(destinationDirectory.listFiles()[0].getName()).isEqualTo("test");
        assertThat(destinationDirectory.listFiles()[0].listFiles()).hasSize(1);
        assertThat(destinationDirectory.listFiles()[0].listFiles()[0].getName()).isEqualTo("test.S01E01.truc.mkv");
    }

    @Test
    public void canMoveTvShownFileOnly() throws Exception {
        newFile("test.S01E01.truc.mkv");
        newFile("rien.a.voir.boby.mkv");

        new TvShowManager(destinationDirectory).move(sourceDirectory);

        assertThat(destinationDirectory.listFiles()).hasSize(1);
        assertThat(destinationDirectory.listFiles()[0].getName()).isEqualTo("test");
        assertThat(destinationDirectory.listFiles()[0].listFiles()).hasSize(1);
        assertThat(destinationDirectory.listFiles()[0].listFiles()[0].getName()).isEqualTo("test.S01E01.truc.mkv");
    }

    private void newFile(String filename) throws IOException {
        File newFile = new File(sourceDirectory.getAbsolutePath(), filename);
        newFile.createNewFile();
    }
}