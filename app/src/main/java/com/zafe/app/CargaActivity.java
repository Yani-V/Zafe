package com.zafe.app;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import androidx.appcompat.app.AppCompatActivity;

public class CargaActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_carga);

        new Handler(Looper.getMainLooper()).postDelayed(() -> {
            startActivity(new Intent(CargaActivity.this, IniciarActivity.class));
            finish();
        }, 2000);
    }
}