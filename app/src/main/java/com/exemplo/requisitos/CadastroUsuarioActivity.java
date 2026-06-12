package com.exemplo.requisitos;

import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

public class CadastroUsuarioActivity extends AppCompatActivity {

    private EditText etNome, etEmail, etSenha, etConfirmarSenha;
    private DatabaseHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cadastro_usuario);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        setTitle("Cadastro de Usuario");
        if (getSupportActionBar() != null)
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        dbHelper         = new DatabaseHelper(this);
        etNome           = findViewById(R.id.etNome);
        etEmail          = findViewById(R.id.etEmail);
        etSenha          = findViewById(R.id.etSenha);
        etConfirmarSenha = findViewById(R.id.etConfirmarSenha);

        Button btnCadastrar = findViewById(R.id.btnCadastrar);
        btnCadastrar.setOnClickListener(v -> cadastrarUsuario());
    }

    private void cadastrarUsuario() {
        String nome    = etNome.getText().toString().trim();
        String email   = etEmail.getText().toString().trim();
        String senha   = etSenha.getText().toString().trim();
        String confirma = etConfirmarSenha.getText().toString().trim();

        if (TextUtils.isEmpty(nome)) {
            etNome.setError("Informe o nome"); etNome.requestFocus(); return;
        }
        if (TextUtils.isEmpty(email)) {
            etEmail.setError("Informe o email"); etEmail.requestFocus(); return;
        }
        if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            etEmail.setError("Email invalido"); etEmail.requestFocus(); return;
        }
        if (TextUtils.isEmpty(senha)) {
            etSenha.setError("Informe a senha"); etSenha.requestFocus(); return;
        }
        if (senha.length() < 6) {
            etSenha.setError("Senha deve ter no minimo 6 caracteres"); etSenha.requestFocus(); return;
        }
        if (!senha.equals(confirma)) {
            etConfirmarSenha.setError("Senhas nao conferem"); etConfirmarSenha.requestFocus(); return;
        }
        if (dbHelper.emailJaCadastrado(email)) {
            etEmail.setError("Email ja cadastrado"); etEmail.requestFocus(); return;
        }

        long id = dbHelper.inserirUsuario(nome, email, senha);
        if (id != -1) {
            Toast.makeText(this, "Usuario cadastrado com sucesso!", Toast.LENGTH_SHORT).show();
            finish();
        } else {
            Toast.makeText(this, "Erro ao cadastrar usuario.", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public boolean onSupportNavigateUp() { finish(); return true; }
}
