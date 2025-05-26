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
                "CONSTRAINT FK_CAPTURA_UBICACION FOREIGN KEY (idCaptura) REFERENCES Captura(id) ON DELETE CASCADE)");

        //Crear tabla Condiciones
        db.execSQL("CREATE TABLE Condiciones(" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL," +
                "idCaptura INTEGER," +
                "temperatura REAL," +
                "CONSTRAINT FK_CAPTURA_CONDICIONES FOREIGN KEY (idCaptura) REFERENCES Captura(id) ON DELETE CASCADE)");

        //Inserción de datos Especies
        db.execSQL("INSERT INTO Especie (nombreEspecie) VALUES ('Abadejo')");
        db.execSQL("INSERT INTO Especie (nombreEspecie) VALUES ('Aguja')");
        db.execSQL("INSERT INTO Especie (nombreEspecie) VALUES ('Albacora')");
        db.execSQL("INSERT INTO Especie (nombreEspecie) VALUES ('Alfonsino')");
        db.execSQL("INSERT INTO Especie (nombreEspecie) VALUES ('Anchoa')");
        db.execSQL("INSERT INTO Especie (nombreEspecie) VALUES ('Anguila')");
        db.execSQL("INSERT INTO Especie (nombreEspecie) VALUES ('Anguililla')");
        db.execSQL("INSERT INTO Especie (nombreEspecie) VALUES ('Atún Blanco')");
        db.execSQL("INSERT INTO Especie (nombreEspecie) VALUES ('Atún Rojo')");
        db.execSQL("INSERT INTO Especie (nombreEspecie) VALUES ('Bacalao')");
        db.execSQL("INSERT INTO Especie (nombreEspecie) VALUES ('Bailarina')");
        db.execSQL("INSERT INTO Especie (nombreEspecie) VALUES ('Barbo')");
        db.execSQL("INSERT INTO Especie (nombreEspecie) VALUES ('Besugo')");
        db.execSQL("INSERT INTO Especie (nombreEspecie) VALUES ('Besugo Negro')");
        db.execSQL("INSERT INTO Especie (nombreEspecie) VALUES ('Black Bass')");
        db.execSQL("INSERT INTO Especie (nombreEspecie) VALUES ('Bocaccio')");
        db.execSQL("INSERT INTO Especie (nombreEspecie) VALUES ('Boga')");
        db.execSQL("INSERT INTO Especie (nombreEspecie) VALUES ('Bonito')");
        db.execSQL("INSERT INTO Especie (nombreEspecie) VALUES ('Caballa')");
        db.execSQL("INSERT INTO Especie (nombreEspecie) VALUES ('Caballito de Mar')");
        db.execSQL("INSERT INTO Especie (nombreEspecie) VALUES ('Calamar')");
        db.execSQL("INSERT INTO Especie (nombreEspecie) VALUES ('Cangrejo')");
        db.execSQL("INSERT INTO Especie (nombreEspecie) VALUES ('Caracol de Mar')");
        db.execSQL("INSERT INTO Especie (nombreEspecie) VALUES ('Carpa')");
        db.execSQL("INSERT INTO Especie (nombreEspecie) VALUES ('Cherna')");
        db.execSQL("INSERT INTO Especie (nombreEspecie) VALUES ('Chopa')");
        db.execSQL("INSERT INTO Especie (nombreEspecie) VALUES ('Cigala')");
        db.execSQL("INSERT INTO Especie (nombreEspecie) VALUES ('Congrio')");
        db.execSQL("INSERT INTO Especie (nombreEspecie) VALUES ('Dab')");
        db.execSQL("INSERT INTO Especie (nombreEspecie) VALUES ('Dorada')");
        db.execSQL("INSERT INTO Especie (nombreEspecie) VALUES ('Dorado')");
        db.execSQL("INSERT INTO Especie (nombreEspecie) VALUES ('Eperlano')");
        db.execSQL("INSERT INTO Especie (nombreEspecie) VALUES ('Esturión')");
        db.execSQL("INSERT INTO Especie (nombreEspecie) VALUES ('Fletán')");
        db.execSQL("INSERT INTO Especie (nombreEspecie) VALUES ('Gamba')");
        db.execSQL("INSERT INTO Especie (nombreEspecie) VALUES ('Gallo')");
        db.execSQL("INSERT INTO Especie (nombreEspecie) VALUES ('Garfio')");
        db.execSQL("INSERT INTO Especie (nombreEspecie) VALUES ('Gurami')");
        db.execSQL("INSERT INTO Especie (nombreEspecie) VALUES ('Herrera')");
        db.execSQL("INSERT INTO Especie (nombreEspecie) VALUES ('Jara')");
        db.execSQL("INSERT INTO Especie (nombreEspecie) VALUES ('Jurel')");
        db.execSQL("INSERT INTO Especie (nombreEspecie) VALUES ('Koi')");
        db.execSQL("INSERT INTO Especie (nombreEspecie) VALUES ('Langosta')");
        db.execSQL("INSERT INTO Especie (nombreEspecie) VALUES ('Lenguado')");
        db.execSQL("INSERT INTO Especie (nombreEspecie) VALUES ('Lenguado Negro')");
        db.execSQL("INSERT INTO Especie (nombreEspecie) VALUES ('Lucioperca')");
        db.execSQL("INSERT INTO Especie (nombreEspecie) VALUES ('Lubina')");
        db.execSQL("INSERT INTO Especie (nombreEspecie) VALUES ('Lucio')");
        db.execSQL("INSERT INTO Especie (nombreEspecie) VALUES ('Mackerel')");
        db.execSQL("INSERT INTO Especie (nombreEspecie) VALUES ('Marlin')");
        db.execSQL("INSERT INTO Especie (nombreEspecie) VALUES ('Mero')");
        db.execSQL("INSERT INTO Especie (nombreEspecie) VALUES ('Merluza')");
        db.execSQL("INSERT INTO Especie (nombreEspecie) VALUES ('Napoleón')");
        db.execSQL("INSERT INTO Especie (nombreEspecie) VALUES ('Ostrón')");
        db.execSQL("INSERT INTO Especie (nombreEspecie) VALUES ('Palometa')");
        db.execSQL("INSERT INTO Especie (nombreEspecie) VALUES ('Palometa Negra')");
        db.execSQL("INSERT INTO Especie (nombreEspecie) VALUES ('Pejerrey')");
        db.execSQL("INSERT INTO Especie (nombreEspecie) VALUES ('Perca')");
        db.execSQL("INSERT INTO Especie (nombreEspecie) VALUES ('Pez Ángel')");
        db.execSQL("INSERT INTO Especie (nombreEspecie) VALUES ('Pez Globo')");
        db.execSQL("INSERT INTO Especie (nombreEspecie) VALUES ('Pez León')");
        db.execSQL("INSERT INTO Especie (nombreEspecie) VALUES ('Pez Mandarín')");
        db.execSQL("INSERT INTO Especie (nombreEspecie) VALUES ('Pez Payaso')");
        db.execSQL("INSERT INTO Especie (nombreEspecie) VALUES ('Pez Sierra')");
        db.execSQL("INSERT INTO Especie (nombreEspecie) VALUES ('Pez Espada')");
        db.execSQL("INSERT INTO Especie (nombreEspecie) VALUES ('Pez Trompeta')");
        db.execSQL("INSERT INTO Especie (nombreEspecie) VALUES ('Piraña')");
        db.execSQL("INSERT INTO Especie (nombreEspecie) VALUES ('Raya')");
        db.execSQL("INSERT INTO Especie (nombreEspecie) VALUES ('Robalo')");
        db.execSQL("INSERT INTO Especie (nombreEspecie) VALUES ('Rodaballo')");
        db.execSQL("INSERT INTO Especie (nombreEspecie) VALUES ('Salmon Chinook')");
        db.execSQL("INSERT INTO Especie (nombreEspecie) VALUES ('Salmon Coho')");
        db.execSQL("INSERT INTO Especie (nombreEspecie) VALUES ('Salmonete')");
        db.execSQL("INSERT INTO Especie (nombreEspecie) VALUES ('Salmón')");
        db.execSQL("INSERT INTO Especie (nombreEspecie) VALUES ('Salmón Atlántico')");
        db.execSQL("INSERT INTO Especie (nombreEspecie) VALUES ('Sargo')");
        db.execSQL("INSERT INTO Especie (nombreEspecie) VALUES ('Sargo Real')");
        db.execSQL("INSERT INTO Especie (nombreEspecie) VALUES ('Sargo Verdoso')");
        db.execSQL("INSERT INTO Especie (nombreEspecie) VALUES ('Sardina')");
        db.execSQL("INSERT INTO Especie (nombreEspecie) VALUES ('Siluro')");
        db.execSQL("INSERT INTO Especie (nombreEspecie) VALUES ('Tiburón Ballena')");
        db.execSQL("INSERT INTO Especie (nombreEspecie) VALUES ('Tiburón Martillo')");
        db.execSQL("INSERT INTO Especie (nombreEspecie) VALUES ('Tiburón Toro')");
        db.execSQL("INSERT INTO Especie (nombreEspecie) VALUES ('Tilapia')");
        db.execSQL("INSERT INTO Especie (nombreEspecie) VALUES ('Trucha')");
        db.execSQL("INSERT INTO Especie (nombreEspecie) VALUES ('Tucunaré')");
        db.execSQL("INSERT INTO Especie (nombreEspecie) VALUES ('Tuna')");
        db.execSQL("INSERT INTO Especie (nombreEspecie) VALUES ('Verdel')");
        db.execSQL("INSERT INTO Especie (nombreEspecie) VALUES ('Zarzuela')");
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

    //Para habilitar las claves foráneas en SQLite
    @Override
    public void onConfigure(SQLiteDatabase db) {
        super.onConfigure(db);
        db.execSQL("PRAGMA foreign_keys=ON;");
    }
}
