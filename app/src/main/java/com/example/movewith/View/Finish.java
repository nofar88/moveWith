package com.example.movewith.View;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.movewith.Model.Driver;
import com.example.movewith.R;

import java.net.URLEncoder;

public class Finish extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_finish);

        Driver driver = (Driver) getIntent().getSerializableExtra("driver");//על מנת לעבור ממסך למסך עם התנונים צריך להעביר אותם בביטים -
                                                               // בחרנו את הנהג שאיתו נרצה לנסוע - ולכן ניקח אותו למסך הסופי כדי שנשלח לו הודעה
        TextView view = findViewById(R.id.msg);
        view.setText("בחרת לנסוע עם " + driver.fullName + ", לחץ על הכפתור כדי ליצור קשר");

        StringBuilder builder = new StringBuilder();
        builder.append("היי ");
        builder.append(driver.fullName);
        builder.append(",\n");
        builder.append("אשמח להצטרף איתך לנסיעה אל ");
        builder.append(driver.destination);

        Button sms = findViewById(R.id.contact_sms);
        sms.setOnClickListener(v -> {
            SmsManager manager = SmsManager.getDefault();
            manager.sendTextMessage(driver.phoneNumber, null, builder.toString(), null, null);
            Toast.makeText(this, "ההודעה נשלחה", Toast.LENGTH_SHORT).show();
        });

        Button whatsapp = findViewById(R.id.contact_whatsapp);
        whatsapp.setOnClickListener(v -> {
            String phone = driver.phoneNumber.substring(1);
            String url = "http://wa.me/+972" + phone + "?text=";
            String encoded = URLEncoder.encode(builder.toString());
            Uri uri = Uri.parse(url + encoded);
            Intent intent = new Intent(Intent.ACTION_VIEW, uri);
            startActivity(intent);
        });
    }

    //לחזור למסך הראשי בסיום בחירת הנסיעה ושליחת ההודעות
    @Override
    public void onBackPressed() {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }
}