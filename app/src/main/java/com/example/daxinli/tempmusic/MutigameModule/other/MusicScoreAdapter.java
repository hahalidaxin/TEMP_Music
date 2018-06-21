package com.example.daxinli.tempmusic.MutigameModule.other;

import android.app.Activity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.daxinli.tempmusic.R;

import java.util.List;

/**
 * Created by DaxinLi on 2018/6/21.
 */

public class MusicScoreAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{
    private List<MusicScoreItem> mList;
    private Activity activity;
    private static final String TAG = "SettingsAdapter";

    public int[] DID_medal = {R.drawable.medal1,R.drawable.medal2,
            R.drawable.medal3,R.drawable.medal4};

    public MusicScoreAdapter(Activity activity,List<MusicScoreItem> list) {
        this.activity = activity;
        this.mList = list;
    }
    class MyHolder extends RecyclerView.ViewHolder {
        TextView rank;
        ImageView medal;
        TextView musicName;
        TextView musicScore;

        public MyHolder(View view) {
            super(view);
            this.rank = (TextView) view.findViewById(R.id.rank_listSorted);
            this.medal = (ImageView) view.findViewById(R.id.medal_image_listSorted);
            this.musicName = (TextView) view.findViewById(R.id.text_musicname_listSorted);
            this.musicScore = (TextView) view.findViewById(R.id.scoretoshow_listSorted);
        }
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        //根据不同的ViewType获取不同的recycler布局
        final MyHolder firstViewHolder =
                new MyHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_musicsorted,parent,false));
        return firstViewHolder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        MusicScoreItem musicItem = mList.get(position);
        MyHolder fholder = (MyHolder) holder;
        //对显示资源记性加载
        int rank = musicItem.getRank();
        if(rank<=2) {
            Glide.with(activity).load(DID_medal[rank]).into(fholder.medal);
            fholder.rank.setText("");
        } else {
            Glide.with(activity).load(DID_medal[3]).into(fholder.medal);
            fholder.rank.setText(Integer.toString(rank+1));
        }
        fholder.musicName.setText(musicItem.getMusicName());
        fholder.musicScore.setText(Integer.toString(musicItem.getMusicScore()));
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }
}
