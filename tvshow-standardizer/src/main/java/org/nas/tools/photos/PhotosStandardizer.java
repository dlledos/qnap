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

import java.io.*;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;
import java.util.regex.Pattern;

public class PhotosStandardizer extends Standardizer {

    public static void main(String[] args) throws Exception {
        Main.standardize(args, new PhotosStandardizer());
    }

    @Override
    public List<Pattern> getPatterns() {
        return null;
    }

    @Override
    public String getNewFilename(File file) {
        return getDate(file) + "." + getType(file) + getId(file) + "." + getExtension(file);
    }

    private String getDate(File file) {
        if (getType(file).equals("VID"))
            return formatDate(getDateFromTika(file));
        if (getType(file).equals("IMG"))
            return formatDate(getDateFromExif(file));
        return null;
    }

    private String getId(File file) {

        
        return "1";
    }

    private String getExtension(File file) {
        return FilenameUtils.getExtension(file.getName());
    }

    private String getType(File file) {
        if (getExtension(file).equals("mp4")) {
            return "VID";
        }
        if (getExtension(file).equals("jpg")) {
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
            tika.getParser().parse(stream, new BodyContentHandler(handler), metadata, e);
            return metadata.getDate(org.apache.tika.metadata.Metadata.DATE);
        } catch (Exception e) {
            return null;
        }
    }

    private Date getDateFromExif(File file) {
        Date date = getDate(file, ExifIFD0Directory.class, ExifIFD0Directory.TAG_DATETIME);
        if (date == null) {
            date = getDate(file, ExifSubIFDDirectory.class, ExifSubIFDDirectory.TAG_DATETIME_ORIGINAL);
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


    @Override
    public String getNewDir(File file) {
        return Paths.get(file.getPath()).getParent().getFileName().toString() + "-standardized";
    }
}
