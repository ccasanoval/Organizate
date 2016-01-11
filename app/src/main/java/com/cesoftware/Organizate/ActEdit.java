package com.cesoftware.Organizate;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.DisplayMetrics;
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
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.RatingBar;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import com.cesoftware.Organizate.models.Objeto;

//TODO: Check support libraries : need, do i use it?
////////////////////////////////////////////////////////////////////////////////////////////////////
public class ActEdit extends AppCompatActivity
{
	private static final int TOP_NODE = -1;
	private static final String PADRE = "+ ";
	private static final String HIJO = "   - ";
	private static final String SEP = "::";

	private static ArrayList<Objeto> _lista;
		public static void setLista(ArrayList<Objeto> lista){_lista = lista;}
	private static MainActivity _act;
		public static void setParentAct(MainActivity act){_act = act;}

	//______________________________________________________________________________________________
	private String		_popUpContents[];
    private PopupWindow	_popupPadre;
    private Button		_btnPadre;
	private ImageButton _btnEliminar;
	private ImageButton _btnHablar;

		//Si no mueves de lugar guarda solo _o, pero si no, guarda tanto origen como destino
	private boolean _isNuevo=false;
	private long _idPadre = TOP_NODE;
	private Objeto _o;
	private EditText _txtNombre;
	private EditText _txtDescripcion;
	private RatingBar _rbPrioridad;

	//______________________________________________________________________________________________
	/*@Override
	protected void onStart()
	{
		super.onStart();
	}*/

	//______________________________________________________________________________________________
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.act_edit);

		_txtNombre = (EditText)findViewById(R.id.txtNombre);
		_txtDescripcion = (EditText)findViewById(R.id.txtDescripcion);
		_rbPrioridad = (RatingBar)findViewById(R.id.rbPrioridad);

		_rbPrioridad.setNumStars(5);
		_btnEliminar = (ImageButton)findViewById(R.id.btnEliminar);
		_btnHablar = (ImageButton)findViewById(R.id.btnHablar);
		ImageButton	btnGuardar = (ImageButton)findViewById(R.id.btnGuardar);

		Toolbar toolbar = (Toolbar)findViewById(R.id.toolbar);
		setSupportActionBar(toolbar);
		FloatingActionButton fab = (FloatingActionButton)findViewById(R.id.fab);

		//------------------------------------------------------------------------------------------
		fab.setOnClickListener(new View.OnClickListener()
		{
			@Override
			public void onClick(View view)
			{
				//Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG).setAction("Action", null).show();
				ActEdit.this.finish();
			}
		});

		_btnEliminar.setOnClickListener(new View.OnClickListener()
		{
			@Override
			public void onClick(final View v)
			{
				AlertDialog.Builder dialog = new AlertDialog.Builder(ActEdit.this);
				dialog.setTitle(ActEdit.this.getString(R.string.eliminar));
				dialog.setMessage(ActEdit.this.getString(R.string.seguro_eliminar));
				dialog.setPositiveButton("OK", new DialogInterface.OnClickListener()
				{
					@Override
					public void onClick(DialogInterface dialog, int which)
					{
						ActEdit.dbDel(_o);
						Snackbar.make(v, R.string.eliminar, Snackbar.LENGTH_LONG).setAction("Action", null).show();
						_act.refrescarLista();
						ActEdit.this.finish();
					}
				});
				/*dialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener()
				{
					@Override
					public void onClick(DialogInterface dialog, int which)
					{
						Toast.makeText(getBaseContext(), "Cancelled", Toast.LENGTH_SHORT).show(); // just show a Toast, do nothing else
					}
				});*/
				dialog.create().show();
			}
		});
		_btnHablar.setOnClickListener(new View.OnClickListener()
		{
			@Override
			public void onClick(View v)
			{
				hablar();
			}
		});
		btnGuardar.setOnClickListener(new View.OnClickListener()
		{
			@Override
			public void onClick(View v)
			{
				saveValores();
			}
		});

		//------------------------------------------------------------------------------------------
		/*if(_lista == null)
		{
			//Avisar
			System.err.println("ActEdit:onCreate:ERROR: Sin lista de objetos");
			finish();
		}*/

		List<String> lst = new ArrayList<>();
		lst.add(this.getBaseContext().getString(R.string.nodo_padre)+SEP+TOP_NODE);
		if(_lista != null &&_lista.size()>0)
		for(Objeto o : Objeto.filtroN(_lista, Objeto.NIVEL1))
		{
			lst.add(PADRE + o.getNombre() + SEP + o.getId());
			if(o.getHijos().length > 0)
				for(Objeto o2 : o.getHijos())
					lst.add(HIJO + o2.getNombre() + SEP + o2.getId());
		}
		/*ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_dropdown_item_1line, lst);
        AutoCompleteTextView txtPadre = (AutoCompleteTextView)findViewById(R.id.txtPadre);
        txtPadre.setAdapter(adapter);*/
        _popUpContents = new String[lst.size()];
        lst.toArray(_popUpContents);
        _popupPadre = popupPadre();
        View.OnClickListener handler = new View.OnClickListener()
		{
			public void onClick(View v)
			{
				switch(v.getId())
				{
				case R.id.btnPadre:
					_popupPadre.showAsDropDown(v, -7, 0);
					break;
				}
			}
		};
        _btnPadre = (Button)findViewById(R.id.btnPadre);
        _btnPadre.setOnClickListener(handler);


		//------------------------------------------------------------------------------------------
		try
		{
			_o = this.getIntent().getParcelableExtra("objeto");
			if(_o != null)
				setValores();
			else
				setValoresNuevo();
		}
		catch(Exception e)
		{
			System.err.println("ActEdit:onCreate:ERROR:"+e);
			this.finish();
		}
		//------------------------------------------------------------------------------------------
    }

	//____________________________________________________________________________________________________________________________________________________

	//______________________________________________________________________________________________
	private void setValoresNuevo()
	{
		_isNuevo = true;
		_o = new Objeto();
		_btnHablar.setVisibility(View.INVISIBLE);
		_btnEliminar.setVisibility(View.INVISIBLE);
System.err.println("setValoresNuevo-----------------getId=" + _o.getId());
	}
	//______________________________________________________________________________________________
	private void setValores()
	{
System.err.println("setValores-----------------_o=" + _o);
		_isNuevo = false;
		_txtNombre.setText(_o.getNombre());
		_txtDescripcion.setText(_o.getDescripcion());
		_rbPrioridad.setRating(_o.getPrioridad());
		Objeto oPadre = _o.getPadre();
		if(oPadre == null)
		{
			_idPadre = -1;
			//_btnPadre.setText();
		}
		else
		{
			_idPadre = oPadre.getId();
			_btnPadre.setText(oPadre.getNombre());
		}
	}

	//______________________________________________________________________________________________
	// DB SAVE
	private void saveValores()
	{
		_o.setNombre(_txtNombre.getText().toString());
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
			/*for(Objeto o : _lista)
				if(o.getId().equals(_o.getId()))
					_lista.set();*/
		}

System.err.println("TO ADD------------o=" + _o);
		fixPadres();
Objeto.printLista(_lista);

		//BBDD---------------------------------------------------------
		clearDataBase();
		if(_lista != null && _lista.size() > 0)
		for(Objeto o : _lista)
		{
			o.save();//TODO:Listener?? todoListAdapter.notifyDataSetChanged();
		}
		_act.refrescarLista();
		_act.selectObjeto(_o);
		ActEdit.this.finish();
	}

	//______________________________________________________________________________________________
	public static void clearDataBase()
	{
		try
		{
			Objeto.deleteAll(Objeto.class);
		}
		catch(Exception e){System.err.println("Objeto:delTodo:ERROR:"+e);}
	}

	//______________________________________________________________________________________________
	// Reordena padres
	private void fixPadres()
	{
		if(   !(_o.getPadre() == null && _idPadre == TOP_NODE)
			&& (_o.getPadre() == null || _o.getPadre().getId() != _idPadre))
		{
			if(_o.getPadre() != null)_o.getPadre().delHijo(_o);
			for(Objeto o1 : _lista)
			{
				if(o1.getId().equals(_idPadre))
				{
					o1.addHijo(_o);
					_o.setPadre(o1);
					break;
				}
				else if(o1.getHijos().length > 0)
				{
					boolean b=false;
					for(Objeto o2 : o1.getHijos())
					{
						if(o2.getId().equals(_idPadre))
						{
							o2.addHijo(_o);
							_o.setPadre(o2);
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
	// DB DELETE
	private static void dbDel(Objeto o)
	{
		o.delete();
		for(Objeto o1 : o.getHijos())
			dbDel(o1);
	}


	//____________________________________________________________________________________________________________________________________________________
	// POPUP PADRE
	//______________________________________________________________________________________________
    public PopupWindow popupPadre()
	{
        PopupWindow popupWindow = new PopupWindow(this);
        ListView list = new ListView(this);
        list.setAdapter(padreAdapter(_popUpContents));
        list.setOnItemClickListener(new PadreDropdownOnItemClickListener());
        // some other visual settings
        DisplayMetrics metrics = getResources().getDisplayMetrics();
        popupWindow.setWidth(metrics.widthPixels-40);
		popupWindow.setFocusable(true);
        popupWindow.setHeight(WindowManager.LayoutParams.WRAP_CONTENT);
        // set the list view as pop up window content
        popupWindow.setContentView(list);
		//
        return popupWindow;
    }
    //______________________________________________________________________________________________
    private ArrayAdapter<String> padreAdapter(String padreArray[])
	{
        return new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, padreArray)
		{
            @Override
            public View getView(int position, View convertView, ViewGroup parent)
			{
                // setting the ID and text for every items in the list
                String item = getItem(position);
                String[] itemArr = item.split(SEP);
                String text = itemArr[0];
                String id = itemArr[1];

                // visual settings for the list item
                TextView listItem = new TextView(ActEdit.this);
                listItem.setText(text);
                listItem.setTag(id);
                listItem.setTextSize(22);
                listItem.setPadding(10, 10, 10, 10);
                listItem.setTextColor(Color.WHITE);

                return listItem;
            }
        };
    }
	//______________________________________________________________________________________________
	class PadreDropdownOnItemClickListener implements AdapterView.OnItemClickListener
	{
		@Override
		public void onItemClick(AdapterView<?> arg0, View v, int arg2, long arg3)
		{
			// get the context and main activity to access variables
			Context mContext = v.getContext();
			ActEdit act = ((ActEdit)mContext);

			// add some animation when a list item was clicked
			Animation fadeInAnimation = AnimationUtils.loadAnimation(v.getContext(), android.R.anim.fade_in);
			fadeInAnimation.setDuration(10);
			v.startAnimation(fadeInAnimation);

			// dismiss the pop up
			act._popupPadre.dismiss();

			// get the text and set it as the button text
			String selectedItemText = ((TextView)v).getText().toString();
			selectedItemText = selectedItemText.replace(HIJO, "").replace(PADRE, "");
			act._btnPadre.setText(selectedItemText);
			// get the id
			_idPadre = Long.parseLong(v.getTag().toString());
			//Toast.makeText(mContext, "ID is: " + _idPadre, Toast.LENGTH_SHORT).show();
		}
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
		int id = item.getItemId();
		//noinspection SimplifiableIfStatement
		//if(id == R.id.)return true;
		saveValores();
		return super.onOptionsItemSelected(item);
    }


	//______________________________________________________________________________________________
	private static TextToSpeech tts = null;
	private void hablar()
	{
		String texto = String.format("Prioridad %d, %s, %s", _o.getPrioridad(), _o.getNombre(), _o.getDescripcion());

		if(tts == null)
		tts = new TextToSpeech(getApplicationContext(), new TextToSpeech.OnInitListener()
		{
   			@Override
   			public void onInit(int status)
			{
				if(status != TextToSpeech.ERROR)
					tts.setLanguage(getResources().getConfiguration().locale);//new Locale("es", "ES");Locale.forLanguageTag("ES")
			}
		});
		//tts.speak(s, TextToSpeech.QUEUE_FLUSH, null);//DEPRECATED
		if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP)
    		ttsGreater21(tts, texto);
		else
    		ttsUnder20(tts, texto);

	}
	//______________________________________________________________________________________________
	@SuppressWarnings("deprecation")
	private void ttsUnder20(TextToSpeech tts, String texto)
	{
		System.err.println("------ttsUnder20");
    	HashMap<String, String> map = new HashMap<>();
    	map.put(TextToSpeech.Engine.KEY_PARAM_UTTERANCE_ID, "MessageId");
    	tts.speak(texto, TextToSpeech.QUEUE_FLUSH, map);
	}
	//______________________________________________________________________________________________
	@TargetApi(Build.VERSION_CODES.LOLLIPOP)
	private void ttsGreater21(TextToSpeech tts, String texto)
	{
		System.err.println("------ttsGreater21");
    	String utteranceId=this.hashCode() + "";
    	tts.speak(texto, TextToSpeech.QUEUE_FLUSH, null, utteranceId);
	}
}
