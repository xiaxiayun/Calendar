package com.werb.mycalendardemo.database;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.werb.mycalendardemo.database.DBHelper;

//向表中插入签到数据
public class SinInDao {
	Context c;
	DBHelper dbhelper;
	SQLiteDatabase db;
	
	public SinInDao(Context c)
	{
		this.c = c;
	}
	public boolean open()
	{
		dbhelper = new DBHelper(c);
		db=dbhelper.getWritableDatabase();
		if(db==null)
		{
			return false;
		}
		return true;
	}
	public void close()
	{
		dbhelper.close();
	}
	//插入签到
	public void insertSinInfo(String dateString) {
		String sql = "insert into sinTB(sindate) values(?)";
		db.execSQL(sql, new Object[]{dateString});
	}
	//查询是否签到
	public boolean isSigned(String dateString) {
		String sql = "select * from sinTB where sindate = " + dateString;
		Cursor cur = db.rawQuery(sql, null);
		return cur.moveToFirst();
	}

}
