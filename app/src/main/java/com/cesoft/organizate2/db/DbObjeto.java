package com.cesoft.organizate2.db;

import com.cesoft.organizate2.models.AvisoGeo;
import com.cesoft.organizate2.models.AvisoTem;
import com.cesoft.organizate2.models.Objeto;
import com.cesoft.organizate2.util.Log;
import com.squareup.sqlbrite.BriteDatabase;

import android.content.ContentValues;
import android.database.Cursor;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

//TODO: transactions : http://www.programcreek.com/java-api-examples/index.php?api=com.squareup.sqlbrite.BriteDatabase
////////////////////////////////////////////////////////////////////////////////////////////////////
public class DbObjeto
{
	private static final String TAG = DbObjeto.class.getSimpleName();

	private static final String ID = "_id";
	private static final String NOMBRE = "nombre";
	private static final String DESCRIPCION = "descripcion";
	private static final String ORDEN = "orden";
	private static final String PRIORIDAD = "prioridad";
	private static final String CREACION = "creacion";
	private static final String MODIFICADO = "modificado";
	private static final String LIMITE = "limite";
	private static final String ID_PADRE = "id_padre";

	public static final String TABLE = "tarea";
	//public static final String QUERY = "SELECT * FROM "+TABLE+" ";
	public static final String QUERY =
			"SELECT * "
					//+DbObjeto.TABLE+".*, "
					//+DbAvisoTem.TABLE+".*, "
					//+DbAvisoGeo.TABLE+".*, "
					//+DbAvisoTem.TABLE+"."+DbAvisoTem.ACTIVO+" as "
				+" FROM "+DbObjeto.TABLE
				+" LEFT OUTER JOIN "+DbAvisoTem.TABLE+" ON "+DbObjeto.TABLE+"."+ID+" = "+DbAvisoTem.TABLE+"."+DbAvisoTem.ID
				+" LEFT OUTER JOIN "+DbAvisoGeo.TABLE+" ON "+DbObjeto.TABLE+"."+ID+" = "+DbAvisoGeo.TABLE+"."+DbAvisoGeo.ID
				;

	//CREATE TABLE IF NOT EXISTS
	static final String SQL_CREATE_TABLE =
			"CREATE TABLE "+ TABLE+" ( "
					+ DbObjeto.ID			+ " TEXT   NOT NULL   PRIMARY KEY,"
					+ DbObjeto.CREACION		+ " INTEGER,"
					+ DbObjeto.MODIFICADO	+ " INTEGER,"
					+ DbObjeto.LIMITE		+ " INTEGER,"
					+ DbObjeto.ORDEN		+ " INTEGER,"
					+ DbObjeto.PRIORIDAD	+ " INTEGER,"
					+ DbObjeto.NOMBRE		+ " TEXT,"
					+ DbObjeto.DESCRIPCION	+ " TEXT,"
					+ DbObjeto.ID_PADRE		+ " TEXT   REFERENCES "+DbObjeto.TABLE+" ("+DbObjeto.ID+") "
					+" )";
	static final String SQL_CREATE_INDEX1 =
			"CREATE UNIQUE INDEX idx_id_"+TABLE+" ON "+DbObjeto.TABLE+" ("+DbObjeto.ID+")";
	static final String SQL_CREATE_INDEX2 =
			"CREATE INDEX idx_padre_id_"+TABLE+" ON "+DbObjeto.TABLE+" ("+DbObjeto.ID_PADRE+")";

	//----------------------------------------------------------------------------------------------
	public static Func1<Cursor, Objeto> MAPPER = new Func1<Cursor, Objeto>()
	{
		@Override public Objeto call(final Cursor cursor)
		{
			int i = -1;
			// Objeto
			String id = cursor.getString(++i);//TODO: array ordenado por cambios...
			Date creacion = new Date(cursor.getLong(++i));
			Date modificado = new Date(cursor.getLong(++i));
			Date limite = new Date(cursor.getLong(++i));
			int orden = cursor.getInt(++i);
			int prioridad = cursor.getInt(++i);
			String nombre = cursor.getString(++i);
			String descripcion = cursor.getString(++i);
			String idPadre = cursor.getString(++i);
			// AvisoTem
			++i;//String idAT = cursor.getString(++i);
			String textoAT = cursor.getString(++i);
			boolean activoAT = cursor.getInt(++i) > 0;
			byte[] mes = cursor.getBlob(++i);
			byte[] diames = cursor.getBlob(++i);
			byte[] diasem = cursor.getBlob(++i);
			byte[] hora = cursor.getBlob(++i);
			byte[] minuto = cursor.getBlob(++i);
			if(mes == null)mes = new byte[0];
			if(diames == null)diames = new byte[0];
			if(diasem == null)diasem = new byte[0];
			if(hora == null)hora = new byte[0];
			if(minuto == null)minuto = new byte[0];
			// AvisoGeo
			++i;//String idAG = cursor.getString(++i);
			String textoAG = cursor.getString(++i);
			boolean activoAG = cursor.getInt(++i) > 0;
			double latitud = cursor.getDouble(++i);
			double longitud = cursor.getDouble(++i);
			float radio = cursor.getFloat(++i);
			//
			Objeto o = new Objeto(id, nombre, descripcion, orden, prioridad, creacion, modificado, limite, idPadre);
			o.setAvisoGeo(new AvisoGeo(id, textoAG, activoAG, latitud, longitud, radio));
			o.setAvisoTem(new AvisoTem(id, textoAT, activoAT, mes, diames, diasem, hora, minuto));
			return o;
		}
	};

	//----------------------------------------------------------------------------------------------
	private static ContentValues code(Objeto o)
	{
		ContentValues cv = new ContentValues();
		cv.put(DbObjeto.ID, o.getId());
		cv.put(DbObjeto.CREACION, o.getCreacion().getTime());
		cv.put(DbObjeto.MODIFICADO, o.getModificado().getTime());
		cv.put(DbObjeto.LIMITE, o.getLimite().getTime());
		cv.put(DbObjeto.ORDEN, o.getOrden());
		cv.put(DbObjeto.PRIORIDAD, o.getPrioridad());
		cv.put(DbObjeto.NOMBRE, o.getNombre());
		cv.put(DbObjeto.DESCRIPCION, o.getDescripcion());
		cv.put(DbObjeto.ID_PADRE, o.getIdPadre());
		return cv;
	}
	//______________________________________________________________________________________________
	public static void saveAll(BriteDatabase db, List<Objeto> lista)
	{
		try
		{
			if(db == null)
			{
				Log.e(TAG, "saveAll:e:------------------------------------------------------------------ DB == NULL");
				return;
			}
			db.delete(DbObjeto.TABLE, null);
			db.delete(DbAvisoGeo.TABLE, null);
			db.delete(DbAvisoTem.TABLE, null);
			for(Objeto o : lista)
			{
				db.insert(DbObjeto.TABLE, code(o));
				DbAvisoGeo.save(db, o.getAvisoGeo());
				DbAvisoTem.save(db, o.getAvisoTem());
			}
		}
		catch(Exception e)
		{
			Log.e(TAG, "saveAll:e:------------------------------------------------------------------", e);
		}
	}
	//----------------------------------------------------------------------------------------------
	public static void delete(BriteDatabase db, Objeto o)
	{
		db.delete(TABLE, ID+" LIKE ?", o.getId());
	}


	//----------------------------------------------------------------------------------------------
	public interface Listener<T>
	{
		void onError(Throwable t);
		void onDatos(List<T> lista);
	}
	//----------------------------------------------------------------------------------------------
	public static void getListaSync(BriteDatabase db, final Listener<Objeto> listener)
	{
		//String LISTA0 = QUERY+" where "+ID_PADRE+" is not null and "+PRIORIDAD+" > 3 order by "+PRIORIDAD+" desc";
		//String LISTA1 = QUERY+" where "+ID_PADRE+" is not null order by "+PRIORIDAD+" desc";
		//String LISTA2 = QUERY+" where "+PRIORIDAD+" > 3 order by "+PRIORIDAD+" desc";
		String LISTA2 = QUERY+" order by "+PRIORIDAD+" desc";

		db.createQuery(DbObjeto.TABLE, LISTA2)
			.mapToList(DbObjeto.MAPPER)
			.observeOn(Schedulers.immediate())
			.subscribeOn(Schedulers.immediate())
			.doOnError(new Action1<Throwable>()
			{
				@Override
				public void call(Throwable err)
				{
					listener.onError(err);
				}
			})
			.subscribe(new Action1<List<Objeto>>()
			{
				@Override
				public void call(List<Objeto> l)
				{
					listener.onDatos(l);
				}
			});
	}
}