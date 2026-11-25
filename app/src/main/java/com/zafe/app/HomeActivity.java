package com.zafe.app;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import java.util.ArrayList;
import java.util.List;

public class HomeActivity extends AppCompatActivity {
    Button home_btnSalirZafe;
    TextView home_btnProductoZafe, home_numProductosZafe, home_numStockBajoZafe, home_numVencidosZafe;
    EditText home_buscarZafe;
    LinearLayout home_listaProductos;
    FirebaseFirestore db = FirebaseFirestore.getInstance();

    // Productos que vienen de Firestore
    List<Producto> productos = new ArrayList<>();

    // Datos de un producto (el id es el del documento en Firestore)
    static class Producto {
        String id, nombre, codigo, stock, minimo, precio, vencimiento, proveedor, ubicacion;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        home_btnSalirZafe = findViewById(R.id.home_btnSalirZafe);
        home_btnProductoZafe = findViewById(R.id.home_btnProductoZafe);
        home_buscarZafe = findViewById(R.id.home_buscarZafe);
        home_numProductosZafe = findViewById(R.id.home_numProductosZafe);
        home_numStockBajoZafe = findViewById(R.id.home_numStockBajoZafe);
        home_numVencidosZafe = findViewById(R.id.home_numVencidosZafe);
        home_listaProductos = findViewById(R.id.home_listaProductos);

        home_btnSalirZafe.setOnClickListener(v -> {
            startActivity(new Intent(HomeActivity.this, IniciarActivity.class));
            finish();
        });

        home_btnProductoZafe.setOnClickListener(v -> {
            startActivity(new Intent(HomeActivity.this, NuevoProductoActivity.class));
        });

        // Buscador: vuelve a dibujar la lista con lo que se escribe
        home_buscarZafe.addTextChangedListener(new TextWatcher() {
            @Override public void beforeTextChanged(CharSequence s, int st, int c, int a) {}
            @Override public void onTextChanged(CharSequence s, int st, int b, int c) {
                mostrarProductos(s.toString());
            }
            @Override public void afterTextChanged(Editable s) {}
        });
    }

    // Se ejecuta cada vez que se vuelve al Home, así aparece lo que se agregó, editó o borró
    @Override
    protected void onResume() {
        super.onResume();
        cargarProductos();
    }

    private void cargarProductos() {
        db.collection("productos").get()
                .addOnSuccessListener(snapshot -> {
                    productos.clear();
                    for (DocumentSnapshot doc : snapshot.getDocuments()) {
                        Producto p = new Producto();
                        p.id = doc.getId();
                        p.nombre = texto(doc, "nombre");
                        p.codigo = texto(doc, "codigo_barras");
                        p.stock = texto(doc, "cantidad_stock");
                        p.minimo = texto(doc, "stock_minimo");
                        p.precio = texto(doc, "precio_unitario");
                        p.vencimiento = texto(doc, "vencimiento");
                        p.proveedor = texto(doc, "proveedor");
                        p.ubicacion = texto(doc, "ubicacion");
                        productos.add(p);
                    }
                    mostrarProductos(home_buscarZafe.getText().toString());
                })
                .addOnFailureListener(e ->
                        Toast.makeText(this, "Error al leer Firestore: " + e.getMessage(), Toast.LENGTH_LONG).show());
    }

    // Lee un campo del documento como texto, sin importar si en Firestore es texto o número
    private String texto(DocumentSnapshot doc, String campo) {
        Object valor = doc.get(campo);
        if (valor == null) {
            return "";
        }
        // Un número entero (20.0) se muestra sin decimales (20)
        if (valor instanceof Number) {
            double d = ((Number) valor).doubleValue();
            if (d == Math.floor(d)) {
                return String.valueOf((long) d);
            }
        }
        return String.valueOf(valor);
    }

    // Dibuja una tarjeta por cada producto y actualiza los 3 contadores de arriba
    private void mostrarProductos(String filtro) {
        home_listaProductos.removeAllViews();
        String q = filtro.trim().toLowerCase();
        int vencidos = 0;
        int stockBajo = 0;

        for (Producto p : productos) {
            String estado = ProductoUtil.estado(p.stock, p.minimo, p.vencimiento);
            // Los contadores cuentan todos los productos, aunque el buscador esconda alguno
            if (ProductoUtil.VENCIDO.equals(estado)) {
                vencidos++;
            }
            if (ProductoUtil.stockBajo(p.stock, p.minimo)) {
                stockBajo++;
            }

            if (!q.isEmpty() && !p.nombre.toLowerCase().contains(q)) {
                continue;
            }

            View card = getLayoutInflater().inflate(R.layout.item_producto, home_listaProductos, false);

            // Proveedor y ciudad (de "El Bolsón - Río Negro" se muestra solo "El Bolsón")
            String ciudad = p.ubicacion.split(" - ")[0];
            String lineaProveedor = ciudad.isEmpty() ? p.proveedor : p.proveedor + " · " + ciudad;

            double precio = ProductoUtil.parseNumero(p.precio);
            boolean tieneFecha = ProductoUtil.parseFecha(p.vencimiento) != null;

            ((TextView) card.findViewById(R.id.home_nombreProducto1)).setText(p.nombre);
            ((TextView) card.findViewById(R.id.home_proveedorProducto1)).setText(lineaProveedor);
            ((TextView) card.findViewById(R.id.home_cantidadProducto1)).setText(p.stock);
            ((TextView) card.findViewById(R.id.home_minimoProducto1)).setText("/  mín. " + p.minimo);
            ((TextView) card.findViewById(R.id.home_precioPesosProducto1)).setText(ProductoUtil.formatoPesos(precio));
            ((TextView) card.findViewById(R.id.home_precioDolarProducto1)).setText(ProductoUtil.formatoDolares(precio));
            ((TextView) card.findViewById(R.id.home_vencimientoProducto1))
                    .setText(tieneFecha ? p.vencimiento : "Sin vencimiento");
            ProductoUtil.aplicarEstado((TextView) card.findViewById(R.id.home_estadoProducto1), estado);

            card.setOnClickListener(v -> abrirDetalle(p));
            home_listaProductos.addView(card);
        }

        home_numProductosZafe.setText(String.valueOf(productos.size()));
        home_numStockBajoZafe.setText(String.valueOf(stockBajo));
        home_numVencidosZafe.setText(String.valueOf(vencidos));
    }

    private void abrirDetalle(Producto p) {
        Intent i = new Intent(HomeActivity.this, DetalleProductoActivity.class);
        i.putExtra("id", p.id);
        i.putExtra("nombre", p.nombre);
        i.putExtra("codigo", p.codigo);
        i.putExtra("stock", p.stock);
        i.putExtra("minimo", p.minimo);
        i.putExtra("precio", p.precio);
        i.putExtra("vencimiento", p.vencimiento);
        i.putExtra("proveedor", p.proveedor);
        i.putExtra("ubicacion", p.ubicacion);
        startActivity(i);
    }
}