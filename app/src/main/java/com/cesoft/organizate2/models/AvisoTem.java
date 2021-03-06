package com.cesoft.organizate2.models;

import android.os.Parcel;

import com.cesoft.organizate2.util.Log;

import java.util.Calendar;
import java.util.Date;

// Created by Cesar_Casanova on 12/01/2016
////////////////////////////////////////////////////////////////////////////////////////////////////
public class AvisoTem extends AvisoAbs
{
	private static final String TAG = AvisoTem.class.getSimpleName();
	/*@Ignore
	protected Objeto _o;
		public Objeto getObjeto(){return _o;}
		public void setObjeto(Objeto o){_o=o;}*/
	public Objeto getObjeto()
	{
		//TODO
		/*List<Objeto> ao = Objeto.find(Objeto.class, "_AVISO_TEM = ?", getId().toString());
		if(ao.size() > 0)return ao.get(0);*/
		return null;
	}

	//@ Ignore
	private Date _dtActivo = new Date(0);//Fecha para desactivar un dia //TODO: variable con periodo a aguardar para siguiente aviso: 1h, 1 dia...
	public void desactivarPorHoy()
	{
		_dtActivo = Calendar.getInstance().getTime();
		save();
	}
	public void reactivarPorHoy()
	{
		//Calendar cal = Calendar.getInstance();cal.add(Calendar.DATE, -2);//cal.getTime();
		_dtActivo = new Date(0);
		save();
	}

	//---Temp
	public static final byte NADA = -1;
	public static final byte TODO = -2;

	private byte[] _aMes = new byte[0];
	private byte[] _aDiaMes = new byte[0];
	private byte[] _aDiaSemana = new byte[0];
	private byte[] _aHora = new byte[0];
	private byte[] _aMinuto = new byte[0];
	// Sugar no guarda array list... ArrayList<Integer> _aMes = new ArrayList<>();

	private static final int MAX_FECHAS = 20;
	public boolean isLimitMes(){return _aMes.length >= MAX_FECHAS;}
	public boolean isLimitDiaMes(){return _aDiaMes.length >= MAX_FECHAS;}
	public boolean isLimitDiaSemana(){return _aDiaSemana.length >= MAX_FECHAS;}
	public boolean isLimitHora(){return _aHora.length >= MAX_FECHAS;}
	public boolean isLimitMinuto(){return _aMinuto.length >= MAX_FECHAS;}

	//public AvisoTem(){}//NO BORRAR: Necesario para sugar
	public AvisoTem(String id, String msg){_id = id; _sTexto = msg;}
	public AvisoTem(String id, String texto, boolean activo, byte[] mes, byte[] diaMes, byte[] diaSem, byte[] hora, byte[] minuto)
	{
		_id = id;
		_sTexto = texto;
		_bActivo = activo;
		_aMes=(mes);
		_aDiaMes=(diaMes);
		_aDiaSemana=(diaSem);
		_aHora=(hora);
		_aMinuto=(minuto);
	}

	/// MES ----------------------------------------------------------------------------------------
	public void addMes(byte v)
	{
		if(v == TODO)
		{
			_aMes = new byte[0];
			_aMes = add(_aMes, v);
		}
		else if(_aMes.length < MAX_FECHAS && !contains(_aMes, v) && v >= Calendar.JANUARY && v <= Calendar.DECEMBER)
			_aMes = add(_aMes, v);
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

	/// DIA MES ------------------------------------------------------------------------------------
	public void addDiaMes(byte v)
	{
		if(v == TODO)
		{
			_aDiaMes = new byte[0];
			_aDiaMes = add(_aDiaMes, v);
		}
		else if(_aDiaMes.length < MAX_FECHAS && !contains(_aDiaMes, v) && v > 0 && v < 32)
			_aDiaMes = add(_aDiaMes, v);
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

	/// DIA SEMANA ---------------------------------------------------------------------------------
	public void addDiaSemana(byte v)
	{
		if(v == TODO)
		{
			_aDiaSemana = new byte[0];
			_aDiaSemana = add(_aDiaSemana, v);
		}
		else if(_aDiaSemana.length < MAX_FECHAS && !contains(_aDiaSemana, v) && v >= Calendar.SUNDAY && v <= Calendar.SATURDAY)
			_aDiaSemana = add(_aDiaSemana, v);
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

	/// HORA ---------------------------------------------------------------------------------------
	public void addHora(byte v)
	{
		if(v == TODO)
		{
			_aHora = new byte[0];
			_aHora = add(_aHora, v);
		}
		else if(_aHora.length < MAX_FECHAS && !contains(_aHora, v) && v >= 0 && v <= 23)
			_aHora = add(_aHora, v);
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

	/// MINUTO -------------------------------------------------------------------------------------
	public void addMinuto(byte v)
	{
		if(_aMinuto.length < MAX_FECHAS && !contains(_aMinuto, v) && v >= 0 && v <= 59)
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
	public String toString(){return "{id="+getId()+", act="+_bActivo+", diaM="+_aDiaMes.length+", diaS="+_aDiaSemana.length+", mes="+_aMes.length+", hor="+_aHora.length+", min="+_aMinuto.length+", txt="+_sTexto+", _dtAct="+_dtActivo+" }";}


	///----- PARCELABLE
	private AvisoTem(Parcel in)
	{
		setId(in.readString());
		//
		_dtActivo.setTime(in.readLong());
		_bActivo = in.readByte() > 0;
		_sTexto = in.readString();
		//
		int i;
		byte[] ai;
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
		dest.writeString(getId());
		//
		dest.writeLong(_dtActivo.getTime());
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
	public static final Creator<AvisoTem> CREATOR = new Creator<AvisoTem>()
	{
		@Override
		public AvisoTem createFromParcel(Parcel in)
		{
			return new AvisoTem(in);
		}
		@Override
		public AvisoTem[] newArray(int size)
		{
			return new AvisoTem[size];
		}
	};

	//______________________________________________________________________________________________
	public long save()
	{
		Log.e(TAG,"Aviso:save:------------" + this);
		java.util.Arrays.sort(_aMes);//quickSort(_aMes, 0, _aMes.length - 1);
		java.util.Arrays.sort(_aDiaMes);
		java.util.Arrays.sort(_aDiaSemana);
		java.util.Arrays.sort(_aHora);
		java.util.Arrays.sort(_aMinuto);
		//return super.save();
		return 0;//TODO
	}

	//______________________________________________________________________________________________
	public boolean isDueTime()
	{
		Calendar now = Calendar.getInstance();
//Log.e(TAG,"isDueTime-----"+this.getTexto()+"****************************************************************------now=" + now.getTime() + " _dtActivo="+(_dtActivo.getTime())+" : ");

		if(_dtActivo.getTime() + 24*60*60*1000 > now.getTimeInMillis())//Aun no ha pasado un dia
			return false;

		byte[] aMeses = getMeses();
		byte[] aDiasMes = getDiasMes();
		byte[] aDiasSemana = getDiasSemana();
		byte[] aHoras = getHoras();
		byte[] aMinutos = getMinutos();
		if(aMeses.length > 0 && aMeses[0] != AvisoTem.TODO)
		{
			boolean b = false;
			for(byte mes : aMeses)
				if(b = now.get(Calendar.MONTH)+1 == mes)
					break;
			if(!b)return false;
		}
//Log.e(TAG,"isDueTime-----------6 dm:" + now.get(Calendar.DAY_OF_MONTH));
		if(aDiasMes.length > 0 && aDiasMes[0] != AvisoTem.TODO)
		{
			boolean b = false;
			for(byte diaMes : aDiasMes)
				if(b = now.get(Calendar.DAY_OF_MONTH) == diaMes)
					break;
			if(!b)return false;
		}
//Log.e(TAG,"isDueTime-----------7 ds:" + now.get(Calendar.DAY_OF_WEEK));
		if(aDiasSemana.length > 0 && aDiasSemana[0] != AvisoTem.TODO)
		{
			boolean b = false;
			for(byte diaSemana : aDiasSemana)
				if(b = now.get(Calendar.DAY_OF_WEEK) == diaSemana)
					break;
			if(!b)return false;
		}
//Log.e(TAG,"isDueTime-----------8 hor:" + now.get(Calendar.HOUR_OF_DAY) + " : " + now.get(Calendar.HOUR));
		if(aHoras.length > 0 && aHoras[0] != AvisoTem.TODO)
		{
			boolean b = false;
			for(byte hora : aHoras)
				if(b = now.get(Calendar.HOUR_OF_DAY) == hora)
					break;
			if(!b)return false;
		}
//Log.e(TAG,"isDueTime-----------9 min:" + now.get(Calendar.MINUTE));
		if(aMinutos.length > 0 && aMinutos[0] != AvisoTem.TODO)
		{
			boolean b = false;
			for(byte minuto : aMinutos)
				if(b = (now.get(Calendar.MINUTE)-2 <= minuto && now.get(Calendar.MINUTE)+2 >= minuto) )
					break;
//				else Log.e(TAG,"AvisoTem:isDueTime-----------PPP:"+now.get(Calendar.MINUTE)+":::"+minuto);
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

