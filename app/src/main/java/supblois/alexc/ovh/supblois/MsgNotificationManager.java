package supblois.alexc.ovh.supblois;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.support.v4.app.NotificationCompat;

import supblois.alexc.ovh.supblois.dao.MyDbManager;
import supblois.alexc.ovh.supblois.dao.RegAccount;

import static android.content.Context.NOTIFICATION_SERVICE;

/*
 Source: https://www.simplifiedcoding.net/firebase-cloud-messaging-tutorial-android/
 Modifié par Alex
 */

public class MsgNotificationManager {

    private Context mCtx;
    private static MsgNotificationManager mInstance;

    private MsgNotificationManager(Context context) {
        mCtx = context;
    }

    public static synchronized MsgNotificationManager getInstance(Context context) {
        if (mInstance == null) {
            mInstance = new MsgNotificationManager(context);
        }
        return mInstance;
    }

    public void displayNotification(String number, String message) {

        NotificationCompat.Builder mBuilder =
                new NotificationCompat.Builder(mCtx, "messages")
                        .setSmallIcon(R.drawable.notif_icon)
                        .setContentTitle(number)
                        .setContentText(message)
                        .setLights(Color.CYAN, 500, 250)
                        .setVibrate(new long[]{500, 1000})
                        .setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION));


        Intent resultIntent = new Intent(mCtx, Conversation.class);
        resultIntent.setAction("SHOW_MESSAGE");
        resultIntent.putExtra("PHONE_NB", number);
        resultIntent.putExtra("account", number);

        try {
            MyDbManager dbManager = MyDbManager.getInstance(mCtx);
            RegAccount sender = dbManager.getAccountDAO().getByNumber(number);
            resultIntent.putExtra("firstname", sender.getFirstName());
            resultIntent.putExtra("lastname", sender.getLastName());
            resultIntent.putExtra("id", dbManager.getConnectedDAO().getAllConnected().get(0).getNum());
        }catch (Exception ignored){
            System.out.println("i like bagel");
            ignored.printStackTrace();
        }

        PendingIntent pendingIntent = PendingIntent.getActivity(mCtx, 0, resultIntent, PendingIntent.FLAG_UPDATE_CURRENT);

        mBuilder.setContentIntent(pendingIntent);

        NotificationManager mNotifyMgr =
                (NotificationManager) mCtx.getSystemService(NOTIFICATION_SERVICE);

        if (mNotifyMgr != null) {
            mNotifyMgr.notify(1, mBuilder.build());
        }
    }

}