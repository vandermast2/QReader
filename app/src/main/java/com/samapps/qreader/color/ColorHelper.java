package com.samapps.qreader.color;

import android.support.annotation.IntRange;

/**
 * Created by breez_000 on 8/20/2017.
 */

public final class ColorHelper {
    private static int assertColorInRange(@IntRange(from = 0, to = 255) int colorValue) {
        return ((0 <= colorValue) && (colorValue <= 255)) ? colorValue : 0;
    }

    static String formatColorValues(
            @IntRange(from = 0, to = 255) int alpha,
            @IntRange(from = 0, to = 255) int red,
            @IntRange(from = 0, to = 255) int green,
            @IntRange(from = 0, to = 255) int blue) {

        return String.format("%02X%02X%02X%02X",
                assertColorInRange(alpha),
                assertColorInRange(red),
                assertColorInRange(green),
                assertColorInRange(blue)
        );
    }
}
