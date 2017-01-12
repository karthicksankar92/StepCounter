package com.example.karthick.justjava;

import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
//import android.util.Log;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ViewAnimator;
import com.example.karthick.common.logger.SampleActivityBase;

public class bluetoothChat extends SampleActivityBase   {



    public static final String TAG = "MainActivity";
    public String step_Count,distance,calories;
    // Whether the Log Fragment is currently shown
    private boolean mLogShown;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle bundle = getIntent().getExtras();
        if (bundle!= null) {
            step_Count = bundle.getString("MY_STEP_COUNT");
            distance = bundle.getString("MY_DISTANCE");
            calories = bundle.getString("MY_CALORIES");

            Log.v("OUTPUT",String.valueOf(step_Count));
            Log.v("OUTPUT_dis",String.valueOf(distance));

            Log.v("OUTPUT_cal",String.valueOf(calories));

        }


        setContentView(R.layout.activity_bluetooth_chat);

        if (savedInstanceState == null) {
            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
            BluetoothChatFragment fragment = new BluetoothChatFragment();
            transaction.replace(R.id.sample_content_fragment, custom_fragment());
            transaction.commit();
        }
    }
    private BluetoothChatFragment custom_fragment()
        {
        Bundle bundle = new Bundle();
        bundle.putString("STEP_COUNT",step_Count);
        bundle.putString("DISTANCE_TRAVELLED",distance);
        bundle.putString("CALORIES_BURNED",calories);
        BluetoothChatFragment bf= new BluetoothChatFragment();
        bf.setArguments(bundle);
        return bf;

    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
//        getMenuInflater().inflate(R.menu.main, menu);

        return true;
    }



    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {


        return super.onPrepareOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        return super.onOptionsItemSelected(item);
    }

    /** Create a chain of targets that will receive log data */
    @Override
    public void initializeLogging() {
    }
}
