package com.zafe.app;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import android.view.View;

import com.google.firebase.firestore.FirebaseFirestore;

public class DetalleProductoActivity extends AppCompatActivity {
    TextView txtNombreDetalle, txtEstado, txtAlerta, txtStock, txtStockMinimo, txtCodigo, txtPrecio, txtVencimiento, txtProveedor, txtUbicacion;
    ImageView btnVolver;
    Button btnEliminarr, btnEditar;
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    String idProducto;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detalle_producto);

        txtNombreDetalle = findViewById(R.id.txtNombreDetalle);
        txtAlerta = findViewById(R.id.txtAlerta);
        txtEstado = findViewById(R.id.txtEstado);
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
        idProducto = intent.getStringExtra("id");
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

            // El estado se calcula con los datos del producto.
            String vencimiento = intent.getStringExtra("vencimiento");
            String estado = ProductoUtil.estado(intent.getStringExtra("stock"), intent.getStringExtra("minimo"), vencimiento);
            ProductoUtil.aplicarEstado(txtEstado, estado);

            // El aviso rojo solo aparece si el producto está vencido.
            if (ProductoUtil.VENCIDO.equals(estado)) {
                long dias = ProductoUtil.diasVencido(ProductoUtil.parseFecha(vencimiento));
                txtAlerta.setText("Venció el " + vencimiento + ", hace " + dias + (dias == 1 ? " día." : " días."));
                txtAlerta.setVisibility(View.VISIBLE);
            } else {
                txtAlerta.setVisibility(View.GONE);
            }
        }

        btnVolver.setOnClickListener(v -> finish());

        btnEliminarr.setOnClickListener(v -> {
            btnEliminarr.setEnabled(false);
            // Se borra el documento de Firestore.
            db.collection("productos").document(idProducto).delete()
                    .addOnSuccessListener(x -> {
                        Toast.makeText(this, "Producto eliminado", Toast.LENGTH_SHORT).show();

                        finish();
                    })
                    .addOnFailureListener(e -> {
                        btnEliminarr.setEnabled(true);
                        Toast.makeText(this, "Error al eliminar: " + e.getMessage(), Toast.LENGTH_LONG).show();
                    });
        });

        btnEditar.setOnClickListener(v -> {
            Intent i = new Intent(DetalleProductoActivity.this, NuevoProductoActivity.class);
            i.putExtras(intent.getExtras() != null ? intent.getExtras() : new Bundle());
            startActivity(i);
        });
    }
}