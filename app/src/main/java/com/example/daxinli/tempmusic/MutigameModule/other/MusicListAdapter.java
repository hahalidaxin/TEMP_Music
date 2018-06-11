package com.example.daxinli.tempmusic.MutigameModule.other;

/**
 * Created by Daxin Li on 2018/6/10.
 */

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.daxinli.tempmusic.MutigameModule.Activity.gameplay.ChooseMusicActivity;
import com.example.daxinli.tempmusic.R;

import java.util.List;
/*
 * Created by Daxin Li on 2018/3/8.
*/

public class MusicListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{
    private List<MusicItem> mList;
    private ChooseMusicActivity activity;
    private static final String TAG = "SettingsAdapter";
    private ChooseMusicActivity chooseMusicActivity;
    public MusicListAdapter(ChooseMusicActivity activity,List<MusicItem> list) {
        this.activity = activity;
        this.mList = list;
        this.chooseMusicActivity = chooseMusicActivity;
    }
    static class FirstViewHolder extends RecyclerView.ViewHolder {
        TextView imageView;
        TextView musicName;
        TextView musicInfo;
        View view;
        public FirstViewHolder(View view) {
            super(view);
            this.view = view;
            this.imageView = (TextView) view.findViewById(R.id.image_musicinfo);
            this.musicName = (TextView) view.findViewById(R.id.text_musicname);
            this.musicInfo = (TextView) view.findViewById(R.id.text_musicinfo);
        }
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        //根据不同的ViewType获取不同的recycler布局
        final FirstViewHolder firstViewHolder = new FirstViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.musicinfoitem,parent,false));
        firstViewHolder.view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO: 2018/6/10 activity切换
                chooseMusicActivity.sendMessage(firstViewHolder.musicName.getText().toString()+
                                firstViewHolder.musicInfo.getText().toString());
            }
        });
        return firstViewHolder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        MusicItem musicItem = mList.get(position);
        FirstViewHolder fholder = (FirstViewHolder) holder;
        //对显示资源记性加载
        fholder.imageView.setText(musicItem.getImageRes());
        fholder.musicName.setText(musicItem.getMusicName());
        fholder.musicInfo.setText(musicItem.getMusicInfo());
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }
}
