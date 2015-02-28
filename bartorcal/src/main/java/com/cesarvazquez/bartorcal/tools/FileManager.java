package com.cesarvazquez.bartorcal.tools;

import android.content.Context;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStreamWriter;

/**
 * Created by cesar on 2/9/14.
 */
public class FileManager {
    public static final FileManager instance = new FileManager();

    public synchronized void writeFile(Context context, String file, String data) throws IOException {
        OutputStreamWriter outputStreamWriter = new OutputStreamWriter(context.openFileOutput(file, Context.MODE_PRIVATE));
        outputStreamWriter.write(data);
        outputStreamWriter.close();
    }

    public String readFromFile(Context context, String file) throws IOException {
        StringBuffer ret = new StringBuffer();
        InputStream is = context.openFileInput(file);
        while (is.available()>0)
            ret.append((char)is.read());
        is.close();
        return ret.toString();
    }
}
