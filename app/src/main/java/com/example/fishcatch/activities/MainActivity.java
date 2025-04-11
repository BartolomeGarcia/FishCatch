package com.example.fishcatch.activities;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.example.fishcatch.R;
import com.example.fishcatch.repositories.AdaptadorBaseDeDatos;

public class MainActivity extends AppCompatActivity {
    private AdaptadorBaseDeDatos adaptadorBaseDeDatos;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Instanciación AdaptadorBaseDeDatos
        adaptadorBaseDeDatos=new AdaptadorBaseDeDatos(this);

    }
}
