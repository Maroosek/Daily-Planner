package pl.marosek.myappxd

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.media.RingtoneManager
import android.os.Vibrator
import android.widget.Toast
import android.widget.ToggleButton

class AlarmReceiver : BroadcastReceiver() {
    val MONDAY = "MONDAY"
    val TUESDAY = "TUESDAY"
    val WEDNESDAY = "WEDNESDAY"
    val THURSDAY = "THURSDAY"
    val FRIDAY = "FRIDAY"
    val SATURDAY = "SATURDAY"
    val SUNDAY = "SUNDAY"
    val REPEATING = "REPEATING"
    val TITLE = "TITLE"


    override fun onReceive(context: Context?, intent: Intent?) {

        //Starts the vibration and toast
        val vibration = context!!.getSystemService(Context.VIBRATOR_SERVICE) as Vibrator
        vibration.vibrate(4000)
        Toast.makeText(context, "Aaaaaaaaaaaaa",Toast.LENGTH_SHORT).show()
        //Sets default alarm sound
        var alarmSound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_ALARM)
        //If there is no default alarm sound, sets default notification sound
        if (alarmSound == null){
            alarmSound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)
        }

        val ringtone = RingtoneManager.getRingtone(context, alarmSound)
        ringtone.play()

        //val toggleButton : ToggleButton = findViewById<ToggleButton>(R.id.toggleBtn).isChecked)

        //if (toggleButton.isChecked){
        //   ringtone.stop()
        //}

        if (Intent.ACTION_BOOT_COMPLETED.equals(intent?.action)){ // Podobno po resecie ma się pojawić, nie ma co to trzymać
            Toast.makeText(context, "Alarm Reboot",Toast.LENGTH_SHORT).show()
            //startRescheduleAlarmsService(context)
        }
        else {
            Toast.makeText(context, "Alarm Received",Toast.LENGTH_SHORT).show() // Ten to sam nie wiem o co chodzi
            //ringtone.stop()
            if (intent!!.getBooleanExtra(REPEATING, false)){
                //startAlarmService(context, intent)
            }
            //else if (alarmisToday(intent)){
                //startAlarmService(context, intent)
            //}
        }

    }



//    fun alarmisToday(intent : Intent) : Boolean{
//        val calendar = java.util.Calendar.getInstance()
//        val today = calendar.get(java.util.Calendar.DAY_OF_WEEK)
//        return when(today){
//            java.util.Calendar.MONDAY -> intent.getBooleanExtra(MONDAY, false)
//            java.util.Calendar.TUESDAY -> intent.getBooleanExtra(TUESDAY, false)
//            java.util.Calendar.WEDNESDAY -> intent.getBooleanExtra(WEDNESDAY, false)
//            java.util.Calendar.THURSDAY -> intent.getBooleanExtra(THURSDAY, false)
//            java.util.Calendar.FRIDAY -> intent.getBooleanExtra(FRIDAY, false)
//            java.util.Calendar.SATURDAY -> intent.getBooleanExtra(SATURDAY, false)
//            java.util.Calendar.SUNDAY -> intent.getBooleanExtra(SUNDAY, false)
//            else -> false
//        }
//    }
//
//    fun startAlarmService(context: Context, intent: Intent){
//        val serviceIntent = Intent(context, AlarmService::class.java)
//        serviceIntent.putExtra(TITLE, intent.getStringExtra(TITLE))
//        context.startService(serviceIntent)
//    }
//
//    fun startRescheduleAlarmsService(context: Context){
//        val intent = Intent(context, RescheduleAlarmsService::class.java)
//        context.startService(intent)
//    }


}