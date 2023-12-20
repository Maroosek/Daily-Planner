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
        Toast.makeText(context, "WAKE UP!!!", Toast.LENGTH_SHORT).show()
        //Sets default alarm sound
        var alarmSound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_ALARM)
        //If there is no default alarm sound, sets default notification sound
        if (alarmSound == null) {
            alarmSound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)
        }
        val ringtone = RingtoneManager.getRingtone(context, alarmSound)
        ringtone.play()
        //Starts the vibration and toast
//        val vibration = context!!.getSystemService(Context.VIBRATOR_SERVICE) as Vibrator
//        vibration.vibrate(4000)



//        val SnoozeIntent = Intent(context, MainActivity::class.java)
//        //SnoozeIntent.putExtra("source", "Snooze")
//        val broadcastIntent = Intent(context, AlarmReceiver::class.java)
        //MainActivity().Snooze()

        //NOTIFICATION
        val manager = context!!.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        val notification = NotificationCompat.Builder(context, notificationChannelID)
            .setSmallIcon(R.drawable.ic_launcher_foreground)
            .setContentTitle(title)
            .setContentText(notifDescription)
            .setPriority(NotificationCompat.PRIORITY_HIGH) //Used for heads-up notification
            //.addAction(R.drawable.ic_launcher_foreground, "Snooze", PendingIntent.getActivity(context, 0, SnoozeIntent, PendingIntent.FLAG_UPDATE_CURRENT))
            .build()

        manager.notify(notificationID, notification)
    }
    if (source == "calendarFragment"){
        title = "Calendar"
        notifDescription = intent?.getStringExtra("eventName")!!
        notificationID = intent.getIntExtra("eventID", 1)
        //Toast.makeText(context, "Test", Toast.LENGTH_SHORT).show()//debugging
        //var alarmSound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)

        //NOTIFICATION
        val manager = context!!.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        val notification = NotificationCompat.Builder(context, notificationChannelID)
            .setSmallIcon(R.drawable.ic_launcher_foreground)
            .setContentTitle(title)
            .setContentText(notifDescription)
            .setPriority(NotificationCompat.PRIORITY_HIGH) //Used for heads-up notification
            //.addAction(R.drawable.ic_launcher_foreground, "Snooze", PendingIntent.getActivity(context, 0, SnoozeIntent, PendingIntent.FLAG_UPDATE_CURRENT))
            //.setSound(alarmSound)
            .build()

        manager.notify(notificationID, notification)
        }

    }
}