package com.example.swaptv.calcsalary;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.EditTextPreference;
import android.preference.Preference;
import android.preference.PreferenceFragment;
import android.support.annotation.Nullable;

public class SettingFragment extends PreferenceFragment
        implements SharedPreferences.OnSharedPreferenceChangeListener {

    // 変更イベントをActivityに通知する
    public interface SettingFragmentListener {
        void onSettingError();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // ファイル名を指定する
        getPreferenceManager().setSharedPreferencesName(
                SettingPrefUtil.PREF_FILE_NAME);

        addPreferencesFromResource(R.xml.preferences);
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

        setSummary(getPreferenceManager().getSharedPreferences());
    }

    private void setSummary(SharedPreferences sharedPreferences) {
        setMonthSalarySummary(sharedPreferences);
        setWorkStartTimeSummary(sharedPreferences);
        setWorkEndTimeSummary(sharedPreferences);
    }

    private void setMonthSalarySummary(SharedPreferences sharedPreferences) {
        String key = getActivity().getString(R.string.month_salary);

        Preference preference = findPreference(key);

        String salary = sharedPreferences.getString(key, "") + " 円";
        preference.setSummary(salary);
    }

    private void setWorkStartTimeSummary(SharedPreferences sharedPreferences) {
        String key = getActivity().getString(R.string.work_start_time);

        Preference preference = findPreference(key);

        String hour = sharedPreferences.getString(key, "").substring(0, 2);
        String min = sharedPreferences.getString(key, "").substring(2, 4);
        String time = hour + ":" + min;
        preference.setSummary(time);
    }

    private void setWorkEndTimeSummary(SharedPreferences sharedPreferences) {
        String key = getActivity().getString(R.string.work_end_time);

        Preference preference = findPreference(key);

        String hour = sharedPreferences.getString(key, "").substring(0, 2);
        String min = sharedPreferences.getString(key, "").substring(2, 4);
        String time = hour + ":" + min;
        preference.setSummary(time);
    }

    @Override
    public void onSharedPreferenceChanged(
            SharedPreferences sharedPreferences, String key) {
        // Activityを取得
        Activity activity = getActivity();

        // サマリーに反映する
        if (activity.getString(R.string.month_salary).equals(key)) {
            setMonthSalarySummary(sharedPreferences);
        } else if (activity.getString(R.string.work_start_time).equals(key) &&
                isRightTime(key)) {
            setWorkStartTimeSummary(sharedPreferences);
        } else if (activity.getString(R.string.work_end_time).equals(key) &&
                isRightTime(key)) {
            setWorkEndTimeSummary(sharedPreferences);
        }
    }

    private boolean isRightTime(String key) {
        // EditTextPreferenceの取得
        EditTextPreference editTextPreference = (EditTextPreference)
                findPreference(key);

        // 入力された値を取得
        String time = editTextPreference.getText();

        // 4桁のみ許容、時間が23まで、分が59まで
        if(time.length() != 4 || Integer.parseInt(time.substring(0, 2)) > 23 ||
                Integer.parseInt(time.substring(2, 4)) > 59 ) {
            // Activityを取得
            Activity activity = getActivity();

            // ActivityがSettingFragmentListenerを実装しているのであれば、トースト表示のため通知
            if (activity instanceof SettingFragmentListener) {
                SettingFragmentListener listener = (SettingFragmentListener) activity;

                // Activityにエラー通知
                listener.onSettingError();
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
