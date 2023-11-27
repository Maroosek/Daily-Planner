package pl.marosek.myappxd

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
class CalendarFragment : Fragment(R.layout.fragment_calendar), DateTransfer {

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
        textLabel?.visibility = View.GONE //debugging
        //textLabel?.setText("Selected date is $currentDate") //debugging

        var today = eventsList.filter { it.eventDate == currentDate }
        var adapter = ArrayAdapter(requireContext(), android.R.layout.simple_list_item_1,
            today.map { it.eventTime+ " " +it.eventName })
        eventListView?.adapter = adapter

        eventListView?.onItemClickListener = object : AdapterView.OnItemClickListener {
            override fun onItemClick(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                //Toast.makeText(context, "Clicked", Toast.LENGTH_SHORT).show()
                Toast.makeText(context, "Selected" +eventsList[position], Toast.LENGTH_SHORT).show()
                selectedEvent = null
                selectedEvent = eventsList[position]
            }
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
                eventsList.remove(selectedEvent)
                Toast.makeText(context, "Event deleted", Toast.LENGTH_SHORT).show()
            }
            else
            {
                Toast.makeText(context, "No event selected", Toast.LENGTH_SHORT).show()
            }

            refreshList(selectedDate.toString())
        }

        calendarView?.setOnDateChangeListener { view, year, month, dayOfMonth ->
            //Toast.makeText(context, "Selected date is $dayOfMonth.$month.$year", Toast.LENGTH_SHORT).show()//debugging
            val month = month + 1 //add 1 to month because it starts from 0
            selectedDate = "$dayOfMonth-$month-$year"
            textLabel?.setText("Selected date is $selectedDate")
            refreshList(selectedDate.toString())
        }

        fun editEvent() {
            if (selectedEvent != null) {
                val calendarFragmentEvent = CalendarFragmentEvent()
                val bundle = Bundle()
                bundle.putString("eventName", selectedEvent?.eventName)
                bundle.putString("eventTime", selectedEvent?.eventTime)
                bundle.putString("eventDate", selectedEvent?.eventDate)
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
    }

    override fun passDate(date: String) {
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