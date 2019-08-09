package com.cpr.ads.module;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.net.Uri;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.cpr.ads.R;
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
 * Quảng cáo dạng banner
 */
public class BannerAds extends LinearLayout {
    private LinearLayout container;
    private ImageView ivLogo;
    private TextView btnInstall;
    private TextView tvNameApp, tvAds;
    private Runnable mRun, mLoad;
    private ArrayList<ItemData> listItem;
    private int position = 0;
    private String appPackageName;
    private Handler handle, handleCheck;

    /**
     * Constructor khi thêm bằng code
     *
     * @param context activity
     */
    public BannerAds(Context context) {
        super(context);
        initView(context, null);
    }

    /**
     * Constructor khi tạo trên xml
     * @param context activity
     * @param attrs thuộc tính thêm
     */
    public BannerAds(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        initView(context, attrs);
    }

    /**
     * Constructor khi tạo trên xml
     * @param context activity
     * @param attrs thuộc tính thêm
     */
    public BannerAds(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView(context, attrs);
    }

    /**
     * Chay retrofit để lấy thông tin api
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
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(Call<ListData> call, Throwable t) {
            }
        });
    }

    /**
     * Thông tin lấy được khi retrofit trả kết quả về
     * @param data
     */
    private void getInfo(List<ItemData> data) {
        if (data != null && data.size() > 0) {
            listItem = (ArrayList<ItemData>) data;
            if (mRun != null) {
                handle.removeCallbacks(mRun);
                handle.post(mRun);
            }
        }
    }

    /**
     * Tạo các sự kiện, xử lý
     * @param context   activity
     * @param attrs thuộc tính khi thêm bởi xml
     */
    private void initView(final Context context, AttributeSet attrs) {
        inflate(context, R.layout.layout_banner_ads, this);
        ivLogo = findViewById(R.id.ivLogo);
        container = findViewById(R.id.container);
        btnInstall = findViewById(R.id.btnInstall);
        tvNameApp = findViewById(R.id.tvNameApp);
        tvAds = findViewById(R.id.tvAds);
        listItem = new ArrayList<>();
        handle = new Handler();
        handleCheck = new Handler();
        mLoad = new Runnable() {
            @Override
            public void run() {
                setupRetrofit();

                handleCheck.postDelayed(mLoad, 30000);
            }
        };
        mRun = new Runnable() {
            @Override
            public void run() {
                ItemData item = listItem.get(position);
                if (position >= listItem.size() - 1) {
                    position = 0;
                } else {
                    position++;
                }
                appPackageName = item.getJsonMemberPackage();
                tvNameApp.setText(item.getName());
                tvAds.setTextColor(Color.parseColor(item.getTextTitleColor()));
                setGradient(item);
                Glide.with(context)
                        .asBitmap()
                        .load(item.getLogo())
                        .skipMemoryCache(false)
                        .diskCacheStrategy(DiskCacheStrategy.RESOURCE)
                        .into(ivLogo);
//                ivLogo.setImageBitmap(BitmapFactory.decodeFile(item.getLogo()));
                handle.postDelayed(this, 5000);
            }
        };
        container.setOnTouchListener(new OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        container.setAlpha(0.5f);
                        break;
                    case MotionEvent.ACTION_UP:
                        container.setAlpha(1);
                        try {
                            context.startActivity(new Intent(Intent.ACTION_VIEW,
                                    Uri.parse("market://details?id=" + appPackageName)));
                        } catch (android.content.ActivityNotFoundException anfe) {
                            context.startActivity(new Intent(Intent.ACTION_VIEW,
                                    Uri.parse("https://play.google.com/store/apps/details?id=" + appPackageName)));
                        }
                        break;
                    case MotionEvent.ACTION_CANCEL:
                        container.setAlpha(1);
                        break;
                }
                return true;
            }
        });
        btnInstall.setOnTouchListener(new OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        btnInstall.setAlpha(0.5f);
                        break;
                    case MotionEvent.ACTION_UP:
                        btnInstall.setAlpha(1);
                        try {
                            context.startActivity(new Intent(Intent.ACTION_VIEW,
                                    Uri.parse("market://details?id=" + appPackageName)));
                        } catch (android.content.ActivityNotFoundException anfe) {
                            context.startActivity(new Intent(Intent.ACTION_VIEW,
                                    Uri.parse("https://play.google.com/store/apps/details?id=" + appPackageName)));
                        }
                        if (mLoad != null) {
                            handleCheck.removeCallbacks(mLoad);
                            handleCheck.post(mLoad);
                        }
                        break;
                    case MotionEvent.ACTION_CANCEL:
                        btnInstall.setAlpha(1);
                        break;
                }
                return true;
            }
        });

    }

    /**
     * Set background cho layout
     *
     * @param item đối tượng DataGetted
     */
    private void setGradient(final ItemData item) {
        GradientDrawable gd = new GradientDrawable(
                GradientDrawable.Orientation.RIGHT_LEFT,
                new int[]{Color.parseColor(item.getBgStartColor()), Color.parseColor(item.getBgEndColor())});
        gd.setCornerRadius(10f);
        container.setBackground(gd);
        GradientDrawable gdtext = new GradientDrawable();
        gdtext.setCornerRadius(10);
        if (item.getJsonMemberPackage().equalsIgnoreCase("com.cpr.cpr_video_editor_images_to_video")) {
            gdtext.setStroke(2, Color.WHITE);
        } else {
            gdtext.setStroke(2, Color.parseColor(item.getTextIntallColor()));
        }
        btnInstall.setTextColor(Color.parseColor(item.getTextIntallColor()));
        btnInstall.setBackground(gdtext);
    }

    /**
     * Khi bị gỡ khỏi activity
     * gỡ các handle
     */
    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        if (mRun != null) {
            handle.removeCallbacks(mRun);
        }
        if (mLoad != null) {
            handleCheck.removeCallbacks(mLoad);
        }
    }

    /**
     * Khi được gán vào activity
     * Bắt dầu chạy animation
     */
    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        if (mLoad != null) {
            handleCheck.post(mLoad);
        }
    }
}
