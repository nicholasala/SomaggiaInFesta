package it.somaggia.somaggiainfesta;

import android.content.Intent;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button kitchen = findViewById(R.id.cucina);
        Button cashDesk = findViewById(R.id.cassa);

        kitchen.setOnClickListener(v -> startActivity(new Intent(MainActivity.this, Kitchen.class)));

        cashDesk.setOnClickListener(v -> startActivity(new Intent(MainActivity.this, CashDesk.class)));
    }
}

//TODO: refactor dell'applicazione, l'ip della cucina non dovrà più essere statico, findkitchen dovrà quindi aprire una connessione con le macchine trovate nella
//TODO rete e chiedere a loro se sono una cucina. Quindi scansione di rete chiedendo ad ogni ip attivo (tranne router e broadcast).

//TODO IMPORTANTE E GENERALE: al blocco del dispositivo cucina le connessioni vanno chiuse e prima della loro chiusura bisogna inviare ad ogni cassa un messaggio
// inerente e coerente alla chiusura, per esempio Keys.MessageCode.kicthenBlocked;

//TODO IMPORTANTE E GENERALE: al blocco del dispositivo cassa la connessione dev'essere chiusa e quindi la cucina avvisata Keys.MessageCode.cashDeskBlocked, per poi essere riaperta
