package org.nas.tools.standardizer;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;

import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

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
        standardizer = getStandardizer();
    }

    @After
    public void after() {
        destinationFolder.delete();
        sourceFolder.delete();
    }

    private Standardizer getStandardizer() {
        return new Standardizer() {
            @Override
            public List<Pattern> getPatterns() {
                ArrayList<Pattern> patterns = new ArrayList<>();
                patterns.add( Pattern.compile(".*"));
                return patterns;
            }

            @Override
            public String getNewFilename(File file) {
                return "machin.S00E00.truc.avi";
            }

            @Override
            public String getNewDir(File file) {
                return "machin";
            }
        };
    }

    @Test
    public void move() throws IOException {
        File sourceFile = newFile(sourceFolder, "machin.S00E00.truc.avi", 0);
        assertThat(sourceFile).exists();

        new Mover(destinationFolder, standardizer, false).move(sourceFile);

        File expectedFile = Paths.get(destinationFolder.getPath(), "machin", "machin.S00E00.truc.avi").toFile();
        assertThat(expectedFile).exists();
    }

    @Test
    public void moveAndStandardizeFile() throws IOException {
        File sourceFile = newFile(sourceFolder, "..machin S00E00 truc....avi", 0);
        assertThat(sourceFile).exists();

        new Mover(destinationFolder, standardizer, false).move(sourceFile);

        File expectedFile = Paths.get(destinationFolder.getPath(), "machin", "machin.S00E00.truc.avi").toFile();
        assertThat(expectedFile).exists();
    }

    @Test
    public void changeNameIfFileAlreadyExistAnsSizeEquals() throws Exception {
        String filename = "machin.S00E00.truc.avi";
        File sourceFile = newFile(sourceFolder, filename, 0);
        newFile(destinationFolder, Paths.get(standardizer.getNewDir(sourceFile), filename).toString(), 0);
        newFile(destinationFolder, Paths.get(standardizer.getNewDir(sourceFile), "machin.S00E00.truc.copy1.avi").toString(), 0);
        newFile(destinationFolder, Paths.get(standardizer.getNewDir(sourceFile), "machin.S00E00.truc.copy2.avi").toString(), 0);

        new Mover(destinationFolder, standardizer, false).move(sourceFile);

        assertThat(Paths.get(destinationFolder.getPath(), "machin", "machin.S00E00.truc.avi").toFile()).exists();
        assertThat(Paths.get(destinationFolder.getPath(), "machin", "machin.S00E00.truc.copy1.avi").toFile()).exists();
        assertThat(Paths.get(destinationFolder.getPath(), "machin", "machin.S00E00.truc.copy2.avi").toFile()).exists();
        assertThat(Paths.get(destinationFolder.getPath(), "machin", "machin.S00E00.truc.copy3.avi").toFile()).exists();
    }

    @Test
    public void moveDoNotMoveIfTestIsTrue() throws Exception {
        String filename = "machin.S00E00.truc.avi";
        File sourceFile = newFile(sourceFolder, filename, 0);

        new Mover(destinationFolder, standardizer, true).move(sourceFile);

        assertThat(Paths.get(destinationFolder.getPath(), "machin", "machin.S00E00.truc.avi").toFile()).doesNotExist();
    }

    /*
        @Test
        public void changeNameIfFileAlreadyExistAndSizeDifferent() throws Exception {
            File sourceFile = newFile(sourceFolder, "machin.S00E00.truc.avi", 7);
            newFile(destinationFolder, Paths.get(standardizer.getNewDir(sourceFile), "machin.S00E00.truc.avi").toString(), 5);

            new Mover(destinationFolder, standardizer).move(sourceFile);

            assertThat(Paths.get(destinationFolder.getPath(), "machin", "machin.S00E00.truc.avi").toFile()).exists();
            assertThat(Paths.get(destinationFolder.getPath(), "machin", "machin.S00E00.truc.size-7.avi").toFile()).exists();
        }
    */
    private File newFile(File folder, String filename, int size) throws IOException {
        File sourceFile = Paths.get(folder.getPath(), filename).toFile();
        sourceFile.mkdirs();
        sourceFile.createNewFile();
        //System.out.println(sourceFile.setWritable(true));
        //Files.write("123", sourceFile, Charset.defaultCharset());
        assertThat(sourceFile).exists();
        return sourceFile;
    }

}
