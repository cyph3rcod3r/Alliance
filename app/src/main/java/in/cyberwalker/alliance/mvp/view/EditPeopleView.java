package in.cyberwalker.alliance.mvp.view;

import android.graphics.Bitmap;
import android.support.annotation.StringRes;

import java.util.Date;

import in.cyberwalker.alliance.data.entity.User;
import in.cyberwalker.alliance.mvp.View;

public interface EditPeopleView extends View {
    void onReceiveUser(User user);

    void readFromContact();

    void showPermissionInfo(@StringRes int resId);

    String getName();

    void showToast(String msg);

    void pickImage();

    void postImage(Bitmap bmp);

    String getTag();

    int getRepeat();

    String getPhoneNumber();

    String getImgPath();

    void onUserSaved();

    void scheduleNotifierJob(User user);

    void showTimeSelector();

    Date getSelectedDate();

    String getNotes();

    String getPhone();

    Date getDob();

    void showDatePicker();
}
