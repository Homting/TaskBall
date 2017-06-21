package io.homtingli.github.taskball;

import android.content.Context;
import android.graphics.*;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.view.View;

/**
 * Project Name: todoBall
 * Created By: LiJin
 * Email:jin.li@vub.ac  homtingli@gmail.com
 * By IntelliJ IDEA 15
 * Date: 02/September/2016 Time: 00:13
 */
public class BallCircleView extends View {
    private int width = 170;
    private int height = 170;
    private Paint circlePaint;
    private Paint textPaint;
    private boolean drag = false;
    private Bitmap bitmap;
    private final int CHANGED = 123456;

    //private Paint progressPaint;

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case CHANGED:
                    invalidate();
                    handler.sendEmptyMessageDelayed(CHANGED,1000);
                    break;
                default:
                    break;
            }
        }
    };

    public BallCircleView(Context context) {
        super(context);
        initPaints();
    }

    private void initPaints() {
        circlePaint = new Paint();
        circlePaint.setColor(Color.parseColor("#30E610"));
        circlePaint.setAntiAlias(true);

        textPaint = new Paint();
        textPaint.setTextSize(45);
        textPaint.setColor(Color.YELLOW);
        textPaint.setAntiAlias(true);
        textPaint.setFakeBoldText(true);

        Bitmap src = BitmapFactory.decodeResource(getResources(), R.mipmap.task);
        bitmap = Bitmap.createScaledBitmap(src,width,height,true);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        setMeasuredDimension(width,height);
    }

    public BallCircleView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public BallCircleView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onDraw(Canvas canvas) {

        if(drag) {
            canvas.drawBitmap(bitmap,0,0,null);
        } else {
            String text;
            canvas.drawCircle(width / 2, height / 2, width / 2, circlePaint);

            if(Const.totalnumber==0) {
                text = "0%";
            }else {
                text = String.format("%.1f",((double)Const.finishnumber / Const.totalnumber * 100)) + "%";
            }
            handler.sendEmptyMessageDelayed(CHANGED,1000);
            float textWidth = textPaint.measureText(text);
            float x = width / 2 - textWidth / 2;
            Paint.FontMetrics metrics = textPaint.getFontMetrics();
            float dy = metrics.descent + metrics.ascent;
            dy = -dy / 2;
            float y = height / 2 + dy;
            canvas.drawText(text, x, y, textPaint);
        }
    }

    public void setDragState(boolean b) {
        drag = b;
        // to refresh
        invalidate();
    }

    public int getCircleWidth() {
        return this.width;
    }
    public int getCircleHeight() {
        return this.height;
    }

}
