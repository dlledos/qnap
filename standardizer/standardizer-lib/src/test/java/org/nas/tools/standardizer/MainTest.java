package org.nas.tools.standardizer;

import com.google.common.collect.Lists;
import org.junit.Before;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;

import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.List;
import java.util.regex.Pattern;

import static org.fest.assertions.Assertions.assertThat;


public class MainTest {
    private File sourceDirectory;
    private TemporaryFolder temporaryFolder;
    private File destinationDirectory;

    @Before
    public void setUp() throws Exception {
        temporaryFolder = new TemporaryFolder();
        temporaryFolder.create();
        destinationDirectory = temporaryFolder.newFolder();
        sourceDirectory = temporaryFolder.newFolder();
    }

    @Test
    public void standardize() throws Exception {
        File rootFile = newFile("test1.jpg");
        File fileWithOneSubDir = newFile("subdir", "test1.jpg");
        File fileWithTwoSubDir = newFile(Paths.get("subdir1", "subdir2").toString(), "test1.jpg");

        Main.standardize(getArgs(), getStandardizer());

        assertThat(Paths.get(destinationDirectory.getAbsolutePath(), "new." + rootFile.getName()).toFile()).exists();
        assertThat(Paths.get(destinationDirectory.getAbsolutePath(), "subdir", "new." + fileWithOneSubDir.getName()).toFile()).exists();
        assertThat(Paths.get(destinationDirectory.getAbsolutePath(), "subdir1", "subdir2",  "new." + fileWithTwoSubDir.getName()).toFile()).exists();
    }

    private File newFile(String filename) throws IOException {
        return newFile(".", filename);
    }

    private File newFile(String dir, String filename) throws IOException {
        File newFile = Paths.get(sourceDirectory.getAbsolutePath(), dir, filename).toFile();
        newFile.mkdirs();
        newFile.createNewFile();
        assertThat(newFile).exists();
        return newFile;
    }

    private String[] getArgs() {
        String[] args = new String[5];
        args[0] = "test";
        args[1] = "-s";
        args[2] = sourceDirectory.getAbsolutePath();
        args[3] = "-d";
        args[4] = destinationDirectory.getAbsolutePath();
        return args;
    }

    private Standardizer getStandardizer() {
        return new Standardizer() {
            @Override
            public List<Pattern> getPatterns() {
                return Lists.newArrayList(Pattern.compile(".*\\.jpg"));
            }

            @Override
            public String getNewFilename(File file) {
                return "new." + file.getName();
            }

            @Override
            public String getNewDir(File file) {
                return ".";
            }
        };
    }
}