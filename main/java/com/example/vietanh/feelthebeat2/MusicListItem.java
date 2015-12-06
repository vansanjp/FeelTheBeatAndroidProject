package com.example.vietanh.feelthebeat2;

public class MusicListItem {

    private String mName;
    private String mDuration;
    private String mArtist;
    private boolean mCheck;
    private String mPath;
    private int mFavorite;
    private int mTurn;
    private int mLastListen;

    public MusicListItem(String mName, String mDuration, String mArtist, String mPath,int mFavorite,int mTurn,int mLastListen) {
        this.mName = mName;
        this.mDuration = mDuration;
        this.mArtist = mArtist;
        mCheck = false;
        this.mPath = mPath;
        this.mFavorite = mFavorite;
        this.mTurn = mTurn;
        this.mLastListen = mLastListen;
    }

    public String getmName() {
        return mName;
    }

    public String getmDuration() {
        return mDuration;
    }

    public String getmArtist() {
        return mArtist;
    }

    public String getmPath() {
        return mPath;
    }

    public void setmFavorite(int mFavorite){
        this.mFavorite = mFavorite;
    }
    public int getmFavorite(){
        return mFavorite;
    }

    public void setmTurn(int mTurn){
        this.mTurn = mTurn;
    }
    public int getmTurn(){
        return mTurn;
    }

    public void setmLastListen(int mLastListen){
        this.mLastListen = mLastListen;
    }
    public int getmLastListen(){
        return mLastListen;
    }

}
