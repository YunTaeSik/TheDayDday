package tsdday.com.yts.tsdday.service;

import android.appwidget.AppWidgetManager;
import android.content.Intent;
import android.util.Log;

import com.firebase.jobdispatcher.JobParameters;
import com.firebase.jobdispatcher.JobService;

import tsdday.com.yts.tsdday.ui.widget.BigWidget;
import tsdday.com.yts.tsdday.util.NotificationCreate;
import tsdday.com.yts.tsdday.util.WidgetUpdater;

public class NotificationJobFriebaseService extends JobService {
    @Override
    public boolean onStartJob(JobParameters job) {
        // Do some work here
        NotificationCreate.startAndStop(this);
        WidgetUpdater.update(this);

        return false; // Answers the question: "Is there still work going on?"
    }

    @Override
    public boolean onStopJob(JobParameters job) {
        NotificationCreate.startAndStop(this);
        return false; // Answers the question: "Should this job be retried?"
    }
}
