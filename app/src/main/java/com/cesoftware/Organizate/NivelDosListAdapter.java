package com.cesoftware.Organizate;

import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.SimpleExpandableListAdapter;
import android.widget.TextView;

import com.cesoftware.Organizate.models.Objeto;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class NivelDosListAdapter extends SimpleExpandableListAdapter
{
	//______________________________________________________________________________________________
	private static ArrayList<Objeto> _lista;
	public static void setLista(ArrayList<Objeto> lista){_lista=lista;}

	private Context _context;
	private int _seccion = 0;

	//______________________________________________________________________________________________
    public NivelDosListAdapter(Context context, List<? extends Map<String, ?>> groupData, int groupLayout, String[] groupFrom, int[] groupTo, List<? extends List<? extends Map<String, ?>>> childData, int childLayout, String[] childFrom, int[] childTo, int groupPosition)
    {
        super(context, groupData, groupLayout, groupFrom, groupTo, childData, childLayout, childFrom, childTo);
		_context = context;
		_seccion=groupPosition;
    }

	//______________________________________________________________________________________________
	@Override
    public View getChildView(final int groupPosition, final int childPosition, boolean isLastChild, View convertView, ViewGroup parent)
	{
        View v = super.getChildView(groupPosition, childPosition, isLastChild, convertView, parent );

		/// NIVEL 3 --------------------------------------------------------------------------------
		TextView txtChild = (TextView)v.findViewById(R.id.childname);
		txtChild.setOnClickListener(new View.OnClickListener()
		{
			@Override
			public void onClick(View view)
			{
				Intent intent = new Intent(_context, ActEdit.class);
				intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
				intent.putExtra("objeto", _lista.get(_seccion).getHijos()[groupPosition].getHijos()[childPosition]);
				_context.startActivity(intent);
			}
		});

        return v;
    }

	//______________________________________________________________________________________________
	@Override
    public View getGroupView(final int groupPosition, boolean isExpanded, View convertView, final ViewGroup parent)
	{
        View v = super.getGroupView( groupPosition, isExpanded, convertView, parent );

		/// NIVEL 2 --------------------------------------------------------------------------------
		ImageButton btnEditar = (ImageButton)v.findViewById(R.id.btn_editar);
		btnEditar.setOnClickListener(new View.OnClickListener()
		{
			@Override
			public void onClick(View view)
			{
				Intent intent = new Intent(_context, ActEdit.class);
				intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
       			intent.putExtra("objeto", _lista.get(_seccion).getHijos()[groupPosition]);
    			_context.startActivity(intent);
			}
		});
		btnEditar.setFocusable(false);//NO HACE CASO EN LAYOUT XML

        return v;
    }
}

