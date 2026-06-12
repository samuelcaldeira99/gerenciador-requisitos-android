package com.exemplo.requisitos;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import java.util.Calendar;

public class CadastroProjetoActivity extends AppCompatActivity {

    private EditText etNome, etDataInicio, etDataEntrega, etLink;
    private DatabaseHelper dbHelper;
    private final Calendar calendario = Calendar.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cadastro_projeto);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        setTitle("Cadastro de Projeto");
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

        Button btnSalvar = findViewById(R.id.btnSalvar);
        btnSalvar.setOnClickListener(v -> salvarProjeto());
    }

    private void mostrarDatePicker(EditText campo) {
        int ano = calendario.get(Calendar.YEAR);
        int mes = calendario.get(Calendar.MONTH);
        int dia = calendario.get(Calendar.DAY_OF_MONTH);
        new DatePickerDialog(this, (view, y, m, d) ->
                campo.setText(String.format("%02d/%02d/%04d", d, m+1, y)),
                ano, mes, dia).show();
    }

    private void salvarProjeto() {
        String nome        = etNome.getText().toString().trim();
        String dataInicio  = etDataInicio.getText().toString().trim();
        String dataEntrega = etDataEntrega.getText().toString().trim();
        String link        = etLink.getText().toString().trim();

        if (TextUtils.isEmpty(nome)) {
            etNome.setError("Informe o nome"); etNome.requestFocus(); return;
        }
        if (TextUtils.isEmpty(dataInicio)) {
            etDataInicio.setError("Informe a data de inicio"); return;
        }
        if (TextUtils.isEmpty(dataEntrega)) {
            etDataEntrega.setError("Informe a data de entrega"); return;
        }

        Projeto p = new Projeto(nome, dataInicio, dataEntrega, link);
        long id = dbHelper.inserirProjeto(p);

        if (id != -1) {
            Toast.makeText(this, "Projeto salvo com sucesso!", Toast.LENGTH_SHORT).show();
            etNome.setText(""); etDataInicio.setText(""); etDataEntrega.setText(""); etLink.setText("");
        } else {
            Toast.makeText(this, "Erro ao salvar projeto.", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public boolean onSupportNavigateUp() { finish(); return true; }
}
