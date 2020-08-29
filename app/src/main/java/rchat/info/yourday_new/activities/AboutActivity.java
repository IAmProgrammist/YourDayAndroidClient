package rchat.info.yourday_new.activities;

import android.os.Bundle;
import android.util.DisplayMetrics;
import android.widget.LinearLayout;
import android.widget.ScrollView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import rchat.info.yourday_new.R;

public class AboutActivity extends AppCompatActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.about);
        LinearLayout top = findViewById(R.id.start_empty);
        LinearLayout bottom = findViewById(R.id.end_empty);
        DisplayMetrics display = getResources().getDisplayMetrics();
        int height = display.heightPixels;
        top.setPadding(0, 0, 0, ((int) height / 6));
        bottom.setPadding(0, 0, 0, 100);
        ScrollView listView = findViewById(R.id.scroller);
        listView.setEnabled(false);
        listView.setClickable(false);
    }
}
