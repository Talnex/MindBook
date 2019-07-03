package com.talnex.wrongsbook.Fragments;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.PopupWindow;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.gc.materialdesign.views.ButtonFlat;
import com.scanlibrary.ScanActivity;
import com.scanlibrary.ScanConstants;
import com.talnex.wrongsbook.Adapter.PhotoWallAdapter;
import com.talnex.wrongsbook.Components.PopuChoooseWindow;
import com.talnex.wrongsbook.Net.Userinfo;
import com.talnex.wrongsbook.R;
import com.talnex.wrongsbook.Utils.UUID;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import butterknife.ButterKnife;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class Wrongbookfragment extends Fragment {
    private static final int REQUEST_CAMERA = 1;
    private static final int REQUEST_ALBUM_OK = 2;
    private String resposetext = "";
    private static final int REQUEST_CODE = 99;
    private boolean isdelete = false;
    String TAG = "test";
    RecyclerView mRecyView;
    private EditText input;
    private ButtonFlat upload;
    private ArrayList<String> mDatas = new ArrayList<>();
    private PhotoWallAdapter mPhotoWallAdapter;
    private Map<String, String> fileUrl = new HashMap<>();

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
    public View onCreateView(final LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.wrongbookfragment, container, false);
        ButterKnife.bind(getActivity());
        mPhotoWallAdapter = new PhotoWallAdapter(getActivity(), mDatas, 10);

        input = view.findViewById(R.id.edit_text);
        upload = view.findViewById(R.id.uploadbtn);
        upload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String inputtext = input.getText().toString();
                String title = "";
                String photo = null;
                final StringBuilder text = new StringBuilder();
                String[] tempstring = inputtext.split("\n");
                if (tempstring[0].split("：")[0].equals("标题")) {
                    title = tempstring[0].split("：")[1];
                }
                if (tempstring[1].split("：")[0].equals("头图")) {
                    photo = tempstring[1].split("：")[1];
                }
                for (int i = 2; i < tempstring.length; i++) {
                    text.append(tempstring[i]);
                }

                final OkHttpClient client = new OkHttpClient();
                final String finalPhoto = photo;
                final String finalTitle = title;
                Toast.makeText(getActivity(), "正在上传", Toast.LENGTH_LONG).show();
                new Thread() {
                    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
                    @Override
                    public void run() {
                        RequestBody requestBody = new MultipartBody.Builder()
                                .setType(MultipartBody.FORM)
                                .addFormDataPart("img", UUID.getUUID(),
                                        RequestBody.create(MediaType.parse("image/png"), new File(mDatas.get(Integer.parseInt(finalPhoto)))))
                                .addFormDataPart("title", finalTitle)
                                .addFormDataPart("author", Userinfo.username)
                                .addFormDataPart("text", text.toString())
                                .build();

                        Request request = new Request.Builder()
                                .url("http://129.28.168.201:8888/upload/blog")
                                .post(requestBody)
                                .build();

                        try (Response response = client.newCall(request).execute()) {
                            if (!response.isSuccessful()) {
                                resposetext = response.body().string();
                                throw new IOException("Unexpected code " + response);
                            }
                            resposetext = response.body().string();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }.start();

                while (resposetext.equals("")) ;
                Toast.makeText(getActivity(), "上传成功", Toast.LENGTH_SHORT).show();
                resposetext = "";

            }
        });

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
        linearLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        mRecyView = view.findViewById(R.id.img_recycleview);
        mRecyView.setLayoutManager(linearLayoutManager);
        mRecyView.setAdapter(mPhotoWallAdapter);
        mPhotoWallAdapter.setOnItemClickListener(new PhotoWallAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View v, int position) {
                if (mDatas.size() < 10) {
                    if (position == mDatas.size()) showChoose();
                    else {
                        if (position != -1) {
                            if (!fileUrl.containsKey(mDatas.get(position))) {
                                resposetext = "";
                                try {
                                    uploadImg(mDatas.get(position));
                                    Toast.makeText(getActivity(), "正在上传", Toast.LENGTH_LONG).show();
                                    while (resposetext.equals("")) ;
                                    Toast.makeText(getActivity(), "上传成功", Toast.LENGTH_SHORT).show();
                                    fileUrl.put(mDatas.get(position), JSON.parseObject(resposetext).getString("url"));
                                    resposetext = "";
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                            } else {
                                String mdurl = "\n![](" + fileUrl.get(mDatas.get(position)) + ")\n";
                                input.getText().insert(input.getSelectionStart(), mdurl);
                            }
                        }
                    }
                } else {
                    Toast.makeText(getActivity(), "数量超了", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onItemLongClick(View v, int position) {
                isdelete = !isdelete;
                mPhotoWallAdapter.setIsDelete(isdelete);
                mPhotoWallAdapter.notifyDataSetChanged();
            }
        });
        return view;
    }

    private void uploadImg(final String path) throws IOException {
        final OkHttpClient client = new OkHttpClient();
        new Thread() {
            @RequiresApi(api = Build.VERSION_CODES.KITKAT)
            @Override
            public void run() {
                RequestBody requestBody = new MultipartBody.Builder()
                        .setType(MultipartBody.FORM)
                        .addFormDataPart("img", Userinfo.username,
                                RequestBody.create(MediaType.parse("image/png"), new File(path)))
                        .build();

                Request request = new Request.Builder()
                        .url("http://129.28.168.201:8888/upload/image")
                        .post(requestBody)
                        .build();

                try (Response response = client.newCall(request).execute()) {
                    if (!response.isSuccessful()) {
                        resposetext = response.body().string();
                        throw new IOException("Unexpected code " + response);
                    }
                    resposetext = response.body().string();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }.start();
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }


    private void showChoose() {
        PopuChoooseWindow popuChoooseWindow = new PopuChoooseWindow(getActivity());

        popuChoooseWindow.showAtLocation(mRecyView, Gravity.BOTTOM, 0, 0);

        lightOff();

        popuChoooseWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                lightOn();
            }
        });

        popuChoooseWindow.setOnFeedBackChooseListener(new PopuChoooseWindow.OnFeedBackChooseListener() {
            @Override
            public void onCameraItemClick() {
                //吊起扫描相机
                ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.CAMERA}, 3);
                int preference = ScanConstants.OPEN_CAMERA;
                Intent intent = new Intent(getActivity(), ScanActivity.class);
                intent.putExtra(ScanConstants.OPEN_INTENT_PREFERENCE, preference);
                startActivityForResult(intent, REQUEST_CODE);
            }

            @Override
            public void onAlbumItemClick() {
                Intent albumIntent = new Intent(Intent.ACTION_PICK, null);
                albumIntent.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*");
                startActivityForResult(albumIntent, REQUEST_ALBUM_OK);
            }
        });

    }

    private void lightOff() {
        WindowManager.LayoutParams layoutParams = getActivity().getWindow().getAttributes();
        layoutParams.alpha = 0.3f;
        getActivity().getWindow().setAttributes(layoutParams);

    }

    private void lightOn() {

        WindowManager.LayoutParams layoutParams = getActivity().getWindow().getAttributes();
        layoutParams.alpha = 1.0f;
        getActivity().getWindow().setAttributes(layoutParams);
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case REQUEST_ALBUM_OK:
                if (data != null) {
                    Uri uri = data.getData();
                    Cursor cursor = getActivity().getContentResolver().query(uri, null, null, null, null);
                    cursor.moveToFirst();
                    int idx = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA);
                    mDatas.add(cursor.getString(idx));
                    cursor.close();
                }
                break;
            case REQUEST_CODE:
                Uri uri = data.getExtras().getParcelable(ScanConstants.SCANNED_RESULT);
                Cursor cursor = getActivity().getContentResolver().query(uri, null, null, null, null);
                cursor.moveToFirst();
                int idx = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA);
                mDatas.add(cursor.getString(idx));
                cursor.close();
                break;

        }
        mPhotoWallAdapter.notifyDataSetChanged();
    }

    //压缩图片
    private Bitmap resizePhono(String path) {
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;//仅仅加载图片
        BitmapFactory.decodeFile(path, options);
        double radio = Math.max(options.outWidth * 1.0d / 1024f, options.outHeight * 1.0d / 1024f);
        options.inSampleSize = (int) Math.ceil(radio);
        options.inJustDecodeBounds = false;
        Bitmap photoImg = BitmapFactory.decodeFile(path, options);
        return photoImg;
    }
}
