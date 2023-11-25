package pl.marosek.myappxd

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import com.google.gson.Gson


class MainActivity : AppCompatActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        Log.d("LIFECYCLE","onCreate") //debugging

        val btnHomeFragment = findViewById<Button>(R.id.btnFragmentHome)
        val btnClockFragment = findViewById<Button>(R.id.btnFragmentClock)
        val btnCalendarFragment = findViewById<Button>(R.id.btnFragmentCalendar)
        val homeFragment = HomeFragment()//creating fragment
        val clockFragment = ClockFragment()
        val calendarFragment = CalendarFragment()

        getArraysFromSharedPref() //getting arrays from shared preferences when opening app

        //Starting fragment
        supportFragmentManager.beginTransaction().apply {
            replace(R.id.flFragment, homeFragment)
            commit()//commit changes, without it nothing will happen
        }
        //Fragment buttons
        btnHomeFragment.setOnClickListener {
            supportFragmentManager.beginTransaction().apply {
                replace(R.id.flFragment, homeFragment)
                addToBackStack(null)//allows to go back to previous fragment
                commit()
            }
        }

        btnClockFragment.setOnClickListener {
            supportFragmentManager.beginTransaction().apply {
                replace(R.id.flFragment, clockFragment)
                addToBackStack(null)
                commit()
            }
        }

        btnCalendarFragment.setOnClickListener {
            supportFragmentManager.beginTransaction().apply {
                replace(R.id.flFragment, calendarFragment)
                addToBackStack(null)
                commit()
            }
        }
    }
    //Saving Arrays to Shared Preferences using Gson
    fun saveArraysToSharedPref() {
        val sharedPref = getSharedPreferences("sharedPref", MODE_PRIVATE)
        val editor = sharedPref.edit()
        //val gson = Gson()
        val calendar: String = Gson().toJson(eventsList)
        val clock: String = Gson().toJson(alarmsList)

        editor.putString("eventList", calendar)
        editor.putString("alarmList", clock)
        editor.apply()
    }
    //Getting Arrays from Shared Preferences using Gson
    fun getArraysFromSharedPref() {
        val sharedPref = getSharedPreferences("sharedPref", MODE_PRIVATE)
        val calendarList = sharedPref.getString("eventList", null)
        val clockList = sharedPref.getString("alarmList", null)
        if (calendarList != null) {
            //eventsList = calendarList.split(",") as mutableListOf<String>
            //calendarList.split(",").forEach { eventsList }
            val calendar = Gson().fromJson(calendarList, Array<Event>::class.java)
            eventsList = calendar.toMutableList()
        }
        if (clockList != null) {
            //alarmsList = clockList.split(",") as ArrayList<String>
            val clock = Gson().fromJson(clockList, Array<Alarm>::class.java)
            alarmsList = clock.toMutableList()
        }
    }

    override fun onStart() {
        super.onStart()
        Log.d("LIFECYCLE","onStart") //debugging
    }

    override fun onResume() {
        super.onResume()
        Log.d("LIFECYCLE","onResume") //debugging
    }

    override fun onPause() {
        super.onPause()
        Log.d("LIFECYCLE","onPause") //debugging
    }

    override fun onStop() {
        super.onStop()
        Log.d("LIFECYCLE","onStop") //debugging
    }

    override fun onRestart() {
        super.onRestart()
        Log.d("LIFECYCLE","onRestart") //debugging
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.d("LIFECYCLE","onDestroy") //debugging
        saveArraysToSharedPref() //saving arrays to shared preferences when closing app
    }

}

