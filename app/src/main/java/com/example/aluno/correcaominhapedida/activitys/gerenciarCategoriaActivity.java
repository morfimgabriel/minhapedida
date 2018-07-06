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
import android.widget.Toast;

import com.example.aluno.correcaominhapedida.R;
import com.example.aluno.correcaominhapedida.dao.MyORMLiteHelper;
import com.example.aluno.correcaominhapedida.model.AdapterCategoria;
import com.example.aluno.correcaominhapedida.model.Categoria;
import com.j256.ormlite.dao.Dao;

import java.sql.SQLException;
import java.util.ArrayList;

public class gerenciarCategoriaActivity extends AppCompatActivity {

    EditText editNome;
    ListView lvCategorias;
    ArrayList<Categoria> listCategorias;
   // ArrayAdapter<Categoria> adapterCategoria;
    Categoria categoria = null;
    AdapterCategoria adapterCategoria;
    MyORMLiteHelper banco;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gerenciar_categoria);
        editNome = findViewById(R.id.editNome);
        lvCategorias = findViewById(R.id.lvCategorias);
        lvCategorias.setOnItemClickListener(cliqueCurto());
        lvCategorias.setOnItemLongClickListener(cliqueLongo());

        listCategorias = new ArrayList<>();
       // adapterCategoria = new ArrayAdapter<Categoria>(this,android.R.layout.simple_list_item_1,listCategorias);

      //  lvCategorias.setAdapter(adapterCategoria);

        banco = MyORMLiteHelper.getInstance(this);

        try {
            listCategorias = (ArrayList<Categoria>) banco.getDaoCategoria().queryForAll();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        adapterCategoria = new AdapterCategoria(this, listCategorias);
        //adapterCategoria = new ArrayAdapter<Categoria>(this, android.R.layout.simple_list_item_1, listCategorias);
        lvCategorias.setAdapter(adapterCategoria);
    }

    public AdapterView.OnItemClickListener cliqueCurto(){
        return new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //Algoritmo Executando
                Toast.makeText(gerenciarCategoriaActivity.this, "Clique Curto", Toast.LENGTH_SHORT).show();


                //Resgatar Categoria Escolhida
                categoria = adapterCategoria.getItem(position);


                //Criar um Dialog perguntando se quer editar
                AlertDialog.Builder alerta = new AlertDialog.Builder(gerenciarCategoriaActivity.this);
                alerta.setTitle("Visualizando Dados");
                alerta.setIcon(android.R.drawable.ic_menu_view);
                alerta.setMessage(categoria.toString());
                alerta.setNegativeButton("Fechar",null);
                alerta.setPositiveButton("editar", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        editNome.setText(categoria.getNome());

                    }
                });
                alerta.show();


            }
        };

    }

    public AdapterView.OnItemLongClickListener cliqueLongo(){
        return new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                Toast.makeText(gerenciarCategoriaActivity.this, "Clique Longo", Toast.LENGTH_SHORT).show();

                //Resgatar categoria escolhida
                categoria = adapterCategoria.getItem(position);

                //Alerta
                AlertDialog.Builder alerta = new AlertDialog.Builder(gerenciarCategoriaActivity.this);
                alerta.setTitle("Ecluindo Categoria");
                alerta.setIcon(android.R.drawable.ic_menu_delete);
                alerta.setMessage("Confirmar Exclusão de " + categoria.getNome()+"?");
                alerta.setNegativeButton("Não", null);

                alerta.setPositiveButton("Sim", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        adapterCategoria.remove(categoria);
                        try {
                            banco.getDaoCategoria().delete(categoria);
                        } catch (SQLException e) {
                            e.printStackTrace();
                        }
                        categoria = null;
                    }
                });
                alerta.show();

                return true;
            }
        };

    }

    public void salvar(View view) throws SQLException {
        if (categoria == null){
            categoria = new Categoria();
            categoria.setNome(editNome.getText().toString());
            adapterCategoria.add(categoria);

            // salvar no banco de dados
            banco.getDaoCategoria().create(categoria);


        }else{
            categoria.setNome(editNome.getText().toString());
            adapterCategoria.notifyDataSetChanged();
            banco.getDaoCategoria().update(categoria);
        }

        Dao.CreateOrUpdateStatus res = banco.getDaoCategoria().createOrUpdate(categoria);
        if(res.isCreated()){
            Toast.makeText(this, "Cadastrado com sucesso!", Toast.LENGTH_SHORT).show();
        }else if(res.isUpdated()){
            Toast.makeText(this, "Atualizado com sucesso!", Toast.LENGTH_SHORT).show();
        }
        categoria = null;
        editNome.setText("");

    }

    public void voltar(View view) {
        Intent it = new Intent(gerenciarCategoriaActivity.this, GerenciarProdutoActivity.class);
        startActivity(it);
    }
}
