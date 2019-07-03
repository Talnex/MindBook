package com.talnex.wrongsbook.Components;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.view.View;
import android.widget.EditText;

import com.talnex.wrongsbook.Beans.Node;
import com.talnex.wrongsbook.Fragments.MindFragment;
import com.talnex.wrongsbook.R;
import com.talnex.wrongsbook.Utils.ColorUtils;
import com.talnex.wrongsbook.Utils.TreeUtil;
import com.talnex.wrongsbook.Utils.ViewIds;

public class EditNodeMyDialog extends Dialog {
    private EditText et_name;
    private EditText et_rank;
    private EditText et_url;
    private String nodeid;
    private Node nodecopy;
    private myTextView contentview;
    private MindFragment mindFragment;

    public EditNodeMyDialog(Context context, View contentview, MindFragment mindFragment) {
        super(context);
        setContentView(R.layout.dialog_layout);
        nodeid = ViewIds.map_ViewIdtoNodeId.get(contentview.getId()).id;
        nodecopy = ViewIds.map_ViewIdtoNodeId.get(contentview.getId());
        et_name = findViewById(R.id.et_name);
        et_rank = findViewById(R.id.et_rank);
        et_url = findViewById(R.id.et_url);
        this.contentview = (myTextView) contentview;
        this.mindFragment = mindFragment;

    }

    @Override
    public void show() {
        super.show();
        et_name.setText(nodecopy.info);
        et_rank.setText(nodecopy.rank + "");
        et_url.setText(nodecopy.url);
    }

    @Override
    public void setOnDismissListener(DialogInterface.OnDismissListener listener) {
        super.setOnDismissListener(listener);

    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)
    @Override
    protected void onStop() {
        super.onStop();
        TreeUtil.map_IDtoClass.get(nodeid).info = et_name.getText().toString();
        TreeUtil.map_IDtoClass.get(nodeid).url = et_url.getText().toString();
        TreeUtil.map_IDtoClass.get(nodeid).rank = Integer.parseInt(et_rank.getText().toString());
        if (TreeUtil.map_IDtoClass.get(nodeid).url.equals("")) {
            TreeUtil.map_IDtoClass.get(nodeid).type = 0;
        } else TreeUtil.map_IDtoClass.get(nodeid).type = 1;
        if (!nodecopy.info.equals(""))
            contentview.setText(nodecopy.info);
        else contentview.setText("编辑");
        contentview.setBorderColor(ColorUtils.rankColor.get(nodecopy.rank));
        if (nodecopy.type == 1) {
            contentview.setBackgroundColor(ColorUtils.rankColor.get(nodecopy.rank));
            contentview.setTextColor(Color.BLACK);
        }
        mindFragment.reDraw(TreeUtil.mindTree);
    }
}
