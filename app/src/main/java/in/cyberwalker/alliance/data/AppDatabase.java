package in.cyberwalker.alliance.data;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.arch.persistence.room.TypeConverters;
import android.content.Context;

import in.cyberwalker.alliance.data.dao.UserDao;
import in.cyberwalker.alliance.data.entity.User;
import in.cyberwalker.alliance.util.Converters;

@Database(entities = {User.class}, version = 1)
@TypeConverters({Converters.class})
public abstract class AppDatabase extends RoomDatabase {
    private static AppDatabase INSTANCE;
    private static final String DB_NAME = "alliance_room";

    public static AppDatabase get(Context context) {
        if (INSTANCE == null) {
            INSTANCE = Room.databaseBuilder(context, AppDatabase.class, DB_NAME).build();
        }

        return INSTANCE;
    }

    public abstract UserDao userDao();
}
