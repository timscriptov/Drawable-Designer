package com.mcal.drawabledesigner;

import android.annotation.SuppressLint;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;

import androidx.annotation.LayoutRes;
import androidx.appcompat.app.AlertDialog.Builder;

public class AppUtils {
    @SuppressLint("SetTextI18n")
    public static void showAbout(Context context, @LayoutRes int resId) {
        View view = LayoutInflater.from(context).inflate(resId, null);
        //((TextView) view.findViewById(R.id.version)).setText("Version 1.2.9");
        new Builder(context).setView(view).create().show();
    }

    /*public static void showStorePage(Context context) {
        Intent it = new Intent("android.intent.action.VIEW");
        it.setData(Uri.parse("market://details?id=" + context.getPackageName()));
        try {
            context.startActivity(it);
        } catch (ActivityNotFoundException e) {
            it.setData(Uri.parse("http://play.google.com/store/apps/details?id=" + context.getPackageName()));
            context.startActivity(it);
        }
    }*/
}