package com.mcal.drawabledesigner.activity;

import android.annotation.SuppressLint;
import android.view.Menu;
import android.view.MenuItem;

import androidx.appcompat.app.AppCompatActivity;

import com.mcal.drawabledesigner.AppUtils;
import com.mcal.drawabledesigner.R;

@SuppressLint("Registered")
public class BaseActivity extends AppCompatActivity {
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return super.onCreateOptionsMenu(menu);
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.about:
                AppUtils.showAbout(this, R.layout.about);
                break;
        }
        return super.onOptionsItemSelected(item);
    }
}
