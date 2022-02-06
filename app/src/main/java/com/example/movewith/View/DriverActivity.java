package com.example.movewith.View;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.Toast;

import com.example.movewith.Control.Control;
import com.example.movewith.Model.Address;
import com.example.movewith.Model.Consts;
import com.example.movewith.Model.Driver;
import com.example.movewith.Model.FirebaseManagement;
import com.example.movewith.Model.GPSLocation;
import com.example.movewith.Model.MemoryAccess;
import com.example.movewith.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.sql.Time;
import java.time.LocalTime;
import java.util.Calendar;
import java.util.Date;

public class DriverActivity extends AppCompatActivity {
    EditText city, street, number, fullName, ageString, phoneNumber;// תיבת טקסט שניתן להקליד בתוכה
    FloatingActionButton next;
    AutoCompleteTextView gender;// השלמה אוטומטית למין
    GPSLocation gpsLocation;

    // מסך של הנהג
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_driver);

        // השלמה אוטומטית של זכר או נקבה במין של הנהג
        String[] genders = {"זכר", "נקבה"};
        //לוקח רשימה של סטרינג ויודע להשלים , יודע להציב את הערכים
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_dropdown_item_1line, genders);
        gender = findViewById(R.id.gender_list);
        gender.setAdapter(adapter);
// ממיר את האקסמל לאוביקט של גאבה
        city = findViewById(R.id.city);
        street = findViewById(R.id.steet_src);
        number = findViewById(R.id.number_src);

        gpsLocation = new GPSLocation();
        gpsLocation.setUp(city, street, number, this, null, null);

        next = findViewById(R.id.next_page);
        next.setOnClickListener(v -> {
            Toast.makeText(this, "מעלה את הנתונים לשרת", Toast.LENGTH_LONG).show();
            createDriver();
        });

        fullName = findViewById(R.id.full_name);
        ageString = findViewById(R.id.age);
        phoneNumber = findViewById(R.id.phone_number);

        // כל פעם שמזהה שינוי בתיבת טקסט הוא שומר את זה בזיכרון
        fullName.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                MemoryAccess.saveToMemory(Consts.driverFullName, s.toString());
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });

        ageString.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                MemoryAccess.saveToMemory(Consts.driverAge, s.toString());
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });

        phoneNumber.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                MemoryAccess.saveToMemory(Consts.driverPhone, s.toString());
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });

        gender.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                MemoryAccess.saveToMemory(Consts.driverGender, s.toString());
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
// ברגע שהמסך עולה אז מעלים מהזיכרון את התנונים הקודמים ששמורים בו
        fullName.setText(MemoryAccess.loadFromMemory(Consts.driverFullName));
        ageString.setText(MemoryAccess.loadFromMemory(Consts.driverAge));
        phoneNumber.setText(MemoryAccess.loadFromMemory(Consts.driverPhone));
        gender.setText(MemoryAccess.loadFromMemory(Consts.driverGender));
    }

    // יוצרת נהג שמעלה את כל הנתונים עליו תוך בדיקה שמילא את כל השדות נכון
    public void createDriver() {
        String fullName = (this.fullName).getText().toString();
        if (fullName.length() == 0) {
            Toast.makeText(this, "שדה השם ריק", Toast.LENGTH_SHORT).show();
            return;
        }

        String gender = this.gender.getText().toString();
        if (gender.length() == 0) {
            Toast.makeText(this, "שדה המין ריק", Toast.LENGTH_SHORT).show();
            return;
        }

        String ageString = this.ageString.getText().toString();
        if (ageString.length() == 0) {
            Toast.makeText(this, "שדה הגיל ריק", Toast.LENGTH_SHORT).show();
            return;
        }
        int age = Integer.parseInt(ageString);

        String phoneNumber = this.phoneNumber.getText().toString();
        if (!phoneNumber.matches("^05\\d([-]?)\\d{7}$")) {// ביטוי ריגולארי
            Toast.makeText(this, "מספר טלפון לא תקין", Toast.LENGTH_SHORT).show();
            return;
        }

        String priceString = ((EditText) findViewById(R.id.cost)).getText().toString();
        if (priceString.length() == 0) {
            Toast.makeText(this, "שדה המחיר ריק", Toast.LENGTH_SHORT).show();
            return;
        }
        double price = Double.parseDouble(priceString);

        String timeString = ((EditText) findViewById(R.id.time)).getText().toString();
        if (!timeString.matches("^([0-9]|0[0-9]|1[0-9]|2[0-3]):[0-5][0-9]$")) {
            Toast.makeText(this, "השעה אינה תקינה", Toast.LENGTH_SHORT).show();
            return;
        }
        String[] timeArray = timeString.split(":");
        int hour = Integer.parseInt(timeArray[0]);
        int minute = Integer.parseInt(timeArray[1]);
        int second = 0;
        if (timeArray.length > 2)
            second = Integer.parseInt(timeArray[2]);
        Date time = new Date();
        LocalTime refTime = LocalTime.of(hour, minute, second);
        LocalTime now = LocalTime.now();
        if (now.isAfter(refTime)) {
            Calendar c = Calendar.getInstance();
            c.setTime(time);
            c.add(Calendar.DATE, 1);
            time = c.getTime();
        }
        time.setHours(hour);
        time.setMinutes(minute);
        time.setSeconds(second);

        String street = ((EditText) findViewById(R.id.steet_src)).getText().toString();
        if (street.length() == 0) {
            Toast.makeText(this, "שדה הרחוב ריק", Toast.LENGTH_SHORT).show();
            return;
        }

        String city = ((EditText) findViewById(R.id.city)).getText().toString();
        if (city.length() == 0) {
            Toast.makeText(this, "שדה העיר ריק", Toast.LENGTH_SHORT).show();
            return;
        }

        String numberString = ((EditText) findViewById(R.id.number_src)).getText().toString();
        if (numberString.length() == 0) {
            Toast.makeText(this, "שדה מספר ריק", Toast.LENGTH_SHORT).show();
            return;
        }
        int number = Integer.parseInt(numberString);
        Address source = new Address(city, street, number);

        street = ((EditText) findViewById(R.id.street_dest)).getText().toString();
        if (street.length() == 0) {
            Toast.makeText(this, "שדה הרחוב ריק", Toast.LENGTH_SHORT).show();
            return;
        }

        city = ((EditText) findViewById(R.id.city_dest)).getText().toString();
        if (city.length() == 0) {
            Toast.makeText(this, "שדה העיר ריק", Toast.LENGTH_SHORT).show();
            return;
        }

        numberString = ((EditText) findViewById(R.id.number_dest)).getText().toString();
        if (numberString.length() == 0) {
            Toast.makeText(this, "שדה מספר ריק", Toast.LENGTH_SHORT).show();
            return;
        }
        number = Integer.parseInt(numberString);

        Address destination = new Address(city, street, number);
//ברגע שכל הקהלטים תקינים - מכניס את כל הנתונים לנהג בפרייבייס
        next.setEnabled(false);
        Driver driver = new Driver(fullName, gender, age, phoneNumber, price, time, source, destination);
        Control.saveDriver(driver);
    }
//פונקציה של האטיביתי שקוראים לה שהמסך ניסגר
    @Override
    protected void onDestroy() {
        super.onDestroy();
        gpsLocation.stopListeners();
    }
}