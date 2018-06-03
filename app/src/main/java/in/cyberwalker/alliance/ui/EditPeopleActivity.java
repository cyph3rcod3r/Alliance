package in.cyberwalker.alliance.ui;

import android.Manifest;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.AppCompatEditText;
import android.support.v7.widget.AppCompatSeekBar;
import android.support.v7.widget.AppCompatTextView;
import android.text.Html;
import android.view.Gravity;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.SeekBar;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;

import de.hdodenhof.circleimageview.CircleImageView;
import in.cyberwalker.alliance.BaseActivity;
import in.cyberwalker.alliance.R;
import in.cyberwalker.alliance.data.AppDatabase;
import in.cyberwalker.alliance.data.entity.User;
import in.cyberwalker.alliance.mvp.presenter.EditPeoplePresenter;
import in.cyberwalker.alliance.mvp.view.EditPeopleView;
import in.cyberwalker.alliance.util.DateUtils;
import in.cyberwalker.alliance.util.PermissionHelper;
import in.cyberwalker.alliance.util.Tuple;
import in.cyberwalker.alliance.view.QuickContactHelper;
import in.cyberwalker.alliance.view.RadioButtonWBg;

import static in.cyberwalker.alliance.mvp.presenter.EditPeoplePresenter.INCREMENT;

public class EditPeopleActivity extends BaseActivity<EditPeoplePresenter> implements EditPeopleView {

    private static final int PICK_CONTACT = 20;
    private static final int P_REQ_READ_CONTACT = 21;
    private static final int P_REQ_READ_EXTERNAL_STORAGE = 23;
    private static final int PICK_PHOTO_FOR_AVATAR = 22;
    private AppCompatSeekBar seekBar;
    private AppCompatTextView txvReach;
    private AppCompatEditText edtName;
    private RadioGroup rgTag;
    private CircleImageView contactBadge;
    private HashMap<Integer, Tuple<String, Integer>> reachOutMap = new HashMap<>();
    private String currentTime;
    private Date selectedDateAndTime = new Date();
    private Uri imgUri;
    private AppCompatTextView txvWhen;
    private EditText edPhone, edtBday, edtNotes;
    private Date selectedDOB;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        setContentView(R.layout.edit_people_activity);
        super.onCreate(savedInstanceState);

        presenter = new EditPeoplePresenter(AppDatabase.get(this));
        presenter.bindView(this);
        presenter.onCreate(getIntent().getIntExtra("uId", 0));
        initReachOut();
        initViews();
    }

    private void initViews() {
        seekBar = findViewById(R.id.skbReach);
        txvReach = findViewById(R.id.txvReachOut);
        edtName = findViewById(R.id.edtName);
        edPhone = findViewById(R.id.edtPhone);
        edtBday = findViewById(R.id.edtBday);
        edtNotes = findViewById(R.id.edtNotes);
        txvWhen = findViewById(R.id.txvWhen);
        contactBadge = findViewById(R.id.imvIcon);
        rgTag = findViewById(R.id.rgTag);

        contactBadge.setOnClickListener(v -> {
            if (!PermissionHelper.hasPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE)) {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, P_REQ_READ_EXTERNAL_STORAGE);
                return;
            }
            pickImage();
        });

        seekBar.setMax(INCREMENT);
        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                txvWhen.setText(reachOutMap.get(progress * INCREMENT).a);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        findViewById(R.id.btnSave).setOnClickListener(v -> presenter.save());
        findViewById(R.id.txvReachOut).setOnClickListener(v -> showTimeSelector());
        findViewById(R.id.edtBday).setOnClickListener(v -> showDatePicker());

    }

    @Override
    public void showProgress() {

    }

    @Override
    public void hideProgress() {

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (data != null) {
            imgUri = data.getData();
            if (requestCode == PICK_PHOTO_FOR_AVATAR && resultCode == RESULT_OK) {
                presenter.processAvatarImg(imgUri, getContentResolver());
            }
        }
    }

    @Override
    public void onReceiveUser(User user) {
        if (user != null) {
            edtName.setText(user.name);
            if (user.repeat == 7) {
                seekBar.setProgress(1);
            } else if (user.repeat < 7) {
                seekBar.setProgress(0);
            } else {
                seekBar.setProgress(2);
            }

            if (user.tag.equalsIgnoreCase(getString(R.string.txt_family))) {
                ((RadioButtonWBg) rgTag.getChildAt(0)).setChecked(true);
            } else if (user.tag.equalsIgnoreCase(getString(R.string.txt_family))) {
                ((RadioButtonWBg) rgTag.getChildAt(1)).setChecked(true);
            } else {
                ((RadioButtonWBg) rgTag.getChildAt(2)).setChecked(true);
            }

            currentTime = new SimpleDateFormat(User.DATE_FORMAT_TIME).format(user.reminderDate);
            txvReach.setText(Html.fromHtml(getString(R.string.txt_reach_out_every) + " <b>" + currentTime + "</b>"));

            if (user.phoneNumber != null && user.phoneNumber.length() > 0) {
                edPhone.setText(user.phoneNumber);
            }

            if (user.dateOfBirth != null) {
                edtBday.setText(DateUtils.format(user.dateOfBirth, DateUtils.MONTH_DAY_FORMAT));
            }

            if (user.notes != null && user.notes.length() > 0) {
                edtNotes.setText(user.notes);
            }
            if (user.isContactImg) {
                new QuickContactHelper(this, contactBadge, user.phoneNumber).addThumbnail();
            } else if (user.imgPath != null && user.imgPath.length() > 0) {
                imgUri = Uri.parse(user.imgPath);
                presenter.processAvatarImg(imgUri, getContentResolver());
            }
        }
    }

    @Override
    public void readFromContact() {
        Intent i = new Intent(Intent.ACTION_PICK, ContactsContract.CommonDataKinds.Phone.CONTENT_URI);
        startActivityForResult(i, PICK_CONTACT);
    }

    @Override
    public void showPermissionInfo(int res) {
        new AlertDialog.Builder(this).setMessage(getString(res))
                .setPositiveButton("Allow", (dialog, which) -> {
                    dialog.dismiss();
                    PermissionHelper.openSettings(this);
                }).setNegativeButton("Cancel", (dialog, which) ->
                dialog.dismiss()
        ).create().show();
    }

    @Override
    public String getName() {
        return edtName.getText().toString().trim();
    }

    @Override
    public void showToast(String msg) {
        Toast toast = Toast.makeText(this, msg, Toast.LENGTH_SHORT);
        toast.setGravity(Gravity.CENTER, 0, 0);
        toast.show();
    }

    @Override
    public void pickImage() {
        Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
        intent.setType("image/*");
        startActivityForResult(intent, PICK_PHOTO_FOR_AVATAR);
    }

    @Override
    public void postImage(Bitmap bmp) {
        contactBadge.setImageBitmap(bmp);
    }

    @Override
    public String getTag() {
        return ((RadioButtonWBg) rgTag.findViewById(rgTag.getCheckedRadioButtonId())).getText().toString().trim();
    }

    @Override
    public int getRepeat() {
        return reachOutMap.get(INCREMENT * seekBar.getProgress()).b;
    }

    @Override
    public Date getSelectedDate() {
        return selectedDateAndTime;
    }

    @Override
    public String getNotes() {
        return edtNotes.getText().toString().trim();
    }

    @Override
    public String getPhone() {
        return edPhone.getText().toString().trim();
    }

    @Override
    public Date getDob() {
        return selectedDOB;
    }

    @Override
    public void showDatePicker() {
        Calendar newCalendar = Calendar.getInstance();
        if (selectedDOB == null) {
            newCalendar.setTimeInMillis(System.currentTimeMillis());
        } else {
            newCalendar.setTimeInMillis(selectedDOB.getTime());
        }

        new DatePickerDialog(this, (view, year, monthOfYear, dayOfMonth) -> {
            Calendar newDate = Calendar.getInstance();
            newDate.set(year, monthOfYear, dayOfMonth);
            selectedDOB = newDate.getTime();
            edtBday.setText(DateUtils.format(selectedDOB, DateUtils.MONTH_DAY_FORMAT));
        }, newCalendar.get(Calendar.YEAR), newCalendar.get(Calendar.MONTH), newCalendar.get(Calendar.DAY_OF_MONTH)).show();
    }

    @Override
    public String getPhoneNumber() {
        return edPhone.getText().toString().trim();
    }

    @Override
    public String getImgPath() {
        return String.valueOf(imgUri);
    }

    @Override
    public void onUserSaved() {
        setResult(RESULT_OK);
        finish();
    }

    @Override
    public void scheduleNotifierJob(User user) {
        onUserSaved();
    }

    @Override
    public void showTimeSelector() {
        Calendar mcurrentTime = Calendar.getInstance();
        int hour = mcurrentTime.get(Calendar.HOUR_OF_DAY);
        int minute = mcurrentTime.get(Calendar.MINUTE);
        TimePickerDialog mTimePicker;
        mTimePicker = new TimePickerDialog(this, (timePicker, selectedHour, selectedMinute) -> {
            Date selectDate = DateUtils.getDate(selectedHour, selectedMinute);
            if (selectDate.before(new Date())) {
                Toast.makeText(this, "Please select a time in future", Toast.LENGTH_LONG).show();
                return;
            }
            selectedDateAndTime = selectDate;

            currentTime = new SimpleDateFormat(User.DATE_FORMAT_TIME).format(selectedDateAndTime);
            txvReach.setText(Html.fromHtml(getString(R.string.txt_reach_out_every) + " <b>" + currentTime + "</b>"));
        }, hour, minute, false);//Yes 24 hour time
        mTimePicker.show();
    }

    private void initReachOut() {
        reachOutMap.clear();

        reachOutMap.put(INCREMENT * 0, new Tuple<>(" Every Day", 1));
        reachOutMap.put(INCREMENT * 1, new Tuple<>(" Every Week", 7));
        reachOutMap.put(INCREMENT * 2, new Tuple<>(" Every Month", 30));
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == P_REQ_READ_CONTACT) {
            if (PermissionHelper.hasPermission(this, Manifest.permission.READ_CONTACTS)) {
                readFromContact();
            } else {
                showPermissionInfo(R.string.msg_perm_contact);
            }
        } else if (requestCode == P_REQ_READ_EXTERNAL_STORAGE) {
            if (PermissionHelper.hasPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE)) {
                pickImage();
            } else {
                showPermissionInfo(R.string.msg_perm_read_external);
            }
        }
    }
}
