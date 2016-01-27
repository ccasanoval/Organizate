package com.cesoftware.Organizate;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
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

////////////////////////////////////////////////////////////////////////////////////////////////////
//TODO: no permitir que repita los mismos elementos...
//TODO: Check support libraries : need, do i use it?
////////////////////////////////////////////////////////////////////////////////////////////////////
public class ActAvisoEdit extends AppCompatActivity
{
	private static String TODO;
	private static String NADA;
	private static final String SEP = ":";
	private final static int MES=0, DIA_MES=1, DIA_SEMANA=2, HORA=3, MINUTO=4, MAX_TIPO=5;
	private final static int[] _anMax = {12+2, 31+2, 7+2, 24+1, 12+1};

	private Aviso _a;
	//private boolean _isNuevo=false;
	private String[][] _asPopUp;

	private int[] _aIdBtn = {R.id.btnMes, R.id.btnDiaMes, R.id.btnDiaSemana, R.id.btnHora, R.id.btnMinuto};
	private LinearLayout[] _aLay;
	private int[] _aIdLay = {R.id.layMes, R.id.layDiaMes, R.id.layDiaSemana, R.id.layHora, R.id.layMinuto};
	private PopupWindow[] _apw;
	private Button[] _btn;

	private TextView _txtAviso;

	//______________________________________________________________________________________________
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.act_aviso_edit);

		Toolbar toolbar = (Toolbar)findViewById(R.id.toolbar);
		setSupportActionBar(toolbar);
		FloatingActionButton fab = (FloatingActionButton)findViewById(R.id.fab);
		fab.setOnClickListener(new View.OnClickListener()
		{
			@Override
			public void onClick(View view)
			{
				ActAvisoEdit.this.finish();
			}
		});

		_txtAviso = (TextView)findViewById(R.id.txtAviso);

		//------------------------------------------------------------------------------------------
		// PopUp
		TODO = getString(R.string.todo);
		NADA = getString(R.string.nada);
		_asPopUp = new String[MAX_TIPO][];
		for(int i=0; i < MAX_TIPO; i++)_asPopUp[i] = new String[_anMax[i]];
		_apw = new PopupWindow[MAX_TIPO];
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

		_aLay = new LinearLayout[_aIdLay.length];
		for(int i=0; i < _aLay.length; i++)_aLay[i]=(LinearLayout)findViewById(_aIdLay[i]);

		_asPopUp[MES][0] = TODO+SEP+Aviso.TODO;
		_asPopUp[MES][_anMax[MES]-1] = NADA+SEP+Aviso.NADA;
		_asPopUp[DIA_MES][0] = TODO+SEP+Aviso.TODO;
		_asPopUp[DIA_MES][_anMax[DIA_MES]-1] = NADA+SEP+Aviso.NADA;
		_asPopUp[HORA][_asPopUp[HORA].length-1] = NADA+SEP+Aviso.NADA;
		_asPopUp[MINUTO][_anMax[MINUTO]-1] = NADA+SEP+Aviso.NADA;
		for(int i=1; i < _anMax[MES]-1; i++)		_asPopUp[MES][i]=String.valueOf(i);
		for(int i=1; i < _anMax[DIA_MES]-1; i++)	_asPopUp[DIA_MES][i]=String.valueOf(i);
		for(int i=0; i < _anMax[HORA]-1; i++)		_asPopUp[HORA][i]=String.valueOf(i);
		for(int i=0; i < _anMax[MINUTO]-1; i++)		_asPopUp[MINUTO][i]=String.valueOf(i*5);

		_asPopUp[DIA_SEMANA][0] = TODO+SEP+Aviso.TODO;
		_asPopUp[DIA_SEMANA][_anMax[DIA_SEMANA]-1] = NADA+SEP+Aviso.NADA;
		_asPopUp[DIA_SEMANA][Calendar.MONDAY] =		getResources().getString(R.string.lunes)+SEP+(Calendar.MONDAY);
		_asPopUp[DIA_SEMANA][Calendar.TUESDAY] =	getResources().getString(R.string.martes)+SEP+(Calendar.TUESDAY);
		_asPopUp[DIA_SEMANA][Calendar.WEDNESDAY] =	getResources().getString(R.string.miercoles)+SEP+(Calendar.WEDNESDAY);
		_asPopUp[DIA_SEMANA][Calendar.THURSDAY] =	getResources().getString(R.string.jueves)+SEP+(Calendar.THURSDAY);
		_asPopUp[DIA_SEMANA][Calendar.FRIDAY] = 	getResources().getString(R.string.viernes)+SEP+(Calendar.FRIDAY);
		_asPopUp[DIA_SEMANA][Calendar.SATURDAY] =	getResources().getString(R.string.sabado)+SEP+(Calendar.SATURDAY);
		_asPopUp[DIA_SEMANA][Calendar.SUNDAY] =		getResources().getString(R.string.domingo)+SEP+(Calendar.SUNDAY);

		//------------------------------------------------------------------------------------------
		try
		{
			_a = this.getIntent().getParcelableExtra("aviso");
			//if(_a == null)setValoresNuevo(); else
				setValores();
		}
		catch(Exception e)
		{
			System.err.println("ActAvisoEdit:onCreate:ERROR:"+e);
			this.finish();
		}
		//------------------------------------------------------------------------------------------

    }

	//____________________________________________________________________________________________________________________________________________________

/*	//______________________________________________________________________________________________
	private void setValoresNuevo()
	{
		//_isNuevo = true;
		_a = new Aviso();
//System.err.println("setValoresNuevo-----------------getId=" + _a);
	}*/
	//______________________________________________________________________________________________
	private void setValores()
	{
		//_isNuevo = false;
		_txtAviso.setText(_a.getTexto());
		for(byte i : _a.getMeses())//TODO: meter y sacar ordenados ...
			createItemView(MES, i, i==Aviso.TODO ? TODO : String.valueOf(i));
		for(byte i : _a.getDiasMes())
			createItemView(DIA_MES, i, i==Aviso.TODO ? TODO : String.valueOf(i));
		for(byte i : _a.getDiasSemana())
			createItemView(DIA_SEMANA, i, i==Aviso.TODO ? TODO : String.valueOf(i));
		for(byte i : _a.getHoras())
			createItemView(HORA, i, i==Aviso.TODO ? TODO : String.valueOf(i));
		for(byte i : _a.getMinutos())
			createItemView(MINUTO, i, String.valueOf(i));
	}


	private View createItemView(final int iTipo, Object tag, String texto)
	{
		if(Integer.parseInt(tag.toString()) == Aviso.NADA)return null;
		LayoutInflater inflater = LayoutInflater.from(ActAvisoEdit.this);
		final View item = inflater.inflate(R.layout.aviso_item, null, false);
		//item.setTag(tag);
		TextView lbl = (TextView)item.findViewById(R.id.lbl);
		lbl.setText(texto);
		ImageButton btnDel = (ImageButton)item.findViewById(R.id.btnDel);
		btnDel.setTag(tag);
		btnDel.setOnClickListener(new View.OnClickListener()
		{
			@Override
			public void onClick(View v)
			{
				_aLay[iTipo].removeView(item);
				Object tag = ((View)(v.getParent())).getTag();
				if(tag != v.getTag())
					delItem(iTipo, Byte.parseByte(v.getTag().toString()));
			}
		});
		_aLay[iTipo].addView(item);
		return item;
	}

	private void delItem(int iTipo, byte nValor)
	{
		switch(iTipo)//TODO: Que otra manera hay?
		{
		case MES:		_a.delMes(nValor);break;
		case DIA_MES:	_a.delDiaMes(nValor);break;
		case DIA_SEMANA:_a.delDiaSemana(nValor);break;
		case HORA:		_a.delHora(nValor);break;
		case MINUTO:	_a.delMinuto(nValor);break;
		}
	}
	private void addItem(int iTipo, byte nValor)
	{
		switch(iTipo)//TODO: Que otra manera hay?
		{
		case MES:		_a.addMes(nValor);break;
		case DIA_MES:	_a.addDiaMes(nValor);break;
		case DIA_SEMANA:_a.addDiaSemana(nValor);break;
		case HORA:		_a.addHora(nValor);break;
		case MINUTO:	_a.addMinuto(nValor);break;
		}
	}

	private void manageItems(int iTipo, byte nValor, String sTexto)
	{
		if(nValor == Aviso.NADA || nValor == Aviso.TODO)
		{
			delItem(iTipo, Aviso.TODO);
			_aLay[iTipo].removeAllViews();
			if(nValor == Aviso.NADA)return;
		}
		createItemView(iTipo, nValor, sTexto);
		addItem(iTipo, nValor);
	}

	//______________________________________________________________________________________________
	// DB SAVE
	private void saveValores()
	{
		_a.setTexto(_txtAviso.getText().toString());
		Intent data = new Intent();
    	data.putExtra("aviso", _a);
		setResult(Activity.RESULT_OK, data);
		finish();
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
		_apw = new PopupWindow[MAX_TIPO];
		for(int iTipo_=0; iTipo_ < MAX_TIPO; iTipo_++)
		{
			final int iTipo = iTipo_;
        	final PopupWindow popupWindow = new PopupWindow(this);
        	ListView list = new ListView(this);
        	list.setAdapter(adapter(_asPopUp[iTipo]));
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
							String sTexto = ((TextView)v).getText().toString();
							//Object oValor = v.getTag();
							byte nValor;
							if(v.getTag().toString().equals(TODO))nValor = Aviso.TODO;
							else if(v.getTag().toString().equals(NADA))nValor = Aviso.NADA;
							else nValor = Byte.parseByte(v.getTag().toString());
							manageItems(iTipo, nValor, sTexto);
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
			_apw[iTipo] = popupWindow;
		}
    }
    //______________________________________________________________________________________________
    private ArrayAdapter<String> adapter(String padreArray[])
	{
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
                listItem.setPadding(40, 10, 10, 10);
                listItem.setTextColor(Color.WHITE);

                return listItem;
            }
        };
    }



/*

use editText.setOnFocusChangeListenerinstead, works on first click
I've solved this by using android:focusable="false" and onclicklistner

Calendar myCalendar = Calendar.getInstance();

DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {

    @Override
    public void onDateSet(DatePicker view, int year, int monthOfYear,
            int dayOfMonth) {
        // TODO Auto-generated method stub
        myCalendar.set(Calendar.YEAR, year);
        myCalendar.set(Calendar.MONTH, monthOfYear);
        myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
        updateLabel();
    }

};

   edittext.setOnClickListener(new OnClickListener() {

        @Override
        public void onClick(View v) {
            // TODO Auto-generated method stub
            new DatePickerDialog(classname.this, date, myCalendar
                    .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                    myCalendar.get(Calendar.DAY_OF_MONTH)).show();
        }
    });

      private void updateLabel() {

    String myFormat = "MM/dd/yy"; //In which you need put here
    SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);

    edittext.setText(sdf.format(myCalendar.getTime()));
    }

*/
}
