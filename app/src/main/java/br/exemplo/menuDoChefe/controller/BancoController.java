package br.exemplo.menuDoChefe.controller;

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

import br.exemplo.menuDoChefe.dao.BancoDao;
import br.exemplo.menuDoChefe.entity.Post;

public class BancoController {

    private SQLiteDatabase db;
    private BancoDao banco;

    public BancoController(Context context){
        banco = new BancoDao(context);
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

    public String inserirImagemLocalmente(Bitmap bitmap, String name, Context context) {
        //TODO fazer a inserção local
        ContextWrapper cw = new ContextWrapper(context.getApplicationContext());
        // path to /data/data/yourapp/app_data/imageDir
        File directory = cw.getDir("imageDir", Context.MODE_PRIVATE);
        // Create imageDir
        File mypath=new File(directory,name+".jpg");

        FileOutputStream fos = null;
        try {
            fos = new FileOutputStream(mypath);
            // Use the compress method on the BitMap object to write image to the OutputStream
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

    public boolean verificaBancoPopulado() {
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

    public List<Post> carregaPosts() {
        db = banco.getReadableDatabase();

        Cursor cursor= db.rawQuery("SELECT id, titulo, descricao, urlImage FROM posts", null);
        List<Post> posts = new ArrayList<>();

        while (cursor.moveToNext()) {
            String descricao = cursor.getString(2);
            String urlImage = cursor.getString(3);
            Integer id = cursor.getInt(0);
            String titulo = cursor.getString(1);

            posts.add(new Post(id.longValue(), titulo,descricao, urlImage));
        }

        return posts;
    }

    public List<Bitmap> carregaImagens(List<Post> posts, String pathImages) {
        //TODO CARREGAR DA BASE POR URL
        List<Bitmap> imagens = new ArrayList<>();
        try {
            for (int i = 0; i < posts.size(); i++) {
                File f=new File(pathImages, "imagem"+posts.get(i).getId()+".jpg");
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
