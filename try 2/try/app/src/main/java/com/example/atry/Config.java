package com.example.atry;

import android.app.Activity;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdLoader;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.LoadAdError;
import com.google.android.gms.ads.interstitial.InterstitialAd;
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback;
import com.google.android.gms.ads.nativead.MediaView;
import com.google.android.gms.ads.nativead.NativeAdView;

public class Config {

    public static NativeAdView adView_admob;
    public static com.google.android.gms.ads.nativead.NativeAd admob_nativeAd;
    public static InterstitialAd mInterstitialAd;

    public static AdSize getAdSize(Activity activity) {
        DisplayMetrics displayMetrics = new DisplayMetrics();
        activity.getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        int width = displayMetrics.widthPixels;

        float density = activity.getResources().getDisplayMetrics().density;
        int adWidth = (int) (width / density);

        return AdSize.getCurrentOrientationAnchoredAdaptiveBannerAdSize(activity, adWidth);
    }

    public static void banner(FrameLayout ad_frame, Activity activity) {
        if (SplashScreen.network.equals("admob")){
            AdView adViewAdmob = new AdView(activity);
            AdSize adSize = Config.getAdSize(activity);
            adViewAdmob.setAdSize(adSize);
            adViewAdmob.setAdUnitId(SplashScreen.admob_ban);

            if (ad_frame.getChildCount() != 0) {
                ad_frame.removeAllViews();
            }

            ad_frame.addView(adViewAdmob);
            ad_frame.setVisibility(View.VISIBLE);

            AdRequest adRequestAdmob = new AdRequest.Builder().build();
            adViewAdmob.loadAd(adRequestAdmob);
            adViewAdmob.setAdListener(new AdListener() {
                @Override
                public void onAdFailedToLoad(@NonNull LoadAdError loadAdError) {
                    ad_frame.setVisibility(View.GONE);
                }
            });
        }else{
            //Toast.makeText(this, "other network", Toast.LENGTH_SHORT).show();
        }
    }

    public static void interstitial(Activity activity) {
        AdRequest adRequest = new AdRequest.Builder().build();
        InterstitialAd.load(activity, SplashScreen.admob_int, adRequest, new InterstitialAdLoadCallback() {
            @Override
            public void onAdLoaded(@NonNull InterstitialAd interstitialAd) {
                mInterstitialAd = interstitialAd;
                mInterstitialAd.show(activity);
            }
            @Override
            public void onAdFailedToLoad(@NonNull LoadAdError loadAdError) {
                mInterstitialAd = null;
            }
        });
    }

    public static void admobNative(FrameLayout frameLayout, Activity activity){
        AdLoader.Builder builder = new AdLoader.Builder(activity, SplashScreen.admob_native);
        builder.forNativeAd(
                nativeAd -> {
                    admob_nativeAd = nativeAd;
                    // If this callback occurs after the activity is destroyed, you must call
                    // destroy and return or you may get a memory leak.
                    boolean isDestroyed = false;
                    isDestroyed = activity.isDestroyed();
                    if (isDestroyed || activity.isFinishing() || activity.isChangingConfigurations()) {
                        admob_nativeAd.destroy();
                        return;
                    }
                    adView_admob = (NativeAdView) activity.getLayoutInflater().inflate(R.layout.native_admob, null);
                    // Set the media view.
                    adView_admob.setMediaView((MediaView) adView_admob.findViewById(R.id.ad_media));

                    // Set other ad assets.
                    adView_admob.setHeadlineView(adView_admob.findViewById(R.id.ad_headline));
                    adView_admob.setBodyView(adView_admob.findViewById(R.id.ad_body));
                    adView_admob.setIconView(adView_admob.findViewById(R.id.ad_icon));
                    adView_admob.setCallToActionView(adView_admob.findViewById(R.id.ad_call_to_action));
                    adView_admob.setPriceView(adView_admob.findViewById(R.id.ad_price));
                    adView_admob.setStarRatingView(adView_admob.findViewById(R.id.ad_stars));
                    adView_admob.setStoreView(adView_admob.findViewById(R.id.ad_store));
                    adView_admob.setAdvertiserView(adView_admob.findViewById(R.id.ad_advertiser));

                    ((MediaView) adView_admob.findViewById(R.id.ad_media)).setOnHierarchyChangeListener(new ViewGroup.OnHierarchyChangeListener() {
                        @Override
                        public void onChildViewAdded(View parent, View child) {
                            if (child instanceof ImageView) {
                                ImageView imageView = (ImageView) child;
                                imageView.setAdjustViewBounds(true);
                                imageView.setScaleType(ImageView.ScaleType.FIT_CENTER);
                            }
                        }
                        @Override
                        public void onChildViewRemoved(View parent, View child) {}
                    });

                    // The headline and mediaContent are guaranteed to be in every NativeAd.
                    ((TextView) adView_admob.getHeadlineView()).setText(admob_nativeAd.getHeadline());
                    adView_admob.getMediaView().setMediaContent(admob_nativeAd.getMediaContent());

                    if (admob_nativeAd.getBody() == null) {
                        adView_admob.getBodyView().setVisibility(View.GONE);
                    } else {
                        adView_admob.getBodyView().setVisibility(View.VISIBLE);
                        ((TextView) adView_admob.getBodyView()).setText(admob_nativeAd.getBody());
                    }

                    if (admob_nativeAd.getIcon() == null) {
                        adView_admob.getIconView().setVisibility(View.GONE);
                    } else {
                        ((ImageView) adView_admob.getIconView()).setImageDrawable(
                                admob_nativeAd.getIcon().getDrawable());
                        adView_admob.getIconView().setVisibility(View.VISIBLE);
                    }

                    if (admob_nativeAd.getPrice() == null) {
                        adView_admob.getPriceView().setVisibility(View.INVISIBLE);
                    } else {
                        adView_admob.getPriceView().setVisibility(View.VISIBLE);
                        ((TextView) adView_admob.getPriceView()).setText(admob_nativeAd.getPrice());
                    }

                    if (admob_nativeAd.getStore() == null) {
                        adView_admob.getStoreView().setVisibility(View.INVISIBLE);
                    } else {
                        adView_admob.getStoreView().setVisibility(View.VISIBLE);
                        ((TextView) adView_admob.getStoreView()).setText(admob_nativeAd.getStore());
                    }

                    if (admob_nativeAd.getStarRating() == null) {
                        adView_admob.getStarRatingView().setVisibility(View.INVISIBLE);
                    } else {
                        ((RatingBar) adView_admob.getStarRatingView()).setRating(admob_nativeAd.getStarRating().floatValue());
                        adView_admob.getStarRatingView().setVisibility(View.VISIBLE);
                    }

                    if (admob_nativeAd.getAdvertiser() == null) {
                        adView_admob.getAdvertiserView().setVisibility(View.INVISIBLE);
                    } else {
                        ((TextView) adView_admob.getAdvertiserView()).setText(admob_nativeAd.getAdvertiser());
                        adView_admob.getAdvertiserView().setVisibility(View.VISIBLE);
                    }

                    if (admob_nativeAd.getCallToAction() == null) {
                        adView_admob.getCallToActionView().setVisibility(View.GONE);
                    } else {
                        adView_admob.getCallToActionView().setVisibility(View.VISIBLE);
                        ((Button) adView_admob.getCallToActionView()).setText(admob_nativeAd.getCallToAction());
                    }

                    if (frameLayout.getChildCount() != 0) {
                        frameLayout.removeAllViews();
                    }
                    frameLayout.addView(adView_admob);

                    adView_admob.setNativeAd(admob_nativeAd);
                });

        AdLoader adLoader = builder.withAdListener(new AdListener() {
            @Override
            public void onAdFailedToLoad(@NonNull LoadAdError loadAdError) {
            }

            @Override
            public void onAdLoaded() {
            }
        }).build();

        adLoader.loadAd(new AdRequest.Builder().build());
    }
}
