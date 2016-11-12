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

public class PhotosManagerTest {
    private File sourceDirectory;
    private TemporaryFolder temporaryFolder;
    private PhotosManager photosManager;

    @Before
    public void setUp() throws Exception {
        temporaryFolder = new TemporaryFolder();
        temporaryFolder.create();
        sourceDirectory = temporaryFolder.newFolder();
        photosManager = new PhotosManager();
    }

    @After
    public void tearDown() throws Exception {
        temporaryFolder.delete();
    }

    @Test
    public void nominal() throws Exception {
        assertNewFilename("IMG.jpg", "2015-02-07_19-00-32.IMG1.jpg");
        assertNewFilename("PANO.jpg", "2014-12-14_12-21-14.IMG1.jpg");//[Exif IFD0] - Date/Time = 2014:12:14 12:21:14
        assertNewFilename("VID.mp4", "2015-07-30_11-59-36.VID1.mp4");
    }

    private void assertNewFilename(String actualFilename, String expectedFilename) throws ImageProcessingException, IOException, TikaException, SAXException {
        File file = new File(getClass().getClassLoader().getResource(actualFilename).getFile());
        assertThat(file).exists();
        //printMetatData(file);
        assertThat(photosManager.getNewFilename(file)).isEqualTo(expectedFilename);
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