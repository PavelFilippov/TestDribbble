package ru.com.testdribbble.views;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;

import com.facebook.drawee.drawable.ProgressBarDrawable;

public class ProfileImageProgressBar extends ProgressBarDrawable {


    float level;

    Paint paint = new Paint();

    int color = Color.parseColor("#ea4c89");

    final RectF oval = new RectF();

    int radius = 40;

    public ProfileImageProgressBar(){
        paint.setStrokeWidth(5);
        paint.setStyle(Paint.Style.STROKE);
    }

    @Override
    protected boolean onLevelChange(int level) {
        this.level = level;
        invalidateSelf();
        return true;
    }

    @Override
    public void draw(Canvas canvas) {
        oval.set(canvas.getWidth() / 2 - radius, canvas.getHeight() / 2 - radius,
                canvas.getWidth() / 2 + radius, canvas.getHeight() / 2 + radius);

        drawCircle(canvas, level, color);
    }


    private void drawCircle(Canvas canvas, float level, int color) {
        paint.setColor(color);
        float angle;
        angle = 360 / 1f;
        angle = level * angle;
        canvas.drawArc(oval, 0, Math.round(angle), false, paint);
    }

}
