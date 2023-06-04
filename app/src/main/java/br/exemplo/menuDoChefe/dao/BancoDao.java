package br.exemplo.menuDoChefe.dao;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class BancoDao extends SQLiteOpenHelper {
    private static final String DESCRICAO = "descricao";
    private static final String IMAGE = "urlImage";
    private static final String NOME_BANCO = "banco.db";
    private static final String TITULO = "titulo";
    private static final String TABELA = "posts";
    private static final String ID = "id";


    private static final int VERSAO = 1;

    public BancoDao(Context context){
        super(context, NOME_BANCO,null,VERSAO);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        String sql = "CREATE TABLE "+TABELA+"("
                + ID + " integer primary key autoincrement,"
                + TITULO + " text,"
                + DESCRICAO + " text,"
                + IMAGE + " text"
                +")";
        sqLiteDatabase.execSQL(sql);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + TABELA);
        onCreate(sqLiteDatabase);
    }
}
