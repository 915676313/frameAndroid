package com.arlen.frame.common.view.banner;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;

public class AdViewHolderView implements Holder<Object> {
        private ImageView imageView;

        @Override
        public View createView(Context context) {
            imageView = new ImageView(context);
            imageView.setScaleType(ImageView.ScaleType.FIT_XY);
            return imageView;
        }

        @Override
        public void UpdateUI(Context context, int position, Object advertisement) {
//            ImageUtil.load(context, advertisement.img, imageView);
        }
    }