package com.example.madhurarora.treebo.Util;

import android.widget.EditText;

/**
 * Created by madhur.arora on 21/07/16.
 */
public class ViewUtils {

    public static boolean isEmpty(EditText myEditText) {
        return myEditText != null && myEditText.getText().toString().trim().length() == 0;
    }
}
