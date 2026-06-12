package com.exemplo.requisitos;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import java.util.Calendar;

public class EdicaoProjetoActivity extends AppCompatActivity {

    private EditText etNome, etDataInicio, etDataEntrega, etLink;
    private DatabaseHelper dbHelper;
    private Projeto projeto;
    private final Calendar calendario = Calendar.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edicao_projeto);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        setTitle("Editar Projeto");
        if (getSupportActionBar() != null)
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        dbHelper      = new DatabaseHelper(this);
        etNome        = findViewById(R.id.etNome);
        etDataInicio  = findViewById(R.id.etDataInicio);
        etDataEntrega = findViewById(R.id.etDataEntrega);
        etLink        = findViewById(R.id.etLink);

        etDataInicio.setFocusable(false);
        etDataInicio.setOnClickListener(v -> mostrarDatePicker(etDataInicio));
        etDataEntrega.setFocusable(false);
        etDataEntrega.setOnClickListener(v -> mostrarDatePicker(etDataEntrega));

        int projetoId = getIntent().getIntExtra("PROJETO_ID", -1);
        projeto = dbHelper.buscarProjeto(projetoId);
        if (projeto != null) {
            etNome.setText(projeto.getNome());
            etDataInicio.setText(projeto.getDataInicio());
            etDataEntrega.setText(projeto.getDataEntrega());
            etLink.setText(projeto.getLinkDocumentacao());
        }

        Button btnSalvar   = findViewById(R.id.btnSalvar);
        Button btnCancelar = findViewById(R.id.btnCancelar);
        Button btnWebView  = findViewById(R.id.btnWebView);

        btnSalvar.setOnClickListener(v -> salvarEdicao());
        btnCancelar.setOnClickListener(v -> finish());
        btnWebView.setOnClickListener(v -> abrirWebView());
    }

    private void mostrarDatePicker(EditText campo) {
        int ano = calendario.get(Calendar.YEAR);
        int mes = calendario.get(Calendar.MONTH);
        int dia = calendario.get(Calendar.DAY_OF_MONTH);
        new DatePickerDialog(this, (view, y, m, d) ->
                campo.setText(String.format("%02d/%02d/%04d", d, m+1, y)),
                ano, mes, dia).show();
    }

    private void abrirWebView() {
        String link = etLink.getText().toString().trim();
        if (TextUtils.isEmpty(link)) {
            Toast.makeText(this, "Informe um link de documentacao primeiro.", Toast.LENGTH_SHORT).show();
            return;
        }
        Intent i = new Intent(this, WebViewActivity.class);
        i.putExtra("URL", link);
        i.putExtra("NOME_PROJETO", etNome.getText().toString().trim());
        startActivity(i);
    }

    private void salvarEdicao() {
        String nome        = etNome.getText().toString().trim();
        String dataInicio  = etDataInicio.getText().toString().trim();
        String dataEntrega = etDataEntrega.getText().toString().trim();
        String link        = etLink.getText().toString().trim();

        if (TextUtils.isEmpty(nome)) { etNome.setError("Informe o nome"); etNome.requestFocus(); return; }
        if (TextUtils.isEmpty(dataInicio)) { etDataInicio.setError("Informe a data"); return; }
        if (TextUtils.isEmpty(dataEntrega)) { etDataEntrega.setError("Informe a data"); return; }

        projeto.setNome(nome);
        projeto.setDataInicio(dataInicio);
        projeto.setDataEntrega(dataEntrega);
        projeto.setLinkDocumentacao(link);

        int rows = dbHelper.atualizarProjeto(projeto);
        if (rows > 0) {
            Toast.makeText(this, "Projeto atualizado!", Toast.LENGTH_SHORT).show();
            finish();
        } else {
            Toast.makeText(this, "Erro ao atualizar.", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public boolean onSupportNavigateUp() { finish(); return true; }
}
