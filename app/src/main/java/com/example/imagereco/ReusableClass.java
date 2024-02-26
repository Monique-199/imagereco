package com.example.imagereco;

import android.content.Context;
import android.widget.Toast;

public class ReusableClass {
    public static void showToast(Context context, String message){
        Toast.makeText(context,message, Toast.LENGTH_SHORT).show();
    }
}
