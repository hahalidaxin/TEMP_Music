package com.example.daxinli.tempmusic.musicTouch;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.AssetManager;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.view.animation.LayoutAnimationController;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.daxinli.tempmusic.MutigameModule.Activity.CreateAHomeActivity;
import com.example.daxinli.tempmusic.MutigameModule.Activity.EnterAHomeActivity;
import com.example.daxinli.tempmusic.MutigameModule.other.MusicItem;
import com.example.daxinli.tempmusic.MutigameModule.other.MusicListAdapter;
import com.example.daxinli.tempmusic.MutigameModule.other.MusicScoreAdapter;
import com.example.daxinli.tempmusic.MutigameModule.other.MusicScoreItem;
import com.example.daxinli.tempmusic.R;
import com.example.daxinli.tempmusic.util.SettingItem;
import com.example.daxinli.tempmusic.util.SettingsAdapter;
import com.example.daxinli.tempmusic.view.floatbackground.FloatBackLayout;
import com.example.daxinli.tempmusic.view.floatbackground.FloatBitmap;
import com.yalantis.phoenix.PullToRefreshView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Random;

import de.hdodenhof.circleimageview.CircleImageView;
import devlight.io.library.ntb.NavigationTabBar;

//游戏的主界面

public class HomeActivity extends BaseActivity implements View.OnClickListener{
    private static final String TAG = "HomeActivity";
    public int pageViewNum;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        initUI();
    }
    private void initUI() {
        final ViewPager viewPager = (ViewPager) findViewById(R.id.vp_horizontal_ntb);
        viewPager.setAdapter(new PagerAdapter() {
            public View[] views = new View[4];
            private int[] RID_views  = { R.layout.view_single_choose,R.layout.view_ranklist,
                    R.layout.view_mutigame,R.layout.view_settings};
            @Override
            public int getCount() {
                return 4;
            }

            @Override
            public boolean isViewFromObject(final View view, final Object object) {
                return view.equals(object);
            }

            @Override
            public void destroyItem(final View container, final int position, final Object object) {
                ((ViewPager) container).removeView((View) object);
            }

            @Override
            public Object instantiateItem(final ViewGroup container, final int position) {      //当数据发生变化时触发这个函数
                if(views[position]==null) {
                    views[position] = LayoutInflater
                            .from(getBaseContext()).inflate(RID_views[position], null, false);       //从这里加载不同的viewPage
                    final int finalPos = position;
                    new Thread(new Runnable() {     //因为page的加载有延时 所以需要延后一段时间获取layout资源
                        @Override
                        public void run() {
                            try {
                                Thread.sleep(500);
                            } catch(Exception e) {
                                e.printStackTrace();
                            }

                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    inflateWithData(finalPos);
                                }
                            });
                        }
                    }).start();
                }
                container.addView(views[position]);
                return views[position];
            }
        });

        final String[] colors = getResources().getStringArray(R.array.vertical_ntb);
        final NavigationTabBar navigationTabBar = (NavigationTabBar) findViewById(R.id.ntb_horizontal);
        final ArrayList<NavigationTabBar.Model> models = new ArrayList<>();
        models.add(
                new NavigationTabBar.Model.Builder(
                        getResources().getDrawable(R.drawable.ic_first),
                        Color.parseColor(colors[0]))
                        .selectedIcon(getResources().getDrawable(R.drawable.ic_sixth))
                        .title("音乐")
                        .build()
        );
        models.add(
                new NavigationTabBar.Model.Builder(
                        getResources().getDrawable(R.drawable.ic_second),
                        Color.parseColor(colors[1]))
//                        .selectedIcon(getResources().getDrawable(R.drawable.ic_eighth))
                        .title("排行")
                        .build()
        );
        models.add(
                new NavigationTabBar.Model.Builder(
                        getResources().getDrawable(R.drawable.ic_third),
                        Color.parseColor(colors[2]))
                        .selectedIcon(getResources().getDrawable(R.drawable.ic_seventh))
                        .title("多人")
                        .build()
        );
        models.add(
                new NavigationTabBar.Model.Builder(
                        getResources().getDrawable(R.drawable.ic_fourth),
                        Color.parseColor(colors[3]))
//                        .selectedIcon(getResources().getDrawable(R.drawable.ic_eighth))
                        .title("设置")
                        .build()
        );

        navigationTabBar.setModels(models);
        navigationTabBar.setViewPager(viewPager, 0);            //index代表初始的Viewpager提供显示的position
        navigationTabBar.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(final int position, final float positionOffset, final int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(final int position) {
                navigationTabBar.getModels().get(position).hideBadge();
            }

            @Override
            public void onPageScrollStateChanged(final int state) {

            }
        });

        navigationTabBar.postDelayed(new Runnable() {
            @Override
            public void run() {
                for (int i = 0; i < navigationTabBar.getModels().size(); i++) {
                    final NavigationTabBar.Model model = navigationTabBar.getModels().get(i);
                    navigationTabBar.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            model.showBadge();
                        }
                    }, i * 100);
                }
            }
        }, 500);
    }

    @Override
    public void onClick(View v) {
        Intent intent = null;
        switch(v.getId()) {
            //多人游戏 按键
            case R.id.btn_createAHome:
                intent = new Intent(HomeActivity.this,CreateAHomeActivity.class);
                startActivity(intent);
                break;
            case R.id.btn_enterAHome:
                intent = new Intent(HomeActivity.this,EnterAHomeActivity.class);
                startActivity(intent);
                break;
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {

        return true;
    }

    public void inflateWithData(int pos) {
        //根据不同的View进行View数据的初始化
        Log.e(TAG, String.format("inflateWithData: %d",pos));
        switch(pos) {
            case 0:
                initMusicList();
                break;
            case 1:
                initRankList();
                break;
            case 2:
                initMutiGame();
                break;
            case 3:
                initSetting();
                break;
        }
     }

     //初始化乐曲选择界面
     private void initMusicList() {
         String[] fileList = null;
         AssetManager manager = this.getAssets();
         try {
             fileList = manager.list("text/music");
         } catch(Exception e) {
             Log.e(TAG, "startFindLocalMusic: error"  );
             e.printStackTrace();
         }
         ArrayList<MusicItem> settingList = new ArrayList<>();
         RecyclerView recyclerView = (RecyclerView) findViewById(R.id.GrecyclerView_singleChoose);
         TextView textMusicNum = (TextView)findViewById(R.id.text_musicDetectedNum_singleChoose);
         textMusicNum.setText(String.format("(嘿ヾ(✿ﾟ▽ﾟ)ノ，我们找到了%d首歌曲：",fileList.length));
         for(int i=0;i< fileList.length;i++) {
             settingList.add(new MusicItem(i+1,fileList[i],"",new ArrayList<Integer>()));
         }
         LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
         recyclerView.setLayoutManager(linearLayoutManager);
         MusicListAdapter settingsAdapter = new MusicListAdapter(this,settingList);
         recyclerView.setAdapter(settingsAdapter);

         //激发动画
         final Context context = recyclerView.getContext();
         final LayoutAnimationController controller =
                 AnimationUtils.loadLayoutAnimation(context, R.anim.layout_animation_fall_down);
         recyclerView.setLayoutAnimation(controller);
         recyclerView.getAdapter().notifyDataSetChanged();
         recyclerView.scheduleLayoutAnimation();
     }


     //初始化排行榜界面private
     ArrayList<turple> turList;
     boolean initRankListFlag = false;
     PullToRefreshView mPullToRefreshView;
     private void initRankList() {
         //设置下拉刷新事件
         mPullToRefreshView = (PullToRefreshView) findViewById(R.id.pull_to_refresh_listSorted);
         if(!initRankListFlag) {
             initRankListFlag = true;
             mPullToRefreshView.setOnRefreshListener(new PullToRefreshView.OnRefreshListener() {
                 @Override
                 public void onRefresh() {
                     initRankList();
                 }
             });
         }
        //设置recyclerView的显示数据
         turList = new ArrayList<>();
         SharedPreferences pref = getSharedPreferences("music",MODE_PRIVATE);
         Map<String,?> map = pref.getAll();
         for(Map.Entry<String,?> item : map.entrySet()) {
            String finame =""+item.getKey();
            String score = ""+item.getValue();
            turList.add(new turple(finame,score));
         }
         Collections.sort(turList);
         ArrayList<MusicScoreItem> settingList = new ArrayList<>();
         for(int i=0;i<turList.size();i++) {
             settingList.add(new MusicScoreItem(i,turList.get(i).name,Integer.parseInt(turList.get(i).score)));
         }
         RecyclerView recyclerView = (RecyclerView) findViewById(R.id.recyclerView_listSorted);
         LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
         recyclerView.setLayoutManager(linearLayoutManager);
         MusicScoreAdapter settingsAdapter = new MusicScoreAdapter(this,settingList);
         recyclerView.setAdapter(settingsAdapter);
         //激发动画
         final Context context = recyclerView.getContext();
         final LayoutAnimationController controller =
                 AnimationUtils.loadLayoutAnimation(context, R.anim.layout_animation_fall_down);
         recyclerView.setLayoutAnimation(controller);
         recyclerView.getAdapter().notifyDataSetChanged();
         recyclerView.scheduleLayoutAnimation();

         if(mPullToRefreshView!=null) {
             mPullToRefreshView.setRefreshing(false);       //通知停止刷新
         }
     }

     //初始化游戏设置界面
     private List<SettingItem> settingList = new ArrayList<>();
     ActionBar actionBar;
     TextView tv_usernameActionBar;
     public void initSetting() {
         Toolbar toolbar =(Toolbar) findViewById(R.id.content_toolbar);
         CollapsingToolbarLayout collapsingToolbarLayout = (CollapsingToolbarLayout)
                 findViewById(R.id.collapsingToolBarLayout);
         setSupportActionBar(toolbar);
         ImageView imageView = (ImageView) findViewById(R.id.content_imageView);
         CircleImageView headP = (CircleImageView) findViewById(R.id.headP);
         actionBar = getSupportActionBar();
         if(actionBar != null) {
             actionBar.setTitle("");
             //actionBar.setDisplayHomeAsUpEnabled(true);
         }

         //添加标题栏的用户名称
         /*
         tv_usernameActionBar = (TextView) findViewById(R.id.login_name_setting);
         String userTitle="";
         if(userTitle.length()==0)
             tv_usernameActionBar.setText("尚未登录");
         else
             tv_usernameActionBar.setText(userTitle);
           */
         Glide.with(this).load(R.drawable.myback).into(imageView);
         Glide.with(this).load(R.drawable.touxiang).into(headP);

         headP.setOnClickListener(this);
//         tv_usernameActionBar.setOnClickListener(this);

         settingList.add(new SettingItem(R.drawable.pic_effect,"音效","",3));
         settingList.add(new SettingItem(R.drawable.pic_help,"帮助","",1));
         settingList.add(new SettingItem(R.drawable.pic_about,"关于游戏","",1));
         settingList.add(new SettingItem(R.drawable.pic_edition,"乐动指间","版本1.0.0",2));
         RecyclerView recyclerView = (RecyclerView)findViewById(R.id.recyclerView);
         LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
         recyclerView.setLayoutManager(linearLayoutManager);
         SettingsAdapter settingsAdapter = new SettingsAdapter(this,settingList);
         recyclerView.setAdapter(settingsAdapter);
     }

     //初始化多人游戏界面
     private int RID_picNote[] = {R.drawable.pic_note1,R.drawable.pic_note2,R.drawable.pic_note3,R.drawable.pic_note4,
            R.drawable.pic_note5,R.drawable.pic_note6,R.drawable.pic_note7,R.drawable.pic_note8,R.drawable.pic_note9,
             R.drawable.pic_note10,R.drawable.pic_note11,R.drawable.pic_note12,R.drawable.pic_note13,R.drawable.pic_note14,
             R.drawable.pic_note15,R.drawable.pic_note16,R.drawable.pic_note17};
     private Random random = new Random();
     private FloatBackLayout floatBackLayout;
     private void initMutiGame() {
         //设置监听
         Button btn_createHome  = (Button) findViewById(R.id.btn_createAHome);
         Button btn_enterHome  = (Button) findViewById(R.id.btn_enterAHome);
         btn_enterHome.setOnClickListener(this);
         btn_createHome.setOnClickListener(this);
        //设置浮动背景
         floatBackLayout =  (FloatBackLayout) findViewById(R.id.welcome_floatbackground);
         //floatBackLayout.setBackGround(this,R.drawable.pic_start_backg);
         for(int i=0;i<17;i++) {
             float posX = ((float)random.nextInt(100)/100.0f);
             float posY = ((float)random.nextInt(100)/100.0f);
             floatBackLayout.addFloatView(new FloatBitmap(this,posX,posY,RID_picNote[i]));
         }
         floatBackLayout.initFloatObject(1080,1920);
         new Thread(new Runnable() {
             @Override
             public void run() {
                 try {
                     Thread.sleep(100);
                 } catch(Exception e) {
                     e.printStackTrace();
                 }
                 runOnUiThread(new Runnable() {
                     @Override
                     public void run() {
                         floatBackLayout.startFloat();
                     }
                 });
             }
         }).start();
     }

    public void onActvitiyTrans(Intent msg) {
        Intent intent = null;
        int type = msg.getIntExtra("type",-1);
        switch(type) {
            case 0:
                intent = new Intent(HomeActivity.this,GameActivity.class);
                intent.putExtra("musicName",msg.getStringExtra("musicName"));
                startActivity(intent);
                break;
            case 1:
                intent = new Intent(HomeActivity.this,HelpActivity.class);
                startActivity(intent);
                break;
            case 2:
                intent = new Intent(HomeActivity.this,AboutActivity.class);
                startActivity(intent);
                break;
            case 3:
                intent = new Intent(HomeActivity.this,LoginActivity.class);
                startActivity(intent);
                break;
        }
    }

    private class turple implements Comparable{
        public String name;
        public String score;
        public turple(String name,String score) {
            super();
            this.name = name;
            this.score = score;
        }

        @Override
        public int hashCode() {
            return super.hashCode();
        }

        @Override
        public boolean equals(Object obj) {
            return super.equals(obj);
        }

        @Override
        protected Object clone() throws CloneNotSupportedException {
            return super.clone();
        }

        @Override
        public String toString() {
            return super.toString();
        }

        @Override
        protected void finalize() throws Throwable {
            super.finalize();
        }

        @Override
        public int compareTo(@NonNull Object o) {
            return -Integer.parseInt(this.score)+
                    Integer.parseInt(((turple)o).score);
        }
    }
}
