package in.cyberwalker.alliance.mvp.presenter;

import android.content.ContentResolver;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.provider.ContactsContract;
import android.text.TextUtils;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.Date;

import in.cyberwalker.alliance.Defaults;
import in.cyberwalker.alliance.data.AppDatabase;
import in.cyberwalker.alliance.data.entity.User;
import in.cyberwalker.alliance.data.repo.PeopleRepo;
import in.cyberwalker.alliance.mvp.Presenter;
import in.cyberwalker.alliance.mvp.view.AddPeopleView;
import in.cyberwalker.alliance.util.DateUtils;
import io.reactivex.Completable;
import io.reactivex.CompletableObserver;
import io.reactivex.Single;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class AddPeoplePresenter extends Presenter<AddPeopleView> {

    private PeopleRepo peopleRepo;
    public static final int INCREMENT = 2;
    private User user;

    public AddPeoplePresenter(AppDatabase appDatabase) {
        peopleRepo = new PeopleRepo(appDatabase);
        user = new User();
        user.jobTag = User.tagGenerator();
    }

    public void processContactData(Uri data, ContentResolver contentResolver) {
        Disposable d = Single.just(contentResolver.query(data, new String[]{ContactsContract.Contacts._ID,
                ContactsContract.Contacts.DISPLAY_NAME,
                ContactsContract.CommonDataKinds.Phone.NUMBER,
                ContactsContract.Contacts.PHOTO_ID}, null, null, null))
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(cursor -> {
                    user.isContactImg = true;
                    cursor.moveToFirst();
                    view().postNumber(cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER)));
                    view().postName(cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME)));
                    view().postImage();

                });
        addDisposablesToUnsubscribe(d);
    }

    public void save() {
        if (TextUtils.isEmpty(view().getName())) {
            view().showToast("Name Can't be Empty..");
            return;
        }
        user.lastContactedDate = new Date();
        user.name = view().getName();
        user.tag = view().getTag();
        user.repeat = view().getRepeat();
        user.reminderDate = DateUtils.addDays(user.lastContactedDate, user.repeat);
        user.phoneNumber = view().getPhoneNumber();
        user.imgPath = view().getImgPath();

        Completable.fromAction(() -> peopleRepo.saveUser(user))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new CompletableObserver() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onComplete() {
                        view().scheduleNotifierJob(user);
                    }

                    @Override
                    public void onError(Throwable e) {

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
