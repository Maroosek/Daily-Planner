package pl.marosek.myappxd

data class Event(
    val eventName: String,
    val eventTime: String,
    val eventDate: String
)
var eventsList = mutableListOf<Event>()

fun addEvent(eventName: String, selectedTime: String, eventDate: String) {
    val event = Event(eventName, selectedTime, eventDate)
    eventsList.add(event)
}