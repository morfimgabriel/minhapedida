package com.example.aluno.correcaominhapedida.model;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.aluno.correcaominhapedida.R;

import org.w3c.dom.Text;

import java.util.ArrayList;

/**
 * Created by Aluno on 16/05/2018.
 */

public class AdapterProduto extends BaseAdapter {

    ArrayList<Produto> listProduto;
    LayoutInflater layoutInflater;


    public AdapterProduto(Context ctx, ArrayList<Produto> lista) {
        listProduto = lista;
        layoutInflater = LayoutInflater.from(ctx);
    }

    public void remove(Produto p) {
        listProduto.remove(p);
        notifyDataSetChanged();
    }

    public void add(Produto p) {
        listProduto.add(p);
        notifyDataSetChanged();

    }

    @Override
    public int getCount() {
        return listProduto.size();
    }

    @Override
    public Produto getItem(int position) {
        return listProduto.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Produto p = listProduto.get(position);

        convertView = layoutInflater.inflate(R.layout.item_list_view_produto, null);

        TextView tvCaroBarato = convertView.findViewById(R.id.caroBarato);
        TextView tvValor = convertView.findViewById(R.id.tvValor);
        tvValor.setText(String.valueOf(p.getValor()));
        if(p.getValor() > 10.0){
            tvCaroBarato.setText("Caro");
        }else{
            tvCaroBarato.setText("Barato");
        }


        TextView tvNomeCategoria = convertView.findViewById(R.id.tvNomeCategoria);
        if(p.getCategoria().getNome().equals("")) {
            tvNomeCategoria.setText("Sem nome");
        }else{
            tvNomeCategoria.setText(p.getCategoria().getNome());
        }

        TextView tvProduto = convertView.findViewById(R.id.tvProdutosCategoria);
        if(p.getNome().equals("")){
            tvProduto.setText("sem Nome");
        }else{
            tvProduto.setText(p.getNome());
        }





        return convertView;
    }
}
