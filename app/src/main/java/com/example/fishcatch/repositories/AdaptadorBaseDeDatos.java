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

    public void insertarPlantacion(String nombrePlanta, Integer numeroPlantas, String grupoDeClase, Integer tipoDePlanta) {    //Devuelve el identificador que recibe el nuevo registro al ser insertado (ya sea conclave de tipo autonumérico o no)
        SQLiteDatabase writableDatabase = instance.getWritableDatabase();
        String queryDarAltaPlantacion = "INSERT INTO PLANTACION (NOMBREPLANTA,NUMPLANTAS,GRUPOCLASE,IDTIPO) VALUES (?,?,?,?)";
        writableDatabase.execSQL(queryDarAltaPlantacion, new Object[]{nombrePlanta, numeroPlantas, grupoDeClase, tipoDePlanta});
        Toast.makeText(contexto, "La plantación " + nombrePlanta + " se ha insertado correctamente", Toast.LENGTH_SHORT).show();
    }

    public Cursor consultarPlantacion(String idPlantacion) {
        Cursor cursor = null;
        try {
            // Abrir la base de datos en modo lectura
            SQLiteDatabase readableDatabase = instance.getReadableDatabase();
            String query = "SELECT * FROM plantacion WHERE idPlantacion=?";
            cursor = readableDatabase.rawQuery(query, new String[]{idPlantacion});

            if (cursor != null && cursor.moveToFirst()) {
                return cursor; // Devuelve el cursor si tiene datos
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        // No cierres el cursor aquí si lo estás devolviendo
        return null;
    }

    public void modificarDatosPlantacion(int idPlantacionAModificar, String nombrePlantacion, Integer numeroPlantas, String grupoClase, Integer idTipoPlanta) {
        SQLiteDatabase writableDatabase = instance.getWritableDatabase();
        try {
            String query = "UPDATE plantacion SET nombrePlanta = ?, numPlantas = ?, grupoClase=?,idTipo=? WHERE idPlantacion = ?";
            writableDatabase.execSQL(query, new Object[]{nombrePlantacion, numeroPlantas, grupoClase, idTipoPlanta, idPlantacionAModificar});
            Toast.makeText(contexto, "Los datos de la plantación se han modificado correctamente", Toast.LENGTH_SHORT).show();
        } catch (Exception e) {
            Toast.makeText(contexto, "Error al modificar los datos", Toast.LENGTH_SHORT).show();
        }
    }

    public int mostrarPlantacionesPorTipo(String nombreTipo) {
        SQLiteDatabase readableDatabase = instance.getReadableDatabase();
        Cursor cursor = readableDatabase.rawQuery(
                "SELECT SUM(numPlantas) FROM plantacion WHERE idTipo=(SELECT idTipo FROM tipoPlanta WHERE nombreTipo LIKE ?)",
                new String[]{nombreTipo}
        );
        if(cursor!=null && cursor.moveToFirst()){
            return cursor.getInt(0);
        }else{
            return 0;
        }
    }
}
