package com.cesoft.organizate;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;//TODO: Check support libraries : need, do i use it?
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
import java.util.List;

import com.cesoft.organizate.models.AvisoGeo;
import com.cesoft.organizate.models.AvisoTem;
import com.cesoft.organizate.models.Objeto;

////////////////////////////////////////////////////////////////////////////////////////////////////
//Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG).setAction("Action", null).show();
/*Snackbar snackbar = Snackbar
        .make(coordinatorLayout, "Message is deleted", Snackbar.LENGTH_LONG)
        .setAction("UNDO", new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar snackbar1 = Snackbar.make(coordinatorLayout, "Message is restored!", Snackbar.LENGTH_SHORT);
                snackbar1.show();
            }
        });
snackbar.show();*/
////////////////////////////////////////////////////////////////////////////////////////////////////
public class ActEdit extends AppCompatActivity
{
	private static final int TOP_NODE = -1;
	private static final String PADRE = "+ ";
	private static final String HIJO = "   - ";
	private static final String SEP = "::";

	private static ArrayList<Objeto> _lista;
		public static void setLista(ArrayList<Objeto> lista){_lista = lista;}
	private static ActMain _act;
		public static void setParentAct(ActMain act){_act = act;}

	//______________________________________________________________________________________________
	private String[]	_popUpContents;
    private PopupWindow	_popupPadre;
    private Button		_btnPadre;
	private ImageButton _btnEliminar;
	private ImageButton _btnHablar;

	private boolean		_isNuevo=false;
	private long		_idPadre = TOP_NODE;
	private Objeto		_o;
	private EditText	_txtNombre;
	private EditText	_txtDescripcion;
	private RatingBar	_rbPrioridad;

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
		ImageButton	btnAviso = (ImageButton)findViewById(R.id.btnAviso);
		ImageButton	btnAvisoGeo = (ImageButton)findViewById(R.id.btnAvisoGeo);

		Toolbar toolbar = (Toolbar)findViewById(R.id.toolbar);
		setSupportActionBar(toolbar);
		FloatingActionButton fab = (FloatingActionButton)findViewById(R.id.fab);

		//------------------------------------------------------------------------------------------
		fab.setOnClickListener(new View.OnClickListener()
		{
			@Override
			public void onClick(View view)
			{
				ActEdit.this.finish();
			}
		});
		_btnEliminar.setOnClickListener(new View.OnClickListener()
		{
			@Override
			public void onClick(View v)
			{
				borrar(v);
			}
		});
		_btnHablar.setOnClickListener(new View.OnClickListener()
		{
			@Override
			public void onClick(View v)
			{
				Util.hablar(getApplicationContext(), String.format("Prioridad %d, %s, %s", _o.getPrioridad(), _o.getNombre(), _o.getDescripcion()));
			}
		});
		btnAviso.setOnClickListener(new View.OnClickListener()
		{
			@Override
			public void onClick(View v)
			{
				showAviso();
			}
		});
		btnAvisoGeo.setOnClickListener(new View.OnClickListener()
		{
			@Override
			public void onClick(View v)
			{
				showAvisoGeo();
			}
		});
		//------------------------------------------------------------------------------------------

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
			_o = this.getIntent().getParcelableExtra(Objeto.class.getName());
			if(_o != null)
				setValores();
			else
				setValoresNuevo();
		}
		catch(Exception e)
		{
			System.err.println("ActEdit:onCreate:e:"+e);
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
	}
	//______________________________________________________________________________________________
	private void setValores()
	{
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
		}

		fixPadres();

		//BBDD---------------------------------------------------------
		clearDataBase();
		if(_lista != null && _lista.size() > 0)
			for(Objeto o : _lista)
				o.save();
		_act.refrescarLista();//TODO:Listener?? todoListAdapter.notifyDataSetChanged();
		_act.selectObjeto(_o);
		ActEdit.this.finish();
	}

	//______________________________________________________________________________________________
	public static void clearDataBase()
	{
		Objeto.delTodo();
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
	}

	private void borrar(final View v)
	{
		AlertDialog.Builder dialog = new AlertDialog.Builder(ActEdit.this);
		dialog.setTitle(getString(R.string.eliminar));
		dialog.setMessage(ActEdit.this.getString(R.string.seguro_eliminar));
		dialog.setPositiveButton("OK", new DialogInterface.OnClickListener()
		{
			@Override
			public void onClick(DialogInterface dialog, int which)
			{
				ActEdit.dbDel(_o);
				Snackbar.make(v, R.string.eliminar, Snackbar.LENGTH_LONG).show();
				_act.refrescarLista();
				ActEdit.this.finish();
			}
		});
		/*dialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener()
		{
			@Override
			public void onClick(DialogInterface dialog, int which)
			{
				Toast.makeText(getBaseContext(), "Cancelled", Toast.LENGTH_SHORT).show();
			}
		});*/
		dialog.create().show();
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
	// AVISO
	//______________________________________________________________________________________________
	private String getStringAviso()
	{
		String sAviso = "";
		if( ! _txtNombre.getText().toString().isEmpty())
			sAviso = _txtNombre.getText().toString()+" : ";
		if( ! _txtDescripcion.getText().toString().isEmpty())
			sAviso += _txtDescripcion.getText().toString();
		return sAviso;
	}
	private static final int AVISO = 200;
	private void showAviso()
	{
		Intent i = new Intent(this, ActAvisoEdit.class);
		AvisoTem a = _o.getAvisoTem();
		if(a == null)
			a = new AvisoTem(getStringAviso());
		i.putExtra(AvisoTem.class.getName(), a);
		startActivityForResult(i, AVISO);
	}
	private static final int AVISO_GEO = 201;
	private void showAvisoGeo()
	{
		Intent i = new Intent(this, ActAvisoGeoEdit.class);
		AvisoGeo a = _o.getAvisoGeo();
		if(a == null)
			a = new AvisoGeo(getStringAviso());
		i.putExtra(AvisoGeo.class.getName(), a);
		startActivityForResult(i, AVISO_GEO);
	}
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data)
	{
		super.onActivityResult(requestCode, resultCode, data);
		if(resultCode != RESULT_OK)return;
		if(requestCode == AVISO)
		{
			_o.setAvisoTem((AvisoTem) data.getParcelableExtra(AvisoTem.class.getName()));
System.err.println("onActivityResult----------A:" + _o.getAvisoTem());
		}
		else if(requestCode == AVISO_GEO)
		{
			_o.setAvisoGeo((AvisoGeo)data.getParcelableExtra(AvisoGeo.class.getName()));
System.err.println("onActivityResult----------AG:" + _o.getAvisoGeo());
		}
	}

}
