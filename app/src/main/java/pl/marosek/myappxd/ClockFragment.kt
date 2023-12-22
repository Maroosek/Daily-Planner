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
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.ListView
import android.widget.TextView
import android.widget.Toast
import android.widget.ToggleButton
import java.util.Calendar

class ClockFragment : Fragment(R.layout.fragment_clock) {

    private var pendingIntent : PendingIntent? = null
    private var alarmManager : AlarmManager? = null
    private var addAlarmButton : Button? = null
    private var deleteAlarmButton : Button? = null
    private var editAlarmButton : Button? = null
    private var textLabel : TextView? = null
    private var alarmList : ListView? = null
    private var toggleButton : ToggleButton? = null


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
        toggleButton = view.findViewById(R.id.toggleBtn)
        alarmManager = context?.getSystemService(ALARM_SERVICE) as AlarmManager

        var selectedAlarm: Alarm? = null

        refreshList()

        //lambda function for listview item click
        alarmList?.onItemClickListener = AdapterView.OnItemClickListener { parent, view, position, id ->
            Toast.makeText(context, "Selected: " +alarmsList[position].alarmTitle, Toast.LENGTH_SHORT).show()
            selectedAlarm = null
            selectedAlarm = alarmsList[position]
            if (selectedAlarm != null) {
                //checking if alarm is active and setting toggle button accordingly
                toggleButton?.isChecked = selectedAlarm?.isActive != true
            }
        }

        toggleButton?.setOnClickListener {
            if (selectedAlarm != null) {
                if (toggleButton?.isChecked == true){
                    Toast.makeText(context, "Suspended: " +selectedAlarm?.alarmTitle, Toast.LENGTH_SHORT).show()//debugging
                    cancelRepeating(selectedAlarm!!)
                    selectedAlarm?.isActive = false

                } else if (toggleButton?.isChecked == false){
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
                transaction.commit()
            }
            else
            {
                Toast.makeText(context, "No alarm selected", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun refreshList() {
        val alarmsList = alarmsList.sortedBy { it.alarmTime } //sorting alarms by time ascending
        val adapter = ArrayAdapter(requireContext(), android.R.layout.simple_list_item_1,
            alarmsList.map {if (it.isActive) {"Active"}else {"Inactive"} + ", " +
                    it.alarmTitle+ ", " + it.alarmTime + " " + if (it.monday == true){"Monday"}else {""} +
                    " " + if (it.tuesday == true){"Tuesday"}else {""}+ " " + if (it.wednesday == true){"Wednesday"}else {""} +
                    " " + if (it.thursday == true){"Thursday"}else {""}+ " " + if (it.friday == true){"Friday"}else {""} +
                    " " + if (it.saturday == true){"Saturday"}else {""}+ " " + if (it.sunday == true){"Sunday"}else {""}} )

        alarmList?.adapter = adapter
        }
    private fun setAlarm(selectedAlarm: Alarm){
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
    private fun setRepeating(selectedAlarm: Alarm, day: Int){
        val calendar = Calendar.getInstance()
        calendar[Calendar.HOUR_OF_DAY] = selectedAlarm.alarmTime.split(":")[0].toInt()
        calendar[Calendar.MINUTE] = selectedAlarm.alarmTime.split(":")[1].toInt()
        calendar[Calendar.SECOND] = 0
        calendar[Calendar.DAY_OF_WEEK] = selectedAlarm.alarmID - day

        val intent = Intent(context,AlarmReceiver::class.java)
        intent.putExtra("alarmName", selectedAlarm.alarmTitle)
        intent.putExtra("source", "clockFragment")
        intent.putExtra("alarmID", day)
        intent.putExtra("state", "play")

        pendingIntent = PendingIntent.getBroadcast(context, day, intent, PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE)
        alarmManager!!.setRepeating(AlarmManager.RTC_WAKEUP, calendar.timeInMillis, AlarmManager.INTERVAL_DAY * 7, pendingIntent)
    }
    private fun setSingleAlarm(selectedAlarm: Alarm){
        val calendar = Calendar.getInstance()
        calendar[Calendar.HOUR_OF_DAY] = selectedAlarm.alarmTime.split(":")[0].toInt()
        calendar[Calendar.MINUTE] = selectedAlarm.alarmTime.split(":")[1].toInt()
        calendar[Calendar.SECOND] = 0
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
    private fun cancelRepeating(selectedAlarm: Alarm) {
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
    private fun cancelAlarm(index: Int){
        val intent = Intent(requireContext(), AlarmReceiver::class.java)
        alarmManager?.cancel(PendingIntent.getBroadcast(requireContext(), index, intent, PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT))
    }

}


