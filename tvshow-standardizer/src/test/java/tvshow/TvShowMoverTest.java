package tvshow;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;

import java.io.File;

import static org.fest.assertions.Assertions.assertThat;


public class TvShowMoverTest {


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
        System.out.println(sourceDirectory.getAbsolutePath() + "\\test.S01E01.truc.mkv");
        File newFile = new File(sourceDirectory.getAbsolutePath() + "\\test.S01E01.truc.mkv");
        newFile.createNewFile();

        new TvShowMover(destinationDirectory).move(sourceDirectory);

        assertThat(destinationDirectory.listFiles()).hasSize(1);
        assertThat(destinationDirectory.listFiles()[0].listFiles()).hasSize(1);
        assertThat(destinationDirectory.listFiles()[0].listFiles()[0]).hasSize(1);
        assertThat(new File(destinationDirectory.getAbsolutePath() + "\\test\\test.S01E01.truc.mkv")).exists();
    }
}