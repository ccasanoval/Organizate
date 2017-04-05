package com.cesoft.organizate.db;

import com.cesoft.organizate.models.Objeto;
import com.squareup.sqlbrite.BriteDatabase;

import android.content.ContentValues;
import android.database.Cursor;
import android.util.Log;

import java.util.Date;
import java.util.List;

import rx.functions.Func1;


////////////////////////////////////////////////////////////////////////////////////////////////////
public class DbObjeto
{
	private static final String TAG = DbObjeto.class.getSimpleName();

	public static final String TABLE = "tarea";
	public static final String QUERY = "SELECT * FROM "+TABLE+" ";

	static final String ID = "_id";
	static final String NOMBRE = "nombre";
	static final String DESCRIPCION = "descripcion";
	static final String ORDEN = "orden";
	static final String PRIORIDAD = "prioridad";
	static final String CREACION = "creacion";
	static final String MODIFICADO = "modificado";
	static final String LIMITE = "limite";
	static final String ID_PADRE = "id_padre";

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
			String id = Db.getString(cursor, ID);
			String nombre = Db.getString(cursor, NOMBRE);
			String descripcion = Db.getString(cursor, DESCRIPCION);
			int orden = Db.getInt(cursor, ORDEN);
			int prioridad = Db.getInt(cursor, PRIORIDAD);
			Date creacion = new Date(Db.getLong(cursor, CREACION));
			Date modificado = new Date(Db.getLong(cursor, MODIFICADO));
			Date limite = new Date(Db.getLong(cursor, LIMITE));
			String idPadre = Db.getString(cursor, ID_PADRE);
			return new Objeto(id, nombre, descripcion, orden, prioridad, creacion, modificado, limite, idPadre);
		}
	};

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
			db.delete(DbObjeto.TABLE, "", null);
			db.delete(DbAvisoGeo.TABLE, "", null);
			db.delete(DbAvisoTem.TABLE, "", null);
			for(Objeto o : lista)
			{
				db.insert(DbObjeto.TABLE, code(o));
			}
			//TOdO: insertar avisos
			//DbAvisoGeo.saveAll(db, lista);
			//DbAvisoTem.saveAll(db, lista);
		}
		catch(Exception e)
		{
			Log.e(TAG, "saveAll:e:------------------------------------------------------------------", e);
		}
	}
	public static void delete(BriteDatabase db, Objeto o)
	{
		db.delete(TABLE, "WHERE "+ID+" LIKE ?", o.getId());
	}
	//______________________________________________________________________________________________
	/*public static long save(Objeto o)
	{
		try
		{
			ContentValues cv = new ContentValues();
			cv.put(DbObjeto.ID, o.getId());//UUID.randomUUID().toString());
			cv.put(DbObjeto.CREACION, o.getCreacion().getTime());
			cv.put(DbObjeto.MODIFICADO, o.getModificado().getTime());
			cv.put(DbObjeto.LIMITE, o.getLimite().getTime());
			cv.put(DbObjeto.ORDEN, o.getOrden());
			cv.put(DbObjeto.PRIORIDAD, o.getPrioridad());
			cv.put(DbObjeto.NOMBRE, o.getNombre());
			cv.put(DbObjeto.DESCRIPCION, o.getDescripcion());
			cv.put(DbObjeto.ID_PADRE, o.getIdPadre());
			long i1 = db.insert(DbObjeto.TABLE, null, cv);
			//
			if(o.getAvisoTem() != null)DbAvisoTem.save(o.getAvisoTem());
			if(o.getAvisoGeo() != null)DbAvisoGeo.save(o.getAvisoGeo());
			return 0;
		}
		catch(Exception e)
		{
			Log.e("DbObjeto", "save:e:------------------------------------------------------------"+e);
			return -1;
		}
	}
	public static void delTodo()
	{
		try
		{
			DbObjeto.deleteAll(DbObjeto.class);
			AvisoTem.deleteAll(AvisoTem.class);
			AvisoGeo.deleteAll(AvisoGeo.class);
		}
		catch(Exception e){System.err.println("DbObjeto:delTodo:e:"+e);}
	}
	//@Override
	public boolean delete()
	{
		if(_avisoTem != null)_avisoTem.delete();
		if(_avisoGeo != null)_avisoGeo.delete();
		for(DbObjeto o1 : getHijos())
			o1.delete();
		return super.delete();
		return true;
	}

	//______________________________________________________________________________________________
	public static Objeto getById(String id)
	{
		//return DbObjeto.findById(DbObjeto.class, Long.parseLong(id));
		return null;
	}*/

}
