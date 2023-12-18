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
import android.widget.EditText
import android.widget.TextView
import android.widget.TimePicker
import android.widget.Toast
import java.util.Calendar

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER

/**
 * A simple [Fragment] subclass.
 * Use the [CalendarFragmentEvent.newInstance] factory method to
 * create an instance of this fragment.
 */
class CalendarFragmentEvent : Fragment(R.layout.fragment_calendar_event) {

    var textLabel : TextView? = null
    var eventTitle : EditText? = null
    var submitButton : Button? = null
    var cancelButton : Button? = null
    var eventTime : TimePicker? = null


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_calendar_event, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        textLabel = view.findViewById(R.id.daySelection)
        eventTitle = view.findViewById(R.id.eventName)
        eventTime = view.findViewById(R.id.eventTimePicker)
        eventTime!!.setIs24HourView(true)
        submitButton = view.findViewById(R.id.submitEventButton)
        cancelButton = view.findViewById(R.id.cancelEventButton)
        textLabel?.setText("Selected date is ${arguments?.getString("date")}")

        var calendar = Calendar.getInstance()
        var selectedTime = String.format("%02d:%02d", calendar[Calendar.HOUR_OF_DAY], calendar[Calendar.MINUTE])
        var eventDate = arguments?.getString("date")
        var eventIndex = arguments?.getString("eventIndex")



        eventTime?.setOnTimeChangedListener { view, hourOfDay, minute -> // getting time from time picker
            selectedTime = String.format("%02d:%02d", hourOfDay, minute)
        }

        if (eventIndex != null) { //if event is being edited
            var index = eventIndex.toInt()
            eventTitle!!.setText(eventsList[index].eventName)
            eventTime!!.hour = eventsList[index].eventTime.split(":")[0].toInt()
            eventTime!!.minute = eventsList[index].eventTime.split(":")[1].toInt()
            textLabel?.setText("Selected date is ${eventsList[index].eventDate}")
            eventDate = eventsList[index].eventDate
            //textLabel?.setText("Selected date is $eventIndex")
            cancelEvent(index)
        }

        submitButton?.setOnClickListener {
            val calendarFragment = CalendarFragment()
            val transaction = requireActivity().supportFragmentManager.beginTransaction()
            transaction.replace(R.id.flFragment, calendarFragment)
            transaction.commit()
            val eventName = eventTitle?.text.toString() //works
            val eventDate = eventDate.toString()
            if (eventIndex != null) {
                eventsList.removeAt(eventIndex.toInt())
            }
            addEvent(eventName, selectedTime, eventDate)
            var event = Event(eventName, selectedTime, eventDate)
            val indexOfEvent = eventsList.indexOf(event)
            scheduleEvent(indexOfEvent)
            Toast.makeText(context, "Event Added", Toast.LENGTH_SHORT).show()

            //val Event = Event(eventName, selectedTime, eventDate) //debugging
//            Toast.makeText(context, "Event Submited, $eventName, $selectedTime," +
//                    " $eventDate", Toast.LENGTH_SHORT).show()//debugging
        }

        cancelButton?.setOnClickListener {
            cancelEvent()
        }

    }
    fun cancelEvent(){
        val calendarFragment = CalendarFragment()
        val transaction = requireActivity().supportFragmentManager.beginTransaction()
        transaction.replace(R.id.flFragment, calendarFragment)
        transaction.commit()
        Toast.makeText(context, "Event Canceled", Toast.LENGTH_SHORT).show()
    }
    fun scheduleEvent(indexOfEvent: Int) {
        var calendar = Calendar.getInstance()
        calendar[Calendar.HOUR_OF_DAY] = eventsList[indexOfEvent].eventTime.split(":")[0].toInt()
        calendar[Calendar.MINUTE] = eventsList[indexOfEvent].eventTime.split(":")[1].toInt()
        calendar[Calendar.DAY_OF_MONTH] = eventsList[indexOfEvent].eventDate.split("-")[0].toInt()
        calendar[Calendar.MONTH] = eventsList[indexOfEvent].eventDate.split("-")[1].toInt() - 1
        calendar[Calendar.YEAR] = eventsList[indexOfEvent].eventDate.split("-")[2].toInt()

        val alarmManager = requireActivity().getSystemService(ALARM_SERVICE) as AlarmManager
        val intent = Intent(requireContext(), AlarmReceiver::class.java)
        intent.putExtra("source", "calendarFragment")
        intent.putExtra("eventIndex", indexOfEvent.toString())
        val pendingIntent = PendingIntent.getBroadcast(requireContext(), indexOfEvent, intent,
            PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT)
        alarmManager.setExact(AlarmManager.RTC_WAKEUP, calendar.timeInMillis, pendingIntent)
    }
    fun cancelEvent(indexOfEvent: Int) {
        val alarmManager = requireActivity().getSystemService(ALARM_SERVICE) as AlarmManager
        val intent = Intent(requireContext(), AlarmReceiver::class.java)
        alarmManager.cancel(PendingIntent.getBroadcast(requireContext(), indexOfEvent, intent, PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT))
    }

}