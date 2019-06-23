package com.talnex.wrongsbook;

import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.BounceInterpolator;
import android.view.animation.ScaleAnimation;
import android.widget.RelativeLayout;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.talnex.wrongsbook.Beans.Node;
import com.talnex.wrongsbook.Components.myTextView;
import com.talnex.wrongsbook.MindMap.DrawGeometryView;
import com.talnex.wrongsbook.MindMap.HVScrollView;
import com.talnex.wrongsbook.Utils.JsonUtil;
import com.talnex.wrongsbook.Utils.TreeUtil;
import com.talnex.wrongsbook.Utils.ViewIds;
import com.talnex.wrongsbook.Utils.WigetController;
import com.talnex.wrongsbook.Utils.sample;

import java.io.FileOutputStream;
import java.util.List;

import static com.talnex.wrongsbook.Utils.TreeUtil.GAP;
import static com.talnex.wrongsbook.Utils.TreeUtil.SCREEN_HEIGHT;
import static com.talnex.wrongsbook.Utils.TreeUtil.SCREEN_WIDTH;
import static com.talnex.wrongsbook.Utils.TreeUtil.map_IDtoClass;

public class MindTreeEngine extends AppCompatActivity {

    private DrawGeometryView drawGeometryView;
    private RelativeLayout insertLayout;
    private HVScrollView hv;
    private View lines;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        insertLayout = (RelativeLayout) findViewById(R.id.layout_zone);
        hv = (HVScrollView) findViewById(R.id.hvscrollview);

        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);
        SCREEN_WIDTH = dm.widthPixels;
        SCREEN_HEIGHT = dm.heightPixels;

        Node node = sample.getANode();
        node.treeParm.leftpoint_x = 1000;
        node.treeParm.leftpoint_y = 11000;
        TreeUtil.initUtil(node);
        TreeUtil.loadAllNode(node);
        TreeUtil.computeOffSet();
        TreeUtil.computeXY(node);

        String json = JSON.toJSONString(node);
        JsonUtil.e("json", json);

        drawNode(node);
        drawTree(node);

    }

    void drawTree(Node node) {

        for (Node child :
                node.children) {
            drawNode(child);

            if (node.treeParm.center_y < child.treeParm.center_y) {
                //画线
                lines = new DrawGeometryView(this, 0, 0, Math.abs(child.treeParm.getLeftpoint_x() - node.treeParm.getRightpoint_x()), Math.abs(child.treeParm.getCenter_y() - node.treeParm.getCenter_y()), null
                );
                lines.invalidate();

                AlphaAnimation alphaAnimation = new AlphaAnimation(0, 1);
                alphaAnimation.setDuration(1500);
                alphaAnimation.setFillAfter(true);
                lines.setAnimation(alphaAnimation);

                RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(Math.abs(child.treeParm.getLeftpoint_x() - node.treeParm.getRightpoint_x()), Math.abs(child.treeParm.getCenter_y() - node.treeParm.getCenter_y()));
                insertLayout.addView(lines, layoutParams);

                WigetController.setLayout(lines, node.treeParm.getRightpoint_x(),
                        node.treeParm.getRightpoint_y());
            }else if (node.treeParm.center_y > child.treeParm.center_y){
                lines = new DrawGeometryView(this, 0, Math.abs(child.treeParm.getCenter_y() - node.treeParm.getCenter_y()),Math.abs(child.treeParm.getLeftpoint_x() - node.treeParm.getRightpoint_x()),0, null
                );
                lines.invalidate();

                AlphaAnimation alphaAnimation = new AlphaAnimation(0, 1);
                alphaAnimation.setDuration(1500);
                alphaAnimation.setFillAfter(true);
                lines.setAnimation(alphaAnimation);

                RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(Math.abs(child.treeParm.getLeftpoint_x() - node.treeParm.getRightpoint_x()), Math.abs(child.treeParm.getCenter_y() - node.treeParm.getCenter_y()));
                insertLayout.addView(lines, layoutParams);

                WigetController.setLayout(lines, node.treeParm.getRightpoint_x(),
                        child.treeParm.getRightpoint_y());
            }else{
                lines = new DrawGeometryView(this, 0, 5,Math.abs(child.treeParm.getLeftpoint_x() - node.treeParm.getRightpoint_x()),5, null
                );
                lines.invalidate();

                AlphaAnimation alphaAnimation = new AlphaAnimation(0, 1);
                alphaAnimation.setDuration(1500);
                alphaAnimation.setFillAfter(true);
                lines.setAnimation(alphaAnimation);

                RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(Math.abs(child.treeParm.getLeftpoint_x() - node.treeParm.getRightpoint_x()), 10);
                insertLayout.addView(lines, layoutParams);

                WigetController.setLayout(lines, node.treeParm.getRightpoint_x(),
                        node.treeParm.rightpoint_y-5);

            }

        }

        for (Node child :
                node.children) {
            if (child.hasChildren()) drawTree(child);
        }

    }

    /**
     * 绘制节点Textview
     *
     * @param node
     * @return
     */
    private myTextView drawNode(Node node) {

        //增加的节点
        myTextView textView = new myTextView(this, null);
        textView.setTextColor(Color.BLACK);
        textView.setTextSize(20);
        textView.setText(node.id);
        textView.setMaxLines(2);
        textView.setEllipsize(TextUtils.TruncateAt.END);
        textView.setMaxEms(10);
        textView.setMinEms(4);
        textView.setPadding(45, 25, 45, 25);
        textView.setGravity(Gravity.CENTER);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
            int id = View.generateViewId();
            ViewIds.putViewID(node.getId(), id);
            textView.setId(id);
        }
        textView.setBorderColor(getResources().getColor(R.color.blue));
        textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                hv.smoothScrollTo((int) view.getX() - SCREEN_WIDTH / 2 + view.getWidth() / 2
                        , (int) view.getY() - SCREEN_HEIGHT / 2 + view.getHeight() / 2);
            }
        });


        //出现的动画
        ScaleAnimation animation = new ScaleAnimation(0.0f, 1.0f, 0.0f, 1.0f, Animation.RELATIVE_TO_SELF, 0.5f,
                Animation.RELATIVE_TO_SELF, 0.5f);
        animation.setInterpolator(new BounceInterpolator());
        animation.setStartOffset(0);// 动画秒数。
        animation.setFillAfter(true);
        animation.setDuration(500);
        textView.startAnimation(animation);

        //增加的View
        RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT,
                RelativeLayout.LayoutParams.WRAP_CONTENT);
        //layoutParams.topMargin = y;
        //layoutParams.bottomMargin = 1000;
        //layoutParams.rightMargin = 200;
        //layoutParams.leftMargin = x;
        insertLayout.setBackgroundColor(Color.WHITE);
        insertLayout.addView(textView, layoutParams);
        WigetController.setLayout(textView, node.treeParm.leftpoint_x, node.treeParm.leftpoint_y - WigetController.getHeight(textView) / 2);

        node.treeParm.setHight(WigetController.getHeight(textView));
        node.treeParm.setWidth(WigetController.getWidth(textView));

        node.treeParm.setRightpoint_x(node.treeParm.leftpoint_x + node.treeParm.width);
        node.treeParm.setRightpoint_y(node.treeParm.leftpoint_y);
        node.treeParm.setCenter_x(node.treeParm.leftpoint_x + node.treeParm.width / 2);
        node.treeParm.setCenter_y(node.treeParm.leftpoint_y);


        return textView;
    }
}
