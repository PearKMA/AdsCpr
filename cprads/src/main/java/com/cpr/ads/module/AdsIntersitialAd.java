package com.cpr.ads.module;

import android.animation.ObjectAnimator;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.Point;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.GradientDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.cpr.ads.R;
import com.cpr.ads.model.ItemData;

import java.util.ArrayList;

public class AdsIntersitialAd extends Dialog {
    private Activity c;
    private ImageView ivClose, ivPrevious, ivNext, ivCover, ivLogo;
    private TextView tvTitle, tvInstall;
    private LinearLayout llContainer;
    private ArrayList<ItemData> list;
    private Handler handler;
    private int position = 0;
    private String path;
    private ObjectAnimator animation;
    private Animation fadeIn, fadeOut;
    private String appPackageName;
    private Runnable mRun;

    public AdsIntersitialAd(@NonNull Activity context) {
        super(context);
        this.c = context;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        if (getWindow() != null) {
            getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        }
        setContentView(R.layout.layout_interstitial_ads);
        addControls();
        loadImageIntoView();

        if (c != null) {
            addEvents();
        }
    }
    public void setList(ArrayList<ItemData> list){
        this.list = list;
    }

    public void onShow(){
        if (mRun != null) {
            handler.post(mRun);
        }
    }
    public void onDismiss(){
        if (mRun != null) {
            handler.removeCallbacks(mRun);
        }
        list.clear();
    }



    /**
     * Sự kiện trên các view
     */
    private void addEvents() {
        ivClose.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        Glide.with(c)
                                .load(R.drawable.x_clicked)
                                .into(ivClose);
                        break;
                    case MotionEvent.ACTION_UP:
                        Glide.with(c)
                                .load(R.drawable.x_unclicked)
                                .into(ivClose);
                        dismiss();
                        break;
                    case MotionEvent.ACTION_CANCEL:
                        Glide.with(c)
                                .load(R.drawable.x_unclicked);
                        break;
                }
                return true;
            }
        });
        ivPrevious.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        Glide.with(c)
                                .load(R.drawable.ic_ads_clicked)
                                .into(ivPrevious);
                        break;
                    case MotionEvent.ACTION_UP:
                        Glide.with(c)
                                .load(R.drawable.ic_ads_unclicked)
                                .into(ivPrevious);
                        //clicked
                        handler.removeCallbacks(mRun);
                        if (list.size() >= 2) {
                            if (position == 0) {
                                position = list.size() - 2;
                            } else if (position >= 2) {
                                position -= 2;
                            }
                        }
                        handler.post(mRun);
                        break;
                    case MotionEvent.ACTION_CANCEL:
                        Glide.with(c)
                                .load(R.drawable.ic_ads_unclicked)
                                .into(ivPrevious);
                        break;
                }
                return true;
            }
        });
        ivNext.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        Glide.with(c)
                                .load(R.drawable.ic_ads_clicked)
                                .into(ivNext);
                        break;
                    case MotionEvent.ACTION_UP:
                        Glide.with(c)
                                .load(R.drawable.ic_ads_unclicked)
                                .into(ivNext);
                        //clicked
                        handler.removeCallbacks(mRun);
//                        if (position < listItem.size() - 1) {
//                            position++;
//                        }
                        handler.post(mRun);
                        break;
                    case MotionEvent.ACTION_CANCEL:
                        Glide.with(c)
                                .load(R.drawable.ic_ads_unclicked)
                                .into(ivNext);
                        break;
                }
                return true;
            }
        });
        tvInstall.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        tvInstall.setAlpha(0.5f);
                        break;
                    case MotionEvent.ACTION_UP:
                        tvInstall.setAlpha(1);
                        try {
                            c.startActivity(new Intent(Intent.ACTION_VIEW,
                                    Uri.parse("market://details?id=" + appPackageName)));
                        } catch (android.content.ActivityNotFoundException anfe) {
                            c.startActivity(new Intent(Intent.ACTION_VIEW,
                                    Uri.parse("https://play.google.com/store/apps/details?id=" + appPackageName)));
                        }
                        break;
                    case MotionEvent.ACTION_CANCEL:
                        tvInstall.setAlpha(1);
                        break;
                }
                return true;
            }
        });
    }

    private void addControls() {
        ivClose = findViewById(R.id.ivClose);
        ivCover = findViewById(R.id.ivApp);
        ivLogo = findViewById(R.id.ivLogo);
        ivPrevious = findViewById(R.id.btnPrevious);
        ivNext = findViewById(R.id.btnNext);
        tvTitle = findViewById(R.id.tvAds);
        tvInstall = findViewById(R.id.tvInstall);
        llContainer = findViewById(R.id.layoutRoot);
        //handler change animation
        handler = new Handler();

        //flip animation
        animation = ObjectAnimator.ofFloat(ivLogo, "rotationY", 0.0f, 360f);
        animation.setDuration(5000);
        animation.setRepeatCount(ObjectAnimator.INFINITE);
        animation.setInterpolator(new AccelerateDecelerateInterpolator());
        //fade in, fade out
        fadeOut = new AlphaAnimation(1.0f, 0.0f);
        fadeIn = new AlphaAnimation(0.0f, 1.0f);
        fadeIn.setDuration(1000);
        fadeOut.setDuration(1000);
        mRun = new Runnable() {
            @Override
            public void run() {
                if (position == 0) {
                    if (list.size() == 1) {
                        ivPrevious.setVisibility(View.INVISIBLE);
                        ivNext.setVisibility(View.INVISIBLE);
                    } else {
                        ivPrevious.setVisibility(View.INVISIBLE);
                        ivNext.setVisibility(View.VISIBLE);
                    }
                } else if (position == list.size() - 1) {
                    ivNext.setVisibility(View.INVISIBLE);
                    ivPrevious.setVisibility(View.VISIBLE);
                } else {
                    ivPrevious.setVisibility(View.VISIBLE);
                    ivNext.setVisibility(View.VISIBLE);
                }

                ItemData item = list.get(position);
                if (position >= list.size() - 1) {
                    position = 0;
                } else {
                    position++;
                }
                appPackageName = item.getJsonMemberPackage();
                path = item.getCover();
                tvTitle.setTextColor(Color.parseColor(item.getTextTitleColor()));
                setGradient(item);
                if (c != null) {
                    animation.start();
                    Glide.with(c)
                            .asBitmap()
                            .load(item.getLogo())
                            .skipMemoryCache(false)
                            .diskCacheStrategy(DiskCacheStrategy.RESOURCE)
                            .into(ivLogo);
//                    ivLogo.setImageBitmap(BitmapFactory.decodeFile(item.getLogo()));
                    Animation slideIn = AnimationUtils.loadAnimation(c, R.anim.slide_out);
                    slideIn.setAnimationListener(new Animation.AnimationListener() {
                        @Override
                        public void onAnimationStart(Animation animation) {

                        }

                        @Override
                        public void onAnimationEnd(Animation animation) {
                            ivCover.startAnimation(AnimationUtils.loadAnimation(c, R.anim.slide_in));
                            Glide.with(c)
                                    .asBitmap()
                                    .load(path)
                                    .skipMemoryCache(false)
                                    .diskCacheStrategy(DiskCacheStrategy.RESOURCE)
                                    .into(ivCover);
//                            ivApp.setImageBitmap(BitmapFactory.decodeFile(path));
                        }

                        @Override
                        public void onAnimationRepeat(Animation animation) {

                        }
                    });
                    ivCover.startAnimation(slideIn);
                }
                handler.postDelayed(this, 5000);
            }
        };
    }

    private static int convertDpToPixel(float dp, Context context) {
        Resources resources = context.getResources();
        DisplayMetrics metrics = resources.getDisplayMetrics();
        float px = dp * (metrics.densityDpi / 160f);
        return (int) px;
    }

    private void loadImageIntoView() {
        //load image
        if (c != null) {
            //Resize
            Display display = c.getWindowManager().getDefaultDisplay();
            Point size = new Point();
            display.getSize(size);
            int width = size.x;
            ivCover.getLayoutParams().width = width - convertDpToPixel(100, c);
            ivCover.getLayoutParams().height = (width - convertDpToPixel(84, c)) * 313 / 231;
            ivCover.requestLayout();
            llContainer.getLayoutParams().width = width - convertDpToPixel(16, c);
            llContainer.requestLayout();

            Glide.with(c)
                    .asBitmap()
                    .load(R.drawable.x_unclicked)
                    .skipMemoryCache(true)
                    .diskCacheStrategy(DiskCacheStrategy.NONE)
                    .into(ivClose);
            Glide.with(c)
                    .asBitmap()
                    .load(R.drawable.ic_ads_unclicked)
                    .skipMemoryCache(true)
                    .diskCacheStrategy(DiskCacheStrategy.NONE)
                    .into(ivPrevious);
            Glide.with(c)
                    .asBitmap()
                    .load(R.drawable.ic_ads_unclicked)
                    .skipMemoryCache(true)
                    .diskCacheStrategy(DiskCacheStrategy.NONE)
                    .into(ivNext);
        }
    }

    /**
     * Set background cho layout
     *
     * @param item đối tượng DataGetted
     */
    private void setGradient(final ItemData item) {
        fadeOut.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                llContainer.startAnimation(fadeIn);
                GradientDrawable gd = new GradientDrawable(
                        GradientDrawable.Orientation.RIGHT_LEFT,
                        new int[]{Color.parseColor(item.getBgStartColor()), Color.parseColor(item.getBgEndColor())});
                gd.setCornerRadius(10f);
                llContainer.setBackground(gd);
                //
                GradientDrawable gdtext = new GradientDrawable();
                gdtext.setCornerRadius(10);
                if (item.getJsonMemberPackage().equalsIgnoreCase("com.cpr.cpr_video_editor_images_to_video")) {
                    gdtext.setStroke(2, Color.WHITE);
                } else {
                    gdtext.setColor(Color.parseColor(item.getTextIntallColor()));
                }
                tvInstall.setBackground(gdtext);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
        llContainer.startAnimation(fadeOut);
    }
}
