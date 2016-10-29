package standardizer;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;

import java.io.File;
import java.io.IOException;

import static org.fest.assertions.Assertions.assertThat;

public class StandardizerTest {


    private File sourceDirectory;
    private TemporaryFolder temporaryFolder;

    @Before
    public void setUp() throws Exception {
        temporaryFolder = new TemporaryFolder();
        temporaryFolder.create();
        sourceDirectory = temporaryFolder.newFolder();
    }

    @After
    public void tearDown() throws Exception {
        temporaryFolder.delete();
    }

    @Test
    public void nominal() throws Exception {
        File file = new File("machin.S01E01.truc.avi");
        Standardizer standardizer = new Standardizer(Standardizer.TvShownPattern.S0XE0X);
        assertThat(standardizer.getNewFilename(file)).isEqualTo("machin.S01E01.truc.avi");
        assertThat(standardizer.getNewDir(file)).isEqualTo("machin");
    }

    @Test
    public void espaceDansFichier() throws Exception {
        File file = new File("machin S01E01 truc avi");
        Standardizer standardizer = new Standardizer(Standardizer.TvShownPattern.S0XE0X);
        assertThat(standardizer.getNewFilename(file)).isEqualTo("machin.S01E01.truc.avi");
        assertThat(standardizer.getNewDir(file)).isEqualTo("machin");
    }

    @Test
    public void commenceAvecUnPoint() throws Exception {
        File file = new File(".machin S01E01 truc avi");
        Standardizer standardizer = new Standardizer(Standardizer.TvShownPattern.S0XE0X);
        assertThat(standardizer.getNewFilename(file)).isEqualTo("machin.S01E01.truc.avi");
        assertThat(standardizer.getNewDir(file)).isEqualTo("machin");
    }


    @Test
    public void commenceAvecUnTiret() throws Exception {
        File file = new File("-machin-S01E01-truc.avi");
        Standardizer standardizer = new Standardizer(Standardizer.TvShownPattern.S0XE0X);
        assertThat(standardizer.getNewFilename(file)).isEqualTo("machin.S01E01.truc.avi");
        assertThat(standardizer.getNewDir(file)).isEqualTo("machin");
    }

    @Test
    public void removeDuplicatePoint() throws Exception {
        File file = new File("machin..S01E01........truc...avi");
        Standardizer standardizer = new Standardizer(Standardizer.TvShownPattern.S0XE0X);
        assertThat(standardizer.getNewFilename(file)).isEqualTo("machin.S01E01.truc.avi");
        assertThat(standardizer.getNewDir(file)).isEqualTo("machin");
    }

    @Test
    public void nombreSur2Chiffres() throws Exception {
        File file = new File("-machin S1E1 truc avi");
        Standardizer standardizer = new Standardizer(Standardizer.TvShownPattern.S0XE0X);
        assertThat(standardizer.getNewFilename(file)).isEqualTo("machin.S01E01.truc.avi");
        assertThat(standardizer.getNewDir(file)).isEqualTo("machin");
    }

    @Test
    public void seDansLeTexteResteSe() throws Exception {
        File file = new File("machin.S01E01.se.avi");
        Standardizer standardizer = new Standardizer(Standardizer.TvShownPattern.S0XE0X);
        assertThat(standardizer.getNewFilename(file)).isEqualTo("machin.S01E01.se.avi");
        assertThat(standardizer.getNewDir(file)).isEqualTo("machin");
    }

    @Test
    public void parenthèseAprèsSxxExx() throws Exception {
        File file = new File("machin.S02E03(1).truc.mp4");
        Standardizer standardizer = new Standardizer(Standardizer.TvShownPattern.S0XE0X);
        assertThat(standardizer.getNewFilename(file)).isEqualTo("machin.S02E03.1.truc.mp4");
        assertThat(standardizer.getNewDir(file)).isEqualTo("machin");
    }

    @Test
    public void deuxFoisSxxExxPrends1er() throws Exception {
        File file = new File("machin.S02E03.truc.S01E48.bidule.mp4");
        Standardizer standardizer = new Standardizer(Standardizer.TvShownPattern.S0XE0X);
        assertThat(standardizer.getNewFilename(file)).isEqualTo("machin.S02E03.truc.S01E48.bidule.mp4");
        assertThat(standardizer.getNewDir(file)).isEqualTo("machin");
    }

    @Test
    public void deuxFoisPointAuDebut() throws Exception {
        File file = new File(".....machin.S01E01.truc.avi");
        Standardizer standardizer = new Standardizer(Standardizer.TvShownPattern.S0XE0X);
        assertThat(standardizer.getNewFilename(file)).isEqualTo("machin.S01E01.truc.avi");
        assertThat(standardizer.getNewDir(file)).isEqualTo("machin");
    }

    @Test
    public void episodeSepratedWithDot() throws Exception {
        File file = new File("machin.S01.E01.truc.avi");
        Standardizer standardizer = new Standardizer(Standardizer.TvShownPattern.S0XE0X);
        assertThat(standardizer.getNewFilename(file)).isEqualTo("machin.S01E01.truc.avi");
        assertThat(standardizer.getNewDir(file)).isEqualTo("machin");
    }
    @Test
    public void multiEpisode() throws Exception {
        File file = new File("machin.S01E01-E02.truc.avi");
        Standardizer standardizer = new Standardizer(Standardizer.TvShownPattern.S0XE0X);
        assertThat(standardizer.getNewFilename(file)).isEqualTo("machin.S01E01.E02.truc.avi");
        assertThat(standardizer.getNewDir(file)).isEqualTo("machin");
    }
    @Test
    public void episodeSepratedWithX() throws Exception {
        File file = new File("machin.1x01.truc.avi");
        Standardizer standardizer = new Standardizer(Standardizer.TvShownPattern._1x01);
        assertThat(standardizer.getNewFilename(file)).isEqualTo("machin.S01E01.truc.avi");
        assertThat(standardizer.getNewDir(file)).isEqualTo("machin");
    }

    @Test
    public void episodeLike101() throws Exception {
        File file = new File("machin.0203.truc.avi");
        Standardizer standardizer = new Standardizer(Standardizer.TvShownPattern._101);
        assertThat(standardizer.getNewFilename(file)).isEqualTo("machin.S02E03.truc.avi");
        assertThat(standardizer.getNewDir(file)).isEqualTo("machin");
    }

    private void newFile(String filename) throws IOException {
        File newFile = new File(sourceDirectory.getAbsolutePath(), filename);
        newFile.createNewFile();
    }
    @Test
    public void name() throws Exception {
        newFile("machin.1x01.truc.avi");
        newFile("machin.0203.truc.avi");
        Standardizer standardizer = new Standardizer(Standardizer.TvShownPattern._1x01);

        File[] matchingFile = standardizer.findMatchingFile(sourceDirectory);

        assertThat(matchingFile).hasSize(1);

    }
}
