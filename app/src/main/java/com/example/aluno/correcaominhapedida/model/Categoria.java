package com.example.aluno.correcaominhapedida.model;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.field.ForeignCollectionField;
import com.j256.ormlite.table.DatabaseTable;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;

/**
 * Created by Aluno on 16/04/2018.
 */
@DatabaseTable
public class Categoria {

    @DatabaseField(allowGeneratedIdInsert = true, generatedId = true)
    private Integer id;

    @DatabaseField(canBeNull = false)
    private String nome;

    @ForeignCollectionField
    private Collection<Produto> listaProdutos;

    public Categoria() {
    }

    public Categoria(String nome) {
        this.nome = nome;
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

    public ArrayList<Produto> getArrayListProdutos(Categoria c) {
        ArrayList<Produto> selecaoProdutos = new ArrayList<>();
        Iterator<Produto> iterator = c.getListaProdutos().iterator();
        while (iterator.hasNext()) {
            Produto p = iterator.next();
            selecaoProdutos.add(p);
        }
        return selecaoProdutos;
    }

    public void setListaProdutos(Collection<Produto> listaProdutos) {
        this.listaProdutos = listaProdutos;
    }

    public Collection<Produto> getListaProdutos() {
        return listaProdutos;
    }

    @Override
    public String toString() {
        return  id + " - " + nome;
    }
}
