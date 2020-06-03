package com.werb.mycalendardemo.alarmremind;

import android.app.Activity;
import android.content.Intent;

/**
 * Created by abc-pc on 2020/3/21.
 * 发送广播
 */
public class SendAlarmBroadcast {

    public static void startAlarmService(Activity activity){
        Intent startAlarmServiceIntent = new Intent(activity,AlarmServiceBroadcastReceiver.class);
        activity.sendBroadcast(startAlarmServiceIntent,null);
    }
}
