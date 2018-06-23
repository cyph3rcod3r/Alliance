package in.cyberwalker.alliance.ui.receivers;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;

import java.util.Date;

import in.cyberwalker.alliance.R;
import in.cyberwalker.alliance.data.AppDatabase;
import in.cyberwalker.alliance.data.entity.User;
import in.cyberwalker.alliance.data.repo.PeopleRepo;
import in.cyberwalker.alliance.util.DateUtils;
import in.cyberwalker.alliance.util.StringUtils;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

public class ProvokeNotificationReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent != null && intent.hasExtra("ID")) {
            int uId = intent.getIntExtra("ID", -1);
            if (uId > -1) {
                new PeopleRepo(AppDatabase.get(context)).getUserById(uId)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(user -> createNotification(context, user), e -> {
                        });
            }
        }
    }


    private void createNotification(Context context, User user) {
        if (user == null) {
            return;
        }

        String contentText;

        if (DateUtils.isSameDay(new Date(), DateUtils.addDays(user.dateOfBirth, -1))) {
            contentText = "Its " + user.name + "'s Birthday Tomorrow!";
        } else {
            contentText = "Its time to call " + user.name;
        }

        Intent openDialerIntent = new Intent(Intent.ACTION_DIAL);
        if (!StringUtils.isNull(user.phoneNumber)) {
            openDialerIntent.setData(Uri.parse("tel:" + user.phoneNumber));
        }

        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(context, user.jobTag)
                .setSmallIcon(R.drawable.ic_stat_icons_friends)
                .setContentTitle("Hey There!")
                .setContentText(contentText)
                .setAutoCancel(true)
                .setContentIntent(PendingIntent.getActivity(context, user.id,
                        openDialerIntent, 0))
                .setPriority(NotificationCompat.PRIORITY_DEFAULT);
        createNotificationChannel(context, user.jobTag);
        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(context);
        notificationManager.notify(user.id, mBuilder.build());
    }

    private void createNotificationChannel(Context context, String CHANNEL_ID) {
        // Create the NotificationChannel, but only on API 26+ because
        // the NotificationChannel class is new and not in the support library
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = context.getString(R.string.channel_name);
            String description = context.getString(R.string.channel_description);
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID, name, importance);
            channel.setDescription(description);
            // Register the channel with the system; you can't change the importance
            // or other notification behaviors after this
            NotificationManager notificationManager = context.getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
    }
}
