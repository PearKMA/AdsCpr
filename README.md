# AdsCpr
#Tích hợp vào app:
  - Import module -> chọn đường dẫn tới module này
  - Trong build.gradle level app thêm: implementation project(':/*Tên module*/')
  - Cấp quyền Internet trong manifest


1. Quảng cáo biểu ngữ (banner)
  #1:Trong xml:
    <com.cpr.ads.module.BannerAds
      android:layout_width="match_parent"
      android:layout_height="wrap_content">
    </com.cpr.ads.module.BannerAds>
  #2:Trong code:
    BannerAds bannerAds = new BannerAds(this);
    //add view vào layout ...
2. Quảng cáo xen kẽ:
  #1: add trong oncreate
    InterstitialAd ad = new InterstitialAd(this);
    ad.loadAds();
  #2: Khi muốn show:
    if (ad.isLoaded()){
       ad.showAd();
    }
  
