package in.cyberwalker.alliance.mvp.presenter;

import android.content.ContentResolver;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.text.TextUtils;
import android.util.Log;

import java.io.FileNotFoundException;
import java.io.InputStream;

import in.cyberwalker.alliance.Defaults;
import in.cyberwalker.alliance.data.AppDatabase;
import in.cyberwalker.alliance.data.entity.User;
import in.cyberwalker.alliance.data.repo.PeopleRepo;
import in.cyberwalker.alliance.mvp.Presenter;
import in.cyberwalker.alliance.mvp.view.EditPeopleView;
import in.cyberwalker.alliance.util.DateUtils;
import io.reactivex.Completable;
import io.reactivex.CompletableObserver;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class EditPeoplePresenter extends Presenter<EditPeopleView> {
    private PeopleRepo peopleRepo;
    public static final int INCREMENT = 2;
    private User user;

    public EditPeoplePresenter(AppDatabase appDatabase) {
        peopleRepo = new PeopleRepo(appDatabase);
    }

    public void onCreate(int uId) {
        Disposable d = peopleRepo.getUserById(uId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(user -> {
                    EditPeoplePresenter.this.user = user;
                    view().onReceiveUser(user);
                });
        addDisposablesToUnsubscribe(d);

    }

    public void save() {
        if (TextUtils.isEmpty(view().getName())) {
            view().showToast("Name Can't be Empty..");
            return;
        }
        user.lastContactedDate = view().getSelectedDate();
        user.name = view().getName();
        user.tag = view().getTag();
        user.repeat = view().getRepeat();
        user.reminderDate = DateUtils.addDays(user.lastContactedDate, user.repeat);
        user.phoneNumber = view().getPhoneNumber();
        user.imgPath = view().getImgPath();
        user.notes = view().getNotes();
        user.phoneNumber = view().getPhoneNumber();
        user.dateOfBirth = view().getSelectedDate();

        Completable.fromAction(() -> peopleRepo.updateUser(user))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new CompletableObserver() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        Log.e("Edit", "subscribed");
                    }

                    @Override
                    public void onComplete() {
                        Log.e("Edit", "complete");
                        view().scheduleNotifierJob(user);
                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.e("Edit", "error" + e.getMessage());
                    }

                });
    }

    public void processAvatarImg(Uri data, ContentResolver contentResolver) {
        try {
            contentResolver.takePersistableUriPermission(data, Intent.FLAG_GRANT_READ_URI_PERMISSION | Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
            InputStream is = contentResolver.openInputStream(data);
            if (is != null) {
                Bitmap bmp = BitmapFactory.decodeStream(is);
                bmp = Bitmap.createScaledBitmap(bmp, Defaults.THUMB, Defaults.THUMB, false);
                view().postImage(bmp);
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }
}
