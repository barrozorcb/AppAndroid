package com.example.barrozo.appcontatos;

import android.app.AlertDialog;
import android.content.Intent;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;

import com.example.barrozo.appcontatos.databse.DataBase;
import com.example.barrozo.appcontatos.dominio.ContatoRepository;

public class ActContato extends AppCompatActivity implements View.OnClickListener{


    private ImageButton btnAdicionar;
    private EditText edtPesquisa;
    private ListView lstContatos;
    private DataBase dataBase;
    private SQLiteDatabase conn;
    private ArrayAdapter<String> adpContatos;
    private ContatoRepository contatoRepository;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_contato);

        btnAdicionar = (ImageButton)findViewById(R.id.btnAdicionar);
        edtPesquisa = (EditText)findViewById(R.id.edtPesquisa);
        lstContatos = (ListView)findViewById(R.id.lstContatos);
        btnAdicionar.setOnClickListener(this);

        try {
            dataBase = new DataBase(this);
            conn = dataBase.getWritableDatabase();

            contatoRepository = new ContatoRepository(conn);
            contatoRepository.testeInserirContatos();
            adpContatos = contatoRepository.buscaContatos(this);

            lstContatos.setAdapter(adpContatos);

            AlertDialog.Builder dlg = new AlertDialog.Builder(this);
            dlg.setMessage("Conexão criada com sucesso");
            dlg.setNeutralButton("OK", null);
            dlg.show();

        }catch (SQLException e){
            AlertDialog.Builder dlg = new AlertDialog.Builder(this);
            dlg.setMessage("Erro ao criar o banco: " + e.getMessage());
            dlg.setNeutralButton("OK", null);
            dlg.show();
        }

    }

    @Override
    public void onClick(View v) {
        Intent it = new Intent(this, ActCadContatos.class);
        startActivity(it);
    }
}
