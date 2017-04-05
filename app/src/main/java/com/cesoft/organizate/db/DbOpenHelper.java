package com.cesoft.organizate.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.Date;
import java.util.UUID;

////////////////////////////////////////////////////////////////////////////////////////////////////
public final class DbOpenHelper extends SQLiteOpenHelper
{
	private static final int VERSION = 1;

	// TODO : mover a DbObjeto ?



	public DbOpenHelper(Context context)
	{
		super(context, "organizate.db", null, VERSION);
		android.util.Log.e("DbOpenHelper", "constructor---------------------------------------------");
	}

	@Override
	public void onCreate(SQLiteDatabase db)
	{
		android.util.Log.e("DbOpenHelper", "onCreate---------------------------------------------"+DbObjeto.SQL_CREATE_TABLE);
		db.execSQL(DbObjeto.SQL_CREATE_TABLE);
		db.execSQL(DbObjeto.SQL_CREATE_INDEX1);
		db.execSQL(DbObjeto.SQL_CREATE_INDEX2);
		//
		db.execSQL(DbAvisoGeo.SQL_CREATE_TABLE);
		db.execSQL(DbAvisoGeo.SQL_CREATE_INDEX);
		//
		db.execSQL(DbAvisoTem.SQL_CREATE_TABLE);
		db.execSQL(DbAvisoTem.SQL_CREATE_INDEX);

		//------------------------ TEST ---------------------------
		db.delete(DbObjeto.TABLE, "", null);
		//
		ContentValues cv = new ContentValues();
		cv.put(DbObjeto.ID, UUID.randomUUID().toString());
		cv.put(DbObjeto.CREACION, (new Date()).getTime());
		cv.put(DbObjeto.MODIFICADO, (new Date()).getTime());
		cv.put(DbObjeto.LIMITE, (new Date()).getTime() + 60*60*60*24);
		cv.put(DbObjeto.ORDEN, 1);
		cv.put(DbObjeto.PRIORIDAD, 1);
		cv.put(DbObjeto.NOMBRE, "Item 1");
		cv.put(DbObjeto.DESCRIPCION, "desc 1");
		cv.put(DbObjeto.ID_PADRE, 0);
		long i1 = db.insert(DbObjeto.TABLE, null, cv);
			//new Objeto(1, "Item1", "desc1", 1, 1, new Date(), new Date(), new Date(), 0));
Log.e("TAG", "+++++++++++++++ "+i1+"   ==   "+cv.get(DbObjeto.ID));
		cv.put(DbObjeto.ID, UUID.randomUUID().toString());
		cv.put(DbObjeto.CREACION, (new Date()).getTime());
		cv.put(DbObjeto.MODIFICADO, (new Date()).getTime());
		cv.put(DbObjeto.LIMITE, (new Date()).getTime() + 60*60*60*24);
		cv.put(DbObjeto.ORDEN, 2);
		cv.put(DbObjeto.PRIORIDAD, 2);
		cv.put(DbObjeto.NOMBRE, "Item 1B");
		cv.put(DbObjeto.DESCRIPCION, "desc 1B");
		cv.put(DbObjeto.ID_PADRE, i1);
		long i1b = db.insert(DbObjeto.TABLE, null, cv);

		cv.put(DbObjeto.ID, UUID.randomUUID().toString());
		cv.put(DbObjeto.CREACION, (new Date()).getTime());
		cv.put(DbObjeto.MODIFICADO, (new Date()).getTime());
		cv.put(DbObjeto.LIMITE, (new Date()).getTime() + 60*60*60*24);
		cv.put(DbObjeto.ORDEN, 2);
		cv.put(DbObjeto.PRIORIDAD, 5);
		cv.put(DbObjeto.NOMBRE, "Item 1BB");
		cv.put(DbObjeto.DESCRIPCION, "desc 1BB");
		cv.put(DbObjeto.ID_PADRE, i1b);
		long i1b1 = db.insert(DbObjeto.TABLE, null, cv);

		cv.put(DbObjeto.ID, UUID.randomUUID().toString());
		cv.put(DbObjeto.CREACION, (new Date()).getTime());
		cv.put(DbObjeto.MODIFICADO, (new Date()).getTime());
		cv.put(DbObjeto.LIMITE, (new Date()).getTime() + 60*60*60*24);
		cv.put(DbObjeto.ORDEN, 3);
		cv.put(DbObjeto.PRIORIDAD, 3);
		cv.put(DbObjeto.NOMBRE, "Item 2");
		cv.put(DbObjeto.DESCRIPCION, "desc 2");
		cv.put(DbObjeto.ID_PADRE, 0);
		long i2 = db.insert(DbObjeto.TABLE, null, cv);

		cv.put(DbObjeto.ID, UUID.randomUUID().toString());
		cv.put(DbObjeto.CREACION, (new Date()).getTime());
		cv.put(DbObjeto.MODIFICADO, (new Date()).getTime());
		cv.put(DbObjeto.LIMITE, (new Date()).getTime() + 60*60*60*24);
		cv.put(DbObjeto.ORDEN, 3);
		cv.put(DbObjeto.PRIORIDAD, 3);
		cv.put(DbObjeto.NOMBRE, "Item 3");
		cv.put(DbObjeto.DESCRIPCION, "desc 3");
		cv.put(DbObjeto.ID_PADRE, 0);
		long i3 = db.insert(DbObjeto.TABLE, null, cv);
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) { }
}
