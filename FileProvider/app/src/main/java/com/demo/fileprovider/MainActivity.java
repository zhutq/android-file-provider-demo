package com.demo.fileprovider;

import android.content.ClipData;
import android.content.ClipDescription;
import android.content.Intent;
import android.net.Uri;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.UUID;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void provide(View view) {
        String content = "Hello FileProvider! ".concat(String.valueOf(System.currentTimeMillis()));
        File file = new File(getFilesDir(), UUID.randomUUID().toString().concat(".txt"));
        if (!writeFile(file, content)) {
            return;
        }

        Uri uri = FileProvider.getUriForFile(this, "com.demo.fileprovider", file);

        Intent intent = new Intent().setClassName("com.demo.filereceiver", "com.demo.filereceiver.MainActivity");
        intent.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);

        ClipData clipData = new ClipData(new ClipDescription("Meshes", new String[]{ClipDescription.MIMETYPE_TEXT_URILIST}), new ClipData.Item(uri));
        intent.setClipData(clipData);
        startActivity(intent);
    }

    private boolean writeFile(File file, String content) {
        FileOutputStream stream = null;
        try {
            if (!file.exists()) {
                boolean created = file.createNewFile();
                if (!created) {
                    return false;
                }
            }
            stream = new FileOutputStream(file);
            stream.write(content.getBytes());
            stream.flush();
            stream.close();
            return true;
        } catch (IOException e) {
            Log.e("provider", "IOException writing file: ", e);
        } finally {
            try {
                if (stream != null) {
                    stream.close();
                }
            } catch (IOException e) {
                Log.e("provider", "IOException closing stream: ", e);
            }
        }
        return false;
    }
}
