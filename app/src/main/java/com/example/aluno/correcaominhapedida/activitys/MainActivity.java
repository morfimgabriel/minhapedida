package com.example.aluno.correcaominhapedida.activitys;

import android.content.ActivityNotFoundException;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.aluno.correcaominhapedida.R;
import com.example.aluno.correcaominhapedida.dao.MyORMLiteHelper;
import com.example.aluno.correcaominhapedida.model.Categoria;
import com.example.aluno.correcaominhapedida.model.Item;
import com.example.aluno.correcaominhapedida.model.Produto;

import org.w3c.dom.Text;

import java.sql.SQLException;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    final int REQUEST_QR_CODE = 10;
    final int REQUEST_BAR_CODE = 11;
    MyORMLiteHelper banco;
    ListView lvItens;
    ArrayList<Item> listItens;
    ArrayAdapter<Item> adapterItens;
    Item item;
    TextView textViewTotal;
    double valtotal;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        lvItens = findViewById(R.id.listViewItens);
        listItens = new ArrayList<>();
        lvItens.setOnItemLongClickListener(cliqueLongo());

        textViewTotal = findViewById(R.id.totalText);

        banco = MyORMLiteHelper.getInstance(this);

        try {
            listItens = (ArrayList<Item>) banco.getDaoItem().queryForAll();
            System.out.println("TAMANHO: " + listItens.size());
            calcularTotal();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        adapterItens = new ArrayAdapter<Item>(this, android.R.layout.simple_list_item_1, listItens);
        lvItens.setAdapter(adapterItens);

        lvItens.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                item = adapterItens.getItem(i);
                AlertDialog.Builder alerta = new AlertDialog.Builder(MainActivity.this);
                alerta.setTitle("Adicionar mais um na sua quantidade?");
                alerta.setMessage("Deseja adicionar mais uma unidade do item: " + item.toString() + " ?");
                alerta.setNeutralButton("Fechar", null);
                alerta.setPositiveButton("Adicionar", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        item.setQuantidade(item.getQuantidade() + 1);
                        valtotal = valtotal + item.getProduto().getValor();
                        adapterItens.notifyDataSetChanged();
                        try {
                            banco.getDaoItem().update(item);
                        } catch (SQLException e) {
                            e.printStackTrace();
                        }
                        calcularTotal();
                    }
                });
                alerta.show();
            }
        });
    }

    public void calcularTotal() {
        int sum = 0;
        if (listItens.size() > 0) {
            for (Item i : listItens) {
                sum += i.getValor() * i.getQuantidade();
            }
        }
        textViewTotal.setText("" + sum);
    }

    public void scanQRCode(View v) {
        try {
            Intent intent = new Intent("com.google.zxing.client.android.SCAN");
            intent.putExtra("SCAN_MODE", "QR_CODE_MODE");
            startActivityForResult(intent, REQUEST_QR_CODE);
        } catch (ActivityNotFoundException anfe) {
            Toast.makeText(this, "Baixe o qr code", Toast.LENGTH_SHORT).show();
            Uri uri = Uri.parse("markt://search?q=pname:" + "com.google.zxing.client.android");
            Intent intent = new Intent(Intent.ACTION_VIEW, uri);
            startActivity(intent);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {

            switch (requestCode) {
                case REQUEST_QR_CODE:
                    Toast.makeText(this, "Qrcode - " + data.getStringExtra("SCAN_RESULT"), Toast.LENGTH_SHORT).show();
                    Integer id = Integer.parseInt(data.getStringExtra("SCAN_RESULT"));
                    try {

                        Produto produto = banco.getDaoProduto().queryForId(id);

                        Toast.makeText(this, "item do banco ->" + item, Toast.LENGTH_SHORT).show();

                        if (produto != null) {
                            item = new Item();
                            item.setProduto(produto);
                            item.setQuantidade(1);
                            item.setValor(item.getProduto().getValor());
                            adapterItens.add(item);
                        } else {
                            Toast.makeText(this, "Produto não encontrado", Toast.LENGTH_SHORT).show();
                        }

                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                    break;

                case REQUEST_BAR_CODE:
                    Toast.makeText(this, "BarCode:\n" + data.getStringExtra("SCAN_RESULT"), Toast.LENGTH_SHORT).show();
                    break;
            }
        } else {
            Toast.makeText(this, "Cancelou", Toast.LENGTH_SHORT).show();
        }
    }


    public void limpar(View view) {
        try {
            if (listItens != null && listItens.size() > 0) {
                ArrayList<Item> list = new ArrayList<>(listItens);
                for(Item i : list) {
                    banco.getDaoItem().delete(i);
                    listItens = new ArrayList<>();
                    adapterItens.clear();
                    valtotal = 0d;
                    textViewTotal.setText("" + valtotal);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void btnAdicionarProduto(View view) {
        Intent it = new Intent(MainActivity.this, addProdutoActivity.class);
        startActivity(it);
    }

    public AdapterView.OnItemLongClickListener cliqueLongo() {
        return new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                Toast.makeText(MainActivity.this, "Clique Longo", Toast.LENGTH_SHORT).show();

                //Resgatar categoria escolhida
                item = adapterItens.getItem(position);

                //Alerta
                AlertDialog.Builder alerta = new AlertDialog.Builder(MainActivity.this);
                alerta.setTitle("Excluindo Item");
                alerta.setIcon(android.R.drawable.ic_menu_delete);
                alerta.setMessage("Confirmar Exclusão de " + item.getProduto().getNome() + "?");
                alerta.setNegativeButton("Não", null);

                alerta.setPositiveButton("Sim", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        adapterItens.remove(item);
                        valtotal = valtotal - item.getValor();
                        try {
                            banco.getDaoItem().delete(item);
                        } catch (SQLException e) {
                            e.printStackTrace();
                        }
                        textViewTotal.setText(String.valueOf(valtotal));
                        item = null;
                        calcularTotal();
                    }
                });
                alerta.show();

                return true;
            }
        };

    }

}