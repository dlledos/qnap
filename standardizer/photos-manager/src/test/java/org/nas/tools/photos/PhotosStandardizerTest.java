package org.nas.tools.photos;

import com.drew.imaging.ImageMetadataReader;
import com.drew.imaging.ImageProcessingException;
import com.drew.metadata.Directory;
import com.drew.metadata.Metadata;
import com.drew.metadata.Tag;
import org.apache.tika.exception.TikaException;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;
import org.xml.sax.SAXException;

import java.io.File;
import java.io.IOException;

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
        photosStandardizer = new PhotosStandardizer(true);
    }

    @After
    public void tearDown() throws Exception {
        temporaryFolder.delete();
    }

    @Test
    public void nominal() throws Exception {
        assertNewFilename("IMG.jpg", "2015-02-07_19-00-32.IMG.jpg");
        assertNewFilename("IMG1.JPG", "2015-02-07_19-00-32.IMG.jpg");
        assertNewFilename("PANO.jpg", "2014-12-14_12-21-14.IMG.jpg");
        assertNewFilename("VID.MOV", "2014-03-17_13-00-17.VID.mov");
        assertNewFilename("1904.mp4", "2015-09-11_19-38-58.VID.mp4");
   }

    @Test
    public void pattern() throws Exception {
        String filename = "bla bla.JPG";
        newFile(filename);
        assertThat(photosStandardizer.getPatterns().get(0).matcher(filename).matches()).isTrue();
        assertThat(photosStandardizer.findMatchingFile(sourceDirectory)).hasSize(1);
    }
    private File newFile(String filename) throws IOException {
        File newFile = new File(sourceDirectory.getAbsolutePath(), filename);
        newFile.createNewFile();
        return newFile;
    }
    private void assertNewFilename(String actualFilename, String expectedFilename) throws ImageProcessingException, IOException, TikaException, SAXException {
        File file = new File(getClass().getClassLoader().getResource(actualFilename).getFile());
        assertThat(file).exists();
        //printMetatData(file);
        assertThat(photosStandardizer.getNewFilename(file)).isEqualTo(expectedFilename);
        assertThat(photosStandardizer.getNewDir(file)).isEqualTo(".");
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