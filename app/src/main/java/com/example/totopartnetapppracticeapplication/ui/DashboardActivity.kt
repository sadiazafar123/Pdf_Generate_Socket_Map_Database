package com.example.totopartnetapppracticeapplication.ui

import android.Manifest
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.content.pm.PackageManager
import android.location.Location
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.annotation.LayoutRes
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.databinding.DataBindingUtil
import com.example.totopartnetapppracticeapplication.R
import com.example.totopartnetapppracticeapplication.databinding.ActivityDashboardBinding
import com.example.totopartnetapppracticeapplication.model.Dummy
import com.example.totopartnetapppracticeapplication.model.SaveLatLng
import com.example.totopartnetapppracticeapplication.services.LocationService
import com.example.totopartnetapppracticeapplication.viewmodel.MyViewModel
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.MapsInitializer
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter


class DashboardActivity : AppCompatActivity(){

    private val viewModel: MyViewModel by viewModel()

    var permissionCode = 101
    var services: Intent? = null
    private lateinit var mMap: GoogleMap
    private val layoutResID: Int @LayoutRes get() = R.layout.activity_dashboard
    private lateinit var binding:ActivityDashboardBinding
    private lateinit var myBroadcastReceiver: MyBroadcastReceiver
    private var lat: Double? = 0.0
    private var lng: Double? = 0.0


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, layoutResID)
        binding.mapView.onCreate(savedInstanceState)
        with(binding.mapView) {
            // Initialise the MapView
            onCreate(null)
            // Set the map ready callback to receive the GoogleMap object
            getMapAsync{
                MapsInitializer.initialize(applicationContext)
                mMap = it
                //setMapLocation(it)
            }
        }
        //register broadcast receiver
        myBroadcastReceiver= MyBroadcastReceiver()
        val intentFilter2 = IntentFilter(Intent.ACTION_PACKAGE_ADDED)
        var intentFilter3 = IntentFilter("LocationToast")
        intentFilter2.addDataScheme("package")

        registerReceiver(myBroadcastReceiver,intentFilter2)
        registerReceiver(myBroadcastReceiver,intentFilter3)
        services = Intent(this, LocationService::class.java)
        initView()
    }

    private fun setMapLocation(googleMap: GoogleMap) {

        // This marker will show at my app screen !
        val latLng = LatLng(31.518740230887587, 74.31950130760099)
        googleMap.addMarker(
            MarkerOptions().position(latLng)
                .title("This is office Marker")
        )
        googleMap.moveCamera(CameraUpdateFactory.newLatLng(latLng))
    }

    override fun onStop() {
        binding.mapView.onStop()
        super.onStop()
    }

    override fun onResume() {
        binding.mapView.onResume()
        super.onResume()
    }

    override fun onStart() {
        binding.mapView.onStart()
        super.onStart()
    }

    override fun onDestroy() {
        super.onDestroy()
        binding.mapView.onDestroy()
//        if (::myBroadcastReceiver.isInitialized){
//            unregisterReceiver(myBroadcastReceiver)
//        }
    }

    private fun initView() {
        checkpermission()
        binding.btnDelete.setOnClickListener {
            viewModel.deleteAllRecord()
        }
        binding.btnSpecificDel.setOnClickListener {
//            viewModel.deleteSpecificRecord()
            distance()
        }

    }

    private fun checkpermission() {
        if (ActivityCompat.checkSelfPermission(
                this, Manifest.permission.ACCESS_COARSE_LOCATION)
            != PackageManager.PERMISSION_GRANTED &&
            ActivityCompat.checkSelfPermission(
                this, Manifest.permission.ACCESS_FINE_LOCATION)
            != PackageManager.PERMISSION_GRANTED)
        {
            ActivityCompat.requestPermissions(this, arrayOf(
                Manifest.permission.ACCESS_COARSE_LOCATION,
                Manifest.permission.ACCESS_FINE_LOCATION),permissionCode)
        }
        else{
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
                startForegroundService(services)

            } else {
                startService(services)

            }

        }

    }

    inner class MyBroadcastReceiver:BroadcastReceiver(){
        override fun onReceive(context: Context?, intent: Intent?) {

            if (Build.VERSION.SDK_INT > Build.VERSION_CODES.O){
                val currentTime = LocalDateTime.now()
                val formatter = DateTimeFormatter.ofPattern("HH:mm:ss a")
                val formattedTime = currentTime.format(formatter)
                if (intent?.action == "LocationToast"){
                    lat = intent.extras?.getDouble("lat",0.0)
                    lng = intent.extras?.getDouble("lng",0.0)
                    if (::mMap.isInitialized){
                        mMap.clear()
                        mMap.addMarker(MarkerOptions().position(LatLng(lat?:0.0,lng?:0.0)).title("Your current location"))
                        mMap.moveCamera(
                            CameraUpdateFactory.newLatLngZoom(LatLng(lat?:0.0,lng?:0.0), 15f)
                        )
                        viewModel.insertLatLng(lat,lng,formattedTime)
                    }
                }
                if (intent?.action == Intent.ACTION_PACKAGE_ADDED){
                    val data: Uri? = intent.data
                    val installedPackageName = data?.encodedSchemeSpecificPart
//                    intent.extras.toString()
                    Toast.makeText(this@DashboardActivity, intent.extras.toString() , Toast.LENGTH_SHORT).show()
                    Toast.makeText(this@DashboardActivity, "package added $installedPackageName", Toast.LENGTH_SHORT).show()
                }

            }

        }
    }
    fun distance(){
//        val arraylist: ArrayList<Dummy> = ArrayList()
//        arraylist.add(Dummy(31.506320794181022, 74.31868384741813))//(31.506320794181022, 74.31868384741813),dist:187m
//        arraylist.add(Dummy(31.507660851995166, 74.31990693469609))//31.507660851995166, 74.31990693469609
//        arraylist.add(Dummy(31.507166908005754, 74.32075451272203))//31.507166908005754, 74.32075451272203
//        arraylist.add(Dummy(31.507802631732186, 74.32139287845042))//31.507802631732186, 74.32139287845042
//        arraylist.add(Dummy(31.50912437447754,  74.32273934747974))//31.50912437447754, 74.32273934747974
//        arraylist.add(Dummy(31.508740202060086, 74.32374785804228))//31.508740202060086, 74.32374785804228
//        arraylist.add(Dummy(31.507240085118696, 74.32248185542123))//31.507240085118696, 74.32248185542123
//        arraylist.add(Dummy(31.504971569867017, 74.3187696782443))//31.504971569867017, 74.3187696782443

        viewModel.getWholeRecord().observe(this@DashboardActivity){
           // val abc= it
            var arraylist: ArrayList<SaveLatLng> = ArrayList()
            arraylist = it as ArrayList<SaveLatLng>
            Log.v("fetch record","$it")
            Toast.makeText(this@DashboardActivity, "List: $it", Toast.LENGTH_SHORT).show()
            var totalDistanceInMeters = 0.0
            val startLocation = Location("point A")
            startLocation.latitude = arraylist[0].lat!!
            startLocation.longitude = arraylist[0].lng!!
            for (i in 1 until arraylist.size){
                val endLocation = Location("point B")
                endLocation.latitude = arraylist[i].lat!!
                endLocation.longitude = arraylist[i].lng!!
                val distanceInMeters  = startLocation.distanceTo(endLocation)
                totalDistanceInMeters += distanceInMeters
                // Set the current endLocation as the startLocation for the next iteration
                startLocation.latitude =  endLocation.latitude
                startLocation.longitude = endLocation.longitude
                Log.v("MyArraylist","calculated distance in meter: $totalDistanceInMeters")
            }

        }
        //loop
//        var totalDistanceInMeters = 0.0
//        val startLocation = Location("point A")
//        startLocation.latitude =  arraylist.get(0).lat
//        startLocation.longitude = arraylist.get(0).lng
//        Log.v("MyArraylist"," arraylist size:${startLocation}")
//        for (i in 1 until arraylist.size){
//            val endLocation = Location("point B")
//            endLocation.latitude =  arraylist[i].lat
//            endLocation.longitude = arraylist[i].lng
//            val distanceInMeters  = startLocation.distanceTo(endLocation)
//            totalDistanceInMeters += distanceInMeters
//            // Set the current endLocation as the startLocation for the next iteration
//            startLocation.latitude = endLocation.latitude
//            startLocation.longitude = endLocation.longitude
//            Log.v("MyArraylist","calculated distance in meter: $totalDistanceInMeters")
//        }
//
//        Log.v("MyArraylist"," arraylist size:${arraylist.size}")
//        Log.v("MyArraylist"," all list data:$arraylist")
//        Log.v("MyArraylist"," specific index data :${arraylist.get(4)}")
//
//
//        val locationA = Location("point A")
//        locationA.latitude = 31.506682158080444 // Replace with the actual latitude of point A
//        locationA.longitude = 74.3205506248439 // Replace with the actual longitude of point A
//
//        val locationB = Location("point B")
//        locationB.latitude = 31.50903295442674// Replace with the actual latitude of point B
//        locationB.longitude = 74.32266420549088// Replace with the actual longitude of point B
//        val distance = locationA.distanceTo(locationB)
//        Log.v("distance"," distance in meter:$distance")
//// The distance is in meters
//// You can convert it to other units like kilometers or miles if needed
//        val distanceInKm = distance / 1000
//        val distanceInMiles = distance / 1609.34
//        Log.v("distance"," distance in km:$distanceInKm")
//        Log.v("distance"," distance in miles:$distanceInMiles")

    }
}
