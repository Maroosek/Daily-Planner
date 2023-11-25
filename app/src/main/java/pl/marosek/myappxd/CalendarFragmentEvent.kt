package pl.marosek.myappxd

import android.app.AlarmManager
import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.CalendarView
import android.widget.EditText
import android.widget.TextView
import android.widget.TimePicker
import android.widget.Toast
import android.widget.ToggleButton
import androidx.core.os.bundleOf
import java.time.DayOfWeek
import java.time.LocalDate
import java.time.format.DateTimeFormatter
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


        var selectedTime = ""
        var eventDate = arguments?.getString("date")

        var calendar = Calendar.getInstance()

        selectedTime = String.format("%02d:%02d", calendar[Calendar.HOUR_OF_DAY], calendar[Calendar.MINUTE])


        eventTime?.setOnTimeChangedListener { view, hourOfDay, minute ->
            selectedTime = String.format("%02d:%02d", hourOfDay, minute)
        }



        submitButton?.setOnClickListener {
            val calendarFragment = CalendarFragment()
            val transaction = requireActivity().supportFragmentManager.beginTransaction()
            transaction.replace(R.id.flFragment, calendarFragment)
            transaction.commit()
            val eventName = eventTitle?.text.toString() //works
            val eventDate = eventDate.toString()

            addEvent(eventName, selectedTime, eventDate)
            //val Event = Event(eventName, selectedTime, eventDate) //debugging


            Toast.makeText(context, "Event Added", Toast.LENGTH_SHORT).show()
//            Toast.makeText(context, "Event Submited, $eventName, $selectedTime," +
//                    " $eventDate", Toast.LENGTH_SHORT).show()//debugging


        }

        cancelButton?.setOnClickListener {
            val calendarFragment = CalendarFragment()
            val transaction = requireActivity().supportFragmentManager.beginTransaction()
            transaction.replace(R.id.flFragment, calendarFragment)
            transaction.commit()
            Toast.makeText(context, "Event Canceled", Toast.LENGTH_SHORT).show()
        }

    }

}