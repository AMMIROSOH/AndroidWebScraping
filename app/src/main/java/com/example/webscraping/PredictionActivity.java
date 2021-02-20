package com.example.webscraping;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EdgeEffect;
import android.widget.EditText;
import android.widget.TextView;

import com.chaquo.python.PyObject;
import com.chaquo.python.Python;
import com.chaquo.python.android.AndroidPlatform;

public class PredictionActivity extends AppCompatActivity {

    EditText num1, num2;
    Button btn;
    TextView resualt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_prediction);

        num1 = (EditText)findViewById(R.id.num1);
        num2 = (EditText)findViewById(R.id.num2);
        btn = (Button)findViewById(R.id.btn);
        resualt = (TextView)findViewById(R.id.resualt);


        if(!Python.isStarted())
            Python.start(new AndroidPlatform(this));

        Python py = Python.getInstance();
        final PyObject pyobj = py.getModule("main");

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PyObject obj = pyobj.callAttr("main", num1.getText().toString(),num2.getText().toString());
                resualt.setText("sum is: "+obj.toString());
            }
        });
    }
}