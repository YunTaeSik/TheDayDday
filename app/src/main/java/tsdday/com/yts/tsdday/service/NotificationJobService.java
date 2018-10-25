package tsdday.com.yts.tsdday.service;

import android.annotation.TargetApi;
import android.content.IntentFilter;
import android.os.Build;
import android.util.Log;

import com.firebase.jobdispatcher.JobParameters;
import com.firebase.jobdispatcher.JobService;

import tsdday.com.yts.tsdday.R;
import tsdday.com.yts.tsdday.util.NotificationCreate;

public class NotificationJobService extends JobService {
    private ServiceReciver mReciver;

    @Override
    public boolean onStartJob(JobParameters jobParameters) {
        Log.d("NotificationJobService", "onStartJob");
        NotificationCreate.startAndStop(this);
        return true;
    }

    @Override
    public boolean onStopJob(JobParameters jobParameters) {

        return false;
    }
}
