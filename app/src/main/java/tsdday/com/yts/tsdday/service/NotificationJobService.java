package tsdday.com.yts.tsdday.service;


import android.util.Log;

import com.firebase.jobdispatcher.JobParameters;
import com.firebase.jobdispatcher.JobService;

import tsdday.com.yts.tsdday.util.NotificationCreate;
import tsdday.com.yts.tsdday.util.WidgetUpdater;

public class NotificationJobService extends JobService {
    @Override
    public boolean onStartJob(JobParameters jobParameters) {
        System.out.print("NotificationJobService = onStartJob");
        Log.e(getClass().getName(),"onStartJob");
        NotificationCreate.startAndStop(this);
        WidgetUpdater.update(this);
        return false;
    }

    @Override
    public boolean onStopJob(JobParameters jobParameters) {
        System.out.print("NotificationJobService = onStopJob");
        Log.e(getClass().getName(),"onStopJob");
        NotificationCreate.startAndStop(this);
        WidgetUpdater.update(this);
        return false;
    }
}
