package com.demo.filereceiver;

import android.content.ClipData;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import java.io.IOException;
import java.io.InputStream;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        TextView textView = (TextView) findViewById(R.id.textView);

        Intent intent = getIntent();
        ClipData clipData = intent.getClipData();
        if (clipData.getItemCount() == 1) {
            ClipData.Item item = clipData.getItemAt(0);
            Uri uri = item.getUri();
            String content = readUri(uri);
            if (content == null) {
                textView.setText("Error reading Uri ".concat(uri.toString()));
            } else {
                textView.setText(content);
            }
        }
    }

    private String readUri(Uri uri) {
        InputStream inputStream = null;
        try {
            inputStream = getContentResolver().openInputStream(uri);
            if (inputStream != null) {
                byte[] buffer = new byte[1024];
                int result;
                String content = "";
                while ((result = inputStream.read(buffer)) != -1) {
                    content = content.concat(new String(buffer, 0, result));
                }
                return content;
            }
        } catch (IOException e) {
            Log.e("receiver", "IOException when reading uri", e);
        } finally {
            if (inputStream != null) {
                try {
                    inputStream.close();
                } catch (IOException e) {
                    Log.e("receiver", "IOException when closing stream", e);
                }
            }
        }
        return null;
    }
}
