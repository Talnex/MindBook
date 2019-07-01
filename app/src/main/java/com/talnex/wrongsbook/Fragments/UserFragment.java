package com.talnex.wrongsbook.Fragments;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.gc.materialdesign.views.ButtonFlat;
import com.talnex.wrongsbook.Adapter.FileAdapter;
import com.talnex.wrongsbook.Beans.Node;
import com.talnex.wrongsbook.File.JsonUtil;
import com.talnex.wrongsbook.File.MbFile;
import com.talnex.wrongsbook.R;
import com.talnex.wrongsbook.Utils.TreeUtil;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class UserFragment extends Fragment {
    private ListView listView;
    private android.support.v7.widget.Toolbar toolbar;
    public static final String SDcard = Environment.getExternalStorageDirectory().getAbsolutePath();
    public static String currDir = SDcard + "/mindbook";
    private ButtonFlat btn_expMD = null;
    private ButtonFlat btn_inMD = null;
    private ButtonFlat btn_expPDF = null;
    private ButtonFlat btn_setting = null;

    FileAdapter adapter;
    List<File> list = new ArrayList<>();
    ArrayList<String> file_choosed_list = new ArrayList<>();

    private class myOncliclistener implements View.OnClickListener{

        @Override
        public void onClick(View view) {
            switch (view.getId()){
                case R.id.btn_expMD:
                    MbFile.exportMdFile(TreeUtil.mindTree);
                    try {
                        MbFile.writeMdFile(MbFile.newMdFile);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    break;
                case  R.id.btn_inMD:
                    try {
                        Node node = MbFile.readMdFile(MbFile.MdFile);
                        node.treeParm.leftpoint_x = 2000;
                        node.treeParm.leftpoint_y = 5000;
                        TreeUtil.initUtil(node);
                        TreeUtil.loadAllNode(node);
                        TreeUtil.computeOffSet();
                        TreeUtil.computeXY(node);
                        MbFile.writeTreeFile(JsonUtil.nodetoJson(node));

                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    break;
                case  R.id.btn_setting:
                    Intent intent = new Intent(getActivity(),SettingFragment.class);
                    startActivity(intent);
                    break;
            }
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.filechoosefragment, container, false);
        btn_expMD = view.findViewById(R.id.btn_expMD);
        btn_expPDF = view.findViewById(R.id.btn_expPDF);
        btn_inMD = view.findViewById(R.id.btn_inMD);
        btn_setting = view.findViewById(R.id.btn_setting);

        myOncliclistener myOncliclistener = new myOncliclistener();
        btn_setting.setOnClickListener(myOncliclistener);
        btn_inMD.setOnClickListener(myOncliclistener);
        btn_expMD.setOnClickListener(myOncliclistener);

        adapter = new FileAdapter(getActivity(), list, file_choosed_list);
        listView = (ListView) view.findViewById(R.id.listview_send);
        Log.d("test", currDir);
        choose_file();
        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    private void choose_file() {
        listView.setVisibility(View.VISIBLE);
        listView.setAdapter(adapter);

        getAllFiles();
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                File file = list.get(position);

                if (file_choosed_list.contains(file.getAbsolutePath())) {
                    file_choosed_list.remove(file.getAbsolutePath());
                } else {
                    file_choosed_list.add(file.getAbsolutePath());
                }
                adapter.notifyDataSetChanged();//刷新一下checkbox视图

            }
        });
    }

    public void getAllFiles() {
        list.clear();
        final File file = new File(currDir);
        if (file.isDirectory()) {
            File[] files=file.listFiles();
            if (files!=null){
                for (File file2:files){
                    if (file2.getAbsolutePath().endsWith(".mb"))
                    list.add(file2);
                }
            }
        } else {
            list.add(file);
        }
        sort();
        adapter.notifyDataSetChanged();
    }

    private void sort() {
        Collections.sort(list, new Comparator<File>() {
            @Override
            public int compare(File o1, File o2) {
                if (o1.isDirectory() && o2.isDirectory() || o1.isFile() && o2.isFile()) {
                    return o1.compareTo(o2);
                }
                return o1.isDirectory() ? -1 : 1;
            }
        });
    }

}
