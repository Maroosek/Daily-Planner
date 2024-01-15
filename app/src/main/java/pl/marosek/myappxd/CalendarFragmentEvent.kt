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
import kotlin.random.Random

class CalendarFragmentEvent : Fragment(R.layout.fragment_calendar_event) {

    private var textLabel : TextView? = null
    private var eventTitle : EditText? = null
    private var submitButton : Button? = null
    private var cancelButton : Button? = null
    private var eventTime : TimePicker? = null
    private var alarmManager : AlarmManager? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_calendar_event, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        eventTitle = view.findViewById(R.id.eventName)
        eventTime = view.findViewById(R.id.eventTimePicker)
        eventTime!!.setIs24HourView(true)
        submitButton = view.findViewById(R.id.submitEventButton)
        cancelButton = view.findViewById(R.id.cancelEventButton)
        textLabel = view.findViewById(R.id.daySelection)
        textLabel?.setText("Selected date is ${arguments?.getString("date")}")
        alarmManager = context?.getSystemService(ALARM_SERVICE) as AlarmManager

        val calendar = Calendar.getInstance()
        var selectedTime = String.format("%02d:%02d", calendar[Calendar.HOUR_OF_DAY], calendar[Calendar.MINUTE])
        var eventDate = arguments?.getString("date") //sets date from bundle
        val eventIndex = arguments?.getString("eventIndex") //sets index from bundle, its actual list index
        var eventID = Random.nextInt(800001, 2000000)

        eventTime?.setOnTimeChangedListener { view, hourOfDay, minute -> // getting time from time picker
            selectedTime = String.format("%02d:%02d", hourOfDay, minute)
        }

        if (eventIndex != null) { //if event is being edited
            val index = eventIndex.toInt()
            eventID = eventsList[index].eventID
            eventTitle!!.setText(eventsList[index].eventName)
            eventTime!!.hour = eventsList[index].eventTime.split(":")[0].toInt()
            eventTime!!.minute = eventsList[index].eventTime.split(":")[1].toInt()
            textLabel?.setText("Selected date is ${eventsList[index].eventDate}")
            eventDate = eventsList[index].eventDate

        }

        submitButton?.setOnClickListener {
            val eventName = eventTitle?.text.toString() //works
            val eventDate = eventDate.toString()
            if (eventIndex != null) {
                cancelEvent(eventIndex.toInt())
                eventsList.removeAt(eventIndex.toInt())
            }
            addEvent(eventID, eventName, selectedTime, eventDate)
            val event = Event(eventID ,eventName, selectedTime, eventDate)
            val indexOfEvent = eventsList.indexOf(event)

            scheduleEvent(indexOfEvent)

            changeFragment()
            Toast.makeText(context, "Added event: $eventName", Toast.LENGTH_SHORT).show()
        }

        cancelButton?.setOnClickListener {
            changeFragment()
        }
    }
    fun changeFragment(){
        val calendarFragment = CalendarFragment()
        val transaction = requireActivity().supportFragmentManager.beginTransaction()
        transaction.replace(R.id.flFragment, calendarFragment)
        transaction.commit()
    }
    fun scheduleEvent(indexOfEvent: Int) {
        val calendar = Calendar.getInstance()
        calendar[Calendar.HOUR_OF_DAY] = eventsList[indexOfEvent].eventTime.split(":")[0].toInt()
        calendar[Calendar.MINUTE] = eventsList[indexOfEvent].eventTime.split(":")[1].toInt()
        calendar[Calendar.DAY_OF_MONTH] = eventsList[indexOfEvent].eventDate.split("-")[0].toInt()
        calendar[Calendar.MONTH] = eventsList[indexOfEvent].eventDate.split("-")[1].toInt() - 1
        calendar[Calendar.YEAR] = eventsList[indexOfEvent].eventDate.split("-")[2].toInt()

        if (calendar.timeInMillis >= System.currentTimeMillis()) {
            val index = eventsList[indexOfEvent].eventID
            val intent = Intent(requireContext(), AlarmReceiver::class.java)
            intent.putExtra("source", "calendarFragment") //used to determine which fragment is calling the receiver
            intent.putExtra("eventID", index) //used to tell which ID is calling the receiver
            intent.putExtra("eventName", eventsList[indexOfEvent].eventName) //Passing event name to receiver
            val pendingIntent = PendingIntent.getBroadcast(requireContext(), index, intent,
                PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT)
            alarmManager?.setExact(AlarmManager.RTC_WAKEUP, calendar.timeInMillis, pendingIntent)
        }
    }
    fun cancelEvent(indexOfEvent: Int) {
        val index = eventsList[indexOfEvent].eventID //gets index from list to call for intent
        val intent = Intent(requireContext(), AlarmReceiver::class.java)
        alarmManager?.cancel(PendingIntent.getBroadcast(requireContext(), index, intent, PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT))
    }
}