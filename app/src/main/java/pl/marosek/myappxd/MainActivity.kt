package pl.marosek.myappxd

import android.app.NotificationChannel
import android.app.NotificationManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.google.gson.Gson


class MainActivity : AppCompatActivity() {
//TODO update res/values/strings.xml

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
        val snoozeFragment = SnoozeFragment()

        val intentTest = intent.getStringExtra("source")

        //Creating notification channel
        notificationChannel()

        //getting arrays from shared preferences when opening app
        getArraysFromSharedPref()

        //Starting fragment
        setFragment(homeFragment)

        //Checking if received intent from AlarmReceiver
        if(intentTest == "snoozeFragment") {
            supportFragmentManager.beginTransaction().apply {
                replace(R.id.flFragment, snoozeFragment)
                commit()
            }
            Toast.makeText(this, "Aaaaaa!", Toast.LENGTH_SHORT).show()
        }

        //Fragment buttons
        btnHomeFragment.setOnClickListener {
            setFragment(homeFragment)
        }

        btnClockFragment.setOnClickListener {
            setFragment(clockFragment)
        }

        btnCalendarFragment.setOnClickListener {
            setFragment(calendarFragment)
        }
    }
    //Function for creating notification channel, required for Android 8.0 and above
    private fun notificationChannel() {
        val notificationChannel = NotificationChannel(
            "pl.marosek.myappxd",
            "Alarms",
            NotificationManager.IMPORTANCE_HIGH
        )
        notificationChannel.description = "Notification Channel for Daily Planner"
        val notificationManager = getSystemService(NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.createNotificationChannel(notificationChannel)
    }
    //Function for setting fragment
    private fun setFragment(fragment: Fragment){
        supportFragmentManager.beginTransaction().apply {
            replace(R.id.flFragment, fragment)
            addToBackStack(null)
            commit()
        }
    }
    //Saving Arrays to Shared Preferences using Gson
    private fun saveArraysToSharedPref() {
        val sharedPref = getSharedPreferences("sharedPref", MODE_PRIVATE)
        val editor = sharedPref.edit()
        val calendar: String = Gson().toJson(eventsList)
        val clock: String = Gson().toJson(alarmsList)

        editor.putString("eventList", calendar)
        editor.putString("alarmList", clock)
        editor.apply()
    }
    //Getting Arrays from Shared Preferences using Gson
    private fun getArraysFromSharedPref() {
        val sharedPref = getSharedPreferences("sharedPref", MODE_PRIVATE)
        val calendarList = sharedPref.getString("eventList", null)
        val clockList = sharedPref.getString("alarmList", null)
        if (calendarList != null) {
            val calendar = Gson().fromJson(calendarList, Array<Event>::class.java)
            eventsList = calendar.toMutableList()
        }
        if (clockList != null) {
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
        saveArraysToSharedPref()
    }

    override fun onStop() {
        super.onStop()
        Log.d("LIFECYCLE","onStop") //debugging
        saveArraysToSharedPref()
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

