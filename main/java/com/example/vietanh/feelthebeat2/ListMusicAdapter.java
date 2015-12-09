package com.example.vietanh.feelthebeat2;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import java.util.ArrayList;

public class ListMusicAdapter extends ArrayAdapter {

    ArrayList<MusicListItem> arrayMusics = null;
    Activity context = null;
    int layoutID;

    class MyViewHolder {
        TextView name;
        TextView duration;
        TextView artist;
        ImageView check;

        MyViewHolder (View view) {
            name = (TextView) view.findViewById(R.id.name);
            duration = (TextView) view.findViewById(R.id.duration);
            artist = (TextView) view.findViewById(R.id.artist);
            check = (ImageView) view.findViewById(R.id.icon);
        }
    }

    public ListMusicAdapter(Activity context, int layoutID, ArrayList<MusicListItem> arrayMusics) {
        super(context , layoutID , arrayMusics);
        this.context = context;
        this.layoutID   = layoutID;
        this.arrayMusics = arrayMusics;
    }

    public View getView (final int position, View convertView, ViewGroup parent) {
        final MyViewHolder holder;

        if (convertView == null) {
            LayoutInflater inflater = context.getLayoutInflater();
            convertView = inflater.inflate(layoutID , null);
            holder = new MyViewHolder(convertView);
            convertView.setTag(holder);
        } else {
            holder = (MyViewHolder) convertView.getTag();
        }

        final String name = arrayMusics.get(position).getmName();
        final String duration = arrayMusics.get(position).getmDuration();
        final String artist = arrayMusics.get(position).getmArtist();

        holder.name.setText(name);
        holder.duration.setText("" + StorageUtil.convertDuration(duration));
        holder.artist.setText(artist);
        holder.check.setBackgroundResource(R.drawable.double_sol);

        return convertView;
    }

}
