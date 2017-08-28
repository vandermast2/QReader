package com.samapps.qreader;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel;
import com.samapps.qreader.color.ColorDialog;
import com.samapps.qreader.qr.OpenFileDialog;
import com.samapps.qreader.qr.QRHelper;

import java.util.HashMap;
import java.util.Map;

import static android.graphics.Color.WHITE;
import static com.samapps.qreader.qr.Constants.HEIGHT;
import static com.samapps.qreader.qr.Constants.LOGO_HEIGHT;
import static com.samapps.qreader.qr.Constants.LOGO_WIDTH;
import static com.samapps.qreader.qr.Constants.REQUEST_PERMISSIONS;
import static com.samapps.qreader.qr.Constants.WIDTH;

public class MainActivity extends AppCompatActivity {
    private EditText resultScan;
    private EditText codeGen;
    private EditText etColor;
    private EditText etColorBG;
    public static int qrCodeColor = Color.BLACK;
    public static int qrBackgroundColor = WHITE;
    public static String path;
    //        String charset = "UTF-8";
    private String charset = "ISO-8859-1";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getSupportActionBar().hide();
        setContentView(R.layout.activity_main);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);


        int smallestDimension = WIDTH < HEIGHT ? WIDTH : HEIGHT;
        Map<EncodeHintType, ErrorCorrectionLevel> hintMap = new HashMap<EncodeHintType, ErrorCorrectionLevel>();
        hintMap.put(EncodeHintType.ERROR_CORRECTION, ErrorCorrectionLevel.H);
        resultScan = findViewById(R.id.etResult);
        etColor = findViewById(R.id.etColor);
        etColorBG = findViewById(R.id.etColorBG);
        codeGen = findViewById(R.id.etGenerate);
        Button btnChangeColor = findViewById(R.id.btnChangeColor);
        Button logo = findViewById(R.id.btnLogo);
        Button btnScan = findViewById(R.id.btnScan);
        Button btnGen = findViewById(R.id.button2);

        logo.setOnClickListener(view -> new OpenFileDialog(this).show());

        btnChangeColor.setOnClickListener(view -> setColor());

        btnScan.setOnClickListener(view -> new QRHelper(this).initScan());

        btnGen.setOnClickListener(view -> new QRHelper(this).generateQRCode(codeGen.getText().toString(), charset, hintMap, smallestDimension, smallestDimension));

        checkPermission();
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String permissions[], @NonNull int[] grantResults) {
        if (requestCode == REQUEST_PERMISSIONS) {

        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
        if (result != null) {
            if (result.getContents() != null) {
                resultScan.setText(result.getContents());
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    private void checkPermission() {
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED && ContextCompat.checkSelfPermission(this,
                Manifest.permission.READ_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {//Can add more as per requirement

            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE},
                    REQUEST_PERMISSIONS);

        } else {

        }
    }

    private void setColor() {
        ColorDialog dialog = new ColorDialog(MainActivity.this, 255, 125, 125, 125);
        dialog.show();
        dialog.setCallback(this::setColorQR);
    }

    private void setColorQR(int color) {
        RadioGroup radioGroup = findViewById(R.id.RG);
        switch (radioGroup.getCheckedRadioButtonId()) {
            case R.id.rbtnQRColor:
                qrCodeColor = Color.argb(Color.alpha(color), Color.red(color), Color.green(color), Color.blue(color));
                etColor.setText(String.format("#%08X", (color)));
                break;
            case R.id.rbtnBackgroundColor:
                qrBackgroundColor = Color.argb(Color.alpha(color), Color.red(color), Color.green(color), Color.blue(color));
                etColorBG.setText(String.format("#%08X", (color)));
                break;
        }

    }
}