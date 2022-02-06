package com.example.movewith.Control;

import static android.content.Intent.FLAG_ACTIVITY_NEW_TASK;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.widget.Toast;

import com.example.movewith.Model.Driver;
import com.example.movewith.Model.FirebaseManagement;
import com.example.movewith.Model.Hitchhiker;
import com.example.movewith.Model.Match;
import com.example.movewith.View.MainActivity;
import com.example.movewith.View.Prefer;
import com.example.movewith.View.SuccessDriver;

import java.util.ArrayList;
import java.util.List;

public class Control {
    public static MainActivity activity;// מסך ראשוני
    public static Prefer prefer;// מסך של העדפה
    public static Hitchhiker hitchhiker;

    // פונקציה שמטרתה זה לשמור נהג
    public static void saveDriver(Driver driver) {
        FirebaseManagement.saveDriver(driver, activity);
    }

    // פוקנציה שמטרת לבדוק שפרטי הנהג הועלו בהצלחה.
    public static void driverUploaded(boolean success) {
        if (success) {
            Intent myIntent = new Intent(activity, SuccessDriver.class);// מעביר למסך של הצליח לעלות
            myIntent.addFlags(FLAG_ACTIVITY_NEW_TASK);
            activity.startActivity(myIntent);// פתיחת המסך שאליו עברנו
        } else {
            Toast.makeText(activity, "לא הצלחנו להעלות את הנתונים, נסה שוב", Toast.LENGTH_SHORT).show();
        }
        refreshView();// מרענן את המס על מנת שהכפתור האדום יופיע
    }

    // מקבל מהזיכרון את הנסיעה האחרונה שהועלתה
    public static String lastDrive() {
        SharedPreferences sharedPref = activity.getPreferences(Context.MODE_PRIVATE);
        return sharedPref.getString("lastDrive", "");// אם אין שום נסיעה אחרונה תחזיר סטרינג ריק אחרת תחזיר את הנסיעה עצמה
    }

    // פונקציה שמתחילה את החיפוש
    public static void startMatch(Hitchhiker hitchhiker, Prefer prefer) {
        Control.hitchhiker = hitchhiker;
        Control.prefer = prefer;
        FirebaseManagement.downloadDrivers();// רצה בתהליכון נפרד כדי לא לתקוע את האפליקציה
    }

    // יוצר רשימה של התאמות ואז מפעיל את הפונקציה של החישוב עצמו שמביא לנו לפי דירוג מסוים את ההתאמות.
    public static void findMatch(List<Driver> drivers) {
        ArrayList<Match> matches = new ArrayList<>();// יצירת רשימה חדשה של התאמות
        for (Driver driver : drivers) {
            matches.add(new Match(driver, Control.hitchhiker));// מחקה מאצ' עושה מופע למחלקה כדי שנשלח את זה לקבלת ציון
        }
        new MatchCalculator().execute(matches);// רץ בתהליכון נפרד, מפעיל את החישובים לניתן ציון
    }

    //פונקציה שמראה את כל ההתאומות שנמצאו
    public static void showMatches(List<Match> matches) {
        Control.prefer.showMatches(matches);
    }

    // ריענון התצוגה על מנת לראות את הכפתור האדום של נסיעה האחרונה.
    public static void refreshView() {
        activity.refreshView();
    }
}
