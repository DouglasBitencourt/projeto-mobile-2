package br.exemplo.menuDoChefe.entity;

import java.io.Serializable;

public class Post implements Serializable {

    private Long id;
    private String descricao;

    private String urlImagem;
    private String titulo;

    private Double preco;

    public Post() {
    }

    public Post(Long id, String titulo, String descricao, String urlImagem, Double preco) {
        this.descricao = descricao;
        this.urlImagem = urlImagem;
        this.id = id;
        this.titulo = titulo;

        this.preco = preco;
    }

    public Post(Long id, String titulo, String descricao, String urlImagem) {
        this.id = id;
        this.descricao = descricao;
        this.titulo = titulo;
        this.urlImagem = urlImagem;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public String getUrlImagem() {
        return urlImagem;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public String getDescricao() {
        return descricao;
    }



    public void setUrlImagem(String urlImagem) {
        this.urlImagem = urlImagem;
    }

    public Double getPreco() {
        return preco;
    }

    public void setPreco(Double preco) {
        this.preco = preco;
    }
}
