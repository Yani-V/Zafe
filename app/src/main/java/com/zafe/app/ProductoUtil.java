package com.zafe.app;

import android.graphics.Color;
import android.widget.TextView;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

// Funciones que comparten el Home y el Detalle: fechas, precios y estado del producto
public class ProductoUtil {

    public static final String VENCIDO = "Vencido";
    public static final String STOCK_BAJO = "Stock bajo";
    public static final String VIGENTE = "Vigente";

    // Valor de ejemplo para mostrar el precio en dólares. Se puede cambiar.
    public static final double TIPO_CAMBIO = 1540.0;

    // Convierte "dd/mm/aaaa" en fecha.
    public static Date parseFecha(String texto) {
        if (texto == null || texto.trim().isEmpty()) {
            return null;
        }
        // Se unifican los separadores: "17-10-2026" pasa a "17/10/2026"
        String limpio = texto.trim().replace("-", "/").replace(".", "/");
        // El año tiene que tener 4 dígitos, así "17/10/26" no se lee como el año 26
        if (!limpio.matches("\\d{1,2}/\\d{1,2}/\\d{4}")) {
            return null;
        }
        try {
            SimpleDateFormat formato = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
            formato.setLenient(false);
            return formato.parse(limpio);
        } catch (ParseException e) {
            return null;
        }
    }

    // Fecha de hoy a las 00:00
    private static Date inicioDeHoy() {
        Calendar c = Calendar.getInstance();
        c.set(Calendar.HOUR_OF_DAY, 0);
        c.set(Calendar.MINUTE, 0);
        c.set(Calendar.SECOND, 0);
        c.set(Calendar.MILLISECOND, 0);
        return c.getTime();
    }

    // Cuántos días pasaron desde el vencimiento
    public static long diasVencido(Date fecha) {
        long diferencia = inicioDeHoy().getTime() - fecha.getTime();
        return diferencia / (24L * 60 * 60 * 1000);
    }

    // Convierte textos como "24", "1800.50" o "$ 1.800,00" en número
    public static double parseNumero(String texto) {
        if (texto == null) {
            return 0;
        }
        String s = texto.replace("$", "").replace(" ", "").trim();
        if (s.isEmpty()) {
            return 0;
        }
        if (s.contains(",")) {
            s = s.replace(".", "").replace(",", ".");
        }
        try {
            return Double.parseDouble(s);
        } catch (NumberFormatException e) {
            return 0;
        }
    }

    public static boolean stockBajo(String stock, String minimo) {
        return parseNumero(stock) <= parseNumero(minimo);
    }

    // Prioridad: primero vencido, después stock bajo, si no vigente
    public static String estado(String stock, String minimo, String vencimiento) {
        Date fecha = parseFecha(vencimiento);
        if (fecha != null && fecha.before(inicioDeHoy())) {
            return VENCIDO;
        }
        if (stockBajo(stock, minimo)) {
            return STOCK_BAJO;
        }
        return VIGENTE;
    }

    // Pinta la etiqueta de estado con el fondo y el color que corresponden
    public static void aplicarEstado(TextView etiqueta, String estado) {
        etiqueta.setText(estado);
        if (VENCIDO.equals(estado)) {
            etiqueta.setBackgroundResource(R.drawable.bg_badge_vencido);
            etiqueta.setTextColor(Color.parseColor("#a31515"));
        } else if (STOCK_BAJO.equals(estado)) {
            etiqueta.setBackgroundResource(R.drawable.bg_badge_stock_bajo);
            etiqueta.setTextColor(Color.parseColor("#7a5200"));
        } else {
            etiqueta.setBackgroundResource(R.drawable.bg_badge_vigente);
            etiqueta.setTextColor(Color.parseColor("#1e6b32"));
        }
    }

    public static String formatoPesos(double valor) {
        return String.format(new Locale("es", "AR"), "$ %,.2f", valor);
    }

    public static String formatoDolares(double pesos) {
        return String.format(new Locale("es", "AR"), "US$ %,.2f", pesos / TIPO_CAMBIO);
    }
}