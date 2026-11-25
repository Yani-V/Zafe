package com.zafe.app;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

public class HomeActivity extends AppCompatActivity {
    Button home_btnSalirZafe;
    TextView home_btnProductoZafe, home_numProductosZafe;
    EditText home_buscarZafe;
    View card1, card2, card3, card4;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        home_btnSalirZafe = findViewById(R.id.home_btnSalirZafe);
        home_btnProductoZafe = findViewById(R.id.home_btnProductoZafe);
        home_buscarZafe = findViewById(R.id.home_buscarZafe);
        home_numProductosZafe = findViewById(R.id.home_numProductosZafe);

        card1 = findViewById(R.id.home_cardProducto1);
        card2 = findViewById(R.id.home_CardProducto2);
        card3 = findViewById(R.id.home_cardProducto3);
        card4 = findViewById(R.id.home_cardProducto4);

        String nuevo = getIntent().getStringExtra("nombre_nuevo");
        if (nuevo != null) {
            Toast.makeText(this, "Producto guardado: " + nuevo, Toast.LENGTH_LONG).show();
            home_numProductosZafe.setText("8");
        }

        home_btnSalirZafe.setOnClickListener(v -> {
            startActivity(new Intent(HomeActivity.this, IniciarActivity.class));
            finish();
        });

        home_btnProductoZafe.setOnClickListener(v -> {
            startActivity(new Intent(HomeActivity.this, NuevoProductoActivity.class));
        });

        card1.setOnClickListener(v -> abrirDetalle("aceite", "7790001234567", "24", "15", "$ 1.800,00", "17/10/2026", "HIERBASRURAL.SRL", "El Bolsón - Río Negro"));
        card2.setOnClickListener(v -> abrirDetalle("Lavandina Ayudín 1l", "7790001112223", "2", "2", "$ 1.300,00", "Sin vencimiento", "LIMPI.SRL", "Buenos Aires"));
        card3.setOnClickListener(v -> abrirDetalle("Aceite Girasol 900 ml", "7790003334445", "12", "3", "$ 1.800,00", "03/02/2027", "FRANKTUP.SRL", "Jacobacci"));
        card4.setOnClickListener(v -> abrirDetalle("yerba", "7790005556667", "48", "15", "$ 4.200,00", "26/11/2026", "DAUSUR.SRL", "Córdoba Capital"));

        home_buscarZafe.addTextChangedListener(new TextWatcher() {
            @Override public void beforeTextChanged(CharSequence s, int st, int c, int a) {}
            @Override public void onTextChanged(CharSequence s, int st, int b, int c) {
                String q = s.toString().toLowerCase();
                card1.setVisibility("aceite".contains(q) || q.isEmpty() ? View.VISIBLE : View.GONE);
                card2.setVisibility("lavandina".contains(q) || q.isEmpty() ? View.VISIBLE : View.GONE);
                card3.setVisibility("girasol".contains(q) || q.isEmpty() ? View.VISIBLE : View.GONE);
                card4.setVisibility("yerba".contains(q) || q.isEmpty() ? View.VISIBLE : View.GONE);
            }
            @Override public void afterTextChanged(Editable s) {}
        });
    }

    private void abrirDetalle(String nombre, String codigo, String stock, String minimo, String precio, String venc, String prov, String ubi) {
        Intent i = new Intent(HomeActivity.this, DetalleProductoActivity.class);
        i.putExtra("nombre", nombre);
        i.putExtra("codigo", codigo);
        i.putExtra("stock", stock);
        i.putExtra("minimo", minimo);
        i.putExtra("precio", precio);
        i.putExtra("vencimiento", venc);
        i.putExtra("proveedor", prov);
        i.putExtra("ubicacion", ubi);
        startActivity(i);
    }
}