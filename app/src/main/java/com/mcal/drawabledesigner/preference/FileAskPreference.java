package com.mcal.drawabledesigner.preference;

import android.annotation.SuppressLint;
import android.app.AlertDialog.Builder;
import android.content.Context;
import android.os.Environment;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.webkit.WebView;
import android.widget.EditText;

import androidx.preference.Preference;

import com.mcal.drawabledesigner.R;
import com.mcal.drawabledesigner.Utils;
import com.mcal.drawabledesigner.state.State;

import java.io.File;
import java.io.IOException;

public abstract class FileAskPreference extends Preference {
    FileAskPreference(Context context, AttributeSet attrs) {
        super(context, attrs);
        setSummary(new File(Environment.getExternalStorageDirectory(), State.projectFolder).getAbsolutePath());
    }

    public abstract void save(String str) throws Exception;

    public void onClick() {
        super.onClick();
        askFilename(State.shape.filename);
    }

    private void askFilename(String filename) {
        final String defaultFilename;
        String DEFAULT_FILE = "output";
        if (filename == null || filename.equals("+new")) {
            defaultFilename = DEFAULT_FILE;
        } else {
            defaultFilename = filename;
        }
        final EditText editText = new EditText(getContext());
        editText.setHint(defaultFilename);
        editText.setMaxLines(1);
        editText.setSingleLine(true);
        new Builder(getContext()).setTitle("Enter filename").setView(editText).setPositiveButton("Export", (dialogInterface, i) -> {
            String name = editText.getText().toString();
            if (name.equals("")) {
                name = defaultFilename;
            }
            try {
                FileAskPreference.this.save(Utils.stripExt(name));
            } catch (Exception x) {
                x.printStackTrace();
            }
            dialogInterface.dismiss();
        }).setNegativeButton(android.R.string.cancel, (dialogInterface, i) -> dialogInterface.dismiss()).create().show();
    }

    private void showContent(String filename) throws IOException {
        File file = Utils.getFile(filename, false);
        @SuppressLint("InflateParams") View view = LayoutInflater.from(getContext()).inflate(R.layout.multiline_edit, null);
        WebView webView = view.findViewById(R.id.webView);
        webView.loadUrl("view-source:" + file.toURI().toURL().toString());
        new Builder(getContext()).setView(view).create().show();
        webView.loadUrl(file.toURI().toURL().toString());
    }
}
