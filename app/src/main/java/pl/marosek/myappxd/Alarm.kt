package pl.marosek.myappxd

data class Alarm(
    val alarmTime: String,
    val alarmTitle: String,
    val alarmID: Int,
    var isActive: Boolean,
    val monday: Boolean?,
    val tuesday: Boolean?,
    val wednesday: Boolean?,
    val thursday: Boolean?,
    val friday: Boolean?,
    val saturday: Boolean?,
    val sunday: Boolean?
)
var alarmsList = mutableListOf<Alarm>()
//fun getAlarms(): List<Alarm> {
//    return alarmsList
//}
//fun addstatic(){
//    val staticAlarm = Alarm("12:00", "Test", 10, false, false, true, false, true, true, true, true)
//    alarmsList.add(staticAlarm)
//}//debugging

fun addAlarm(alarmTime: String, alarmTitle: String, alarmID: Int, isActive: Boolean, monday: Boolean?, tuesday: Boolean?,
             wednesday: Boolean?, thursday: Boolean?, friday: Boolean?, saturday: Boolean?,
             sunday: Boolean?) {

    val alarm = Alarm(alarmTime, alarmTitle, alarmID, isActive, monday, tuesday, wednesday, thursday, friday,
        saturday, sunday)
    alarmsList.add(alarm)
}

