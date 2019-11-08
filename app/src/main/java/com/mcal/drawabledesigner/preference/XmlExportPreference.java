package com.mcal.drawabledesigner.preference;

import android.annotation.SuppressLint;
import android.app.AlertDialog.Builder;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.os.Environment;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.webkit.WebView;
import android.widget.EditText;
import android.widget.Toast;

import androidx.preference.Preference;

import com.mcal.drawabledesigner.R;
import com.mcal.drawabledesigner.state.State;
import com.mcal.drawabledesigner.state.StatePersist;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;

public class XmlExportPreference extends Preference {
    public XmlExportPreference(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public void onClick() {
        super.onClick();
        askFilename(State.shape.filename);
    }

    private String getFilename(String filename) {
        if (filename == null || filename.endsWith(".xml")) {
            return filename;
        }
        return filename + ".xml";
    }

    private void askFilename(String filename) {
        final String defaultFilename = (filename == null || filename.equals("+new")) ? "output.xml" : getFilename(filename);
        final EditText editText = new EditText(getContext());
        editText.setHint(defaultFilename);
        editText.setMaxLines(1);
        editText.setSingleLine(true);
        new Builder(getContext()).setTitle("Enter filename").setView(editText).setPositiveButton("Export", (dialogInterface, i) -> {
            String name = editText.getText().toString();
            if (name.equals("")) {
                name = defaultFilename;
            } else if (!name.toLowerCase().endsWith(".xml")) {
                name = name + ".xml";
            }
            XmlExportPreference.this.exportXml(name);
            dialogInterface.dismiss();
        }).setNegativeButton("Cancel", (dialogInterface, i) -> dialogInterface.dismiss()).create().show();
    }

    @SuppressLint("WrongConstant")
    private void exportXml(String filename) {
        try {
            StatePersist.save(State.shape, new OutputStreamWriter(new FileOutputStream(getFile(filename))));
            Toast.makeText(getContext(), "Export saved at " + getFile(filename).getAbsolutePath(), 0).show();
            View view = LayoutInflater.from(getContext()).inflate(R.layout.multiline_edit, null);
            WebView webView = view.findViewById(R.id.webView);
            webView.loadUrl("view-source:" + getFile(filename).toURI().toURL().toString());
            new Builder(getContext()).setView(view).create().show();
            webView.loadUrl(getFile(filename).toURI().toURL().toString());
        } catch (IOException x) {
            x.printStackTrace();
        }
    }

    private File getFile(String name) throws IOException {
        File dir = new File(Environment.getExternalStorageDirectory(), "DrawableDesigner");
        if (!dir.exists()) {
            dir.mkdirs();
        }
        File file = new File(dir, name);
        if (!file.exists()) {
            file.createNewFile();
        }
        return file;
    }
}
