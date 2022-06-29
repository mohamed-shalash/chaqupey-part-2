package com.example.chaqupey;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.chaquo.python.PyObject;
import com.chaquo.python.Python;
import com.chaquo.python.android.AndroidPlatform;

public class MainActivity extends AppCompatActivity {
    EditText name,pass;
    Button btn;
    TextView tv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        name= findViewById(R.id.main_name);
        pass= findViewById(R.id.main_pass);
        btn= findViewById(R.id.main_button);
        tv= findViewById(R.id.main_textView);

        if (! Python.isStarted()) {
            Python.start(new AndroidPlatform(this));
        }

        Python py = Python.getInstance();
        PyObject pyobj = py.getModule("script");

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PyObject obj = pyobj.callAttr("main",name.getText().toString(),pass.getText().toString());
                tv.setText(obj.toString());
            }
        });
    }
}