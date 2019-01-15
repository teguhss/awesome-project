package com.awesomeproject;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Handler;
import android.util.Log;

import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.react.uimanager.SimpleViewManager;
import com.facebook.react.uimanager.ThemedReactContext;
import com.facebook.react.uimanager.annotations.ReactProp;
import com.facebook.react.views.image.ReactImageView;

import java.net.URL;

import javax.annotation.Nullable;

public class ReactImageManager extends SimpleViewManager<ReactImageView> {
    /* Interface Listener to start loading the image if the source is set */
    private interface ImgStartListener {
        void startLoading(String imgUrl);
    }

    public static final String REACT_CLASS = "CustomImageView";
    private final @Nullable
    Object mCallerContext = null;
    private ImgStartListener imgStartListener;

    @Override
    public String getName() {
        return REACT_CLASS;
    }

    /* Method which sets the source from React Native */
    @ReactProp(name = "src")
    public void setSrc(ReactImageView view, String uri) {
        imgStartListener.startLoading(uri);
    }

    @Override
    protected ReactImageView createViewInstance(ThemedReactContext reactContext) {

        final ReactImageView reactImageView = new ReactImageView(reactContext, Fresco.newDraweeControllerBuilder(), null, mCallerContext);

        final Handler handler = new Handler();
        imgStartListener = new ImgStartListener() {
            @Override
            public void startLoading(final String imgUrl) {
                startDownloading(imgUrl, handler, reactImageView);

            }
        };

        return reactImageView;
    }

    private void startDownloading(final String imgUrl, final Handler handler, final ReactImageView reactImageView) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    URL url = new URL(imgUrl);
                    final Bitmap bmp = BitmapFactory.decodeStream(url.openConnection().getInputStream());
                    setImage(bmp, handler, reactImageView);
                } catch (Exception e) {
                    Log.e("ReactImageManager", "Error : " + e.getMessage());
                }
            }
        }).start();
    }

    private void setImage(final Bitmap bmp, Handler handler, final ReactImageView reactImageView) {
        handler.post(new Runnable() {
            @Override
            public void run() {
                reactImageView.setImageBitmap(bmp);
            }
        });
    }
}