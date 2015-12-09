package com.example.vietanh.feelthebeat2;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.media.MediaPlayer;
import android.net.Uri;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.KeyEvent;
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
    public static ArrayList<MusicListItem> musics = new ArrayList<MusicListItem>();
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
        isPlaying = getIntent().getIntExtra("isPlaying",0);

        list = (ListView)findViewById(R.id.list);
        if(musics.size() == 0){
            helper = new MusicHelper(MainActivity.this);
            GetListFile.insertMusic(MainActivity.this, helper);
            musics = new ArrayList<MusicListItem>();
            helper.getAllSongs(musics);
            if(isPlaying != 1)
                mediaPlayer = MediaPlayer.create(MainActivity.this,Uri.parse(musics.get(recentIndex).getmPath()));
            helper.CloseBD();
        }
        adapter = new ListMusicAdapter(MainActivity.this,R.layout.music_row,musics);
        list.setAdapter(adapter);
        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (position == recentIndex) ;
                else {
                    if (mediaPlayer.isPlaying())
                        mediaPlayer.stop();
                    recentIndex = position;
                    turnOnMusic(recentIndex);
                }
            }
        });
        list.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                recentIndex = position;
                Intent i = new Intent(MainActivity.this,Setting.class);
                i.putExtra("key",recentIndex);
                startActivity(i);
                return true;
            }
        });

        name = (TextView)findViewById(R.id.name);
        String music_name = getIntent().getStringExtra("name");
        if(music_name != null)
            name.setText(music_name);
        else
            name.setText(musics.get(recentIndex).getmName());

        artist = (TextView)findViewById(R.id.artist);
        String music_singer = getIntent().getStringExtra("artist");
        if(music_singer != null)
            artist.setText(music_singer);
        else
            artist.setText(musics.get(recentIndex).getmArtist());

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
                    if(started){
                        mediaPlayer.start();
                        playBtn.setBackgroundResource(R.drawable.img_btn_pause_pressed);
                        mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                            public void onCompletion(MediaPlayer mp) {
                                if (recentIndex < musics.size() - 1) {
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
                if(recentIndex >= musics.size()-1)
                    recentIndex = -1;
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
                if(recentIndex <= 0){
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
                i.putExtra("recentIndex",recentIndex);
                i.putExtra("name",name.getText().toString());
                i.putExtra("artist",artist.getText().toString());
                startActivity(i);
            }
        });

        LocalBroadcastManager.getInstance(MainActivity.this).registerReceiver(receiver,new IntentFilter("abc"));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.most_played) {
            /*if(mediaPlayer.isPlaying()) {
                mediaPlayer.stop();
            }*/
            helper = new MusicHelper(MainActivity.this);
            musics = new ArrayList<MusicListItem>();
            helper.getTurnList(musics);
            adapter = new ListMusicAdapter(MainActivity.this,R.layout.music_row,musics);
            list.setAdapter(adapter);
            helper.CloseBD();
            mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                @Override
                public void onCompletion(MediaPlayer mp) {
                    recentIndex = 0;
                    turnOnMusic(recentIndex);
                }
            });
        }
        if(id == R.id.favorite){
            helper = new MusicHelper(MainActivity.this);
            musics = new ArrayList<MusicListItem>();
            helper.getFavoriteList(musics);
            adapter = new ListMusicAdapter(MainActivity.this,R.layout.music_row,musics);
            list.setAdapter(adapter);
            helper.CloseBD();
            mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                @Override
                public void onCompletion(MediaPlayer mp) {
                    recentIndex = 0;
                    turnOnMusic(recentIndex);
                }
            });
        }

        if(id == R.id.last_listen){
            helper = new MusicHelper(MainActivity.this);
            musics = new ArrayList<MusicListItem>();
            helper.getLastList(musics);
            adapter = new ListMusicAdapter(MainActivity.this,R.layout.music_row,musics);
            list.setAdapter(adapter);
            helper.CloseBD();
            mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                @Override
                public void onCompletion(MediaPlayer mp) {
                    recentIndex = 0;
                    turnOnMusic(recentIndex);
                }
            });
        }

        if(id == R.id.reset){
            helper = new MusicHelper(MainActivity.this);
            helper.deleteTable();
            GetListFile.insertMusic(MainActivity.this, helper);
            musics = new ArrayList<MusicListItem>();
            helper.getAllSongs(musics);
            adapter = new ListMusicAdapter(MainActivity.this,R.layout.music_row,musics);
            list.setAdapter(adapter);
            helper.CloseBD();
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if(keyCode == KeyEvent.KEYCODE_BACK){
            helper = new MusicHelper(MainActivity.this);
            musics = new ArrayList<MusicListItem>();
            helper.getAllSongs(musics);
            helper.CloseBD();
            adapter = new ListMusicAdapter(MainActivity.this,R.layout.music_row,musics);
            list.setAdapter(adapter);
            mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                @Override
                public void onCompletion(MediaPlayer mp) {
                    recentIndex = 0;
                    turnOnMusic(recentIndex);
                }
            });
            artist.setText(musics.get(recentIndex).getmArtist());
            name.setText(musics.get(recentIndex).getmName());
        }
        return true;
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
        helper.setNewLast(name, 0);
        helper.setAllLast();
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

    BroadcastReceiver receiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            helper = new MusicHelper(MainActivity.this);
            musics = new ArrayList<MusicListItem>();
            helper.getAllSongs(musics);
            helper.CloseBD();
            adapter = new ListMusicAdapter(MainActivity.this,R.layout.music_row,musics);
            list.setAdapter(adapter);
            mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                @Override
                public void onCompletion(MediaPlayer mp) {
                    recentIndex = 0;
                    turnOnMusic(recentIndex);
                }
            });
            artist.setText(musics.get(recentIndex).getmArtist());
            name.setText(musics.get(recentIndex).getmName());
        }
    };
}
