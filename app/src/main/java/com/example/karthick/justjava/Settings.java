package com.example.karthick.justjava;

import android.app.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class Settings extends Activity {
    EditText height,weight;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

            height=(EditText) findViewById(R.id.heightValues);
            weight=(EditText) findViewById(R.id.WeightValues);


        Button done=(Button) findViewById(R.id.doneBtn);
//
        done.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v)
            {   System.out.println(Integer.parseInt(height.getText().toString()));
                Log.v("HEIGHTVALUE123",height.getText().toString());
//
            Intent output = new Intent();
            output.putExtra("HEIGHT",Integer.parseInt(height.getText().toString()));
            output.putExtra("WEIGHT",Integer.parseInt(weight.getText().toString()));
            setResult(Activity.RESULT_OK, output);
             finish();
            }
        });
//
    }


}
