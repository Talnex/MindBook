package com.talnex.wrongsbook.Fragments;

import android.content.Context;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.BounceInterpolator;
import android.view.animation.ScaleAnimation;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.dxtt.coolmenu.CoolMenuFrameLayout;
import com.talnex.wrongsbook.Beans.Node;
import com.talnex.wrongsbook.Components.PopList;
import com.talnex.wrongsbook.Components.myTextView;
import com.talnex.wrongsbook.File.JsonUtil;
import com.talnex.wrongsbook.File.MbFile;
import com.talnex.wrongsbook.Components.DrawGeometryView;
import com.talnex.wrongsbook.Components.HVScrollView;
import com.talnex.wrongsbook.Utils.TreeUtil;
import com.talnex.wrongsbook.R;
import com.talnex.wrongsbook.Utils.ColorUtils;
import com.talnex.wrongsbook.Utils.ViewIds;
import com.talnex.wrongsbook.Utils.WigetController;
import com.talnex.wrongsbook.Utils.sample;

import java.util.ArrayList;
import java.util.List;

import static com.talnex.wrongsbook.Utils.DisplayUtil.SCREEN_HEIGHT;
import static com.talnex.wrongsbook.Utils.DisplayUtil.SCREEN_WIDTH;


public class MindFragment extends Fragment {
    private DrawGeometryView drawGeometryView;
    private RelativeLayout insertLayout;
    private ColorUtils colorUtils = new ColorUtils();
    private HVScrollView hv;
    private View lines;
    private List<String> popMenuItemList = new ArrayList<>();
    private CoolMenuFrameLayout coolMenuFrameLayout;
    private Node node;
    private List<Fragment> fragments = new ArrayList<>();
    private List<String> titleList = null;


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.mindfragment, container, false);

        hv = view.findViewById(R.id.hvscrollview);
        insertLayout = view.findViewById(R.id.layout_zone);


        if (MbFile.hasfile()){
            node = JsonUtil.jsontoNode(MbFile.readTreeFile());
        }else {
            node = new Node(null);
            node.setId("root");
        }

//        Node node = sample.getANode();
//        node = sample.getANode();
//        node.setId("root");
        node.treeParm.leftpoint_x = 2000;
        node.treeParm.leftpoint_y = 5000;
        TreeUtil.initUtil(node);
        TreeUtil.loadAllNode(node);
        TreeUtil.computeOffSet();
        TreeUtil.computeXY(node);
        MbFile.writeTreeFile(JsonUtil.nodetoJson(node));

        //String json = JSON.toJSONString(node);
        //JsonUtil.e("json", json);

        popMenuItemList.add("同级");
        popMenuItemList.add("下一级");
        popMenuItemList.add("删除");
        popMenuItemList.add("编辑");
        drawNode(node);
        drawTree(node);

        ImageView imageView = view.findViewById(R.id.refresh);
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                TreeUtil.computeOffSet();
                TreeUtil.computeXY(node);
                reDraw(node);
                myTextView myTextView = getActivity().findViewById(ViewIds.map_NodetoViewID.get(node));
                insertLayout.setScaleX(1);
                insertLayout.setScaleY(1);
                HVScrollView.scale = 1;
                HVScrollView.offset = 1;
                hv.smoothScrollTo((int) (myTextView.getX() - SCREEN_WIDTH  / 2) + myTextView.getWidth() / 2
                       , (int) (myTextView.getY() - SCREEN_HEIGHT  / 2) + myTextView.getHeight() / 2);
            }
        });
        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }


    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)
    void drawTree(Node node) {
        List<Integer> linesid = new ArrayList<>();

        for (Node child :
                node.children) {
            //如果已经绘制了
            if (ViewIds.map_NodetoViewID.containsKey(child)) {
                myTextView myTextView = getView().findViewById(ViewIds.map_NodetoViewID.get(child));
                child.treeParm.setRightpoint_x(child.treeParm.leftpoint_x + child.treeParm.width);
                child.treeParm.setRightpoint_y(child.treeParm.leftpoint_y);
                child.treeParm.setCenter_x(child.treeParm.leftpoint_x + child.treeParm.width / 2);
                child.treeParm.setCenter_y(child.treeParm.leftpoint_y);
                modifyXY(child);
            } else drawNode(child);


            if (node.treeParm.center_y < child.treeParm.center_y) {
                //画线
                lines = new DrawGeometryView(getActivity(), 0, 0, Math.abs(child.treeParm.getLeftpoint_x() - node.treeParm.getRightpoint_x()), Math.abs(child.treeParm.getCenter_y() - node.treeParm.getCenter_y())
                        , ColorUtils.rankColor.get(child.rank)
                );
                lines.invalidate();
                AlphaAnimation alphaAnimation = new AlphaAnimation(0, 1);
                alphaAnimation.setDuration(1500);
                alphaAnimation.setFillAfter(true);
                lines.setAnimation(alphaAnimation);
                lines.setId(View.generateViewId());


                RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(Math.abs(child.treeParm.getLeftpoint_x() - node.treeParm.getRightpoint_x()), Math.abs(child.treeParm.getCenter_y() - node.treeParm.getCenter_y()));
                insertLayout.addView(lines, layoutParams);

                WigetController.setLayout(lines, node.treeParm.getRightpoint_x(),
                        node.treeParm.rightpoint_y);

                linesid.add(lines.getId());
            } else if (node.treeParm.center_y > child.treeParm.center_y) {
                lines = new DrawGeometryView(getActivity(), 0, Math.abs(child.treeParm.center_y - node.treeParm.center_y)
                        , Math.abs(child.treeParm.leftpoint_x - node.treeParm.rightpoint_x), 0, ColorUtils.rankColor.get(child.rank));
                lines.invalidate();

                AlphaAnimation alphaAnimation = new AlphaAnimation(0, 1);
                alphaAnimation.setDuration(1500);
                alphaAnimation.setFillAfter(true);
                lines.setAnimation(alphaAnimation);
                lines.setId(View.generateViewId());

                RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(Math.abs(child.treeParm.leftpoint_x - node.treeParm.rightpoint_x)
                        , Math.abs(child.treeParm.center_y - node.treeParm.center_y));
                insertLayout.addView(lines, layoutParams);

                WigetController.setLayout(lines, node.treeParm.rightpoint_x,
                        child.treeParm.rightpoint_y);
                linesid.add(lines.getId());
            } else {
                lines = new DrawGeometryView(getActivity(), 0, 5,
                        Math.abs(child.treeParm.leftpoint_x - node.treeParm.rightpoint_x),
                        5, ColorUtils.rankColor.get(child.rank));
                lines.invalidate();

                AlphaAnimation alphaAnimation = new AlphaAnimation(0, 1);
                alphaAnimation.setDuration(1500);
                alphaAnimation.setFillAfter(true);
                lines.setAnimation(alphaAnimation);
                lines.setId(View.generateViewId());

                RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(Math.abs(child.treeParm.leftpoint_x - node.treeParm.rightpoint_x), 10);
                insertLayout.addView(lines, layoutParams);

                WigetController.setLayout(lines, node.treeParm.rightpoint_x,
                        node.treeParm.rightpoint_y - 5);
                linesid.add(lines.getId());

            }

        }
        ViewIds.list_Lines.addAll(linesid);

        for (Node child :
                node.children) {
            if (child.hasChildren()) drawTree(child);
        }
    }

    private void modifyXY(Node child) {
        myTextView textView = getView().findViewById(ViewIds.getViewIDfromNode(child));
        WigetController.setLayout(textView, child.treeParm.leftpoint_x, child.treeParm.leftpoint_y - WigetController.getHeight(textView) / 2);
    }

    /**
     * 绘制节点Textview
     *
     * @param node
     * @return
     */
    private void drawNode(Node node) {

        //增加的节点
        final myTextView textView = new myTextView(getActivity(), null);
        textView.setTextColor(Color.BLACK);
        textView.setTextSize(5);
        if (node.info!=null){
            textView.setText(node.info);
        }else textView.setText("编辑");
        textView.setMaxLines(2);
        textView.setEllipsize(TextUtils.TruncateAt.END);
        textView.setMaxEms(10);
        textView.setMinEms(4);
        textView.setPadding(30, 20, 30, 20);
        textView.setGravity(Gravity.CENTER);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
            int id = View.generateViewId();
            ViewIds.putViewID(node, id);
            ViewIds.putNode(id, node);
            textView.setId(id);
        }
        //改变框的颜色
        textView.setBorderColor(ColorUtils.rankColor.get(node.rank));
        textView.setBackground(null);
        textView.setFocusable(false);
        if (node.type == 1) {
            //TODO
            textView.setBackgroundColor(ColorUtils.rankColor.get(node.rank));
            textView.setTextColor(Color.BLACK);
        }

        if (!node.id.equals("root")) {
            PopList popList = new PopList(getActivity());
            popList.setNormalBackgroundColor(Color.GRAY);
            popList.setNormalTextColor(Color.WHITE);
            popList.setDividerColor(Color.GRAY);
            popList.bind(textView, popMenuItemList, new PopList.PopupListListener() {
                @Override
                public boolean showPopupList(View adapterView, View contextView, int contextPosition) {
                    return true;
                }

                @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)
                @Override
                public void onPopupListClick(View contextView, int contextPosition, int position) {
                    switch (position) {
                        //同级节点
                        case 0: {
                            int viewid = contextView.getId();
                            Node node = ViewIds.getNodefromViewId(viewid);
                            int no = node.no;
                            node = TreeUtil.map_IDtoClass.get(node.parent);
                            Node newnode = new Node(node.id);
                            TreeUtil.map_IDtoClass.put(newnode.id, newnode);
                            node.addChild(newnode, no + 1);
                            reDraw(node);
                            break;
                        }
                        //下一级节点
                        case 1: {
                            int viewid = contextView.getId();
                            Node node = ViewIds.getNodefromViewId(viewid);
                            int no = node.no;
                            Node newnode = new Node(node.id);
                            TreeUtil.map_IDtoClass.put(newnode.id, newnode);
                            if (node.children.size() != 0) {
                                node.addChild(newnode, node.children.size() - 1);
                            } else node.children.add(newnode);
                            reDraw(node);
                            break;
                        }
                        //删除
                        case 2: {
                            int viewid = contextView.getId();
                            Node node = ViewIds.getNodefromViewId(viewid);
                            TreeUtil.map_IDtoClass.get(node.parent).children.remove(node);
                            deletechildren(node);
                            TreeUtil.computeOffSet();
                            TreeUtil.computeXY(TreeUtil.mindTree);
                            reDraw(TreeUtil.mindTree);
                            break;
                        }
                        //编辑
                        case 3: {
                            coolMenuFrameLayout = getActivity().findViewById(R.id.rl_main);
                            coolMenuFrameLayout.toggle();
                            int viewid = contextView.getId();
                            TreeUtil.currentNode = ViewIds.getNodefromViewId(viewid);
                        }

                    }

                }
            });
        }else {
            PopList popList = new PopList(getActivity());
            popList.setNormalBackgroundColor(Color.GRAY);
            popList.setNormalTextColor(Color.WHITE);
            popList.setDividerColor(Color.GRAY);
            List<String> list = new ArrayList<>();
            list.add("下一级");
            list.add("编辑");
            popList.bind(textView, list, new PopList.PopupListListener() {
                @Override
                public boolean showPopupList(View adapterView, View contextView, int contextPosition) {
                    return true;
                }

                @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)
                @Override
                public void onPopupListClick(View contextView, int contextPosition, int position) {
                    switch (position) {
                        //下一级节点
                        case 0: {
                            int viewid = contextView.getId();
                            Node node = ViewIds.getNodefromViewId(viewid);
                            int no = node.no;
                            Node newnode = new Node(node.id);
                            TreeUtil.map_IDtoClass.put(newnode.id, newnode);
                            if (node.children.size() != 0) {
                                node.addChild(newnode, node.children.size() - 1);
                            } else node.children.add(newnode);
                            reDraw(node);
                            break;
                        }
                        case 1: {
                            coolMenuFrameLayout = getActivity().findViewById(R.id.rl_main);
                            coolMenuFrameLayout.toggle();
                            int viewid = contextView.getId();
                            TreeUtil.currentNode = ViewIds.getNodefromViewId(viewid);
                        }

                    }

                }
            });
        }

//        设置节点的点击事件
        textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                hv.smoothScrollTo((int) (view.getX() - SCREEN_WIDTH  / 2) + view.getWidth() / 2
                        , (int) (view.getY() - SCREEN_HEIGHT  / 2) + view.getHeight() / 2);
//                Log.d("left", (int) (view.getX() - SCREEN_WIDTH / 2) + view.getWidth() / 2 + "");

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
        insertLayout.setBackgroundColor(Color.WHITE);
        insertLayout.addView(textView, layoutParams);
        WigetController.setLayout(textView, node.treeParm.leftpoint_x, node.treeParm.leftpoint_y - WigetController.getHeight(textView) / 2);

        node.treeParm.setHight(WigetController.getHeight(textView));
        node.treeParm.setWidth(WigetController.getWidth(textView));

        node.treeParm.setRightpoint_x(node.treeParm.leftpoint_x + node.treeParm.width);
        node.treeParm.setRightpoint_y(node.treeParm.leftpoint_y);
        node.treeParm.setCenter_x(node.treeParm.leftpoint_x + node.treeParm.width / 2);
        node.treeParm.setCenter_y(node.treeParm.leftpoint_y);

    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)
    private void reDraw(Node parentnode) {
        while (parentnode.parent != null) {
            TreeUtil.computeBP(parentnode);
            parentnode = TreeUtil.map_IDtoClass.get(parentnode.parent);
        }
        TreeUtil.computeXY(TreeUtil.mindTree);
        //销毁掉所有的线
        DrawGeometryView line = null;
        for (int id :
                ViewIds.list_Lines) {
            line = getView().findViewById(id);
            line.clearAnimation();
            line.setVisibility(View.GONE);
            insertLayout.removeView(line);
        }
        ViewIds.list_Lines.clear();

        drawTree(parentnode);
    }

    @Override
    public void onPause() {
        super.onPause();
        MbFile.writeTreeFile(JsonUtil.nodetoJson(TreeUtil.mindTree));
    }

    @Override
    public void onStop() {
        super.onStop();
        MbFile.writeTreeFile(JsonUtil.nodetoJson(TreeUtil.mindTree));
    }

    public void deletechildren(Node root){
        for (Node child:
            root.children ) {
            if (child.hasChildren()){
                deletechildren(child);
            }else {
                insertLayout.removeView(getView().findViewById(ViewIds.map_NodetoViewID.get(child)));
                ViewIds.map_NodetoViewID.remove(child);
                TreeUtil.map_IDtoClass.remove(child.id);
            }
        }
        insertLayout.removeView(getView().findViewById(ViewIds.map_NodetoViewID.get(root)));
        ViewIds.map_NodetoViewID.remove(root);
        TreeUtil.map_IDtoClass.remove(root.id);
    }
}

