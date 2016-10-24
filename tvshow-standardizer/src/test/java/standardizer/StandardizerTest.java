package standardizer;

import org.junit.Test;
import org.junit.rules.TemporaryFolder;
import standardizer.Standardizer;

import java.io.File;

import static org.fest.assertions.Assertions.assertThat;

public class StandardizerTest {


    @Test
    public void nominal() throws Exception {
        String result = new Standardizer().transform("machin.S01E01.truc.avi");

        assertThat(result).isEqualTo("machin/machin.S01E01.truc.avi");
    }

    @Test
    public void espaceDansFichier() throws Exception {
        String result = new Standardizer().transform("machin S01E01 truc avi");

        assertThat(result).isEqualTo("machin/machin.S01E01.truc.avi");
    }

    @Test
    public void commenceAvecUnPoint() throws Exception {
        String result = new Standardizer().transform(".machin S01E01 truc avi");

        assertThat(result).isEqualTo("machin/machin.S01E01.truc.avi");
    }

    @Test
    public void commenceAvecUnTiret() throws Exception {
        String result = new Standardizer().transform("-machin-S01E01-truc.avi");

        assertThat(result).isEqualTo("machin/machin.S01E01.truc.avi");
    }


    @Test
    public void removeDuplicatePoint() throws Exception {
        String result = new Standardizer().transform("machin..S01E01....truc...avi");

        assertThat(result).isEqualTo("machin/machin.S01E01.truc.avi");
    }

    @Test
    public void nombreSur2Chiffres() throws Exception {
        String result = new Standardizer().transform("-machin S1E1 truc avi");

        assertThat(result).isEqualTo("machin/machin.S01E01.truc.avi");
    }

    @Test
    public void seDansLeTexteResteSe() throws Exception {
        String result = new Standardizer().transform("machin.S01E01.se.avi");

        assertThat(result).isEqualTo("machin/machin.S01E01.se.avi");
    }

    @Test
    public void parenthèseAprèsSxxExx() throws Exception {
        String result = new Standardizer().transform("machin.S02E03(1).truc.mp4");

        assertThat(result).isEqualTo("machin/machin.S02E03.1.truc.mp4");
    }

    @Test
    public void deuxFoisSxxExxPrends1er() throws Exception {
        String result = new Standardizer().transform("machin.S02E03.truc.S01E48.bidule.mp4");

        assertThat(result).isEqualTo("machin/machin.S02E03.truc.S01E48.bidule.mp4");
    }

    @Test
    public void getNewDir() throws Exception {
        String newDir = new Standardizer().getNewDir("machin.S02E03.truc.S01E48.bidule.mp4");

        assertThat(newDir).isEqualTo("machin");
    }

    @Test
    public void getNewFile() throws Exception {

        TemporaryFolder temporaryFolder = new TemporaryFolder();
        File newDir = new Standardizer(temporaryFolder.create()).getNewFile("machin.S02E03.truc.S01E48.bidule.mp4");

        assertThat(newDir.getAbsolutePath()).contains("machin\\machin.S02E03.truc.S01E48.bidule.mp4");

        temporaryFolder.delete();
    }
}
