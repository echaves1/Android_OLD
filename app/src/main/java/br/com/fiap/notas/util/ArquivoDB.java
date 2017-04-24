package br.com.fiap.notas.util;

import android.content.Context;
import android.content.SharedPreferences;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by logonrm on 20/03/2017.
 */

public class ArquivoDB {

    private SharedPreferences pref;

    public void gravarChaves(Context  context, String prefName, HashMap<String, String> map){
        pref = context.getSharedPreferences(prefName, Context.MODE_PRIVATE);
        SharedPreferences.Editor e = pref.edit();

        for (Map.Entry<String, String> entry : map.entrySet()){
            e.putString(entry.getKey(), entry.getValue());
        }
        e.commit();
    }

    public void excluirChaves(Context  context, String prefName, HashMap<String, String> map){
        pref = context.getSharedPreferences(prefName, Context.MODE_PRIVATE);
        SharedPreferences.Editor e = pref.edit();

        for (Map.Entry<String, String> entry : map.entrySet()){
            e.remove(entry.getKey());
        }
        e.commit();
    }

    public boolean verificarChave(Context context, String prefName, String key){
        pref = context.getSharedPreferences(prefName, Context.MODE_PRIVATE);
        return pref.contains(key);
    }

    public String retornarValor(Context context, String prefName, String key){

        pref = context.getSharedPreferences(prefName, Context.MODE_PRIVATE);
     return pref.getString(key, "");
    }


    public  void gravaArquivo(Context context, String arqName, String value)throws IOException{
        FileOutputStream fos= context.openFileOutput(arqName, Context.MODE_PRIVATE);

        try {
            fos.write(value.getBytes());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public  String lerArquivo(Context contex, String arqName) throws  IOException{
        String txt = null;
        FileInputStream fis = contex.openFileInput(arqName);
        BufferedReader br = new BufferedReader(new InputStreamReader(fis));

        try {
            txt =br.readLine();
            fis.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return txt;
    }

    public boolean excluiArquivo(Context context, String arqName){

        try {
            return context.deleteFile(arqName);
        }catch (Exception e){
            e.printStackTrace();
        }
        return false;
    }


}
