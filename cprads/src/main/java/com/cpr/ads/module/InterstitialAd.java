package com.cpr.ads.module;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.pm.PackageManager;

import com.cpr.ads.Utils.RetrofitClientInstance;
import com.cpr.ads.interfaces.IGetApi;
import com.cpr.ads.model.ItemData;
import com.cpr.ads.model.ListData;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class InterstitialAd {
    private AdsIntersitialAd ads;
    private Activity a;
    private boolean ready=false;

    public InterstitialAd(Activity a) {
        this.a = a;
    }

    public void showAd() {
        if (ads != null) {
            ads.show();
        }
    }

    public void loadAds() {
        setupRetrofit();
    }

    public boolean isLoaded() {
        return ready;
    }

    private void setupRetrofit() {
        IGetApi api = RetrofitClientInstance.getRetrofitInstance().create(IGetApi.class);
        Call<ListData> call = api.getListData();
        call.enqueue(new Callback<ListData>() {
            @Override
            public void onResponse(Call<ListData> call, Response<ListData> response) {
                if (response.isSuccessful()) {
                    try {
                        if (response.body() != null) {
                            getInfo(response.body().getData());
                        }
                    } catch (Exception e) {
                        ready = false;
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(Call<ListData> call, Throwable t) {
                ready = false;
            }
        });
    }

    private void getInfo(List<ItemData> data) {
        ArrayList<ItemData> list = new ArrayList<>();
        for (ItemData itemData : data) {
            if (!isPackageInstalled(itemData.getJsonMemberPackage(), a.getPackageManager())) {
                list.add(itemData);
            }
        }
        if (list.size() > 0) {
            ready = true;
        }
        if (ads==null) {
            ads = new AdsIntersitialAd(a);
        }
        ads.setList(list);
        ads.setOnShowListener(new DialogInterface.OnShowListener() {
            @Override
            public void onShow(DialogInterface dialog) {
                ads.onShow();
            }
        });
        ads.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialog) {
                ads.onDismiss();
                setupRetrofit();
            }
        });
    }

    private boolean isPackageInstalled(String packageName, PackageManager packageManager) {
        boolean found = true;
        try {
            packageManager.getPackageInfo(packageName, 0);
        } catch (PackageManager.NameNotFoundException e) {
            found = false;
        }
        return found;
    }
}
