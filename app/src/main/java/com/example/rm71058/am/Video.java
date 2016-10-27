package com.example.rm71058.am;

/**
 * Created by rm71058 on 27/10/2016.
 */
public class Video {
    int codigo;
    int tempo;
    String descricao;

    public Video(int codigo, int tempo, String descricao) {
        this.codigo = codigo;
        this.tempo = tempo;
        this.descricao = descricao;
    }

    @Override
    public String toString(){
        return "Código: " + codigo + " Duração:" + tempo + " Descrição: " + descricao;
    }
    public int getCodigo() {
        return codigo;
    }
    public int getTempo() {
        return tempo;
    }
    public String getDescricao() {
        return descricao;
    }

    public void setCodigo(int codigo) {
        this.codigo = codigo;
    }
    public void setTempo(int tempo) {
        this.tempo = tempo;
    }
    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }
}
