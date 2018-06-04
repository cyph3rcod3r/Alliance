package in.cyberwalker.alliance.ui;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;

import com.hololo.tutorial.library.Step;
import com.hololo.tutorial.library.TutorialActivity;

import in.cyberwalker.alliance.R;

public class StartActivity extends TutorialActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        SharedPreferences sharedPref = getPreferences(Context.MODE_PRIVATE);
        int shown = sharedPref.getInt("shown", 0);

        if (shown > 0) {
            startActivity(new Intent(this, HomeActivity.class));
            finish();
            return;
        }

        addFragment(new Step.Builder().setTitle("Welcome To Alliance")
                .setBackgroundColor(getResources().getColor(R.color.green))
                .setContent("A Relationship Manager")
                .setDrawable(R.mipmap.ic_launcher_round)
                .setSummary("Summary")
                .build());
        addFragment(new Step.Builder().setTitle("Welcome To Alliance")
                .setBackgroundColor(getResources().getColor(R.color.colorAccent))
                .setContent("A Relationship Manager")
                .setSummary("Summary")
                .build());
    }

    @Override
    public void finishTutorial() {
        super.finishTutorial();

        SharedPreferences sharedPref = getPreferences(Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putInt("shown", 1);
        editor.commit();

        startActivity(new Intent(this, HomeActivity.class));
    }
}
