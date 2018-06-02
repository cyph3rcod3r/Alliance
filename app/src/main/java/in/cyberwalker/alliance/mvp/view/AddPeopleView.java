package in.cyberwalker.alliance.mvp.view;

import android.graphics.Bitmap;
import android.support.annotation.StringRes;

import in.cyberwalker.alliance.data.entity.User;
import in.cyberwalker.alliance.mvp.View;

public interface AddPeopleView extends View {

    void postNumber(String phoneNumber);

    void postName(String name);

    void postImage();

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

}
