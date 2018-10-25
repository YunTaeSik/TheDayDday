package tsdday.com.yts.tsdday.service;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import androidx.annotation.Nullable;

import tsdday.com.yts.tsdday.util.NotificationCreate;

public class NotificationService extends Service {
    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        NotificationCreate.startAndStop(this);
        return super.onStartCommand(intent, flags, startId);
    }
}
