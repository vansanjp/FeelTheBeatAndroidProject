package com.example.vietanh.feelthebeat2;

import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.os.Handler;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;

import java.util.concurrent.TimeUnit;

public class SelectedMusic extends AppCompatActivity {
    TextView music_name;
    TextView music_singer;
    TextView tx1;
    TextView tx2;
    ImageView nextBtn;
    ImageView previosBtn;
    ImageView playBtn;
    SeekBar seekBar;


    private double startTime = 0;
    private double finalTime = 0;
    private Handler myHandler = new Handler();

    int recentIndex;
    boolean started = MainActivity.started;
    int isPlaying ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.selected_music);
        music_name = (TextView)this.findViewById(R.id.music_name);
        music_singer = (TextView)this.findViewById(R.id.music_singer);
        tx1 = (TextView)this.findViewById(R.id.tx1);
        tx2 = (TextView)this.findViewById(R.id.tx2);
        playBtn = (ImageView)findViewById(R.id.playBtn);
        nextBtn = (ImageView)findViewById(R.id.next);
        previosBtn = (ImageView)findViewById(R.id.previos);

        recentIndex = getIntent().getIntExtra("recentIndex", 0);
        music_name.setText(MainActivity.musics.get(recentIndex).getmName());
        music_singer.setText(MainActivity.musics.get(recentIndex).getmArtist());
        seekBar = (SeekBar)findViewById(R.id.seek_bar);
        setSeekBar();

        MainActivity.mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {// Xet su kien khi chay xong bai nhac.
            @Override
            public void onCompletion(MediaPlayer mp) {
                if (recentIndex < MainActivity.musics.size() - 1) {
                    turnOnMusic(++recentIndex);
                } else {
                    recentIndex = 0;
                    turnOnMusic(0);
                }
            }
        });

        if(MainActivity.mediaPlayer.isPlaying()){
            isPlaying = 1;
            playBtn.setBackgroundResource(R.drawable.img_btn_pause_pressed);
        }
        else{
            isPlaying = 0;
            playBtn.setBackgroundResource(R.drawable.img_btn_play_pressed);
        }

        playBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) { // Xet su kien khi bam nut play/pause.
                if(MainActivity.mediaPlayer.isPlaying()){
                    MainActivity.mediaPlayer.pause();
                    playBtn.setBackgroundResource(R.drawable.img_btn_play_pressed);
                    isPlaying = 0;
                }
                else{
                    if(started){
                        MainActivity.mediaPlayer.start();
                        playBtn.setBackgroundResource(R.drawable.img_btn_pause_pressed);
                        MainActivity.mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                            public void onCompletion(MediaPlayer mp) {
                                if (recentIndex < MainActivity.musics.size() - 1) {
                                    turnOnMusic(++recentIndex);
                                } else {
                                    recentIndex = 0;
                                    turnOnMusic(0);
                                }
                            }
                        });
                    }
                    else{
                        turnOnMusic(recentIndex);
                    }
                    isPlaying = 1;
                }
            }
        });

        nextBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(MainActivity.mediaPlayer.isPlaying()){
                    MainActivity.mediaPlayer.stop();
                }
                turnOnMusic(++recentIndex);
            }
        });

        previosBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(MainActivity.mediaPlayer.isPlaying()){
                    MainActivity.mediaPlayer.stop();
                }
                if(recentIndex == 0){
                    recentIndex = MainActivity.musics.size()-1;
                    turnOnMusic(recentIndex);
                }
                else {
                    turnOnMusic(--recentIndex);
                }
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_selected_music, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK ) {
            Intent i = new Intent(SelectedMusic.this,MainActivity.class);
            i.putExtra("isPlaying",isPlaying);
            i.putExtra("key",recentIndex);
            startActivity(i);
        }
        return super.onKeyDown(keyCode, event);
    }

    private void turnOnMusic(final int position){
        music_name.setText(MainActivity.musics.get(position).getmName());
        music_singer.setText(MainActivity.musics.get(position).getmArtist());
        playBtn.setBackgroundResource(R.drawable.img_btn_pause_pressed);
        MainActivity.mediaPlayer = MediaPlayer.create(SelectedMusic.this, Uri.parse(MainActivity.musics.get(position).getmPath()));
        MainActivity.mediaPlayer.start();
        setSeekBar();
        MainActivity.started = true;
        MainActivity.mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            public void onCompletion(MediaPlayer mp) {
                if (position < MainActivity.musics.size() - 1) {
                    turnOnMusic(position + 1);
                    recentIndex++;
                } else {
                    recentIndex = 0;
                    turnOnMusic(0);
                }
            }
        });
    }

    private Runnable UpdateSongTime = new Runnable() {
        public void run() {
            startTime = MainActivity.mediaPlayer.getCurrentPosition();
            tx1.setText(String.format("%d : %d",

                            TimeUnit.MILLISECONDS.toMinutes((long) startTime),
                            TimeUnit.MILLISECONDS.toSeconds((long) startTime) -
                                    TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.
                                            toMinutes((long) startTime)))
            );
            seekBar.setProgress((int) startTime);
            myHandler.postDelayed(this, 100);
        }
    };

    private void setSeekBar(){
        seekBar.setClickable(false);
        finalTime = MainActivity.mediaPlayer.getDuration();
        startTime = MainActivity.mediaPlayer.getCurrentPosition();
        seekBar.setMax((int)finalTime);
        tx2.setText(String.format("%d : %d",
                        TimeUnit.MILLISECONDS.toMinutes((long) finalTime),
                        TimeUnit.MILLISECONDS.toSeconds((long) finalTime) -
                                TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes((long) finalTime)))
        );

        tx1.setText(String.format("%d : %d",
                        TimeUnit.MILLISECONDS.toMinutes((long) startTime),
                        TimeUnit.MILLISECONDS.toSeconds((long) startTime) -
                                TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes((long) startTime)))
        );
        seekBar.setProgress((int)startTime);
        myHandler.postDelayed(UpdateSongTime,100);
    }
}
