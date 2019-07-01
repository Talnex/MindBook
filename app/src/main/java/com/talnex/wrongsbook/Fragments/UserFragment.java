package com.talnex.wrongsbook.Fragments;

import android.content.Context;
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

import com.talnex.wrongsbook.Adapter.FileAdapter;
import com.talnex.wrongsbook.R;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class UserFragment extends Fragment {
    private ListView listView;
    private android.support.v7.widget.Toolbar toolbar;
    public static final String SDcard = Environment.getExternalStorageDirectory().getAbsolutePath();
    public static String currDir = SDcard + "/mindbook";

    FileAdapter adapter;
    List<File> list = new ArrayList<>();
    ArrayList<String> file_choosed_list = new ArrayList<>();

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
