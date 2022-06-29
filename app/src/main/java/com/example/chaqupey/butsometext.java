package com.example.chaqupey;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Base64;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.chaquo.python.PyObject;
import com.chaquo.python.Python;
import com.chaquo.python.android.AndroidPlatform;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

public class butsometext extends AppCompatActivity {
    Button btn,btn2,gd;
    EditText et;
    ImageView iv,iv2;

    BitmapDrawable drawable;
    Bitmap bitmap;
    String imagestring;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_butsometext);

        btn =findViewById(R.id.button_butsometext);
        et =findViewById(R.id.editText_butsometext);
        iv =findViewById(R.id.imageView_butsometext);
        btn2 =findViewById(R.id.button2_butsometext);
        iv2 =findViewById(R.id.imageView2_butsometext);
        gd =findViewById(R.id.button_grt_data_butsometext);

        if (! Python.isStarted()) {
            Python.start(new AndroidPlatform(this));
        }



        ActivityResultLauncher<Intent> activityResult =registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                new ActivityResultCallback<ActivityResult>() {
                    @Override
                    public void onActivityResult(ActivityResult result) {
                        iv.setImageURI(result.getData().getData());
                        Uri uri =result.getData().getData();
                        try {
                            bitmap = MediaStore.Images.Media.getBitmap(getBaseContext().getContentResolver(), uri);
                        }catch (IOException e) {
                            // TODO Handle the exception
                        }
                    }
                }
        );

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                activityResult.launch(intent);
            }
        });

        btn2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                drawable = (BitmapDrawable) iv.getDrawable();
                bitmap=drawable.getBitmap();
                imagestring = getstringimage(bitmap);

                Python py = Python.getInstance();
                PyObject pyobj = py.getModule("py_image_text");

                PyObject obj = pyobj.callAttr("main",imagestring,et.getText().toString());
                String str = obj.toString();
                byte[] data =android.util.Base64.decode(str, Base64.DEFAULT);
                Bitmap bmp = BitmapFactory.decodeByteArray(data,0,data.length);

                iv2.setImageBitmap(bmp);
                Toast.makeText(getBaseContext(),"Done",Toast.LENGTH_LONG).show();
            }
        });

        gd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                drawable = (BitmapDrawable) iv2.getDrawable();
                bitmap=drawable.getBitmap();
                imagestring = getstringimage(bitmap);

                Python py = Python.getInstance();
                PyObject pyobj = py.getModule("py_text_image_2");

                PyObject obj = pyobj.callAttr("main",imagestring);
                String str = obj.toString();

                Toast.makeText(getBaseContext(),str,Toast.LENGTH_LONG).show();
            }
        });

    }
    private String getstringimage(Bitmap bitmap){
        ByteArrayOutputStream baos =new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG,100,baos);
        byte[] imagebyte =baos.toByteArray();
        String encodedImage = android.util.Base64.encodeToString(imagebyte, Base64.DEFAULT);
        return encodedImage;
    }
}