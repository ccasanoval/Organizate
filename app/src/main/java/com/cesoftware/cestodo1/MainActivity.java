package com.cesoftware.cestodo1;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
//import android.support.design.widget.Snackbar;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ExpandableListView;

import com.cesoftware.cestodo1.models.Objeto;
import com.orm.SugarContext;

import java.util.ArrayList;
import java.util.Iterator;

////////////////////////////////////////////////////////////////////////////////////////////////////
public class MainActivity extends AppCompatActivity
{
	private static ExpandableListView _expListView;

	//______________________________________________________________________________________________
	@Override
	protected  void onDestroy()
	{
		super.onDestroy();
		SugarContext.terminate();//look at manifest
	}

	//______________________________________________________________________________________________
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		//------
		ActEdit.setParentAct(this);
		_expListView = (ExpandableListView)findViewById(R.id.elv_todo);
		SugarContext.init(this);
//datos();
		cargarLista();
		//------
		//En layout debes anadir app:layout_behavior="@string/appbar_scrolling_view_behavior" para que el toolbar no se coma el listview
		Toolbar toolbar = (Toolbar)findViewById(R.id.toolbar);
		setSupportActionBar(toolbar);
	}

	//______________________________________________________________________________________________
	@Override
	public boolean onCreateOptionsMenu(Menu menu)
	{
		getMenuInflater().inflate(R.menu.menu_main, menu);
		return true;
	}
	//______________________________________________________________________________________________
	@Override
	public boolean onOptionsItemSelected(MenuItem item)
	{
		// Handle action bar item clicks here. The action bar will automatically handle clicks on the Home/Up button, so long as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		//System.err.println("onOptionsItemSelected-------------------------------------"+id);
		//noinspection SimplifiableIfStatement
		if(id == R.id.nuevo)
		{
			Intent intent = new Intent(this, ActEdit.class);
			intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
    		this.startActivity(intent);
		}
		return super.onOptionsItemSelected(item);
    }

	//______________________________________________________________________________________________
	private void cargarLista()
	{
		Iterator<Objeto> it = Objeto.findAll(Objeto.class);
		ArrayList<Objeto> lista = Objeto.conectarHijos(it);
		ActEdit.setLista(lista);
		//Objeto[] ao = new Objeto[lista.size()];lista.toArray(ao);
		_expListView.setAdapter(new NivelUnoListAdapter(this.getApplicationContext(), _expListView, lista));
	}
	//______________________________________________________________________________________________
	public void refrescarLista(ArrayList<Objeto> lista)
	{
		//TODO: setRows !!
		//Iterator<Objeto> it = Objeto.findAll(Objeto.class);
		//ArrayList<Objeto> lista = Objeto.conectarHijos(it);
		ActEdit.setLista(lista);
		_expListView.setAdapter(new NivelUnoListAdapter(this.getApplicationContext(), _expListView, lista));
		_expListView.refreshDrawableState();
	}


//______________________________________________________________________________________________
	private static void datos()
	{
		int i=0;
		Objeto[] lista = new Objeto[13];
		Objeto o0, o1, o2;

		Objeto.deleteAll(Objeto.class);

		o0 = new Objeto("VIDA SANA", null);
			o1 = new Objeto("Dormir más", o0);
			o0.addHijo(o1);
				o2 = new Objeto("No pierdas tiempo", o1);
				o1.addHijo(o2);
				o2 = new Objeto("Despues de cena a dormir o leer!", o1);
				o1.addHijo(o2);
				o2 = new Objeto("Calcula horas de sueño y planifica", o1);
				o1.addHijo(o2);
				o2 = new Objeto("No duermas demasiado de mas para compensar", o1);
				o1.addHijo(o2);
			o1 = new Objeto("bbb", o0);
			o0.addHijo(o1);
				o2 = new Objeto("444------55555", o1);
				o1.addHijo(o2);
				o2 = new Objeto("555------66666", o1);
				o1.addHijo(o2);
			o1 = new Objeto("ccc", o0);
			o0.addHijo(o1);
				o2 = new Objeto("66666---12667", o1);
				o1.addHijo(o2);
				o2 = new Objeto("7777", o1);
				o1.addHijo(o2);
			o1 = new Objeto("zzz", o0);
			o0.addHijo(o1);
				o2 = new Objeto("444------55555", o1);
				o1.addHijo(o2);
				o2 = new Objeto("555------66666", o1);
				o1.addHijo(o2);
		lista[i++]=o0;

		o0 = new Objeto("DINERO", null);
			o1 = new Objeto("RENT T HOUSE", o0);
			o0.addHijo(o1);
				o2 = new Objeto("555", o1);
				o1.addHijo(o2);
				o2 = new Objeto("666", o1);
				o1.addHijo(o2);
				o2 = new Objeto("777", o1);
				o1.addHijo(o2);
			o1 = new Objeto("DONT SPEND WITHOUT NEED", o0);
			o0.addHijo(o1);
		lista[i++]=o0;

		o0 = new Objeto("TIEMPO", null);
			o1 = new Objeto("dddd", o0);
			o0.addHijo(o1);
				o2 = new Objeto("666", o1);
				o1.addHijo(o2);
			o1 = new Objeto("eeeee", o0);
			o0.addHijo(o1);
		lista[i++]=o0;
		o0 = new Objeto("CUATRO", null);
		lista[i++]=o0;

		for(int j=0; j < i; j++)
		{
			long id = lista[j].save();
			Objeto[] ao =  lista[j].getHijos();
			for(int k=0; k < ao.length; k++)
			{
				ao[k].save();
				Objeto[] ao2 =  ao[k].getHijos();
				for(int l=0; l < ao2.length; l++)
					ao2[l].save();
			}
		}
	}
}

