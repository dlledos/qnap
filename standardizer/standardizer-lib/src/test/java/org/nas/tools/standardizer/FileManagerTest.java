package org.nas.tools.standardizer;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Pattern;

import static org.fest.assertions.Assertions.assertThat;


public class FileManagerTest {


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

    private Standardizer getStandardizer() {
        return new Standardizer() {
            @Override
            public List<Pattern> getPatterns() {
                ArrayList<Pattern> patterns = new ArrayList<>();
                patterns.add( Pattern.compile(".*.mkv"));
                return patterns;
            }

            @Override
            public String getNewFilename(File file) {
                return file.getName();
            }

            @Override
            public String getNewDir(File file) {
                return file.getName().split("\\.")[0];
            }
        };
    }

    @Test
    public void canMove() throws Exception {
        newFile("test.S01E01.truc.mkv");

        new FileManager(destinationDirectory, getStandardizer(), false).move(sourceDirectory);

        assertThat(destinationDirectory.listFiles()).hasSize(1);
        assertThat(destinationDirectory.listFiles()[0].getName()).isEqualTo("test");
        assertThat(destinationDirectory.listFiles()[0].listFiles()).hasSize(1);
        assertThat(destinationDirectory.listFiles()[0].listFiles()[0].getName()).isEqualTo("test.S01E01.truc.mkv");
    }

    @Test
    public void canMoveVideoFileOnly() throws Exception {
        newFile("test.S01E01.truc.mkv");
        newFile("rien.a.voir.boby");

        new FileManager(destinationDirectory, getStandardizer(), false).move(sourceDirectory);

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