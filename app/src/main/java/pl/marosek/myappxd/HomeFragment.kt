package pl.marosek.myappxd

import android.app.AlertDialog
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.ListView
import java.time.LocalDate
import java.time.format.DateTimeFormatter

class HomeFragment : Fragment(R.layout.fragment_home) {

    private var todayEvents : ListView? = null
    private var activeAlarms : ListView? = null
    private var helpButton : Button? = null
    private var snoozeButton : Button? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_home, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        todayEvents = view.findViewById(R.id.todayEvents)
        activeAlarms = view.findViewById(R.id.activeAlarms)
        helpButton = view.findViewById(R.id.helpButton)
        snoozeButton = view.findViewById(R.id.snoozeButton)

        //Setting current date for todayEvents
        val currentDate = LocalDate.now().format(DateTimeFormatter.ofPattern("d-M-yyyy"))

        todayEventList(currentDate)
        activeAlarms()

        helpButton?.setOnClickListener {
            helpButton()
        }
        snoozeButton?.setOnClickListener {
            setFragment()
        }

    }

    private fun setFragment() {
        val snoozeFragment = SnoozeFragment()
        val transaction = requireActivity().supportFragmentManager.beginTransaction()
        transaction.replace(R.id.flFragment, snoozeFragment)
        transaction.addToBackStack(null)
        transaction.commit()
    }

    private fun todayEventList(selectedDate: String) {
        val filtered = eventsList.filter { it.eventDate == selectedDate }.sortedBy { it.eventTime } //filtering events by date
        val adapter = ArrayAdapter(requireContext(), android.R.layout.simple_list_item_1,
            filtered.map { it.eventTime+ " " + it.eventName }) //creating adapter for listview to show only event name and time
        todayEvents?.adapter = adapter
    }
    private fun activeAlarms() {
        val active = alarmsList.filter { it.isActive }.sortedBy { it.alarmTime } //filtering events by isActive
        val adapter = ArrayAdapter(requireContext(), android.R.layout.simple_list_item_1,
            active.map { it.alarmTitle+ ", " + it.alarmTime + " " + if (it.monday == true){"Monday"}else {""} +
                 " " + if (it.tuesday == true){"Tuesday"}else {""}+ " " + if (it.wednesday == true){"Wednesday"}else {""} +
                 " " + if (it.thursday == true){"Thursday"}else {""}+ " " + if (it.friday == true){"Friday"}else {""} +
                 " " + if (it.saturday == true){"Saturday"}else {""}+ " " + if (it.sunday == true){"Sunday"}else {""}} )
        activeAlarms?.adapter = adapter //creating adapter for listview to show only active days in alarm
    }
    private fun helpButton(){
        val clock = "Clock controls:" +
                "\n - To add a new alarm tap '+' on right side of screen" +
                "\n - Set time and days in which clock should set alarm" +
                "\n - If no day was selected alarm will fire only once" +
                "\n - Turn ON/OFF alarm by selecting one from list and click toggle button" +
                "\n - Delete alarm by selecting one from list and tap '-' on left side of screen" +
                "\n - Edit alarm by selecting one from list and tap 'Edit' option" +
                "\n !Every edit will turn off that alarm!"
        val calendar = "Calendar controls:" +
                "\n - To add a new event select day on calendar and tap '+' on right side of screen" +
                "\n - Set name and time for a event" +
                "\n - Delete event by selecting one from list and tap '-' on left side of screen" +
                "\n - Edit event by selecting one from list and tap 'Edit' option"
        val snooze = "Snooze controls:" +
                "\n - Snooze will appear as notification on alarm trigger time" +
                "\n - Click alarm notification to enter snooze screen" +
                "\n - You can set snooze time you desire or exit snooze screen"

        AlertDialog.Builder(requireContext())
            .setTitle("Snooze")
            .setMessage(snooze)
            .setPositiveButton("OK") { _, _ -> }
            .show()
        AlertDialog.Builder(requireContext())
            .setTitle("Calendar")
            .setMessage(calendar)
            .setPositiveButton("OK") { _, _ -> }
            .show()
        AlertDialog.Builder(requireContext())
            .setTitle("Clock")
            .setMessage(clock)
            .setPositiveButton("OK") { _, _ -> }
            .show()
    }
}