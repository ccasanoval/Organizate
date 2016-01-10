package com.cesoftware.Organizate.models;

import android.os.Parcel;
import android.os.Parcelable;

import com.orm.dsl.Ignore;
import com.orm.SugarRecord;

import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
///https://guides.codepath.com/android/Clean-Persistence-with-Sugar-ORM
/**
 * Created by Cesar_Casanova on 10/12/2015.
 */
//TODO: add numero de orden dentro del nivel
////////////////////////////////////////////////////////////////////////////////////////////////////
public class Objeto extends SugarRecord implements Parcelable
{
	public static final int NIVEL1 = 0;
	public static final int NIVEL2 = 1;
	public static final int NIVEL3 = 2;
	public static final int NIVEL_MAX = 3;

	private Long _idUsr = Long.valueOf(0);
	private Date _dtCreacion = new Date();
	private Date _dtModificado = new Date();
	private Date _dtLimite = new Date();
	private Integer _iPrioridad = Integer.valueOf(0);
	private String _sNombre = "";
	private String _sDescripcion = "";
	private Objeto _padre = null;
	@Ignore
	private Objeto[] _hijos = new Objeto[0];
	//TODO:hacer esto por id
	/*@Ignore
	private int _posicion[] = new int[NIVEL_MAX];//TODO: anadir posicion absoluta en _lista (para editar elementos mas rapido?)
	public int getPosicion(int nivel)
	{
		if(nivel >= NIVEL_MAX)return -1;
		return _posicion[nivel];
	}
	public void setPosicion(int nivel, int posicion)
	{
		if(nivel >= NIVEL_MAX)return;
		_posicion[nivel] = posicion;
	}*/

	//______________________________________________________________________________________________
	public Objeto(){}
	public Objeto(String s, Objeto padre){_sNombre=s; _padre=padre;}

	//______________________________________________________________________________________________
	@Override
	public String toString(){return "{id="+getId()+", pri="+_iPrioridad+", niv="+this.getNivel()+", mod="+_dtModificado+", nom="+_sNombre+", des="+_sDescripcion+", pad="+_padre+", hij="+_hijos.length+">>"+_hijos+"}";}
	public static void printLista(ArrayList<Objeto> lista){System.err.println("*****************");for(Objeto o : lista)System.err.println(o);System.err.println("*****************");}

	//______________________________________________________________________________________________
	@Override
	public boolean equals(Object obj)
	{
    	if(this == obj)return true;
    	if(obj == null)return false;
    	if(this.getClass() != obj.getClass())return false;
		Objeto o = (Objeto)obj;
		if(this.getId() == null || o.getId() == null)return (this.getNombre().compareTo(o.getNombre()) == 0);
		return (this.getId().compareTo(o.getId()) == 0);
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
System.err.println("\n"+this+"********************************"+len + " ::: "+hijo);
		if(len < 1)return;
		Objeto[] hijos = new Objeto[len-1];
		for(int i=0, j=0; i < len; i++)
			if(_hijos[i].equals(hijo))
			{
				isEliminado = true;
				System.err.println(this+"------------delHijo"+hijo);
			}
			else
				hijos[j++] = _hijos[i];
		if(isEliminado)
			_hijos = hijos;
	}

	//______________________________________________________________________________________________
	/*public void delHijos()
	{
		_hijos = new Objeto[0];
	}*/

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
		return i;
	}

	//______________________________________________________________________________________________
	public Long getIdUsr(){return _idUsr;}
	public void setIdUsr(Long idUsr){_idUsr = idUsr;}
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
	//public void setHijos(Objeto[] hijos){_hijos = hijos;}


	//______________________________________________________________________________________________
	/*@Override
	public Long getId()
	{
		return id;
	}

	@Override
	public Objeto setId(Long id)
	{
		this.id = id;
		return this;
	}*/

	//______________________________________________________________________________________________
	protected Objeto(Parcel in)
	{
		setId(in.readLong());
		_idUsr = in.readLong();
		_dtCreacion = new Date(in.readLong());
		_dtModificado = new Date(in.readLong());
		_dtLimite = new Date(in.readLong());
		_iPrioridad = in.readInt();
		_sNombre = in.readString();
		_sDescripcion = in.readString();
		_padre = in.readParcelable(Objeto.class.getClassLoader());
		_hijos = in.createTypedArray(Objeto.CREATOR);
	}
	@Override
	public void writeToParcel(Parcel dest, int flags)
	{
		dest.writeLong(getId());
		dest.writeLong(_idUsr);
		dest.writeLong(_dtCreacion.getTime());
		dest.writeLong(_dtModificado.getTime());
		dest.writeLong(_dtLimite.getTime());
		dest.writeInt(_iPrioridad);
		dest.writeString(_sNombre);
		dest.writeString(_sDescripcion);
		dest.writeParcelable(_padre, flags);
		dest.writeTypedArray(_hijos, flags);
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
	//public static void conectarHijos(ArrayList<Objeto> lista)
	public static ArrayList<Objeto> conectarHijos(Iterator<Objeto> it)
	{
		ArrayList<Objeto> lista = new ArrayList<>();
		ArrayList<Objeto> nivel1 = new ArrayList<>();
		while(it.hasNext())
		{
			lista.add(it.next());
		}
		for(Objeto o : lista)
		{
			if(o.getPadre() == null)
			{
				nivel1.add(o);
			}
			else
			{
				for(Objeto o1 : lista)
				{
					if(o1.getId().equals(o.getPadre().getId()))
					{
						o1.addHijo(o);
						break;
					}
				}
			}
			System.err.println("---"+o.toString());
		}
		//calcPosiciones(lista);
		//return nivel1;
		return lista;
	}
	//______________________________________________________________________________________________
	public static ArrayList<Objeto> filtroN(ArrayList<Objeto> lista, int iNivel)
	{
		if(lista == null)return null;
		ArrayList<Objeto> aNivel = new ArrayList<>();
		for(Objeto o : lista)
			if(o.getNivel() == iNivel)
				aNivel.add(o);
		return aNivel;
	}

	//______________________________________________________________________________________________
	/*public static void calcPosiciones(ArrayList<Objeto> lista)
	{
		ArrayList<Objeto> n1 = filtroN(lista, NIVEL1);
		ArrayList<Objeto> n2 = filtroN(lista, NIVEL1);
		ArrayList<Objeto> n3 = filtroN(lista, NIVEL1);

		int i = 0;
		for(Objeto o1 : n1)
		{
			o1.setPosicion(NIVEL1, i++);
			if(o1.getHijos().length > 0)
			{
				for(Objeto o2 : n1)
				{
				}
			}
		}
	}*/


	////////////////////////////////////////////////////////////////////////////////////////////////
/*	static class Iterador implements Iterable<Objeto>
	{
		private ArrayList<Objeto> _lista;
		public Iterador(ArrayList<Objeto> lista, boolean bPadres)
		{
			_lista = new ArrayList<>();
			for(Objeto o1 : lista)
			{
				_lista.add(o1);
				for(Objeto o2 : o1.getHijos())
				{
					_lista.add(o2);
					if( ! bPadres)
						for(Objeto o3 : o2.getHijos())
							_lista.add(o3);
				}
			}
		}
		public Iterator<Objeto> iterator()
		{
			return new PadreIterator();
		}
		////////////////////////////////////////////////////////////////////////////
		private class PadreIterator implements Iterator<Objeto>
		{
			private int _i=0;
			public PadreIterator()
			{
			}
			public boolean hasNext()
			{
				return _i < Iterador.this._lista.size()-1;
			}
			public Objeto next()
			{
				if(this.hasNext())
					return Iterador.this._lista.get(_i++);
				throw new NoSuchElementException();
			}
			public void remove()
			{
				throw new UnsupportedOperationException();
			}
		}
	}
		/*
		// Long way
		Iterator<Integer> it = range.iterator();
		while(it.hasNext()) {
		int cur = it.next();
		System.out.println(cur);
		}

		// Shorter, nicer way:		// Read ":" as "in"
		for(Integer cur : range) {
		System.out.println(cur);
		}
		}*/
}


/*
*
		ArrayList<Objeto> lista = new ArrayList<>();
		while(it.hasNext())
		{
			Objeto o = it.next();
			if(o.getPadre() == null)
			{
				lista.add(o);
			}
			else
			{
				boolean b = true;
				for(Objeto o1 : lista)
				{
					if(o2.getId().equals(o1.getPadre().getId()))
					{
						o2.addHijo(o1);
						b = false;
						break;
					}
					else if(o2.getHijos().length > 0)
					{
						for(Objeto o3 : o2.getHijos())
						{
							o3.addHijo(o2);
							b = false;
							break;
						}
					}
				}
			}
		}
* */