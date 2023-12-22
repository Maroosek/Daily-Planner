package pl.marosek.myappxd

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context.ALARM_SERVICE
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.CheckBox
import android.widget.EditText
import android.widget.TimePicker
import android.widget.Toast
import java.util.Calendar
import kotlin.random.Random

class ClockFragmentAlarm : Fragment(R.layout.fragment_clock_alarm) {

    private var alarmTimePicker :TimePicker? = null
    private var title :EditText? = null
    private var mon :CheckBox? = null
    private var tue :CheckBox? = null
    private var wed :CheckBox? = null
    private var thu :CheckBox? = null
    private var fri :CheckBox? = null
    private var sat :CheckBox? = null
    private var sun :CheckBox? = null
    private var addAlarm :Button? = null
    private var cancelAlarm :Button? = null
    private var alarmManager : AlarmManager? = null


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_clock_alarm, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        alarmTimePicker = view.findViewById(R.id.alarmTimePicker)
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
        alarmManager = context?.getSystemService(ALARM_SERVICE) as AlarmManager

        val calendar = Calendar.getInstance()
        //AlarmID is random to avoid collision with callender intents
        var alarmID = Random.nextInt(1, 100000)
        alarmID *= 8 //to avoid collision with repeating intents, 7 days of week + 1 single fire
        var alarmTime = String.format("%02d:%02d", calendar[Calendar.HOUR_OF_DAY], calendar[Calendar.MINUTE])
        val alarmIndex = arguments?.getString("alarmIndex") //sets index from bundle, its actual list index
        val active = false

        alarmTimePicker?.setOnTimeChangedListener { view, hourOfDay, minute ->
            alarmTime = String.format("%02d:%02d", hourOfDay, minute)
        }

        if (alarmIndex != null) { //if alarm is being edited
            val alarm = alarmsList[alarmIndex.toInt()]
            title?.setText(alarm.alarmTitle)
            alarmID = alarm.alarmID
            val time = alarm.alarmTime.split(":")
            alarmTimePicker?.hour = time[0].toInt()
            alarmTimePicker?.minute = time[1].toInt()
            alarmTime = alarm.alarmTime
            if(alarm.monday == true) {
                mon?.isChecked = true
            }
            if(alarm.tuesday == true) {
                tue?.isChecked = true
            }
            if(alarm.wednesday == true) {
                wed?.isChecked = true
            }
            if(alarm.thursday == true) {
                thu?.isChecked = true
            }
            if(alarm.friday == true) {
                fri?.isChecked = true
            }
            if(alarm.saturday == true) {
                sat?.isChecked = true
            }
            if(alarm.sunday == true) {
                sun?.isChecked = true
            }
        }

        addAlarm?.setOnClickListener {
            val alarmName = title?.text.toString()
            val mon = mon?.isChecked
            val tue = tue?.isChecked
            val wed = wed?.isChecked
            val thu = thu?.isChecked
            val fri = fri?.isChecked
            val sat = sat?.isChecked
            val sun = sun?.isChecked

            if (alarmIndex != null) {
                val alarm = alarmsList[alarmIndex.toInt()]
                cancelAlarm(alarm)
                alarmsList.removeAt(alarmIndex.toInt())
            }

            addAlarm(alarmTime, alarmName, alarmID, active, mon, tue, wed, thu, fri, sat, sun)

            changeFragment()

            if (alarmIndex != null) {
                Toast.makeText(context, "Edited alarm: $alarmName", Toast.LENGTH_SHORT).show()
            }
            else {
                Toast.makeText(context, "Added alarm: $alarmName", Toast.LENGTH_SHORT).show()
            }
        }

        cancelAlarm?.setOnClickListener {
            changeFragment()
        }
    }
    private fun changeFragment(){
        val clockFragment = ClockFragment()
        val transaction = requireActivity().supportFragmentManager.beginTransaction()
        transaction.replace(R.id.flFragment, clockFragment)
        transaction.commit()
    }
    private fun cancelAlarm(alarm : Alarm) {
        val index = alarm.alarmID //getting ID of alarm
        cancelIntent(index) // calling for nearest day
        //calling for every day of week of repeating alarm
        if(alarm.monday == true) {
            cancelIntent(index - 7)
        }
        if(alarm.tuesday == true) {
            cancelIntent(index - 6)
        }
        if(alarm.wednesday == true) {
            cancelIntent(index - 5)
        }
        if(alarm.thursday == true) {
            cancelIntent(index - 4)
        }
        if(alarm.friday == true) {
            cancelIntent(index - 3)
        }
        if(alarm.saturday == true) {
            cancelIntent(index - 2)
        }
        if(alarm.sunday == true) {
            cancelIntent(index - 1)
        }

    }
    private fun cancelIntent(index: Int){
        val intent = Intent(requireContext(), AlarmReceiver::class.java)
        alarmManager?.cancel(PendingIntent.getBroadcast(requireContext(), index, intent, PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT))
    }

}
