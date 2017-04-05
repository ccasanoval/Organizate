package com.cesoft.organizate.models;

import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;


import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;


/* When using proguard
As stated in the README from the github repo, if you are using proguard, you should ensure that entities remain un-obfuscated so table and columns are named correctly.
To do so, add this line to proguard-rules.pro:
-keep class com.cesoft.organizate.models.** { *; }
*/

///https://guides.codepath.com/android/Clean-Persistence-with-Sugar-ORM
////////////////////////////////////////////////////////////////////////////////////////////////////
/// Created by Cesar_Casanova on 10/12/2015
////////////////////////////////////////////////////////////////////////////////////////////////////
public class Objeto implements Parcelable
{
	private static final String TAG = Objeto.class.getSimpleName();
	public static final int NIVEL1 = 0;
	public static final String TOP_NODE = null;

	private String _id=null, _idPadre=null;
	private Integer _iOrden = 0;
	private Date _dtCreacion = new Date();
	private Date _dtModificado = new Date();
	private Date _dtLimite = new Date();
	private Integer _iPrioridad = 0;//Integer.valueOf(0);
	private String _sNombre = "";
	private String _sDescripcion = "";
	private Objeto _padre = null;
	private AvisoTem _avisoTem = null;
	private AvisoGeo _avisoGeo = null;

	private Objeto[] _hijos = new Objeto[0];

	//______________________________________________________________________________________________
	public Objeto(){}

	//______________________________________________________________________________________________
	@Override
	public String toString()
	{
		return String.format(Locale.ENGLISH, "{id=%s, pri=%d, niv=%d, mod=%s, nom=%s, des=%s, pad=%s, hij=%d >> %s, avi=%s, aviGeo=%s}",
			getId(), _iPrioridad, this.getNivel(), _dtModificado, _sNombre, _sDescripcion, _padre, _hijos.length, _hijos, _avisoTem, _avisoGeo);
	}

	//______________________________________________________________________________________________
	@Override
	public boolean equals(Object obj)
	{
    	if(this == obj)return true;
    	if(obj == null)return false;
    	if(this.getClass() != obj.getClass())return false;
		Objeto o = (Objeto)obj;
		if(this.getId() == null || o.getId() == null)
			return (this.getNombre().equals(o.getNombre()));
		return this.getId().equals(o.getId());
	}

	//______________________________________________________________________________________________
	public void addHijo(Objeto hijo)
	{
		if(equals(hijo))return;
		boolean isRepetido = false;
		int len = _hijos.length;
		Objeto[] hijos = new Objeto[len+1];
		for(int i=0; i < len; i++)
			if(isRepetido = (_hijos[i].equals(hijo)))
				break;
			else
				hijos[i] = _hijos[i];
		hijos[_hijos.length] = hijo;
		if(!isRepetido)
			_hijos = hijos;
	}

	//______________________________________________________________________________________________
	public void delHijo(Objeto hijo)
	{
		boolean isEliminado = false;
		int len = _hijos.length;
		if(len < 1)return;
		Objeto[] hijos = new Objeto[len-1];
		for(int i=0, j=0; i < len; i++)
			if(_hijos[i].equals(hijo))
				isEliminado = true;
			else
				hijos[j++] = _hijos[i];
		if(isEliminado)
			_hijos = hijos;
	}

	//______________________________________________________________________________________________
	public int getNivel()
	{
		int i=NIVEL1;
		Objeto o = _padre;
		while(o != null)
		{
			i++;
			o = o._padre;
		}
//Log.e(TAG, "getNivel------------"+_id+" : "+_idPadre+" :::: "+i);
		return i;
	}

	//______________________________________________________________________________________________
	public String getId(){return _id;}
	public void setId(String v){_id=v;}
	public String getIdPadre(){return _idPadre;}
	public void setIdPadre(String v){_idPadre=v;}
	public Integer getOrden(){return _iOrden;}
	public void setOrden(Integer iOrden){_iOrden = iOrden;}
	public Date getCreacion(){return _dtCreacion;}
	public void setCreacion(Date dtCreacion){_dtCreacion = dtCreacion;}
	public Date getLimite(){return _dtLimite;}
	public void setLimite(Date dtLimite){_dtLimite = dtLimite;}
	public Date getModificado(){return _dtModificado;}
	public void setModificado(Date dtModificado){_dtModificado = dtModificado;}
	public Integer getPrioridad(){return _iPrioridad;}
	public void setPrioridad(Integer iPrioridad){_iPrioridad = iPrioridad;}
	public String getNombre(){return _sNombre;}
	public void setNombre(String sText){_sNombre = sText;}
	public String getDescripcion(){return _sDescripcion;}
	public void setDescripcion(String sDescripcion){_sDescripcion = sDescripcion;}
	public Objeto getPadre(){return _padre;}
	public void setPadre(Objeto padre){_padre = padre;}
	public Objeto[] getHijos(){return _hijos;}
	//public void setHijos(DbObjeto[] hijos){_hijos = hijos;}
	public AvisoTem getAvisoTem(){return _avisoTem;}
	public void setAvisoTem(AvisoTem avisoTem){_avisoTem = avisoTem;}
	public AvisoGeo getAvisoGeo(){return _avisoGeo;}
	public void setAvisoGeo(AvisoGeo avisoGeo){_avisoGeo = avisoGeo;}


	//______________________________________________________________________________________________
	protected Objeto(Parcel in)
	{
		_id = in.readString();
		_iOrden = in.readInt();
		_iPrioridad = in.readInt();
		_dtCreacion = new Date(in.readLong());
		_dtModificado = new Date(in.readLong());
		_dtLimite = new Date(in.readLong());
		_sNombre = in.readString();
		_sDescripcion = in.readString();
		_avisoTem = in.readParcelable(AvisoTem.class.getClassLoader());
		_avisoGeo = in.readParcelable(AvisoGeo.class.getClassLoader());
		_padre = in.readParcelable(Objeto.class.getClassLoader());
		//_hijos = in.createTypedArray(Objeto.CREATOR);
	}
	@Override
	public void writeToParcel(Parcel dest, int flags)
	{
		dest.writeString(getId());
		dest.writeInt(_iOrden);
		dest.writeInt(_iPrioridad);
		dest.writeLong(_dtCreacion.getTime());
		dest.writeLong(_dtModificado.getTime());
		dest.writeLong(_dtLimite.getTime());
		dest.writeString(_sNombre);
		dest.writeString(_sDescripcion);
		dest.writeParcelable(_avisoTem, flags);
		dest.writeParcelable(_avisoGeo, flags);
		dest.writeParcelable(_padre, flags);
		//dest.writeTypedArray(_hijos, flags);
	}
	@Override
	public int describeContents()
	{
		return 0;
	}
	public static final Creator<Objeto> CREATOR = new Creator<Objeto>()
	{
		@Override
		public Objeto createFromParcel(Parcel in)
		{
			return new Objeto(in);
		}
		@Override
		public Objeto[] newArray(int size)
		{
			return new Objeto[size];
		}
	};


	//______________________________________________________________________________________________
	public static void conectarHijos(List<Objeto> lista)
	{
		for(Objeto o : lista)
		{
			if(o.getIdPadre() != null)
			{
				for(Objeto o1 : lista)
				{
					if(o1.getId().equals(o.getIdPadre()))
					{
						o.setPadre(o1);
						o1.addHijo(o);
						break;
					}
				}
			}
		}
	}
	public void fixPadres(List<Objeto> lista)
	{
		if(   !(getPadre() == null && _idPadre == TOP_NODE)
				&& (getPadre() == null || !getPadre().getId().equals(_idPadre)))
		{
			if(getPadre() != null)getPadre().delHijo(this);
			for(Objeto o1 : lista)
			{
				if(o1.getId().equals(_idPadre))
				{
					o1.addHijo(this);
					setPadre(o1);
					break;
				}
				else if(o1.getHijos().length > 0)
				{
					boolean b=false;
					for(Objeto o2 : o1.getHijos())
					{
						if(o2.getId().equals(_idPadre))
						{
							o2.addHijo(this);
							this.setPadre(o2);
							b=true;
							break;
						}
					}
					if(b)break;
				}
			}
		}
	}
	//______________________________________________________________________________________________
	public static ArrayList<Objeto> filtroN(List<Objeto> lista, int iNivel)
	{
		if(lista == null)return null;
		ArrayList<Objeto> aNivel = new ArrayList<>();
		for(Objeto o : lista)
			if(o.getNivel() == iNivel)
				aNivel.add(o);
		return aNivel;
	}

	//______________________________________________________________________________________________
	// BBDD
	//@Override
	public long save()
	{
		try
		{
			if(_avisoTem != null)_avisoTem.save();
			if(_avisoGeo != null)_avisoGeo.save();
			//return super.save();
			return -1;
		}
		catch(Exception e)
		{
			System.err.println("DbObjeto:save:e:"+e+" : "+this);
			return -1;
		}
	}
	public static void delTodo()
	{
		try
		{
			/*DbObjeto.deleteAll(DbObjeto.class);
			AvisoTem.deleteAll(AvisoTem.class);
			AvisoGeo.deleteAll(AvisoGeo.class);*/
		}
		catch(Exception e){System.err.println("DbObjeto:delTodo:e:"+e);}
	}
	//@Override
	public boolean delete()
	{
		/*if(_avisoTem != null)_avisoTem.delete();
		if(_avisoGeo != null)_avisoGeo.delete();
		for(DbObjeto o1 : getHijos())
			o1.delete();
		return super.delete();*/
		return true;
	}

	//______________________________________________________________________________________________
	public static Objeto getById(String id)
	{
		//return DbObjeto.findById(DbObjeto.class, Long.parseLong(id));
		return null;
	}


	/*public static final String TABLE = "tarea";
	public static final String QUERY = "SELECT * FROM "+TABLE+" ";

	public static final String ID = "_id";
	public static final String NOMBRE = "nombre";
	public static final String DESCRIPCION = "descripcion";
	public static final String ORDEN = "orden";
	public static final String PRIORIDAD = "prioridad";
	public static final String CREACION = "creacion";
	public static final String MODIFICADO = "modificado";
	public static final String LIMITE = "limite";
	public static final String ID_PADRE = "id_padre";*/
	//----------------------------------------------------------------------------------------------
	/*public static Func1<Cursor, Objeto> MAPPER = new Func1<Cursor, Objeto>()
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
	};*/
	public Objeto(String id, String nombre, String descripcion, int orden, int prioridad, Date creacion, Date modificado, Date limite, String idPadre)
	{
		_id = id;
		_iOrden = orden;
		_dtCreacion = creacion;
		_dtModificado = modificado;
		_dtLimite = limite;
		_iPrioridad = prioridad;
		_sNombre = nombre;
		_sDescripcion = descripcion;
		_idPadre = idPadre;
		//_avisoTem = in.readParcelable(AvisoTem.class.getClassLoader());
		//_avisoGeo = in.readParcelable(AvisoGeo.class.getClassLoader());
		//_padre = _padre;
		//_hijos = in.createTypedArray(DbObjeto.CREATOR);
	}
}
