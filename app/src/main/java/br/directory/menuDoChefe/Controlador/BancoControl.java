package br.directory.menuDoChefe.Controlador;

import android.content.ContentValues;
import android.content.Context;
import android.content.ContextWrapper;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import br.directory.menuDoChefe.DAOO.BancoDaOO;
import br.directory.menuDoChefe.Entidade.Metodo;

public class BancoControl {

    private SQLiteDatabase db;
    private BancoDaOO banco;

    public BancoControl(Context context){
        banco = new BancoDaOO(context);
    }

    public String inserir(String titulo, String descricao, String urlImagem){
        ContentValues valores;
        long resultado;

        db = banco.getWritableDatabase();
        valores = new ContentValues();
        valores.put("urlImage", urlImagem);
        valores.put("titulo", titulo);
        valores.put("descricao", descricao);
        resultado = db.insert("posts", null, valores);
        db.close();

        if (resultado ==-1)
            return "Erro ao inserir registro";

        return "Registro Inserido com sucesso";

    }

    public void resetarBanco() {
        banco.onUpgrade(db,0,0);
    }

    public String ImputImageLocal(Bitmap bitmap, String name, Context context) {
        ContextWrapper cw = new ContextWrapper(context.getApplicationContext());
        File directory = cw.getDir("imageDir", Context.MODE_PRIVATE);
        File mypath=new File(directory,name+".jpg");

        FileOutputStream fos = null;
        try {
            fos = new FileOutputStream(mypath);
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, fos);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                fos.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return directory.getAbsolutePath();
    }
    public List<Metodo> carregaPosts() {
        db = banco.getReadableDatabase();

        Cursor cursor= db.rawQuery("SELECT id, titulo, descricao, urlImage FROM posts", null);
        List<Metodo> metodos = new ArrayList<>();

        while (cursor.moveToNext()) {
            String descricao = cursor.getString(2);
            String urlImage = cursor.getString(3);
            Integer id = cursor.getInt(0);
            String titulo = cursor.getString(1);

            metodos.add(new Metodo(id.longValue(), titulo,descricao, urlImage));
        }

        return metodos;
    }
    public boolean ChecksBD() {
        db = banco.getReadableDatabase();

        try {
            Cursor count= db.rawQuery("SELECT COUNT(id) FROM posts", null);

            while (count.moveToNext()) {
                return count.getInt(0) > 0;
            }
        } catch (Exception e) {
            return false;
        }
        return false;
    }



    public List<Bitmap> UpploadImagens(List<Metodo> metodos, String pathImages) {
        //TODO CARREGAR DA BASE POR URL
        List<Bitmap> imagens = new ArrayList<>();
        try {
            for (int i = 0; i < metodos.size(); i++) {
                File f=new File(pathImages, "imagem"+ metodos.get(i).getId()+".jpg");
                Bitmap b = BitmapFactory.decodeStream(new FileInputStream(f));
                imagens.add(b);
            }
        }
        catch (FileNotFoundException e)
        {
            e.printStackTrace();
        }
        return imagens;
    }
}
