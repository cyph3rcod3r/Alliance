package in.cyberwalker.alliance.ui.receivers;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.job.JobInfo;
import android.app.job.JobScheduler;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import java.util.Calendar;
import java.util.concurrent.TimeUnit;

import in.cyberwalker.alliance.ui.services.DailyRunnerSchedulerService;
import in.cyberwalker.alliance.util.AppUtils;


public class DailyReceiver extends BroadcastReceiver {
    public static final int REQUEST_CODE = 303;

    @Override
    public void onReceive(Context context, Intent intent) {
        Log.e("Fuck", "Will schedule Job " + DailyRunnerSchedulerService.TAG);
        JobScheduler tm = (JobScheduler) context.getSystemService(Context.JOB_SCHEDULER_SERVICE);
        tm.cancelAll();

        JobInfo.Builder builder = new JobInfo.Builder(DailyRunnerSchedulerService.SYNC_PER_JOB_ID,
                new ComponentName(context.getPackageName(),
                        DailyRunnerSchedulerService.class.getName()));
        builder.setOverrideDeadline(TimeUnit.MINUTES.toMillis(15L));

        // Schedule the repeating job
        tm.schedule(builder.build());
        schedulerDailyJob(context);
    }

    private void schedulerDailyJob(Context context) {
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        PendingIntent alarmIntent = PendingIntent.getBroadcast(context,
                DailyReceiver.REQUEST_CODE, new Intent(context,
                        DailyReceiver.class), 0);
        if (alarmManager != null) {
            alarmManager.cancel(alarmIntent);
        }

        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(System.currentTimeMillis());
        calendar.set(Calendar.HOUR_OF_DAY, AppUtils.DAILY_NOTIFICATION_TIME);

        alarmManager.setInexactRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(),
                AlarmManager.INTERVAL_DAY, alarmIntent);

        Log.e("Fuck", "Daily Job " + calendar.getTime());
    }
}
