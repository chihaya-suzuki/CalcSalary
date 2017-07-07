package com.example.swaptv.calcsalary;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Typeface;

import java.util.Collections;
import java.util.Set;

public class SettingPrefUtil {
    // 保存先ファイル名
    public static final String PREF_FILE_NAME = "settings";

    private static final String KEY_MONTH_SALARY = "month.salary";
    private static final String KEY_MONTH_SALARY_DEFAULT = "200000";

    private static final String KEY_WORK_START_TIME = "work.start.time";
    private static final String KEY_WORK_START_TIME_DEFAULT = "0900";

    private static final String KEY_WORK_END_TIME = "work.end.time";
    private static final String KEY_WORK_END_TIME_DEFAULT = "1800";

    // Utilクラスのため、インスタンスを作成させない
    private SettingPrefUtil() {
    }

    public static String getMonthSalary(Context context) {
        // SharedPreferencesを取得
        SharedPreferences sp = context.getSharedPreferences(
                PREF_FILE_NAME, Context.MODE_PRIVATE);

        // SharedPreferencesから設定値を取得する。
        return sp.getString(KEY_MONTH_SALARY, KEY_MONTH_SALARY_DEFAULT);
    }

    public static String getKeyWorkStartTime(Context context) {
        // SharedPreferencesを取得
        SharedPreferences sp = context.getSharedPreferences(
                PREF_FILE_NAME, Context.MODE_PRIVATE);

        // SharedPreferencesから設定値を取得する。
        return sp.getString(KEY_WORK_START_TIME, KEY_WORK_START_TIME_DEFAULT);
    }

    public static String getKeyWorkEndTime(Context context) {
        // SharedPreferencesを取得
        SharedPreferences sp = context.getSharedPreferences(
                PREF_FILE_NAME, Context.MODE_PRIVATE);

        // SharedPreferencesから設定値を取得する。
        return sp.getString(KEY_WORK_END_TIME, KEY_WORK_END_TIME_DEFAULT);
    }
}