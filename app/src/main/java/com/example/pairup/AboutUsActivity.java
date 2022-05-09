package com.example.pairup;

import android.os.Bundle;
import android.view.View;
import android.webkit.WebView;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

public class AboutUsActivity extends AppCompatActivity {

    Button youtube;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_aboutus);

        youtube = findViewById(R.id.youtube);
        WebView webView = findViewById(R.id.webView);
        youtube.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                webView.setVisibility(View.VISIBLE);
                webView.loadUrl("https://www.youtube.com/channel/UCzaG4o-tWgyOg08yo19UgcQ");
            }
        });
    }
}