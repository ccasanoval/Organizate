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
		String id1 = UUID.randomUUID().toString();
		cv.put(DbObjeto.ID, id1);
		cv.put(DbObjeto.CREACION, (new Date()).getTime());
		cv.put(DbObjeto.MODIFICADO, (new Date()).getTime());
		cv.put(DbObjeto.LIMITE, (new Date()).getTime() + 60*60*60*24);
		cv.put(DbObjeto.ORDEN, 1);
		cv.put(DbObjeto.PRIORIDAD, 1);
		cv.put(DbObjeto.NOMBRE, "Item 1");
		cv.put(DbObjeto.DESCRIPCION, "desc 1");
		cv.put(DbObjeto.ID_PADRE, (String)null);
		long i1 = db.insert(DbObjeto.TABLE, null, cv);
			//new Objeto(1, "Item1", "desc1", 1, 1, new Date(), new Date(), new Date(), 0));
Log.e("TAG", "+++++++++++++++ "+i1+"   ==   "+cv.get(DbObjeto.ID));


		String id2 = UUID.randomUUID().toString();
		cv.put(DbObjeto.ID, id2);
		cv.put(DbObjeto.CREACION, (new Date()).getTime());
		cv.put(DbObjeto.MODIFICADO, (new Date()).getTime());
		cv.put(DbObjeto.LIMITE, (new Date()).getTime() + 60*60*60*24);
		cv.put(DbObjeto.ORDEN, 2);
		cv.put(DbObjeto.PRIORIDAD, 2);
		cv.put(DbObjeto.NOMBRE, "Item 1B");
		cv.put(DbObjeto.DESCRIPCION, "desc 1B");
		cv.put(DbObjeto.ID_PADRE, id1);
		db.insert(DbObjeto.TABLE, null, cv);

		String id3 = UUID.randomUUID().toString();
		cv.put(DbObjeto.ID, id3);
		cv.put(DbObjeto.CREACION, (new Date()).getTime());
		cv.put(DbObjeto.MODIFICADO, (new Date()).getTime());
		cv.put(DbObjeto.LIMITE, (new Date()).getTime() + 60*60*60*24);
		cv.put(DbObjeto.ORDEN, 2);
		cv.put(DbObjeto.PRIORIDAD, 5);
		cv.put(DbObjeto.NOMBRE, "Item 1BB");
		cv.put(DbObjeto.DESCRIPCION, "desc 1BB");
		cv.put(DbObjeto.ID_PADRE, id2);
		db.insert(DbObjeto.TABLE, null, cv);

		cv.put(DbObjeto.ID, UUID.randomUUID().toString());
		cv.put(DbObjeto.CREACION, (new Date()).getTime());
		cv.put(DbObjeto.MODIFICADO, (new Date()).getTime());
		cv.put(DbObjeto.LIMITE, (new Date()).getTime() + 60*60*60*24);
		cv.put(DbObjeto.ORDEN, 3);
		cv.put(DbObjeto.PRIORIDAD, 3);
		cv.put(DbObjeto.NOMBRE, "Item 2");
		cv.put(DbObjeto.DESCRIPCION, "desc 2");
		cv.put(DbObjeto.ID_PADRE, (String)null);
		long i2 = db.insert(DbObjeto.TABLE, null, cv);

		cv.put(DbObjeto.ID, UUID.randomUUID().toString());
		cv.put(DbObjeto.CREACION, (new Date()).getTime());
		cv.put(DbObjeto.MODIFICADO, (new Date()).getTime());
		cv.put(DbObjeto.LIMITE, (new Date()).getTime() + 60*60*60*24);
		cv.put(DbObjeto.ORDEN, 3);
		cv.put(DbObjeto.PRIORIDAD, 3);
		cv.put(DbObjeto.NOMBRE, "Item 3");
		cv.put(DbObjeto.DESCRIPCION, "desc 3");
		cv.put(DbObjeto.ID_PADRE, (String)null);
		long i3 = db.insert(DbObjeto.TABLE, null, cv);

		///---------

		cv = new ContentValues();
		cv.put(DbAvisoTem.ID, id1);
		cv.put(DbAvisoTem.ACTIVO, true);
		cv.put(DbAvisoTem.TEXTO, "TEST de Aviso!");
		cv.put(DbAvisoTem.HORA, new byte[]{21, 22, 23});
		cv.put(DbAvisoTem.MINUTO, new byte[]{55});
		db.insert(DbAvisoTem.TABLE, null, cv);

		cv = new ContentValues();
		cv.put(DbAvisoGeo.ID, id1);
		cv.put(DbAvisoGeo.ACTIVO, true);
		cv.put(DbAvisoGeo.TEXTO, "TEST de Aviso!");
		cv.put(DbAvisoGeo.LATITUD, 40.0);
		cv.put(DbAvisoGeo.LONGITUD, 3.0);
		cv.put(DbAvisoGeo.RADIO, 50.0);
		db.insert(DbAvisoGeo.TABLE, null, cv);
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) { }
}
