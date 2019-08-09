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

/**
 * Quảng cáo xen kẽ
 */
public class InterstitialAd {
    private AdsIntersitialAd ads;   //dialog quảng cáo
    private Activity a;
    private boolean ready = false;  //Kiểm tra tính sẵn sàng để hiện dialog

    /**
     * Constructor
     *
     * @param a activity
     */
    public InterstitialAd(Activity a) {
        this.a = a;
    }

    /**
     * Hiện dialog quảng cáo lên màn hình
     */
    public void showAd() {
        if (ads != null) {
            ads.show();
        }
    }

    /**
     * Chạy retrofit để lấy thông tin về
     */
    public void loadAds() {
        setupRetrofit();
    }

    /**
     * Kiểm tra đã lấy được thông tin từ api hay chưa
     */
    public boolean isLoaded() {
        return ready;
    }

    /**
     * Khởi tạo retrofit
     */
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

    /**
     * Xử lý thông tin lấy được từ retrofit trả về
     * @param data  data từ api
     */
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
        if (ads == null) {
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

    /**
     * Kiểm tra package đã được cài đặt hay chưa
     * @param packageName   tên package cần ktra
     * @param packageManager    package manager
     * @return kết quả dạng boolean: true- đã được cài đặt, false- chưa được cài
     */
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
