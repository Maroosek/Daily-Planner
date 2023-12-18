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

        calendarView = view.findViewById(R.id.calendarView)
        textLabel = view.findViewById(R.id.daySelection)
        addEventButton = view.findViewById(R.id.addEventButton)
        deleteEventButton = view.findViewById(R.id.deleteEventButton)
        editEventButton = view.findViewById(R.id.editEventButton)
        eventListView = view.findViewById(R.id.eventList)
        //textLabel?.visibility = View.GONE //debugging
        textLabel?.setText("Selected date is $currentDate") //debugging

        var today = eventsList.filter { it.eventDate == currentDate }
        var adapter = ArrayAdapter(requireContext(), android.R.layout.simple_list_item_1,
            today.map { it.eventTime+ " " +it.eventName })
        eventListView?.adapter = adapter

//        eventListView?.onItemClickListener = object : AdapterView.OnItemClickListener {
//            override fun onItemClick(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
//                //Toast.makeText(context, "Clicked", Toast.LENGTH_SHORT).show()
//                val selected = eventListView?.getItemAtPosition(position).toString()
//                for (event in eventsList) {
//                    if (event.eventName + " " + event.eventTime == selected) {
//                        selectedEvent = event
//                    }
//                }
//                Toast.makeText(context, "Selected" +selectedEvent, Toast.LENGTH_SHORT).show()
//            }
//        }
        eventListView?.onItemClickListener = AdapterView.OnItemClickListener { parent, view, position, id ->
            //Toast.makeText(context, "Clicked", Toast.LENGTH_SHORT).show()
            val selected = eventListView?.getItemAtPosition(position).toString()
            for (event in eventsList) {
                if (event.eventName + " " + event.eventTime == selected) {
                    selectedEvent = event
                }
            }
            Toast.makeText(context, "Selected" +selectedEvent, Toast.LENGTH_SHORT).show()
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
        //TODO make buttons appear and disappear when event is selected or not
        deleteEventButton?.setOnClickListener {
            if (selectedEvent != null) {
                eventsList.remove(selectedEvent)
                var indexOfEvent = eventsList.indexOf(selectedEvent)
                val alarmManager = requireActivity().getSystemService(Context.ALARM_SERVICE) as AlarmManager
                val intent = Intent(requireContext(), AlarmReceiver::class.java)
                alarmManager.cancel(PendingIntent.getBroadcast(requireContext(), indexOfEvent, intent, PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT))
                Toast.makeText(context, "Event deleted", Toast.LENGTH_SHORT).show()
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
                val indexOfEvent = eventsList.indexOf(selectedEvent)
                //PutString is used due to PutInt default value being 0 which is the first element of the list
                bundle.putString("eventIndex", indexOfEvent.toString())
                calendarFragmentEvent.arguments = bundle

                val transaction = requireActivity().supportFragmentManager.beginTransaction()
                transaction.replace(R.id.flFragment, calendarFragmentEvent)
                transaction.addToBackStack(null)
                transaction.commit()
            }
            else
            {
                Toast.makeText(context, "No event selected", Toast.LENGTH_SHORT).show()
            }
        }

        calendarView?.setOnDateChangeListener { view, year, month, dayOfMonth ->
            //Toast.makeText(context, "Selected date is $dayOfMonth.$month.$year", Toast.LENGTH_SHORT).show()//debugging
            val month = month + 1 //add 1 to month because it starts from 0
            selectedDate = "$dayOfMonth-$month-$year"
            textLabel?.setText("Selected date is $selectedDate")
            refreshList(selectedDate.toString())
        }
    }

    fun passDate(date: String) {
        val bundle = Bundle()
        val calendarFragmentEvent = CalendarFragmentEvent()

        bundle.putString("date", date)
        calendarFragmentEvent.arguments = bundle

        val transaction = requireActivity().supportFragmentManager.beginTransaction()
        transaction.replace(R.id.flFragment, calendarFragmentEvent)
        transaction.addToBackStack(null)
        transaction.commit()
    }

    fun refreshList(selectedDate: String) {
        var filtered = eventsList.filter { it.eventDate == selectedDate }
        var adapter = ArrayAdapter(requireContext(), android.R.layout.simple_list_item_1,
            filtered.map { it.eventName+ " " + it.eventTime })
        eventListView?.adapter = adapter

    }


}