package com.example.projetofinalandroid;

import java.util.List;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class LocalDataBaseHelper extends SQLiteOpenHelper{

	private List<String> sqlCreation;
	private List<String> sqlUpdate;

	public LocalDataBaseHelper(Context context, String name, List<String> sqlCreation,
			List<String> sqlUpdate, int version) {
		
		super(context, name, null, version);

		this.sqlCreation = sqlCreation;
		this.sqlUpdate = sqlUpdate;

	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		
		for (String sqlStatement : sqlCreation) {
			db.execSQL(sqlStatement);
		}

	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		
		for (String sqlStatement : sqlUpdate) {
			db.execSQL(sqlStatement);
		}
		
		this.onCreate(db);
		
	}
	
	
	
	

}

