package com.example.aluno.correcaominhapedida.activitys;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.NumberPicker;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.aluno.correcaominhapedida.R;
import com.example.aluno.correcaominhapedida.dao.MyORMLiteHelper;
import com.example.aluno.correcaominhapedida.model.Categoria;
import com.example.aluno.correcaominhapedida.model.Item;
import com.example.aluno.correcaominhapedida.model.Produto;
import com.j256.ormlite.dao.Dao;

import java.sql.SQLException;
import java.util.ArrayList;

public class addProdutoActivity extends AppCompatActivity {

    Spinner spinner;
    NumberPicker npQuantidade;
    ArrayList<Produto> listProdutos;
    ArrayAdapter<Produto> adapterProdutos;
    MyORMLiteHelper banco;
    Item item = null;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_produto);
        banco = MyORMLiteHelper.getInstance(this);

        npQuantidade = findViewById(R.id.numberPicker);
        npQuantidade.setMaxValue(10);
        npQuantidade.setMinValue(0);

        try {
            listProdutos = (ArrayList<Produto>) banco.getDaoProduto().queryForAll();
            if (listProdutos.size() == 0) {
                popularProdutos();
                listProdutos = (ArrayList<Produto>) banco.getDaoProduto().queryForAll();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        //Mandando Spinner para banco
        adapterProdutos = new ArrayAdapter<Produto>(this, android.R.layout.simple_spinner_item
                , listProdutos);
        spinner = findViewById(R.id.spinner);
        spinner.setAdapter(adapterProdutos);


        //Adicionar novo produto
//        Produto prod4 = new Produto("Água", 2.50);
//        try {
//            banco.getProdutoDao().create(prod4);
//        } catch (SQLException e) {
//            e.printStackTrace();
//        }
//        adapterProdutos.add(prod4);

        //   }

    }

    public void popularProdutos() throws SQLException {
       /* Produto prod1 = new Produto("Refri", 3.50);
        Produto prod2 = new Produto("Cerveja", 3.50);
        Produto prod3 = new Produto("Batata", 3.50);


        banco.getDaoProduto().create(prod1);
        banco.getDaoProduto().create(prod2);
        banco.getDaoProduto().create(prod3);*/


    }


    public void salvarProduto(View view) throws SQLException {
        if (item == null) {
            item = new Item();
            item.setProduto((Produto) spinner.getSelectedItem());
            item.setQuantidade(npQuantidade.getValue());
            item.setValor(item.getProduto().getValor());


            banco.getDaoItem().create(item);
            Toast.makeText(this, "item Cadastrado", Toast.LENGTH_SHORT).show();

            item = null;

        }
    }

    public void comanda(View view) {
        Intent it = new Intent(addProdutoActivity.this, MainActivity.class);
        startActivity(it);
    }
}
