package com.example.fishcatch.repositories;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class BaseDeDatosSingleton extends SQLiteOpenHelper {
    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "FishCatch";
    private static BaseDeDatosSingleton dbOpen;

    /**
     * Constructor privado
     */
    private BaseDeDatosSingleton(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        dbOpen = this;
    }

    /**
     * Obtener una instancia, patrón Singleton
     */
    public static BaseDeDatosSingleton getInstance(Context context) {
        if (dbOpen == null) {
            new BaseDeDatosSingleton(context);
        }
        return dbOpen;
    }

    /**
     * Llamado de forma automática cuando se accede por primera
     * vez.
     * Incluir aquí las creaciones de tablas e inicializaciones
     */
    @Override
    public void onCreate(SQLiteDatabase db) {
        //Crear tabla Especie
        db.execSQL("CREATE TABLE Especie(" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL," +
                "nombreEspecie VARCHAR(250) NOT NULL) ");

        //Crear tabla Captura
        db.execSQL("CREATE TABLE Captura(" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL," +
                "idEspecie INTEGER NOT NULL," +
                "peso REAL NOT NULL," +
                "tamanno REAL," +
                "fecha TEXT NOT NULL," +
                "hora TEXT NOT NULL," +
                "comentario TEXT," +
                "foto TEXT," +
                "CONSTRAINT FK_ESPECIE FOREIGN KEY (idEspecie) REFERENCES Especie(id))");

        //Crear tabla Ubicacion
        db.execSQL("CREATE TABLE Ubicacion(" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL," +
                "idCaptura INTEGER," +
                "latitud REAL," +
                "longitud REAL," +
                "CONSTRAINT FK_CAPTURA_UBICACION FOREIGN KEY (idCaptura) REFERENCES Captura(id))");

        //Crear tabla Condiciones
        db.execSQL("CREATE TABLE Condiciones(" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL," +
                "idCaptura INTEGER," +
                "temperatura REAL," +
                "CONSTRAINT FK_CAPTURA_CONDICIONES FOREIGN KEY (idCaptura) REFERENCES Captura(id))");

        //Inserción de datos Especies
        db.execSQL("INSERT INTO Especie (nombreEspecie) VALUES ('Trucha')");
        db.execSQL("INSERT INTO Especie (nombreEspecie) VALUES ('Carpa')");
        db.execSQL("INSERT INTO Especie (nombreEspecie) VALUES ('Lucio')");
        db.execSQL("INSERT INTO Especie (nombreEspecie) VALUES ('Black Bass')");
        db.execSQL("INSERT INTO Especie (nombreEspecie) VALUES ('Barbo')");
        db.execSQL("INSERT INTO Especie (nombreEspecie) VALUES ('Salmón')");
        db.execSQL("INSERT INTO Especie (nombreEspecie) VALUES ('Lubina')");
        db.execSQL("INSERT INTO Especie (nombreEspecie) VALUES ('Mero')");
        db.execSQL("INSERT INTO Especie (nombreEspecie) VALUES ('Dorada')");
        db.execSQL("INSERT INTO Especie (nombreEspecie) VALUES ('Atún Rojo')");
        db.execSQL("INSERT INTO Especie (nombreEspecie) VALUES ('Jurel')");
        db.execSQL("INSERT INTO Especie (nombreEspecie) VALUES ('Merluza')");
        db.execSQL("INSERT INTO Especie (nombreEspecie) VALUES ('Sargo')");
        db.execSQL("INSERT INTO Especie (nombreEspecie) VALUES ('Robalo')");
        db.execSQL("INSERT INTO Especie (nombreEspecie) VALUES ('Boga')");
    }

    /**
     * Llamado de forma automática cuando se actualiza la
     * versión.
     * Incluir aquí las alteraciones de tablas y datos
     */
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVer, int
            newVer) {
    }
}
