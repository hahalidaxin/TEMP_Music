package com.example.daxinli.tempmusic.MutigameModule.other;

/**
 * Created by Daxin Li on 2018/6/10.
 */

import android.app.Activity;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.daxinli.tempmusic.MutigameModule.Activity.gameplay.ChooseMusicActivity;
import com.example.daxinli.tempmusic.R;
import com.example.daxinli.tempmusic.musicTouch.HomeActivity;
import com.example.daxinli.tempmusic.musicTouch.SingleChooseActivity;

import java.util.ArrayList;
import java.util.List;
/*
 * Created by Daxin Li on 2018/3/8.
*/

public class MusicListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{
    private List<MusicItem> mList;
    private Activity activity;
    private static final String TAG = "SettingsAdapter";

    public int[] DID_iconInstru = {R.drawable.icon_instru1_p1,R.drawable.icon_instru2_p1,
            R.drawable.icon_instru3_p1,R.drawable.icon_instru4_p1};

    public MusicListAdapter(Activity activity,List<MusicItem> list) {
        this.activity = activity;
        this.mList = list;
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
                if(activity instanceof ChooseMusicActivity) {
                    ((ChooseMusicActivity)activity).sendMessage(firstViewHolder.musicName.getText().toString());
                } else if(activity instanceof SingleChooseActivity) {
                    ((SingleChooseActivity)activity).onActivityTrans(firstViewHolder.musicName.getText().toString());
                } else  if(activity instanceof HomeActivity) {
                    Intent intent = new Intent();
                    intent.putExtra("type",0);
                    intent.putExtra("musicName",firstViewHolder.musicName.getText().toString());
                    ((HomeActivity)activity).onActvitiyTrans(intent);
                }
            }
        });
        return firstViewHolder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        MusicItem musicItem = mList.get(position);
        FirstViewHolder fholder = (FirstViewHolder) holder;
        //对显示资源记性加载
        if(musicItem.getImageRes().length()!=0) {
            fholder.imageView.setText(musicItem.getImageRes());
        }
        if(musicItem.getMusicName().length()!=0) {
            String name = musicItem.getMusicName();
            fholder.musicName.setText(name);
        }
        if(musicItem.getMusicInfo().length()!=0)
            fholder.musicInfo.setText(musicItem.getMusicInfo());
        ArrayList<Integer> ls = musicItem.getInstrus();
        if(ls.size()!=0) {
            for (int i = 0; i < 4; i++) {
                if (i < ls.size())
                    Glide.with(activity).load(DID_iconInstru[i]).into(fholder.iconInstru[i]);
                else
                    fholder.iconInstru[i].setVisibility(View.INVISIBLE);
            }
        }
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }
}
