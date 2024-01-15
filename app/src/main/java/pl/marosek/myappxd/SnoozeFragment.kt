package pl.marosek.myappxd

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.media.RingtoneManager
import android.os.Bundle
import android.os.Handler
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.NumberPicker
import android.widget.Toast
import java.util.Calendar

class SnoozeFragment : Fragment(R.layout.fragment_snooze){
    private var snoozeButton : Button? = null
    private var cancelSnooze : Button? = null
    private var alarmManager : AlarmManager? = null
    private var handler = Handler()
    private var numberPicker : NumberPicker? = null
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_snooze, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        snoozeButton = view.findViewById(R.id.snoozeButton)
        cancelSnooze = view.findViewById(R.id.cancelSnoozeButton)
        alarmManager = context?.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        numberPicker = view.findViewById(R.id.numberPicker)

        numberPicker?.minValue = 1
        numberPicker?.maxValue = 30
        numberPicker?.value = 10 //Default snooze time
        var time = numberPicker?.value

        numberPicker?.setOnValueChangedListener { picker, oldVal, newVal ->
            time = newVal
        }

        //Sets default alarm sound
        var alarmSound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_ALARM)
        //If there is no default alarm sound, sets as default notification sound
        if (alarmSound == null) {
            alarmSound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)
        }
        val ringtone = RingtoneManager.getRingtone(context, alarmSound)

        ringtone.play()

        handler.postDelayed({
            ringtone.stop()
        }, 4000)

        snoozeButton?.setOnClickListener {
            scheduleSnooze(time!!)
            ringtone.stop()
            setFragment()
            Toast.makeText(context, "Snooze set for $time minutes", Toast.LENGTH_SHORT).show()
        }

        cancelSnooze?.setOnClickListener {
            ringtone.stop()
            cancelSnooze()
            setFragment()
        }
    }
    private fun scheduleSnooze(snoozeTime : Int){
        val snoozeIntent = Intent(context, AlarmReceiver::class.java)
        snoozeIntent.putExtra("alarmName", "Snooze")
        snoozeIntent.putExtra("source", "clockFragment")
        val calendar = Calendar.getInstance()
        calendar.add(Calendar.MINUTE, snoozeTime) //SnoozeTime is in minutes
        //request code 0 due to free space and no need for unique request code
        val snoozePendingIntent = PendingIntent.getBroadcast(context, 0, snoozeIntent, PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT)
        alarmManager?.setExact(AlarmManager.RTC_WAKEUP, calendar.timeInMillis, snoozePendingIntent)
    }
    private fun cancelSnooze(){
        val snoozeIntent = Intent(context, AlarmReceiver::class.java)
        val snoozePendingIntent = PendingIntent.getBroadcast(context, 0, snoozeIntent, PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT)
        alarmManager?.cancel(snoozePendingIntent)
    }
    private fun setFragment(){
        val homeFragment = HomeFragment()
        val transaction = requireActivity().supportFragmentManager.beginTransaction()
        transaction.replace(R.id.flFragment, homeFragment)
        transaction.commit()
    }
}