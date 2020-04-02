package com.rvtechnologies.grigorahq.utils

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.ContentResolver
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Color
import android.media.RingtoneManager
import android.net.Uri
import android.provider.Settings
import android.util.Log
import androidx.core.app.NotificationCompat
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import com.google.gson.Gson
import com.rvtechnologies.grigorahq.MyApplication
import com.rvtechnologies.grigorahq.R
import com.rvtechnologies.grigorahq.navigation.NavigationActivity
import com.rvtechnologies.grigorahq.services.models.NotificationDataModel


class MyFirebaseMessagingService : FirebaseMessagingService() {
    companion object {

        private val TAG = MyFirebaseMessagingService::class.java.simpleName
        val NOTIFICATION_CHANNEL_ID = "10001"
    }


//    override fun onNewToken(s: String?) {
//        super.onNewToken(s)
//        Log.d(TAG, "onNewToken: " + s!!)
//    }

    override fun onNewToken(p0: String) {
        Log.d(TAG, "onNewToken: " + p0)
    }


//    Bundle[
//    {
//        google.delivered_priority =
//            high, content-available = 1, google.sent_time = 1567422817731, google.ttl = 2419200, google.original_priority = high, sendby = Grigora, body = Order Is Accepted By Driver, data ={ "order_id":3 }, from = 652242170156, type = Grigora, badge = 0, sound = default, google.message_id = 0:1567422817737898%65d2fb8bf9fd7ecd, establishment_detail = Grigora
//    }]

    override fun onMessageReceived(remoteMessage: RemoteMessage) {
        if (remoteMessage?.data != null) {
            showNotificationMessage(remoteMessage)
            MyApplication.instance!!.updateData(
                BroadcastConstants.noti_new_order,
                remoteMessage.data.get("body")
            )
            MyApplication.instance!!.updateData(
                BroadcastConstants.noti_new_scheduled_order,
                remoteMessage.data.get("body")
            )
            MyApplication.instance!!.updateData(
                BroadcastConstants.noti_order_delivered,
                remoteMessage.data.get("body")
            )
            MyApplication.instance!!.updateData(
                BroadcastConstants.noti_book_table,
                remoteMessage.data.get("body")
            )
        }
    }


    internal fun showNotificationMessage(remoteMessage: RemoteMessage) {
        var largeIcon: Bitmap? = null
        if (resources != null)
            largeIcon = BitmapFactory.decodeResource(resources, R.drawable.logo_img)

        val icon = R.drawable.logo_img
        val name: String
        val message: String?
        var pendingIntent: PendingIntent

        name = "Request Received!"
        message = remoteMessage.data.get("body")

        Log.e("noti_response", remoteMessage.data.toString())

        val noti_pojo= Gson().fromJson(remoteMessage.data.get("data").toString(), NotificationDataModel::class.java)


        if (noti_pojo.notificationType == "300" || noti_pojo.notificationType  == "303" || noti_pojo.notificationType == "304") {

            pendingIntent = PendingIntent.getActivity(
                applicationContext,
                0,
                loadAcivity("type","new_order"),
                PendingIntent.FLAG_CANCEL_CURRENT
            )
        }else if(noti_pojo.notificationType  == "301" ){
            pendingIntent = PendingIntent.getActivity(
                applicationContext,
                0,
                loadAcivity("type","Scheduled_order"),
                PendingIntent.FLAG_CANCEL_CURRENT
            )
        }else if(noti_pojo.notificationType  == "305" || noti_pojo.notificationType  == "309" ){
            pendingIntent = PendingIntent.getActivity(
                applicationContext,
                0,
                loadAcivity("type","past_order"),
                PendingIntent.FLAG_CANCEL_CURRENT
            )
        }else if(noti_pojo.notificationType  == "311"){
            pendingIntent = PendingIntent.getActivity(
                applicationContext,
                0,
                loadAcivity("type","book_table"),
                PendingIntent.FLAG_CANCEL_CURRENT
            )
        } else{
            pendingIntent = PendingIntent.getActivity(
                applicationContext,
                0,
                loadAcivity("type","new_order"),
                PendingIntent.FLAG_CANCEL_CURRENT
            )
        }

        val mBuilder = NotificationCompat.Builder(applicationContext)
        val inboxStyle = NotificationCompat.InboxStyle()

        inboxStyle.addLine(message)
        val notificationCompat: NotificationCompat.Builder?
        if (largeIcon != null)

            notificationCompat = mBuilder
                .setSmallIcon(icon)
                .setLargeIcon(largeIcon)
                .setTicker(applicationContext.getString(R.string.app_name))
                .setAutoCancel(true)
                .setContentTitle(name)
                .setContentIntent(pendingIntent)
                .setSound(Settings.System.DEFAULT_NOTIFICATION_URI)
                .setStyle(inboxStyle)
                .setContentText(message)
        else
            notificationCompat = mBuilder
                .setSmallIcon(icon)
                .setTicker(applicationContext.getString(R.string.app_name))
                .setAutoCancel(true)
                .setContentTitle(name)
                .setContentIntent(pendingIntent)
                .setSound(Settings.System.DEFAULT_NOTIFICATION_URI)
                .setStyle(inboxStyle)
                .setContentText(message)


        val notification = notificationCompat.build()

        val mNotificationManager =
            applicationContext.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            val importance = NotificationManager.IMPORTANCE_HIGH
            val notificationChannel = NotificationChannel(
                NOTIFICATION_CHANNEL_ID,
                "NOTIFICATION_CHANNEL_NAME",
                importance
            )
            notificationChannel.enableLights(true)
            notificationChannel.lightColor = Color.RED
            notificationChannel.enableVibration(true)
            notificationChannel.setShowBadge(true)
            mBuilder.setChannelId(NOTIFICATION_CHANNEL_ID)
            mNotificationManager.createNotificationChannel(notificationChannel)
            mNotificationManager.notify(System.currentTimeMillis().toInt(), mBuilder.build())
        } else {
            mNotificationManager.notify(System.currentTimeMillis().toInt(), notification)
        }
    }

    private fun playNotificationSound() {
        try {
            val alarmSound = Uri.parse(
                ContentResolver.SCHEME_ANDROID_RESOURCE
                        + "://" + applicationContext.packageName + "/raw/notification"
            )
            val r = RingtoneManager.getRingtone(applicationContext, alarmSound)
            r.play()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private fun loadAcivity(name: String, value: String): Intent {
        // load fragment
        var i = Intent(this, NavigationActivity::class.java);
        i.putExtra(name, value);
        return i;
    }

}