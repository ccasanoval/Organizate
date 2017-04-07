package com.cesoft.organizate.db;

import com.cesoft.organizate.models.AvisoTem;
import com.cesoft.organizate.util.Log;
import com.squareup.sqlbrite.BriteDatabase;

import android.content.ContentValues;


////////////////////////////////////////////////////////////////////////////////////////////////////
class DbAvisoTem
{
	private static final String TAG = "DbAvisoTem";

	static final String TABLE = "avisotem";
	//static final String QUERY = "SELECT * FROM "+TABLE+" ";

	static final String ID = "_id";
	private static final String TEXTO = "texto";
	private static final String ACTIVO = "activo";
	private static final String MES = "mes";
	private static final String DIA_MES = "diames";
	private static final String DIA_SEMANA = "diasem";
	private static final String HORA = "hora";
	private static final String MINUTO = "minuto";
	//public static final String ID_PADRE = "id_padre";

	static final String SQL_CREATE_TABLE =
			"CREATE TABLE "+ TABLE+" ( "
					+ ID		+ " TEXT   NOT NULL   PRIMARY KEY, "
					+ TEXTO		+ " TEXT, "
					+ ACTIVO	+ " INTEGER, "
					+ MES		+ " BLOB, "
					+ DIA_MES	+ " BLOB, "
					+ DIA_SEMANA+ " BLOB, "
					+ HORA		+ " BLOB, "
					+ MINUTO	+ " BLOB  "
					//+ ID_PADRE	+ " TEXT   REFERENCES "+DbObjeto.TABLE+" ("+DbObjeto.ID+") "
					+" )";
	static final String SQL_CREATE_INDEX =
			"CREATE UNIQUE INDEX idx_id_"+TABLE+" ON "+TABLE+" ("+ID+")";

	//----------------------------------------------------------------------------------------------
	/*public static Func1<Cursor, AvisoTem> MAPPER = new Func1<Cursor, AvisoTem>()
	{
		@Override public AvisoTem call(final Cursor cursor)
		{
			int i = -1;
			String id = cursor.getString(++i);
			String texto = cursor.getString(++i);
			boolean activo = cursor.getInt(++i) > 0;
			byte[] mes = cursor.getBlob(++i);
			byte[] diaMes = cursor.getBlob(++i);
			byte[] diaSem = cursor.getBlob(++i);
			byte[] hora = cursor.getBlob(++i);
			byte[] minuto = cursor.getBlob(++i);
			return new AvisoTem(id, texto, activo, mes, diaMes, diaSem, hora, minuto);
		}
	};*/

	//______________________________________________________________________________________________
	private static ContentValues code(AvisoTem o)
	{
		ContentValues cv = new ContentValues();
		cv.put(ID, o.getId());
		cv.put(TEXTO, o.getTexto());
		cv.put(ACTIVO, o.isActivo());
		cv.put(MES, o.getMeses());
		cv.put(DIA_MES, o.getDiasMes());
		cv.put(DIA_SEMANA, o.getDiasSemana());
		cv.put(HORA, o.getHoras());
		cv.put(MINUTO, o.getMinutos());
		return cv;
	}
	//______________________________________________________________________________________________
	public static void save(BriteDatabase db, AvisoTem o)
	{
		try
		{
			if(o != null)db.insert(DbAvisoTem.TABLE, code(o));
		}
		catch(Exception e)
		{
			Log.e(TAG, "save:e:---------------------------------------------------------------------",e);
		}
	}

}
