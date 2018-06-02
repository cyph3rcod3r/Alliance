package in.cyberwalker.alliance.data.entity;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.PrimaryKey;
import android.support.annotation.NonNull;

import java.util.Date;
import java.util.UUID;

@Entity
public class User implements Comparable<User> {
    @Ignore
    public static final String DATE_FORMAT = "dd-MM-yyyy HH:mm:ss";
    @Ignore
    public static final String DATE_FORMAT_TIME = "HH:mm";
    @Ignore
    public static final String DATE_FORMAT_GENERAL = "dd-MM-yyyy";

    @PrimaryKey(autoGenerate = true)
    public int id;

    public String name;

    public Date dateOfBirth;

    public Date reminderDate;

    public String phoneNumber;

    public Date lastContactedDate;

    public String notes;

    public String tag;

    public String imgPath;

    public int repeat;

    public boolean isContactImg;

    public boolean haveContacted;

    public String jobTag;

    public static String tagGenerator() {
        return UUID.randomUUID().toString();
    }

    @Override
    public int compareTo(@NonNull User o) {
        return name.compareTo(o.name);
    }
}
