package com.cesoftware.Organizate;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;

import java.util.Calendar;

import com.cesoftware.Organizate.models.Aviso;

//TODO: no permitir que repita los mismos elementos...
//TODO: Check support libraries : need, do i use it?
////////////////////////////////////////////////////////////////////////////////////////////////////
public class ActAvisoEdit extends AppCompatActivity
{
	private static final String SEP = "::";

	private Aviso _a, _a2;
	private boolean _isNuevo=false;
	private String[] _asPopUpMes = new String[12];
	private String[] _asPopUpDiaMes = new String[31];
	private String[] _asPopUpDiaSemana = new String[7];
	private String[] _asPopUpHora = new String[24];
	private String[] _asPopUpMinuto = new String[12];
	private final static int MES=0, DIA_MES=1, DIA_SEMANA=2, HORA=3, MINUTO=4, MAX=5;
	private String[][] _asPopUp = {_asPopUpMes, _asPopUpDiaMes, _asPopUpDiaSemana, _asPopUpHora, _asPopUpMinuto};
	private int[] _aIdBtn = {R.id.btnMes, R.id.btnDiaMes, R.id.btnDiaSemana, R.id.btnHora, R.id.btnMinuto};
	private LinearLayout[] _aLay;
	private int[] _aIdLay = {R.id.layMes, R.id.layDiaMes, R.id.layDiaSemana, R.id.layHora, R.id.layMinuto};
	private PopupWindow[] _apw;
	private Button[] _btn;

	//______________________________________________________________________________________________
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.act_aviso_edit);

		// PopUp
		_apw = new PopupWindow[_aIdBtn.length];
		popUp();
		View.OnClickListener handler = new View.OnClickListener()
		{
			public void onClick(View v)
			{
				for(int i=0; i < _btn.length; i++)
				{
					if(v.getId() == _aIdBtn[i])
					{
						_apw[i].showAsDropDown(v, -7, 0);
						break;
					}
				}
			}
		};
		_btn = new Button[_aIdBtn.length];
		for(int i=0; i < _aIdBtn.length; i++)
		{
			_btn[i] = (Button)findViewById(_aIdBtn[i]);
			_btn[i].setOnClickListener(handler);
		}

		for(int i=0; i < _asPopUpMes.length; i++)	_asPopUpMes[i]=String.valueOf(i);
		for(int i=0; i < _asPopUpDiaMes.length; i++)_asPopUpDiaMes[i]=String.valueOf(i);
		for(int i=0; i < _asPopUpHora.length; i++)	_asPopUpHora[i]=String.valueOf(i);
		for(int i=0; i < _asPopUpMinuto.length; i++)_asPopUpMinuto[i]=String.valueOf(i*5);

		_aLay = new LinearLayout[_aIdLay.length];
		for(int i=0; i < _aLay.length; i++)_aLay[i]=(LinearLayout)findViewById(_aIdLay[i]);

		_asPopUpDiaSemana[Calendar.MONDAY-1] =	getResources().getString(R.string.lunes)+SEP+(Calendar.MONDAY-1);
		_asPopUpDiaSemana[Calendar.TUESDAY-1] =	getResources().getString(R.string.martes)+SEP+(Calendar.TUESDAY-1);
		_asPopUpDiaSemana[Calendar.WEDNESDAY-1]=getResources().getString(R.string.miercoles)+SEP+(Calendar.WEDNESDAY-1);
		_asPopUpDiaSemana[Calendar.THURSDAY-1] =getResources().getString(R.string.jueves)+SEP+(Calendar.THURSDAY-1);
		_asPopUpDiaSemana[Calendar.FRIDAY-1] = 	getResources().getString(R.string.viernes)+SEP+(Calendar.FRIDAY-1);
		_asPopUpDiaSemana[Calendar.SATURDAY-1] =getResources().getString(R.string.sabado)+SEP+(Calendar.SATURDAY-1);
		_asPopUpDiaSemana[Calendar.SUNDAY-1] =	getResources().getString(R.string.domingo)+SEP+(Calendar.SUNDAY-1);

		//------------------------------------------------------------------------------------------
		try
		{
			_a = this.getIntent().getParcelableExtra("aviso");
			if(_a != null)
				setValores();
			else
				setValoresNuevo();
		}
		catch(Exception e)
		{
			System.err.println("ActAvisoEdit:onCreate:ERROR:"+e);
			this.finish();
		}
		//------------------------------------------------------------------------------------------

    }

	//____________________________________________________________________________________________________________________________________________________

	//______________________________________________________________________________________________
	private void setValoresNuevo()
	{
		_isNuevo = true;
		_a = new Aviso();
//System.err.println("setValoresNuevo-----------------getId=" + _a);
	}
	//______________________________________________________________________________________________
	private void setValores()
	{
System.err.println("setValores-----------------_a=" + _a+" : "+_a.getMeses());
		_isNuevo = false;
		for(Integer i : _a.getMeses())//TODO: meter y sacar ordenados ...
			createItemView(i, i.toString(), MES);
		for(Integer i : _a.getDiasMes())
			createItemView(i, i.toString(), DIA_MES);
		for(Integer i : _a.getDiasSemana())
			createItemView(i, i.toString(), DIA_SEMANA);
		for(Integer i : _a.getHoras())
			createItemView(i, i.toString(), HORA);
		for(Integer i : _a.getMinutos())
			createItemView(i, i.toString(), MINUTO);
	}


	private View createItemView(Object tag, String texto, final int i)
	{
		LayoutInflater inflater = LayoutInflater.from(ActAvisoEdit.this);
		final View item = inflater.inflate(R.layout.aviso_item, null, false);
		item.setTag(tag);
		TextView lbl = (TextView)item.findViewById(R.id.lbl);
		lbl.setText(texto);
		ImageButton btnDel = (ImageButton)item.findViewById(R.id.btnDel);
		btnDel.setOnClickListener(new View.OnClickListener()
		{
			@Override
			public void onClick(View v)
			{
				_aLay[i].removeView(item);
				delItem(item, i);
			}
		});
		_aLay[i].addView(item);
		return item;
	}

	private void delItem(View v, int i)
	{
		switch(i)//TODO: Que otra manera hay?
		{
		case MES:		_a.delMes(Integer.parseInt(v.getTag().toString()));break;
		case DIA_MES:	_a.delDiaMes(Integer.parseInt(v.getTag().toString()));break;
		case DIA_SEMANA:_a.delDiaSemana(Integer.parseInt(v.getTag().toString()));break;
		case HORA:		_a.delHora(Integer.parseInt(v.getTag().toString()));break;
		case MINUTO:	_a.delMinuto(Integer.parseInt(v.getTag().toString()));break;
		}
	}


	//______________________________________________________________________________________________
	// DB SAVE
	private void saveValores()
	{
		/*_o.setNombre(_txtNombre.getText().toString());
		_o.setDescripcion(_txtDescripcion.getText().toString());
		_o.setPrioridad((int) _rbPrioridad.getRating());
		_o.setModificado(new Date());
		if(_isNuevo)
		{
			//if(_o.getPadre() == null)_lista.add(_o);
			_o.setCreacion(new Date());
			_lista.add(_o);
		}
		else
		{
			_lista.set(_lista.indexOf(_o), _o);
		}

		fixPadres();
//Objeto.printLista(_lista);

		//BBDD---------------------------------------------------------
		clearDataBase();
		if(_lista != null && _lista.size() > 0)
		for(Objeto o : _lista)
		{
			o.save();//TODO:Listener?? todoListAdapter.notifyDataSetChanged();
		}
		_act.refrescarLista();
		_act.selectObjeto(_o);
		ActEdit.this.finish();*/
	}




	//____________________________________________________________________________________________________________________________________________________
	/// MENU
	//______________________________________________________________________________________________
	@Override
	public boolean onCreateOptionsMenu(Menu menu)
	{
		getMenuInflater().inflate(R.menu.menu, menu);
		return true;
	}
	//______________________________________________________________________________________________
	@Override
	public boolean onOptionsItemSelected(MenuItem item)
	{
		saveValores();
		return super.onOptionsItemSelected(item);
    }



	//____________________________________________________________________________________________________________________________________________________
	// POPUPs
	//______________________________________________________________________________________________
    public void popUp()
	{
		_apw = new PopupWindow[_asPopUp.length];
		for(int i=0; i < _asPopUp.length; i++)
		{
			final int i_ = i;
        	final PopupWindow popupWindow = new PopupWindow(this);
        	ListView list = new ListView(this);
        	list.setAdapter(adapter(_asPopUp[i]));
        	list.setOnItemClickListener(
					new AdapterView.OnItemClickListener()
					{
						@Override
						public void onItemClick(AdapterView<?> arg0, View v, int arg2, long arg3)
						{
							ActAvisoEdit act = ((ActAvisoEdit)v.getContext());
							// add some animation when a list item was clicked
							Animation fadeInAnimation = AnimationUtils.loadAnimation(v.getContext(), android.R.anim.fade_in);
							fadeInAnimation.setDuration(10);
							v.startAnimation(fadeInAnimation);
							// dismiss the pop up
							popupWindow.dismiss();
							//
							String texto = ((TextView)v).getText().toString();
							Object valor = v.getTag();
							//
 							createItemView(valor, texto, i_);
							switch(i_)//TODO: Que otra manera hay?
							{
							case MES:		_a.addMes(Integer.parseInt(valor.toString()));break;
							case DIA_MES:	_a.addDiaMes(Integer.parseInt(valor.toString()));break;
							case DIA_SEMANA:_a.addDiaSemana(Integer.parseInt(valor.toString()));break;
							case HORA:		_a.addHora(Integer.parseInt(valor.toString()));break;
							case MINUTO:	_a.addMinuto(Integer.parseInt(valor.toString()));break;
							}
						}
					}
			);
        	// some other visual settings
        	DisplayMetrics metrics = getResources().getDisplayMetrics();
        	popupWindow.setWidth(250);
			popupWindow.setFocusable(true);
        	popupWindow.setHeight(WindowManager.LayoutParams.WRAP_CONTENT);
        	// set the list view as pop up window content
        	popupWindow.setContentView(list);
			_apw[i] = popupWindow;
		}
    }
    //______________________________________________________________________________________________
    private ArrayAdapter<String> adapter(String padreArray[])
	{
		final DisplayMetrics metrics = getResources().getDisplayMetrics();
        //setWidth(metrics.widthPixels/2);

        return new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, padreArray)
		{
            @Override
            public View getView(int position, View convertView, ViewGroup parent)
			{
				String id;
				String text;

                // setting the ID and text for every items in the list
                String item = getItem(position);
				if(item.contains(SEP))
				{
                	String[] itemArr = item.split(SEP);
                	text = itemArr[0];
                	id = itemArr[1];
				}
				else
				{
					id = item;
					text = item;
				}

                // visual settings for the list item
                TextView listItem = new TextView(ActAvisoEdit.this);
                listItem.setText(text);
                listItem.setTag(id);
                listItem.setTextSize(22);
                listItem.setPadding(10, 10, 10, 10);
                listItem.setTextColor(Color.WHITE);

                return listItem;
            }
        };
    }

}
