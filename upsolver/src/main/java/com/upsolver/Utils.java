package com.upsolver;

import java.text.NumberFormat;
import java.text.ParseException;

public class Utils {
    public static Number checkNumber(final String value) {
        // null or empty
        if (value == null || value.length() == 0) {
            return null;
        }

        if(value.matches("[-+]?[0-9]*\\.?[0-9]+")) {
            try {
                return NumberFormat.getInstance().parse(value);
            } catch (ParseException e) {
                return null;
            }
        } else {
            return null;
        }
    }
}
