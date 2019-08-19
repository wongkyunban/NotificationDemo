package com.jk.mytestapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.core.app.Person;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;

public class AlertDetailsActivity extends AppCompatActivity {
    // 渠道名
    private static final String CHANNEL_NAME = "聊天消息通知";
    // 渠道ID
    private static final String CHANNEL_ID = "message";
    // 渠道描述
    private static final String CHANNEL_DESCRIPTION = "这是一个聊天消息的通知";
    // 通知的唯一ID
    private static final int NOTIFICATION_UNIQUE_ID = 2000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alert_details);

        // 如果你的设备的是Android8.0或以上，那么在你发送一个通知之前，必须先注册你的通知渠道,注册方式就是通知传递一个NotificationChannel实例到createNotificationChannel()
        // 这种注册可以在Application里注册，重复注册是安全的，因为当已存在时，它就不会再做什么操作了
        createNotificationChannel();
        // 设置点击通知的行为,通常是打开一个对应的activity。为了实现这一目标，我们必须用PendingIntent定义一个content intent，
        // 然后把它传递到setContentIntent里去
        Intent intent = new Intent(this,ChatActivity.class);
        // 下面的setFlags意味着打开一个新的任务，而不是将AlertDetailsActivity添加到已存在的任务和回退栈里
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_CLEAR_TASK);
        PendingIntent pendingIntent = PendingIntent.getActivity(this,0,intent,0);

        // 通过NotificationCompat.Builder创建通知的内容和渠道
        Bundle bundle = new Bundle();
        bundle.putInt("icon",R.mipmap.ic_launcher);
        bundle.putString("name","Me");
        final NotificationCompat.Builder builder = new NotificationCompat.Builder(this,CHANNEL_ID);
        builder.setSmallIcon(R.mipmap.ic_launcher)// 小图标
                .setContentTitle("聊天消息")// 通知标题
                .setContentText("这是一条普通的通知,这是一条普通的通知,这是一条普通的通知,这是一条普通的通知,这是一条普通的通知,")// 通知内容
                .setStyle(new NotificationCompat.MessagingStyle(Person.fromBundle(bundle))
                        .setConversationTitle("新消息")
                        .addMessage("Hi",5000,Person.fromBundle(bundle))
                .addMessage("What's the matter?",4000,Person.fromBundle(bundle))
                .addMessage("not much",3000,Person.fromBundle(bundle)))// 如果不设置setStyle那么它将会在一行显示，显示不完的，则以省略号表示
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)// 优先级
                .setContentIntent(pendingIntent)
                .setAutoCancel(true);// 当用户点击了就会自动消失
        // 显示通知
        final NotificationManagerCompat notificationManagerCompat = NotificationManagerCompat.from(this);
        // 要记住这个ID，当你需要更新通知或移除通知时就需要这个ID
        notificationManagerCompat.notify(NOTIFICATION_UNIQUE_ID,builder.build());


    }
    private void createNotificationChannel(){
        // Android8.0(API 26)
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID,CHANNEL_NAME,importance);
            channel.setDescription(CHANNEL_DESCRIPTION);
            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            if(notificationManager != null) {
                notificationManager.createNotificationChannel(channel);
            }

        }
    }

}
