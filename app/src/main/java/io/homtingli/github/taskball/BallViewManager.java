package io.homtingli.github.taskball;

import android.content.Context;
import android.content.Intent;
import android.graphics.PixelFormat;
import android.graphics.Point;
import android.util.Log;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;

import java.lang.reflect.Field;

/**
 * Project Name: todoBall
 * Created By: LiJin
 * Email:jin.li@vub.ac  homtingli@gmail.com
 * By IntelliJ IDEA 15
 * Date: 02/September/2016 Time: 00:20
 */
public class BallViewManager {
    private Context context;
    private WindowManager wm;//we use windowmanager to manage

    private BallCircleView circleView;
    private WindowManager.LayoutParams params;
    private static BallViewManager instance;

    private BallViewManager(final Context context){
        this.context = context;
        wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        //to get service
        circleView = new BallCircleView(context);
        circleView.setOnTouchListener(circleViewTouchListener);

        circleView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Toast.makeText(context,"hehe", Toast.LENGTH_SHORT).show();
                //circle hide, animation starts
                //wm.removeView(circleView);
                //showFloatMenuView();
                if(Const.totalnumber !=0) {
                    Intent intent = new Intent("io.homtingli.activitytest.ACTION_START");
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    context.startActivity(intent);
                }
            }
        });
    }

    private View.OnTouchListener circleViewTouchListener =
            new View.OnTouchListener() {
                float startx,starty,x,y,dx,dy,x0,y0;
                @Override
                public boolean onTouch(View view, MotionEvent motionEvent) {
                    switch (motionEvent.getAction()) {
                        case MotionEvent.ACTION_DOWN:
                            startx = motionEvent.getRawX();
                            starty = motionEvent.getRawY();
                            x0 = startx;
                            y0 = starty;
                            break;
                        case MotionEvent.ACTION_MOVE:
                            x = motionEvent.getRawX();
                            y = motionEvent.getRawY();
                            dx = x-startx;
                            dy = y-starty;
                            params.x += dx;
                            params.y += dy;
                            circleView.setDragState(true);
                            wm.updateViewLayout(circleView,params);
                            startx = x;
                            starty = y;
                            break;
                        case MotionEvent.ACTION_UP:
                            float x1 = motionEvent.getRawX();
                            if (x1>getScreenWidth()/2) {
                                params.x = getScreenWidth()-circleView.getWidth();
                            } else {
                                params.x = 0;
                            }
                            circleView.setDragState(false);
                            wm.updateViewLayout(circleView,params);
                            // to avoid conflict
                            return Math.abs(x1 - x0) > 6;
                        default:
                            break;
                    }
                    return false;
                }
            };

    //get instnace
    static BallViewManager getInstance(Context context) {
        if(instance == null) {
            //safe
            synchronized (BallViewManager.class) {
                if(instance==null) {
                    instance = new BallViewManager(context);
                }
            }
        }
        return instance;
    }

    public void showFloatCircleView() {
        Log.e("show","cirlee");
        params = new WindowManager.LayoutParams(
                circleView.getCircleWidth(),
                circleView.getCircleHeight(),
                WindowManager.LayoutParams.TYPE_SYSTEM_ALERT,
                //WindowManager.LayoutParams.TYPE_TOAST,
                WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE
                        | WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL,
                PixelFormat.TRANSPARENT
        );

        params.gravity = Gravity.START | Gravity.TOP;
        params.x=0;
        params.y=0;
        wm.addView(circleView,params);
    }

    //to hide
    public void hideFloatCircleView() {
        wm.removeView(circleView);
    }

    public int getScreenWidth() {
        //return wm.getDefaultDisplay().getWidth();
        Point point = new Point();
        wm.getDefaultDisplay().getSize(point);
        return point.y;
    }

    public int getScreenHeight() {
        //return wm.getDefaultDisplay().getHeight();
        Point point = new Point();
        wm.getDefaultDisplay().getSize(point);
        return point.x;
    }

    public int getSuatusHeight(){
        try {
            Class<?> c = Class.forName("com.android.internal.R$dimen");
            Object o = c.newInstance();
            Field f = c.getField("status_bar_height");
            int x = (Integer) f.get(o);
            return context.getResources().getDimensionPixelSize(x);
        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        }
    }
}
