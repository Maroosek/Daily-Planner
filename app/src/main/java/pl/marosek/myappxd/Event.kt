package pl.marosek.myappxd

data class Event(
    val eventID: Int,
    val eventName: String,
    val eventTime: String,
    val eventDate: String
)
var eventsList = mutableListOf<Event>()

fun addEvent(eventID : Int, eventName: String, selectedTime: String, eventDate: String) {
    val event = Event(eventID, eventName, selectedTime, eventDate)
    eventsList.add(event)
}