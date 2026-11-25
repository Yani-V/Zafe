package com.zafe.app;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

public class NuevoProductoActivity extends AppCompatActivity {
    EditText nuevoInputNombre, nuevoInputCodigoBarras, nuevoInputCantidadStock, nuevoInputStockMinimo, nuevoInputPrecio, nuevoInputVencimiento, nuevoInputProveedor, nuevoInputUbicacion;
    TextView nuevoErrorNombre;
    Button nuevoBtnCancelar, nuevoBtnGuardar;
    ImageView nuevoBtnVolver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nuevo_producto);

        nuevoInputNombre = findViewById(R.id.nuevoInputNombre);
        nuevoInputCodigoBarras = findViewById(R.id.nuevoInputCodigoBarras);
        nuevoInputCantidadStock = findViewById(R.id.nuevoInputCantidadStock);
        nuevoInputStockMinimo = findViewById(R.id.nuevoInputStockMinimo);
        nuevoInputPrecio = findViewById(R.id.nuevoInputPrecio);
        nuevoInputVencimiento = findViewById(R.id.nuevoInputVencimiento);
        nuevoInputProveedor = findViewById(R.id.nuevoInputProveedor);
        nuevoInputUbicacion = findViewById(R.id.nuevoInputUbicacion);
        nuevoErrorNombre = findViewById(R.id.nuevoErrorNombre);
        nuevoBtnCancelar = findViewById(R.id.nuevoBtnCancelar);
        nuevoBtnGuardar = findViewById(R.id.nuevoBtnGuardar);
        nuevoBtnVolver = findViewById(R.id.nuevoBtnVolver);

        if (getIntent().hasExtra("nombre")) {
            nuevoInputNombre.setText(getIntent().getStringExtra("nombre"));
            nuevoInputCodigoBarras.setText(getIntent().getStringExtra("codigo"));
            nuevoInputCantidadStock.setText(getIntent().getStringExtra("stock"));
            nuevoInputStockMinimo.setText(getIntent().getStringExtra("minimo"));
            nuevoInputPrecio.setText(getIntent().getStringExtra("precio"));
            nuevoInputVencimiento.setText(getIntent().getStringExtra("vencimiento"));
            nuevoInputProveedor.setText(getIntent().getStringExtra("proveedor"));
            nuevoInputUbicacion.setText(getIntent().getStringExtra("ubicacion"));
        }

        nuevoBtnVolver.setOnClickListener(v -> finish());
        nuevoBtnCancelar.setOnClickListener(v -> finish());

        nuevoBtnGuardar.setOnClickListener(v -> {
            String nombre = nuevoInputNombre.getText().toString().trim();
            if (nombre.isEmpty()) {
                nuevoErrorNombre.setVisibility(View.VISIBLE);
                return;
            }
            nuevoErrorNombre.setVisibility(View.GONE);
            String codigo = nuevoInputCodigoBarras.getText().toString().trim();
            if (!codigo.isEmpty() && codigo.length() != 13) {
                Toast.makeText(this, "El código debe tener 13 dígitos", Toast.LENGTH_SHORT).show();
                return;
            }
            Intent i = new Intent(NuevoProductoActivity.this, HomeActivity.class);
            i.putExtra("nombre_nuevo", nombre);
            i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(i);
            finish();
        });
    }
}