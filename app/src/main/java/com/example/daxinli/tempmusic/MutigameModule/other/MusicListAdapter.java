package com.example.daxinli.tempmusic.MutigameModule.other;

/**
 * Created by Daxin Li on 2018/6/10.
 */

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.daxinli.tempmusic.R;
import com.example.daxinli.tempmusic.musicTouch.SettingsActivity;
import com.example.daxinli.tempmusic.util.SettingItem;

import java.util.List;package com.example.daxinli.tempmusic.util;

 * Created by Daxin Li on 2018/3/8.
 */

public class MusicListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{
    private List<SettingItem> mList;
    private SettingsActivity activity;
    private static final String TAG = "SettingsAdapter";

    static class FirstViewHolder extends RecyclerView.ViewHolder {
        ImageView imageView;
        TextView musicName;
        TextView musicInfo;
        View view;
        public FirstViewHolder(View view) {
            super(view);
            this.view = view;
            this.imageView = (ImageView) view.findViewById(R.id.image_musicinfo);
            this.musicName = (TextView) view.findViewById(R.id.text_musicname);
            this.musicInfo = (TextView) view.findViewById(R.id.text_musicinfo);
        }
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        //根据不同的ViewType获取不同的recycler布局
        FirstViewHolder firstViewHolder = new FirstViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.musicinfoitem,parent,false));
        firstViewHolder.view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO: 2018/6/10 activity切换
            }
        }
        return firstViewHolder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        MusicItem settingitem = mList.get(position);
        FirstViewHolder fholder = FirstViewHolder) holder;
        //对显示资源记性加载
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }
}
