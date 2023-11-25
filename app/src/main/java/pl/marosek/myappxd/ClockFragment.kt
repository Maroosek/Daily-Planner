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
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.CheckBox
import android.widget.EditText
import android.widget.ListView
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
 * Use the [ClockFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class ClockFragment : Fragment(R.layout.fragment_clock) {
    //var alarmTimePicker : TimePicker? = null
    var pendingIntent : PendingIntent? = null
    var alarmManager : AlarmManager? = null
    var newAlarmButton : Button? = null
    var textLabel : TextView? = null
    var alarmList : ListView? = null


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_clock, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        newAlarmButton = view.findViewById(R.id.newAlarmButton)
        alarmManager = context?.getSystemService(ALARM_SERVICE) as AlarmManager
        //alarmTimePicker!!.setIs24HourView(true)
//        val toggleBtn = view.findViewById<ToggleButton>(R.id.toggleBtn) // Tutaj tez chyba do wywalenia
//        toggleBtn.setOnCheckedChangeListener { toggleBtn, b -> onToggleClicked(view) }

        textLabel = view.findViewById(R.id.debug)
        alarmList = view.findViewById(R.id.alarmList)

        var adapter = ArrayAdapter(requireContext(), android.R.layout.simple_list_item_1, alarmsList)
        alarmList?.adapter = adapter


        newAlarmButton?.setOnClickListener {

            val clockFragmentAlarm = ClockFragmentAlarm()
            val transaction = requireActivity().supportFragmentManager.beginTransaction()
            transaction.replace(R.id.flFragment, clockFragmentAlarm)
            transaction.addToBackStack(null)
            transaction.commit()
            refreshList()

        }


    }

//    fun toggleTest(view: View) {
//        if ((view.findViewById<ToggleButton>(R.id.toggleBtn)).isChecked) {
//            Toast.makeText(context, "Alarm ustawiono", Toast.LENGTH_SHORT).show()
//        }
//        else{
//            Toast.makeText(context,"Alarm wstrzymano",Toast.LENGTH_SHORT).show()
//        }
//    }

//    fun alarmTest(){
//        var time :Long
//
//        Toast.makeText(context,"Alarm ustawiono",Toast.LENGTH_SHORT).show()
//
//        var calendar = Calendar.getInstance()
//
//        calendar[Calendar.HOUR_OF_DAY] = alarmTimePicker!!.currentHour
//        calendar[Calendar.MINUTE] = alarmTimePicker!!.currentMinute
//
//        val intent = Intent(context,AlarmReceiver::class.java)
//
//        pendingIntent = PendingIntent.getBroadcast(context,
//            0,intent, PendingIntent.FLAG_MUTABLE)
//
//        time = calendar.timeInMillis - calendar.timeInMillis % 60000
//
//
//        if (System.currentTimeMillis() > time){
//            time = if (Calendar.AM_PM == 0) {
//                time + 1000 * 60 * 60 * 12
//            }
//            else{
//                time + 1000 * 60 * 60 * 24
//            }
//        }
//        alarmManager!!.setRepeating(AlarmManager.RTC_WAKEUP,time, 1000,pendingIntent)
//    }

//    fun onToggleClicked(view: View) { //Ta kalasa do wywalenia
//
//        if (view.findViewById<ToggleButton>(R.id.toggleBtn).isChecked){
//            Toast.makeText(context,"Alarm ustawiono",Toast.LENGTH_SHORT).show()
//
//            var calendar = Calendar.getInstance()
//
//            calendar[Calendar.HOUR_OF_DAY] = alarmTimePicker!!.hour
//            calendar[Calendar.MINUTE] = alarmTimePicker!!.minute
//
//            val intent = Intent(context,AlarmReceiver::class.java)
//
//            pendingIntent = PendingIntent.getBroadcast(context,
//                0,intent, PendingIntent.FLAG_MUTABLE)
//
//            //alarmManager!!.setRepeating(AlarmManager.RTC_WAKEUP, calendar.timeInMillis, 1000,pendingIntent)
//            //alarmManager!!.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, calendar.timeInMillis, pendingIntent)
//            //alarmManager!!.setExact(AlarmManager.RTC_WAKEUP, calendar.timeInMillis, pendingIntent)
//            alarmManager!!.set(AlarmManager.RTC_WAKEUP, calendar.timeInMillis, pendingIntent)
//
//            textLabel?.setText("Clock is set for ${calendar.timeInMillis % 60000}")
//
//        }
//        else{
//            alarmManager!!.cancel(pendingIntent)
//            Toast.makeText(context,"Alarm wstrzymano",Toast.LENGTH_SHORT).show()
//            textLabel?.setText("Clock diasbled ")
//        }
//    }

fun refreshList() {
    var adapter = ArrayAdapter(requireContext(), android.R.layout.simple_list_item_1, alarmsList)
    alarmList?.adapter = adapter

}

}