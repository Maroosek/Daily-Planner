package pl.marosek.myappxd

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.CalendarView
import android.widget.ListView
import android.widget.TextView
import android.widget.Toast
import java.time.LocalDate
import java.time.format.DateTimeFormatter

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER

/**
 * A simple [Fragment] subclass.
 * Use the [CalendarFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class CalendarFragment : Fragment(R.layout.fragment_calendar){
    var calendarView : CalendarView? = null
    var textLabel : TextView? = null
    var addEventButton : Button? = null
    var deleteEventButton : Button? = null
    var editEventButton : Button? = null
    var eventListView : ListView? = null
    var alarmManager : AlarmManager? = null


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_calendar, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        var selectedDate: String? = null
        var selectedEvent: Event? = null
        var currentDate = LocalDate.now().format(DateTimeFormatter.ofPattern("d-M-yyyy"))

        textLabel?.visibility = View.GONE //debugging
        //textLabel?.setText("Selected date is $currentDate") //debugging
        textLabel = view.findViewById(R.id.daySelection) //debugging

        calendarView = view.findViewById(R.id.calendarView)
        addEventButton = view.findViewById(R.id.addEventButton)
        deleteEventButton = view.findViewById(R.id.deleteEventButton)
        editEventButton = view.findViewById(R.id.editEventButton)
        eventListView = view.findViewById(R.id.eventList)
        alarmManager = context?.getSystemService(Context.ALARM_SERVICE) as AlarmManager

        refreshList(currentDate) //refreshes list on startup
        //On click listener to pick event from list
        eventListView!!.onItemClickListener = AdapterView.OnItemClickListener { parent, view, position, id ->
            //Toast.makeText(context, "Clicked", Toast.LENGTH_SHORT).show()//Debugging
            selectedEvent = null
            val selected = eventListView?.getItemAtPosition(position).toString()
            for (event in eventsList) {
                if (event.eventName + " " + event.eventTime == selected) {
                    selectedEvent = event
                }
            }
            Toast.makeText(context, "Selected: " +selectedEvent?.eventName, Toast.LENGTH_SHORT).show()//Debugging
        }
        addEventButton?.setOnClickListener {
            //Toast.makeText(context, "Event added", Toast.LENGTH_SHORT).show()//debugging
            if (selectedDate != null) {
                passDate(selectedDate.toString())
            }
            else
            {
                passDate(currentDate)
            }
        }

        deleteEventButton?.setOnClickListener {
            if (selectedEvent != null) {
                var indexOfEvent = selectedEvent!!.eventID
                cancelEvent(indexOfEvent)
                eventsList.remove(selectedEvent)
                Toast.makeText(context, "Deleted: "+selectedEvent?.eventName, Toast.LENGTH_SHORT).show()
            }
            else
            {
                Toast.makeText(context, "No event selected", Toast.LENGTH_SHORT).show()
            }

            refreshList(selectedDate.toString())
        }

        editEventButton?.setOnClickListener {
            if (selectedEvent != null) {
                val calendarFragmentEvent = CalendarFragmentEvent()
                val bundle = Bundle()
                val indexOfEvent = eventsList.indexOf(selectedEvent) //gets index from list
                //PutString is used due to PutInt default value being 0 which is the first element of the list
                //Which causes problems when value should be null instead of 0
                bundle.putString("eventIndex", indexOfEvent.toString())
                calendarFragmentEvent.arguments = bundle

                val transaction = requireActivity().supportFragmentManager.beginTransaction()
                transaction.replace(R.id.flFragment, calendarFragmentEvent)
                //transaction.addToBackStack(null)
                transaction.commit()
            }
            else
            {
                Toast.makeText(context, "No event selected", Toast.LENGTH_SHORT).show()
            }
        }

        calendarView?.setOnDateChangeListener { view, year, month, dayOfMonth ->
            //Toast.makeText(context, "Selected date is $dayOfMonth.$month.$year", Toast.LENGTH_SHORT).show()//debugging
            selectedEvent = null
            val month = month + 1 //add 1 to month because it starts from 0
            selectedDate = "$dayOfMonth-$month-$year"
            //textLabel?.setText("Selected date is $selectedDate")//debugging
            refreshList(selectedDate.toString())
        }
    }

    fun passDate(date: String) {
        val bundle = Bundle()
        val calendarFragmentEvent = CalendarFragmentEvent()

        bundle.putString("date", date) //passing date to calendarFragmentEvent
        calendarFragmentEvent.arguments = bundle

        val transaction = requireActivity().supportFragmentManager.beginTransaction()
        transaction.replace(R.id.flFragment, calendarFragmentEvent)
        transaction.addToBackStack(null)
        transaction.commit()
    }

    fun refreshList(selectedDate: String) {
        var filtered = eventsList.filter { it.eventDate == selectedDate } //filtering events by date
        var adapter = ArrayAdapter(requireContext(), android.R.layout.simple_list_item_1,
            filtered.map { it.eventName+ " " + it.eventTime }) //creating adapter for listview to show only event name and time
        eventListView?.adapter = adapter

    }
    fun cancelEvent(index: Int){
        val intent = Intent(requireContext(), AlarmReceiver::class.java)
        alarmManager?.cancel(PendingIntent.getBroadcast(requireContext(), index, intent, PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT))
    }


}