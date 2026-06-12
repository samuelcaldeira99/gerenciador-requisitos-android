package com.exemplo.requisitos;

public class Projeto {
    private int id;
    private String nome;
    private String dataInicio;
    private String dataEntrega;
    private String linkDocumentacao;

    public Projeto() {}

    public Projeto(String nome, String dataInicio, String dataEntrega, String linkDocumentacao) {
        this.nome = nome;
        this.dataInicio = dataInicio;
        this.dataEntrega = dataEntrega;
        this.linkDocumentacao = linkDocumentacao;
    }

    public Projeto(int id, String nome, String dataInicio, String dataEntrega, String linkDocumentacao) {
        this.id = id;
        this.nome = nome;
        this.dataInicio = dataInicio;
        this.dataEntrega = dataEntrega;
        this.linkDocumentacao = linkDocumentacao;
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    public String getNome() { return nome; }
    public void setNome(String nome) { this.nome = nome; }
    public String getDataInicio() { return dataInicio; }
    public void setDataInicio(String dataInicio) { this.dataInicio = dataInicio; }
    public String getDataEntrega() { return dataEntrega; }
    public void setDataEntrega(String dataEntrega) { this.dataEntrega = dataEntrega; }
    public String getLinkDocumentacao() { return linkDocumentacao; }
    public void setLinkDocumentacao(String linkDocumentacao) { this.linkDocumentacao = linkDocumentacao; }
}
