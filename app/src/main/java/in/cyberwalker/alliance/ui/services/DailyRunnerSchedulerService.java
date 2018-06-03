package in.cyberwalker.alliance.ui.services;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.job.JobParameters;
import android.app.job.JobService;
import android.content.Context;
import android.content.Intent;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

import in.cyberwalker.alliance.data.AppDatabase;
import in.cyberwalker.alliance.data.entity.User;
import in.cyberwalker.alliance.data.repo.PeopleRepo;
import in.cyberwalker.alliance.ui.receivers.ProvokeNotificationReceiver;
import in.cyberwalker.alliance.util.AppUtils;
import in.cyberwalker.alliance.util.DateUtils;
import io.reactivex.Completable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

public class DailyRunnerSchedulerService extends JobService {
    public static final String TAG = "DailyRunnerSchedulerService";
    public static final int SYNC_PER_JOB_ID = TAG.hashCode();
    private PeopleRepo peopleRepo;

    @Override
    public boolean onStartJob(JobParameters params) {
        peopleRepo = new PeopleRepo(AppDatabase.get(this));
        peopleRepo.listUsers(null).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(list -> {
                    processUsers(list);
                }, e -> {
                });
        return false;
    }

    private void processUsers(List<User> list) {
        if (list == null || list.isEmpty()) {
            return;
        }
        for (User user :
                list) {
            if (DateUtils.isSameDay(new Date(), user.reminderDate)) {
                setUserReminder(user.reminderDate, user.id);
                // set new date
                user.lastContactedDate = user.reminderDate;
                user.reminderDate = DateUtils.addDays(user.reminderDate, user.repeat);
                Completable.fromAction(() -> peopleRepo.updateUser(user))
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe();
            }
        }
    }

    private void setUserReminder(Date remiderDate, int id) {
        int duration = 0;
        if (DateUtils.isAfter(remiderDate, new Date())) {
            int hours = DateUtils.hoursBetween(remiderDate, new Date());
            if (hours > 1) {
                duration = hours;
            }
        }

        AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        PendingIntent alarmIntent = PendingIntent.getBroadcast(this,
                id, new Intent(this,
                        ProvokeNotificationReceiver.class).putExtra("ID", id), 0);
        if (alarmManager != null) {
            alarmManager.cancel(alarmIntent);
        }

        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(System.currentTimeMillis());
        if (duration > 0) {
            calendar.set(Calendar.HOUR_OF_DAY, duration);
        } else {
            calendar.set(Calendar.MINUTE, 10);
        }
        if (AppUtils.isGreaterAndEqual23()) {
            alarmManager.setAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), alarmIntent);
        } else {
            alarmManager.setExact(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), alarmIntent);
        }
    }

    @Override
    public boolean onStopJob(JobParameters params) {
        return false;
    }

}