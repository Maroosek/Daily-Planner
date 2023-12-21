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
import android.widget.TextView
import android.widget.TimePicker
import android.widget.Toast
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
    var alarmManager : AlarmManager? = null


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_clock_event, container, false)
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
        //AlarmID is random to avoid collision with callender intents
        var alarmID = Random.nextInt(1, 100000)
        var alarmTime = ""
        val active = false
        alarmID *= 8 //to avoid collision with repeating intents, 7 days of week + 1 single fire

        textLabel = view.findViewById(R.id.debug)
        var alarmIndex = arguments?.getString("alarmIndex") //sets index from bundle, its actual list index

        var calendar = Calendar.getInstance()
        alarmTime = String.format("%02d:%02d", calendar[Calendar.HOUR_OF_DAY], calendar[Calendar.MINUTE])
        alarmTimePicker?.setOnTimeChangedListener { view, hourOfDay, minute ->
            alarmTime = String.format("%02d:%02d", hourOfDay, minute)
        }

        if (alarmIndex != null) { //if alarm is being edited
            var alarm = alarmsList[alarmIndex.toInt()]
            title?.setText(alarm.alarmTitle)
            alarmID = alarm.alarmID
            //active = alarm.isActive//debugging
            var time = alarm.alarmTime.split(":")
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
            val clockFragment = ClockFragment()
            val transaction = requireActivity().supportFragmentManager.beginTransaction()
            transaction.replace(R.id.flFragment, clockFragment)
            transaction.commit()
            val alarmName = title?.text.toString()

            var mon = mon?.isChecked
            var tue = tue?.isChecked
            var wed = wed?.isChecked
            var thu = thu?.isChecked
            var fri = fri?.isChecked
            var sat = sat?.isChecked
            var sun = sun?.isChecked

            if (alarmIndex != null) {

                var alarm = alarmsList[alarmIndex.toInt()]
                cancelAlarm(alarm)
                alarmsList.removeAt(alarmIndex.toInt())
            }

            //textLabel?.setText("$mon $tue $wed $thu $fri $sat $sun") //debugging
            //val addedAlarm = Alarm(alarmTime, alarmName, active, mon, tue, wed, thu, fri, sat, sun) //debugging
            addAlarm(alarmTime, alarmName, alarmID, active, mon, tue, wed, thu, fri, sat, sun)

            Toast.makeText(context, "Added alarm: "+alarmName, Toast.LENGTH_SHORT).show()
        }

        cancelAlarm?.setOnClickListener {
            cancelButton()
        }
    }
    fun cancelButton(){
        val clockFragment = ClockFragment()
        val transaction = requireActivity().supportFragmentManager.beginTransaction()
        transaction.replace(R.id.flFragment, clockFragment)
        transaction.commit()
        Toast.makeText(context, "Canceled adding alarm", Toast.LENGTH_SHORT).show()
    }
    fun cancelAlarm(alarm : Alarm) {
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
    fun cancelIntent(index: Int){
        val intent = Intent(requireContext(), AlarmReceiver::class.java)
        alarmManager?.cancel(PendingIntent.getBroadcast(requireContext(), index, intent, PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT))
    }

}
