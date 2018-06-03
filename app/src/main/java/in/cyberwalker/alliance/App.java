package in.cyberwalker.alliance;

import android.app.AlarmManager;
import android.app.Application;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;

import java.util.Calendar;

import in.cyberwalker.alliance.ui.receivers.DailyReceiver;
import in.cyberwalker.alliance.util.AppUtils;

public class App extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        schedulerDailyJob();
    }

    private void schedulerDailyJob() {
        AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        PendingIntent alarmIntent = PendingIntent.getBroadcast(this,
                DailyReceiver.REQUEST_CODE, new Intent(this,
                        DailyReceiver.class), 0);
        if (alarmManager != null) {
            alarmManager.cancel(alarmIntent);
        }

        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(System.currentTimeMillis());
        calendar.set(Calendar.HOUR_OF_DAY, AppUtils.DAILY_NOTIFICATION_TIME);

        alarmManager.setInexactRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(),
                AlarmManager.INTERVAL_DAY, alarmIntent);

    }
}
