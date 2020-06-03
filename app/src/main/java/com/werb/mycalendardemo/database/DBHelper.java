package com.werb.mycalendardemo.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
//用来创建读取数据库工具类，
public class DBHelper extends SQLiteOpenHelper {

	public DBHelper(Context context) {
		super(context, "sin.db", null, 1);
	}

	@Override
	public void onCreate(SQLiteDatabase db) {	//创建签到表
		String sql="create table sinTB(" +
				"sin_id integer primary key autoincrement," +
				"sindate varchar(20)" +
				")";
		db.execSQL(sql);
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		
	}
	
}
