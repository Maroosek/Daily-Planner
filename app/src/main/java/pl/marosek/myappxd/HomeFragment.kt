package pl.marosek.myappxd

import android.app.AlertDialog
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.ListView
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import java.time.LocalDate
import java.time.format.DateTimeFormatter

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER

/**
 * A simple [Fragment] subclass.
 * Use the [HomeFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class HomeFragment : Fragment(R.layout.fragment_home) {

    var todayEvents : ListView? = null
    var activeAlarms : ListView? = null
    var helpButton : Button? = null
    var snoozeButton : Button? = null

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

        var currentDate = LocalDate.now().format(DateTimeFormatter.ofPattern("d-M-yyyy"))

        todayEventList(currentDate)
        activeAlarms()

        helpButton?.setOnClickListener {
            helpButton()
        }
        snoozeButton?.setOnClickListener {
            snooze()
        }

    }
    fun todayEventList(selectedDate: String) {
        var filtered = eventsList.filter { it.eventDate == selectedDate } //filtering events by date
        var adapter = ArrayAdapter(requireContext(), android.R.layout.simple_list_item_1,
            filtered.map { it.eventName+ " " + it.eventTime }) //creating adapter for listview to show only event name and time
        todayEvents?.adapter = adapter
    }
    fun activeAlarms() {
        var active = alarmsList.filter { it.isActive == true } //filtering events by isActive
        var adapter = ArrayAdapter(requireContext(), android.R.layout.simple_list_item_1,
            active.map { it.alarmTitle+ " " + it.alarmTime + " " + if (it.monday == true){"Monday"}else {""} +
                 " " + if (it.tuesday == true){"Tuesday"}else {""}+ " " + if (it.wednesday == true){"Wednesday"}else {""} +
                 " " + if (it.thursday == true){"Thursday"}else {""}+ " " + if (it.friday == true){"Friday"}else {""} +
                 " " + if (it.saturday == true){"Saturday"}else {""}+ " " + if (it.sunday == true){"Sunday"}else {""}} )
        activeAlarms?.adapter = adapter //creating adapter for listview to show only active days in alarm
    }
    fun helpButton(){
        val clock = "Obsługa budzika:" +
                "\n - Dodaj nowy budzik przyciskiem '+' w prawym rogu ekranu" +
                "\n - Ustaw godzinę oraz dni w które chcesz aby budzik zadzwonił" +
                "\n - Brak ustawienia dni skutkuje utworzeniem jednorazowego alarmu" +
                "\n - Włącz/wyłącz budzik wybierając go z listy i wciśnij guzik WŁ" +
                "\n - Usuń budzik wybierając go z listy po czym wciśnij guzik -" +
                "\n - Edytuj budzik wybierając go z listy po czym wciśnij guzik edytuj" +
                "\n !Każdorazowa edycja wyłącza budzik!"
        val calendar = "Obsługa kalendarza:" +
                "\n - Wybierz dzień z ekranu kalendarza, po wybraniu wciśnij guzik + aby dodać wydarznie" +
                "\n - Ustaw nazwę oraz godzinę wewnątrz nowego ekranu" +
                "\n - Usuń wydarzenie wybierając je z listy po czym wciśnij guzik -" +
                "\n - Edytuj wydarzenie wybierając je z listy po czym wciśnij guzik edytuj"

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
    fun snooze(){
        val snoozeFragment = SnoozeFragment()
        val transaction = requireActivity().supportFragmentManager.beginTransaction()
        transaction.replace(R.id.flFragment, snoozeFragment)
        transaction.commit()
    }





}