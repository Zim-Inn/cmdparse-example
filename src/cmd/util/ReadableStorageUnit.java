//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package cmd.util;

import java.text.DecimalFormat;

public class ReadableStorageUnit {
    static final String[] units = new String[]{"Bytes", "KB", "MB", "GB", "TB", "PB", "EB", "YB"};
    static final double ln1024 = Math.log(1024.0);

    public ReadableStorageUnit() {
    }

    public static String convertBytesToString(long size) {
        return convertBytesToString(size, 3);
    }

    public static String convertBytesToString(long size, int dotNum) {
        if (size <= 0L) {
            return "0";
        } else {
            int digitGroups = (int)(Math.log((double)size) / ln1024);
            StringBuilder pattern = new StringBuilder("#,##0");
            dotNum = Math.min(3, Math.max(0, dotNum));
            if (dotNum != 0) {
                pattern.append(".");

                for(int i = 0; i < dotNum; ++i) {
                    pattern.append("#");
                }
            }

            return (new DecimalFormat(pattern.toString())).format((double)size / Math.pow(1024.0, (double)digitGroups)) + units[digitGroups];
        }
    }

    public static String convert(long size, int sourceUnitIdx, int targetUnitIdx, int dotNum) {
        if (size <= 0L) {
            return "0";
        } else {
            dotNum = Math.min(3, Math.max(0, dotNum));
            check(sourceUnitIdx);
            check(targetUnitIdx);
            int offset = targetUnitIdx - sourceUnitIdx;
            double value;
            if (offset > 0) {
                value = (double)size / Math.pow(1024.0, (double)offset);
            } else {
                value = (double)size / Math.pow(1024.0, (double)(-offset));
            }

            StringBuilder pattern = new StringBuilder("#,##0");
            if (dotNum != 0) {
                pattern.append(".");

                for(int i = 0; i < dotNum; ++i) {
                    pattern.append("#");
                }
            }

            return (new DecimalFormat(pattern.toString())).format(value) + units[targetUnitIdx];
        }
    }

    static void check(int idx) {
        if (idx < 0 || idx > 7) {
            throw new IllegalArgumentException("Unsupported storage unit");
        }
    }
}
