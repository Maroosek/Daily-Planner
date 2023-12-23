package pl.marosek.myappxd

import android.app.NotificationManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.widget.Toast
import androidx.core.app.NotificationCompat

class AlarmReceiver : BroadcastReceiver() {
    var notificationID = 1
    val notificationChannelID = "pl.marosek.myappxd"
    var notifDescription = "TestDescription"

    override fun onReceive(context: Context?, intent: Intent?) {
    //Checking from which fragment the alarm was set
    val source = intent?.getStringExtra("source")
    var title: String?

    if(source == "clockFragment") {
        title = "Clock"
        notifDescription = intent.getStringExtra("alarmName")!!
        notificationID = intent.getIntExtra("alarmID", 1)

        Toast.makeText(context, "TIME UP!!!", Toast.LENGTH_SHORT).show()
        //Intent for opening main activity
        val snoozeIntent = Intent(context, MainActivity::class.java)
        snoozeIntent.putExtra("source", "snoozeFragment")
        val snoozePendingIntent = PendingIntent.getActivity(context, 0, snoozeIntent, PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT)

        //NOTIFICATION
        val manager = context!!.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        val notification = NotificationCompat.Builder(context, notificationChannelID)
            .setSmallIcon(R.drawable.ic_launcher_foreground) // custom icon
            .setContentTitle(title)
            .setContentText("[Set Snooze] $notifDescription")
            .setAutoCancel(true)//closes notification after clicking on it
            .setContentIntent(snoozePendingIntent)//opens snooze fragment after clicking on notification
            .setPriority(NotificationCompat.PRIORITY_MAX) //Used for heads-up notification and wake up
            .build()

        manager.notify(notificationID, notification)
    }
    if (source == "calendarFragment"){
        title = "Calendar"
        notifDescription = intent.getStringExtra("eventName")!!
        notificationID = intent.getIntExtra("eventID", 1)
        //Toast.makeText(context, "Test", Toast.LENGTH_SHORT).show()//debugging

        //NOTIFICATION
        val manager = context!!.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        val notification = NotificationCompat.Builder(context, notificationChannelID)
            .setSmallIcon(R.drawable.ic_launcher_foreground) // custom icon
            .setContentTitle(title)
            .setContentText(notifDescription)
            .setPriority(NotificationCompat.PRIORITY_MAX) //Used for heads-up notification and wake up
            //.setSound(alarmSound) //No need for sound, otherwise it will replace device notofication sound
            .build()

        manager.notify(notificationID, notification)
        }
    }
}