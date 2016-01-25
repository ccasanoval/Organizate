package com.cesoftware.Organizate.models;

import android.os.Parcel;
import android.os.Parcelable;

import com.orm.SugarRecord;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;

/**
 * Created by Cesar_Casanova on 12/01/2016.
 */
public class Aviso extends SugarRecord implements Parcelable
{
	private static final long OFFSET_DATE = 60*60*1000;//ms
	public static final int NADA = -1;
	public static final int TODO = -2;

	private Date _dt;
	//boolean[] bDiaMes = new boolean[31];
	private ArrayList<Integer> _aMes = new ArrayList<>();
	private ArrayList<Integer> _aDiaMes = new ArrayList<>();
	private ArrayList<Integer> _aDiaSemana = new ArrayList<>();
	private ArrayList<Integer> _aHora = new ArrayList<>();
	private ArrayList<Integer> _aMinuto = new ArrayList<>();

	/// MES
	public void addMes(Integer v)
	{
		if( !_aMes.contains(v) && v >= Calendar.JANUARY && v <= Calendar.DECEMBER)
			_aMes.add(v);
		if(v == TODO)
		{
			_aMes.clear();
			_aMes.add(v);
		}
	}
	public void delMes(Integer v)
	{
		if(v == TODO)	_aMes.clear();
		else			_aMes.remove(v);
	}
	public ArrayList<Integer> getMeses()
	{
		return (ArrayList<Integer>)_aMes.clone();//TODO:CHECK
	}

	/// DIA MES
	public void addDiaMes(Integer v)
	{
		if( !_aDiaMes.contains(v) && v > 0 && v < 32)
			_aDiaMes.add(v);
		if(v == TODO)
		{
			_aDiaMes.clear();
			_aDiaMes.add(v);
		}
	}
	public void delDiaMes(Integer v)
	{
		if(v == TODO)	_aDiaMes.clear();
		else			_aDiaMes.remove(v);//TODO:check
	}
	public ArrayList<Integer> getDiasMes()
	{
		return (ArrayList<Integer>)_aDiaMes.clone();
	}

	/// DIA SEMANA
	public void addDiaSemana(Integer v)
	{
		if( !_aDiaSemana.contains(v) && v >= Calendar.SUNDAY && v <= Calendar.SATURDAY)
			_aDiaSemana.add(v);
		if(v == TODO)
		{
			_aDiaSemana.clear();
			_aDiaSemana.add(v);
		}
	}
	public void delDiaSemana(Integer v)
	{
		if(v == TODO)	_aDiaSemana.clear();
		else			_aDiaSemana.remove(v);
	}
	public ArrayList<Integer> getDiasSemana()
	{
		return (ArrayList<Integer>)_aDiaSemana.clone();
	}

	/// HORA
	public void addHora(Integer v)
	{
		if( !_aHora.contains(v) && v >= 0 && v <= 23)
			_aHora.add(v);
		if(v == TODO)
		{
			_aHora.clear();
			_aHora.add(v);
		}
	}
	public void delHora(Integer v)
	{
		if(v == TODO)	_aHora.clear();
		else			_aHora.remove(v);
	}
	public ArrayList<Integer> getHoras()
	{
		return (ArrayList<Integer>)_aHora.clone();
	}

	/// MINUTO
	public void addMinuto(Integer v)
	{
		if( !_aMinuto.contains(v) && v >= 0 && v <= 59)
			_aMinuto.add(v);
	}
	public void delMinuto(Integer v)
	{
		if(v == TODO)	_aMinuto.clear();
		else			_aMinuto.remove(v);
	}
	public ArrayList<Integer> getMinutos()
	{
		return (ArrayList<Integer>)_aMinuto.clone();
	}

	///-----
	public Aviso(){}
	public String toString(){return "{id="+getId()+", dt="+_dt+", diaM="+_aDiaMes+", diaS="+_aDiaSemana+", mes="+_aMes+", hor="+_aHora+", min="+_aMinuto+"}";}


	///----- PARCELABLE
	protected Aviso(Parcel in)
	{
		long l;
		int i;
		int[] ai;
		//
		l = in.readLong();
		if(l >= 0)setId(l);
		//
		l = in.readLong();
		if(l >= 0)_dt = new Date(l);
		//
		ai = in.createIntArray();
		for(i=0; i < ai.length; i++)_aMes.add(ai[i]);
		ai = in.createIntArray();
		for(i=0; i < ai.length; i++)_aDiaMes.add(ai[i]);
		ai = in.createIntArray();
		for(i=0; i < ai.length; i++)_aDiaSemana.add(ai[i]);
		ai = in.createIntArray();
		for(i=0; i < ai.length; i++)_aHora.add(ai[i]);
		ai = in.createIntArray();
		for(i=0; i < ai.length; i++)_aMinuto.add(ai[i]);
	}
	@Override
	public void writeToParcel(Parcel dest, int flags)
	{
		int[] ai;
		dest.writeLong(getId()!=null?getId():-1);
		dest.writeLong(_dt!=null?_dt.getTime():-1);
		//
		ai = convertIntegers(_aMes);
		dest.writeIntArray(ai);
		ai = convertIntegers(_aDiaMes);
		dest.writeIntArray(ai);
		ai = convertIntegers(_aDiaSemana);
		dest.writeIntArray(ai);
		ai = convertIntegers(_aHora);
		dest.writeIntArray(ai);
		ai = convertIntegers(_aMinuto);
		dest.writeIntArray(ai);
	}
	@Override
	public int describeContents()
	{
		return 0;
	}
	public static final Creator<Aviso> CREATOR = new Creator<Aviso>()
	{
		@Override
		public Aviso createFromParcel(Parcel in)
		{
			return new Aviso(in);
		}
		@Override
		public Aviso[] newArray(int size)
		{
			return new Aviso[size];
		}
	};


	private static int[] convertIntegers(ArrayList<Integer> ai)
	{
		int[] ret = new int[ai.size()];
		Iterator<Integer> iterator = ai.iterator();
		for(int i=0; i < ret.length; i++)
		{
			ret[i] = iterator.next();//.intValue();
System.err.println("--"+ret[i]);
		}
		return ret;
	}

	//TODO: Servicio que recorra objetos de db, coja sus avisos y los procese....
	//TODO: Pantalla para introducir al menos un aviso por objeto
	///-----
	public boolean check(Date now)
	{
		if(now == null) now = new Date();

		// DATE
		if(_dt != null)
		{
			long dif = _dt.getTime() - now.getTime();
			if(dif < 0) dif = -dif;
			if(dif < OFFSET_DATE) return true;
		}

		//
		boolean bOk;
		Calendar c = Calendar.getInstance();
		c.setTime(now);
		int minuto = c.get(Calendar.MINUTE);
		int hora = c.get(Calendar.HOUR_OF_DAY);
		int diaSem = c.get(Calendar.DAY_OF_WEEK);
		int diaMes = c.get(Calendar.DAY_OF_MONTH);
		int mes = c.get(Calendar.MONTH);

		// mes
		bOk = false;
		for(int v : _aMes)
		{
			if(v == mes)
			{
				bOk=true;
				break;
			}
		}
		if( ! bOk)return false;

		// dia mes
		bOk = false;
		for(int v : _aDiaMes)
		{
			if(v == diaMes)
			{
				bOk=true;
				break;
			}
		}
		if( ! bOk)return false;

		// dia semana
		bOk = false;
		for(int v : _aDiaSemana)
		{
			if(v == diaSem)
			{
				bOk=true;
				break;
			}
		}
		if( ! bOk)return false;

		// hora
		bOk = false;
		for(int v : _aHora)
		{
			if(v == hora)
			{
				bOk=true;
				break;
			}
		}
		if( ! bOk)return false;

		// minuto
		bOk = false;
		for(int v : _aMinuto)
		{
			if(v == minuto)
			{
				bOk=true;
				break;
			}
		}
		return bOk;
	}
}
