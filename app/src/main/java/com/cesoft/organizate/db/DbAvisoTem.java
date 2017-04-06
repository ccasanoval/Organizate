package com.cesoft.organizate.db;

import com.cesoft.organizate.models.AvisoTem;
import com.cesoft.organizate.util.Log;
import com.squareup.sqlbrite.BriteDatabase;

import android.content.ContentValues;
import android.database.Cursor;
import rx.functions.Func1;


////////////////////////////////////////////////////////////////////////////////////////////////////
public class DbAvisoTem
{
	public static final String TAG = "DbAvisoTem";

	public static final String TABLE = "avisotem";
	public static final String QUERY = "SELECT * FROM "+TABLE+" ";

	public static final String ID = "_id";
	public static final String TEXTO = "texto";
	public static final String ACTIVO = "activo";
	public static final String MES = "mes";
	public static final String DIA_MES = "diames";
	public static final String DIA_SEMANA = "diasem";
	public static final String HORA = "hora";
	public static final String MINUTO = "minuto";
	//public static final String ID_PADRE = "id_padre";

	public static final String SQL_CREATE_TABLE =
			"CREATE TABLE "+ TABLE+" ( "
					+ ID		+ " TEXT   NOT NULL   PRIMARY KEY, "
					+ TEXTO		+ " TEXT, "
					+ ACTIVO	+ " INTEGER, "
					+ MES		+ " INTEGER, "
					+ DIA_MES	+ " INTEGER, "
					+ DIA_SEMANA+ " INTEGER, "
					+ HORA		+ " INTEGER, "
					+ MINUTO	+ " INTEGER "
					//+ ID_PADRE	+ " TEXT   REFERENCES "+DbObjeto.TABLE+" ("+DbObjeto.ID+") "
					+" )";
	public static final String SQL_CREATE_INDEX =
			"CREATE UNIQUE INDEX idx_id_"+TABLE+" ON "+TABLE+" ("+ID+")";

	//----------------------------------------------------------------------------------------------
	public static Func1<Cursor, AvisoTem> MAPPER = new Func1<Cursor, AvisoTem>()
	{
		@Override public AvisoTem call(final Cursor cursor)
		{
			String id = Db.getString(cursor, ID);
			String texto = Db.getString(cursor, TEXTO);
			boolean activo = Db.getBoolean(cursor, ACTIVO);
			long mes = Db.getLong(cursor, MES);
			long diaMes = Db.getLong(cursor, DIA_MES);
			long diaSem = Db.getLong(cursor, DIA_SEMANA);
			long hora = Db.getLong(cursor, HORA);
			long minuto = Db.getLong(cursor, MINUTO);
			return new AvisoTem(id, texto, activo, decode(mes), decode(diaMes), decode(diaSem), decode(hora), decode(minuto));
		}
	};

	//______________________________________________________________________________________________
	private static ContentValues code(AvisoTem o)
	{
		ContentValues cv = new ContentValues();
		cv.put(ID, o.getId());
		cv.put(TEXTO, o.getTexto());
		cv.put(ACTIVO, o.isActivo());
		cv.put(MES, getMesesDb(o));
		cv.put(DIA_MES, getDiasMesDb(o));
		cv.put(DIA_SEMANA, getDiasSemanaDb(o));
		cv.put(HORA, getHorasDb(o));
		cv.put(MINUTO, getMinutosDb(o));
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

	public static AvisoTem newObj(String id, String texto, boolean activo, long mes, long diaMes, long diaSem, long hora, long minuto)
	{
		return new AvisoTem(id, texto, activo, decode(mes), decode(diaMes), decode(diaSem), decode(hora), decode(minuto));
	}
	public static final int MAX_FECHAS = 8;
	public static long getMesesDb(AvisoTem o){return code(o.getMeses());}
	public static long getDiasMesDb(AvisoTem o){return code(o.getDiasMes());}
	public static long getDiasSemanaDb(AvisoTem o){return code(o.getDiasSemana());}
	public static long getHorasDb(AvisoTem o){return code(o.getHoras());}
	public static long getMinutosDb(AvisoTem o){return code(o.getMinutos());}
	/*public void setMesesDb(long v){_aMes=decode(v);}
	public void setDiasMesDb(long v){_aDiaMes=decode(v);}
	public void setDiasSemanaDb(long v){_aDiaSemana=decode(v);}
	public void setHorasDb(long v){_aHora=decode(v);}
	public void setMinutosDb(long v){_aMinuto=decode(v);}*/
	//
	private static long code(byte[] ab)
	{
		if(ab == null || ab.length == 0)return 0L;
		long res = 0;
		for(int i=0; i < MAX_FECHAS && i < ab.length; i++)
		{
			res = res << 8;
			res += ab[i];
		}
		return res;
	}
	private static byte[] decode(long l)
	{
		if(l == 0)return new byte[0];
		int i;
		byte[] ab = new byte[8];
		for(i=0; i < MAX_FECHAS; i++)
		{
			ab[i] = (byte)(l & 0xff);
			l = l >> 8;
			if(l == 0)break;
		}
		byte[] res = new byte[i+1];
		for(i=0; i < res.length; i++)res[i]=ab[res.length-1 - i];
		return res;
	}


}
