package com.samapps.qreader;

import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;

import static com.samapps.qreader.MainActivity.qrBackgroundColor;

public class GeneratorActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_generator);
        ConstraintLayout layout = findViewById(R.id.backLayout);
        layout.setBackgroundColor(qrBackgroundColor);
        ImageView imageView = findViewById(R.id.imgQRCode);
        imageView.setImageBitmap(getIntent().getParcelableExtra("qr"));

    }
}
