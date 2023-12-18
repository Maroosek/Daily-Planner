package pl.marosek.myappxd

import android.app.Service
import android.content.Context
import android.content.Intent
import android.media.RingtoneManager
import android.os.IBinder
import android.os.Vibrator
//TODO Implement firing and stopping the alarm sound and vibration
class AlarmService : Service() {
    override fun onBind(intent: Intent?): IBinder? {
        return null
    }

    override fun onCreate() {
        super.onCreate()
        val alarmSound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_ALARM)
        var ringtone = RingtoneManager.getRingtone(applicationContext, alarmSound)
        var vibration = applicationContext.getSystemService(Context.VIBRATOR_SERVICE) as Vibrator
    }
    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {



        //vibration?.vibrate(4000)
        return START_STICKY
    }
    override fun onDestroy() {
        super.onDestroy()

    }

}