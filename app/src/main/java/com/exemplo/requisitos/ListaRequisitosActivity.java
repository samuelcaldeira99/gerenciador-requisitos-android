package com.exemplo.requisitos;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class ListaRequisitosActivity extends AppCompatActivity {

    private DatabaseHelper dbHelper;
    private RequisitoAdapter adapter;
    private TextView tvVazio;
    private int projetoId;
    private String projetoNome;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lista_requisitos);

        projetoId   = getIntent().getIntExtra("PROJETO_ID", -1);
        projetoNome = getIntent().getStringExtra("PROJETO_NOME");

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        setTitle("Requisitos: " + projetoNome);
        if (getSupportActionBar() != null)
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        dbHelper = new DatabaseHelper(this);
        tvVazio  = findViewById(R.id.tvVazio);

        RecyclerView rv = findViewById(R.id.recyclerView);
        rv.setLayoutManager(new LinearLayoutManager(this));
        adapter = new RequisitoAdapter(this, null,
            requisito -> {
                Intent i = new Intent(this, EdicaoRequisitoActivity.class);
                i.putExtra("REQUISITO_ID", requisito.getId());
                startActivity(i);
            },
            requisito -> confirmarExclusaoRequisito(requisito)
        );
        rv.setAdapter(adapter);

        findViewById(R.id.btnNovoRequisito).setOnClickListener(v -> {
            Intent i = new Intent(this, CadastroRequisitoActivity.class);
            i.putExtra("PROJETO_ID",   projetoId);
            i.putExtra("PROJETO_NOME", projetoNome);
            startActivity(i);
        });

        carregarRequisitos();
    }

    private void confirmarExclusaoRequisito(Requisito requisito) {
        new AlertDialog.Builder(this)
            .setTitle("Excluir Requisito")
            .setMessage("Deseja excluir o requisito?\n\n\"" + requisito.getDescricao() + "\"")
            .setPositiveButton("Excluir", (dialog, which) -> {
                int rows = dbHelper.excluirRequisito(requisito.getId());
                if (rows > 0) {
                    Toast.makeText(this, "Requisito excluido!", Toast.LENGTH_SHORT).show();
                    carregarRequisitos();
                } else {
                    Toast.makeText(this, "Erro ao excluir.", Toast.LENGTH_SHORT).show();
                }
            })
            .setNegativeButton("Cancelar", null)
            .show();
    }

    @Override
    protected void onResume() { super.onResume(); carregarRequisitos(); }

    private void carregarRequisitos() {
        List<Requisito> lista = dbHelper.listarRequisitosPorProjeto(projetoId);
        adapter.setLista(lista);
        tvVazio.setVisibility(lista.isEmpty() ? View.VISIBLE : View.GONE);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) { getMenuInflater().inflate(R.menu.menu_principal, menu); return true; }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.menu_projetos) { startActivity(new Intent(this, ListaProjetosActivity.class)); return true; }
        if (id == R.id.menu_novo_projeto) { startActivity(new Intent(this, CadastroProjetoActivity.class)); return true; }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onSupportNavigateUp() { finish(); return true; }
}
