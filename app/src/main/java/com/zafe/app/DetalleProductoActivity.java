package com.zafe.app;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

public class DetalleProductoActivity extends AppCompatActivity {
    TextView txtNombreDetalle, txtAlerta, txtStock, txtStockMinimo, txtCodigo, txtPrecio, txtVencimiento, txtProveedor, txtUbicacion;
    ImageView btnVolver;
    Button btnEliminarr, btnEditar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detalle_producto);

        txtNombreDetalle = findViewById(R.id.txtNombreDetalle);
        txtAlerta = findViewById(R.id.txtAlerta);
        txtStock = findViewById(R.id.txtStock);
        txtStockMinimo = findViewById(R.id.txtStockMinimo);
        txtCodigo = findViewById(R.id.txtCodigo);
        txtPrecio = findViewById(R.id.txtPrecio);
        txtVencimiento = findViewById(R.id.txtVencimiento);
        txtProveedor = findViewById(R.id.txtProveedor);
        txtUbicacion = findViewById(R.id.txtUbicacion);
        btnVolver = findViewById(R.id.btnVolver);
        btnEliminarr = findViewById(R.id.btnEliminarr);
        btnEditar = findViewById(R.id.btnEditar);

        Intent intent = getIntent();
        String nombre = intent.getStringExtra("nombre");
        if (nombre != null) {
            txtNombreDetalle.setText(nombre);
            txtCodigo.setText(intent.getStringExtra("codigo"));
            txtStock.setText(intent.getStringExtra("stock"));
            txtStockMinimo.setText(intent.getStringExtra("minimo"));
            txtPrecio.setText(intent.getStringExtra("precio"));
            txtVencimiento.setText(intent.getStringExtra("vencimiento"));
            txtProveedor.setText(intent.getStringExtra("proveedor"));
            txtUbicacion.setText(intent.getStringExtra("ubicacion"));
            if ("17/10/2026".equals(intent.getStringExtra("vencimiento"))) {
                txtAlerta.setText("Venció el 17/10/2026, hace 38 días.");
            } else {
                txtAlerta.setText("Producto vigente. Vencimiento: " + intent.getStringExtra("vencimiento"));
            }
        }

        btnVolver.setOnClickListener(v -> finish());

        btnEliminarr.setOnClickListener(v -> {
            Toast.makeText(this, "Producto eliminado", Toast.LENGTH_SHORT).show();
            startActivity(new Intent(DetalleProductoActivity.this, HomeActivity.class));
            finish();
        });

        btnEditar.setOnClickListener(v -> {
            Intent i = new Intent(DetalleProductoActivity.this, NuevoProductoActivity.class);
            i.putExtras(intent.getExtras() != null ? intent.getExtras() : new Bundle());
            startActivity(i);
        });
    }
}