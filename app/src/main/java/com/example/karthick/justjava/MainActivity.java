package com.example.karthick.justjava;

import android.app.ActionBar;
import android.content.SharedPreferences;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.content.Intent;
import android.support.v7.widget.ShareActionProvider;
import android.os.Vibrator;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.text.DecimalFormat;
import java.text.NumberFormat;

public class MainActivity extends AppCompatActivity implements SensorEventListener {


    private String MY_PREFS_NAME = null;
    private SensorManager sensorManager;

    private Sensor accelerometer;
    private TextView count,distance1,calories;
    boolean activityRunning;
    private Button buttonReset;

    private float vibrateThreshold = 0;
    public Vibrator v;
    private int stepsInSensor = 0;
    private int stepsAtReset;
    int stepsSinceReset;

    static double weight = 67.0; // kg

    static double height = 178.0; // cm

//    static double stepsCount = 4793;

    final static double walkingFactor = 0.57;

    static double CaloriesBurnedPerMile;

    static double strip;

    static double stepCountMile; // step/mile

    static double conversationFactor;

    static double CaloriesBurned;

    static NumberFormat formatter = new DecimalFormat("#0.00");

    static double distance;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        count = (TextView) findViewById(R.id.count);
        distance1 = (TextView) findViewById(R.id.activeDistanceValues);
        calories = (TextView) findViewById(R.id.caloriesValues);

        buttonReset=(Button)findViewById(R.id.buttonReset);

        sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);

        SharedPreferences prefs = getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE);
        stepsAtReset = prefs.getInt("stepsAtReset", 0);

    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate menu resource file.
        getMenuInflater().inflate(R.menu.share_menu, menu);

        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_item_share: {

                Intent intern = new Intent(getApplicationContext(), bluetoothChat.class);
                intern.putExtra("MY_STEP_COUNT", String.valueOf(stepsSinceReset));
                intern.putExtra("MY_DISTANCE",String.valueOf(formatter.format(distance)));
                intern.putExtra("MY_CALORIES",String.valueOf(formatter.format(CaloriesBurned)));
                startActivityForResult(intern, 0);
                break;
            }
            case R.id.share_options: {
                // Launch the DeviceListActivity to see devices and do scan
                ShareActionProvider mshare = (ShareActionProvider) MenuItemCompat.getActionProvider(item);
                Intent shareIntent= new Intent(Intent.ACTION_SEND);
                shareIntent.setAction(Intent.ACTION_SEND);
                shareIntent.setType("text/plain");
                shareIntent.putExtra(Intent.EXTRA_TEXT, "Steps Today" + stepsAtReset);
                startActivity(Intent.createChooser(shareIntent, "Share via"));

                if (mshare != null) {

                    mshare.setShareIntent(Intent.createChooser(shareIntent,"share via"));
                }
                // Return true to display menu
                return true;

            }
            case R.id.settings: {

                Intent intern = new Intent(getApplicationContext(), Settings.class);
                startActivityForResult(intern,42);

                break;
            }

        }
        return false;
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode,resultCode,data);
        switch (requestCode) {
            case 42: {

               height=data.getIntExtra("HEIGHT",0);
               weight=data.getIntExtra("WEIGHT",0);

//              Log.v("HEIGHTVALUE", String.valueOf(height));
//                Log.v("WEIGHTVALUE", String.valueOf(weight));

//

                break;
            }
        }
    }
    @Override
    protected void onResume() {
        super.onResume();
        activityRunning = true;
        Sensor countSensor = sensorManager.getDefaultSensor(Sensor.TYPE_STEP_COUNTER);
        if (countSensor != null) {
            sensorManager.registerListener(this, countSensor, SensorManager.SENSOR_DELAY_UI);
            accelerometer = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
            vibrateThreshold = accelerometer.getMaximumRange() / 2;
        } else {
            Toast.makeText(this, "Count sensor not available!", Toast.LENGTH_LONG).show();
        }
        v = (Vibrator) this.getSystemService(Context.VIBRATOR_SERVICE);

    }

    @Override
    protected void onPause() {
        super.onPause();
        activityRunning = false;
        // if you unregister the last listener, the hardware will stop detecting step events
//        sensorManager.unregisterListener(this);
    }

    @Override
    public void onSensorChanged(SensorEvent event) {

        if (activityRunning) {
            stepsInSensor = Integer.valueOf((int) event.values[0]);
             stepsSinceReset = stepsInSensor - stepsAtReset;
            count.setText(String.valueOf(stepsSinceReset));
            v.vibrate(50);
            CalculateDistanceCalories();
        }
        else
        {
            event.values[0] = 0;
        }

    }
      public void CalculateDistanceCalories()
      {
          CaloriesBurnedPerMile = walkingFactor * (weight * 2.2);

          strip = height * 0.415;

          stepCountMile = 160934.4 / strip;

          conversationFactor = CaloriesBurnedPerMile / stepCountMile;

          CaloriesBurned = stepsSinceReset * conversationFactor;
            calories.setText(formatter.format(CaloriesBurned));
//          System.out.println("Calories burned: "
//                  + formatter.format(CaloriesBurned) + " cal");

          distance = (stepsSinceReset * strip) / 100000;
            distance1.setText(formatter.format(distance));
//          System.out.println("Distance: " + formatter.format(distance)
//                  + " km");
      }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
    }
    public void resetSteps(View V)
    {
        stepsAtReset = stepsInSensor;

        SharedPreferences.Editor editor =
                getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE).edit();
        editor.putInt("stepsAtReset", stepsAtReset);
        editor.commit();
        distance=0;
        CaloriesBurned=0;

        // you can now display 0:
        count.setText(String.valueOf(0));
        calories.setText(String.valueOf(0));
        distance1.setText(String.valueOf(0));

    }
}


