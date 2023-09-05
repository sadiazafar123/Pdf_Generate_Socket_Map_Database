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
import androidx.room.util.query
import com.example.totopartnetapppracticeapplication.R
import com.example.totopartnetapppracticeapplication.databinding.ActivityDashboardBinding
import com.example.totopartnetapppracticeapplication.model.SaveLatLng
import com.example.totopartnetapppracticeapplication.services.LocationService
import com.example.totopartnetapppracticeapplication.socket.SocketIo
import com.example.totopartnetapppracticeapplication.viewmodel.MyViewModel
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.MapsInitializer
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.google.firebase.firestore.Filter.and
import com.google.firebase.firestore.Filter.arrayContains
import com.google.firebase.firestore.Filter.equalTo
import com.google.firebase.firestore.Filter.or
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import io.reactivex.SingleObserver
import io.reactivex.disposables.Disposable
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter


class DashboardActivity : AppCompatActivity(){
    private lateinit var firebaseFirestore:FirebaseFirestore

    private val viewModel: MyViewModel by viewModel()

    var permissionCode = 101
    var services: Intent? = null
    private lateinit var mMap: GoogleMap
    private val layoutResID: Int @LayoutRes get() = R.layout.activity_dashboard
    private lateinit var binding:ActivityDashboardBinding
    private lateinit var myBroadcastReceiver: MyBroadcastReceiver
    private var lat2: Double? = 0.0
    private var lng2: Double? = 0.0
    private var distance: Float? = 0.0f
    val startLocation = Location("point A")
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
        //get instance
        firebaseFirestore= FirebaseFirestore.getInstance()

        //socket connection
        SocketIo.connect()
        SocketIo.listenLatLng()

        //register broadcast receiver
        myBroadcastReceiver= MyBroadcastReceiver()
        val intentFilter2 = IntentFilter(Intent.ACTION_PACKAGE_ADDED)
        val intentFilter3 = IntentFilter("LocationToast")
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
          //  viewModel.deleteAllRecord()
        }
        binding.btnSpecificDel.setOnClickListener {
            viewModel.deleteSpecificRecord()
            distance()
        }
        binding.btnCalculateDistance.setOnClickListener {
           // calculateDistance()
            //observe data using rx java with 3 method implement(onSubscribe,onSuccess,onError)
            //onError will triggered when there woukd be no entry in db
            val totalDistanceCompletable =viewModel.calculateTotalDistance()
            totalDistanceCompletable.subscribe(object : SingleObserver<Int> {
                override fun onSubscribe(d: Disposable) {
                    Log.d("DashboardActivity", "on subscribe: $d")
                    Toast.makeText(this@DashboardActivity, "on subscribe: $d", Toast.LENGTH_SHORT).show()
                }
                override fun onSuccess(t: Int) {
                    Log.d("DashboardActivity", "on success: $t")
                    Toast.makeText(this@DashboardActivity, "on success: $t", Toast.LENGTH_SHORT).show()
                }
                override fun onError(e: Throwable) {
                    Log.d("DashboardActivity", "Error while reading Sports Data: $e")
                    Toast.makeText(this@DashboardActivity, "on Error: $e", Toast.LENGTH_SHORT).show()
                }
            })

// observe data through livedata
//            viewModel.calculateTotalDistance().observe(this){
//                Log.v("total distance","$it")
//                Toast.makeText(this, "total distance: $it", Toast.LENGTH_SHORT).show()
//
//            }
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
                    lat2 = intent.extras?.getDouble("lat",0.0)
                    lng2 = intent.extras?.getDouble("lng",0.0)
                    val endLocation = Location("point B")
                    endLocation.latitude = lat2!!
                    endLocation.longitude= lng2!!
                    if (startLocation.latitude == 0.0 /*&& startLocation.longitude == 0.0*/){
                        distance = 0.0f
                        startLocation.latitude  = lat2 as Double
                        startLocation.longitude = lng2 as Double
                    }else{
                        distance  = startLocation.distanceTo(endLocation)
                        startLocation.latitude  = lat2 as Double
                        startLocation.longitude = lng2 as Double
                    }
                    if (::mMap.isInitialized){
                        mMap.clear()
                        mMap.addMarker(MarkerOptions().position(LatLng(lat2?:0.0,lng2?:0.0)).title("Your current location"))
                        mMap.moveCamera(
                            CameraUpdateFactory.newLatLngZoom(LatLng(lat2?:0.0,lng2?:0.0), 15f)
                        )
                        ///data insert in db
                        viewModel.insertLatLng(lat2,lng2,formattedTime, distance!!)
                        //insert in firestore
//                        val addLatLng = HashMap<String,Any>()
//                        addLatLng["latitude"] = lat2!!
//                        addLatLng["longitude"]= lng2!!
////
//                        firebaseFirestore.collection("Location")
//                            .add(addLatLng).addOnSuccessListener {
//                                Log.d("firestore","data insert succesfully")
//                            }
//                            .addOnFailureListener{ e->
//                                Log.d("firestore","Error writing document")
//                            }
                        /////
                        val addLatLng1 = HashMap<String,Any>()
                       addLatLng1["latitude"] = "31.49786"
                        addLatLng1["longitude"]= "74.30609"
                        val addLatLng2 = HashMap<String,Any>()
                        addLatLng2["latitude"] = "31.49687"
                        addLatLng2["longitude"]= "74.30497"
                        val addLatLng3 = HashMap<String,Any>()
                        addLatLng3["latitude"] = "31.49565"
                        addLatLng3["longitude"]= "74.30388"
                        val addLatLng4 = HashMap<String,Any>()
                        addLatLng4["latitude"] = "31.4947"
                        addLatLng4["longitude"]= "74.30296"
                        val addLatLng5 = HashMap<String,Any>()
                        addLatLng5["latitude"] = "31.49382"
                        addLatLng5["longitude"]= "74.30201"
                        val addLatLng6 = HashMap<String,Any>()
                        addLatLng6["latitude"] = "31.49307"
                        addLatLng6["longitude"]= "74.30128"
// hashMapOf
//                        val data1 = hashMapOf(
//                            "name" to "San Francisco",
//                            "state" to "CA",
//                            "country" to "USA",
//                            "capital" to false,
//                            "population" to 860000,
//                            "regions" to listOf("west_coast", "norcal"),
//                        )

                        firebaseFirestore.collection("Location").document("id:1").set(addLatLng1)
                        firebaseFirestore.collection("Location").document("id:2").set(addLatLng2)
                        firebaseFirestore.collection("Location").document("id:3").set(addLatLng3)
                        firebaseFirestore.collection("Location").document("id:4").set(addLatLng4)
                        firebaseFirestore.collection("Location").document("id:5").set(addLatLng5)
                        firebaseFirestore.collection("Location").document("id:6").set(addLatLng6)
                        ///applying where queries
//                        val latLngRef=firebaseFirestore.collection("Location")
//                        val query= latLngRef.whereEqualTo("latitude","31.49307")
//                        Log.d("query", "$query")



                        firebaseFirestore.collection("Location")
                            .whereEqualTo("latitude", "31.49382")
                            .get()
                            .addOnSuccessListener { documents ->
                                for (document in documents) {
                                    val data = document.data
                                    val latitude = data["latitude"] as String
                                    val longitude = data["longitude"] as String
                                     Log.d("query", "data is: $data")
                                    // Log.d("query", "longitude is: $latitude")
                                     //Log.d("query", "longitude is: $longitude")
                                }
                            }
                            .addOnFailureListener { exception ->
                                Log.w("query", "Error getting documents: ", exception)
                            }
                        ///to get specific data applying OR query
                       firebaseFirestore.collection("Location")
                            .where(or(equalTo("latitude","31.49307"), equalTo("longitude","74.30201")))
                            .get()
                           .addOnSuccessListener {documents->
                           for (document in documents ){
                               val data= document.data
                               Log.d("query", "and query: $data")
                           }

                           }.addOnFailureListener{exception->
                               Log.d("query", "and query: $exception")
                           }
                        ///applying AND query if firestore
                        firebaseFirestore.collection("Location")
                            .where(and(equalTo("latitude","31.49307"), equalTo("longitude","74.30201")))
                            .get()
                            .addOnSuccessListener {documents->
                                for (document in documents ){
                                    val data= document.data
                                    Log.d("query", "and query: $data")
                                }

                            }.addOnFailureListener{exception->
                                Log.d("query", "and query: $exception")
                            }




//                        firebaseFirestore.collection("Location")
//                            .document("id")
//                            .set(addLatLng)
//                            .addOnSuccessListener {
//                                Log.d("firestore","data insert succesfully")
//                            }
//                            .addOnFailureListener{ e->
//                                Log.d("firestore","Error writing document")
//
//                            }
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
            /////listen socket

        }
    }
    fun distance(){
        //insert dummy data in arraylist
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
            val arraylist: ArrayList<SaveLatLng>
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
    fun calculateDistance(){
        val locationA = Location("point A")
        locationA.latitude = 31.48983   // Replace with the actual latitude of point A
        locationA.longitude = 74.29712  // Replace with the actual longitude of point A

        val locationB = Location("point B")
        locationB.latitude = 31.49107 // Replace with the actual latitude of point B
        locationB.longitude = 74.29884 // Replace with the actual longitude of point B

        val distance = locationA.distanceTo(locationB)
        Log.v("calculateDistance"," distance in meter:$distance")
    }
//    fun socketInit(){
//        //send to server
//        socket.emit("save_lat_lng","LatLng")
//        ///listen data from server
//        socket.on("save_lat_lng", object : Emitter.Listener{
//            override fun call(vararg args: Any?) {
//            }
//        })
//        //listen
//        socket.on("save_lat_lng"){ args->
//            Log.d("newLatLng","${args[0]}")
//        }
//    }

//    fun emitJsonObject(){
//        val username  =  "sadia"
//        val password1 =  "12345"
//        val password2 =  "67890"
//        val jsonObject = JSONObject()
//        jsonObject.put("username"  , username)
//        jsonObject.put("password1" , password1)
//        jsonObject.put("password2" , password2)
//        socket.emit("Signup",jsonObject)
//    }
//    fun onJsonObject(){
//        socket.on("Signup"){ args ->
//            Log.d("Signup","${args[0]}")
//        }
//        socket.on(Socket.EVENT_CONNECT){
//
//        }
//    }

}
