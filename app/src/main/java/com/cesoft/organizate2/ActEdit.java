package com.cesoft.organizate2;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.DisplayMetrics;
import android.util.Log;
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

import java.util.Date;
import java.util.List;
import java.util.Locale;

import com.cesoft.organizate2.models.AvisoGeo;
import com.cesoft.organizate2.models.AvisoTem;
import com.cesoft.organizate2.models.Objeto;
import com.cesoft.organizate2.util.Util;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

//TODO: guardar de una sola vez...
////////////////////////////////////////////////////////////////////////////////////////////////////
//http://www.androidhive.info/2015/09/android-material-design-snackbar-example/
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
	private static final String TAG = ActEdit.class.getSimpleName();

	@Inject	PreEdit _presenter;

	//______________________________________________________________________________________________
	private String[]	_popUpContents;
    private PopupWindow	_popupPadre;

	private boolean		_isNuevo=false;
	private String		_idPadre = Objeto.TOP_NODE;
	private Objeto		_o;

	@BindView(R.id.txtNombre)		EditText	_txtNombre;
	@BindView(R.id.txtDescripcion)	EditText	_txtDescripcion;
	@BindView(R.id.rbPrioridad)		RatingBar	_rbPrioridad;

	@BindView(R.id.btnEliminar)		ImageButton	_btnEliminar;
	@BindView(R.id.btnHablar)		ImageButton	_btnHablar;
	@OnClick(R.id.btnEliminar)		void btnEliminar(View v){ borrar(v); }
	@OnClick(R.id.btnHablar)		void btnHablar()
	{
		Util.hablar(getApplicationContext(),
			String.format(Locale.getDefault(),
				getString(R.string.hablar), _o.getPrioridad(), _o.getNombre(), _o.getDescripcion()));
	}

	@OnClick(R.id.btnAviso)			void btnAviso(){ showAviso(); }
	@OnClick(R.id.btnAvisoGeo)		void btnAvisoGeo(){ showAvisoGeo(); }
	@OnClick(R.id.fab)				void btnAtras(){ finish(); }

	@BindView(R.id.btnPadre)		Button		_btnPadre;
	@OnClick(R.id.btnPadre)			void btnPadre(View v){ _popupPadre.showAsDropDown(v, -7, 0); }


	//______________________________________________________________________________________________
	@Override
	public void onStart()
	{
		super.onStart();
		Util.iniHablar(this);
	}
	//______________________________________________________________________________________________
	@Override
	public void onResume()
	{
		super.onResume();
		_presenter.subscribe();
	}
	//______________________________________________________________________________________________
	@Override public void onPause()
	{
		super.onPause();
		_presenter.unsubscribe();
	}
	//______________________________________________________________________________________________
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.act_edit);

		App.getComponent(this).inject(this);
		ButterKnife.bind(this);
		//_presenter.subscribe(this);

		setSupportActionBar((Toolbar)findViewById(R.id.toolbar));
		//
		ActionBar actionBar = getSupportActionBar();
		if(actionBar!=null)actionBar.setDisplayHomeAsUpEnabled(true);

		List<String> lst = _presenter.get();

        _popUpContents = new String[lst.size()];
        lst.toArray(_popUpContents);
        _popupPadre = popupPadre();

		//------------------------------------------------------------------------------------------
		try
		{
			_o = getIntent().getParcelableExtra(Objeto.class.getName());
//Log.e(TAG, "onCreate--------------------------------"+_o);
			if(_o != null)
				setValores();
			else
				setValoresNuevo();
		}
		catch(Exception e)
		{
			Log.e(TAG,"ActEdit:onCreate:e:----------------------------------------------------------",e);
			this.finish();
		}
		//------------------------------------------------------------------------------------------
    }

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
			_idPadre = Objeto.TOP_NODE;
			//_btnPadre.setText();
		}
		else
		{
			_idPadre = oPadre.getId();
			_btnPadre.setText(oPadre.getNombre());
		}
	}

	//______________________________________________________________________________________________
	// SAVE
	private void saveValores()
	{
		_o.setNombre(_txtNombre.getText().toString());
		_o.setDescripcion(_txtDescripcion.getText().toString());
		_o.setPrioridad((int) _rbPrioridad.getRating());
		_o.setModificado(new Date());
		_o.setIdPadre(_idPadre);
		_presenter.save(_o, _isNuevo);
		ActEdit.this.finish();
	}

	//______________________________________________________________________________________________
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
				_presenter.del(_o);
				Snackbar.make(v, R.string.eliminar, Snackbar.LENGTH_LONG).show();
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
            @NonNull
			@Override
            public View getView(int position, View convertView, @NonNull ViewGroup parent)
			{
                // setting the ID and text for every items in the list
                String item = getItem(position);
				String text = _presenter.getTexto(item);
				String id   = _presenter.getId(item);

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
	private class PadreDropdownOnItemClickListener implements AdapterView.OnItemClickListener
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
			String id = _presenter.getSelectedId(((TextView)v).getText().toString());
			/*String selectedItemText = ((TextView)v).getText().toString();
			selectedItemText = selectedItemText.replace(HIJO, "").replace(PADRE, "");*/
			act._btnPadre.setText(id);
			// get the id
			_idPadre = v.getTag().toString();
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
		switch(item.getItemId())
		{
			case android.R.id.home:
				onBackPressed();
				return true;
			case R.id.action_user:
				saveValores();
				return true;
		}
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
		if(a == null)a = new AvisoTem(_o.getId(), getStringAviso());
		i.putExtra(AvisoTem.class.getName(), a);
		startActivityForResult(i, AVISO);
	}
	private static final int AVISO_GEO = 201;
	private void showAvisoGeo()
	{
		Intent i = new Intent(this, ActAvisoGeoEdit.class);
		AvisoGeo a = _o.getAvisoGeo();
		if(a == null)a = new AvisoGeo(getStringAviso());
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
			_o.setAvisoTem((AvisoTem)data.getParcelableExtra(AvisoTem.class.getName()));
Log.e(TAG,"onActivityResult----------A:" + _o.getAvisoTem());
		}
		else if(requestCode == AVISO_GEO)
		{
			_o.setAvisoGeo((AvisoGeo)data.getParcelableExtra(AvisoGeo.class.getName()));
Log.e(TAG,"onActivityResult----------AG:" + _o.getAvisoGeo());
		}
	}

}
