package com.exemplo.requisitos;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;

public class DatabaseHelper extends SQLiteOpenHelper {

    private static final String DB_NAME    = "requisitosdb";
    private static final int    DB_VERSION = 3;

    // Tabela PROJETO
    public static final String TB_PROJETO        = "projeto";
    public static final String COL_PROJ_ID       = "id";
    public static final String COL_PROJ_NOME     = "nome";
    public static final String COL_PROJ_INICIO   = "data_inicio";
    public static final String COL_PROJ_ENTREGA  = "data_entrega";
    public static final String COL_PROJ_LINK     = "link_documentacao";

    // Tabela REQUISITO
    public static final String TB_REQUISITO      = "requisito";
    public static final String COL_REQ_ID        = "id";
    public static final String COL_REQ_PROJ_ID   = "projeto_id";
    public static final String COL_REQ_DESC      = "descricao";
    public static final String COL_REQ_DATA      = "data_registro";
    public static final String COL_REQ_IMP       = "nivel_importancia";
    public static final String COL_REQ_DIF       = "nivel_dificuldade";
    public static final String COL_REQ_TEMPO     = "tempo_estimado";
    public static final String COL_REQ_LAT       = "latitude";
    public static final String COL_REQ_LNG       = "longitude";
    public static final String COL_REQ_FOTO1     = "foto1";
    public static final String COL_REQ_FOTO2     = "foto2";

    // Tabela USUARIO
    public static final String TB_USUARIO        = "usuario";
    public static final String COL_USR_ID        = "id";
    public static final String COL_USR_NOME      = "nome";
    public static final String COL_USR_EMAIL     = "email";
    public static final String COL_USR_SENHA     = "senha";

    public DatabaseHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE " + TB_PROJETO + " (" +
                COL_PROJ_ID      + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COL_PROJ_NOME    + " TEXT NOT NULL, " +
                COL_PROJ_INICIO  + " TEXT NOT NULL, " +
                COL_PROJ_ENTREGA + " TEXT NOT NULL, " +
                COL_PROJ_LINK    + " TEXT DEFAULT '')");

        db.execSQL("CREATE TABLE " + TB_REQUISITO + " (" +
                COL_REQ_ID      + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COL_REQ_PROJ_ID + " INTEGER NOT NULL, " +
                COL_REQ_DESC    + " TEXT NOT NULL, " +
                COL_REQ_DATA    + " TEXT NOT NULL, " +
                COL_REQ_IMP     + " INTEGER NOT NULL, " +
                COL_REQ_DIF     + " INTEGER NOT NULL, " +
                COL_REQ_TEMPO   + " REAL NOT NULL, " +
                COL_REQ_LAT     + " REAL DEFAULT 0, " +
                COL_REQ_LNG     + " REAL DEFAULT 0, " +
                COL_REQ_FOTO1   + " TEXT DEFAULT '', " +
                COL_REQ_FOTO2   + " TEXT DEFAULT '', " +
                "FOREIGN KEY(" + COL_REQ_PROJ_ID + ") REFERENCES " + TB_PROJETO + "(" + COL_PROJ_ID + "))");

        db.execSQL("CREATE TABLE " + TB_USUARIO + " (" +
                COL_USR_ID    + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COL_USR_NOME  + " TEXT NOT NULL, " +
                COL_USR_EMAIL + " TEXT NOT NULL UNIQUE, " +
                COL_USR_SENHA + " TEXT NOT NULL)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        if (oldVersion < 3) {
            // Adiciona coluna link se nao existir (sem perder dados)
            try {
                db.execSQL("ALTER TABLE " + TB_PROJETO +
                        " ADD COLUMN " + COL_PROJ_LINK + " TEXT DEFAULT ''");
            } catch (Exception ignored) {}

            // Cria tabela usuario se nao existir
            db.execSQL("CREATE TABLE IF NOT EXISTS " + TB_USUARIO + " (" +
                    COL_USR_ID    + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    COL_USR_NOME  + " TEXT NOT NULL, " +
                    COL_USR_EMAIL + " TEXT NOT NULL UNIQUE, " +
                    COL_USR_SENHA + " TEXT NOT NULL)");
        }
    }

    // ── USUARIO ───────────────────────────────────────────────────────────────

    public long inserirUsuario(String nome, String email, String senha) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues v = new ContentValues();
        v.put(COL_USR_NOME,  nome);
        v.put(COL_USR_EMAIL, email);
        v.put(COL_USR_SENHA, senha);
        long id = db.insert(TB_USUARIO, null, v);
        db.close();
        return id;
    }

    public boolean emailJaCadastrado(String email) {
        SQLiteDatabase db = getReadableDatabase();
        Cursor c = db.query(TB_USUARIO, null, COL_USR_EMAIL + "=?",
                new String[]{email}, null, null, null);
        boolean existe = c.getCount() > 0;
        c.close();
        return existe;
    }

    public boolean autenticarUsuario(String email, String senha) {
        SQLiteDatabase db = getReadableDatabase();
        Cursor c = db.query(TB_USUARIO, null,
                COL_USR_EMAIL + "=? AND " + COL_USR_SENHA + "=?",
                new String[]{email, senha}, null, null, null);
        boolean ok = c.getCount() > 0;
        c.close();
        return ok;
    }

    // ── PROJETO ───────────────────────────────────────────────────────────────

    public long inserirProjeto(Projeto p) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues v = new ContentValues();
        v.put(COL_PROJ_NOME,    p.getNome());
        v.put(COL_PROJ_INICIO,  p.getDataInicio());
        v.put(COL_PROJ_ENTREGA, p.getDataEntrega());
        v.put(COL_PROJ_LINK,    p.getLinkDocumentacao() != null ? p.getLinkDocumentacao() : "");
        long id = db.insert(TB_PROJETO, null, v);
        db.close();
        return id;
    }

    public int atualizarProjeto(Projeto p) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues v = new ContentValues();
        v.put(COL_PROJ_NOME,    p.getNome());
        v.put(COL_PROJ_INICIO,  p.getDataInicio());
        v.put(COL_PROJ_ENTREGA, p.getDataEntrega());
        v.put(COL_PROJ_LINK,    p.getLinkDocumentacao() != null ? p.getLinkDocumentacao() : "");
        int rows = db.update(TB_PROJETO, v, COL_PROJ_ID + "=?", new String[]{String.valueOf(p.getId())});
        db.close();
        return rows;
    }

    public List<Projeto> listarProjetos() {
        List<Projeto> lista = new ArrayList<>();
        SQLiteDatabase db = getReadableDatabase();
        Cursor c = db.query(TB_PROJETO, null, null, null, null, null, COL_PROJ_ID + " DESC");
        if (c.moveToFirst()) {
            do { lista.add(cursorToProjeto(c)); } while (c.moveToNext());
        }
        c.close();
        return lista;
    }

    public Projeto buscarProjeto(int id) {
        SQLiteDatabase db = getReadableDatabase();
        Cursor c = db.query(TB_PROJETO, null, COL_PROJ_ID + "=?",
                new String[]{String.valueOf(id)}, null, null, null);
        if (c != null && c.moveToFirst()) {
            Projeto p = cursorToProjeto(c);
            c.close();
            return p;
        }
        return null;
    }

    public int excluirProjeto(int id) {
        SQLiteDatabase db = getWritableDatabase();
        db.delete(TB_REQUISITO, COL_REQ_PROJ_ID + "=?", new String[]{String.valueOf(id)});
        int rows = db.delete(TB_PROJETO, COL_PROJ_ID + "=?", new String[]{String.valueOf(id)});
        db.close();
        return rows;
    }

    private Projeto cursorToProjeto(Cursor c) {
        return new Projeto(
                c.getInt(c.getColumnIndexOrThrow(COL_PROJ_ID)),
                c.getString(c.getColumnIndexOrThrow(COL_PROJ_NOME)),
                c.getString(c.getColumnIndexOrThrow(COL_PROJ_INICIO)),
                c.getString(c.getColumnIndexOrThrow(COL_PROJ_ENTREGA)),
                c.getString(c.getColumnIndexOrThrow(COL_PROJ_LINK)));
    }

    // ── REQUISITO ─────────────────────────────────────────────────────────────

    public long inserirRequisito(Requisito r) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues v = new ContentValues();
        v.put(COL_REQ_PROJ_ID, r.getProjetoId());
        v.put(COL_REQ_DESC,    r.getDescricao());
        v.put(COL_REQ_DATA,    r.getDataRegistro());
        v.put(COL_REQ_IMP,     r.getNivelImportancia());
        v.put(COL_REQ_DIF,     r.getNivelDificuldade());
        v.put(COL_REQ_TEMPO,   r.getTempoEstimado());
        v.put(COL_REQ_LAT,     r.getLatitude());
        v.put(COL_REQ_LNG,     r.getLongitude());
        v.put(COL_REQ_FOTO1,   r.getFoto1() != null ? r.getFoto1() : "");
        v.put(COL_REQ_FOTO2,   r.getFoto2() != null ? r.getFoto2() : "");
        long id = db.insert(TB_REQUISITO, null, v);
        db.close();
        return id;
    }

    public int atualizarRequisito(Requisito r) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues v = new ContentValues();
        v.put(COL_REQ_DESC,  r.getDescricao());
        v.put(COL_REQ_IMP,   r.getNivelImportancia());
        v.put(COL_REQ_DIF,   r.getNivelDificuldade());
        v.put(COL_REQ_TEMPO, r.getTempoEstimado());
        v.put(COL_REQ_LAT,   r.getLatitude());
        v.put(COL_REQ_LNG,   r.getLongitude());
        v.put(COL_REQ_FOTO1, r.getFoto1() != null ? r.getFoto1() : "");
        v.put(COL_REQ_FOTO2, r.getFoto2() != null ? r.getFoto2() : "");
        int rows = db.update(TB_REQUISITO, v, COL_REQ_ID + "=?", new String[]{String.valueOf(r.getId())});
        db.close();
        return rows;
    }

    public List<Requisito> listarRequisitosPorProjeto(int projetoId) {
        List<Requisito> lista = new ArrayList<>();
        SQLiteDatabase db = getReadableDatabase();
        Cursor c = db.query(TB_REQUISITO, null, COL_REQ_PROJ_ID + "=?",
                new String[]{String.valueOf(projetoId)}, null, null, COL_REQ_ID + " DESC");
        if (c.moveToFirst()) {
            do { lista.add(cursorToRequisito(c)); } while (c.moveToNext());
        }
        c.close();
        return lista;
    }

    public Requisito buscarRequisito(int id) {
        SQLiteDatabase db = getReadableDatabase();
        Cursor c = db.query(TB_REQUISITO, null, COL_REQ_ID + "=?",
                new String[]{String.valueOf(id)}, null, null, null);
        if (c != null && c.moveToFirst()) {
            Requisito r = cursorToRequisito(c);
            c.close();
            return r;
        }
        return null;
    }

    public int excluirRequisito(int id) {
        SQLiteDatabase db = getWritableDatabase();
        int rows = db.delete(TB_REQUISITO, COL_REQ_ID + "=?", new String[]{String.valueOf(id)});
        db.close();
        return rows;
    }

    private Requisito cursorToRequisito(Cursor c) {
        return new Requisito(
                c.getInt(c.getColumnIndexOrThrow(COL_REQ_ID)),
                c.getInt(c.getColumnIndexOrThrow(COL_REQ_PROJ_ID)),
                c.getString(c.getColumnIndexOrThrow(COL_REQ_DESC)),
                c.getString(c.getColumnIndexOrThrow(COL_REQ_DATA)),
                c.getInt(c.getColumnIndexOrThrow(COL_REQ_IMP)),
                c.getInt(c.getColumnIndexOrThrow(COL_REQ_DIF)),
                c.getFloat(c.getColumnIndexOrThrow(COL_REQ_TEMPO)),
                c.getDouble(c.getColumnIndexOrThrow(COL_REQ_LAT)),
                c.getDouble(c.getColumnIndexOrThrow(COL_REQ_LNG)),
                c.getString(c.getColumnIndexOrThrow(COL_REQ_FOTO1)),
                c.getString(c.getColumnIndexOrThrow(COL_REQ_FOTO2)));
    }
}
