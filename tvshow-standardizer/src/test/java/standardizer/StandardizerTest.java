package standardizer;

import org.junit.Test;

import java.io.File;

import java.nio.file.Path;
import java.nio.file.Paths;

import static org.fest.assertions.Assertions.assertThat;

public class StandardizerTest {


    @Test
    public void nominal() throws Exception {
        File file = new File("machin.S01E01.truc.avi");
        assertThat(Standardizer.getNewFilename(file)).isEqualTo("machin.S01E01.truc.avi");
        assertThat(Standardizer.getNewDir(file)).isEqualTo("machin");
    }

    @Test
    public void espaceDansFichier() throws Exception {
        File file = new File("machin S01E01 truc avi");
        assertThat(Standardizer.getNewFilename(file)).isEqualTo("machin.S01E01.truc.avi");
        assertThat(Standardizer.getNewDir(file)).isEqualTo("machin");
    }

    @Test
    public void commenceAvecUnPoint() throws Exception {
        File file = new File(".machin S01E01 truc avi");
        assertThat(Standardizer.getNewFilename(file)).isEqualTo("machin.S01E01.truc.avi");
        assertThat(Standardizer.getNewDir(file)).isEqualTo("machin");
    }


    @Test
    public void commenceAvecUnTiret() throws Exception {
        File file = new File("-machin-S01E01-truc.avi");
        assertThat(Standardizer.getNewFilename(file)).isEqualTo("machin.S01E01.truc.avi");
        assertThat(Standardizer.getNewDir(file)).isEqualTo("machin");
    }

    @Test
    public void removeDuplicatePoint() throws Exception {
        File file = new File("machin..S01E01........truc...avi");
        assertThat(Standardizer.getNewFilename(file)).isEqualTo("machin.S01E01.truc.avi");
        assertThat(Standardizer.getNewDir(file)).isEqualTo("machin");
    }

    @Test
    public void nombreSur2Chiffres() throws Exception {
        File file = new File("-machin S1E1 truc avi");
        assertThat(Standardizer.getNewFilename(file)).isEqualTo("machin.S01E01.truc.avi");
        assertThat(Standardizer.getNewDir(file)).isEqualTo("machin");
    }

    @Test
    public void seDansLeTexteResteSe() throws Exception {
        File file = new File("machin.S01E01.se.avi");
        assertThat(Standardizer.getNewFilename(file)).isEqualTo("machin.S01E01.se.avi");
        assertThat(Standardizer.getNewDir(file)).isEqualTo("machin");
    }

    @Test
    public void parenthèseAprèsSxxExx() throws Exception {
        File file = new File("machin.S02E03(1).truc.mp4");
        assertThat(Standardizer.getNewFilename(file)).isEqualTo("machin.S02E03.1.truc.mp4");
        assertThat(Standardizer.getNewDir(file)).isEqualTo("machin");
    }

    @Test
    public void deuxFoisSxxExxPrends1er() throws Exception {
        File file = new File("machin.S02E03.truc.S01E48.bidule.mp4");
        assertThat(Standardizer.getNewFilename(file)).isEqualTo("machin.S02E03.truc.S01E48.bidule.mp4");
        assertThat(Standardizer.getNewDir(file)).isEqualTo("machin");
    }

    @Test
    public void deuxFoisPointAuDebut() throws Exception {
        File file = new File(".....machin.S01E01.truc.avi");
        assertThat(Standardizer.getNewFilename(file)).isEqualTo("machin.S01E01.truc.avi");
        assertThat(Standardizer.getNewDir(file)).isEqualTo("machin");
    }
/*
                anything_s01.e02.ext (1)
                anything_s01_e02.ext (1)
                anything_1x02.ext (5)
                anything_102.ext (6)
 */
    @Test
    public void episodeSepratedWithDot() throws Exception {
        File file = new File("machin.S01.E01.truc.avi");
        assertThat(Standardizer.getNewFilename(file)).isEqualTo("machin.S01E01.truc.avi");
        assertThat(Standardizer.getNewDir(file)).isEqualTo("machin");
    }
}
