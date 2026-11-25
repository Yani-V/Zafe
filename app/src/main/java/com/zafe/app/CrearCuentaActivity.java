package com.zafe.app;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

public class CrearCuentaActivity extends AppCompatActivity {
    EditText editNombre, editEmail, editContrasena, editRepetirC;
    Button btnCrearCuenta;
    TextView txtErrorContrasena, txtClickIngresar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_crear_cuenta);

        editNombre = findViewById(R.id.editNombre);
        editEmail = findViewById(R.id.editEmail);
        editContrasena = findViewById(R.id.editContrasena);
        editRepetirC = findViewById(R.id.editRepetirC);
        btnCrearCuenta = findViewById(R.id.btnCrearCuenta);
        txtErrorContrasena = findViewById(R.id.txtErrorContrasena);
        txtClickIngresar = findViewById(R.id.txtClickIngresar);

        btnCrearCuenta.setOnClickListener(v -> {
            String nombre = editNombre.getText().toString().trim();
            String email = editEmail.getText().toString().trim();
            String pass = editContrasena.getText().toString().trim();
            String pass2 = editRepetirC.getText().toString().trim();

            if (nombre.isEmpty() || email.isEmpty() || pass.isEmpty()) {
                Toast.makeText(this, "Completá todos los campos", Toast.LENGTH_SHORT).show();
                return;
            }
            if (!pass.equals(pass2)) {
                txtErrorContrasena.setVisibility(View.VISIBLE);
                return;
            }
            txtErrorContrasena.setVisibility(View.GONE);
            Toast.makeText(this, "Cuenta creada, ¡ingresá!", Toast.LENGTH_SHORT).show();
            startActivity(new Intent(CrearCuentaActivity.this, IniciarActivity.class));
            finish();
        });

        txtClickIngresar.setOnClickListener(v -> {
            startActivity(new Intent(CrearCuentaActivity.this, IniciarActivity.class));
            finish();
        });
    }
}