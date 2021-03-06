package org.nas.tools.standardizer;

import com.google.common.io.Files;
import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Path;
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

        new Mover(sourceFolder, destinationFolder, standardizer, false).move(sourceFile);

        File expectedFile = Paths.get(destinationFolder.getPath(), "machin", "machin.S00E00.truc.avi").toFile();
        assertThat(expectedFile).exists();
    }

    @Test
    public void moveAndStandardizeFile() throws IOException {
        File sourceFile = newFile(sourceFolder, "..machin S00E00 truc....avi", 0);
        assertThat(sourceFile).exists();

        new Mover(sourceFolder, destinationFolder, standardizer, false).move(sourceFile);

        File expectedFile = Paths.get(destinationFolder.getPath(), "machin", "machin.S00E00.truc.avi").toFile();
        assertThat(expectedFile).exists();
    }

    @Test
    public void moveToDoublonFileIfFileAlreadyExistAnsSizeEquals() throws Exception {
        String filename = "machin.S00E00.truc.avi";
        File sourceFile = newFile(sourceFolder, filename, 2);
        newFile(Paths.get(destinationFolder.getPath(), standardizer.getNewDir(sourceFile)).toFile(), filename, 2);
        newFile(Paths.get(destinationFolder.getPath(), "doublons").toFile(),filename, 2);

        new Mover(sourceFolder, destinationFolder, standardizer, false).move(sourceFile);

        assertThat(Paths.get(destinationFolder.getPath(), "machin", "machin.S00E00.truc.avi").toFile()).exists();
        assertThat(Paths.get(destinationFolder.getPath(), "doublons", "machin.S00E00.truc.avi").toFile()).exists();
        assertThat(Paths.get(destinationFolder.getPath(), "doublons", "machin.S00E00.truc.copy1.avi").toFile()).exists();
    }

    @Test
    public void changeNameIfFileAlreadyExistAndSizeDifferent() throws Exception {
        String filename = "machin.S00E00.truc.avi";
        File sourceFile = newFile(sourceFolder, filename, 3);
        newFile(Paths.get(destinationFolder.getAbsolutePath(), standardizer.getNewDir(sourceFile)).toFile(), filename, 5);

        new Mover(sourceFolder, destinationFolder, standardizer, false).move(sourceFile);

        assertThat(Paths.get(destinationFolder.getPath(), "machin", "machin.S00E00.truc.avi").toFile()).exists();
        assertThat(Paths.get(destinationFolder.getPath(), "machin", "machin.S00E00.truc.copy1.avi").toFile()).exists();
    }


    @Test
    public void moveDoNotMoveIfDryRunIsTrue() throws Exception {
        String filename = "machin.S00E00.truc.avi";
        File sourceFile = newFile(sourceFolder, filename, 0);

        new Mover(sourceFolder, destinationFolder, standardizer, true).move(sourceFile);

        assertThat(Paths.get(destinationFolder.getPath(), "machin", "machin.S00E00.truc.avi").toFile()).doesNotExist();
    }

    private File newFile(File folder, String filename, int size) throws IOException {
        folder.mkdirs();
        File sourceFile = Paths.get(folder.getPath(), filename).toFile();
        sourceFile.createNewFile();
        String toWrite="";
        for (int i = 0; i < size; i++)
            toWrite += String.valueOf(i);
        Files.write(toWrite, sourceFile, Charset.defaultCharset());
        assertThat(sourceFile).exists();
        assertThat(sourceFile.length()).isEqualTo(size);
        System.out.println(sourceFile);
        return sourceFile;
    }

}
