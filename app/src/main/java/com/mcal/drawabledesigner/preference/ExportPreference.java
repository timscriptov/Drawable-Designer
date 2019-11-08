package com.mcal.drawabledesigner.preference;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.AttributeSet;
import android.widget.Toast;

import com.mcal.drawabledesigner.Utils;
import com.mcal.drawabledesigner.event.GetEvent;
import com.mcal.drawabledesigner.state.State;
import com.mcal.drawabledesigner.state.StatePersist;
import com.mcal.drawabledesigner.xml.ShapeExport;

import org.greenrobot.eventbus.EventBus;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;

public class ExportPreference extends FileAskPreference {
    public ExportPreference(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public void save(String filename) throws Exception {
        saveXml(filename);
        saveJson(filename);
        saveImage(filename);
    }

    @SuppressLint({"WrongConstant", "ShowToast"})
    private void saveXml(String filename) throws Exception {
        Throwable th;
        OutputStream outputStream = null;
        try {
            File file = Utils.getFile(filename + ".xml", true);
            OutputStream outputStream2 = new FileOutputStream(file);
            try {
                new ShapeExport(State.shape).exportXml(outputStream2);
                Toast.makeText(getContext(), "Saved at " + file.getAbsolutePath(), 0).show();
                outputStream2.close();
            } catch (Throwable th2) {
                th = th2;
                outputStream = outputStream2;
                outputStream.close();
                throw th;
            }
        } catch (Throwable th3) {
            if (outputStream != null) {
                outputStream.close();
            }
        }
    }

    private void saveJson(String filename) throws IOException {
        Throwable th;
        OutputStreamWriter outputStreamWriter = null;
        try {
            OutputStreamWriter outputStreamWriter2 = new OutputStreamWriter(new FileOutputStream(Utils.getFile(filename + ".json", true)));
            try {
                StatePersist.save(State.shape, outputStreamWriter2);
                outputStreamWriter2.close();
            } catch (Throwable th2) {
                th = th2;
                outputStreamWriter = outputStreamWriter2;
                outputStreamWriter.close();
                throw th;
            }
        } catch (Throwable th3) {
            if (outputStreamWriter != null) {
                outputStreamWriter.close();
            }
        }
    }

    @SuppressLint("ResourceType")
    private void saveImage(String filename) {
        if (getSharedPreferences().getBoolean(getContext().getString(0x7f070042), true)) {
            EventBus.getDefault().post(new GetEvent(new ExportPreference1(filename)));
        }
    }
}