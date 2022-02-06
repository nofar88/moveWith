package com.example.movewith.Model;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.view.View;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.core.app.ActivityCompat;
import androidx.viewpager.widget.ViewPager;

import java.util.List;
import java.util.Locale;
// מטרת המחלקה למצוא את המיקום האוטומטי של איפה שאנחנו נמצאים
public class GPSLocation extends Activity implements LocationListener {

    // Acquire a reference to the system Location Manager
    LocationManager locationManager;

    private EditText city;
    private EditText street;
    private EditText number;
    private Context context;
    private LinearLayout GPSon;
    private LinearLayout GPSoff;

    // לאתחל את כל השדות
    public void setUp(EditText _city, EditText _street, EditText _number, Context _context, LinearLayout _GPSon, LinearLayout _GPSoff) {
        city = _city;
        street = _street;
        number = _number;
        context = _context;
        GPSon = _GPSon;
        GPSoff = _GPSoff;

        locationManager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);// פונקציה של אדרואיד שיודעת לזהות את המיקום האוטומטי לוקח את זה מתוך הקונטקסט

        // בדיקה שיש הרשאה לגי פי אס
        if (ActivityCompat.checkSelfPermission((Activity) context, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED ||
                ActivityCompat.checkSelfPermission((Activity) context, Manifest.permission.SEND_SMS) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions((Activity) context, new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.SEND_SMS}, 5);
        } else {
            locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, this);// מתחיל להאזין לשינויים של הגי פי אס במידה וקיימת הרשאה
        }
    }

    @SuppressLint("MissingPermission")
    @Override
    // פונקציה שאם לא היה הרשאה אז מחכים להרשאה ומתחילים להאזין לשינויים של הגי פי אס
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        if (requestCode == 5) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Permission is granted
                locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, this);
            }
        } else {
            Toast.makeText(getApplicationContext(), "Until you grant the permission, we cannot display the location", Toast.LENGTH_SHORT).show();
        }
    }
// לוקח את המיקום וממיר את המיקום לטקסט
    public void updateTextViews(Location location) {
        Geocoder geocoder = new Geocoder(context, Locale.getDefault());
        List<android.location.Address> addresses = null;
        try {
            addresses = geocoder.getFromLocation(location.getLatitude(), location.getLongitude(), 1);
            if (addresses.size() > 0) {
                String temp1 = addresses.get(0).getAddressLine(0);
                String[] temp2 = temp1.split(", ");

                city.setText(temp2[1]);
                street.setText(splitNumberFromStreet(temp2[0])[0]);
                number.setText(splitNumberFromStreet(temp2[0])[1]);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

// מונקציה שמפרידה לפי פיסיקים ומחזיר את רחוב ומספר
    private String[] splitNumberFromStreet(String numberAndStreet) {
        String[] result = {"", ""};
        String[] splitedAddress = numberAndStreet.split(" ");

        for (int i = 0; i < splitedAddress.length - 1; i++)
            result[0] += splitedAddress[i] + " ";

        result[0] = result[0].substring(0, result[0].length() - 1).replace("רחוב", "");
        result[1] = splitedAddress[splitedAddress.length - 1];

        return result;
    }


    @Override
    public void onLocationChanged(Location location) {
        updateTextViews(location);
    }

    @Override
    public void onStatusChanged(String s, int i, Bundle bundle) {
    }

    @Override
    public void onProviderDisabled(String s) {
        Toast.makeText(context, "לזיהוי מיקום אוטמטי הפעל GPS", Toast.LENGTH_LONG).show();
    }

    // כאשר רוצים להפסיק להקשיב לגי פי אס
    public void stopListeners() {
        locationManager.removeUpdates(this);
        locationManager = null;
    }
}
