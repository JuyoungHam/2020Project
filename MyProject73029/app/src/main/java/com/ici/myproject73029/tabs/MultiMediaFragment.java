package com.ici.myproject73029.tabs;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.FrameLayout;
import android.widget.Toast;

import com.ici.myproject73029.Constant;
import com.ici.myproject73029.MainActivity;
import com.ici.myproject73029.R;
import com.ici.myproject73029.items.FundamentalItem;


public class MultiMediaFragment extends Fragment implements MainActivity.onBackPressedListener {

    private WebView webView;
    String url;
    private MainActivity mainActivity;

    public void setItem(FundamentalItem item) {
        this.item = item;
    }

    FundamentalItem item;

    public MultiMediaFragment(String url) {
        this.url = url;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.fragment_multi_media, container, false);

        mainActivity = (MainActivity) getActivity();

        mainActivity.getSupportActionBar().hide();

        webView = rootView.findViewById(R.id.webView);
        webView.setWebViewClient(new WebViewClient());
        WebSettings webSettings = webView.getSettings();
        webSettings.setJavaScriptEnabled(false);
        webSettings.setSupportMultipleWindows(false);
        webSettings.setJavaScriptCanOpenWindowsAutomatically(false);
        webSettings.setLoadWithOverviewMode(true);
        webSettings.setUseWideViewPort(true);
        webSettings.setSupportZoom(true);
        webSettings.setBuiltInZoomControls(true);
        webSettings.setDisplayZoomControls(false);
        webSettings.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.SINGLE_COLUMN);
        webSettings.setCacheMode(WebSettings.LOAD_DEFAULT);
        webSettings.setDomStorageEnabled(true);

        webView.loadUrl(url);

        return rootView;
    }

    @Override
    public void onBack() {
        mainActivity.setOnBackPressedListener(null);
        mainActivity.getSupportActionBar().hide();
    }
}