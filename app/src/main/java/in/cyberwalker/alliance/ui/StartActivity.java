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
                .setBackgroundColor(getResources().getColor(R.color.screen1))
                .setContent("A Simple Relationship Manager. We will remind you to get in touch with people you love")
                .setDrawable(R.drawable.screener_1)
                .setSummary("Friends, Family or Work. We got you cover")
                .build());

        addFragment(new Step.Builder().setTitle("Filter and Sort")
                .setBackgroundColor(getResources().getColor(R.color.screen2))
                .setContent("Find people according to tags and do sorting")
                .setDrawable(R.drawable.screener_2)
                .build());

        addFragment(new Step.Builder().setTitle("Add People")
                .setBackgroundColor(getResources().getColor(R.color.screen3))
                .setContent("Add people who matters the most. Choose time and frequency, we will notify you then")
                .setDrawable(R.drawable.screener_3)
                .setSummary("We know its a busy life. But no more")
                .build());

        addFragment(new Step.Builder().setTitle("Add More")
                .setBackgroundColor(getResources().getColor(R.color.screen4))
                .setContent("Set Phone, Add Birthday or Notes while editing the people if you wish")
                .setDrawable(R.drawable.screener_4)
                .setSummary("Made with Love in India")
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
