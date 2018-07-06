package com.example.aluno.correcaominhapedida.model;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.aluno.correcaominhapedida.R;
import com.example.aluno.correcaominhapedida.model.Categoria;

import java.util.ArrayList;


/**
 * Created by Aluno on 16/05/2018.
 */

public class AdapterCategoria extends BaseAdapter {

    ArrayList<Categoria> listCategoria;
    LayoutInflater inflate;

    public AdapterCategoria(Context ctx, ArrayList<Categoria> lista){
        listCategoria = lista;
        inflate = LayoutInflater.from(ctx);
    }


    @Override
    public int getCount() {
        return listCategoria.size();
    }

    @Override
    public Categoria getItem(int position) {
        return listCategoria.get(position);
    }


    public void remove(Categoria c){
        listCategoria.remove(c);
        notifyDataSetChanged();
    }

    public void add(Categoria c){
        listCategoria.add(c);
        notifyDataSetChanged();
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Categoria c = listCategoria.get(position);

        convertView = inflate.inflate(R.layout.item_list_view_categoria, null);

        TextView tvIdCategoria = convertView.findViewById(R.id.tvIdCategoria);
        tvIdCategoria.setText(String.valueOf(c.getId()));

        TextView tvNome = convertView.findViewById(R.id.tvNomeCategoria);
        if(c.getNome().equals("")) {
            tvNome.setText("Sem nome");
        }else{
            tvNome.setText(c.getNome());
        }

        TextView tvQtd = convertView.findViewById(R.id.tvProdutosCategoria);
        try{
            tvQtd.setText("" + c.getListaProdutos().size());
        }catch (Exception e){
            tvQtd.setText("0");
        }


        return convertView;
    }
}
