package org.nas.tools.photos;

import com.drew.imaging.ImageMetadataReader;
import com.drew.imaging.ImageProcessingException;
import com.drew.metadata.Directory;
import com.drew.metadata.Metadata;
import com.drew.metadata.Tag;
import org.apache.tika.exception.TikaException;
import org.junit.After;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;
import org.nas.tools.standardizer.FileManager;
import org.nas.tools.tvshow.TvShowStandardizer;
import org.xml.sax.SAXException;

import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.Arrays;

import static org.fest.assertions.Assertions.assertThat;

public class PhotosStandardizerTest {
    private File sourceDirectory;
    private TemporaryFolder temporaryFolder;
    private PhotosStandardizer photosStandardizer;
    private File destinationDirectory;

    @Before
    public void setUp() throws Exception {
        temporaryFolder = new TemporaryFolder();
        temporaryFolder.create();
        destinationDirectory = temporaryFolder.newFolder();
        sourceDirectory = temporaryFolder.newFolder();
        photosStandardizer = new PhotosStandardizer();
    }

    @After
    public void tearDown() throws Exception {
        temporaryFolder.delete();
    }

    @Test
    public void nominal() throws Exception {
        assertNewFilename("IMG.jpg", "2015-02-07_19-00-32.IMG.jpg");
        assertNewFilename("PANO.jpg", "2014-12-14_12-21-14.IMG.jpg");
        assertNewFilename("VID.mp4", "2015-07-30_11-59-36.VID.mp4");
    }


    @Test
    public void canMove() throws Exception {
        newFile("test.S01E01.truc.mkv");

        new FileManager(destinationDirectory, new TvShowStandardizer(), false).move(sourceDirectory);

        assertThat(destinationDirectory.listFiles()).hasSize(1);
        assertThat(destinationDirectory.listFiles()[0].getName()).isEqualTo("test");
        assertThat(destinationDirectory.listFiles()[0].listFiles()).hasSize(1);
        assertThat(destinationDirectory.listFiles()[0].listFiles()[0].getName()).isEqualTo("test.S01E01.truc.mkv");
    }

    @Test
    public void canMoveVideoFileOnly() throws Exception {
        newFile("test.S01E01.truc.mkv");
        newFile("rien.a.voir.boby");

        new FileManager(destinationDirectory, new TvShowStandardizer(), false).move(sourceDirectory);

        assertThat(destinationDirectory.listFiles()).hasSize(1);
        assertThat(destinationDirectory.listFiles()[0].getName()).isEqualTo("test");
        assertThat(destinationDirectory.listFiles()[0].listFiles()).hasSize(1);
        assertThat(destinationDirectory.listFiles()[0].listFiles()[0].getName()).isEqualTo("test.S01E01.truc.mkv");
    }

    @Test
    public void canMoveTvShownFileOnly() throws Exception {
        newFile("test.S01E01.truc.mkv");
        newFile("rien.a.voir.boby.mkv");

        new FileManager(destinationDirectory, new TvShowStandardizer(), false).move(sourceDirectory);

        assertThat(destinationDirectory.listFiles()).hasSize(1);
        assertThat(destinationDirectory.listFiles()[0].getName()).isEqualTo("test");
        assertThat(destinationDirectory.listFiles()[0].listFiles()).hasSize(1);
        assertThat(destinationDirectory.listFiles()[0].listFiles()[0].getName()).isEqualTo("test.S01E01.truc.mkv");
    }


    @Test
    public void canMoveDifferentPatternFile() throws Exception {
        newFile("test.S01E01.truc.mkv");
        newFile("rien.a.voir.boby.mkv");
        newFile("youpi.1x02.cestlafete.mkv");
        newFile("pouet.407.cestlafete.mkv");

        new FileManager(destinationDirectory, new TvShowStandardizer(), false).move(sourceDirectory);


        File[] listFiles = destinationDirectory.listFiles();
        Arrays.sort(listFiles);
        assertThat(listFiles).hasSize(3);
        assertThat(listFiles[0].getName()).isEqualTo("pouet");
        assertThat(listFiles[0].listFiles()).hasSize(1);
        assertThat(listFiles[0].listFiles()[0].getName()).isEqualTo("pouet.S04E07.cestlafete.mkv");

        assertThat(listFiles[1].getName()).isEqualTo("test");
        assertThat(listFiles[1].listFiles()).hasSize(1);
        assertThat(listFiles[1].listFiles()[0].getName()).isEqualTo("test.S01E01.truc.mkv");

        assertThat(listFiles[2].getName()).isEqualTo("youpi");
        assertThat(listFiles[2].listFiles()).hasSize(1);
        assertThat(listFiles[2].listFiles()[0].getName()).isEqualTo("youpi.S01E02.cestlafete.mkv");
    }

    private void newFile(String filename) throws IOException {
        File newFile = new File(sourceDirectory.getAbsolutePath(), filename);
        newFile.createNewFile();
    }

    private void assertNewFilename(String actualFilename, String expectedFilename) throws ImageProcessingException, IOException, TikaException, SAXException {
        File file = new File(getClass().getClassLoader().getResource(actualFilename).getFile());
        assertThat(file).exists();
        //printMetatData(file);
        assertThat(photosStandardizer.getNewFilename(file)).isEqualTo(expectedFilename);
        assertThat(photosStandardizer.getNewDir(file)).isEqualTo(Paths.get(file.getPath()).getParent().getFileName().toString() + "-standardized");
    }

    private void printMetatData(File file) throws ImageProcessingException, IOException {
        Metadata metadata = ImageMetadataReader.readMetadata(file);
        for (Directory directory : metadata.getDirectories()) {
            for (Tag tag : directory.getTags()) {
                System.out.format("[%s] - %s (%d) = %s\n",
                        directory.getName(), tag.getTagName(), tag.getTagType(), tag.getDescription());
            }
            if (directory.hasErrors()) {
                for (String error : directory.getErrors()) {
                    System.err.format("ERROR: %s", error);
                }
            }
        }
    }


}