package com.exemplo.requisitos;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
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

public class ListaProjetosActivity extends AppCompatActivity {

    private DatabaseHelper dbHelper;
    private ProjetoAdapter adapter;
    private TextView tvVazio;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lista_projetos);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        setTitle("Projetos Cadastrados");
        if (getSupportActionBar() != null)
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        dbHelper = new DatabaseHelper(this);
        tvVazio  = findViewById(R.id.tvVazio);

        RecyclerView rv = findViewById(R.id.recyclerView);
        rv.setLayoutManager(new LinearLayoutManager(this));
        adapter = new ProjetoAdapter(this, null,
            projeto -> {
                Intent i = new Intent(this, ListaRequisitosActivity.class);
                i.putExtra("PROJETO_ID",   projeto.getId());
                i.putExtra("PROJETO_NOME", projeto.getNome());
                startActivity(i);
            },
            projeto -> {
                Intent i = new Intent(this, EdicaoProjetoActivity.class);
                i.putExtra("PROJETO_ID", projeto.getId());
                startActivity(i);
            },
            projeto -> confirmarExclusaoProjeto(projeto),
            projeto -> abrirWebView(projeto)
        );
        rv.setAdapter(adapter);
        carregarProjetos();
    }

    private void abrirWebView(Projeto projeto) {
        String link = projeto.getLinkDocumentacao();
        if (TextUtils.isEmpty(link)) {
            Toast.makeText(this, "Este projeto nao possui link de documentacao.", Toast.LENGTH_SHORT).show();
            return;
        }
        Intent i = new Intent(this, WebViewActivity.class);
        i.putExtra("URL", link);
        i.putExtra("NOME_PROJETO", projeto.getNome());
        startActivity(i);
    }

    private void confirmarExclusaoProjeto(Projeto projeto) {
        new AlertDialog.Builder(this)
            .setTitle("Excluir Projeto")
            .setMessage("Deseja excluir o projeto \"" + projeto.getNome() + "\"?\n\nTodos os requisitos vinculados tambem serao excluidos.")
            .setPositiveButton("Excluir", (dialog, which) -> {
                int rows = dbHelper.excluirProjeto(projeto.getId());
                if (rows > 0) {
                    Toast.makeText(this, "Projeto excluido!", Toast.LENGTH_SHORT).show();
                    carregarProjetos();
                } else {
                    Toast.makeText(this, "Erro ao excluir.", Toast.LENGTH_SHORT).show();
                }
            })
            .setNegativeButton("Cancelar", null)
            .show();
    }

    @Override
    protected void onResume() { super.onResume(); carregarProjetos(); }

    private void carregarProjetos() {
        List<Projeto> lista = dbHelper.listarProjetos();
        adapter.setLista(lista);
        tvVazio.setVisibility(lista.isEmpty() ? View.VISIBLE : View.GONE);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) { getMenuInflater().inflate(R.menu.menu_principal, menu); return true; }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.menu_projetos) return true;
        if (id == R.id.menu_novo_projeto) { startActivity(new Intent(this, CadastroProjetoActivity.class)); return true; }
        if (id == R.id.menu_logout) {
            Intent i = new Intent(this, LoginActivity.class);
            i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(i); return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onSupportNavigateUp() { finish(); return true; }
}
