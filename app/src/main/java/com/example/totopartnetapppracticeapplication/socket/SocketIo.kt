package com.example.totopartnetapppracticeapplication.socket

import android.util.Log
import com.example.totopartnetapppracticeapplication.model.SaveMyLocation
import com.example.totopartnetapppracticeapplication.util.Urls
import io.socket.client.IO
import io.socket.client.Socket
import org.json.JSONException
import org.json.JSONObject
import java.net.URISyntaxException


object SocketIo {
    private lateinit var socket: Socket
    fun connect() {
        try {
            socket = IO.socket(Urls.SOCKET_URL)
            listenSocketOn()
            Log.d("listensocket", "socket is connected")

        } catch (e: URISyntaxException) {
            e.printStackTrace()
        }
    }

    private fun listenSocketOn() {
        if (socket.connected() == true){
            socket.disconnect()
        }
        socket.on(Socket.EVENT_CONNECT){
            Log.d("listensocket","connect")
        }
        socket.on(Socket.EVENT_CONNECTING){
            Log.d("listensocket","connecting")
        }
        socket.on(Socket.EVENT_DISCONNECT){
            Log.d("listensocket","disconnect")
        }
        socket.on(Socket.EVENT_CONNECT_ERROR){
            for (i in it.indices) {
                Log.e("listensocket", "Error connecting $i: ${it[i]}")
            }
        }

        socket.connect()
    }
    fun disconnect() {
        socket.disconnect()
    }
    fun emitLatLng(saveMyLocation: SaveMyLocation) {
        try {
            val jsonObject = JSONObject().apply {
                this.put("latitude", saveMyLocation.latitude)
                this.put("longitude", saveMyLocation.longitude)
                this.put("driver_status", saveMyLocation.driver_status)
                this.put("booking_id", saveMyLocation.booking_id)
                this.put("area_name", saveMyLocation.area_name)
                this.put("city", saveMyLocation.city)
                this.put("bearing", saveMyLocation.bearing)
                this.put("user_id", saveMyLocation.user_Id)
            }
            Log.d("listensocket", "json object: $jsonObject")
            socket.emit("1-point-to-point-tracking", jsonObject)
//            listenLatLng()

        } catch (e: JSONException) {
            Log.d("emitLatLng", "json exception: $e")
        } catch (e: Exception) {
            Log.d("listensocket", "exception: $e")
        }

    }

    fun listenLatLng() {
        socket.on("1-driverCoordinate") { args ->
            Log.d("listensocket", "driverCoordinate ${args[0]}")
            if (args.isNotEmpty()){
                try {
                    val jsonObject= args[0] as JSONObject

                }catch (e:Exception){
                    Log.d("listensocket", "$e")
                }
                catch (e:JSONException){
                    Log.d("listensocket", "$e")

                }
            }
        }
//         val onMessageReceived = Emitter.Listener { args ->
//            val message = args[0] as String
//            // Handle the received message here
//        }
    }

    private fun socketEvents(){

    }
}