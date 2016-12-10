package org.nas.tools.standardizer;


import java.io.File;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;

public class FileManager {

    private final Standardizer standardizer;
    private boolean dryRun;
    private File destinationDirectory;

    public FileManager(File destinationDirectory, Standardizer standardizer, boolean dryRun) {
        this.destinationDirectory = destinationDirectory;
        this.standardizer = standardizer;
        this.dryRun = dryRun;
    }

    public FileManager(String destinationDirectory, Standardizer standardizer, boolean dryRun) {
        this(new File(destinationDirectory), standardizer, dryRun);
    }

    public void move(String sourceDirectory) throws InvocationTargetException, NoSuchMethodException, InstantiationException, IOException, IllegalAccessException {
        move(new File(sourceDirectory));
    }

    public void move(File sourceDirectory) throws InvocationTargetException, NoSuchMethodException, InstantiationException, IOException, IllegalAccessException {
        System.out.println("Starting scan of " + sourceDirectory.getAbsolutePath());
        int movedFileCount = standardizer.moveLoop(sourceDirectory, destinationDirectory, dryRun);
        System.out.println("Moved " + movedFileCount + " file from " + sourceDirectory.getAbsolutePath() + " to "  + destinationDirectory.getAbsolutePath());
    }

}