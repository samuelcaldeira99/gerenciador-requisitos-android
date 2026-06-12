package com.exemplo.requisitos;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.FileProvider;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class EdicaoRequisitoActivity extends AppCompatActivity {

    private static final int REQ_CAMERA_1 = 101;
    private static final int REQ_CAMERA_2 = 102;

    private EditText etDescricao, etTempoEstimado;
    private SeekBar sbImportancia, sbDificuldade;
    private TextView tvImportancia, tvDificuldade, tvGps;
    private ImageView ivFoto1, ivFoto2;
    private DatabaseHelper dbHelper;
    private FusedLocationProviderClient fusedClient;
    private Requisito requisito;

    private double latitude = 0, longitude = 0;
    private String pathFoto1 = "", pathFoto2 = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edicao_requisito);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        setTitle("Editar Requisito");
        if (getSupportActionBar() != null)
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        dbHelper    = new DatabaseHelper(this);
        fusedClient = LocationServices.getFusedLocationProviderClient(this);

        etDescricao     = findViewById(R.id.etDescricao);
        etTempoEstimado = findViewById(R.id.etTempoEstimado);
        sbImportancia   = findViewById(R.id.sbImportancia);
        sbDificuldade   = findViewById(R.id.sbDificuldade);
        tvImportancia   = findViewById(R.id.tvImportancia);
        tvDificuldade   = findViewById(R.id.tvDificuldade);
        tvGps           = findViewById(R.id.tvGps);
        ivFoto1         = findViewById(R.id.ivFoto1);
        ivFoto2         = findViewById(R.id.ivFoto2);

        int reqId = getIntent().getIntExtra("REQUISITO_ID", -1);
        requisito = dbHelper.buscarRequisito(reqId);

        if (requisito != null) {
            etDescricao.setText(requisito.getDescricao());
            etTempoEstimado.setText(String.valueOf(requisito.getTempoEstimado()));
            sbImportancia.setMax(4);
            sbImportancia.setProgress(requisito.getNivelImportancia() - 1);
            sbDificuldade.setMax(4);
            sbDificuldade.setProgress(requisito.getNivelDificuldade() - 1);
            tvImportancia.setText("Importancia: " + requisito.getNivelImportancia());
            tvDificuldade.setText("Dificuldade: " + requisito.getNivelDificuldade());
            latitude  = requisito.getLatitude();
            longitude = requisito.getLongitude();
            pathFoto1 = requisito.getFoto1() != null ? requisito.getFoto1() : "";
            pathFoto2 = requisito.getFoto2() != null ? requisito.getFoto2() : "";

            if (latitude != 0 || longitude != 0)
                tvGps.setText(String.format(Locale.getDefault(), "GPS: %.5f, %.5f", latitude, longitude));

            if (!pathFoto1.isEmpty()) { Bitmap b = BitmapFactory.decodeFile(pathFoto1); if (b != null) ivFoto1.setImageBitmap(b); }
            if (!pathFoto2.isEmpty()) { Bitmap b = BitmapFactory.decodeFile(pathFoto2); if (b != null) ivFoto2.setImageBitmap(b); }
        }

        sbImportancia.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            public void onProgressChanged(SeekBar sb, int p, boolean u) { tvImportancia.setText("Importancia: " + (p+1)); }
            public void onStartTrackingTouch(SeekBar sb) {}
            public void onStopTrackingTouch(SeekBar sb) {}
        });
        sbDificuldade.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            public void onProgressChanged(SeekBar sb, int p, boolean u) { tvDificuldade.setText("Dificuldade: " + (p+1)); }
            public void onStartTrackingTouch(SeekBar sb) {}
            public void onStopTrackingTouch(SeekBar sb) {}
        });

        findViewById(R.id.btnSalvar).setOnClickListener(v -> salvarEdicao());
        findViewById(R.id.btnCancelar).setOnClickListener(v -> finish());
        findViewById(R.id.btnGps).setOnClickListener(v -> capturarGps());
        findViewById(R.id.btnFoto1).setOnClickListener(v -> abrirCamera(1));
        findViewById(R.id.btnFoto2).setOnClickListener(v -> abrirCamera(2));
    }

    private void capturarGps() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            Toast.makeText(this, "Permissao de localizacao necessaria", Toast.LENGTH_SHORT).show(); return;
        }
        fusedClient.getCurrentLocation(com.google.android.gms.location.Priority.PRIORITY_HIGH_ACCURACY, null).addOnSuccessListener(this, loc -> {
            if (loc != null) {
                latitude = loc.getLatitude(); longitude = loc.getLongitude();
                tvGps.setText(String.format(Locale.getDefault(), "GPS: %.5f, %.5f", latitude, longitude));
            } else {
                Toast.makeText(this, "Ative o GPS e tente novamente", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void abrirCamera(int num) {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        try {
            File foto = criarArquivoFoto();
            Uri uri = FileProvider.getUriForFile(this, getPackageName() + ".fileprovider", foto);
            if (num == 1) { pathFoto1 = foto.getAbsolutePath(); }
            else          { pathFoto2 = foto.getAbsolutePath(); }
            intent.putExtra(MediaStore.EXTRA_OUTPUT, uri);
            startActivityForResult(intent, num == 1 ? REQ_CAMERA_1 : REQ_CAMERA_2);
        } catch (IOException e) {
            Toast.makeText(this, "Erro ao criar arquivo", Toast.LENGTH_SHORT).show();
        }
    }

    private File criarArquivoFoto() throws IOException {
        String ts = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(new Date());
        return File.createTempFile("FOTO_" + ts, ".jpg", getExternalFilesDir(Environment.DIRECTORY_PICTURES));
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (requestCode == REQ_CAMERA_1) { Bitmap b = BitmapFactory.decodeFile(pathFoto1); if (b != null) ivFoto1.setImageBitmap(b); }
            else if (requestCode == REQ_CAMERA_2) { Bitmap b = BitmapFactory.decodeFile(pathFoto2); if (b != null) ivFoto2.setImageBitmap(b); }
        }
    }

    private void salvarEdicao() {
        String descricao = etDescricao.getText().toString().trim();
        String tempoStr  = etTempoEstimado.getText().toString().trim();
        if (TextUtils.isEmpty(descricao)) { etDescricao.setError("Informe a descricao"); etDescricao.requestFocus(); return; }
        if (TextUtils.isEmpty(tempoStr))  { etTempoEstimado.setError("Informe o tempo"); etTempoEstimado.requestFocus(); return; }
        float tempo;
        try { tempo = Float.parseFloat(tempoStr.replace(",", ".")); }
        catch (NumberFormatException e) { etTempoEstimado.setError("Valor invalido"); return; }

        requisito.setDescricao(descricao);
        requisito.setTempoEstimado(tempo);
        requisito.setNivelImportancia(sbImportancia.getProgress() + 1);
        requisito.setNivelDificuldade(sbDificuldade.getProgress() + 1);
        requisito.setLatitude(latitude);
        requisito.setLongitude(longitude);
        requisito.setFoto1(pathFoto1);
        requisito.setFoto2(pathFoto2);

        int rows = dbHelper.atualizarRequisito(requisito);
        if (rows > 0) { Toast.makeText(this, "Requisito atualizado!", Toast.LENGTH_SHORT).show(); finish(); }
        else          { Toast.makeText(this, "Erro ao atualizar.", Toast.LENGTH_SHORT).show(); }
    }

    @Override
    public boolean onSupportNavigateUp() { finish(); return true; }
}
