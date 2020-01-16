package com.adsulganesh.pokemon

import android.content.Context
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.core.app.ActivityCompat

import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.BitmapDescriptor
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions

class MapsActivity : AppCompatActivity(), OnMapReadyCallback {

    private lateinit var mMap: GoogleMap

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_maps)
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)

        checkPermission()
        addPokemons()

    }

    var accessLocation = 123

    fun checkPermission(){
        if(Build.VERSION.SDK_INT >= 23) {
            if (ActivityCompat.checkSelfPermission(this,
                    android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED){ //to check whether user has granted a permission to the application
                requestPermissions(arrayOf(android.Manifest.permission.ACCESS_FINE_LOCATION),accessLocation)
                return
            }

        }
        getUserLocation()

    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        when(requestCode){
            accessLocation ->{
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED)
                    getUserLocation()
                else
                    Toast.makeText(this,"We cannot access to your location",Toast.LENGTH_SHORT).show()
            }
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
    }

    fun getUserLocation() {

        Toast.makeText(this,"User Access Location is On.",Toast.LENGTH_SHORT).show()
        var myLocation = userLocation()
        var locationManager = getSystemService(Context.LOCATION_SERVICE) as LocationManager
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,3,3f, myLocation)
        var myThread = myThread()
        myThread.start()
    }

    var location:Location ?= null
    //Get User Location
    inner class userLocation:LocationListener{
        constructor(){
            location = Location("Start")
            location!!.latitude = 0.0
            location!!.longitude = 0.0
        }

        override fun onLocationChanged(location1: Location?) {

            location = location1
        }

        override fun onStatusChanged(provider: String?, status: Int, extras: Bundle?) { //Used when gps is set on or off
            //TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
        }

        override fun onProviderEnabled(provider: String?) {
            //TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
        }

        override fun onProviderDisabled(provider: String?) {
            //TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
        }
    }

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap

        // Add a marker in Sydney and move the camera
    }


    var oldLocation:Location?=null
    inner class myThread:Thread{
        constructor():super(){

            oldLocation = Location("Start")
            oldLocation!!.latitude = 0.0
            oldLocation!!.longitude = 0.0

        }

        override fun run() {
            while (true){
                try {
                    if(oldLocation!!.distanceTo(location)==0f)
                    {
                        continue
                    }
                    oldLocation = location

                    runOnUiThread(){
                        mMap!!.clear()

                        //To Show User Location
                        val myLoc = LatLng(location!!.latitude, location!!.longitude)
                        mMap.addMarker(MarkerOptions()
                            .position(myLoc)
                            .title("My Location")
                            .snippet("Here is my location.")
                            .icon(BitmapDescriptorFactory.fromResource(R.drawable.mario)))
                        mMap.moveCamera(CameraUpdateFactory.newLatLng(myLoc))

                        //To show Pokemons
                        for (i in listPokemon){

                            var poke = i
                            if (!poke.isCatched){
                                val pokemonLocation = LatLng(poke.lat!!, poke.log!!)
                                mMap.addMarker(MarkerOptions()
                                    .position(pokemonLocation)
                                    .title(poke.name)
                                    .snippet(poke.desc+", Power:"+poke.power)
                                    .icon(BitmapDescriptorFactory.fromResource(poke.image!!)))
                                mMap.moveCamera(CameraUpdateFactory.newLatLng(pokemonLocation))
                            }
                        }

                    }
                    Thread.sleep(1000)
                }catch (ex:Exception){}
            }
        }
    }

    //Adding Pokemons to the map

    var  listPokemon = arrayListOf<Pokemon>()

    fun addPokemons(){
        listPokemon.add(Pokemon("Charmander","Fire Type Pokemon.",199.5,R.drawable.charmander,19.005962,72.831187))
        listPokemon.add(Pokemon("Bulbasaur","Grass and Poison Type Pokemon.",179.5,R.drawable.bulbasaur,19.006248,72.829575))
        listPokemon.add(Pokemon("Squirtle","Water Type Pokemon.",176.3,R.drawable.squirtle,19.009367,72.830715))
    }
}
