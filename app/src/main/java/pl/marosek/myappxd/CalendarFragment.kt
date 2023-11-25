package pl.marosek.myappxd

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.CalendarView
import android.widget.ListView
import android.widget.TextView
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
    var eventButton : Button? = null
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
        var currentDate = LocalDate.now().format(DateTimeFormatter.ofPattern("d-M-yyyy"))

        calendarView = view.findViewById(R.id.calendarView)
        textLabel = view.findViewById(R.id.daySelection)
        eventButton = view.findViewById(R.id.addEventButton)
        eventListView = view.findViewById(R.id.eventList)
        textLabel?.visibility = View.GONE //debugging
        //textLabel?.setText("Selected date is $currentDate") //debugging

        var today = eventsList.filter { it.eventDate == currentDate }
        var adapter = ArrayAdapter(requireContext(), android.R.layout.simple_list_item_1,
            today.map { it.eventTime+ " " +it.eventName })
        eventListView?.adapter = adapter

        eventButton?.setOnClickListener {
            //Toast.makeText(context, "Event added", Toast.LENGTH_SHORT).show()//debugging
            if (selectedDate != null) {
                passDate(selectedDate.toString())
            }
            else
            {
                passDate(currentDate)
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