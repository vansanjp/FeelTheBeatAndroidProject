package com.example.vietanh.feelthebeat2;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;

public class Setting extends AppCompatActivity {

    private ImageView likeBtn;
    private ImageView ringBtn;
    private ImageView deleteBtn;

    private TextView add_or_remove;

    private int recentIndex;
    private MusicListItem temp;
    private MusicHelper helper;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.setting);

        recentIndex = getIntent().getIntExtra("key", 0);
        temp = MainActivity.musics.get(recentIndex);

        likeBtn = (ImageView)findViewById(R.id.favorite);
        add_or_remove = (TextView)findViewById(R.id.textfavorite);
        if(temp.getmFavorite() == 0){
            likeBtn.setBackgroundResource(R.drawable.blur_heart);
            add_or_remove.setText("Add to favorites");
        }
        else{
            likeBtn.setBackgroundResource(R.drawable.heart);
            add_or_remove.setText("Remove from favorites");
        }
        likeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int favorite = temp.getmFavorite();
                if(favorite == 0){
                    favorite = 1;
                    likeBtn.setBackgroundResource(R.drawable.heart);
                    add_or_remove.setText("Remove from favorites");
                }
                else{
                    favorite = 0;
                    likeBtn.setBackgroundResource(R.drawable.blur_heart);
                    add_or_remove.setText("Add to favorites");
                }
                MainActivity.musics.get(recentIndex).setmFavorite(favorite);
                helper = new MusicHelper(Setting.this);
                int success = helper.setNewFavorite(temp.getmName(),favorite);
                if(success != 0)
                    Toast.makeText(Setting.this, "Done", Toast.LENGTH_SHORT).show();
                quit(Setting.this);
            }
        });

        ringBtn = (ImageView)findViewById(R.id.ringtones);
        ringBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                File file = new File(temp.getmPath());
                ContentValues values = new ContentValues();
                values.put(MediaStore.MediaColumns.DATA, file.getAbsolutePath());
                values.put(MediaStore.MediaColumns.TITLE, temp.getmName());
                values.put(MediaStore.MediaColumns.MIME_TYPE, "audio/mp3");
                values.put(MediaStore.Audio.Media.IS_RINGTONE, true);
                Uri uri = MediaStore.Audio.Media.getContentUriForPath(file.getAbsolutePath());
                getContentResolver().delete(uri, MediaStore.MediaColumns.DATA + "=\"" + file.getAbsolutePath() + "\"", null);
                Uri newUri = getApplicationContext().getContentResolver().insert(uri, values);
                RingtoneManager.setActualDefaultRingtoneUri(getApplicationContext(), RingtoneManager.TYPE_RINGTONE, newUri);
                Toast.makeText(Setting.this, "done", Toast.LENGTH_SHORT).show();
                quit(Setting.this);
            }
        });

        deleteBtn = (ImageView)findViewById(R.id.delete);
        deleteBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (MainActivity.mediaPlayer.isPlaying()) {
                    MainActivity.mediaPlayer.stop();
                }
                File file = new File(MainActivity.musics.get(recentIndex).getmPath());
                boolean isSuccessful = file.delete();
                if (isSuccessful) {
                    sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, Uri.fromFile(new File(MainActivity.musics.get(recentIndex).getmPath()))));
                    String name = MainActivity.musics.get(recentIndex).getmName();
                    MusicListItem temp = MainActivity.musics.remove(recentIndex);
                    helper = new MusicHelper(Setting.this);
                    int success = helper.DELETE_Song(name);
                    if(success != 0) {
                        Toast.makeText(Setting.this, "Deleted", Toast.LENGTH_SHORT).show();
                    }
                    helper.CloseBD();
                }
                quit(Setting.this);
            }
        });
    }

    private void quit(Context context){
        Intent i = new Intent("abc");
        LocalBroadcastManager.getInstance(context).sendBroadcast(i);
        finish();
    }
}
