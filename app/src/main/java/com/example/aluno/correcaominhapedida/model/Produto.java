package com.example.aluno.correcaominhapedida.model;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

import java.io.Serializable;
import java.util.Collection;

/**
 * Created by Aluno on 16/04/2018.
 */
@DatabaseTable(tableName = "produtos")
public class Produto implements Serializable {
    @DatabaseField(allowGeneratedIdInsert = true, generatedId = true)
    private Integer id;

    @DatabaseField(columnName = "nome", canBeNull = false, width = 100)
    private String nome;

    @DatabaseField(columnName = "valor", canBeNull = false, width = 10)
    private double valor;

    @DatabaseField(foreign = true, foreignAutoRefresh = true)
    private Categoria categoria;



    public Produto() {
    }

    public Produto(String nome, double valor) {
        this.nome = nome;
        this.valor = valor;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public double getValor() {
        return valor;
    }

    public void setValor(double valor) {
        this.valor = valor;
    }

    public Categoria getCategoria() {
        return categoria;
    }

    public void setCategoria(Categoria categoria) {
        this.categoria = categoria;
    }

    @Override
    public String toString() {
        return id + ": " + categoria.getNome() + " - " +   nome + " - R$"+valor;
    }
}
