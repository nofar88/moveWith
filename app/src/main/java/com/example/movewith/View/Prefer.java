package com.example.movewith.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;

import com.example.movewith.Control.Control;
import com.example.movewith.Model.Driver;
import com.example.movewith.Model.Hitchhiker;
import com.example.movewith.Model.Match;
import com.example.movewith.R;

import java.util.ArrayList;
import java.util.List;

public class Prefer extends AppCompatActivity {
    RecyclerView matchesView;// ממחזר את התצוגה של התוצאות
    LinearLayout loading;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_prefer);
        getSupportActionBar().setTitle("ההתאמות שנמצאו עבורך...");

        Hitchhiker hitchhiker = (Hitchhiker) getIntent().getSerializableExtra("hitchhiker");
        Control.startMatch(hitchhiker, this);

        this.matchesView = findViewById(R.id.all_match);
        this.loading = findViewById(R.id.loading);

        this.matchesView.setVisibility(View.GONE);
        this.loading.setVisibility(View.VISIBLE);
    }

    public void showMatches(List<Match> matches) {
        List<Driver> drivers = new ArrayList<>();
        for (Match match:  matches) {
            drivers.add(match.getDriver());
        }

        this.matchesView.setLayoutManager(new LinearLayoutManager(this));
        MatchAdapter adapter = new MatchAdapter(this, drivers);
        this.matchesView.setAdapter(adapter);

        this.matchesView.setVisibility(View.VISIBLE);
        this.loading.setVisibility(View.GONE);
    }
}