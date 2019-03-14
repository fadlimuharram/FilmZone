package com.example.filmzone.feature.reminder;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.CompoundButton;
import android.widget.Switch;

import com.example.filmzone.R;
import com.example.filmzone.notification.AlarmReceiver;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ReminderSettingActivity extends AppCompatActivity implements CompoundButton.OnCheckedChangeListener{
    @BindView(R.id.sw_today_reminder)
    Switch swToday;

    @BindView(R.id.sw_daily_reminder)
    Switch swDaily;

    @BindView(R.id.toolbar)
    Toolbar mToolbar;

    private AlarmReceiver alarmReceiver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reminder_setting);

        ButterKnife.bind(this);

        mToolbar.setTitle(getString(R.string.reminder_settings));
        setSupportActionBar(mToolbar);
        if (getSupportActionBar() != null){
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }

        alarmReceiver = new AlarmReceiver();

        swDaily.setOnCheckedChangeListener(this);
        swToday.setOnCheckedChangeListener(this);

        isSwDailyChecked();
        isSwTodayChecked();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if (item.getItemId() == android.R.id.home) {
            finish();
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        if (buttonView.getId() == R.id.sw_today_reminder){
            todayReminder(isChecked);
        }else if (buttonView.getId() == R.id.sw_daily_reminder){
            dailyReminder(isChecked);
        }
    }

    private void todayReminder(boolean isChecked){
        if (isChecked){
            alarmReceiver.setTodayReminder(this);
            swToday.setChecked(true);
        }else {
            alarmReceiver.cancelAlarm(this, AlarmReceiver.ID_TODAY);
            swToday.setChecked(false);
        }

    }

    private void dailyReminder(boolean isChecked){
        if (isChecked){
            alarmReceiver.setDailyReminder(this, AlarmReceiver.ID_DAILY,"Catalogmovie missing you!");
            swDaily.setChecked(true);
        }else{
            alarmReceiver.cancelAlarm(this, AlarmReceiver.ID_DAILY);
            swDaily.setChecked(false);
        }
    }

    private void isSwDailyChecked(){
        boolean isActive = AlarmReceiver.isActive(this,AlarmReceiver.ID_DAILY);
        swDaily.setChecked(isActive);
    }

    private void isSwTodayChecked(){
        boolean isActive = AlarmReceiver.isActive(this,AlarmReceiver.ID_TODAY);
        swToday.setChecked(isActive);
    }
}
