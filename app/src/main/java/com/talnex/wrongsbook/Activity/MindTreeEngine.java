package com.talnex.wrongsbook.Activity;

import android.Manifest;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.WindowManager;
import android.widget.RelativeLayout;

import com.dxtt.coolmenu.CoolMenuFrameLayout;
import com.talnex.wrongsbook.Fragments.ComunityFragment;
import com.talnex.wrongsbook.Fragments.Wrongbookfragment;
import com.talnex.wrongsbook.Fragments.MindFragment;
import com.talnex.wrongsbook.Fragments.UserFragment;
import com.talnex.wrongsbook.Components.DrawGeometryView;
import com.talnex.wrongsbook.Components.HVScrollView;
import com.talnex.wrongsbook.R;
import com.talnex.wrongsbook.Utils.ColorUtils;
import com.talnex.wrongsbook.Utils.DisplayUtil;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


public class MindTreeEngine extends AppCompatActivity {

    private DrawGeometryView drawGeometryView;
    private RelativeLayout insertLayout;
    private ColorUtils colorUtils = new ColorUtils();
    private HVScrollView hv;
    private View lines;
    private List<String > popMenuItemList = new ArrayList<>();
    private CoolMenuFrameLayout coolMenuFrameLayout;

    private List<Fragment> fragments = new ArrayList<>();
    private List<String> titleList = null;

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.content_main);

        ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.INTERNET,Manifest.permission.WRITE_EXTERNAL_STORAGE,
        Manifest.permission.READ_EXTERNAL_STORAGE},0);

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);
        DisplayUtil.SCREEN_WIDTH = dm.widthPixels;
        DisplayUtil.SCREEN_HEIGHT = dm.heightPixels;

        //添加coolmenu
        coolMenuFrameLayout = findViewById(R.id.rl_main);
        String[] titles = {"我", "社区", "题目", ""};
        titleList = Arrays.asList(titles);
        coolMenuFrameLayout.setTitles(titleList);
        coolMenuFrameLayout.setScrollBarSize(20);

        fragments.add(new UserFragment());
        fragments.add(new ComunityFragment());
        fragments.add(new Wrongbookfragment());
        fragments.add(new MindFragment());

        FragmentPagerAdapter adapter = new FragmentPagerAdapter(getSupportFragmentManager()) {
            @Override
            public Fragment getItem(int position) {
                return fragments.get(position);
            }

            @Override
            public int getCount() {
                return fragments.size();
            }
        };
        coolMenuFrameLayout.setAdapter(adapter);
    }

}
