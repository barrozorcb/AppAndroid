package com.example.barrozo.appcontatos.dominio;


import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.widget.ArrayAdapter;

public class ContatoRepository {

    private SQLiteDatabase conn;

    public ContatoRepository(SQLiteDatabase conn){
        this.conn = conn;
    }

    public void testeInserirContatos(){
        for (int i = 0; i < 10; i++) {
            ContentValues values = new ContentValues();
            values.put("TELEFONE", "555555");
            conn.insertOrThrow("CONTATO", null, values);
        }
    }

    public ArrayAdapter<String> buscaContatos(Context context){

        ArrayAdapter<String> adpContatos = new ArrayAdapter<String>(context, android.R.layout.simple_list_item_1);

        Cursor cursor = conn.query("CONTATO", null, null, null, null, null, null);

        if (cursor.getCount() > 0 ){
            cursor.moveToFirst();
            do {
                String dado = cursor.getString(1);
                adpContatos.add(dado);
            }while (cursor.moveToNext());

        }
        return adpContatos;
    }

}
