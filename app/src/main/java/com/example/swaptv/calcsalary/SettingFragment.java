package com.example.swaptv.calcsalary;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceFragment;
import android.support.annotation.Nullable;

public class SettingFragment extends PreferenceFragment
        implements SharedPreferences.OnSharedPreferenceChangeListener {

    // 変更イベントをActivityに通知する
    public interface SettingFragmentListener {
        void onSettingChanged();
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

        String salary = sharedPreferences.getString(key, "");
        preference.setSummary(salary);
    }

    private void setWorkStartTimeSummary(SharedPreferences sharedPreferences) {
        String key = getActivity().getString(R.string.work_start_time);

        Preference preference = findPreference(key);

        String time = sharedPreferences.getString(key, "");
        preference.setSummary(time);
    }

    private void setWorkEndTimeSummary(SharedPreferences sharedPreferences) {
        String key = getActivity().getString(R.string.work_end_time);

        Preference preference = findPreference(key);

        String time = sharedPreferences.getString(key, "");
        preference.setSummary(time);
    }

    @Override
    public void onSharedPreferenceChanged(
            SharedPreferences sharedPreferences, String key) {
        // Activityを取得
        Activity activity = getActivity();

        // ActivityがSettingFragmentListenerを実装しているのであれば、通知する
        if (activity instanceof SettingFragmentListener) {
            SettingFragmentListener listener = (SettingFragmentListener)activity;

            // Activityに変更通知
            listener.onSettingChanged();
        }

        // サマリーに反映する
        if (activity.getString(R.string.month_salary).equals(key)) {
            setMonthSalarySummary(sharedPreferences);
        } else if (activity.getString(R.string.work_start_time).equals(key)) {
            setWorkStartTimeSummary(sharedPreferences);
        } else if (activity.getString(R.string.work_end_time).equals(key)) {
            setWorkEndTimeSummary(sharedPreferences);
        }
    }
}
