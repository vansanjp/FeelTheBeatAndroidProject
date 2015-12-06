package com.example.vietanh.feelthebeat2;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.database.Cursor;
import android.net.Uri;
import android.provider.MediaStore;

public class GetListFile {

//=====================================================Musics================================================================

    public static String stringMusic(String string) {
        String child = "-";
        String son = "";
        for (int i = 0; i < string.length(); i++) {
            if (child.compareTo(String.valueOf(string.charAt(i))) == 0) {
                break;
            } else {
                son += String.valueOf(string.charAt(i));
            }
        }
        return son;
    }

    public static String checkMusic(String string) {
        if (string.endsWith(".mp3")) {
            string = string.replace(".mp3", "");
        }
        return string;
    }

    @SuppressLint("Recycle")
    public static void insertMusic(Activity activity,MusicHelper helper) {
        String selectionMusic = MediaStore.Audio.Media.IS_MUSIC + " != 0";
        String selectionRingtone = MediaStore.Audio.Media.IS_RINGTONE + " != 0";
        String selectionNotification = MediaStore.Audio.Media.IS_NOTIFICATION + " != 0";
        String sortOrder = MediaStore.Audio.Media.DISPLAY_NAME + " ASC";
        String[] projection = {
                MediaStore.Audio.Media.ARTIST,
                MediaStore.Audio.Media.DISPLAY_NAME,
                MediaStore.Audio.Media.DURATION,
                MediaStore.Audio.Media.SIZE,
                MediaStore.Audio.Media.DATA
        };
        Uri uri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
        @SuppressLint("Recycle") Cursor musicCursor = activity.getContentResolver().query(uri, projection, selectionMusic, null, sortOrder);
        if (musicCursor != null) {
            while (musicCursor.moveToNext()) {
                helper.INSERT_Music(checkMusic(stringMusic(musicCursor.getString(1))),musicCursor.getString(0),""+musicCursor.getInt(2),musicCursor.getString(4));
            }
        }
        musicCursor = activity.getContentResolver().query(uri, projection, selectionRingtone, null, sortOrder);
        if (musicCursor != null) {
            while (musicCursor.moveToNext()) {
                helper.INSERT_Music(checkMusic(stringMusic(musicCursor.getString(1))),musicCursor.getString(0),""+musicCursor.getInt(2),musicCursor.getString(4));
            }
        }
        musicCursor = activity.getContentResolver().query(uri, projection, selectionNotification, null, sortOrder);
        if (musicCursor != null) {
            while (musicCursor.moveToNext()) {
                helper.INSERT_Music(checkMusic(stringMusic(musicCursor.getString(1))),musicCursor.getString(0),""+musicCursor.getInt(2),musicCursor.getString(4));
            }
        }
    }
//=====================================================Musics================================================================

}