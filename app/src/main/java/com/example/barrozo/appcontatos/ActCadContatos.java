package com.example.barrozo.appcontatos;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;

import com.example.barrozo.appcontatos.databse.DataBase;
import com.example.barrozo.appcontatos.dominio.ContatoRepository;
import com.example.barrozo.appcontatos.dominio.entity.Contato;

import java.text.DateFormat;
import java.util.Calendar;
import java.util.Date;

public class ActCadContatos extends AppCompatActivity {

    private EditText edtNome;
    private EditText edtEmail;
    private EditText edtTelefone;
    private EditText edtEndereco;
    private EditText edtDatasEspeceiais;
    private EditText edtGrupos;
    private Spinner spnTipoEmail;
    private Spinner spnTipoTelefone;
    private Spinner spnTipoEndereco;
    private Spinner spnTipoDatasEspeciais;
    private ArrayAdapter<String> adpTipoEmail;
    private ArrayAdapter<String> adpTipoTelefone;
    private ArrayAdapter<String> adpTipoEndereco;
    private ArrayAdapter<String> adpTipoDatasEspeciais;

    private DataBase dataBase;
    private SQLiteDatabase conn;
    private ContatoRepository contatoRepository;
    private Contato contato;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_cad_contatos);

        edtNome = (EditText)findViewById(R.id.edtNome);
        edtEmail = (EditText)findViewById(R.id.edtEmail);
        edtTelefone = (EditText)findViewById(R.id.edtTelefone);
        edtEndereco = (EditText)findViewById(R.id.edtEndereco);
        edtDatasEspeceiais = (EditText)findViewById(R.id.edtDatasEspeciais);
        edtGrupos = (EditText)findViewById(R.id.edtGrupos);

        spnTipoEmail = (Spinner)findViewById(R.id.spnTipoEmail);
        spnTipoTelefone = (Spinner)findViewById(R.id.spnTipoTelefone);
        spnTipoEndereco = (Spinner)findViewById(R.id.spnTipoEndereco);
        spnTipoDatasEspeciais = (Spinner)findViewById(R.id.spnTipoDatasEspeciais);

        adpTipoEmail = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item);
        adpTipoEmail.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        adpTipoTelefone = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item);
        adpTipoTelefone.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        adpTipoEndereco = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item);
        adpTipoEndereco.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        adpTipoDatasEspeciais = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item);
        adpTipoDatasEspeciais.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        spnTipoEmail.setAdapter(adpTipoEmail);
        spnTipoTelefone.setAdapter(adpTipoTelefone);
        spnTipoEndereco.setAdapter(adpTipoEndereco);
        spnTipoDatasEspeciais.setAdapter(adpTipoDatasEspeciais);

        adpTipoEmail.add("CASA");
        adpTipoEmail.add("TRABALHO");
        adpTipoEmail.add("OUTROS");

        adpTipoTelefone.add("CELULAR");
        adpTipoTelefone.add("CASA");
        adpTipoTelefone.add("TRABALHO");
        adpTipoTelefone.add("PRINCIPAL");
        adpTipoTelefone.add("FAX TRABALHO");
        adpTipoTelefone.add("FAX CASA");
        adpTipoTelefone.add("OUTROS");

        adpTipoEndereco.add("CASA");
        adpTipoEndereco.add("TRABALHO");
        adpTipoEndereco.add("OUTROS");

        adpTipoDatasEspeciais.add("ANIVERSÁRIO");
        adpTipoDatasEspeciais.add("DATA COMEMORATIVA");
        adpTipoDatasEspeciais.add("OUTROS");

        ExibeDataListenner listenner = new ExibeDataListenner();

        edtDatasEspeceiais.setOnClickListener(listenner);
        edtDatasEspeceiais.setOnFocusChangeListener(listenner);


        contato = new Contato();
        try {
            dataBase = new DataBase(this);
            conn = dataBase.getWritableDatabase();
            contatoRepository = new ContatoRepository(conn);
        }catch (SQLException e){
            AlertDialog.Builder dlg = new AlertDialog.Builder(this);
            dlg.setMessage("Erro ao criar o banco: " + e.getMessage());
            dlg.setNeutralButton("OK", null);
            dlg.show();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_act_cad_contatos, menu);

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()){
            case R.id.mn_acao1:
                if(contato == null){
                    inserir();
                }
                finish();

            case R.id.mn_acao2:
                break;
            case R.id.mn_acao3:
                break;
        }

        return super.onOptionsItemSelected(item);
    }

    private void inserir(){
        try {
            contato.setNome(edtNome.getText().toString());
            contato.setTelefone(edtTelefone.getText().toString());
            contato.setTipoTelefone(String.valueOf(spnTipoTelefone.getSelectedItemPosition()));
            contato.setEmail(edtEmail.getText().toString());
            contato.setTipoEmail(String.valueOf(spnTipoEmail.getSelectedItemPosition()));
            contato.setEndereco(edtEndereco.getText().toString());
            contato.setTipoEndereco(String.valueOf(spnTipoEndereco.getSelectedItemPosition()));
            contato.setTipoDatasEspeciais(String.valueOf(spnTipoDatasEspeciais.getSelectedItemPosition()));
            contato.setGrupos(edtGrupos.getText().toString());

            contatoRepository.inserir(contato);
        }catch (Exception ex){
            AlertDialog.Builder dlg = new AlertDialog.Builder(this);
            dlg.setMessage("Erro ao inserir dados: " + ex.getMessage());
            dlg.setNeutralButton("OK", null);
            dlg.show();
        }
    }

    private void exibeData(){

        Calendar calendar = Calendar.getInstance();
        int ano = calendar.get(calendar.YEAR);
        int mes = calendar.get(calendar.MONTH);
        int dia = calendar.get(calendar.DAY_OF_MONTH);

        DatePickerDialog datePickerDialog = new DatePickerDialog(this, new SelecionaDataListener(), ano, mes, dia);
        datePickerDialog.show();
    }

    private class ExibeDataListenner implements View.OnClickListener, View.OnFocusChangeListener{

        @Override
        public void onClick(View v) {
            exibeData();
        }

        @Override
        public void onFocusChange(View v, boolean hasFocus) {
            if(hasFocus)
                exibeData();
        }
    }

    private class SelecionaDataListener implements DatePickerDialog.OnDateSetListener{

        @Override
        public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {

            Calendar calendar = Calendar.getInstance();
            calendar.set(year, monthOfYear, dayOfMonth);

            Date date = calendar.getTime();

            DateFormat dateFormat = DateFormat.getDateInstance(DateFormat.MEDIUM);
            String dt = dateFormat.format(date);

            edtDatasEspeceiais.setText(dt);
            contato.setDatasEspeciais(date);
        }
    }
}
