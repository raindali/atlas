package atlas.util;

import java.io.*;

/**
 * ${DESCRIPTION}
 *
 * @author Ricky Fung
 */
public abstract class FileUtils {

    public static boolean deleteRecursively(File root) {
        if (root != null && root.exists()) {
            if (root.isDirectory()) {
                File[] children = root.listFiles();
                if (children != null) {
                    for (File child : children) {
                        deleteRecursively(child);
                    }
                }
            }
            return root.delete();
        }
        return false;
    }

    public static void copyRecursively(File src, File dest) throws IOException {
        Validator.isTrue(src != null && (src.isDirectory() || src.isFile()), "Source File must denote a directory or file");
        Validator.notNull(dest, "Destination File must not be null");
        doCopyRecursively(src, dest);
    }

    private static void doCopyRecursively(File src, File dest) throws IOException {
        if (src.isDirectory()) {
            dest.mkdir();
            File[] entries = src.listFiles();
            if (entries == null) {
                throw new IOException("Could not list files in directory: " + src);
            }
            for (File entry : entries) {
                doCopyRecursively(entry, new File(dest, entry.getName()));
            }
        }
        else if (src.isFile()) {
            try {
                dest.createNewFile();
            } catch (IOException ex) {
                IOException ioex = new IOException("Failed to create file: " + dest);
                ioex.initCause(ex);
                throw ioex;
            }
            copy(src, dest);
        }
    }

    public static int copy(File in, File out) throws IOException {
        Validator.notNull(in, "No input File specified");
        Validator.notNull(out, "No output File specified");
        return copy(new BufferedInputStream(new FileInputStream(in)),
                new BufferedOutputStream(new FileOutputStream(out)));
    }

    public static int copy(InputStream in, OutputStream out) throws IOException {
        Validator.notNull(in, "No InputStream specified");
        Validator.notNull(out, "No OutputStream specified");
        try {
            return StreamUtils.copy(in, out);
        } finally {
            StreamUtils.closeQuietly(in);
            StreamUtils.closeQuietly(out);
        }
    }
}
