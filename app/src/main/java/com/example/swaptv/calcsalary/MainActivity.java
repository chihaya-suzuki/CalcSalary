package com.example.swaptv.calcsalary;

import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.concurrent.TimeUnit;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
private TextView mResultView;
private Handler mHandler;

private double mMonthSalary;
private double mMillSecSalary;
private long mDateTimeFrom;
private long mDateTimeTo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mResultView = (TextView)findViewById(R.id.resultView);
        findViewById(R.id.sampleButton).setOnClickListener(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        stopCalc();
    }

    @Override
    protected void onResume() {
        super.onResume();
        setTime();
        excuteCalc();
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch(id) {
            case R.id.sampleButton:
                Intent intent = new Intent(this, SettingActivity.class);
                startActivity(intent);
                break;
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_app, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        switch (id) {
            case R.id.action_settings:
                Intent intent = new Intent(this, SettingActivity.class);
                startActivity(intent);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }

    private void setResultView(String text) {
        mResultView.setText(text);
    }

    private void calcTime() {
        // 現在時刻取得
        Calendar nowCalendar = Calendar.getInstance();

        // 現在時刻をミリセカンドに変換
        long dateTimeNow = nowCalendar.getTimeInMillis();

        // 勤務時間中なら計算
        long diff = 0;
        if (mDateTimeFrom < dateTimeNow) {
            if (mDateTimeTo > dateTimeNow) {
                diff = dateTimeNow - mDateTimeFrom;
            } else {
                diff = mDateTimeTo - mDateTimeFrom;
            }
            setResultView((long) (diff * mMillSecSalary) + " 円");
        }
    }

    // 営業日計算
    private int calcWorkDay() {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.DAY_OF_MONTH, 1);
        int targetMonth = calendar.get(Calendar.MONTH);
        int workDayCount = 0;
        while(targetMonth == calendar.get(Calendar.MONTH)) {
            int week = calendar.get(Calendar.DAY_OF_WEEK);
            if(week > 1 && week < 7) workDayCount++;
            calendar.add(Calendar.DAY_OF_MONTH, 1);
        }
        return workDayCount;
    }

    private double calcMillSecSalary() {
        return mMonthSalary / (calcWorkDay() * (mDateTimeTo - mDateTimeFrom));
    }

    private void excuteCalc() {
        mMonthSalary = Double.parseDouble(SettingPrefUtil.getMonthSalary(this));
        mMillSecSalary = calcMillSecSalary();

        mHandler = new Handler();
        final Runnable r = new Runnable() {
            @Override
            public void run() {
                calcTime();
                mHandler.postDelayed(this, 500);
            }
        };
        mHandler.post(r);
    }

    private void stopCalc() {
        mHandler.removeCallbacksAndMessages(null);
    }

    private Calendar setCalendar(String time) {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY, Integer.parseInt(time.substring(0,2)));
        calendar.set(Calendar.MINUTE, Integer.parseInt(time.substring(2,4)));
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        return calendar;
    }

    private void setTime() {
        // 勤怠開始時間作成
        Calendar workStartCalendar = setCalendar(SettingPrefUtil.getKeyWorkStartTime(this));
        Log.d("debug", "calcTime: " + workStartCalendar);

        // 勤怠終了時間作成
        Calendar workEndCalendar = setCalendar(SettingPrefUtil.getKeyWorkEndTime(this));
        Log.d("debug", "calcTime: " + workStartCalendar);

        // ミリ秒に変換
        mDateTimeFrom = workStartCalendar.getTimeInMillis();
        mDateTimeTo = workEndCalendar.getTimeInMillis();
    }
}
