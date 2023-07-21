//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package cmd.util;

public class FilenameUtils {
    private static final int NOT_FOUND = -1;
    public static final char EXTENSION_SEPARATOR = '.';
    private static final char UNIX_SEPARATOR = '/';
    private static final char WINDOWS_SEPARATOR = '\\';

    public FilenameUtils() {
    }

    public static boolean isExtension(String filename, String extension) {
        if (filename == null) {
            return false;
        } else {
            failIfNullBytePresent(filename);
            if (extension != null && !extension.isEmpty()) {
                String fileExt = getExtension(filename);
                return fileExt.equals(extension);
            } else {
                return indexOfExtension(filename) == -1;
            }
        }
    }

    public static String getExtension(String filename) {
        if (filename == null) {
            return null;
        } else {
            int index = indexOfExtension(filename);
            return index == -1 ? "" : filename.substring(index + 1);
        }
    }

    private static void failIfNullBytePresent(String path) {
        int len = path.length();

        for(int i = 0; i < len; ++i) {
            if (path.charAt(i) == 0) {
                throw new IllegalArgumentException("Null byte present in file/path name. There are no known legitimate use cases for such data, but several injection attacks may use it");
            }
        }

    }

    public static int indexOfExtension(String filename) {
        if (filename == null) {
            return -1;
        } else {
            int extensionPos = filename.lastIndexOf(46);
            int lastSeparator = indexOfLastSeparator(filename);
            return lastSeparator > extensionPos ? -1 : extensionPos;
        }
    }

    public static int indexOfLastSeparator(String filename) {
        if (filename == null) {
            return -1;
        } else {
            int lastUnixPos = filename.lastIndexOf(47);
            int lastWindowsPos = filename.lastIndexOf(92);
            return Math.max(lastUnixPos, lastWindowsPos);
        }
    }
}
