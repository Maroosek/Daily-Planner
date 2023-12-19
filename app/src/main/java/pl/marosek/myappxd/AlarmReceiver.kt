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
    val notifName = "TestName"
    var notifDescription = "TestDescription"

    override fun onReceive(context: Context?, intent: Intent?) {
    //Checkin from which fragment the alarm was set
    var source = intent?.getStringExtra("source")
    var title: String? = null

    if(source == "clockFragment") {
        title = "Alarm"
        notifDescription = intent?.getStringExtra("alarmName")!!
        //notificationID = intent?.getIntExtra("alarmID", 1)!!
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
        val vibration = context!!.getSystemService(Context.VIBRATOR_SERVICE) as Vibrator
        vibration.vibrate(4000)
        //code keeps crashing here
//        val homeFragment = HomeFragment()
//        //val transaction = requireActivity().supportFragmentManager.beginTransaction()
//        val transaction = (context as MainActivity).supportFragmentManager.beginTransaction()
//        transaction.replace(R.id.flFragment, homeFragment)
//        transaction.addToBackStack(null)
//        transaction.commitAllowingStateLoss()
    }
    if (source == "calendarFragment"){
        title = "Calendar"
        notifDescription = intent?.getStringExtra("eventName")!!
        notificationID = intent?.getIntExtra("eventID", 1)!!
        Toast.makeText(context, "AAAAAAAAAAAAAAAAAAAAAAAAA", Toast.LENGTH_SHORT).show()
        var alarmSound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_ALARM)
        val ringtone = RingtoneManager.getRingtone(context, alarmSound)
        ringtone.play()

        val manager = context!!.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        val notification = NotificationCompat.Builder(context, notificationChannelID)
            .setSmallIcon(R.drawable.ic_launcher_foreground)
            .setContentTitle(title)
            .setContentText(notifDescription)
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            //.setContentIntent(PendingIntent.getBroadcast(context, notificationID, intent, PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT))
            //.setSound(alarmSound)
            //.setAutoCancel(true)
            .build()

        //TODO Need to re-check about norify id
        manager.notify(1, notification)
        }

    }
}