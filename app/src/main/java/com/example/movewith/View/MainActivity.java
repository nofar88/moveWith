package com.example.movewith.View;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.movewith.Control.Control;
import com.example.movewith.Model.FirebaseManagement;
import com.example.movewith.Model.MemoryAccess;
import com.example.movewith.R;

public class MainActivity extends AppCompatActivity {
    private Button lastDrive;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Control.activity = this;
        MemoryAccess.activity = this;

        Button driver = findViewById(R.id.driver_button);
        Button hitchhiker = findViewById(R.id.hitchhiker_button);
        lastDrive = findViewById(R.id.lastDrive_button);

        // אם נלחץ הכפתור של הנהג הוא מעביר למסך המתאים
        driver.setOnClickListener(v -> {
            Intent myIntent = new Intent(MainActivity.this, DriverActivity.class);
            MainActivity.this.startActivity(myIntent);
        });
        // אם נלחץ הכפתור של הטרמפיסט הוא עובר למסך המתאים
        hitchhiker.setOnClickListener(v -> {
            Intent myIntent = new Intent(MainActivity.this, HitchhikerActivity.class);
            MainActivity.this.startActivity(myIntent);
        });
//דיאלוג
        Activity activity = this;
        lastDrive.setOnClickListener(v -> {
            new AlertDialog.Builder(this)
                    .setTitle("מחיקת הנסיעה המתוכננת")
                    .setMessage("האם ברצונך למחוק את הנסיעה המתוכננת האחרונה שהעלת?")
                    .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            FirebaseManagement.cancelDriver(Control.lastDrive(), activity);
                        }
                    })

                    // A null listener allows the button to dismiss the dialog and take no further action.
                    .setNegativeButton(android.R.string.no, null)
                    .show();
        });

        FirebaseManagement.toLate(Control.lastDrive(), this);
    }

    // בדיקה מתי יש צורך לשים את הכפתור האדום ומתי לא
    public void refreshView() {
        if (!Control.lastDrive().equals("")) {
            lastDrive.setVisibility(View.VISIBLE);
        } else {
            lastDrive.setVisibility(View.GONE);
        }
    }
}