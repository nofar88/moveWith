package com.example.movewith.Control;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;

import com.example.movewith.Control.Control;
import com.example.movewith.Model.Match;
import com.example.movewith.View.Prefer;

import java.util.List;

public class MatchCalculator extends AsyncTask<List<Match>, Void, List<Match>> {
    // פותח תהליכון חדש ומריץ קריאות לאינטרנט בנפרד ומונע קריסה
    @SafeVarargs
    @Override
    protected final List<Match> doInBackground(List<Match>... params) {
        List<Match> matches = params[0];
        double maxSrc = 0, maxDest = 0;
        double maxTime = 0, maxAge = 0;
        for (Match match : matches) {
            match.calculate();
            if (match.getSrcDist() > maxSrc)
                maxSrc = match.getSrcDist();
            if (match.getDestDist() > maxDest)
                maxDest = match.getDestDist();
            if (match.getAgeDist() > maxAge)
                maxAge = match.getAgeDist();
            if (match.getTimeDist() > maxTime)
                maxTime = match.getTimeDist();
        }
// נירמול הנתונים על מנת לראות מי יצא בעדיפות הכי גבוה
        for (Match match : matches) {
            match.setDestDist(maxDest == 0 ? 0 : match.getDestDist() / maxDest);
            match.setSrcDist(maxSrc == 0 ? 0 : match.getSrcDist() / maxSrc);
            match.setTimeDist(maxTime == 0 ? 0 : match.getTimeDist() / maxTime);
            match.setAgeDist(maxAge == 0 ? 0 : match.getAgeDist() / maxAge);
        }

        matches.sort((o1, o2) -> Double.compare(o1.getGrade(), o2.getGrade()));// מיון הציון הכי נמוך להכי גבוהה-כך שההתאמה הטובה ביותר תופיעה ראשונה
        return matches;
    }

    //חזרה לתהליכון הראשי על מנת שנראה את תוצאות ההתאמה
    @Override
    protected void onPostExecute(List<Match> matches) {
        super.onPostExecute(matches);
        Control.showMatches(matches);
    }
}
