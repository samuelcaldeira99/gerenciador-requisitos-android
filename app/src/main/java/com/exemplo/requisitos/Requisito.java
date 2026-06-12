package com.exemplo.requisitos;

public class Requisito {
    private int id;
    private int projetoId;
    private String descricao;
    private String dataRegistro;
    private int nivelImportancia;
    private int nivelDificuldade;
    private float tempoEstimado;
    private double latitude;
    private double longitude;
    private String foto1;
    private String foto2;

    public Requisito() {}

    public Requisito(int projetoId, String descricao, String dataRegistro,
                     int nivelImportancia, int nivelDificuldade, float tempoEstimado,
                     double latitude, double longitude, String foto1, String foto2) {
        this.projetoId = projetoId;
        this.descricao = descricao;
        this.dataRegistro = dataRegistro;
        this.nivelImportancia = nivelImportancia;
        this.nivelDificuldade = nivelDificuldade;
        this.tempoEstimado = tempoEstimado;
        this.latitude = latitude;
        this.longitude = longitude;
        this.foto1 = foto1;
        this.foto2 = foto2;
    }

    public Requisito(int id, int projetoId, String descricao, String dataRegistro,
                     int nivelImportancia, int nivelDificuldade, float tempoEstimado,
                     double latitude, double longitude, String foto1, String foto2) {
        this.id = id;
        this.projetoId = projetoId;
        this.descricao = descricao;
        this.dataRegistro = dataRegistro;
        this.nivelImportancia = nivelImportancia;
        this.nivelDificuldade = nivelDificuldade;
        this.tempoEstimado = tempoEstimado;
        this.latitude = latitude;
        this.longitude = longitude;
        this.foto1 = foto1;
        this.foto2 = foto2;
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    public int getProjetoId() { return projetoId; }
    public void setProjetoId(int projetoId) { this.projetoId = projetoId; }
    public String getDescricao() { return descricao; }
    public void setDescricao(String descricao) { this.descricao = descricao; }
    public String getDataRegistro() { return dataRegistro; }
    public void setDataRegistro(String dataRegistro) { this.dataRegistro = dataRegistro; }
    public int getNivelImportancia() { return nivelImportancia; }
    public void setNivelImportancia(int v) { this.nivelImportancia = v; }
    public int getNivelDificuldade() { return nivelDificuldade; }
    public void setNivelDificuldade(int v) { this.nivelDificuldade = v; }
    public float getTempoEstimado() { return tempoEstimado; }
    public void setTempoEstimado(float tempoEstimado) { this.tempoEstimado = tempoEstimado; }
    public double getLatitude() { return latitude; }
    public void setLatitude(double latitude) { this.latitude = latitude; }
    public double getLongitude() { return longitude; }
    public void setLongitude(double longitude) { this.longitude = longitude; }
    public String getFoto1() { return foto1; }
    public void setFoto1(String foto1) { this.foto1 = foto1; }
    public String getFoto2() { return foto2; }
    public void setFoto2(String foto2) { this.foto2 = foto2; }
}
