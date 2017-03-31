package com.arlen.frame.common.view.banner;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.ColorFilter;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PixelFormat;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;


/****
 * 首页顶部banner ，圆弧
 *
 * @author lmh
 */
public class RoundDrawable extends Drawable {

    private Paint paint;
    private Path path;
    private int height = 36;

    public RoundDrawable(int height) {
        super();
        this.height = height;
        path = new Path();
        paint = new Paint();
        paint.setAntiAlias(true);
        paint.setColor(Color.WHITE);// 设置白色
    }

    public RoundDrawable() {
        super();
        path = new Path();
        paint = new Paint();
        paint.setColor(Color.WHITE);// 设置白色
    }

    @Override
    public void draw(Canvas canvas) {
        drawRound(canvas);
    }

    public void drawRound(Canvas canvas) {
        Rect bounds = getBounds();
        int x1 = bounds.left;
        int y1 = bounds.top;
        int x2 = bounds.right;
        int y2 = bounds.bottom;
//        Logger.d("x1--" + x1 + "--y1--" + y1 + "--x2--" + x2 + "--y2--" + y2);
        path.reset();
        path.moveTo(x1, y2);
        path.quadTo((x1 + x2) / 2, y2 - height, x2, y2); // 设置贝塞尔曲线的控制点坐标和终点坐标
        path.moveTo(x1, y2);
        path.close();
        canvas.drawPath(path, paint);
    }

    @Override
    public void setAlpha(int alpha) {

    }

    @Override
    public void setColorFilter(ColorFilter cf) {

    }

    @Override
    public int getOpacity() {
        return PixelFormat.UNKNOWN;
    }

}
