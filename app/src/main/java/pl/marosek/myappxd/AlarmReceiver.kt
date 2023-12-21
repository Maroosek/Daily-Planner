package pl.marosek.myappxd

import android.app.NotificationManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.media.RingtoneManager
import android.os.Vibrator
import android.widget.Toast
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat

class AlarmReceiver : BroadcastReceiver() {
    var notificationID = 1
    val notificationChannelID = "pl.marosek.myappxd"
    var notifDescription = "TestDescription"

    override fun onReceive(context: Context?, intent: Intent?) {
    //Checking from which fragment the alarm was set
    var source = intent?.getStringExtra("source")
    var title: String?

    if(source == "clockFragment") {
        title = "Clock"
        notifDescription = intent?.getStringExtra("alarmName")!!
        notificationID = intent?.getIntExtra("alarmID", 1)!!
        var state = intent?.getStringExtra("state")

        Toast.makeText(context, "WAKE UP!!!", Toast.LENGTH_SHORT).show()

//        val SnoozeIntent = Intent(context, MainActivity::class.java)
//        //SnoozeIntent.putExtra("source", "Snooze")
//        val broadcastIntent = Intent(context, AlarmReceiver::class.java)
        //MainActivity().Snooze()
//        val SnoozeIntent = Intent(context, MainActivity::class.java)
//        SnoozeIntent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
//        val SnoozePendingIntent = PendingIntent.getActivity(context, 0, SnoozeIntent, PendingIntent.FLAG_ONE_SHOT)

        //NOTIFICATION
        val manager = context!!.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        val notification = NotificationCompat.Builder(context, notificationChannelID)
            .setSmallIcon(R.drawable.ic_launcher_foreground)
            .setContentTitle(title)
            .setContentText(notifDescription)
            //.setContentIntent(SnoozePendingIntent)
            .setPriority(NotificationCompat.PRIORITY_MAX) //Used for heads-up notification and wake up
            //.addAction(R.drawable.ic_launcher_foreground, "Snooze", PendingIntent.getActivity(context, 0, SnoozeIntent, PendingIntent.FLAG_UPDATE_CURRENT))
            .build()

        manager.notify(notificationID, notification)
    }
    if (source == "calendarFragment"){
        title = "Calendar"
        notifDescription = intent?.getStringExtra("eventName")!!
        notificationID = intent.getIntExtra("eventID", 1)
        //Toast.makeText(context, "Test", Toast.LENGTH_SHORT).show()//debugging


        //NOTIFICATION
        val manager = context!!.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        val notification = NotificationCompat.Builder(context, notificationChannelID)
            .setSmallIcon(R.drawable.ic_launcher_foreground)
            .setContentTitle(title)
            .setContentText(notifDescription)
            .setPriority(NotificationCompat.PRIORITY_MAX) //Used for heads-up notification and wake up
            //.setSound(alarmSound) //No need for sound, otherwise it will replace device notofication sound
            .build()

        manager.notify(notificationID, notification)
        }

    }
}