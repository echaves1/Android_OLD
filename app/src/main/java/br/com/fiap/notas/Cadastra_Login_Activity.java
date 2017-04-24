package br.com.fiap.notas;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.View;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import java.io.IOException;
import java.util.HashMap;

import br.com.fiap.notas.util.ArquivoDB;

public class Cadastra_Login_Activity extends AppCompatActivity {

    private ArquivoDB arquivoDB;
    private HashMap<String, String> mapDados;
    private EditText etNome, etSobrenome, etNascimento, etEmail, etSenha;
    private RadioGroup rgSexo;

    private final String ARQ = "dados.txt";
    private final String SP = "dados";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cadastra__login_);

        arquivoDB = new ArquivoDB();
        mapDados = new HashMap<>();

        etNome = (EditText) findViewById(R.id.edtNome);
        etSobrenome = (EditText) findViewById(R.id.edtSobrenome);
        etNascimento = (EditText) findViewById(R.id.edtDataNascimento);
        etEmail = (EditText) findViewById(R.id.edtEmail);
        etSenha = (EditText) findViewById(R.id.edtSenha);
        rgSexo = (RadioGroup) findViewById(R.id.rgSexo);


    }

    //Captura os dados do formulario e valida o preenchimento correto
    public boolean capituraDados() {
        String nome, sobrenome, nascimento, email, senha, sexo;
        boolean dadosOk = false;

        nome = etNome.getText().toString();
        sobrenome = etSobrenome.getText().toString();
        nascimento = etNascimento.getText().toString();
        email = etEmail.getText().toString();
        senha = etSenha.getText().toString();

        //Retorna o ID do RadioButton que esta checado no RadioGroup
        int sexoId = rgSexo.getCheckedRadioButtonId();
        RadioButton rbSexo = (RadioButton) findViewById(sexoId);

        if (Patterns.EMAIL_ADDRESS.matcher(email).matches() &&
                !TextUtils.isEmpty(senha) &&
                !TextUtils.isEmpty(nome) &&
                !TextUtils.isEmpty(sobrenome) &&
                !TextUtils.isEmpty(nascimento) && (sexoId != -1)) {
            dadosOk = true;

            sexo = rbSexo.getText().toString();
            mapDados.put("usuario", email);
            mapDados.put("senha", senha);
            mapDados.put("nome", nome);
            mapDados.put("sobrenome", sobrenome);
            mapDados.put("nascimento", nascimento);
            mapDados.put("sexo", sexo);
        } else {
            Toast.makeText(this, R.string.validacao_cadastro, Toast.LENGTH_SHORT).show();
        }
        return dadosOk;
    }

    public void gravarChaves(View v){

        if(capituraDados()){
            arquivoDB.gravarChaves(this, SP, mapDados);
            Toast.makeText(this, R.string.cadastro_ok, Toast.LENGTH_SHORT).show();
        }
    }

    public void excluirChaves(View v){
        if(capituraDados()){
            arquivoDB.excluirChaves(this, SP, mapDados);
            Toast.makeText(this, R.string.exclusao_ok, Toast.LENGTH_SHORT).show();
        }
    }
    public void carregarChaves(View v){
        etNome.setText(arquivoDB.retornarValor(this, SP, "nome"));
        etSobrenome.setText(arquivoDB.retornarValor(this, SP, "sobrenome"));
        etNascimento.setText(arquivoDB.retornarValor(this, SP, "nascimento"));
        etEmail.setText(arquivoDB.retornarValor(this, SP, "usuario"));
        etSenha.setText(arquivoDB.retornarValor(this, SP, "senha"));


        //getString(R.string.feminino) --- Garantir a internacionalização

        if(arquivoDB.retornarValor(this, SP, "sexo").equals(getString(R.string.feminino))) {
            rgSexo.check(R.id.rbFeminino);
        }else{
            rgSexo.check(R.id.rbMasculino);
        }

    }

    public boolean verificarChaves(){
        if(arquivoDB.verificarChave(this, SP, "usuario") &&
                arquivoDB.verificarChave(this, SP, "senha")){
            Toast.makeText(this, R.string.login_ok, Toast.LENGTH_SHORT).show();
            return  true;
        }else{
            Toast.makeText(this, R.string.login_nok, Toast.LENGTH_SHORT).show();
            return false;
        }
    }

    public void gravarArquivo(View v) throws IOException {

        if(capituraDados()){

            try {
                arquivoDB.gravaArquivo(this, ARQ, mapDados.toString());
                Toast.makeText(this, R.string.arquivo_ok, Toast.LENGTH_SHORT).show();
            }catch (IOException e){

                Toast.makeText(this, R.string.arquivo_nok, Toast.LENGTH_SHORT).show();
                e.printStackTrace();

            }
        }
    }

    //metodo que exibe  a gravação em arquivo por meio de Toast
    public void lerArquivo(View v){
        String txt = "";

        try {
            txt = arquivoDB.lerArquivo(this, ARQ);
        } catch (IOException e) {
            e.printStackTrace();
            Toast.makeText(this, txt, Toast.LENGTH_SHORT).show();
        }
        Toast.makeText(this, txt, Toast.LENGTH_SHORT).show();
    }

    public void excluirArquivo(){

        try {
            arquivoDB.excluiArquivo(this, ARQ);
            Toast.makeText(this, R.string.exclusao_arq_ok, Toast.LENGTH_SHORT).show();

        }catch (Exception e){
            e.printStackTrace();
            Toast.makeText(this, R.string.exclusao_arq_nok, Toast.LENGTH_SHORT).show();

        }

    }


}

