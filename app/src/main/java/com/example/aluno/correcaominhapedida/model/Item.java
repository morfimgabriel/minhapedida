package com.example.aluno.correcaominhapedida.model;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

import java.io.Serializable;

/**
 * Created by Aluno on 16/04/2018.
 */

@DatabaseTable
public class Item implements Serializable {
    @DatabaseField(allowGeneratedIdInsert = true, generatedId = true)
    private Integer id;

    @DatabaseField(canBeNull = false)
    private double valor;

    @DatabaseField(canBeNull = false)
    private int quantidade;

    @DatabaseField(foreign = true, foreignAutoRefresh = true)
    private Produto produto;

    public Item(double valor, int quantidade, Produto produto) {
        this.valor = valor;
        this.quantidade = quantidade;
        this.produto = produto;
    }

    public Item() {
    }

    public Integer getId() {
        return id;
    }

    public double getValor() {
        return valor;
    }

    public void setValor(double valor) {
        this.valor = valor;
    }

    public double subtotal() {
        double valor = this.valor * this.quantidade;
        return valor;

    }

    public int getQuantidade() {
        return quantidade;
    }

    public void setQuantidade(int quantidade) {
        this.quantidade = quantidade;
    }

    public Produto getProduto() {
        return produto;
    }

    public void setProduto(Produto produto) {
        this.produto = produto;
    }

    @Override
    public String toString() {
        return id + " - " + getProduto().getNome() + " - valor: " + valor + " - quantidade: " + quantidade + " - subtotal: " + subtotal() ;
    }
}
