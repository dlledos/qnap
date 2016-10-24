package standardizer;

import org.junit.Test;
import standardizer.Standardizer;

import static org.fest.assertions.Assertions.assertThat;

public class StandardizerTest {

    /*
    myTest "nominal" "machin.S01E01.truc.avi" "machin/machin.S01E01.truc.avi"
myTest "espace-dans-fichier" "machin S01E01 truc avi" "machin/machin.S01E01.truc.avi"
myTest "commence-avec-un-point" ".machin S01E01 truc avi" "machin/machin.S01E01.truc.avi"
myTest "commence-avec-un-tiret" "-machin S01E01 truc avi" "machin/machin.S01E01.truc.avi"
myTest "nombre-sur-2-chiffres" "-machin S1E1 truc avi" "machin/machin.S01E01.truc.avi"
myTest "se-dans-le-texte-reste-se" "machin.S01E01.se.avi" "machin/machin.S01E01.se.avi"
myTest "deux-fois-SxxExx-prends-le-1er" "machin.S02E03.truc.S01E48.bidule.mp4" "machin/machin.S02E03.truc.S01E48.bidule.mp4"
myTest "parenthèse-après-SxxExx" "machin.S02E03(1).truc.mp4" "machin/machin.S02E03.(1).truc.mp4"

     */

    @Test
    public void nominal() throws Exception {
        String result = new Standardizer().transform("machin.S01E01.truc.avi");

        assertThat(result).isEqualTo("machin/machin.S01E01.truc.avi");
    }
}
