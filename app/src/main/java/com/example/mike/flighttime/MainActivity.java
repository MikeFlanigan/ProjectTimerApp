package com.example.mike.flighttime;

import android.content.SharedPreferences;
import android.support.v4.util.LogWriter;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
// above line should handle next two that are commented out
import android.app.Activity;
import android.os.Handler;
import android.os.SystemClock;
import android.util.Log;
import android.util.LogPrinter;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.security.KeyStore;

import static android.R.attr.cursorVisible;


public class MainActivity extends AppCompatActivity {

//    variable declarations
    private Button T1startButton;
    private Button T1ResetButton;
    private Button T1saveButton;
    private Button T2startButton;
    private Button T2ResetButton;
    private Button T2saveButton;
    private Button T3startButton;
    private Button T3ResetButton;
    private Button T3saveButton;

    private TextView timer1HValue;
    private TextView timer1SValue;
    private TextView timer2HValue;
    private TextView timer2SValue;
    private TextView timer3HValue;
    private TextView timer3SValue;
    private EditText Timer1Name;
    private EditText Timer2Name;
    private EditText Timer3Name;
    String Timer1NameS = "Timer1Name";
    String Timer2NameS = "Timer2Name";
    String Timer3NameS = "Timer3Name";

    private long startTime = 0L;

    private Handler customHandler = new Handler();

    long T1CurrentTime = 0L;
    long T2CurrentTime = 0L;
    long T3CurrentTime = 0L;
    long Timer1StartTime = 0L;
    long Timer2StartTime = 0L;
    long Timer3StartTime = 0L;

    boolean Timing1 = false; // state variable
    boolean Timing2 = false; // state variable
    boolean Timing3 = false; // state variable

    int Timer1Seconds = 0;
    int Timer1SavedSecs = 0;
    double Timer1Hours = 0;
    double Timer1SavedHours = 0;

    int Timer2Seconds = 0;
    int Timer2SavedSecs = 0;
    double Timer2Hours = 0;
    double Timer2SavedHours = 0;

    int Timer3Seconds = 0;
    int Timer3SavedSecs = 0;
    double Timer3Hours = 0;
    double Timer3SavedHours = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        timer1HValue = (TextView) findViewById(R.id.timer1HValue);
        timer1SValue = (TextView) findViewById(R.id.timer1SValue);
        Timer1Name = (EditText) findViewById(R.id.Timer1Name);
        T1startButton = (Button) findViewById(R.id.T1startButton);
        T1ResetButton = (Button) findViewById(R.id.T1ResetButton);
        T1saveButton = (Button) findViewById(R.id.T1saveButton);
        final EditText TextAction1 = (EditText) findViewById(R.id.Timer1Name);

        timer2HValue = (TextView) findViewById(R.id.timer2HValue);
        timer2SValue = (TextView) findViewById(R.id.timer2SValue);
        Timer2Name = (EditText) findViewById(R.id.Timer2Name);
        T2startButton = (Button) findViewById(R.id.T2startButton);
        T2ResetButton = (Button) findViewById(R.id.T2ResetButton);
        T2saveButton = (Button) findViewById(R.id.T2saveButton);
        final EditText TextAction2 = (EditText) findViewById(R.id.Timer2Name);

        timer3HValue = (TextView) findViewById(R.id.timer3HValue);
        timer3SValue = (TextView) findViewById(R.id.timer3SValue);
        Timer3Name = (EditText) findViewById(R.id.Timer3Name);
        T3startButton = (Button) findViewById(R.id.T3startButton);
        T3ResetButton = (Button) findViewById(R.id.T3ResetButton);
        T3saveButton = (Button) findViewById(R.id.T3saveButton);
        final EditText TextAction3 = (EditText) findViewById(R.id.Timer3Name);

        SharedPreferences prefs1 = getSharedPreferences("FlightTimePreferencesT1", MODE_PRIVATE);
        Timer1SavedSecs = prefs1.getInt("T1seconds", 0); // 0 is the default value
        Timer1SavedHours = prefs1.getFloat("T1hours", 0);
        Timer1NameS = prefs1.getString("Timer1Name",null);
        Timer1Name.setText(Timer1NameS);
        Timer1Seconds = Timer1SavedSecs;
        Timer1Hours = Timer1SavedHours;
        timer1SValue.setText(String.format("%02d",Timer1Seconds));
        timer1HValue.setText(String.format("%.2f", Timer1Hours));

        SharedPreferences prefs2 = getSharedPreferences("FlightTimePreferencesT2", MODE_PRIVATE);
        Timer2SavedSecs = prefs2.getInt("T2seconds", 0); // 0 is the default value
        Timer2SavedHours = prefs2.getFloat("T2hours", 0);
        Timer2NameS = prefs2.getString("Timer2Name",null);
        Timer2Name.setText(Timer2NameS);
        Timer2Seconds = Timer2SavedSecs;
        Timer2Hours = Timer2SavedHours;
        timer2SValue.setText(String.format("%02d",Timer2Seconds));
        timer2HValue.setText(String.format("%.2f", Timer2Hours));

        SharedPreferences prefs3 = getSharedPreferences("FlightTimePreferencesT3", MODE_PRIVATE);
        Timer3SavedSecs = prefs3.getInt("T3seconds", 0); // 0 is the default value
        Timer3SavedHours = prefs3.getFloat("T3hours", 0);
        Timer3NameS = prefs3.getString("Timer3Name",null);
        Timer3Name.setText(Timer3NameS);
        Timer3Seconds = Timer3SavedSecs;
        Timer3Hours = Timer3SavedHours;
        timer3SValue.setText(String.format("%02d",Timer3Seconds));
        timer3HValue.setText(String.format("%.2f", Timer3Hours));


        TextAction1.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                Timer1NameS = TextAction1.getText().toString();
                return false;
            }
        });
//        TextAction1.onEditorAction(TextView v,);
        TextAction2.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                Timer2NameS = TextAction2.getText().toString();
                return false;
            }
        });
        TextAction3.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                Timer3NameS = TextAction3.getText().toString();
                return false;
            }
        });
        T1startButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (!Timing1) { // start timing
                    Timer1StartTime = SystemClock.elapsedRealtime();
                    customHandler.postDelayed(updateTimerThread, 0);
                    Timing1 = true;
                    T1startButton.setText("Stop");
                }
                else { // stop timing
                    Timer1SavedSecs = Timer1Seconds;
                    Timer1SavedHours = Timer1Hours;
                    Timer1Seconds = 0;
                    Timer1Hours = 0;
                    Timing1 = false;
                    T1startButton.setText("Start");
                }
            }
        });
        T2startButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (!Timing2) { // start timing
                    Timer2StartTime = SystemClock.elapsedRealtime();
                    customHandler.postDelayed(updateTimerThread, 0);
                    Timing2 = true;
                    T2startButton.setText("Stop");
                }
                else { // stop timing
                    Timer2SavedSecs = Timer2Seconds;
                    Timer2SavedHours = Timer2Hours;
                    Timer2Seconds = 0;
                    Timer2Hours = 0;
                    Timing2 = false;
                    T2startButton.setText("Start");
                }
            }
        });
        T3startButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (!Timing3) { // start timing
                    Timer3StartTime = SystemClock.elapsedRealtime();
                    customHandler.postDelayed(updateTimerThread, 0);
                    Timing3 = true;
                    T3startButton.setText("Stop");
                }
                else { // stop timing
                    Timer3SavedSecs = Timer3Seconds;
                    Timer3SavedHours = Timer3Hours;
                    Timer3Seconds = 0;
                    Timer3Hours = 0;
                    Timing3 = false;
                    T3startButton.setText("Start");
                }
            }
        });



        T1ResetButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                if (!Timing1) {
                    Timer1SavedSecs = 0;
                    Timer1SavedHours = 0;
                    Timer1Seconds = 0;
                    Timer1Hours = 0;
                    Timer1StartTime = SystemClock.elapsedRealtime();
                    T1CurrentTime = SystemClock.elapsedRealtime();


                    SharedPreferences.Editor editorT1 = getSharedPreferences("FlightTimePreferencesT1", MODE_PRIVATE).edit();
                    editorT1.putString("Timer1Name",Timer1NameS);
                    editorT1.putInt("T1seconds", Timer1Seconds);
                    editorT1.putFloat("T1hours",(float) Timer1Hours);
                    editorT1.commit();
                }
                else {
                    // do nothing
                }
                customHandler.postDelayed(updateTimerThread, 0);
            }
        });
        T2ResetButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                if (!Timing2) {
                    Timer2SavedSecs = 0;
                    Timer2SavedHours = 0;
                    Timer2Seconds = 0;
                    Timer2Hours = 0;
                    Timer2StartTime = SystemClock.elapsedRealtime();
                    T2CurrentTime = SystemClock.elapsedRealtime();


                    SharedPreferences.Editor editorT2 = getSharedPreferences("FlightTimePreferencesT2", MODE_PRIVATE).edit();
                    editorT2.putString("Timer2Name",Timer2NameS);
                    editorT2.putInt("T2seconds", Timer2Seconds);
                    editorT2.putFloat("T2hours",(float) Timer2Hours);
                    editorT2.commit();
                }
                else {
                    // do nothing
                }
                customHandler.postDelayed(updateTimerThread, 0);
            }
        });
        T3ResetButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                if (!Timing3) {
                    Timer3SavedSecs = 0;
                    Timer3SavedHours = 0;
                    Timer3Seconds = 0;
                    Timer3Hours = 0;
                    Timer3StartTime = SystemClock.elapsedRealtime();
                    T3CurrentTime = SystemClock.elapsedRealtime();


                    SharedPreferences.Editor editorT3 = getSharedPreferences("FlightTimePreferencesT3", MODE_PRIVATE).edit();
                    editorT3.putString("Timer3Name",Timer3NameS);
                    editorT3.putInt("T3seconds", Timer3Seconds);
                    editorT3.putFloat("T3hours",(float) Timer3Hours);
                    editorT3.commit();
                }
                else {
                    // do nothing
                }
                customHandler.postDelayed(updateTimerThread, 0);
            }
        });


        T1saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SharedPreferences.Editor editorT1 = getSharedPreferences("FlightTimePreferencesT1", MODE_PRIVATE).edit();
                editorT1.putString("Timer1Name",Timer1NameS);
                if (Timing1){
                    editorT1.putInt("T1seconds", Timer1Seconds);
                    editorT1.putFloat("T1hours",(float) Timer1Hours);
                }
                else {
                    editorT1.putInt("T1seconds", Timer1SavedSecs);
                    editorT1.putFloat("T1hours",(float) Timer1SavedHours);
                }
                editorT1.commit();
            }
        });
        T2saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SharedPreferences.Editor editorT2 = getSharedPreferences("FlightTimePreferencesT2", MODE_PRIVATE).edit();
                editorT2.putString("Timer2Name",Timer2NameS);
        if (Timing2){
            editorT2.putInt("T2seconds", Timer2Seconds);
            editorT2.putFloat("T2hours",(float) Timer2Hours);
        }
        else {
            editorT2.putInt("T2seconds", Timer2SavedSecs);
            editorT2.putFloat("T2hours",(float) Timer2SavedHours);
        }
                editorT2.commit();
            }
        });
        T3saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SharedPreferences.Editor editorT3 = getSharedPreferences("FlightTimePreferencesT3", MODE_PRIVATE).edit();
                editorT3.putString("Timer3Name",Timer3NameS);
        if (Timing3){
            editorT3.putInt("T3seconds", Timer3Seconds);
            editorT3.putFloat("T3hours",(float) Timer3Hours);
        }
        else {
            editorT3.putInt("T3seconds", Timer3SavedSecs);
            editorT3.putFloat("T3hours",(float) Timer3SavedHours);
        }
                editorT3.commit();
            }
        });
    }


    private Runnable updateTimerThread = new Runnable() {

        public void run() {

            if (Timing1){ // update current time
                T1CurrentTime = SystemClock.elapsedRealtime();
                Timer1Seconds = (int) ((T1CurrentTime - Timer1StartTime)/1000 + Timer1SavedSecs)% 60;
                if (T1CurrentTime - Timer1StartTime >= 0) {
                    Timer1Hours = (double) ((T1CurrentTime - Timer1StartTime)/1000)/3600 + Timer1SavedHours;
                }
                else {
                    Timer1Hours = (double) ((Long.MAX_VALUE - T1CurrentTime + Timer1StartTime)/1000)/3600 + Timer1SavedHours;
                }
                timer1SValue.setText(String.format("%02d",Timer1Seconds));
                timer1HValue.setText(String.format("%.2f", Timer1Hours));
            }
            else {
                timer1SValue.setText(String.format("%02d",Timer1SavedSecs));
                timer1HValue.setText(String.format("%.2f", Timer1SavedHours));
            }

            if (Timing2){ // update current time
                T2CurrentTime = SystemClock.elapsedRealtime();
                Timer2Seconds = (int) ((T2CurrentTime - Timer2StartTime)/1000 + Timer2SavedSecs)% 60;
                if (T2CurrentTime - Timer2StartTime >= 0) {
                    Timer2Hours = (double) ((T2CurrentTime - Timer2StartTime)/1000)/3600 + Timer2SavedHours;
                }
                else {
                    Timer2Hours = (double) ((Long.MAX_VALUE - T2CurrentTime + Timer2StartTime)/1000)/3600 + Timer2SavedHours;
                }
                timer2SValue.setText(String.format("%02d",Timer2Seconds));
                timer2HValue.setText(String.format("%.2f", Timer2Hours));
            }
            else {
                timer2SValue.setText(String.format("%02d",Timer2SavedSecs));
                timer2HValue.setText(String.format("%.2f", Timer2SavedHours));
            }

            if (Timing3){ // update current time
                T3CurrentTime = SystemClock.elapsedRealtime();
                Timer3Seconds = (int) ((T3CurrentTime - Timer3StartTime)/1000 + Timer3SavedSecs)% 60;
                if (T3CurrentTime - Timer3StartTime >= 0) {
                    Timer3Hours = (double) ((T3CurrentTime - Timer3StartTime)/1000)/3600 + Timer3SavedHours;
                }
                else {
                    Timer3Hours = (double) ((Long.MAX_VALUE - T3CurrentTime + Timer3StartTime)/1000)/3600 + Timer3SavedHours;
                }
                timer3SValue.setText(String.format("%02d",Timer3Seconds));
                timer3HValue.setText(String.format("%.2f", Timer3Hours));
            }
            else {
                timer3SValue.setText(String.format("%02d",Timer3SavedSecs));
                timer3HValue.setText(String.format("%.2f", Timer3SavedHours));
            }

            customHandler.postDelayed(this, 0);
        }

    };
}
