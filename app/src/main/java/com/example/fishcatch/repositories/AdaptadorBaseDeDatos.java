package com.example.fishcatch.repositories;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.widget.Toast;

import com.example.fishcatch.model.Captura;

import java.util.ArrayList;

public class AdaptadorBaseDeDatos {
    private Context contexto;
    private BaseDeDatosSingleton instance;

    public AdaptadorBaseDeDatos(Context context) {
        //Almacenamos el contexto
        this.contexto = context;
        //Creamos una instancia a la Base de Datos
        instance = BaseDeDatosSingleton.getInstance(contexto);
        instance.getWritableDatabase();
    }

    //AGREGAR CAPTURA
    //Método para obtener todas las Especies
    public Cursor consultarEspecies(){
        SQLiteDatabase readableDatabase = instance.getReadableDatabase();
        Cursor cursor = readableDatabase.rawQuery("SELECT id,nombreEspecie FROM Especie", null);
        return cursor;
    }

    //Método para insertar la Captura en la BD. Recibe los parámetros a insertar
    public long insertarCaptura(int idEspecie, double peso, double tamanno, String fecha, String hora, String comentario, String fotoUri) {
        SQLiteDatabase writableDatabase = instance.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("idEspecie", idEspecie);
        values.put("peso", peso);
        values.put("tamanno", tamanno);
        values.put("fecha", fecha);
        values.put("hora", hora);
        values.put("comentario", comentario);
        values.put("foto", fotoUri);
        return writableDatabase.insert("Captura", null, values);
    }

    //Método para insertar la Ubicación
    public void insertarUbicacion(long idCaptura, double latitud, double longitud) {
        SQLiteDatabase writableDatabase = instance.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("idCaptura", idCaptura);
        values.put("latitud", latitud);
        values.put("longitud", longitud);
        writableDatabase.insert("Ubicacion", null, values);
    }

    //Método para insertar las Condiciones
    public void insertarCondiciones(long idCaptura, double temperatura) {
        SQLiteDatabase writableDatabase = instance.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("idCaptura", idCaptura);
        values.put("temperatura", temperatura);
        writableDatabase.insert("Condiciones", null, values);
    }

    //Método para obtener el Id de la Especie por el nombre de la Especie
    public int obtenerIdEspeciePorNombre(String nombreEspecie) {
        SQLiteDatabase readableDatabase = instance.getReadableDatabase();
        Cursor cursor = readableDatabase.rawQuery("SELECT id FROM Especie WHERE nombreEspecie = ?", new String[]{nombreEspecie});
        if (cursor.moveToFirst()) {
            return cursor.getInt(0);
        } else {
            return -1;  // Error
        }
    }

    //VER CAPTURA
    //Método para obtener las distintas fechas entre todas las capturas
    public ArrayList<String> obtenerFechasCapturasUnicas() {
        ArrayList<String> fechas = new ArrayList<>();
        SQLiteDatabase readableDatabase = instance.getReadableDatabase();
        Cursor cursor = readableDatabase.rawQuery("SELECT DISTINCT fecha FROM Captura ORDER BY fecha DESC", null);

        if (cursor.moveToFirst()) {
            do {
                fechas.add(cursor.getString(0)); // fecha
            } while (cursor.moveToNext());
        }

        cursor.close();
        return fechas;
    }

    //Método para obtener las capturas de una fecha determinada
    public ArrayList<Captura> obtenerCapturasPorFecha(String fecha) {
        ArrayList<Captura> capturas = new ArrayList<>();

        SQLiteDatabase readableDatabase = instance.getReadableDatabase();
        String consulta = "SELECT c.id, c.idEspecie, e.nombreEspecie, c.peso, c.tamanno, c.fecha, c.hora, c.comentario, c.foto " +
                "FROM Captura c INNER JOIN Especie e ON c.idEspecie = e.id " +
                "WHERE c.fecha = ?";
        Cursor cursor = readableDatabase.rawQuery(consulta, new String[]{fecha});

        if (cursor.moveToFirst()) {
            do {
                Captura captura = new Captura();
                captura.setId(cursor.getInt(0));
                captura.setIdEspecie(cursor.getInt(1));
                captura.setNombreEspecie(cursor.getString(2));
                captura.setPeso(cursor.getDouble(3));
                captura.setTamanno(cursor.getDouble(4));
                captura.setFecha(cursor.getString(5));
                captura.setHora(cursor.getString(6));
                captura.setComentario(cursor.getString(7));
                captura.setFotoUri(cursor.getString(8));

                capturas.add(captura);
            } while (cursor.moveToNext());
        }

        cursor.close();
        return capturas;
    }

    //Método para eliminar una Captura utilizando el idCaptura
    public void eliminarCaptura(int idCaptura) {
        SQLiteDatabase writableDatabase = instance.getWritableDatabase();
        String queryEliminarCaptura = "DELETE FROM Captura WHERE id = ?";
        writableDatabase.execSQL(queryEliminarCaptura, new Object[]{idCaptura});
        Toast.makeText(contexto, "La captura ha sido eliminada correctamente", Toast.LENGTH_SHORT).show();
    }

    //Método para obtener la Captura por id
    public Captura obtenerCapturaPorId(int idCaptura) {
        Captura captura = null;

        SQLiteDatabase readableDatabase = instance.getReadableDatabase();

        //Consulta para obtener la captura y su especie asociada
        String consulta = "SELECT c.id, c.idEspecie, e.nombreEspecie, c.peso, c.tamanno, c.fecha, c.hora, c.comentario, c.foto " +
                "FROM Captura c INNER JOIN Especie e ON c.idEspecie = e.id " +
                "WHERE c.id = ?";
        Cursor cursor = readableDatabase.rawQuery(consulta, new String[]{String.valueOf(idCaptura)});

        if (cursor != null && cursor.moveToFirst()) {
            // Extraemos los datos de la captura
            int id = cursor.getInt(cursor.getColumnIndexOrThrow("id"));
            int idEspecie = cursor.getInt(cursor.getColumnIndexOrThrow("idEspecie"));
            String nombreEspecie = cursor.getString(cursor.getColumnIndexOrThrow("nombreEspecie"));
            double peso = cursor.getDouble(cursor.getColumnIndexOrThrow("peso"));
            double tamanno = cursor.getDouble(cursor.getColumnIndexOrThrow("tamanno"));
            String fecha = cursor.getString(cursor.getColumnIndexOrThrow("fecha"));
            String hora = cursor.getString(cursor.getColumnIndexOrThrow("hora"));
            String comentario = cursor.getString(cursor.getColumnIndexOrThrow("comentario"));
            String fotoUri = cursor.getString(cursor.getColumnIndexOrThrow("foto"));

            // Creamos la captura con los datos obtenidos
            captura = new Captura();
            captura.setId(id);
            captura.setIdEspecie(idEspecie);
            captura.setNombreEspecie(nombreEspecie);
            captura.setPeso(peso);
            captura.setTamanno(tamanno);
            captura.setFecha(fecha);
            captura.setHora(hora);
            captura.setComentario(comentario);
            captura.setFotoUri(fotoUri);

            cursor.close();
        }

        //Ahora obtenemos la ubicación relacionada con esta captura (si existe)
        String ubicacionQuery = "SELECT latitud, longitud FROM Ubicacion WHERE idCaptura = ?";
        Cursor ubicacionCursor = readableDatabase.rawQuery(ubicacionQuery, new String[]{String.valueOf(idCaptura)});

        if (ubicacionCursor != null && ubicacionCursor.moveToFirst()) {
            double latitud = ubicacionCursor.getDouble(ubicacionCursor.getColumnIndexOrThrow("latitud"));
            double longitud = ubicacionCursor.getDouble(ubicacionCursor.getColumnIndexOrThrow("longitud"));

            captura.setLatitud(latitud);
            captura.setLongitud(longitud);

            ubicacionCursor.close();
        }

        //Ahora obtenemos las condiciones relacionadas con esta captura (si existen)
        String condicionesQuery = "SELECT temperatura FROM Condiciones WHERE idCaptura = ?";
        Cursor condicionesCursor = readableDatabase.rawQuery(condicionesQuery, new String[]{String.valueOf(idCaptura)});

        if (condicionesCursor != null && condicionesCursor.moveToFirst()) {
            double temperatura = condicionesCursor.getDouble(condicionesCursor.getColumnIndexOrThrow("temperatura"));

            captura.setTemperatura(temperatura);

            condicionesCursor.close();
        }

        return captura;
    }

    //Método para actualizar la Captura
    public void actualizarCaptura(int idCaptura, String comentario, String fotoUri) {
        SQLiteDatabase writableDatabase = instance.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("comentario", comentario);
        if (fotoUri != null) {
            values.put("foto", fotoUri);
        }

        // Actualizamos la captura por su id
        writableDatabase.update("Captura", values, "id = ?", new String[]{String.valueOf(idCaptura)});
    }
}
