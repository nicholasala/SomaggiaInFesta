package com.example.somaggiainfesta;

import android.content.Intent;
import android.os.Build;
import android.provider.Settings;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {
    private Button kitchen;
    private Button cashDesk;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        kitchen = findViewById(R.id.cucina);
        cashDesk = findViewById(R.id.cassa);

        kitchen.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.M)
            @Override
            public void onClick(View v) {
                //permissions control
                if(!Settings.System.canWrite(MainActivity.this))
                    Toast.makeText(MainActivity.this, "Permessi di scrittura richiesti", Toast.LENGTH_SHORT).show();
                else
                    startActivity(new Intent(MainActivity.this, Kitchen.class));

            }
        });

        cashDesk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) { startActivity(new Intent(MainActivity.this, CashDesk.class)); }
        });
    }
}
