package com.samapps.qreader.color;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.text.InputFilter;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.SeekBar;
import android.widget.TextView;

import com.samapps.qreader.R;

import static com.samapps.qreader.color.ColorHelper.formatColorValues;

/**
 * Created by breez_000 on 8/20/2017.
 */

public class ColorDialog extends Dialog implements SeekBar.OnSeekBarChangeListener {
    private final Activity activity;

    private SeekBar alphaSeekBar, redSeekBar, greenSeekBar, blueSeekBar;
    private EditText hexCode;
    private int alpha, red, green, blue;
    private ColorCalback callback;
    private View colorView;

    public ColorDialog(Activity activity, int alpha, int red, int green, int blue) {
        super(activity);
        this.activity = activity;
        this.alpha = alpha;
        this.red = red;
        this.green = green;
        this.blue = blue;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.color_container_dialog);

        colorView = findViewById(R.id.viewColor);
        colorView.setBackgroundColor(getColor());

        hexCode = findViewById(R.id.colorCode);

        alphaSeekBar = findViewById(R.id.alphaSeekBar);
        redSeekBar = findViewById(R.id.redSeekBar);
        greenSeekBar = findViewById(R.id.greenSeekBar);
        blueSeekBar = findViewById(R.id.blueSeekBar);

        alphaSeekBar.setOnSeekBarChangeListener(this);
        redSeekBar.setOnSeekBarChangeListener(this);
        greenSeekBar.setOnSeekBarChangeListener(this);
        blueSeekBar.setOnSeekBarChangeListener(this);

        alphaSeekBar.setProgress(alpha);
        redSeekBar.setProgress(red);
        greenSeekBar.setProgress(green);
        blueSeekBar.setProgress(blue);

        hexCode.setFilters(new InputFilter[]{new InputFilter.LengthFilter(8)});

        hexCode.setText(formatColorValues(alpha, red, green, blue));

        hexCode.setOnEditorActionListener(
                (v, actionId, event) -> {
                    if (actionId == EditorInfo.IME_ACTION_SEARCH ||
                            actionId == EditorInfo.IME_ACTION_DONE ||
                            event.getAction() == KeyEvent.ACTION_DOWN &&
                                    event.getKeyCode() == KeyEvent.KEYCODE_ENTER) {
                        updateColor(v.getText().toString());
                        InputMethodManager imm = (InputMethodManager) activity
                                .getSystemService(Context.INPUT_METHOD_SERVICE);
                        assert imm != null;
                        imm.hideSoftInputFromWindow(hexCode.getWindowToken(), 0);

                        return true;
                    }
                    return false;
                });

        final Button okColor = findViewById(R.id.apply);
        okColor.setOnClickListener(view -> sendColor());
    }

    public void setCallback(ColorCalback listener) {
        callback = listener;
    }

    private void sendColor() {
        if (callback != null)
            callback.chooseColor(getColor());
        this.dismiss();
    }

    private void updateColor(String input) {
        try {
            final int color = Color.parseColor('#' + input);
            alpha = Color.alpha(color);
            red = Color.red(color);
            green = Color.green(color);
            blue = Color.blue(color);

            colorView.setBackgroundColor(getColor());

            alphaSeekBar.setProgress(alpha);
            redSeekBar.setProgress(red);
            greenSeekBar.setProgress(green);
            blueSeekBar.setProgress(blue);
        } catch (IllegalArgumentException ignored) {
            hexCode.setError("Error!!!");
        }
    }

    @Override
    public void onProgressChanged(SeekBar seekBar, int i, boolean b) {

        if (seekBar.getId() == R.id.alphaSeekBar) {
            alpha = i;
        } else if (seekBar.getId() == R.id.redSeekBar) {
            red = i;
        } else if (seekBar.getId() == R.id.greenSeekBar) {
            green = i;
        } else if (seekBar.getId() == R.id.blueSeekBar) {
            blue = i;
        }

        colorView.setBackgroundColor(getColor());
        hexCode.setText(formatColorValues(alpha, red, green, blue));
    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {

    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {

    }

    public int getColor() {
        return Color.argb(alpha, red, green, blue);
    }
}
