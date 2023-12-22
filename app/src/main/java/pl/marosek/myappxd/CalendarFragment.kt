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
import android.widget.Toast
import java.time.LocalDate
import java.time.format.DateTimeFormatter

class CalendarFragment : Fragment(R.layout.fragment_calendar){

    private var calendarView : CalendarView? = null
    private var addEventButton : Button? = null
    private var deleteEventButton : Button? = null
    private var editEventButton : Button? = null
    private var eventListView : ListView? = null
    private var alarmManager : AlarmManager? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_calendar, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        calendarView = view.findViewById(R.id.calendarView)
        addEventButton = view.findViewById(R.id.addEventButton)
        deleteEventButton = view.findViewById(R.id.deleteEventButton)
        editEventButton = view.findViewById(R.id.editEventButton)
        eventListView = view.findViewById(R.id.eventList)
        alarmManager = context?.getSystemService(Context.ALARM_SERVICE) as AlarmManager

        var selectedEvent: Event? = null
        //setting selected date to current date because calendarView default date is current date
        var selectedDate = LocalDate.now().format(DateTimeFormatter.ofPattern("d-M-yyyy"))

        refreshList(selectedDate) //refreshes list on startup
        //On click listener to pick event from list using lambda expression
        eventListView!!.onItemClickListener = AdapterView.OnItemClickListener { parent, view, position, id ->
            //Toast.makeText(context, "Clicked", Toast.LENGTH_SHORT).show()//Debugging
            selectedEvent = null
            val selected = eventListView?.getItemAtPosition(position).toString()
            for (event in eventsList) {
                if (event.eventTime + " " + event.eventName == selected && event.eventDate == selectedDate) {
                    selectedEvent = event
                }
            }
            Toast.makeText(context, "Selected: " +selectedEvent?.eventName, Toast.LENGTH_SHORT).show()//Debugging
        }
        eventListView!!.onItemClickListener =
            AdapterView.OnItemClickListener { parent, view, position, id ->
                //Toast.makeText(context, "Clicked", Toast.LENGTH_SHORT).show()//Debugging
                selectedEvent = null
                val selected = eventListView?.getItemAtPosition(position)
                for (event in eventsList) {
                    if (event.eventTime + " " + event.eventName == selected) {
                        selectedEvent = event
                    }
                }
                Toast.makeText(context, "Selected: " +selectedEvent?.eventName, Toast.LENGTH_SHORT).show()//Debugging
            }

        addEventButton?.setOnClickListener {
                passDate(selectedDate.toString())
        }

        deleteEventButton?.setOnClickListener {
            if (selectedEvent != null) {
                val indexOfEvent = selectedEvent!!.eventID
                cancelEvent(indexOfEvent)
                eventsList.remove(selectedEvent)
                Toast.makeText(context, "Deleted: "+selectedEvent?.eventName, Toast.LENGTH_SHORT).show()
                refreshList(selectedDate.toString())
            }
            else
            {
                Toast.makeText(context, "No event selected", Toast.LENGTH_SHORT).show()
            }
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
                transaction.commit()
            }
            else
            {
                Toast.makeText(context, "No event selected", Toast.LENGTH_SHORT).show()
            }
        }

        calendarView?.setOnDateChangeListener { view, year, month, dayOfMonth ->
            selectedEvent = null
            val month = month + 1 //add 1 to month because it starts from 0
            selectedDate = "$dayOfMonth-$month-$year"
            refreshList(selectedDate.toString())
        }
    }

    private fun passDate(date: String) {
        val bundle = Bundle()
        val calendarFragmentEvent = CalendarFragmentEvent()

        bundle.putString("date", date) //passing date to calendarFragmentEvent
        calendarFragmentEvent.arguments = bundle

        val transaction = requireActivity().supportFragmentManager.beginTransaction()
        transaction.replace(R.id.flFragment, calendarFragmentEvent)
        transaction.addToBackStack(null)
        transaction.commit()
    }

    private fun refreshList(selectedDate: String) {
        //filtering events by date and sorting them by time ascending
        val filtered = eventsList.filter { it.eventDate == selectedDate }.sortedBy { it.eventTime }
        val adapter = ArrayAdapter(requireContext(), android.R.layout.simple_list_item_1,
            filtered.map { it.eventTime+ " " + it.eventName })
        eventListView?.adapter = adapter

    }
    private fun cancelEvent(index: Int){
        val intent = Intent(requireContext(), AlarmReceiver::class.java)
        alarmManager?.cancel(PendingIntent.getBroadcast(requireContext(), index, intent, PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT))
    }
}