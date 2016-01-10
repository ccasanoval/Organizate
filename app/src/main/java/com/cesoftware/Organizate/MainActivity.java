package com.cesoftware.Organizate;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
//import android.support.design.widget.Snackbar;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ExpandableListView;

import com.cesoftware.Organizate.models.Objeto;
import com.orm.SugarContext;

import java.util.ArrayList;
import java.util.Iterator;

//TODO: Hacer _lista accesible desde servicio para widget?
//TODO: Eliminar espaciado extra en nivel2
//TODO: Cuando el elemento ocupa dos lineas, contar una extra row al calcular espacio
////////////////////////////////////////////////////////////////////////////////////////////////////
public class MainActivity extends AppCompatActivity
{
	private static ExpandableListView _expListView;

	//______________________________________________________________________________________________
	@Override
	protected  void onDestroy()
	{
		super.onDestroy();
		//SugarContext.terminate();//look at manifest//No lo llamamos si queremos que widget pueda consultar bbdd
	}

	//______________________________________________________________________________________________
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		//------ Medir dimensiones submenus
		//findViewById(R.id.elv_todo);
		//------
		ActEdit.setParentAct(this);
		_expListView = (ExpandableListView)findViewById(R.id.elv_todo);
		SugarContext.init(this);
//datosTEST();
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
		ArrayList<Objeto> lista;
		try
		{
			lista = Objeto.conectarHijos(Objeto.findAll(Objeto.class));
		}
		catch(Exception e)
		{
			lista = new ArrayList<>();
		}
		ActEdit.setLista(lista);
		//Objeto[] ao = new Objeto[lista.size()];lista.toArray(ao);
		_expListView.setAdapter(new NivelUnoListAdapter(this.getApplicationContext(), _expListView, lista));
	}
	//______________________________________________________________________________________________
	public void refrescarLista(ArrayList<Objeto> lista)
	{
		Iterator<Objeto> it = Objeto.findAll(Objeto.class);
		lista = Objeto.conectarHijos(it);//TODO:Por que no funciona con la lista pasada????? Lo deja duplicado y el nuevo no es editable???
		_expListView.setAdapter(new NivelUnoListAdapter(this.getApplicationContext(), _expListView, lista));
		_expListView.refreshDrawableState();
	}
	//______________________________________________________________________________________________
	public void selectObjeto(Objeto o)
	{
		//TODO: Guardar es estados visible de los objetos para dejarlos luego igual???
		/*
		if(o.getNivel() == Objeto.NIVEL1)
		{
			_expListView.setSelectedGroup();
		}
		else if(o.getNivel() == Objeto.NIVEL2)
		{
		}
		else
		{
		}*/

		//_expListView.setSelectedChild(groupPosition, childPosition, true);
	}



	//______________________________________________________________________________________________
	private static void datosTEST()
	{
		int i=0;
		Objeto[] lista = new Objeto[13];
		Objeto o0, o1, o2;

		Objeto.deleteAll(Objeto.class);

		o0 = new Objeto("HEALTH", null);
			o1 = new Objeto("SLEEP WELL", o0);
			o0.addHijo(o1);
				o2 = new Objeto("Don't loose time No TV", o1);
				o1.addHijo(o2);
				o2 = new Objeto("After diner go bed", o1);
				o1.addHijo(o2);
				o2 = new Objeto("Count & Plan sleep hours", o1);
				o1.addHijo(o2);
				o2 = new Objeto("Don't waste daytime 4 recover", o1);
				o1.addHijo(o2);
			o1 = new Objeto("EXERCISE", o0);
			o0.addHijo(o1);
				o2 = new Objeto("GYM 2-3 times a week", o1);
				o1.addHijo(o2);
				o2 = new Objeto("Home weights once", o1);
				o1.addHijo(o2);
			o1 = new Objeto("EAT WELL", o0);
			o0.addHijo(o1);
				o2 = new Objeto("Fresh Vegs & Fruit", o1);
				o1.addHijo(o2);
				o2 = new Objeto("No shitty food!", o1);
				o1.addHijo(o2);
				o2 = new Objeto("Protein shakes", o1);
				o1.addHijo(o2);
			o1 = new Objeto("METRO", o0);
			o0.addHijo(o1);
				o2 = new Objeto("Care 4ur clothes", o1);
				o1.addHijo(o2);
				o2 = new Objeto("Care 4ur skin", o1);
				o1.addHijo(o2);
		lista[i++]=o0;

		o0 = new Objeto("WEALTH", null);
			o1 = new Objeto("RENT T HOUSE", o0);
			o0.addHijo(o1);
				o2 = new Objeto("Change Sofa", o1);
				o1.addHijo(o2);
				o2 = new Objeto("Clean terrace", o1);
				o1.addHijo(o2);
				o2 = new Objeto("Sell bike parts", o1);
				o1.addHijo(o2);
			o1 = new Objeto("DONT SPEND WITHOUT NEED", o0);
			o0.addHijo(o1);
				o2 = new Objeto("Don't abuse AliExpress", o1);
				o1.addHijo(o2);
		lista[i++]=o0;

		o0 = new Objeto("TIME", null);
			o1 = new Objeto("PLAN ACTIVITIES", o0);
			o0.addHijo(o1);
				o2 = new Objeto("Day, Week, Month schedules", o1);
				o1.addHijo(o2);
			o1 = new Objeto("MULTITASKING", o0);
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

