package standardizer;

import org.junit.Test;

import java.io.File;
import java.util.regex.Matcher;

import static org.fest.assertions.Assertions.assertThat;

public class StandardizerTest {


    @Test
    public void nominal() throws Exception {
        File file = new File("machin.S01E01.truc.avi");
        Standardizer standardizer = new Standardizer(Standardizer.TVSHOW_S0XE0X);
        assertThat(standardizer.getNewFilename(file)).isEqualTo("machin.S01E01.truc.avi");
        assertThat(standardizer.getNewDir(file)).isEqualTo("machin");
    }

    @Test
    public void espaceDansFichier() throws Exception {
        File file = new File("machin S01E01 truc avi");
        Standardizer standardizer = new Standardizer(Standardizer.TVSHOW_S0XE0X);
        assertThat(standardizer.getNewFilename(file)).isEqualTo("machin.S01E01.truc.avi");
        assertThat(standardizer.getNewDir(file)).isEqualTo("machin");
    }

    @Test
    public void commenceAvecUnPoint() throws Exception {
        File file = new File(".machin S01E01 truc avi");
        Standardizer standardizer = new Standardizer(Standardizer.TVSHOW_S0XE0X);
        assertThat(standardizer.getNewFilename(file)).isEqualTo("machin.S01E01.truc.avi");
        assertThat(standardizer.getNewDir(file)).isEqualTo("machin");
    }


    @Test
    public void commenceAvecUnTiret() throws Exception {
        File file = new File("-machin-S01E01-truc.avi");
        Standardizer standardizer = new Standardizer(Standardizer.TVSHOW_S0XE0X);
        assertThat(standardizer.getNewFilename(file)).isEqualTo("machin.S01E01.truc.avi");
        assertThat(standardizer.getNewDir(file)).isEqualTo("machin");
    }

    @Test
    public void removeDuplicatePoint() throws Exception {
        File file = new File("machin..S01E01........truc...avi");
        Standardizer standardizer = new Standardizer(Standardizer.TVSHOW_S0XE0X);
        assertThat(standardizer.getNewFilename(file)).isEqualTo("machin.S01E01.truc.avi");
        assertThat(standardizer.getNewDir(file)).isEqualTo("machin");
    }

    @Test
    public void nombreSur2Chiffres() throws Exception {
        File file = new File("-machin S1E1 truc avi");
        Standardizer standardizer = new Standardizer(Standardizer.TVSHOW_S0XE0X);
        assertThat(standardizer.getNewFilename(file)).isEqualTo("machin.S01E01.truc.avi");
        assertThat(standardizer.getNewDir(file)).isEqualTo("machin");
    }

    @Test
    public void seDansLeTexteResteSe() throws Exception {
        File file = new File("machin.S01E01.se.avi");
        Standardizer standardizer = new Standardizer(Standardizer.TVSHOW_S0XE0X);
        assertThat(standardizer.getNewFilename(file)).isEqualTo("machin.S01E01.se.avi");
        assertThat(standardizer.getNewDir(file)).isEqualTo("machin");
    }

    @Test
    public void parenthèseAprèsSxxExx() throws Exception {
        File file = new File("machin.S02E03(1).truc.mp4");
        Standardizer standardizer = new Standardizer(Standardizer.TVSHOW_S0XE0X);
        assertThat(standardizer.getNewFilename(file)).isEqualTo("machin.S02E03.1.truc.mp4");
        assertThat(standardizer.getNewDir(file)).isEqualTo("machin");
    }

    @Test
    public void deuxFoisSxxExxPrends1er() throws Exception {
        File file = new File("machin.S02E03.truc.S01E48.bidule.mp4");
        Standardizer standardizer = new Standardizer(Standardizer.TVSHOW_S0XE0X);
        assertThat(standardizer.getNewFilename(file)).isEqualTo("machin.S02E03.truc.S01E48.bidule.mp4");
        assertThat(standardizer.getNewDir(file)).isEqualTo("machin");
    }

    @Test
    public void deuxFoisPointAuDebut() throws Exception {
        File file = new File(".....machin.S01E01.truc.avi");
        Standardizer standardizer = new Standardizer(Standardizer.TVSHOW_S0XE0X);
        assertThat(standardizer.getNewFilename(file)).isEqualTo("machin.S01E01.truc.avi");
        assertThat(standardizer.getNewDir(file)).isEqualTo("machin");
    }

    @Test
    public void episodeSepratedWithDot() throws Exception {
        File file = new File("machin.S01.E01.truc.avi");
        Standardizer standardizer = new Standardizer(Standardizer.TVSHOW_S0XE0X);
        assertThat(standardizer.getNewFilename(file)).isEqualTo("machin.S01E01.truc.avi");
        assertThat(standardizer.getNewDir(file)).isEqualTo("machin");
    }
    @Test
    public void multiEpisode() throws Exception {
        File file = new File("machin.S01E01-E02.truc.avi");
        Standardizer standardizer = new Standardizer(Standardizer.TVSHOW_S0XE0X);
        assertThat(standardizer.getNewFilename(file)).isEqualTo("machin.S01E01.E02.truc.avi");
        assertThat(standardizer.getNewDir(file)).isEqualTo("machin");
    }
    @Test
    public void episodeSepratedWithX() throws Exception {
        File file = new File("machin.1x01.truc.avi");
        Standardizer standardizer = new Standardizer(Standardizer.TVSHOW_1x01);
        assertThat(standardizer.getNewFilename(file)).isEqualTo("machin.S01E01.truc.avi");
        assertThat(standardizer.getNewDir(file)).isEqualTo("machin");
    }

    @Test
    public void episodeLike101() throws Exception {
        File file = new File("machin.0203.truc.avi");
        Standardizer standardizer = new Standardizer(Standardizer.TVSHOW_101);
        assertThat(standardizer.getNewFilename(file)).isEqualTo("machin.S02E03.truc.avi");
        assertThat(standardizer.getNewDir(file)).isEqualTo("machin");
    }

}
