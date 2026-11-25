package com.zafe.app;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

public class IniciarActivity extends AppCompatActivity {
    EditText editTxtEmail, editContrasena;
    Button btnIngresar;
    TextView txtError, txtCrearCuenta;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_iniciar);

        editTxtEmail = findViewById(R.id.editTxtEmail);
        editContrasena = findViewById(R.id.editContrasena);
        btnIngresar = findViewById(R.id.btnIngresar);
        txtError = findViewById(R.id.txtError);
        txtCrearCuenta = findViewById(R.id.txtCrearCuenta);

        btnIngresar.setOnClickListener(v -> {
            String email = editTxtEmail.getText().toString().trim();
            String pass = editContrasena.getText().toString().trim();

            if (email.isEmpty() || pass.isEmpty() || !email.contains("@") || pass.length() < 6) {
                txtError.setVisibility(View.VISIBLE);
            } else {
                txtError.setVisibility(View.GONE);
                Toast.makeText(this, "¡Bienvenida a Zafe!", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(IniciarActivity.this, HomeActivity.class));
                finish();
            }
        });

        txtCrearCuenta.setOnClickListener(v -> {
            startActivity(new Intent(IniciarActivity.this, CrearCuentaActivity.class));
        });
    }
}