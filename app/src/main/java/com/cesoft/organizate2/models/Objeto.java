package com.cesoft.organizate2.models;

import android.os.Parcel;
import android.os.Parcelable;

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
	private void addHijo(Objeto hijo)
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
	private void delHijo(Objeto hijo)
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
	private int getNivel()
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
	//public void setOrden(Integer iOrden){_iOrden = iOrden;}
	public Date getCreacion(){return _dtCreacion;}
	public void setCreacion(Date dtCreacion){_dtCreacion = dtCreacion;}
	public Date getLimite(){return _dtLimite;}
	//public void setLimite(Date dtLimite){_dtLimite = dtLimite;}
	public Date getModificado(){return _dtModificado;}
	public void setModificado(Date dtModificado){_dtModificado = dtModificado;}
	public Integer getPrioridad(){return _iPrioridad;}
	public void setPrioridad(Integer iPrioridad){_iPrioridad = iPrioridad;}
	public String getNombre(){return _sNombre;}
	public void setNombre(String sText){_sNombre = sText;}
	public String getDescripcion(){return _sDescripcion;}
	public void setDescripcion(String sDescripcion){_sDescripcion = sDescripcion;}
	public Objeto getPadre(){return _padre;}
	private void setPadre(Objeto padre){_padre = padre;}
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

	//______________________________________________________________________________________________
	public static boolean hayAvisoActivo(List<Objeto> lista)
	{
		for(Objeto o : lista)
		{
			if(o.getAvisoTem() != null && o.getAvisoTem().isActivo())return true;
			if(o.getAvisoGeo() != null && o.getAvisoGeo().isActivo())return true;
		}
		return false;
	}
}
