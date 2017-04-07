package com.cesoft.organizate;

import android.app.Application;
import android.util.Log;

import com.cesoft.organizate.db.DbObjeto;
import com.cesoft.organizate.models.Objeto;
import com.squareup.sqlbrite.BriteDatabase;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import javax.inject.Inject;

////////////////////////////////////////////////////////////////////////////////////////////////////
// Preseter Edit
public class PreEdit
{
	private static final String TAG = PreEdit.class.getSimpleName();

	//private ActEdit _view;

	private Application _app;
	private BriteDatabase _db;
	@Inject
	PreEdit(Application app, BriteDatabase db)
	{
		_db = db;
		_app = app;
	}

	//----------------------------------------------------------------------------------------------
	void unsubscribe()
	{
		//_view = null;
	}

	//----------------------------------------------------------------------------------------------
	void subscribe(ActEdit view)
	{
		//_view = view;
	}

	//----------------------------------------------------------------------------------------------
	private static final String PADRE = "+ ";
	private static final String HIJO = "   - ";
	private static final String SEP = "::";
	List<String> get()
	{
		//return App.getLista(_app);
		List<String> lst = new ArrayList<>();
		lst.add(_app.getString(R.string.nodo_padre)+SEP+Objeto.TOP_NODE);
		List<Objeto> lista = App.getLista(_app);
		if(lista != null && lista.size()>0)
		for(Objeto o : Objeto.filtroN(lista, Objeto.NIVEL1))
		{
			lst.add(PADRE + o.getNombre() + SEP + o.getId());
			if(o.getHijos().length > 0)
				for(Objeto o2 : o.getHijos())
					lst.add(HIJO + o2.getNombre() + SEP + o2.getId());
		}
		return lst;
	}
	String getSelectedId(String txt)
	{
		return txt.replace(HIJO, "").replace(PADRE, "");
	}
	String getId(String item)
	{
		if(item != null)
		{
			String[] itemArr = item.split(SEP);
			return itemArr[1];
		}
		return "0";
	}
	String getTexto(String item)
	{
		if(item != null)
		{
			String[] itemArr = item.split(SEP);
			return itemArr[0];
		}
		return "";
	}
	//----------------------------------------------------------------------------------------------
	void save(Objeto o, boolean isNuevo)
	{
		List<Objeto> lista = App.getLista(_app);
		if(isNuevo)
		{
			o.setId(UUID.randomUUID().toString());
			o.setCreacion(new Date());
			if(o.getAvisoGeo() != null)o.getAvisoGeo().setId(o.getId());
			if(o.getAvisoTem() != null)o.getAvisoTem().setId(o.getId());
			lista.add(o);
		}
		else
		{
			lista.set(lista.indexOf(o), o);
		}
		o.fixPadres(lista);
		DbObjeto.saveAll(_db, lista);
	}
	//----------------------------------------------------------------------------------------------
	void del(Objeto o)
	{
		DbObjeto.delete(_db, o);
	}
}
