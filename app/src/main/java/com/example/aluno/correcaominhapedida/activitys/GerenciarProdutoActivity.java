package com.example.aluno.correcaominhapedida.activitys;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.aluno.correcaominhapedida.R;
import com.example.aluno.correcaominhapedida.dao.MyORMLiteHelper;
import com.example.aluno.correcaominhapedida.model.AdapterProduto;
import com.example.aluno.correcaominhapedida.model.Categoria;
import com.example.aluno.correcaominhapedida.model.Produto;

import java.sql.SQLException;
import java.util.ArrayList;

public class GerenciarProdutoActivity extends AppCompatActivity {

    Spinner spCategoria;

    ArrayAdapter<Categoria> adapterCategoria;
    ArrayList<Categoria> listCategoria;

    EditText editNomeProd, editValor;
    ListView lvProdutos;

  //  ArrayAdapter<Produto> adapterProduto;
    AdapterProduto adapterProduto;
    ArrayList<Produto> listProduto;
    Produto produto = null;


    MyORMLiteHelper banco;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gerenciar_produto);
        banco = MyORMLiteHelper.getInstance(this);

        editNomeProd = findViewById(R.id.editNome);
        editValor = findViewById(R.id.editValor);
        spCategoria = findViewById(R.id.spCategorias);
        lvProdutos = findViewById(R.id.lvProdutos);


// Montando Spinner
        try {
            listCategoria = (ArrayList<Categoria>) banco.getDaoCategoria().queryForAll();
            System.out.println("aqui: " + listCategoria.size());
            if (listCategoria.size() == 0) {
                listCategoria = (ArrayList<Categoria>) banco.getDaoCategoria().queryForAll();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }


        adapterCategoria = new ArrayAdapter<Categoria>(this, android.R.layout.simple_spinner_item
                , listCategoria);
        spCategoria.setAdapter(adapterCategoria);
        //fim

        // Carregando Produtos
        listProduto = new ArrayList<>();
        try {
            listProduto = (ArrayList<Produto>) banco.getDaoProduto().queryForAll();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        adapterProduto = new AdapterProduto(this, listProduto);
        lvProdutos.setAdapter(adapterProduto);


        try {
            carregarListView();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    public void carregarListView() throws SQLException {
        listProduto = (ArrayList<Produto>) banco.getDaoProduto().queryForAll();
        /*adapterProduto = new ArrayAdapter<Produto>(this, android.R.layout.simple_list_item_1, listProduto);*/
        adapterProduto = new AdapterProduto(this, listProduto);
        lvProdutos.setAdapter(adapterProduto);



        // inicio Clique Curto - Editar
            lvProdutos.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                produto = adapterProduto.getItem(position);
                AlertDialog.Builder alerta = new AlertDialog.Builder(GerenciarProdutoActivity.this);
                alerta.setTitle("Visualizando produto");
                alerta.setMessage(produto.toString());
                alerta.setNeutralButton("fechar", null);
                alerta.setPositiveButton("Editar", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        editNomeProd.setText(produto.getNome());
                        editValor.setText(String.valueOf(produto.getValor()));

                        for (int pos = 0; pos < adapterCategoria.getCount(); pos++) {
                            Categoria c = adapterCategoria.getItem(pos);
                            if (c.getId() == produto.getCategoria().getId()) {
                                spCategoria.setSelection(pos);
                                break;
                            }
                        }

                    }

                });
                alerta.show();

            }
        });
        // fim clique curto

        // inicio clique longo
        lvProdutos.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                produto = adapterProduto.getItem(position);
                AlertDialog.Builder alerta = new AlertDialog.Builder(GerenciarProdutoActivity.this);
                alerta.setTitle("Excluindo Produto");
                alerta.setMessage(produto.toString());
                alerta.setNeutralButton("cancelar", null);
                alerta.setPositiveButton("sim", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        try {
                            banco.getDaoProduto().delete(produto);
                            adapterProduto.remove(produto);
                            produto = null;
                        } catch (SQLException e) {
                            e.printStackTrace();
                        }
                    }
                });
                alerta.show();
                return true;
            }
        });
    }


    public void addCategoria(View view) {
        Intent it = new Intent(GerenciarProdutoActivity.this, gerenciarCategoriaActivity.class);
        startActivity(it);
    }


    public void salvar(View view) throws SQLException {
        if(produto==null){
            produto = new Produto();
            produto.setNome(editNomeProd.getText().toString());
            produto.setValor(Double.parseDouble(editValor.getText().toString()));
            produto.setCategoria((Categoria) spCategoria.getSelectedItem());
            long res = banco.getDaoProduto().create(produto);
            if(res!=-1) {
                adapterProduto.add(produto);
                Toast.makeText(this, "Cadastrado"+res, Toast.LENGTH_SHORT).show();
            }
        }else{
            produto.setNome(editNomeProd.getText().toString());
            produto.setValor(Double.parseDouble(editValor.getText().toString()));
            produto.setCategoria((Categoria) spCategoria.getSelectedItem());
            int res = banco.getDaoProduto().update(produto);
            if(res!=-1){
                adapterProduto.notifyDataSetChanged();
                Toast.makeText(this, "Editado"+res, Toast.LENGTH_SHORT).show();
            }
        }

        produto = null;
        editNomeProd.setText("");
        editValor.setText("");
        spCategoria.setSelection(0);
        editNomeProd.requestFocus();
    }


    public void pesquisaCategoria(View view) throws SQLException {
        Categoria c = new Categoria();
        listProduto = c.getArrayListProdutos((Categoria) spCategoria.getSelectedItem());
        adapterProduto = new AdapterProduto(this, listProduto);
        lvProdutos.setAdapter(adapterProduto);

    }
}


