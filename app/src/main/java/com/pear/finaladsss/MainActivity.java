package com.pear.finaladsss;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import com.cpr.ads.module.BannerAds;
import com.cpr.ads.module.InterstitialAd;

public class MainActivity extends AppCompatActivity {
    private FrameLayout fl;
    private BannerAds bannerAds;
    private InterstitialAd ad;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        fl = findViewById(R.id.maincont);
        ad = new InterstitialAd(this);
        ad.loadAds();
    }

    public void prepare(View view) {
        if (ad.isLoaded()){
            ad.showAd();
        }
    }

    public void showAd(View view) {
        if (bannerAds==null){
            bannerAds = new BannerAds(this);
            fl.addView(bannerAds,new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT));
        }
    }
}
