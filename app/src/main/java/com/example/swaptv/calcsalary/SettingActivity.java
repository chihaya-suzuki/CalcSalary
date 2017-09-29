package com.example.swaptv.calcsalary;

import android.support.v7.app.ActionBar;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.widget.Toast;

public class SettingActivity extends AppCompatActivity
        implements SettingFragment.SettingFragmentListener{
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getFragmentManager().beginTransaction()
                .replace(android.R.id.content, new SettingFragment()).commit();

        // getActionBar()だとnullが返ってきてしまう
        // getSupportActionBar()の戻り値はandroid.support.v7.app.ActionBar型
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setHomeButtonEnabled(true);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            this.finish();
            return true;
        } else {
            return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onSettingTimeError() {
        Toast.makeText(this, R.string.time_error_message, Toast.LENGTH_LONG).show();
    }

    @Override
    public void onSettingSalaryError() {
        Toast.makeText(this, R.string.salary_error_message, Toast.LENGTH_LONG).show();
    }
}