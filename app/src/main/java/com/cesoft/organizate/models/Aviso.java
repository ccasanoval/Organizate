package com.cesoft.organizate.models;

import android.os.Parcel;
import android.os.Parcelable;

import com.orm.SugarRecord;

import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;

/**
 * Created by Cesar_Casanova on 12/01/2016
 */
////////////////////////////////////////////////////////////////////////////////////////////////////
public class Aviso extends SugarRecord implements Parcelable
{
	public static final byte NADA = -1;
	public static final byte TODO = -2;

	private boolean _bActivo = true;
	private String _sTexto="";
	//private Date _dt;//eliminar fecha, es inutil...
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

	//TODO: variable con periodo a aguardar para siguiente aviso: 1h, 1 dia...
	//@ Ignore
	private Date _dtActivo;//Fecha para desactivar un dia
	public void desactivarPorHoy()
	{
		_dtActivo = Calendar.getInstance().getTime();
		save();
	}

	public void setTexto(String s){_sTexto = s;}
	public String getTexto(){return _sTexto;}


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
		return _aMes;//.clone();
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
		else			_aDiaMes = remove(_aDiaMes, v);
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

	public void setActivo(boolean v){_bActivo = v;}
	public boolean getActivo(){return _bActivo;}

	///-----
	public Aviso(){}
	public String toString(){return "{id="+getId()+", act="+_bActivo+", diaM="+_aDiaMes.length+", diaS="+_aDiaSemana.length+", mes="+_aMes.length+", hor="+_aHora.length+", min="+_aMinuto.length+", txt="+_sTexto+", _dtAct="+_dtActivo+" }";}


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
		_bActivo = in.readByte() > 0;
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
		//
		dest.writeByte(_bActivo?(byte)1:(byte)0);
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

	//______________________________________________________________________________________________
	public long save()
	{
		System.err.println("SAVING AVISO:------" + this);
		java.util.Arrays.sort(_aMes);//quickSort(_aMes, 0, _aMes.length - 1);
		java.util.Arrays.sort(_aDiaMes);
		java.util.Arrays.sort(_aDiaSemana);
		java.util.Arrays.sort(_aHora);
		java.util.Arrays.sort(_aMinuto);
		return super.save();
	}

	//______________________________________________________________________________________________
	public static Iterator<Aviso> getActivos()
	{
		return Aviso.findAsIterator(Aviso.class, "_B_ACTIVO > 0");// .findAll(Aviso.class);
	}

	//______________________________________________________________________________________________
	public boolean isDueTime()
	{
		Calendar now = Calendar.getInstance();
//System.err.println("isDueTime-----------" + now);

		if(_dtActivo!=null &&  _dtActivo.getTime() + 24*60*60*1000 > now.getTimeInMillis())//Aun no ha pasado un dia //TODO:Mover a getAvisosActivos ?
		{
//System.err.println("isDueTime--------------------------ESTA DESACTIVADO TEMPORALMENTE : "+now+" : "+_dtActivo);
			return false;
		}
		_dtActivo = null;
//System.err.println("isDueTime-----------5 m:"+now.get(Calendar.MONTH)+1);

		byte[] aMeses = getMeses();
		byte[] aDiasMes = getDiasMes();
		byte[] aDiasSemana = getDiasSemana();
		byte[] aHoras = getHoras();
		byte[] aMinutos = getMinutos();
		if(aMeses.length > 0 && aMeses[0] != Aviso.TODO)
		{
			boolean b = false;
			for(byte mes : aMeses)
				if(b = now.get(Calendar.MONTH)+1 == mes)
					break;
			if(!b)return false;
		}
//System.err.println("isDueTime-----------6 dm:" + now.get(Calendar.DAY_OF_MONTH));
		if(aDiasMes.length > 0 && aDiasMes[0] != Aviso.TODO)
		{
			boolean b = false;
			for(byte diaMes : aDiasMes)
				if(b = now.get(Calendar.DAY_OF_MONTH) == diaMes)
					break;
			if(!b)return false;
		}
//System.err.println("isDueTime-----------7 ds:" + now.get(Calendar.DAY_OF_WEEK));
		if(aDiasSemana.length > 0 && aDiasSemana[0] != Aviso.TODO)
		{
			boolean b = false;
			for(byte diaSemana : aDiasSemana)
				if(b = now.get(Calendar.DAY_OF_WEEK) == diaSemana)
					break;
			if(!b)return false;
		}
//System.err.println("isDueTime-----------8 hor:" + now.get(Calendar.HOUR_OF_DAY) + " : " + now.get(Calendar.HOUR));
		if(aHoras.length > 0 && aHoras[0] != Aviso.TODO)
		{
			boolean b = false;
			for(byte hora : aHoras)
				if(b = now.get(Calendar.HOUR_OF_DAY) == hora)
					break;
			if(!b)return false;
		}
//System.err.println("isDueTime-----------9 min:" + now.get(Calendar.MINUTE));
		if(aMinutos.length > 0 && aMinutos[0] != Aviso.TODO)
		{
			boolean b = false;
			for(byte minuto : aMinutos)
				if(b = (now.get(Calendar.MINUTE)-2 <= minuto && now.get(Calendar.MINUTE)+2 >= minuto) )
					break;
//				else System.err.println("Aviso:isDueTime-----------PPP:"+now.get(Calendar.MINUTE)+":::"+minuto);
			if(!b)return false;
		}
		return true;
	}

	//______________________________________________________________________________________________
	/// HELPING FUNCS ------------------------------------------------------------------------------
	public static boolean contains(byte[] a, byte v)
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
		if(a.length == 0)return a;
		byte[] b = new byte[a.length-1];
		for(int i=0, j=0; i < a.length; i++)
			if(a[i] != v)
				b[j++]=a[i];
		return b;
	}
}

	/*
	// Los sustituyo por java.util.Arrays.sort(ab);
	public static void quickSort(byte[] ab, int low, int high)//int low = 0;	int high = ab.length - 1;
	{
		if(ab == null || ab.length == 0)return;
		if(low >= high)return;

		// pick the pivot
		int middle = low + (high - low) / 2;
		byte pivot = ab[middle];

		// make left < pivot and right > pivot
		int i = low, j = high;
		while(i <= j)
		{
			while(ab[i] < pivot)i++;
			while(ab[j] > pivot)j--;
			if(i <= j)
			{
				byte temp = ab[i];
				ab[i] = ab[j];
				ab[j] = temp;
				i++;
				j--;
			}
		}

		// recursively sort two sub parts
		if(low < j)
			quickSort(ab, low, j);
		if(high > i)
			quickSort(ab, i, high);
	}*/


/*
	//______________________________________________________________________________________________
	public boolean check(Date now)
	{
		boolean bOk;
		if(now == null) now = new Date();
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
* */


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