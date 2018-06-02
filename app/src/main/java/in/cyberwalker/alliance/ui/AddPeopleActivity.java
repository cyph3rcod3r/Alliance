package in.cyberwalker.alliance.ui;

import android.Manifest;
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
import android.view.Menu;
import android.view.MenuItem;
import android.widget.RadioGroup;
import android.widget.SeekBar;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;

import de.hdodenhof.circleimageview.CircleImageView;
import in.cyberwalker.alliance.BaseActivity;
import in.cyberwalker.alliance.R;
import in.cyberwalker.alliance.data.AppDatabase;
import in.cyberwalker.alliance.data.entity.User;
import in.cyberwalker.alliance.mvp.presenter.AddPeoplePresenter;
import in.cyberwalker.alliance.mvp.view.AddPeopleView;
import in.cyberwalker.alliance.util.PermissionHelper;
import in.cyberwalker.alliance.util.Tuple;
import in.cyberwalker.alliance.view.QuickContactHelper;
import in.cyberwalker.alliance.view.RadioButtonWBg;

import static in.cyberwalker.alliance.mvp.presenter.AddPeoplePresenter.INCREMENT;

public class AddPeopleActivity extends BaseActivity<AddPeoplePresenter> implements AddPeopleView {

    private static final int PICK_CONTACT = 20;
    private static final int P_REQ_READ_CONTACT = 21;
    private static final int P_REQ_READ_EXTERNAL_STORAGE = 23;
    private static final int PICK_PHOTO_FOR_AVATAR = 22;
    private AppCompatSeekBar seekBar;
    private AppCompatTextView txvReach;
    private AppCompatEditText edtName;
    private RadioGroup rgTag;
    private CircleImageView contactBadge;
    private String phoneNumber;
    private HashMap<Integer, Tuple<String, Integer>> reachOutMap = new HashMap<>();
    private String currentTime;
    private Uri imgUri;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        setContentView(R.layout.add_people_activity);
        super.onCreate(savedInstanceState);
        presenter = new AddPeoplePresenter(AppDatabase.get(this));
        presenter.bindView(this);
        initReachOut();
        initViews();
    }

    private void initViews() {
        currentTime = new SimpleDateFormat(User.DATE_FORMAT_TIME).format(new Date());
        seekBar = findViewById(R.id.skbReach);
        txvReach = findViewById(R.id.txvReachOut);
        edtName = findViewById(R.id.edtName);
        contactBadge = findViewById(R.id.imvIcon);
        rgTag = findViewById(R.id.rgTag);

        findViewById(R.id.btnContact).setOnClickListener(v -> {

            if (!PermissionHelper.hasPermission(this, Manifest.permission.READ_CONTACTS)) {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_CONTACTS}, P_REQ_READ_CONTACT);
                return;
            }

            readFromContact();
        });

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
                txvReach.setText(Html.fromHtml(getString(R.string.txt_reach_out_every, currentTime)
                        + "<b>"
                        + reachOutMap.get(progress * INCREMENT).a
                        + "</b>"));
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        seekBar.setProgress(1);
        ((RadioButtonWBg) rgTag.getChildAt(1)).setChecked(true);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.add_people, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        super.onOptionsItemSelected(item);
        if (item.getItemId() == R.id.mIdSave) {
            presenter.save();
            return true;
        } else {
            return false;
        }
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
            if (requestCode == PICK_CONTACT && resultCode == RESULT_OK) {
                presenter.processContactData(imgUri, getContentResolver());
            } else if (requestCode == PICK_PHOTO_FOR_AVATAR && resultCode == RESULT_OK) {
                presenter.processAvatarImg(imgUri, getContentResolver());
            }
        }
    }

    @Override
    public void postNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    @Override
    public void postName(String name) {
        edtName.setText(name);
    }

    @Override
    public void postImage() {
        new QuickContactHelper(this, contactBadge, phoneNumber).addThumbnail();
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
    public String getPhoneNumber() {
        return phoneNumber;
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
