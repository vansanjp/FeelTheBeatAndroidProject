package com.example.vietanh.feelthebeat2;

public class StorageUtil {

    public static String convertDuration (String mDuration) {
        long seconds;
        long minutes = 0;
        long hours = 0;
        long duration = Long.parseLong(mDuration);
        seconds = duration / 1000;
        if(seconds >= 60) {
            minutes = seconds / 60;
            seconds %= 60;
            if(minutes > 60) {
                hours = minutes / 60;
                minutes %= 60;
            } else {
                if (seconds < 10) {
                    if (minutes < 10) {
                        return ("0" + minutes + ":0" + seconds);
                    } else {
                        return ("" + minutes + ":0" + seconds);
                    }
                } else if (seconds >= 10){
                    if (minutes < 10) {
                        return ("0" + minutes + ":" + seconds);
                    } else {
                        return ("" + minutes + ":" + seconds);
                    }
                }
            }
        } else {
            if (seconds < 10) {
                return ("0" + minutes + ":0" + seconds);
            } else if (seconds >= 10) {
                return ("0" + minutes + ":" + seconds);
            }
        }
        return ("" + hours + ":" + minutes + ":" + seconds);
    }

    public static String convertStorage(long size) {
        long kb = 1024;
        long mb = kb * 1024;
        long gb = mb * 1024;

        if (size >= gb) {
            return String.format("%.2f GB", (float) size / gb);
        } else if (size >= mb) {
            float f = (float) size / mb;
            return String.format(f > 100 ? "%.1f MB" : "%.2f MB", f);
        } else if (size >= kb) {
            float f = (float) size / kb;
            return String.format(f > 100 ? "%.1f KB" : "%.2f KB", f);
        } else
            return String.format("%d B", size);
    }

    public static String divideString (String string) {
        if (string.substring(string.length() - 2).compareTo("MB") == 0) {
            string = "MB";
        } else if (string.substring(string.length() - 2).compareTo("GB") == 0) {
            string = "GB";
        } else if (string.substring(string.length() - 2).compareTo("KB") == 0) {
            string = "KB";
        } else if (string.substring(string.length() - 1).compareTo("B") == 0) {
            string = "B";
        }
        return string;
    }

}
