package pl.marosek.myappxd

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context.ALARM_SERVICE
import android.content.Intent
import android.os.Bundle
import android.text.style.ClickableSpan
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.CheckBox
import android.widget.EditText
import android.widget.TextView
import android.widget.TimePicker
import android.widget.Toast
import android.widget.ToggleButton
import androidx.core.content.getSystemService
import java.util.Calendar
import kotlin.random.Random

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER

/**
 * A simple [Fragment] subclass.
 * Use the [ClockFragmentAlarm.newInstance] factory method to
 * create an instance of this fragment.
 */
class ClockFragmentAlarm : Fragment(R.layout.fragment_clock_event) {
    var alarmTimePicker :TimePicker? = null
    var title :EditText? = null
    var mon :CheckBox? = null
    var tue :CheckBox? = null
    var wed :CheckBox? = null
    var thu :CheckBox? = null
    var fri :CheckBox? = null
    var sat :CheckBox? = null
    var sun :CheckBox? = null
    var addAlarm :Button? = null
    var cancelAlarm :Button? = null
    var textLabel : TextView? = null


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_clock_event, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        alarmTimePicker = view.findViewById(R.id.timePicker)
        alarmTimePicker!!.setIs24HourView(true)
        mon = view.findViewById(R.id.alarmCheckMon)
        tue = view.findViewById(R.id.alarmCheckTue)
        wed = view.findViewById(R.id.alarmCheckWed)
        thu = view.findViewById(R.id.alarmCheckThu)
        fri = view.findViewById(R.id.alarmCheckFri)
        sat = view.findViewById(R.id.alarmCheckSat)
        sun = view.findViewById(R.id.alarmCheckSun)
        title = view.findViewById((R.id.alarmTitle))
        addAlarm = view.findViewById(R.id.addAlarmButton)
        cancelAlarm = view.findViewById(R.id.cancelAlarmButton)

        textLabel = view.findViewById(R.id.debug)
        var alarmIndex = arguments?.getString("alarmIndex")

        if (alarmIndex != null) { //if alarm is being edited
            var alarm = alarmsList[alarmIndex.toInt()]
            title?.setText(alarm.alarmTitle)
            var time = alarm.alarmTime.split(":")
            alarmTimePicker?.hour = time[0].toInt()
            alarmTimePicker?.minute = time[1].toInt()
            if(alarm.monday == true) mon?.isChecked = true
            if(alarm.tuesday == true) tue?.isChecked = true
            if(alarm.wednesday == true) wed?.isChecked = true
            if(alarm.thursday == true) thu?.isChecked = true
            if(alarm.friday == true) fri?.isChecked = true
            if(alarm.saturday == true) sat?.isChecked = true
            if(alarm.sunday == true) sun?.isChecked = true
        }

        addAlarm?.setOnClickListener {
            val clockFragment = ClockFragment()
            val transaction = requireActivity().supportFragmentManager.beginTransaction()
            transaction.replace(R.id.flFragment, clockFragment)
            transaction.commit()
            val alarmName = title?.text.toString() //works

            var alarmTime = ""
            var calendar = Calendar.getInstance()
            alarmTime = String.format("%02d:%02d", calendar[Calendar.HOUR_OF_DAY], calendar[Calendar.MINUTE])

            alarmTimePicker?.setOnTimeChangedListener { view, hourOfDay, minute ->
                alarmTime = String.format("%02d:%02d", hourOfDay, minute)
            }
            var active = true
            var mon = mon?.isChecked
            var tue = tue?.isChecked
            var wed = wed?.isChecked
            var thu = thu?.isChecked
            var fri = fri?.isChecked
            var sat = sat?.isChecked
            var sun = sun?.isChecked

            //textLabel?.setText("$mon $tue $wed $thu $fri $sat $sun") //debugging
            //val addedAlarm = Alarm(alarmTime, alarmName, active, mon, tue, wed, thu, fri, sat, sun) //debugging
            addAlarm(alarmTime, alarmName, active, mon, tue, wed, thu, fri, sat, sun)

            Toast.makeText(context, "Alarm Added", Toast.LENGTH_SHORT).show()
        }

        cancelAlarm?.setOnClickListener {
            val clockFragment = ClockFragment()
            val transaction = requireActivity().supportFragmentManager.beginTransaction()
            transaction.replace(R.id.flFragment, clockFragment)
            transaction.commit()
            Toast.makeText(context, "Alarm Canceled!", Toast.LENGTH_SHORT).show()
        }
    }

}
