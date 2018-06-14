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

import com.bumptech.glide.Glide;
import com.example.daxinli.tempmusic.MutigameModule.Activity.gameplay.ChooseMusicActivity;
import com.example.daxinli.tempmusic.R;

import java.util.ArrayList;
import java.util.List;
/*
 * Created by Daxin Li on 2018/3/8.
*/

public class MusicListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{
    private List<MusicItem> mList;
    private ChooseMusicActivity activity;
    private static final String TAG = "SettingsAdapter";
    private ChooseMusicActivity chooseMusicActivity;

    public int[] DID_iconInstru = {R.drawable.icon_instru1_p1,R.drawable.icon_instru2_p1,
            R.drawable.icon_instru3_p1,R.drawable.icon_instru4_p1};

    public MusicListAdapter(ChooseMusicActivity activity,List<MusicItem> list) {
        this.activity = activity;
        this.mList = list;
        this.chooseMusicActivity = activity;
    }
    static class FirstViewHolder extends RecyclerView.ViewHolder {
        private int[] RID_iconInstru = {R.id.musicItem_iconInstru1,R.id.musicItem_iconInstru2,
                R.id.musicItem_iconInstru3,R.id.musicItem_iconInstru4};
        TextView imageView;
        TextView musicName;
        TextView musicInfo;
        TextView instruNum;
        ImageView [] iconInstru = new ImageView[4];
        View view;
        public FirstViewHolder(View view) {
            super(view);
            this.view = view;
            this.imageView = (TextView) view.findViewById(R.id.image_musicinfo);
            this.musicName = (TextView) view.findViewById(R.id.text_musicname);
            this.musicInfo = (TextView) view.findViewById(R.id.text_musicinfo);
            this.instruNum = (TextView) view.findViewById(R.id.text_musicInstruNum);
            for(int i=0;i<4;i++) iconInstru[i] = (ImageView) view.findViewById(RID_iconInstru[i]);
        }
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        //根据不同的ViewType获取不同的recycler布局
        final FirstViewHolder firstViewHolder = new FirstViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.musicinfoitem,parent,false));
        firstViewHolder.view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String filename = firstViewHolder.musicName.getText().toString()+"-"+firstViewHolder.musicInfo.getText().toString();
                chooseMusicActivity.sendMessage(filename);
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
        ArrayList<Integer> ls = musicItem.getInstrus();
        //fholder.instruNum.setText("可用乐器数："+Integer.toString(ls.size()));
        for(int i=0;i<4;i++) {
            if(i<ls.size())
                Glide.with(activity).load(DID_iconInstru[i]).into(fholder.iconInstru[i]);
            else
                fholder.iconInstru[i].setVisibility(View.INVISIBLE);
        }
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }
}
