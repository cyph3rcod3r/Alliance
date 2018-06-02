package in.cyberwalker.alliance.view;

import android.content.Context;
import android.graphics.Color;
import android.support.annotation.Nullable;
import android.support.v7.widget.AppCompatRadioButton;
import android.util.AttributeSet;

import in.cyberwalker.alliance.R;


/**
 * Created by Cyph3r on 21/06/17.
 */

public class RadioButtonWBg extends AppCompatRadioButton {

    public RadioButtonWBg(Context context) {
        super(context);
        init();
    }

    public RadioButtonWBg(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public RadioButtonWBg(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        setButtonDrawable(null);
    }

    @Override
    public void setChecked(boolean checked) {
        super.setChecked(checked);
        if (checked) {
            setBackgroundResource(R.drawable.background_round_brand_selected);
            setTextColor(Color.WHITE);
        } else {
            setBackgroundResource(R.drawable.background_round_stroke);
            setTextColor(getResources().getColor(R.color.colorAccent));
        }
    }
}