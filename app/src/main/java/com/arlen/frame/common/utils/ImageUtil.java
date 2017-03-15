package com.arlen.frame.common.utils;

import android.content.Context;
import android.net.Uri;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.RequestManager;

/**
 * 加载图片工具类
 */
public class ImageUtil {

    public static void load(Context mContext, Uri uri, ImageView view, int errId) {
        try {
            Glide.with(mContext)
                    .load(uri)
                    .placeholder(errId)
                    .error(errId)
                    .crossFade()
                    .into(view);
        } catch (Exception ex) {
            Logger.d("Glide", ex.getMessage());
        }
    }

    public static void loadGif(Context mContext, int uri, ImageView view) {
        Glide.with(mContext)
                .load(uri)
                .into(view);
    }

    public static void load(Context mContext, String uri, ImageView view) {
        load(mContext, uri, view, 0, 0);
    }

    public static void load(Context mContext, String uri, ImageView view, int errId) {
        load(mContext, uri, view, errId, errId);
    }

    public static void load(Context mContext, String uri, ImageView view, int errId, int placeholder) {
        try {
            Glide.with(mContext)
                    .load(uri)
                    .placeholder(placeholder)
                    .error(errId)
                    .crossFade()
                    .into(view);
        } catch (Exception ex) {
            Logger.d("Glide", ex.getMessage());
        }
    }

    /**
     * 圆角图片
     * @param mContext
     * @param imageView
     * @param placeholder
     */
    public static void showRoundImageByRes(Context mContext, ImageView imageView, int res, int placeholder) {
        RequestManager glideRequest = Glide.with(mContext);
        glideRequest
                .load(res)
                .placeholder(placeholder)
                .transform(new GlideCircleTransform(mContext))
                .into(imageView);
    }

    public static void showRoundImageByUrl(Context mContext, ImageView imageView, String uri, int placeholder) {
        RequestManager glideRequest = Glide.with(mContext);
        glideRequest
                .load(uri)
                .placeholder(placeholder)
                .transform(new GlideCircleTransform(mContext))
                .into(imageView);
    }
}
