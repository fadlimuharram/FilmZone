package com.example.filmzone.notification;


import android.app.AlarmManager;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.support.v4.app.NotificationCompat;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.widget.Toast;

import com.example.filmzone.BuildConfig;
import com.example.filmzone.R;
import com.example.filmzone.feature.home.MainActivity;
import com.example.filmzone.model.movie.UpComing.UpComing;
import com.example.filmzone.network.NetworkClient;
import com.example.filmzone.network.NetworkStores;
import com.example.filmzone.utils.MovieConverter;
import com.google.gson.Gson;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class AlarmReceiver extends BroadcastReceiver {

    public static final String TYPE_TODAY_REMINDER = "TODAY REMINDER"; // berjalan setiap jam 7 TODO: informasi untuk kembali
    public static final String TYPE_DAILY_REMINDER = "DAILY REMINDER"; // berjalan setiap jam 8 TODO: informasi release hari ini

    private final String time_today_reminder = "10:15";
    private final String time_daily_reminder = "10:16";

    public static final int ID_TODAY = 100;
    public static final int ID_DAILY = 101;

    String EXTRA_TITLE = "EXTRA_TITLE";
    String EXTRA_MESSAGE = "EXTRA_MESSAGE";
    String EXTRA_TYPE = "EXTRA_TYPE";

    private String TODAY_CHANNEL_ID = "Channel 1";
    private String TODAY_CHANNEL_NAME = "Channel Today Reminder";

    private String DAILY_CHANNEL_ID = "Channel 2";
    private String DAILY_CHANNEL_NAME = "Channel Daily Reminder";

    String EXTRA_TODAY_ID = "EXTRA_TODAY_ID";


    @Override
    public void onReceive(Context context, Intent intent) {
        String title = intent.getStringExtra(EXTRA_TITLE);
        String message = intent.getStringExtra(EXTRA_MESSAGE);
        int idType = intent.getIntExtra(EXTRA_TYPE,0);

        String channelId = "";
        String channelName = "";

        if (idType==ID_TODAY){
            channelId = TODAY_CHANNEL_ID;
            channelName = TODAY_CHANNEL_NAME;

        }else {
            channelId = DAILY_CHANNEL_ID;
            channelName = DAILY_CHANNEL_NAME;
        }

        showToast(context,title,message);
        showAlarmNotification(context, title, message, idType, channelId, channelName);
    }

    public void cancelAlarm(Context context, int idType){
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(context, AlarmReceiver.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, idType, intent,0);
        pendingIntent.cancel();
        if (alarmManager != null)alarmManager.cancel(pendingIntent);
        String type = (idType==ID_TODAY) ? TYPE_TODAY_REMINDER : TYPE_DAILY_REMINDER;
        Toast.makeText(context, type + " Repeating dibatalkan", Toast.LENGTH_SHORT).show();

    }



    public void setTodayReminder(final Context context){

        if (isActive(context,ID_TODAY)){
            Toast.makeText(context, "Today Reminder Already Setup", Toast.LENGTH_SHORT).show();
        }else {


            AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
            Intent intent = new Intent(context, AlarmReceiver.class);

            intent.putExtra(EXTRA_TYPE, ID_TODAY);


            Calendar calendar = Calendar.getInstance();
            calendar.set(Calendar.HOUR_OF_DAY, Integer.parseInt(getTime(time_today_reminder)[0]));
            calendar.set(Calendar.MINUTE, Integer.parseInt(getTime(time_today_reminder)[1]));
            calendar.set(Calendar.SECOND,0);

            PendingIntent pendingIntent = PendingIntent.getBroadcast(context, ID_TODAY, intent, 0);
            if (alarmManager!=null){
                alarmManager.setInexactRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), AlarmManager.INTERVAL_DAY, pendingIntent);
            }

            Toast.makeText(context, "Today Reminder set up ", Toast.LENGTH_SHORT).show();

        }



    }

    public void setDailyReminder(Context context, int idType, String message){

        if (isActive(context,idType)){
            Toast.makeText(context, "Daily Reminder Already Setup", Toast.LENGTH_SHORT).show();
        }else {
            String title = (idType==ID_TODAY)?TYPE_TODAY_REMINDER:TYPE_DAILY_REMINDER;


            AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
            Intent intent = new Intent(context, AlarmReceiver.class);
            intent.putExtra(EXTRA_TITLE, title);
            intent.putExtra(EXTRA_MESSAGE, message);
            intent.putExtra(EXTRA_TYPE, ID_DAILY);

            Calendar calendar = Calendar.getInstance();
            calendar.set(Calendar.HOUR_OF_DAY, Integer.parseInt(getTime(time_daily_reminder)[0]));
            calendar.set(Calendar.MINUTE, Integer.parseInt(getTime(time_daily_reminder)[1]));
            calendar.set(Calendar.SECOND,0);

            PendingIntent pendingIntent = PendingIntent.getBroadcast(context, ID_DAILY, intent, 0);
            if (alarmManager!=null){
                alarmManager.setInexactRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), AlarmManager.INTERVAL_DAY, pendingIntent);
            }
            Toast.makeText(context, "Daily Reminder set up ", Toast.LENGTH_SHORT).show();
        }

    }

    private String[] getTime(String time){
        return time.split(":");
    }


    private void showAlarmNotification(Context context, String title, String message, int notifyID, String channel_ID, String chennel_Name){


        if (notifyID == ID_TODAY){
            todayNotification(context, channel_ID, chennel_Name);
        }else {
            dailyNotification(context, title, message, channel_ID, chennel_Name);
        }





    }

    private void showToast(Context context, String title, String message){
        Toast.makeText(context,title + " : " + message, Toast.LENGTH_SHORT).show();
    }

    public static boolean isActive(Context context, int id){
        Intent intent = new Intent(context, AlarmReceiver.class);
        return (PendingIntent.getBroadcast(context, id, intent, PendingIntent.FLAG_NO_CREATE) != null);
    }

    private void todayNotification(final Context context, final String channel_ID, final String chennel_Name){
        Retrofit retrofit = NetworkClient.getRetrofit();

        NetworkStores service = retrofit.create(NetworkStores.class);


        Call<UpComing> upcoming = service.getUpComingAlert(BuildConfig.API_KEY, MovieConverter.langApiDetection());
        upcoming.enqueue(new Callback<UpComing>() {
            @Override
            public void onResponse(Call<UpComing> call, Response<UpComing> response) {
                UpComing upComing = response.body();

                Gson gson = new Gson();

                Log.wtf(AlarmReceiver.class.getSimpleName(), gson.toJson(upComing.getResults().toArray()));
                Date c = Calendar.getInstance().getTime();
                SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
                String now = df.format(c);


                for (int x=0;x<upComing.getResults().size();x++){
                    if (upComing.getResults().get(x).getReleaseDate().equalsIgnoreCase(now)){
                        NotificationManager notificationManagerCompact = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
                        Uri alarmSound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);

                        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, channel_ID)
                                .setSmallIcon(R.drawable.ic_favorite_black_24dp)
                                .setContentTitle(upComing.getResults().get(x).getTitle())
                                .setContentText(upComing.getResults().get(x).getTitle() + " Has Been Release")
                                .setColor(ContextCompat.getColor(context,android.R.color.transparent))
                                .setVibrate(new long[]{1000,1000,1000,1000,1000})
                                .setSound(alarmSound);

                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
                            NotificationChannel channel = new NotificationChannel(channel_ID,chennel_Name,NotificationManager.IMPORTANCE_DEFAULT);
                            channel.enableVibration(true);
                            channel.setVibrationPattern(new long[]{1000,1000,1000,1000,1000});
                            builder.setChannelId(channel_ID);

                            if (notificationManagerCompact != null){
                                notificationManagerCompact.createNotificationChannel(channel);
                            }

                        }

                        PendingIntent intent = PendingIntent.getActivity(context, 0, new Intent(context, MainActivity.class), PendingIntent.FLAG_UPDATE_CURRENT);
                        builder.setContentIntent(intent);

                        Notification notification = builder.build();

                        if (notificationManagerCompact != null){
                            notificationManagerCompact.notify(ID_TODAY, notification);
                        }
                    }
                }


            }

            @Override
            public void onFailure(Call<UpComing> call, Throwable t) {

            }
        });
    }

    private void dailyNotification(Context context, String title, String message, String channel_ID, String chennel_Name){
        NotificationManager notificationManagerCompact = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        Uri alarmSound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);

        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, channel_ID)
                .setSmallIcon(R.drawable.ic_favorite_black_24dp)
                .setContentTitle(title)
                .setContentText(message)
                .setColor(ContextCompat.getColor(context,android.R.color.transparent))
                .setVibrate(new long[]{1000,1000,1000,1000,1000})
                .setSound(alarmSound);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            NotificationChannel channel = new NotificationChannel(channel_ID,chennel_Name,NotificationManager.IMPORTANCE_DEFAULT);
            channel.enableVibration(true);
            channel.setVibrationPattern(new long[]{1000,1000,1000,1000,1000});
            builder.setChannelId(channel_ID);

            if (notificationManagerCompact != null){
                notificationManagerCompact.createNotificationChannel(channel);
            }

        }

        PendingIntent intent = PendingIntent.getActivity(context, 0, new Intent(context, MainActivity.class), PendingIntent.FLAG_UPDATE_CURRENT);
        builder.setContentIntent(intent);

        Notification notification = builder.build();

        if (notificationManagerCompact != null){
            notificationManagerCompact.notify(ID_TODAY, notification);
        }
    }



}

