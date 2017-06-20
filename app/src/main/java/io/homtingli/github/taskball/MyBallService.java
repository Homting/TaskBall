package io.homtingli.github.taskball;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

/**
 * Project Name: todoBall
 * Created By: LiJin
 * Email:jin.li@vub.ac  homtingli@gmail.com
 * By IntelliJ IDEA 15
 * Date: 02/September/2016 Time: 00:19
 */
public class MyBallService extends Service {
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        BallViewManager manager = BallViewManager.getInstance(this);
        manager.showFloatCircleView();
    }
}
