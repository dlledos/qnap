package org.nas.tools.photos;

import com.drew.imaging.ImageMetadataReader;
import com.drew.metadata.Directory;
import com.drew.metadata.Metadata;
import com.drew.metadata.exif.ExifIFD0Directory;
import com.drew.metadata.exif.ExifSubIFDDirectory;
import org.apache.commons.io.FilenameUtils;
import org.apache.tika.Tika;
import org.apache.tika.parser.ParseContext;
import org.apache.tika.parser.Parser;
import org.apache.tika.sax.BodyContentHandler;
import org.apache.tika.sax.WriteOutContentHandler;
import org.nas.tools.standardizer.Main;
import org.nas.tools.standardizer.Standardizer;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;
import java.util.regex.Pattern;

public class PhotosStandardizer extends Standardizer {

    public static final Pattern VIDEO_AND_IMAGE_PATTERN = Pattern.compile(".*" + DOT_SEPARATOR + "(mp4|mov|jpg|jpeg)", Pattern.CASE_INSENSITIVE);
    private boolean lastWriteAcces;

    PhotosStandardizer(boolean lastWriteAcces){
        this.lastWriteAcces = lastWriteAcces;
        pattern = VIDEO_AND_IMAGE_PATTERN;
    }
    public static void main(String[] args) throws Exception {
        Main.standardize(args, new PhotosStandardizer(false));
    }

    @Override
    public List<Pattern> getPatterns() {
        List<Pattern> patternsList = new ArrayList<>();
        patternsList.add(VIDEO_AND_IMAGE_PATTERN);
        return patternsList;
    }

    @Override
    public String getNewDir(File file) {
        return ".";
    }

    @Override
    public String getNewFilename(File file) {
        return getDate(file) + "." + getType(file) + "." + getExtension(file);
    }

    private String getDate(File file) {
        if (getType(file).equals("VID"))
            return formatDate(getDateFromTika(file));
        if (getType(file).equals("IMG"))
            return formatDate(getDateFromExif(file));
        return null;
    }

    private String getExtension(File file) {
        return FilenameUtils.getExtension(file.getName()).toLowerCase();
    }

    private String getType(File file) {
        if (getExtension(file).toLowerCase().matches("mp4|mov")) {
            return "VID";
        }
        if (getExtension(file).toLowerCase().equals("jpg")) {
            return "IMG";
        }
        return null;
    }

    private Date getDateFromTika(File file) {
        Tika tika = new Tika();
        WriteOutContentHandler handler = new WriteOutContentHandler();
        try {
            InputStream stream = new FileInputStream(file);
            ParseContext e = new ParseContext();
            e.set(Parser.class, tika.getParser());
            org.apache.tika.metadata.Metadata metadata = new org.apache.tika.metadata.Metadata();
            BodyContentHandler contentHandler = new BodyContentHandler(handler);
            tika.getParser().parse(stream, contentHandler, metadata, e);
            Date date = metadata.getDate(org.apache.tika.metadata.Metadata.DATE);
            if (date.compareTo(new Date(-2082844800000L)) == 0) {
                String content = contentHandler.toString();
                int endIndex = content.indexOf("\nLavf");
                if (endIndex != -1) {
                    date = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssZ").parse(content.substring(0, endIndex));
                }
                else {
                    date = new Date(file.lastModified());
                }
            }
            return date;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private Date getDateFromExif(File file) {
        Date date = getDate(file, ExifIFD0Directory.class, ExifIFD0Directory.TAG_DATETIME);
        if (date == null) {
            date = getDate(file, ExifSubIFDDirectory.class, ExifSubIFDDirectory.TAG_DATETIME_ORIGINAL);
        }
        if (date == null && lastWriteAcces) {
            date = new Date(file.lastModified());
        }
        return date;
    }

    private Date getDate(File file, Class type, int tagType) {
        try {
            Metadata metadata = ImageMetadataReader.readMetadata(file);
            Directory directory = metadata.getFirstDirectoryOfType(type);
            return directory.getDate(tagType, TimeZone.getDefault());
        } catch (Exception e) {
            System.out.println(e);
            return null;
        }
    }


    private String formatDate(Date date) {
        return new SimpleDateFormat("yyyy-MM-dd_HH-mm-ss").format(date);
    }
}
