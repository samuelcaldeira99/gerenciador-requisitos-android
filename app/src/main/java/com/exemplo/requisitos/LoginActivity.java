package com.exemplo.requisitos;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class LoginActivity extends AppCompatActivity {

    private EditText etEmail, etSenha;
    private DatabaseHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        dbHelper = new DatabaseHelper(this);
        etEmail  = findViewById(R.id.etEmail);
        etSenha  = findViewById(R.id.etSenha);

        Button btnEntrar    = findViewById(R.id.btnEntrar);
        TextView tvCadastro = findViewById(R.id.tvCadastro);

        btnEntrar.setOnClickListener(v -> realizarLogin());
        tvCadastro.setOnClickListener(v ->
            startActivity(new Intent(this, CadastroUsuarioActivity.class)));
    }

    private void realizarLogin() {
        String email = etEmail.getText().toString().trim();
        String senha = etSenha.getText().toString().trim();

        if (TextUtils.isEmpty(email)) {
            etEmail.setError("Informe o email"); etEmail.requestFocus(); return;
        }
        if (TextUtils.isEmpty(senha)) {
            etSenha.setError("Informe a senha"); etSenha.requestFocus(); return;
        }

        if (dbHelper.autenticarUsuario(email, senha)) {
            Toast.makeText(this, "Login realizado com sucesso!", Toast.LENGTH_SHORT).show();
            Intent i = new Intent(this, MainActivity.class);
            i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(i);
        } else {
            Toast.makeText(this, "Email ou senha incorretos.", Toast.LENGTH_LONG).show();
        }
    }
}
