package com.exemplo.requisitos;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        setTitle("Gerenciador de Requisitos");

        Button btnProjeto = findViewById(R.id.btnCadastroProjeto);
        Button btnListar  = findViewById(R.id.btnListarProjetos);

        btnProjeto.setOnClickListener(v ->
            startActivity(new Intent(this, CadastroProjetoActivity.class)));
        btnListar.setOnClickListener(v ->
            startActivity(new Intent(this, ListaProjetosActivity.class)));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_principal, menu); return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.menu_projetos) { startActivity(new Intent(this, ListaProjetosActivity.class)); return true; }
        if (id == R.id.menu_novo_projeto) { startActivity(new Intent(this, CadastroProjetoActivity.class)); return true; }
        if (id == R.id.menu_logout) {
            Intent i = new Intent(this, LoginActivity.class);
            i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(i);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
