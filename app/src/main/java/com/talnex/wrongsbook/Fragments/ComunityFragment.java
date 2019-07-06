package com.talnex.wrongsbook.Fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.DownloadListener;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;

import com.talnex.wrongsbook.Net.Userinfo;
import com.talnex.wrongsbook.R;

public class ComunityFragment extends Fragment {
    public static WebView webView;

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.communityfragment, container, false);


        webView = view.findViewById(R.id.webview);

//        WebSettings webSettings = webView.getSettings();
//        webSettings.setBuiltInZoomControls(false); // 显示放大缩小 controler
//        webSettings.setSupportZoom(true); // 可以缩放
//        webSettings.setDefaultZoom(WebSettings.ZoomDensity.CLOSE);// 默认缩放模式
//        webSettings.setUseWideViewPort(true);
        webView.loadUrl("http://129.28.168.201:8888");

        webView.setWebViewClient(new WebViewClient(){
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                view.loadUrl(url);
                return true;
            }
        });

        webView.setDownloadListener(new MyDownloadListenter());
        return view;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    //创建DownloadListener (webkit包)，重定义下载功能
    class MyDownloadListenter implements DownloadListener{

        @Override
        public void onDownloadStart(String url, String userAgent,
                                    String contentDisposition, String mimetype, long contentLength) {
            Userinfo.chooseurl = url;
            Toast.makeText(getActivity(),"已复制，快去添加到节点上吧",Toast.LENGTH_LONG).show();
        }

    }

}
