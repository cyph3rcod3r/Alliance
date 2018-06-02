package in.cyberwalker.alliance.ui;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomSheetBehavior;
import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.RadioGroup;

import java.util.List;
import java.util.Random;

import in.cyberwalker.alliance.BaseActivity;
import in.cyberwalker.alliance.Defaults;
import in.cyberwalker.alliance.R;
import in.cyberwalker.alliance.data.AppDatabase;
import in.cyberwalker.alliance.data.entity.User;
import in.cyberwalker.alliance.mvp.presenter.HomePresenter;
import in.cyberwalker.alliance.mvp.view.HomeView;
import in.cyberwalker.alliance.ui.adapters.HomeAdapter;
import in.cyberwalker.alliance.view.RadioButtonWBg;

public class HomeActivity extends BaseActivity<HomePresenter> implements HomeView {

    private static final int REQ_ADD_USER = 101;
    private AppCompatTextView txvQuotes;
    private RecyclerView recyclerView;
    private ProgressBar progressBar;
    private HomeAdapter adapter;
    private BottomSheetBehavior<View> bottomSheetBehavior;
    private RadioGroup rgFilter;
    private RadioGroup rgSort;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        setContentView(R.layout.activity_home);
        super.onCreate(savedInstanceState);
        initViews();

        presenter = new HomePresenter(AppDatabase.get(this));
        presenter.bindView(this);
        presenter.init();

        getSupportActionBar().setTitle("");
    }

    private void initViews() {
        progressBar = findViewById(R.id.pbr);
        recyclerView = findViewById(R.id.recyclerView);
        findViewById(R.id.btnAddPeople).setOnClickListener(v ->
                startActivityForResult(new Intent(this, AddPeopleActivity.class), REQ_ADD_USER));
        txvQuotes = findViewById(R.id.txvQuotes);

        View bottomSheet = findViewById(R.id.bottom_home);
        bottomSheetBehavior = BottomSheetBehavior.from(bottomSheet);
        bottomSheetBehavior.setState(BottomSheetBehavior.STATE_HIDDEN);

        adapter = new HomeAdapter(this);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);

        findViewById(R.id.imv_options).setOnClickListener(v -> showHideBottomSheet(BottomSheetBehavior.STATE_EXPANDED));
        findViewById(R.id.imv_cancel).setOnClickListener(v -> showHideBottomSheet(BottomSheetBehavior.STATE_HIDDEN));
        findViewById(R.id.btnApply).setOnClickListener(v -> {
            showHideBottomSheet(BottomSheetBehavior.STATE_HIDDEN);
            presenter.beginFilteringSorting();
        });

        findViewById(R.id.btnClear).setOnClickListener(v -> {
            showHideBottomSheet(BottomSheetBehavior.STATE_HIDDEN);
            ((RadioButtonWBg) rgSort.getChildAt(0)).setChecked(true);
            ((RadioButtonWBg) rgFilter.getChildAt(0)).setChecked(true);
            presenter.beginFilteringSorting();
        });

        rgFilter = findViewById(R.id.rg_filter);
        rgSort = findViewById(R.id.rg_sort);

        RadioButtonWBg rbAll = findViewById(R.id.rbAll);
        RadioButtonWBg rbDate = findViewById(R.id.rbDate);

        rbAll.setChecked(true);
        rbDate.setChecked(true);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQ_ADD_USER && resultCode == RESULT_OK) {
            adapter.clear();
            presenter.init();
        }
    }

    @Override
    public void showProgress() {
        progressBar.setVisibility(View.VISIBLE);
    }

    @Override
    public void hideProgress() {
        progressBar.setVisibility(View.GONE);
    }

    @Override
    public void onUserList(List<User> list, boolean filterApplied) {
        adapter.addUsers(list);
        if (!list.isEmpty()) {
            txvQuotes.setVisibility(View.GONE);
        } else {
            if (filterApplied) {
                txvQuotes.setText("That list is empty. Either clear filters or change again!");
            } else {
                int idx = new Random().nextInt(Defaults.quotes.length);
                txvQuotes.setText(Defaults.quotes[idx]);
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    txvQuotes.setTypeface(getResources().getFont(R.font.aclonica));
                }
            }
            txvQuotes.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void showHideBottomSheet(int state) {
        bottomSheetBehavior.setState(state);
    }

    @Override
    public String filterVal() {
        return ((RadioButtonWBg) rgFilter.findViewById(rgFilter.getCheckedRadioButtonId())).getText().toString().trim();
    }

    @Override
    public String sortVal() {
        return ((RadioButtonWBg) rgSort.findViewById(rgSort.getCheckedRadioButtonId())).getText().toString().trim();
    }

    @Override
    public int getAdapterSize() {
        return adapter.getItemCount();
    }

    @Override
    public void onBackPressed() {
        if (bottomSheetBehavior.getState() == BottomSheetBehavior.STATE_EXPANDED) {
            showHideBottomSheet(BottomSheetBehavior.STATE_HIDDEN);
        } else {
            super.onBackPressed();
        }
    }
}
