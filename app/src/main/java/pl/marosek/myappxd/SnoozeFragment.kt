package pl.marosek.myappxd

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.media.RingtoneManager
import android.os.Bundle
import android.os.Handler
import android.text.BoringLayout
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.CalendarView
import android.widget.ListView
import android.widget.NumberPicker
import android.widget.TextView
import android.widget.Toast
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.Calendar

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER

/**
 * A simple [Fragment] subclass.
 * Use the [SnoozeFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class SnoozeFragment : Fragment(R.layout.fragment_snooze){
    var snoozeButton : Button? = null
    var cancelSnooze : Button? = null
    var alarmManager : AlarmManager? = null
    var handler = Handler()
    var numberPicker : NumberPicker? = null
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

        numberPicker?.value = 10 //Default snooze time
        numberPicker?.minValue = 1
        numberPicker?.maxValue = 30
        var time = numberPicker?.value

        numberPicker?.setOnValueChangedListener { picker, oldVal, newVal ->
            time = numberPicker?.value
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
            //cancelSnooze()
            setFragment()
        }
    }
    fun scheduleSnooze(snoozeTime : Int){
        val snoozeIntent = Intent(context, AlarmReceiver::class.java)
        snoozeIntent.putExtra("alarmName", "Snooze")
        snoozeIntent.putExtra("source", "clockFragment")
        var calendar = Calendar.getInstance()
        calendar.add(Calendar.MINUTE, snoozeTime) //SnoozeTime is in minutes
        //request code 0 due to free space and no need for unique request code
        val snoozePendingIntent = PendingIntent.getBroadcast(context, 0, snoozeIntent, PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT)
        alarmManager?.setExact(AlarmManager.RTC_WAKEUP, calendar.timeInMillis, snoozePendingIntent)
    }
//    fun cancelSnooze(){
//        val snoozeIntent = Intent(context, AlarmReceiver::class.java)
//        val snoozePendingIntent = PendingIntent.getBroadcast(context, 0, snoozeIntent, PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT)
//        alarmManager?.cancel(snoozePendingIntent)
//    }
    fun setFragment(){
        val homeFragment = HomeFragment()
        val transaction = requireActivity().supportFragmentManager.beginTransaction()
        transaction.replace(R.id.flFragment, homeFragment)
        transaction.commit()
    }
}