package com.example.chaqupey;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.util.Base64;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.chaquo.python.PyObject;
import com.chaquo.python.Python;
import com.chaquo.python.android.AndroidPlatform;

import java.io.ByteArrayOutputStream;

public class thresh_Hold_chaqupey extends AppCompatActivity {
    Button btn;
    ImageView iv1,iv2;

    BitmapDrawable drawable;
    Bitmap bitmap;
    String imagestring;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_thresh_hold_chaqupey);

        btn =findViewById(R.id.thresh_button);
        iv1 =findViewById(R.id.thresh_imageView_1);
        iv2 =findViewById(R.id.thresh_imageView_2);

        if (! Python.isStarted()) {
            Python.start(new AndroidPlatform(this));
        }


        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                drawable = (BitmapDrawable) iv1.getDrawable();
                bitmap=drawable.getBitmap();
                imagestring = getstringimage(bitmap);

                Python py = Python.getInstance();
                PyObject pyobj = py.getModule("thri_ho_script");

                PyObject obj = pyobj.callAttr("main",imagestring);
                String str = obj.toString();
                byte[] data =android.util.Base64.decode(str, Base64.DEFAULT);
                Bitmap bmp = BitmapFactory.decodeByteArray(data,0,data.length);

                iv2.setImageBitmap(bmp);
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