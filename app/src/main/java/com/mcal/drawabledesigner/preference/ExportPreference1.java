package com.mcal.drawabledesigner.preference;

import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;

import com.mcal.drawabledesigner.Utils;
import com.mcal.drawabledesigner.event.GetEvent.ReceiveCallback;

import java.io.FileOutputStream;
import java.io.IOException;

class ExportPreference1 implements ReceiveCallback<Bitmap> {
    private final String filename;

    ExportPreference1(String str) {
        this.filename = str;
    }

    public void receive(Bitmap bitmap) {
        FileOutputStream fos = null;
        try {
            FileOutputStream fos2 = new FileOutputStream(Utils.getFile(filename  + ".png", false));
            try {
                bitmap.compress(CompressFormat.PNG, 100, fos2);
                try {
                    fos2.close();
                    fos = fos2;
                } catch (IOException x2) {
                    x2.printStackTrace();
                    fos = fos2;
                }
            } catch (Throwable th3) {
                fos = fos2;
                fos.close();
            }
        } catch (IOException e2) {
            if (fos != null) {
                try {
                    fos.close();
                } catch (IOException ignored) {
                }
            }
        }
    }
}
