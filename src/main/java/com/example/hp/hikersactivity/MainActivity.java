package com.example.hp.hikersactivity;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.ActivityCompat.OnRequestPermissionsResultCallback;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import java.util.List;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {

    LocationManager locationManager;
    LocationListener locationListener;

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == 1) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                startListening();
            }
        }
    }

    public void startListening() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            locationManager = (LocationManager)this.getSystemService(Context.LOCATION_SERVICE);
        }
    }

    public void updateLocation (Location location) {

        TextView LatitudeTextView = (TextView) findViewById(R.id.LatitudeTextView);
        TextView LongitudeTextView = (TextView) findViewById(R.id.LongitudeTextView);
        TextView AccuracyTextView = (TextView) findViewById(R.id.AccuracyTextView);
        TextView AltitudeTextView = (TextView) findViewById(R.id.AltitudeTextView);


        LatitudeTextView.setText("Latitude :" + Double.toString(location.getLatitude()));
        LongitudeTextView.setText("Longitude :" + Double.toString(location.getLongitude()));
        AccuracyTextView.setText("Accuracy :" + Double.toString(location.getAccuracy()));
        AltitudeTextView.setText("Altitude :" + Double.toString(location.getAltitude()));

        String address = "Could not find Address";

        Geocoder geocoder = new Geocoder(getApplicationContext(), Locale.getDefault());

        try {

            List<Address> addressList = geocoder.getFromLocation(location.getLatitude(), location.getLongitude(), 1);


            if (addressList != null && addressList.size() > 0) {


                if (addressList.get(0).getPostalCode() != null) {
                    address += addressList.get(0).getPostalCode() + "\n";
                }
                if (addressList.get(0).getLocality() != null) {
                    address += addressList.get(0).getLocality() + " , ";
                }
                if (addressList.get(0).getAdminArea() != null) {
                    address += addressList.get(0).getAdminArea() + " , ";
                }
                if (addressList.get(0).getCountryName() != null) {
                    address += addressList.get(0).getCountryName();
                }
            }

            TextView AddressTextView = (TextView) findViewById(R.id.AddressTextView);
            AddressTextView.setText("Address: " + address);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

        @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        locationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);
        locationListener = new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {

                updateLocation(location);
            }

            @Override
            public void onStatusChanged(String s, int i, Bundle bundle) {

            }

            @Override
            public void onProviderEnabled(String s) {

            }

            @Override
            public void onProviderDisabled(String s) {

            }
        };
        if (Build.VERSION.SDK_INT < 23) {
            startListening();
        } else {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);
            } else {
                locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, locationListener);
                Location lastKnownLocation = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
                if (lastKnownLocation != null) {
                    updateLocation(lastKnownLocation);
                }


            }

        }
    }


    }

