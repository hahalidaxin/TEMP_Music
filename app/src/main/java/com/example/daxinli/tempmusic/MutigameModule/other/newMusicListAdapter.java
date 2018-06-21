package com.example.daxinli.tempmusic.MutigameModule.other;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.daxinli.tempmusic.R;
import com.example.daxinli.tempmusic.musicTouch.HomeActivity;

import java.util.List;
import java.util.Random;

/**
 * Created by DaxinLi on 2018/6/21.
 */

public class newMusicListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{
    private List<newMusicListItem> mList;
    private Activity activity;
    private static final String TAG = "SettingsAdapter";

    public int[] DID_medal = {R.drawable.medal1,R.drawable.medal2,
            R.drawable.medal3,R.drawable.medal4};

    public newMusicListAdapter(Activity activity,List<newMusicListItem> list) {
        this.activity = activity;
        this.mList = list;
    }
    class MyHolder extends RecyclerView.ViewHolder {
        TextView rank;
        TextView musicName;
        ImageView slidegBar;
        View view;

        public MyHolder(View view) {
            super(view);
            this.view = view;
            this.rank = (TextView) view.findViewById(R.id.rank_newlistSorted);
            this.musicName = (TextView) view.findViewById(R.id.musicname_newlistSorted);
            this.slidegBar = (ImageView) view.findViewById(R.id.slidebar_newlistSorted);
        }
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        //根据不同的ViewType获取不同的recycler布局
        final MyHolder firstViewHolder =
                new MyHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_newmusiclist,parent,false));
        firstViewHolder.view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(activity instanceof HomeActivity) {
                    Intent intent = new Intent();
                    intent.putExtra("type",0);
                    intent.putExtra("musicName",firstViewHolder.musicName.getText().toString()+".txt");
                    ((HomeActivity)activity).onActvitiyTrans(intent);
                }
            }
        });
        return firstViewHolder;
    }

    int[] RID_slideBar = {R.drawable.pic_musicitemg1,R.drawable.pic_musicitemg2,R.drawable.pic_musicitemg3,
        R.drawable.pic_musicitemg4,R.drawable.pic_musicitemg5,R.drawable.pic_musicitemg6};
    Random random = new Random();
    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        newMusicListItem musicItem = mList.get(position);
        MyHolder fholder = (MyHolder) holder;
        //对显示资源记性加载
        int rx = random.nextInt(6);
        Glide.with(activity).load(RID_slideBar[rx]).into(fholder.slidegBar);
        fholder.rank.setText(musicItem.getRank());
        String name = musicItem.getMusicName();
        int x = name.indexOf('.');
        fholder.musicName.setText(name.substring(0,x));
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }
}
