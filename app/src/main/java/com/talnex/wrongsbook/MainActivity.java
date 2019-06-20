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
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.BounceInterpolator;
import android.view.animation.ScaleAnimation;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.alibaba.fastjson.JSONObject;
import com.talnex.wrongsbook.Beans.Node;
import com.talnex.wrongsbook.Components.myTextView;
import com.talnex.wrongsbook.MindMap.DrawGeometryView;
import com.talnex.wrongsbook.MindMap.HVScrollView;
import com.talnex.wrongsbook.Utils.DisplayUtil;
import com.talnex.wrongsbook.Utils.ViewIds;
import com.talnex.wrongsbook.Utils.WigetController;
import com.talnex.wrongsbook.Utils.sample;

import java.util.List;

public class MainActivity extends AppCompatActivity {
    private DrawGeometryView drawGeometryView;
    private RelativeLayout insertLayout;
    private HVScrollView hv;
    private View lines;
    private final int GAP = 200;
    private final int CENG_GAP = 800;
    int SCREEN_WIDTH;
    int SCREEN_HEIGHT;

    @Override
    protected void onResume() {
        super.onResume();

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.test);

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        insertLayout = (RelativeLayout) findViewById(R.id.layout_zone);
        hv = (HVScrollView) findViewById(R.id.hvscrollview);

        //获取手机的窗口宽度
        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);
//        SCREEN_WIDTH = DisplayUtil.px2dip(this,dm.widthPixels);
//        SCREEN_HEIGHT = DisplayUtil.px2dip(this,dm.heightPixels);
        SCREEN_WIDTH = dm.widthPixels;
        SCREEN_HEIGHT = dm.heightPixels;

        Log.d("test", SCREEN_HEIGHT + "  " + SCREEN_WIDTH);


        Node node = sample.getANode();


//        Node node1 = JSONObject.parseObject(jsonObject.get("children").toString(),Node.class);
//
//        draw(node, 720, 1280);
        node.treeParm.setCenter_x(720);
        node.treeParm.setCenter_y(1280);
        drawChild(node);


        String json = JSONObject.toJSONString(node);
        JSONObject jsonObject = JSONObject.parseObject(json);

        Log.d("json", jsonObject.toJSONString());
    }

    private myTextView draw(final Node tree, int x, int y) {

        //增加的节点
        myTextView textView = new myTextView(this, null);
        textView.setTextColor(Color.BLACK);
        textView.setTextSize(20);
        textView.setText(tree.getUrl());
        textView.setMaxLines(2);
        textView.setEllipsize(TextUtils.TruncateAt.END);
        textView.setMaxEms(10);
        textView.setMinEms(4);
        textView.setPadding(45, 25, 45, 25);
        textView.setGravity(Gravity.CENTER);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
            int id = View.generateViewId();
            ViewIds.putViewID(tree.getId(), id);
            textView.setId(id);
        }
        textView.setBorderColor(getResources().getColor(R.color.blue));
        textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                hv.smoothScrollTo((int) view.getX() - SCREEN_WIDTH / 2 + tree.treeParm.getWidth() / 2
                        , (int) view.getY() - SCREEN_HEIGHT / 2 + tree.treeParm.getHight() / 2);
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
        RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
        //layoutParams.topMargin = y;
        //layoutParams.bottomMargin = 1000;
        //layoutParams.rightMargin = 200;
        //layoutParams.leftMargin = x;
        insertLayout.setBackgroundColor(Color.WHITE);
        insertLayout.addView(textView, layoutParams);
        WigetController.setLayout(textView, x - WigetController.getWidth(textView) / 2, y - WigetController.getHeight(textView) / 2);

        tree.treeParm.setHight(WigetController.getHeight(textView));
        tree.treeParm.setWidth(WigetController.getWidth(textView));

        tree.treeParm.setLeftpoint_x(x - tree.treeParm.getWidth() / 2);
        tree.treeParm.setRightpoint_x(x + tree.treeParm.getWidth() / 2);
        tree.treeParm.setLeftpoint_y(y);
        tree.treeParm.setRightpoint_y(y);
        tree.treeParm.setCenter_x(x);
        tree.treeParm.setCenter_y(y);


        return textView;
    }


    void drawChild(Node node) {
        int offset = node.treeParm.getOffset();
        List<Node> children = node.getChildren();
        int father_x = node.treeParm.getCenter_x();
        int father_y = node.treeParm.getCenter_y();
        int num = children.size();
        int i = 0;
        //绘制父节点
        //draw(node, father_x, father_y);
        //绘制最上面的那个
        draw(children.get(i), father_x + CENG_GAP, father_y - ((num - 1) / 2 - i) * GAP);
        int brother_starty = children.get(i).treeParm.getCenter_y();
        int brother_height = children.get(i).treeParm.getHight();
        //绘制
        for (i = 1; i < num; i++) {
            draw(children.get(i), father_x + CENG_GAP, brother_starty + brother_height + GAP);
            brother_starty = children.get(i).treeParm.getCenter_y();
            brother_height = children.get(i).treeParm.getHight();
        }

        draw(node, 720, (children.get(i - 1).treeParm.getCenter_y() + children.get(0).treeParm.getCenter_y()) / 2);


        //画线
        lines = new DrawGeometryView(this, 0, 0, children.get(i - 1).treeParm.getLeftpoint_x() - node.treeParm.getRightpoint_x(), Math.abs(children.get(i - 1).treeParm.getCenter_y() - node.treeParm.getCenter_y()), null
        );
        lines.invalidate();

        AlphaAnimation alphaAnimation = new AlphaAnimation(0, 1);
        alphaAnimation.setDuration(1500);
        alphaAnimation.setFillAfter(true);
        lines.setAnimation(alphaAnimation);

        RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(children.get(i - 1).treeParm.getLeftpoint_x() - node.treeParm.getRightpoint_x(), Math.abs(children.get(i - 1).treeParm.getCenter_y() - node.treeParm.getCenter_y()));
        insertLayout.addView(lines, layoutParams);

        WigetController.setLayout(lines, node.treeParm.getRightpoint_x(),
                node.treeParm.getRightpoint_y());
        Log.d("xy", lines.getX() + lines.getY() + "");

    }

}
