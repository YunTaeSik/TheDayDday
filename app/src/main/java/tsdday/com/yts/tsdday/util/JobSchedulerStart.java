package tsdday.com.yts.tsdday.util;

import android.app.job.JobInfo;
import android.app.job.JobScheduler;
import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.text.format.DateUtils;
import android.util.Log;

import com.firebase.jobdispatcher.FirebaseJobDispatcher;
import com.firebase.jobdispatcher.GooglePlayDriver;
import com.firebase.jobdispatcher.Job;
import com.firebase.jobdispatcher.Lifetime;
import com.firebase.jobdispatcher.RetryStrategy;
import com.firebase.jobdispatcher.Trigger;

import tsdday.com.yts.tsdday.service.NotificationJobFriebaseService;
import tsdday.com.yts.tsdday.service.NotificationJobService;
import tsdday.com.yts.tsdday.service.NotificationService;
import tsdday.com.yts.tsdday.ui.widget.BigWidget;

import static android.content.Context.JOB_SCHEDULER_SERVICE;


public class JobSchedulerStart {
    private static final int JOB_ID = 1111;

    public static void start(Context context) {
        NotificationCreate.startAndStop(context); //노티파이
        WidgetUpdater.update(context); //위젯

        FirebaseJobDispatcher dispatcher = new FirebaseJobDispatcher(new GooglePlayDriver(context));
        Job myJob = dispatcher.newJobBuilder()
                .setService(NotificationJobFriebaseService.class) // the JobService that will be called
                .setTag("NotificationJobFriebaseService")        // uniquely identifies the job
                .setRecurring(true)
                .setLifetime(Lifetime.FOREVER)
                .setTrigger(Trigger.executionWindow(0, 60))
                .setReplaceCurrent(true)
                .setRetryStrategy(RetryStrategy.DEFAULT_EXPONENTIAL)
                .build();
        dispatcher.mustSchedule(myJob);
    }
}
