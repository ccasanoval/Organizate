package com.cesoftware.Organizate.models;

import android.os.Parcel;
import android.os.Parcelable;

import com.orm.SugarRecord;

import java.util.Calendar;
import java.util.Date;

/**
 * Created by Cesar_Casanova on 12/01/2016
 */
public class Aviso extends SugarRecord implements Parcelable
{
	private static final long OFFSET_DATE = 60*60*1000;//ms
	public static final byte NADA = -1;
	public static final byte TODO = -2;

	private String _sTexto="";
	private Date _dt;
	private byte[] _aMes = new byte[0];
	private byte[] _aDiaMes = new byte[0];
	private byte[] _aDiaSemana = new byte[0];
	private byte[] _aHora = new byte[0];
	private byte[] _aMinuto = new byte[0];
	/* Sugar no guarda array list...
	private ArrayList<Integer> _aMes = new ArrayList<>();
	private ArrayList<Integer> _aDiaMes = new ArrayList<>();
	private ArrayList<Integer> _aDiaSemana = new ArrayList<>();
	private ArrayList<Integer> _aHora = new ArrayList<>();
	private ArrayList<Integer> _aMinuto = new ArrayList<>();*/

	public void setTexto(String s){_sTexto = s;}
	public String getTexto(){return _sTexto;}

	/// HELPING FUNCS
	private static boolean contains(byte[] a, byte v)
	{
		for(byte b : a)if(b == v)return true;
		return false;
	}
	private static byte[] add(byte[] a, byte v)
	{
		byte[] b = new byte[a.length+1];
		System.arraycopy(a, 0, b, 0, a.length);//for(int i=0; i < a.length; i++)b[i]=a[i];
		b[b.length-1]=v;
		return b;
	}
	private static byte[] remove(byte[] a, byte v)
	{
System.err.println("AAA-----z:"+v);
		if(a.length == 0)return a;
		byte[] b = new byte[a.length-1];
		for(int i=0, j=0; i < a.length; i++)
			if(a[i] != v)
				b[j++]=a[i];
System.err.println("AAA-----"+b.length);
		return b;
	}


	/// MES
	public void addMes(byte v)
	{
		if( !contains(_aMes, v) && v >= Calendar.JANUARY && v <= Calendar.DECEMBER)
			_aMes = add(_aMes, v);
		if(v == TODO)
		{
			_aMes = new byte[0];
			_aMes = add(_aMes, v);
		}
	}
	public void delMes(byte v)
	{
		if(v == TODO)	_aMes = new byte[0];
		else			_aMes = remove(_aMes, v);
	}
	public byte[] getMeses()
	{
		return _aMes;//.clone();//TODO:CHECK
	}

	/// DIA MES
	public void addDiaMes(byte v)
	{
		if( !contains(_aDiaMes, v) && v > 0 && v < 32)
			_aDiaMes = add(_aDiaMes, v);
		if(v == TODO)
		{
			_aDiaMes = new byte[0];
			_aDiaMes = add(_aDiaMes, v);
		}
	}
	public void delDiaMes(byte v)
	{
		if(v == TODO)	_aDiaMes = new byte[0];
		else			_aDiaMes = remove(_aDiaMes, v);//TODO:check
	}
	public byte[] getDiasMes()
	{
		return _aDiaMes;//.clone();
	}

	/// DIA SEMANA
	public void addDiaSemana(byte v)
	{
		if( !contains(_aDiaSemana, v) && v >= Calendar.SUNDAY && v <= Calendar.SATURDAY)
			_aDiaSemana = add(_aDiaSemana, v);
		if(v == TODO)
		{
			_aDiaSemana = new byte[0];
			_aDiaSemana = add(_aDiaSemana, v);
		}
	}
	public void delDiaSemana(byte v)
	{
		if(v == TODO)	_aDiaSemana = new byte[0];
		else			_aDiaSemana = remove(_aDiaSemana, v);
	}
	public byte[] getDiasSemana()
	{
		return _aDiaSemana;//.clone();
	}

	/// HORA
	public void addHora(byte v)
	{
		if( !contains(_aHora, v) && v >= 0 && v <= 23)
			_aHora = add(_aHora, v);
		if(v == TODO)
		{
			_aHora = new byte[0];
			_aHora = add(_aHora, v);
		}
	}
	public void delHora(byte v)
	{
		if(v == TODO)	_aHora = new byte[0];
		else			_aHora = remove(_aHora, v);
	}
	public byte[] getHoras()
	{
		return _aHora;//.clone();
	}

	/// MINUTO
	public void addMinuto(byte v)
	{
		if( !contains(_aMinuto, v) && v >= 0 && v <= 59)
			_aMinuto = add(_aMinuto, v);
	}
	public void delMinuto(byte v)
	{
		if(v == TODO)	_aMinuto = new byte[0];
		else			_aMinuto = remove(_aMinuto, v);
	}
	public byte[] getMinutos()
	{
		return _aMinuto;//.clone();
	}

	///-----
	public Aviso(){}
	public String toString(){return "{id="+getId()+", dt="+_dt+", diaM="+_aDiaMes.length+", diaS="+_aDiaSemana.length+", mes="+_aMes.length+", hor="+_aHora.length+", min="+_aMinuto.length+", txt="+_sTexto+"}";}


	///----- PARCELABLE
	protected Aviso(Parcel in)
	{
		long l;
		int i;
		byte[] ai;
		//
		l = in.readLong();
		if(l >= 0)setId(l);
		//
		l = in.readLong();
		if(l >= 0)_dt = new Date(l);
		//
		_sTexto = in.readString();
		//
		ai = in.createByteArray();
		for(i=0; i < ai.length; i++)_aMes = add(_aMes, ai[i]);
		ai = in.createByteArray();
		for(i=0; i < ai.length; i++)_aDiaMes = add(_aDiaMes, ai[i]);
		ai = in.createByteArray();
		for(i=0; i < ai.length; i++)_aDiaSemana = add(_aDiaSemana, ai[i]);
		ai = in.createByteArray();
		for(i=0; i < ai.length; i++)_aHora = add(_aHora, ai[i]);
		ai = in.createByteArray();
		for(i=0; i < ai.length; i++)_aMinuto = add(_aMinuto, ai[i]);
	}
	@Override
	public void writeToParcel(Parcel dest, int flags)
	{
		int[] ai;
		dest.writeLong(getId() != null ? getId() : -1);
		dest.writeLong(_dt != null ? _dt.getTime() : -1);
		//
		dest.writeString(_sTexto);
		//
		dest.writeByteArray(_aMes);
		dest.writeByteArray(_aDiaMes);
		dest.writeByteArray(_aDiaSemana);
		dest.writeByteArray(_aHora);
		dest.writeByteArray(_aMinuto);
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


	/*private static int[] convertIntegers(ArrayList<Integer> ai)
	{
		int[] ret = new int[ai.size()];
		Iterator<Integer> iterator = ai.iterator();
		for(int i=0; i < ret.length; i++)
		{
			ret[i] = iterator.next();//.intValue();
System.err.println("--"+ret[i]);
		}
		return ret;
	}*/

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



	//______________________________________________________________________________________________
	public long save()
	{
		System.err.println("SAVING AVISO:------" + this);
		return super.save();
	}
}
