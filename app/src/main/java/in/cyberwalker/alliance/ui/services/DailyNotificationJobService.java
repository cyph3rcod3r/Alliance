package in.cyberwalker.alliance.ui.services;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.job.JobParameters;
import android.app.job.JobService;
import android.os.Build;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;

import in.cyberwalker.alliance.R;
import in.cyberwalker.alliance.data.AppDatabase;
import in.cyberwalker.alliance.data.entity.User;
import in.cyberwalker.alliance.data.repo.PeopleRepo;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

public class DailyNotificationJobService extends JobService {
    @Override
    public boolean onStartJob(JobParameters params) {
        if (params != null && params.getExtras().containsKey("uId")) {
            int uId = params.getExtras().getInt("uId");
//            Log.e("Fuck", "I have reached Notification Service");

            new PeopleRepo(AppDatabase.get(this)).getUserById(uId)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(user -> createNotification(user), e -> {
//                        Log.e("Fuck", "cant find user with id "+ uId);
                    });
        }
        return false;
    }

    @Override
    public boolean onStopJob(JobParameters params) {
        return false;
    }

    private void createNotification(User user) {
        if (user == null) {
            return;
        }

//        Log.e("Fuck", "creating notification now for the user "+ user.name);

        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(this, user.jobTag)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentTitle("Hey There!")
                .setContentText("Its time to call " + user.name)
                .setAutoCancel(true)
                .setPriority(NotificationCompat.PRIORITY_DEFAULT);
        createNotificationChannel(user.jobTag);
        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(this);
        notificationManager.notify(user.id, mBuilder.build());
    }

    private void createNotificationChannel(String CHANNEL_ID) {
        // Create the NotificationChannel, but only on API 26+ because
        // the NotificationChannel class is new and not in the support library
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = getString(R.string.channel_name);
            String description = getString(R.string.channel_description);
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID, name, importance);
            channel.setDescription(description);
            // Register the channel with the system; you can't change the importance
            // or other notification behaviors after this
            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
    }
}
