package com.example.swaptv.calcsalary;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.EditTextPreference;
import android.preference.Preference;
import android.preference.PreferenceFragment;
import android.support.annotation.Nullable;
import android.text.TextUtils;

public class SettingFragment extends PreferenceFragment
        implements SharedPreferences.OnSharedPreferenceChangeListener {

    private static final String MONTH_SALARY_DEFAULT = "200000";
    private static final String WORK_START_TIME_DEFAULT = "0900";
    private static final String WORK_END_TIME_DEFAULT = "1800";

    private SharedPreferences mSharedPreferences;

    // 変更イベントをActivityに通知する
    public interface SettingFragmentListener {
        void onSettingTimeError();
        void onSettingSalaryError();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // ファイル名を指定する
        getPreferenceManager().setSharedPreferencesName(
                SettingPrefUtil.PREF_FILE_NAME);

        addPreferencesFromResource(R.xml.preferences);

        mSharedPreferences = getPreferenceManager().getSharedPreferences();
    }

    @Override
    public void onResume() {
        super.onResume();
        getPreferenceScreen().getSharedPreferences()
                .registerOnSharedPreferenceChangeListener(this);
    }

    @Override
    public void onPause() {
        super.onPause();
        getPreferenceScreen().getSharedPreferences()
                .unregisterOnSharedPreferenceChangeListener(this);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        setSummary();
    }

    private void setSummary() {
        setMonthSalarySummary();
        setWorkStartTimeSummary();
        setWorkEndTimeSummary();
    }

    private void setMonthSalarySummary() {
        String key = getActivity().getString(R.string.month_salary);

        Preference preference = findPreference(key);

        String salary = mSharedPreferences.getString(key, MONTH_SALARY_DEFAULT);
        preference.setSummary(salary);

        // EditTextPreferenceの取得
        EditTextPreference editTextPreference = (EditTextPreference)
                findPreference(key);

        // EditTextにセット
        editTextPreference.setText(salary);

    }

    private void setWorkStartTimeSummary() {
        String key = getActivity().getString(R.string.work_start_time);

        Preference preference = findPreference(key);

        String hour = mSharedPreferences.getString(key, WORK_START_TIME_DEFAULT).substring(0, 2);
        String min = mSharedPreferences.getString(key, WORK_START_TIME_DEFAULT).substring(2, 4);
        String time = hour + ":" + min;
        preference.setSummary(time);
        // EditTextPreferenceの取得
        EditTextPreference editTextPreference = (EditTextPreference)
                findPreference(key);
        // EditTextにセット
        editTextPreference.setText(time.replace(":", ""));
    }

    private void setWorkEndTimeSummary() {
        String key = getActivity().getString(R.string.work_end_time);

        Preference preference = findPreference(key);

        String hour = mSharedPreferences.getString(key, WORK_END_TIME_DEFAULT).substring(0, 2);
        String min = mSharedPreferences.getString(key, WORK_END_TIME_DEFAULT).substring(2, 4);
        String time = hour + ":" + min;
        preference.setSummary(time);
        // EditTextPreferenceの取得
        EditTextPreference editTextPreference = (EditTextPreference)
                findPreference(key);
        // EditTextにセット
        editTextPreference.setText(time.replace(":", ""));
    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
        // Activityを取得
        Activity activity = getActivity();

        // サマリーに反映する
        if (activity.getString(R.string.month_salary).equals(key) && isRightSalary(key)) {
            setMonthSalarySummary();
        } else if (activity.getString(R.string.work_start_time).equals(key) &&
                isRightTime(key)) {
            setWorkStartTimeSummary();
        } else if (activity.getString(R.string.work_end_time).equals(key) &&
                isRightTime(key)) {
            setWorkEndTimeSummary();
        }
    }

    private boolean isRightSalary(String key) {
        // EditTextPreferenceの取得
        EditTextPreference editTextPreference = (EditTextPreference)
                findPreference(key);

        // 入力された値を取得
        String salary = editTextPreference.getText();
        if (TextUtils.isEmpty(salary) || Integer.parseInt(salary) < 1) {
            // Activityを取得
            Activity activity = getActivity();

            // ActivityがSettingFragmentListenerを実装しているのであれば、トースト表示のため通知
            if (activity instanceof SettingFragmentListener) {
                SettingFragmentListener listener = (SettingFragmentListener) activity;

                // Activityにエラー通知
                listener.onSettingSalaryError();
            }
            // 前回入っていた値を取得
            Preference preference = findPreference(key);
            String summary = preference.getSummary().toString();

            // EditTextにセット
            editTextPreference.setText(summary);
            return false;
        }
        return true;
    }

    private boolean isRightTime(String key) {
        // EditTextPreferenceの取得
        EditTextPreference editTextPreference = (EditTextPreference)
                findPreference(key);

        // 入力された値を取得
        String time = editTextPreference.getText();

        // 4桁のみ許容、時間が23まで、分が59まで
        if(TextUtils.isEmpty(time) || time.length() != 4 ||
                Integer.parseInt(time.substring(0, 2)) > 23 ||
                Integer.parseInt(time.substring(2, 4)) > 59 ) {
            // Activityを取得
            Activity activity = getActivity();

            // ActivityがSettingFragmentListenerを実装しているのであれば、トースト表示のため通知
            if (activity instanceof SettingFragmentListener) {
                SettingFragmentListener listener = (SettingFragmentListener) activity;

                // Activityにエラー通知
                listener.onSettingTimeError();
            }
            // 前回入っていた値を取得
            Preference preference = findPreference(key);
            String summary = preference.getSummary().toString().replace(":", "");

            // EditTextにセット
            editTextPreference.setText(summary);
            return false;
        }
        return true;
    }
}
