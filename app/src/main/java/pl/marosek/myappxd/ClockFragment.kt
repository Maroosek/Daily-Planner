package pl.marosek.myappxd

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context.ALARM_SERVICE
import android.content.Intent
import android.media.RingtoneManager
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.ListView
import android.widget.TextView
import android.widget.Toast
import android.widget.ToggleButton
import java.util.Calendar

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER

/**
 * A simple [Fragment] subclass.
 * Use the [ClockFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class ClockFragment : Fragment(R.layout.fragment_clock) {

    var pendingIntent : PendingIntent? = null
    var alarmManager : AlarmManager? = null
    var addAlarmButton : Button? = null
    var deleteAlarmButton : Button? = null
    var editAlarmButton : Button? = null
    var textLabel : TextView? = null
    var alarmList : ListView? = null
    var toggleButton : ToggleButton? = null
    //var stopBtn : Button? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_clock, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        addAlarmButton = view.findViewById(R.id.newAlarmButton)
        deleteAlarmButton = view.findViewById(R.id.deleteAlarmButton)
        editAlarmButton = view.findViewById(R.id.editAlarmButton)
        textLabel = view.findViewById(R.id.debug)
        alarmList = view.findViewById(R.id.alarmList)
        alarmManager = context?.getSystemService(ALARM_SERVICE) as AlarmManager
        toggleButton = view.findViewById(R.id.toggleBtn)
        //stopBtn = view.findViewById(R.id.stopBtn)
        var selectedAlarm: Alarm? = null

        refreshList()

        //addstatic() //debugging

        alarmList?.onItemClickListener = object : AdapterView.OnItemClickListener {
            override fun onItemClick(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                //Toast.makeText(context, "Clicked", Toast.LENGTH_SHORT).show()//Debugging
                Toast.makeText(context, "Selected: " +alarmsList[position].alarmTitle, Toast.LENGTH_SHORT).show()
                selectedAlarm = null
                selectedAlarm = alarmsList[position]
                if (selectedAlarm != null) {
                    if (selectedAlarm?.isActive == true)
                    {
                        toggleButton?.setChecked(false)
                    }
                    else
                    {
                        toggleButton?.setChecked(true)
                    }
                }
            }
        }

//        stopBtn?.setOnClickListener {
//            if (selectedAlarm != null) {
//                //val alarmManager = getSystemService(ALARM_SERVICE) as AlarmManager
//                //alarmManager?.cancel(PendingIntent.getBroadcast(context, selectedAlarm!!.alarmID, Intent(context, AlarmReceiver::class.java), PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE))
//                //AlarmReceiver().stopSound(requireContext())
//                //val alarmServiceIntent = Intent(context, AlarmService::class.java)
//
//
////                val index = selectedAlarm!!.alarmID
////                val alarmServiceIntent = Intent(context, MainActivity::class.java)
////                alarmServiceIntent.putExtra("source", "clockFragment")
////                alarmServiceIntent.putExtra("state", "stop")
////                requireContext().startService(alarmServiceIntent)
//
////                val snoozeFragment = SnoozeFragment()
////                val transaction = requireActivity().supportFragmentManager.beginTransaction()
////                transaction.replace(R.id.flFragment, snoozeFragment)
////                transaction.commit()
//
//                //val pendeing = PendingIntent.getBroadcast(requireContext(), index, alarmServiceIntent, PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT)
//                //Snooze()
//
//            }else
//            {
//                Toast.makeText(context, "No event selected", Toast.LENGTH_SHORT).show()
//            }
//        }

        toggleButton?.setOnClickListener {
            if (selectedAlarm != null) {
                if (toggleButton?.isChecked == true){
                    //toggleButton?.setChecked(false)//debugging
                    Toast.makeText(context, "Suspended: " +selectedAlarm?.alarmTitle, Toast.LENGTH_SHORT).show()//debugging
                    cancelRepeating(selectedAlarm!!)
                    selectedAlarm?.isActive = false

                } else if (toggleButton?.isChecked == false){
                    //toggleButton?.setChecked(true)//debugging
                    Toast.makeText(context,"Set: " +selectedAlarm?.alarmTitle,Toast.LENGTH_SHORT).show()//debugging
                    selectedAlarm?.isActive = true
                    setAlarm(selectedAlarm!!)
                }
            }
            else
            {
                Toast.makeText(context, "No alarm selected", Toast.LENGTH_SHORT).show()
            }

            refreshList()

        }

        addAlarmButton?.setOnClickListener {

            val clockFragmentAlarm = ClockFragmentAlarm()
            val transaction = requireActivity().supportFragmentManager.beginTransaction()
            transaction.replace(R.id.flFragment, clockFragmentAlarm)
            //transaction.addToBackStack(null)
            transaction.commit()
            refreshList()

        }

        deleteAlarmButton?.setOnClickListener {
            if (selectedAlarm != null) {
                alarmsList.remove(selectedAlarm)
                cancelRepeating(selectedAlarm!!)
                Toast.makeText(context, "Deleted: "+selectedAlarm?.alarmTitle, Toast.LENGTH_SHORT).show()
            }
            else
            {
                Toast.makeText(context, "No alarm selected", Toast.LENGTH_SHORT).show()
            }

            refreshList()
        }

        editAlarmButton?.setOnClickListener {
            if (selectedAlarm != null) {
                val clockFragmentAlarm = ClockFragmentAlarm()
                val bundle = Bundle()
                val indexOfAlarm = alarmsList.indexOf(selectedAlarm)
                //PutString is used due to PutInt default value being 0 which is the first element of the list
                //Which causes problems when value should be null instead of 0
                bundle.putString("alarmIndex", indexOfAlarm.toString())
                clockFragmentAlarm.arguments = bundle

                val transaction = requireActivity().supportFragmentManager.beginTransaction()
                transaction.replace(R.id.flFragment, clockFragmentAlarm)
                //transaction.addToBackStack(null)
                transaction.commit()
            }
            else
            {
                Toast.makeText(context, "No alarm selected", Toast.LENGTH_SHORT).show()
            }
        }
    }

    fun refreshList() {
        var adapter = ArrayAdapter(requireContext(), android.R.layout.simple_list_item_1,
            alarmsList.map { it.alarmTitle+ " " + it.alarmTime + " " + if (it.monday == true){"Monday"}else {""} +
                    " " + if (it.tuesday == true){"Tuesday"}else {""}+ " " + if (it.wednesday == true){"Wednesday"}else {""} +
                    " " + if (it.thursday == true){"Thursday"}else {""}+ " " + if (it.friday == true){"Friday"}else {""} +
                    " " + if (it.saturday == true){"Saturday"}else {""}+ " " + if (it.sunday == true){"Sunday"}else {""}} )
        alarmList?.adapter = adapter
        }
    fun setAlarm(selectedAlarm: Alarm){
        val index = selectedAlarm.alarmID
        setSingleAlarm(selectedAlarm)
        if (selectedAlarm.monday == true) {
            setRepeating(selectedAlarm,index - 7)
        }
        if (selectedAlarm.tuesday == true) {
            setRepeating(selectedAlarm,index - 6)
        }
        if (selectedAlarm.wednesday == true) {
            setRepeating(selectedAlarm,index - 5)
        }
        if (selectedAlarm.thursday == true) {
            setRepeating(selectedAlarm,index - 4)
        }
        if (selectedAlarm.friday == true) {
            setRepeating(selectedAlarm,index - 3)
        }
        if (selectedAlarm.saturday == true) {
            setRepeating(selectedAlarm,index - 2)
        }
        if (selectedAlarm.sunday == true) {
            setRepeating(selectedAlarm,index - 1)
        }

    }
    fun setRepeating(selectedAlarm: Alarm, day: Int){
        var calendar = Calendar.getInstance()
        calendar[Calendar.HOUR_OF_DAY] = selectedAlarm.alarmTime.split(":")[0].toInt()
        calendar[Calendar.MINUTE] = selectedAlarm.alarmTime.split(":")[1].toInt()
        calendar[Calendar.DAY_OF_WEEK] = selectedAlarm.alarmID - day
        val intent = Intent(context,AlarmReceiver::class.java)
        intent.putExtra("alarmName", selectedAlarm.alarmTitle)
        intent.putExtra("source", "clockFragment")
        intent.putExtra("alarmID", day)
        intent.putExtra("state", "play")
        pendingIntent = PendingIntent.getBroadcast(context, day, intent, PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE)
        alarmManager!!.setRepeating(AlarmManager.RTC_WAKEUP, calendar.timeInMillis, AlarmManager.INTERVAL_DAY * 7, pendingIntent)
    }
    fun setSingleAlarm(selectedAlarm: Alarm){
        var calendar = Calendar.getInstance()
        calendar[Calendar.HOUR_OF_DAY] = selectedAlarm.alarmTime.split(":")[0].toInt()
        calendar[Calendar.MINUTE] = selectedAlarm.alarmTime.split(":")[1].toInt()
        if (calendar.timeInMillis <= System.currentTimeMillis()) { //If alarm is set for tomorrow
            calendar.add(Calendar.DATE, 1)
        }
        val intent = Intent(context,AlarmReceiver::class.java)
        intent.putExtra("alarmName", selectedAlarm.alarmTitle)
        intent.putExtra("source", "clockFragment")
        intent.putExtra("alarmID", selectedAlarm.alarmID)
        intent.putExtra("state", "play")
        pendingIntent = PendingIntent.getBroadcast(context, selectedAlarm.alarmID, intent, PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE)
        alarmManager!!.setExact(AlarmManager.RTC_WAKEUP, calendar.timeInMillis, pendingIntent)

    }
    fun cancelRepeating(selectedAlarm: Alarm) {
        val index = selectedAlarm.alarmID
        cancelAlarm(index)
        if (selectedAlarm.monday == true) {
            cancelAlarm(index - 7)
        }
        if (selectedAlarm.tuesday == true) {
            cancelAlarm(index - 6)
        }
        if (selectedAlarm.wednesday == true) {
            cancelAlarm(index - 5)
        }
        if (selectedAlarm.thursday == true) {
            cancelAlarm(index - 4)
        }
        if (selectedAlarm.friday == true) {
            cancelAlarm(index - 3)
        }
        if (selectedAlarm.saturday == true) {
            cancelAlarm(index - 2)
        }
        if (selectedAlarm.sunday == true) {
            cancelAlarm(index - 1)
        }
    }
    fun cancelAlarm(index: Int){
        val intent = Intent(requireContext(), AlarmReceiver::class.java)
        alarmManager?.cancel(PendingIntent.getBroadcast(requireContext(), index, intent, PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT))
    }
//    fun Snooze(){
//        //Sets default alarm sound
//        var alarmSound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_ALARM)
////        //If there is no default alarm sound, sets default notification sound
//        if (alarmSound == null) {
//            alarmSound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)
//        }
//        val ringtone = RingtoneManager.getRingtone(context, alarmSound)
//
//        ringtone.play()
//    }

}


