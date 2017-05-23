//package com.arlen.frame.view.account.view;
//
//import android.content.Context;
//import android.graphics.Bitmap;
//import android.graphics.BitmapFactory;
//import android.graphics.Canvas;
//import android.graphics.Paint;
//import android.graphics.Path;
//import android.view.View;
//
//import com.arlen.frame.R;
//
///**
// * Created by Arlen on 2017/5/19 11:11.
// */
//
//public class CircleView extends View {
//
//    private Bitmap mBitmap;
//    private Paint mPaint;
//
//    public CircleView(Context context) {
//        super(context);
//        mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
//        mBitmap = BitmapFactory.decodeResource(getResources(),R.drawable.ic_a);
//    }
//
//    private int agreen;
//
//    @Override
//    protected void onDraw(Canvas canvas) {
//        super.onDraw(canvas);
//        mPaint.setStyle(Paint.Style.FILL);
//        int width = mBitmap.getWidth();
//        int height = mBitmap.getHeight();
//        int min = Math.min(width,height);
//        Bitmap scaleBitmap = Bitmap.createScaledBitmap(mBitmap,min,min,false);
//        Path path = new Path();
//        path.addCircle(min/2,min/2,min/2, Path.Direction.CW);
//        canvas.clipPath(path);
//
////        canvas1.drawCircle(min/2,min/2,min/2,mPaint);
////        mPaint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
////
////        canvas1.drawBitmap(scaleBitmap,0,0,mPaint);
//        canvas.drawBitmap(scaleBitmap,0,0,mPaint);
//    }
//}
