package com.samapps.qreader.qr;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.util.Log;
import android.widget.Toast;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.integration.android.IntentIntegrator;
import com.samapps.qreader.GeneratorActivity;

import java.util.Map;

import static com.samapps.qreader.MainActivity.path;
import static com.samapps.qreader.MainActivity.qrBackgroundColor;
import static com.samapps.qreader.MainActivity.qrCodeColor;
import static com.samapps.qreader.qr.Constants.LOGO_HEIGHT;
import static com.samapps.qreader.qr.Constants.LOGO_WIDTH;

/**
 * Created by breez_000 on 8/28/2017.
 */

public class QRHelper {
    private Context context;

    public QRHelper(Context context) {
        this.context = context;

    }

    public void initScan() {
        IntentIntegrator integrator = new IntentIntegrator((Activity) context);
        integrator.setDesiredBarcodeFormats(IntentIntegrator.ALL_CODE_TYPES);
        integrator.setPrompt("Scanner");
        integrator.setCameraId(0);
        integrator.setOrientationLocked(false);
        integrator.setBeepEnabled(false);
        integrator.setBarcodeImageEnabled(false);
        integrator.initiateScan();
    }

    public void generateQRCode(String qrCodeData, String charset, Map hintMap, int qrCodeheight, int qrCodewidth) {
        try {
            BitMatrix matrix = new MultiFormatWriter().encode(new String(qrCodeData.getBytes(charset), charset),
                    BarcodeFormat.QR_CODE, qrCodewidth, qrCodeheight, hintMap);
            int width = matrix.getWidth();
            int height = matrix.getHeight();
            int[] pixels = new int[width * height];
            for (int y = 0; y < height; y++) {
                int offset = y * width;
                for (int x = 0; x < width; x++) {
                    pixels[offset + x] = matrix.get(x, y) ? qrCodeColor : qrBackgroundColor;
                }
            }
            Bitmap bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
            bitmap.setPixels(pixels, 0, width, 0, 0, width, height);
            Bitmap overlay = getLogo(path);
            if (overlay != null) {
                context.startActivity(new Intent(context, GeneratorActivity.class).putExtra("qr", mergeBitmaps(overlay, bitmap)));
            } else {
                context.startActivity(new Intent(context, GeneratorActivity.class).putExtra("qr", bitmap));
            }

        } catch (Exception er) {
            Log.e("QrGenerate", er.getMessage());
            Toast.makeText(context, "Field DATA is empty!!!", Toast.LENGTH_SHORT).show();
        }
    }

    private Bitmap mergeBitmaps(Bitmap overlay, Bitmap bitmap) {
        overlay = Bitmap.createScaledBitmap(overlay, LOGO_WIDTH, LOGO_HEIGHT, false);
        int height = bitmap.getHeight();
        int width = bitmap.getWidth();

        Bitmap combined = Bitmap.createBitmap(width, height, bitmap.getConfig());
        Canvas canvas = new Canvas(combined);
        int canvasWidth = canvas.getWidth();
        int canvasHeight = canvas.getHeight();

        canvas.drawBitmap(bitmap, new Matrix(), null);

        int centreX = (canvasWidth - overlay.getWidth()) / 2;
        int centreY = (canvasHeight - overlay.getHeight()) / 2;
        canvas.drawBitmap(overlay, centreX, centreY, null);

        return combined;
    }



    private Bitmap getLogo(String path) {
        return BitmapFactory.decodeFile(path);
    }


}
