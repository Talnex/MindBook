package com.talnex.wrongsbook.Fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.DownloadListener;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.talnex.wrongsbook.R;

public class ComunityFragment extends Fragment {
    WebView webView;

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.communityfragment, container, false);


        webView = view.findViewById(R.id.webview);
        webView.loadUrl("https://www.baidu.com");

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

    //创建DownloadListener (webkit包)
    class MyDownloadListenter implements DownloadListener{

        @Override
        public void onDownloadStart(String url, String userAgent,
                                    String contentDisposition, String mimetype, long contentLength) {
            System.out.println("url ==== >" + url);
        }

    }

}
