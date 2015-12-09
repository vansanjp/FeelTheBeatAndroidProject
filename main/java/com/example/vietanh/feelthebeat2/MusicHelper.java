package com.example.vietanh.feelthebeat2;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by vietanh on 12/3/15.
 */
public class MusicHelper extends SQLiteOpenHelper {
    private static final String DB_NAME = "MyMusicDB.db";
    private static final int Version = 1;
    public SQLiteDatabase database;


    public static final String TB_NAME = "MyMusics";

    public static final String CL_Name = "Name";
    public static final String CL_Artist = "Artist";
    public static final String CL_Path = "Path";
    public static final String CL_Duration = "Duration";
    public static final String CL_Favorite = "Favorite";
    public static final String CL_LastListen = "LastListen";
    public static final String CL_ListenTurn = "Turn";

    public MusicHelper (Context context){
        super(context,DB_NAME,null,Version);
        this.database = getWritableDatabase();
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String sql1 = "CREATE TABLE  "+TB_NAME+"("
                +CL_Name+" TEXT PRIMARY KEY,"
                +CL_Artist+" TEXT NOT NULL,"
                +CL_Duration+" TEXT NOT NULL,"
                +CL_Path+" TEXT NOT NULL,"
                +CL_Favorite+" INTEGER,"
                +CL_ListenTurn+" INTEGER,"
                +CL_LastListen+" INTEGER)";
        db.execSQL(sql1);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    public void INSERT_Music(String mName, String mArtist,String mDuration,String mPath){
        ContentValues cv = new ContentValues();
        cv.put(CL_Name,mName);
        cv.put(CL_Artist,mArtist);
        cv.put(CL_Duration,mDuration);
        cv.put(CL_Path,mPath);
        cv.put(CL_Favorite,0);
        cv.put(CL_ListenTurn,0);
        cv.put(CL_LastListen,100);
        database.insert(TB_NAME,null,cv);
    }


    public void CloseBD() {
        if (database != null && database.isOpen())
            database.close();
    }

    public Cursor SELECTSQL(String sql) {
        return database.rawQuery(sql, null);
    }

    public void getAllSongs(ArrayList<MusicListItem> musics){
        String sql = "select * from "+this.TB_NAME;
        Cursor cursor = this.SELECTSQL(sql);
        while (cursor.moveToNext()){
            String tempName = cursor.getString(cursor.getColumnIndex(this.CL_Name));
            String tempDuration = cursor.getString(cursor.getColumnIndex(this.CL_Duration));
            String tempArtist = cursor.getString(cursor.getColumnIndex(this.CL_Artist));
            String tempPath = cursor.getString(cursor.getColumnIndex(this.CL_Path));
            int tempFavorite = cursor.getInt(cursor.getColumnIndex(this.CL_Favorite));
            int tempTurn = cursor.getInt(cursor.getColumnIndex(this.CL_ListenTurn));
            int tempLast = cursor.getInt(cursor.getColumnIndex(this.CL_LastListen));
            MusicListItem temp = new MusicListItem(tempName,tempDuration,tempArtist,tempPath,tempFavorite,tempTurn,tempLast);
            musics.add(temp);
        }
    }

    public void setNewTurn(String name,int newTurn) {
        ContentValues values = new ContentValues();
        values.put(CL_ListenTurn,newTurn);
        database.update(TB_NAME, values, "Name = ?", new String[]{name});
    }

    public int setNewFavorite(String name,int favorite){
        ContentValues values = new ContentValues();
        values.put(CL_Favorite, favorite);
        return database.update(TB_NAME,values,"Name = ?",new String[]{name});
    }

    public int setNewLast(String name,int last){
        ContentValues values = new ContentValues();
        values.put(CL_LastListen,last);
        return database.update(TB_NAME,values,"Name = ?",new String[]{name});
    }

    public void setAllLast(){
        String sql = "select * from "+this.TB_NAME;
        Cursor cursor = this.SELECTSQL(sql);
        while (cursor.moveToNext()){
            String tempName = cursor.getString(cursor.getColumnIndex(this.CL_Name));
            int tempLast = cursor.getInt(cursor.getColumnIndex(this.CL_LastListen));
            this.setNewLast(tempName,tempLast+1);
        }
    }

    public void getLastList(List<MusicListItem> musics){
        String sql = "SELECT * FROM "+this.TB_NAME+" WHERE "+CL_LastListen+" < 10 ORDER BY "+CL_LastListen+" ASC";
        Cursor cursor = this.SELECTSQL(sql);
        while (cursor.moveToNext()){
            String tempName = cursor.getString(cursor.getColumnIndex(this.CL_Name));
            String tempDuration = cursor.getString(cursor.getColumnIndex(this.CL_Duration));
            String tempArtist = cursor.getString(cursor.getColumnIndex(this.CL_Artist));
            String tempPath = cursor.getString(cursor.getColumnIndex(this.CL_Path));
            int tempFavorite = cursor.getInt(cursor.getColumnIndex(this.CL_Favorite));
            int tempTurn = cursor.getInt(cursor.getColumnIndex(this.CL_ListenTurn));
            int tempLast = cursor.getInt(cursor.getColumnIndex(this.CL_LastListen));
            MusicListItem temp = new MusicListItem(tempName,tempDuration,tempArtist,tempPath,tempFavorite,tempTurn,tempLast);
            musics.add(temp);
        }
    }

    public void getTurnList(List<MusicListItem> musics){
        String sql = "SELECT * FROM "+this.TB_NAME+" WHERE "+CL_ListenTurn+" > 0 ORDER BY "+CL_ListenTurn+" DESC";
        Cursor cursor = this.SELECTSQL(sql);
        while (cursor.moveToNext()){
            String tempName = cursor.getString(cursor.getColumnIndex(this.CL_Name));
            String tempDuration = cursor.getString(cursor.getColumnIndex(this.CL_Duration));
            String tempArtist = cursor.getString(cursor.getColumnIndex(this.CL_Artist));
            String tempPath = cursor.getString(cursor.getColumnIndex(this.CL_Path));
            int tempFavorite = cursor.getInt(cursor.getColumnIndex(this.CL_Favorite));
            int tempTurn = cursor.getInt(cursor.getColumnIndex(this.CL_ListenTurn));
            int tempLast = cursor.getInt(cursor.getColumnIndex(this.CL_LastListen));
            MusicListItem temp = new MusicListItem(tempName,tempDuration,tempArtist,tempPath,tempFavorite,tempTurn,tempLast);
            musics.add(temp);
        }
    }

    public void getFavoriteList(List<MusicListItem> musics){
        String sql = "SELECT * FROM "+this.TB_NAME+" WHERE "+CL_Favorite+" = 1";
        Cursor cursor = this.SELECTSQL(sql);
        while (cursor.moveToNext()){
            String tempName = cursor.getString(cursor.getColumnIndex(this.CL_Name));
            String tempDuration = cursor.getString(cursor.getColumnIndex(this.CL_Duration));
            String tempArtist = cursor.getString(cursor.getColumnIndex(this.CL_Artist));
            String tempPath = cursor.getString(cursor.getColumnIndex(this.CL_Path));
            int tempFavorite = cursor.getInt(cursor.getColumnIndex(this.CL_Favorite));
            int tempTurn = cursor.getInt(cursor.getColumnIndex(this.CL_ListenTurn));
            int tempLast = cursor.getInt(cursor.getColumnIndex(this.CL_LastListen));
            MusicListItem temp = new MusicListItem(tempName,tempDuration,tempArtist,tempPath,tempFavorite,tempTurn,tempLast);
            musics.add(temp);
        }
    }

    public void deleteTable(){
        database.execSQL("DROP TABLE IF EXISTS " + TB_NAME);
        onCreate(database);
    }
    public int DELETE_Song(String name) {
        return database.delete(TB_NAME,CL_Name+" = ?",new String[]{name});
    }
}
