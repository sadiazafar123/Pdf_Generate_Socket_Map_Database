package com.example.totopartnetapppracticeapplication.services

import android.Manifest
import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.app.Service
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Looper
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationCompat
import com.example.totopartnetapppracticeapplication.R
import com.example.totopartnetapppracticeapplication.ui.DashboardActivity
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationAvailability
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationResult
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.Priority

class LocationService : Service() {
    companion object{
        const val CHANNEL_ID= "12345"
    }
    lateinit var fusedLocationProviderClient: FusedLocationProviderClient
    lateinit var locationRequest: LocationRequest
    lateinit var locationCallbackListener: LocationCallback
    lateinit var notificationManager: NotificationManager
    private var lat: Double? = 0.0
    private var lng: Double? = 0.0

    override fun onCreate() {
        super.onCreate()
        Log.v("oncreate","on Create")
        Log.v("services", "oncreate")
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this)

    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        super.onStartCommand(intent, flags, startId)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            this.startForeground(1, prepareForgroundNotification())
        }
        createLocationRequest()
        return START_STICKY
    }

    private fun createLocationRequest() {
        locationRequest = LocationRequest.Builder(Priority.PRIORITY_HIGH_ACCURACY, 15000)
            .setIntervalMillis(15000).build()
        locationCallbackListener = object : LocationCallback(){
            override fun onLocationAvailability(p0: LocationAvailability) {
                super.onLocationAvailability(p0)
            }
            @RequiresApi(Build.VERSION_CODES.O)
            override fun onLocationResult(locationResult: LocationResult) {
                super.onLocationResult(locationResult)
                val lastLocation= locationResult.lastLocation
                  lat = lastLocation?.latitude
                  lng = lastLocation?.longitude 

                notificationManager.notify(1, prepareForgroundNotification())

                sendBroadcast(Intent().apply {
                    Log.v("location","broadcast")
                    action = "LocationToast"
                    putExtra("lat",lat)
                    putExtra("lng",lng)
                })
                sendBroadcast(Intent().apply {
                    action = "Add_Marker"
                    putExtra("lat",lat)
                    putExtra("lng",lng)
                })
            }
        }
        if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            return
        }
        fusedLocationProviderClient.requestLocationUpdates(locationRequest, locationCallbackListener, Looper.getMainLooper())

    }
    private fun prepareForgroundNotification(): Notification {
        Log.v("oncreate","on Create")

        notificationManager = this.getSystemService(NOTIFICATION_SERVICE) as NotificationManager
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val notificationChannel =
                NotificationChannel(CHANNEL_ID, "location", NotificationManager.IMPORTANCE_HIGH)
            notificationManager.createNotificationChannel(notificationChannel)
        }

        val pendingIntent: PendingIntent =
            Intent(this, DashboardActivity::class.java).let { notificationIntent ->
                PendingIntent.getActivity(this, 0, notificationIntent,
                    PendingIntent.FLAG_IMMUTABLE)
            }
        var contentText = "Getting Your Location"
        if (lat == 0.0){
            contentText = "($lat,$lng)"
        }
        else{
            contentText = "Your Lacation is ($lat, $lng)"
        }
        val mBuilder: NotificationCompat.Builder = NotificationCompat.Builder(this, CHANNEL_ID)
            .setContentTitle("location update")
            .setContentText(contentText)
            .setSmallIcon(R.mipmap.ic_launcher)
            .setContentIntent(pendingIntent)
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setOngoing(true)

        //mBuilder.setContentIntent(pendingIntent)
        val notification: Notification = mBuilder.build()
//        notificationManager.notify(1, notification)
        return notification
    }

    override fun onBind(intent: Intent?)= null
    override fun onDestroy() {
        super.onDestroy()
       // fusedLocationProviderClient.removeLocationUpdates(locationCallbackListener)
       // fusedLocationProviderClient.flushLocations()
    }
}

