package com.example.projetofinalandroid;

import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

public class ProdutoDAO {

	private static final String NOME_BANCO = "android_db";
	private static final String NOME_TABELA = "table_produtos";
	private static final int VERSAO_BANCO = 1;
	private LocalDataBaseHelper localDataBaseHelper;
	private List<String> sqlCreation;
	private List<String> sqlUpdate;
	private SQLiteDatabase db;

	public ProdutoDAO(Context context) {

		sqlCreation = new ArrayList<String>();
		
		sqlCreation.add("CREATE TABLE IF NOT EXISTS " + NOME_TABELA + " ("
				+ "_id INTEGER PRIMARY KEY AUTOINCREMENT," + "nome TEXT NOT NULL," +
				 "preco_unitario FLOAT," + "quantidade INTEGER, " + "logotipo BLOB);");

		sqlUpdate = new ArrayList<String>();
		sqlUpdate.add("DROP TABLE " + NOME_TABELA);

		localDataBaseHelper = new LocalDataBaseHelper(context, NOME_BANCO, sqlCreation,
				sqlUpdate, VERSAO_BANCO);

		db = localDataBaseHelper.getWritableDatabase();

	}

	public void inserir(Produto prod) {

		ContentValues values = getProduto(prod);

		db.insert(NOME_TABELA, null, values);

	}

	public void atualizar(Produto prod) {

		ContentValues values = getProduto(prod);

		String[] id = { String.valueOf(prod.getId()) };
		db.update(NOME_TABELA, values, "_id = ?", id);
				

	}

	public void excluir(Produto prod) {
		
		String[] id = { String.valueOf(prod.getId()) };
		db.delete(NOME_TABELA, "_id = ?", id);
	}


	public List<Produto> buscarTodosProdutos() {

		List<Produto> listaProd = null;
		String[] columns = { "_id", "nome", "preco_unitario", "quantidade", "logotipo" };
		Cursor c = db.query(NOME_TABELA, columns, null, null, null, null, "nome");
		
		listaProd = new ArrayList<Produto>();
		if (c.getCount() > 0) {
			c.moveToFirst();
			while (!c.isAfterLast()) {
			
				Produto p = setProduto(c);
				
				listaProd.add(p);
				c.moveToNext();
			}
		}
		
		return listaProd;
	}
	
	public Produto buscarProduto(int id) {
		String[] columns = {  "_id", "nome", "preco_unitario", "quantidade", "logotipo" };
		String[] param = { String.valueOf(id) };
		Cursor c = db.query(NOME_TABELA, columns, "_id = ?", param, null, null, "_id");
		c.moveToFirst();
		
		Produto p = setProduto(c);
		
		return p;
	}
	
	
	private Produto setProduto(Cursor c) {
		Produto p = new Produto();
		p.setId(c.getInt(0));
		p.setNome(c.getString(1));
		p.setPrecoUnitario(c.getDouble(2));
		p.setQuantidade(c.getInt(3));
		p.setBufferImg(c.getBlob(4));
		return p;
	}
	
	private ContentValues getProduto(Produto produto) {
		ContentValues values = new ContentValues();
		values.put("nome", produto.getNome());
		values.put("preco_unitario", produto.getPrecoUnitario());
		values.put("quantidade", produto.getQuantidade());
		values.put("logotipo", produto.getBufferImg());
		return values;
	}

}
