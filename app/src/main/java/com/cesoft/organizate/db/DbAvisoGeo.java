package com.cesoft.organizate.db;

import com.cesoft.organizate.models.AvisoGeo;
import com.cesoft.organizate.util.Log;
import com.squareup.sqlbrite.BriteDatabase;

import android.content.ContentValues;
import android.database.Cursor;

import rx.functions.Func1;


////////////////////////////////////////////////////////////////////////////////////////////////////
public class DbAvisoGeo
{
	public static final String TAG = DbAvisoGeo.class.getSimpleName();

	public static final String TABLE = "avisogeo";
	public static final String QUERY = "SELECT * FROM "+TABLE+" ";

	public static final String ID = "_id";
	public static final String TEXTO = "texto";
	public static final String ACTIVO = "activo";
	public static final String LATITUD = "latitud";
	public static final String LONGITUD = "longitud";
	public static final String RADIO = "radio";
	//public static final String ID_PADRE = "id_padre";

	public static final String SQL_CREATE_TABLE =
			"CREATE TABLE "+ TABLE+" ( "
					+ ID		+ " TEXT   NOT NULL   PRIMARY KEY, "
					+ TEXTO		+ " TEXT, "
					+ ACTIVO	+ " INTEGER, "
					+ LATITUD	+ " REAL, "
					+ LONGITUD	+ " REAL, "
					+ RADIO		+ " REAL "
					//+ ID_PADRE	+ " TEXT   REFERENCES "+DbObjeto.TABLE+" ("+DbObjeto.ID+") "
					+" )";
	public static final String SQL_CREATE_INDEX =
			"CREATE UNIQUE INDEX idx_id_"+TABLE+" ON "+TABLE+" ("+ID+")";

	//----------------------------------------------------------------------------------------------
	public static Func1<Cursor, AvisoGeo> MAPPER = new Func1<Cursor, AvisoGeo>()
	{
		@Override public AvisoGeo call(final Cursor cursor)
		{
			String id = Db.getString(cursor, ID);
			String texto = Db.getString(cursor, TEXTO);
			boolean activo = Db.getBoolean(cursor, ACTIVO);
			double lat = Db.getDouble(cursor, LATITUD);
			double lon = Db.getDouble(cursor, LONGITUD);
			double rad = Db.getDouble(cursor, RADIO);
			return new AvisoGeo(id, texto, activo, lat, lon, (float)rad);
		}
	};

	//______________________________________________________________________________________________
	private static ContentValues code(AvisoGeo o)
	{
		ContentValues cv = new ContentValues();
		cv.put(ID, o.getId());
		cv.put(TEXTO, o.getTexto());
		cv.put(ACTIVO, o.isActivo());
		cv.put(LATITUD, o.getLatitud());
		cv.put(LONGITUD, o.getLongitud());
		cv.put(RADIO, o.getRadio());
		return cv;
	}
	//______________________________________________________________________________________________
	public static void save(BriteDatabase db, AvisoGeo o)
	{
		try
		{
			if(o != null)db.insert(DbAvisoGeo.TABLE, code(o));
		}
		catch(Exception e)
		{
			Log.e(TAG, "save:e:---------------------------------------------------------------------",e);
		}
	}

}
