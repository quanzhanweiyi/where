package com.creation.where.activity;

import android.os.Bundle;
import android.view.View;

import com.creation.where.R;
import com.creation.where.base.BaseActivity;

public class SettingsActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
    }

    public void back(View view) {
        finish();
    }
}
