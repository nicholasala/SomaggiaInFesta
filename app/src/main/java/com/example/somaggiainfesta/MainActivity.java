package com.example.somaggiainfesta;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

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
            @Override
            public void onClick(View v) { startActivity(new Intent(MainActivity.this, Kitchen.class)); }
        });

        cashDesk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) { startActivity(new Intent(MainActivity.this, CashDesk.class)); }
        });
    }
}

//TODO: refactor dell'applicazione, l'ip della cucina non dovrà più essere statico, findkitchen dovrà quindi aprire una connessione con le macchine trovate nella
//TODO rete e chiedere a loro se sono una cucina. Quindi scansione di rete chiedendo ad ogni ip attivo (tranne router e broadcast).

//TODO IMPORTANTE E GENERALE: al blocco del dispositivo cucina le connessioni vanno chiuse e prima della loro chiusura bisogna inviare ad ogni cassa un messaggio
// inerente e coerente alla chiusura, per esempio Keys.MessageCode.kicthenBlocked;

//TODO IMPORTANTE E GENERALE: al blocco del dispositivo cassa la connessione dev'essere chiusa e quindi la cucina avvisata Keys.MessageCode.cashDeskBlocked, per poi essere riaperta
