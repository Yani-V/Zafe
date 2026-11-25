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
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import java.util.HashMap;
import java.util.Map;

public class NuevoProductoActivity extends AppCompatActivity {
    EditText nuevoInputNombre, nuevoInputCodigoBarras, nuevoInputCantidadStock, nuevoInputStockMinimo, nuevoInputPrecio, nuevoInputVencimiento, nuevoInputProveedor, nuevoInputUbicacion;
    TextView nuevoErrorNombre, nuevoLabelTitulo;
    Button nuevoBtnCancelar, nuevoBtnGuardar;
    ImageView nuevoBtnVolver;
    FirebaseFirestore db = FirebaseFirestore.getInstance();

    // Si viene un id, estamos editando un producto que ya existe
    String idEditar;

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
        nuevoLabelTitulo = findViewById(R.id.nuevoLabelTitulo);
        nuevoBtnCancelar = findViewById(R.id.nuevoBtnCancelar);
        nuevoBtnGuardar = findViewById(R.id.nuevoBtnGuardar);
        nuevoBtnVolver = findViewById(R.id.nuevoBtnVolver);

        // Modo editar: se llenan los campos con los datos que llegan del detalle
        if (getIntent().hasExtra("nombre")) {
            idEditar = getIntent().getStringExtra("id");
            nuevoLabelTitulo.setText("Editar producto");
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
            String vencimiento = nuevoInputVencimiento.getText().toString().trim();
            if (!vencimiento.isEmpty() && ProductoUtil.parseFecha(vencimiento) == null) {
                Toast.makeText(this, "El vencimiento debe tener el formato dd/mm/aaaa", Toast.LENGTH_SHORT).show();
                return;
            }

            // Datos del producto tal como se guardan en Firestore
            Map<String, Object> producto = new HashMap<>();
            producto.put("nombre", nombre);
            producto.put("codigo_barras", codigo);
            // Stock, mínimo y precio se guardan como número, igual que los productos que ya tenía
            producto.put("cantidad_stock", ProductoUtil.parseNumero(nuevoInputCantidadStock.getText().toString()));
            producto.put("stock_minimo", ProductoUtil.parseNumero(nuevoInputStockMinimo.getText().toString()));
            producto.put("precio_unitario", ProductoUtil.parseNumero(nuevoInputPrecio.getText().toString()));
            producto.put("vencimiento", nuevoInputVencimiento.getText().toString().trim());
            producto.put("vencimiento", vencimiento);
            producto.put("ubicacion", nuevoInputUbicacion.getText().toString().trim());

            // Producto nuevo: documento nuevo. Edición: se guarda sobre el mismo documento
            DocumentReference documento = (idEditar == null)
                    ? db.collection("productos").document()
                    : db.collection("productos").document(idEditar);

            nuevoBtnGuardar.setEnabled(false);
            documento.set(producto)
                    .addOnSuccessListener(x -> {
                        Toast.makeText(this, "Producto guardado", Toast.LENGTH_SHORT).show();
                        Intent i = new Intent(NuevoProductoActivity.this, HomeActivity.class);
                        i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(i);
                        finish();
                    })
                    .addOnFailureListener(e -> {
                        nuevoBtnGuardar.setEnabled(true);
                        Toast.makeText(this, "Error al guardar: " + e.getMessage(), Toast.LENGTH_LONG).show();
                    });
        });
    }
}