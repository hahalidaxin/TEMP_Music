package com.example.daxinli.tempmusic.util;

import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;

import com.example.daxinli.tempmusic.R;
import com.example.daxinli.tempmusic.constant.GameData;
import com.example.daxinli.tempmusic.musicTouch.SettingsActivity;

import java.util.List;

/**
 * Created by Daxin Li on 2018/3/8.
 */

public class SettingsAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{
    private List<SettingItem> mList;
    private SettingsActivity activity;
    private static final String TAG = "SettingsAdapter";

    static class FirstViewHolder extends RecyclerView.ViewHolder {
        ImageView imageView;
        TextView textView;
        View view;
        public FirstViewHolder(View view) {
            super(view);
            this.view = view;
            this.imageView = (ImageView) view.findViewById(R.id.image_setting);
            this.textView = (TextView) view.findViewById(R.id.text_setting);
        }
    }
    static class SecondViewHolder extends RecyclerView.ViewHolder {
        ImageView imageView;
        TextView themeView;
        TextView descripView;
        View view;
        public SecondViewHolder(View view) {
            super(view);
            this.view = view;
            this.imageView = (ImageView) view.findViewById(R.id.image_setting);
            this.themeView = (TextView) view.findViewById(R.id.themetext_setting);
            this.descripView = (TextView) view.findViewById(R.id.descriptext_setting);
        }
    }
    static class ThirdViewHolder extends RecyclerView.ViewHolder {
        ImageView imageView;
        TextView textView;
        Switch mswitch;
        View view;
        public ThirdViewHolder(View view) {
            super(view);
            this.view = view;
            this.imageView = (ImageView) view.findViewById(R.id.image_setting);
            this.textView = (TextView) view.findViewById(R.id.text_setting);
            this.mswitch  = (Switch) view.findViewById(R.id.switch_setting);
        }
    }
    public SettingsAdapter(SettingsActivity activity,List<SettingItem> List) {
        this.mList = List;
        this.activity = activity;
    }
    @Override
    public int getItemViewType(int position) {
        return mList.get(position).getType();
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        //根据不同的ViewType获取不同的recycler布局

        if(viewType==1) {
            final FirstViewHolder firstViewHolder = new FirstViewHolder(LayoutInflater.from(parent.getContext()).
                    inflate(R.layout.item_1, parent, false));
            firstViewHolder.view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(firstViewHolder.textView.getText().toString()=="帮助") {        //转到帮助页面
                        activity.TurnToActivity(1);
                    } else if(firstViewHolder.textView.getText().toString()=="关于游戏") {   //转到关于游戏页面
                        activity.TurnToActivity(2);
                    } else if(firstViewHolder.textView.getText().toString()=="登录") {
                        activity.TurnToActivity(3);
                    }
                }
            });
            return firstViewHolder;
        }
        else if(viewType==2) {
            SecondViewHolder secondViewHolder = new SecondViewHolder(LayoutInflater.from(parent.getContext()).
                    inflate(R.layout.item_2, parent, false));
            secondViewHolder.view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // TODO: 2018/3/8 游戏版本 暂时没有点击反应
                }
            });
            return secondViewHolder;
        }
        else if(viewType==3) {
            final ThirdViewHolder thirdViewHolder = new ThirdViewHolder(LayoutInflater.from(parent.getContext()).
                    inflate(R.layout.item_3, parent, false));
            thirdViewHolder.mswitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    GameData.GameEffect = isChecked;
                    if(GameData.GameEffect) Log.e(TAG, "onCheckedChanged: ");
                }
            });
            return thirdViewHolder;
        }
        return null;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        SettingItem settingitem = mList.get(position);
        switch(settingitem.getType()) {
            case 1:
                FirstViewHolder fholder = (FirstViewHolder) holder;
                fholder.imageView.setImageResource(settingitem.getImageId());
                fholder.textView.setText(settingitem.getText());
                break;
            case 2:
                SecondViewHolder sholder = (SecondViewHolder) holder;
                sholder.imageView.setImageResource(settingitem.getImageId());
                sholder.themeView.setText(settingitem.getText());
                sholder.descripView.setText(settingitem.getDesrip());
                break;
            case 3:
                ThirdViewHolder tholder = (ThirdViewHolder) holder;
                tholder.imageView.setImageResource(settingitem.getImageId());
                tholder.textView.setText(settingitem.getText());
                break;

        }
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }
}
