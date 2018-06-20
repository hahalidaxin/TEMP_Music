package com.example.daxinli.tempmusic.musicTouch;

import android.content.Context;
import android.content.res.AssetManager;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.view.animation.LayoutAnimationController;
import android.widget.TextView;

import com.example.daxinli.tempmusic.MutigameModule.other.MusicItem;
import com.example.daxinli.tempmusic.MutigameModule.other.MusicListAdapter;
import com.example.daxinli.tempmusic.R;

import java.util.ArrayList;

import devlight.io.library.ntb.NavigationTabBar;

//游戏的主界面

public class HomeActivity extends BaseActivity {
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
            private int[] RID_views  = { R.layout.activity_single_choose,R.layout.activity_single_choose,
                    R.layout.activity_create_ahome,R.layout.activity_enter_ahome };
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
            public Object instantiateItem(final ViewGroup container, final int position) {
                if(views[position]==null) {
                    views[position] = LayoutInflater
                            .from(getBaseContext()).inflate(RID_views[position], null, false);       //从这里加载不同的viewPage
                    inflateWithData(position);
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
        navigationTabBar.setViewPager(viewPager, 2);
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
    public void inflateWithData(int pos) {
        //根据不同的View进行View数据的初始化
        if(pos==0) {       //单人游戏曲目
            String[] fileList = null;
            AssetManager manager = this.getAssets();
            try {
                fileList = manager.list("text/music");
            } catch(Exception e) {
                Log.e(TAG, "startFindLocalMusic: error"  );
                e.printStackTrace();
            }
            ArrayList<MusicItem> settingList = new ArrayList<>();
            //String msg = mintent.getStringExtra("musicList");
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
     }

}
