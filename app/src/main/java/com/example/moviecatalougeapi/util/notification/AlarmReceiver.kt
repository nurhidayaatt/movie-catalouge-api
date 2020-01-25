package com.example.moviecatalougeapi.util.notification

import android.app.AlarmManager
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.media.RingtoneManager
import android.os.Build
import android.widget.Toast
import androidx.core.app.NotificationCompat
import androidx.core.content.ContextCompat
import com.example.moviecatalougeapi.BuildConfig
import com.example.moviecatalougeapi.R
import com.example.moviecatalougeapi.data.api.ApiService
import com.example.moviecatalougeapi.data.model.movie.MovieList
import com.example.moviecatalougeapi.data.model.tv.TvList
import com.example.moviecatalougeapi.ui.MainActivity
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

class AlarmReceiver: BroadcastReceiver() {

    companion object    {
        const val TYPE_REMINDER = "Reminder"
        const val TYPE_RELEASE = "Release Movies & Tv Shows Today"
        const val EXTRA_TYPE = "type"

        private const val ID_REMINDER = 100
        private const val ID_RELEASE = 101

        private const val DATE_FORMAT = "yyyy-MM-dd"
        private const val TIME_FORMAT = "HH:mm"
    }

    override fun onReceive(context: Context, intent: Intent) {
        val item = ArrayList<String>()
        val type = intent.getStringExtra(EXTRA_TYPE)

        if (type.equals(TYPE_RELEASE, ignoreCase = true)) {
            GlobalScope.launch {
                try {
                    val date = SimpleDateFormat(DATE_FORMAT, Locale.getDefault()).format(Date())
                    val movies: MovieList = ApiService.retrofitService.releaseMovie(BuildConfig.TMDB_API_KEY, date, date)
                    val tvs: TvList = ApiService.retrofitService.releaseTv(BuildConfig.TMDB_API_KEY, date, date)

                    movies.resultMovies.forEach {
                        item.add(it.title!!)
                    }

                    tvs.results.forEach {
                        item.add(it.name!!)
                    }

                    if (item.size!=0) {
                        showAlarmNotification(context, TYPE_RELEASE, item.toString(), ID_RELEASE)
                    }

                }catch (t: Throwable) {
                    showAlarmNotification(context, TYPE_RELEASE, t.localizedMessage, ID_RELEASE)
                }
            }
        } else {
            showAlarmNotification(context, TYPE_REMINDER, context.getString(R.string.reminder_message), ID_REMINDER)
        }
    }

    fun setRepeatingAlarm(context: Context, type: String, time: String) {

        if (isDateInvalid(time, TIME_FORMAT)) return

        val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        val intent = Intent(context, AlarmReceiver::class.java)
        intent.putExtra(EXTRA_TYPE, type)

        val notifId = if (type.equals(TYPE_REMINDER, ignoreCase = true)) ID_REMINDER else ID_RELEASE

        val timeArray = time.split(":".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()

        val calendar = Calendar.getInstance()
        calendar.set(Calendar.HOUR_OF_DAY, Integer.parseInt(timeArray[0]))
        calendar.set(Calendar.MINUTE, Integer.parseInt(timeArray[1]))
        calendar.set(Calendar.SECOND, 0)

        val pendingIntent = PendingIntent.getBroadcast(context, notifId, intent, 0)
        alarmManager.setInexactRepeating(AlarmManager.RTC_WAKEUP, calendar.timeInMillis, AlarmManager.INTERVAL_DAY, pendingIntent)

        Toast.makeText(context, context.getString(R.string.notification_setup), Toast.LENGTH_SHORT).show()
    }

    fun cancelAlarm(context: Context, type: String) {
        val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        val intent = Intent(context, AlarmReceiver::class.java)
        val requestCode = if (type.equals(TYPE_REMINDER, ignoreCase = true)) ID_REMINDER else ID_RELEASE
        val pendingIntent = PendingIntent.getBroadcast(context, requestCode, intent, 0)
        pendingIntent.cancel()

        alarmManager.cancel(pendingIntent)

        Toast.makeText(context, context.getString(R.string.notification_off), Toast.LENGTH_SHORT).show()
    }

    private fun showAlarmNotification(context: Context, title: String, message: String, notifId: Int) {

        val channelId = "Channel_1"
        val channelName = "Notification channel"

        val intent = Intent(context, MainActivity::class.java)
        val pendingIntent = PendingIntent.getActivity(context, 0, intent, 0)

        val notificationManagerCompat = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        val alarmSound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)
        val builder = NotificationCompat.Builder(context, channelId)
            .setSmallIcon(R.drawable.ic_favorite_border_black_24dp)
            .setContentIntent(pendingIntent)
            .setContentTitle(title)
            .setContentText(message)
            .setColor(ContextCompat.getColor(context, android.R.color.transparent))
            .setVibrate(longArrayOf(1000, 1000, 1000, 1000, 1000))
            .setSound(alarmSound)
            .setAutoCancel(true)
            .setStyle(NotificationCompat.BigTextStyle().bigText(message))

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {

            val channel = NotificationChannel(channelId,
                channelName,
                NotificationManager.IMPORTANCE_DEFAULT)

            channel.enableVibration(true)
            channel.vibrationPattern = longArrayOf(1000, 1000, 1000, 1000, 1000)
            builder.setChannelId(channelId)
            notificationManagerCompat.createNotificationChannel(channel)
        }

        val notification = builder.build()
        notificationManagerCompat.notify(notifId, notification)
    }

    private fun isDateInvalid(date: String, format: String): Boolean {
        return try {
            val df = SimpleDateFormat(format, Locale.getDefault())
            df.isLenient = false
            df.parse(date)
            false
        } catch (e: ParseException) {
            true
        }
    }
}