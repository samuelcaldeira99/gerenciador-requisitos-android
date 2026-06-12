package com.exemplo.requisitos;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
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

public class CadastroRequisitoActivity extends AppCompatActivity {

    private static final int REQ_CAMERA_1    = 101;
    private static final int REQ_CAMERA_2    = 102;
    private static final int REQ_PERMISSOES  = 200;

    private EditText etDescricao, etTempoEstimado;
    private SeekBar sbImportancia, sbDificuldade;
    private TextView tvImportancia, tvDificuldade, tvDataRegistro, tvGps;
    private ImageView ivFoto1, ivFoto2;
    private DatabaseHelper dbHelper;
    private FusedLocationProviderClient fusedClient;

    private int projetoId = -1;
    private double latitude = 0, longitude = 0;
    private String pathFoto1 = "", pathFoto2 = "";
    private Uri uriFoto1, uriFoto2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cadastro_requisito);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        setTitle("Cadastro de Requisito");
        if (getSupportActionBar() != null)
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        dbHelper     = new DatabaseHelper(this);
        fusedClient  = LocationServices.getFusedLocationProviderClient(this);

        etDescricao     = findViewById(R.id.etDescricao);
        etTempoEstimado = findViewById(R.id.etTempoEstimado);
        sbImportancia   = findViewById(R.id.sbImportancia);
        sbDificuldade   = findViewById(R.id.sbDificuldade);
        tvImportancia   = findViewById(R.id.tvImportancia);
        tvDificuldade   = findViewById(R.id.tvDificuldade);
        tvDataRegistro  = findViewById(R.id.tvDataRegistro);
        tvGps           = findViewById(R.id.tvGps);
        ivFoto1         = findViewById(R.id.ivFoto1);
        ivFoto2         = findViewById(R.id.ivFoto2);

        if (getIntent().hasExtra("PROJETO_ID")) {
            projetoId = getIntent().getIntExtra("PROJETO_ID", -1);
            String nome = getIntent().getStringExtra("PROJETO_NOME");
            setTitle("Requisito: " + nome);
        }

        String agora = new SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault()).format(new Date());
        tvDataRegistro.setText("Registrado em: " + agora);

        sbImportancia.setMax(4); sbImportancia.setProgress(2);
        tvImportancia.setText("Importancia: 3");
        sbImportancia.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            public void onProgressChanged(SeekBar sb, int p, boolean u) { tvImportancia.setText("Importancia: " + (p+1)); }
            public void onStartTrackingTouch(SeekBar sb) {}
            public void onStopTrackingTouch(SeekBar sb) {}
        });

        sbDificuldade.setMax(4); sbDificuldade.setProgress(2);
        tvDificuldade.setText("Dificuldade: 3");
        sbDificuldade.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            public void onProgressChanged(SeekBar sb, int p, boolean u) { tvDificuldade.setText("Dificuldade: " + (p+1)); }
            public void onStartTrackingTouch(SeekBar sb) {}
            public void onStopTrackingTouch(SeekBar sb) {}
        });

        Button btnSalvar  = findViewById(R.id.btnSalvar);
        Button btnLimpar  = findViewById(R.id.btnLimpar);
        Button btnGps     = findViewById(R.id.btnGps);
        Button btnFoto1   = findViewById(R.id.btnFoto1);
        Button btnFoto2   = findViewById(R.id.btnFoto2);

        btnSalvar.setOnClickListener(v -> salvarRequisito());
        btnLimpar.setOnClickListener(v -> limparCampos());
        btnGps.setOnClickListener(v -> capturarGps());
        btnFoto1.setOnClickListener(v -> abrirCamera(1));
        btnFoto2.setOnClickListener(v -> abrirCamera(2));

        solicitarPermissoes();
    }

    private void solicitarPermissoes() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED ||
            ActivityCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{
                Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.CAMERA
            }, REQ_PERMISSOES);
        }
    }

    private void capturarGps() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            Toast.makeText(this, "Permissao de localizacao necessaria", Toast.LENGTH_SHORT).show();
            return;
        }
        fusedClient.getCurrentLocation(com.google.android.gms.location.Priority.PRIORITY_HIGH_ACCURACY, null).addOnSuccessListener(this, location -> {
            if (location != null) {
                latitude  = location.getLatitude();
                longitude = location.getLongitude();
                tvGps.setText(String.format(Locale.getDefault(), "GPS: %.5f, %.5f", latitude, longitude));
            } else {
                Toast.makeText(this, "Nao foi possivel obter localizacao. Ative o GPS.", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void abrirCamera(int numeroFoto) {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        try {
            File foto = criarArquivoFoto();
            Uri uri = FileProvider.getUriForFile(this, getPackageName() + ".fileprovider", foto);
            if (numeroFoto == 1) { uriFoto1 = uri; pathFoto1 = foto.getAbsolutePath(); }
            else                 { uriFoto2 = uri; pathFoto2 = foto.getAbsolutePath(); }
            intent.putExtra(MediaStore.EXTRA_OUTPUT, uri);
            startActivityForResult(intent, numeroFoto == 1 ? REQ_CAMERA_1 : REQ_CAMERA_2);
        } catch (IOException e) {
            Toast.makeText(this, "Erro ao criar arquivo de foto", Toast.LENGTH_SHORT).show();
        }
    }

    private File criarArquivoFoto() throws IOException {
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(new Date());
        File dir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        return File.createTempFile("FOTO_" + timeStamp, ".jpg", dir);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (requestCode == REQ_CAMERA_1 && pathFoto1 != null) {
                Bitmap bmp = BitmapFactory.decodeFile(pathFoto1);
                if (bmp != null) ivFoto1.setImageBitmap(bmp);
            } else if (requestCode == REQ_CAMERA_2 && pathFoto2 != null) {
                Bitmap bmp = BitmapFactory.decodeFile(pathFoto2);
                if (bmp != null) ivFoto2.setImageBitmap(bmp);
            }
        }
    }

    private void salvarRequisito() {
        String descricao = etDescricao.getText().toString().trim();
        String tempoStr  = etTempoEstimado.getText().toString().trim();

        if (TextUtils.isEmpty(descricao)) {
            etDescricao.setError("Informe a descricao"); etDescricao.requestFocus(); return;
        }
        if (TextUtils.isEmpty(tempoStr)) {
            etTempoEstimado.setError("Informe o tempo"); etTempoEstimado.requestFocus(); return;
        }

        float tempo;
        try { tempo = Float.parseFloat(tempoStr.replace(",", ".")); }
        catch (NumberFormatException e) {
            etTempoEstimado.setError("Valor invalido"); etTempoEstimado.requestFocus(); return;
        }

        String dataReg = tvDataRegistro.getText().toString().replace("Registrado em: ", "");
        Requisito r = new Requisito(projetoId, descricao, dataReg,
                sbImportancia.getProgress() + 1,
                sbDificuldade.getProgress() + 1,
                tempo, latitude, longitude, pathFoto1, pathFoto2);

        long id = dbHelper.inserirRequisito(r);
        if (id != -1) {
            Toast.makeText(this, "Requisito salvo!", Toast.LENGTH_SHORT).show();
            limparCampos();
        } else {
            Toast.makeText(this, "Erro ao salvar.", Toast.LENGTH_SHORT).show();
        }
    }

    private void limparCampos() {
        etDescricao.setText(""); etTempoEstimado.setText("");
        sbImportancia.setProgress(2); sbDificuldade.setProgress(2);
        tvGps.setText("GPS: nao capturado");
        ivFoto1.setImageResource(android.R.drawable.ic_menu_camera);
        ivFoto2.setImageResource(android.R.drawable.ic_menu_camera);
        latitude = 0; longitude = 0; pathFoto1 = ""; pathFoto2 = "";
        etDescricao.requestFocus();
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
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onSupportNavigateUp() { finish(); return true; }
}
