package com.example.swaptv.calcsalary;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.EditTextPreference;
import android.preference.Preference;
import android.preference.PreferenceFragment;
import android.support.annotation.Nullable;
import android.text.TextUtils;

import org.w3c.dom.Text;

public class SettingFragment extends PreferenceFragment
        implements SharedPreferences.OnSharedPreferenceChangeListener {

    private static final String MONTH_SALARY_DEFAULT = "200000";
    private static final String WORK_START_TIME_DEFAULT = "0900";
    private static final String WORK_END_TIME_DEFAULT = "1800";

    private SharedPreferences mSharedPref;
    private Preference mSalaryPref;
    private Preference mStartTimePref;
    private Preference mEndTimePref;
    private EditTextPreference mSalaryEditTextPref;
    private EditTextPreference mStartTimeEditTextPref;
    private EditTextPreference mEndTimeEditTextPref;
    private String mSalaryKey;
    private String mStartTimeKey;
    private String mEndTimeKey;

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

        mSharedPref = getPreferenceManager().getSharedPreferences();

        mSalaryKey = getActivity().getString(R.string.month_salary);
        mStartTimeKey = getActivity().getString(R.string.work_start_time);
        mEndTimeKey = getActivity().getString(R.string.work_end_time);

        mSalaryPref = findPreference(mSalaryKey);
        mStartTimePref = findPreference(mStartTimeKey);
        mEndTimePref = findPreference(mEndTimeKey);

        mSalaryEditTextPref = (EditTextPreference)findPreference(mSalaryKey);
        mStartTimeEditTextPref = (EditTextPreference)findPreference(mStartTimeKey);
        mEndTimeEditTextPref = (EditTextPreference)findPreference(mEndTimeKey);
    }

    @Override
    public void onResume() {
        super.onResume();
        mSharedPref.registerOnSharedPreferenceChangeListener(this);
    }

    @Override
    public void onPause() {
        super.onPause();
        mSharedPref.unregisterOnSharedPreferenceChangeListener(this);
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
        String salary = mSharedPref.getString(mSalaryKey, MONTH_SALARY_DEFAULT);
        mSalaryPref.setSummary(salary);
        if(TextUtils.isEmpty(mSalaryEditTextPref.getText())) mSalaryEditTextPref.setText(salary);
    }

    private void setWorkStartTimeSummary() {
        String hour = mSharedPref.getString(mStartTimeKey, WORK_START_TIME_DEFAULT).substring(0, 2);
        String min = mSharedPref.getString(mStartTimeKey, WORK_START_TIME_DEFAULT).substring(2, 4);
        String time = hour + ":" + min;
        mStartTimePref.setSummary(time);
        if(TextUtils.isEmpty(mStartTimeEditTextPref.getText()))
            mStartTimeEditTextPref.setText(time.replace(":", ""));
    }

    private void setWorkEndTimeSummary() {
        String hour = mSharedPref.getString(mEndTimeKey, WORK_END_TIME_DEFAULT).substring(0, 2);
        String min = mSharedPref.getString(mEndTimeKey, WORK_END_TIME_DEFAULT).substring(2, 4);
        String time = hour + ":" + min;
        mEndTimePref.setSummary(time);
        if(TextUtils.isEmpty(mEndTimeEditTextPref.getText()))
            mEndTimeEditTextPref.setText(time.replace(":", ""));
    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
        // サマリーに反映する
        if (mSalaryKey.equals(key) && isRightSalary()) {
            setMonthSalarySummary();
        } else if (mStartTimeKey.equals(key) && isRightTime(key)) {
            setWorkStartTimeSummary();
        } else if (mEndTimeKey.equals(key) && isRightTime(key)) {
            setWorkEndTimeSummary();
        }
    }

    private boolean isRightSalary() {
        // 入力された値を取得
        String salary = mSalaryEditTextPref.getText();
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
            String summary = mSalaryPref.getSummary().toString();

            // EditTextにセット
            mSalaryEditTextPref.setText(summary);
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
