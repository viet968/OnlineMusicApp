package com.example.onlinemusicapp.Notification;

import static com.example.onlinemusicapp.Notification.ApplicationClass.ACTION_NEXT;
import static com.example.onlinemusicapp.Notification.ApplicationClass.ACTION_PAUSE;
import static com.example.onlinemusicapp.Notification.ApplicationClass.ACTION_PLAY;
import static com.example.onlinemusicapp.Notification.ApplicationClass.ACTION_PREV;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class NotificationReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        String actionName = intent.getAction();
        Intent serviceIntent = new Intent(context,MusicService.class);
        if(actionName!=null){
            switch (actionName){
                case ACTION_PLAY:
                    serviceIntent.putExtra("ActionName","play");
                    context.startService(serviceIntent);
                    break;

                case ACTION_NEXT:
                    serviceIntent.putExtra("ActionName","next");
                    context.startService(serviceIntent);
                    break;

                case ACTION_PREV:
                    serviceIntent.putExtra("ActionName","previous");
                    context.startService(serviceIntent);
                    break;

                case ACTION_PAUSE:
                    serviceIntent.putExtra("ActionName","pause");
                    context.startService(serviceIntent);
                    break;

            }
        }
    }
}
