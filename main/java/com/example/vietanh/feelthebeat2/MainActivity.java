package com.example.vietanh.feelthebeat2;

import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    public static MediaPlayer mediaPlayer ;
    public static ArrayList<MusicListItem> musics;
    public static ArrayList<MusicListItem> turnMusics;
    public static boolean started = false;

    private ListView list;
    private ListMusicAdapter adapter;
    private MusicHelper helper;

    private  TextView name;
    private TextView artist;
    private ImageView playBtn;
    private ImageView nextBtn;
    private ImageView previosBtn;
    private ImageView musicInoBtn;

    private int recentIndex = 0;
    private int isPlaying;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        recentIndex = getIntent().getIntExtra("key",0);

        list = (ListView)findViewById(R.id.list);
        helper = new MusicHelper(MainActivity.this);
        GetListFile.insertMusic(MainActivity.this,helper);
        musics = new ArrayList<MusicListItem>();
        helper.getAllSongs(musics);
        mediaPlayer = MediaPlayer.create(MainActivity.this,Uri.parse(musics.get(recentIndex).getmPath()));
        helper.CloseBD();
        adapter = new ListMusicAdapter(MainActivity.this,R.layout.music_row,musics);
        list.setAdapter(adapter);
        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if(position == recentIndex);
                else{
                    if(mediaPlayer.isPlaying())
                        mediaPlayer.stop();
                    recentIndex = position;
                    turnOnMusic(recentIndex);
                }
            }
        });

        name = (TextView)findViewById(R.id.name);
        name.setText(musics.get(recentIndex).getmName());

        artist = (TextView)findViewById(R.id.artist);
        artist.setText(musics.get(recentIndex).getmArtist());

        isPlaying = getIntent().getIntExtra("isPlaying",0);
        playBtn = (ImageView)findViewById(R.id.play);
        if(isPlaying == 1)
            playBtn.setBackgroundResource(R.drawable.img_btn_pause_pressed);
        else
            playBtn.setBackgroundResource(R.drawable.img_btn_play_pressed);
        playBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mediaPlayer.isPlaying()){
                    mediaPlayer.pause();
                    playBtn.setBackgroundResource(R.drawable.img_btn_play_pressed);
                }
                else{
                    if(started)
                        mediaPlayer.start();
                    else{
                        turnOnMusic(recentIndex);
                    }
                    playBtn.setBackgroundResource(R.drawable.img_btn_pause_pressed);
                }
            }
        });

        nextBtn = (ImageView)findViewById(R.id.next_main);
        nextBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mediaPlayer.isPlaying()){
                    mediaPlayer.stop();
                }
                turnOnMusic(++recentIndex);
            }
        });

        previosBtn = (ImageView)findViewById(R.id.previos_main);
        previosBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mediaPlayer.isPlaying()){
                    mediaPlayer.stop();
                }
                if(recentIndex == 0){
                    recentIndex = musics.size()-1;
                    turnOnMusic(recentIndex);
                }
                else {
                    turnOnMusic(--recentIndex);
                }
            }
        });

        musicInoBtn = (ImageView)findViewById(R.id.music_info);
        musicInoBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(MainActivity.this,SelectedMusic.class);
                i.putExtra("recentKey",recentIndex);
                startActivity(i);
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
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
            helper = new MusicHelper(MainActivity.this);
            musics = new ArrayList<MusicListItem>();
            helper.setTurnList(musics);
            adapter = new ListMusicAdapter(MainActivity.this,R.layout.music_row,musics);
            list.setAdapter(adapter);
            helper.CloseBD();
        }

        return super.onOptionsItemSelected(item);
    }

    private void turnOnMusic(final int position) {
        name.setText(musics.get(position).getmName());
        artist.setText(musics.get(position).getmArtist());
        playBtn.setBackgroundResource(R.drawable.img_btn_pause_pressed);
        mediaPlayer = MediaPlayer.create(MainActivity.this, Uri.parse(musics.get(position).getmPath()));
        mediaPlayer.start();
        helper = new MusicHelper(MainActivity.this);
        String name = musics.get(position).getmName();
        int newTurn = musics.get(position).getmTurn()+1;
        helper.setNewTurn(name,newTurn);
        helper.CloseBD();
        started = true;
        mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            public void onCompletion(MediaPlayer mp) {
                if (position < musics.size() - 1) {
                    turnOnMusic(position + 1);
                    recentIndex++;
                } else {
                    recentIndex = 0;
                    turnOnMusic(0);
                }
            }
        });
    }
}
