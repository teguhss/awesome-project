package com.awesomeproject;

import android.provider.Settings;
import android.support.v7.widget.LinearLayoutCompat;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.facebook.react.uimanager.SimpleViewManager;
import com.facebook.react.uimanager.ThemedReactContext;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.doubleclick.PublisherAdRequest;
import com.google.android.gms.ads.doubleclick.PublisherAdView;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class AdsViewManager extends SimpleViewManager<LinearLayoutCompat> {
    private static final String DFP_AD_UNIT_ID = "/136715487/freesky_test_front_end";

    @Override
    public String getName() {
        return "AdsView";
    }

    @Override
    protected LinearLayoutCompat createViewInstance(final ThemedReactContext reactContext) {
        final PublisherAdView mPublisherAdView = new PublisherAdView(reactContext);
        mPublisherAdView.setAdSizes(AdSize.BANNER);
        mPublisherAdView.setAdUnitId(DFP_AD_UNIT_ID);

        mPublisherAdView.setAdListener(new AdListener() {
            @Override
            public void onAdClosed() {
                super.onAdClosed();
            }

            @Override
            public void onAdFailedToLoad(int i) {
                super.onAdFailedToLoad(i);
                Toast.makeText(reactContext, "Failed Load Ad", Toast.LENGTH_LONG).show();
            }

            @Override
            public void onAdLeftApplication() {
                super.onAdLeftApplication();
            }

            @Override
            public void onAdOpened() {
                super.onAdOpened();
            }

            @Override
            public void onAdLoaded() {
                super.onAdLoaded();
                Toast.makeText(reactContext, "Ad Loaded", Toast.LENGTH_LONG).show();

                int imgWidth = mPublisherAdView.getAdSize().getWidthInPixels(reactContext);
                int imgHeight = mPublisherAdView.getAdSize().getHeightInPixels(reactContext);

                View parent = (View) mPublisherAdView.getParent();
                int left = 0, top = 0;
                int realWidth = parent.getWidth();
                int realHeight = parent.getHeight();

                if(realHeight == 0 || realWidth == 0) {
                    parent.measure(imgWidth, imgHeight);
                    parent.layout(parent.getLeft(), parent.getTop(),
                            parent.getLeft() + imgWidth, parent.getTop() + imgHeight);
                }

                mPublisherAdView.measure(imgWidth, imgHeight);
                mPublisherAdView.layout(left, top, left + imgWidth, top + imgHeight);
                if(realHeight > 0) {
                    mPublisherAdView.setPivotY(0);
                    mPublisherAdView.setScaleY((float)realHeight/(float)imgHeight);
                }
                if(realWidth > 0) {
                    mPublisherAdView.setPivotX(0);
                    mPublisherAdView.setScaleX((float)realWidth/(float)imgWidth);
                }
            }

            @Override
            public void onAdClicked() {
                super.onAdClicked();
            }

            @Override
            public void onAdImpression() {
                super.onAdImpression();
            }
        });

        PublisherAdRequest.Builder adRequestBuilder = new PublisherAdRequest.Builder();
        if(BuildConfig.DEBUG) {
            String android_id = Settings.Secure.getString(reactContext.getContentResolver(), Settings.Secure.ANDROID_ID);
            String deviceId = md5(android_id).toUpperCase();
            adRequestBuilder.addTestDevice(deviceId);
        }
        mPublisherAdView.loadAd(adRequestBuilder.build());

        LinearLayoutCompat mLinearLayoutCompat = new LinearLayoutCompat(reactContext);
        mLinearLayoutCompat.setBackgroundColor(reactContext.getResources().getColor(android.R.color.black));
        mLinearLayoutCompat.addView(mPublisherAdView);
        return mLinearLayoutCompat;
    }

    public static final String md5(final String s) {
        try {
            // Create MD5 Hash
            MessageDigest digest = java.security.MessageDigest
                    .getInstance("MD5");
            digest.update(s.getBytes());
            byte messageDigest[] = digest.digest();

            // Create Hex String
            StringBuffer hexString = new StringBuffer();
            for (int i = 0; i < messageDigest.length; i++) {
                String h = Integer.toHexString(0xFF & messageDigest[i]);
                while (h.length() < 2)
                    h = "0" + h;
                hexString.append(h);
            }
            return hexString.toString();

        } catch (NoSuchAlgorithmException e) {
            Log.e("AdsViewManager", "md5", e);
        }
        return "";
    }
}
