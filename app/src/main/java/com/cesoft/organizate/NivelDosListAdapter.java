package com.cesoft.organizate;

import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.SimpleExpandableListAdapter;
import android.widget.TextView;

import com.cesoft.organizate.models.Objeto;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class NivelDosListAdapter extends SimpleExpandableListAdapter
{
	//______________________________________________________________________________________________
	private static ArrayList<Objeto> _lista;
	public static void setLista(ArrayList<Objeto> lista){_lista=lista;}
	private static boolean _bIniRowHeight2 = true, _bIniRowHeight3 = true;

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
	/// NIVEL 3 --------------------------------------------------------------------------------
	@Override
    public View getChildView(final int groupPosition, final int childPosition, boolean isLastChild, View convertView, ViewGroup parent)
	{
		View v = super.getChildView(groupPosition, childPosition, isLastChild, convertView, parent );

		TextView txtChild = (TextView)v.findViewById(R.id.txtNivel3);
		txtChild.setOnClickListener(new View.OnClickListener()
		{
			@Override
			public void onClick(View view)
			{
				Intent intent = new Intent(_context, ActEdit.class);
				intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
				intent.putExtra(Objeto.class.getName(), _lista.get(_seccion).getHijos()[groupPosition].getHijos()[childPosition]);
				_context.startActivity(intent);
			}
		});

		if(_bIniRowHeight3)
		{
			_bIniRowHeight3 = false;
			v.measure(android.view.View.MeasureSpec.UNSPECIFIED, android.view.View.MeasureSpec.UNSPECIFIED);
			int height = v.getMeasuredHeight();
			CesExpandableListView.setRowHeight3(height);
		}

        return v;
    }

	//______________________________________________________________________________________________
	@Override
    public View getGroupView(final int groupPosition, boolean isExpanded, View convertView, final ViewGroup parent)
	{
        View v = super.getGroupView(groupPosition, isExpanded, convertView, parent);

		/// NIVEL 2 --------------------------------------------------------------------------------
		ImageButton btnEditar = (ImageButton)v.findViewById(R.id.btnEditarNivel2);
		btnEditar.setOnClickListener(new View.OnClickListener()
		{
			@Override
			public void onClick(View view)
			{
				Intent intent = new Intent(_context, ActEdit.class);
				intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
				intent.putExtra(Objeto.class.getName(), _lista.get(_seccion).getHijos()[groupPosition]);
				_context.startActivity(intent);
			}
		});
		btnEditar.setFocusable(false);//NO HACE CASO EN LAYOUT XML

		if(_bIniRowHeight2)
		{
			_bIniRowHeight2 = false;
			v.measure(android.view.View.MeasureSpec.UNSPECIFIED, android.view.View.MeasureSpec.UNSPECIFIED);
			int height = v.getMeasuredHeight();
			CesExpandableListView.setRowHeight2(height);
		}

        return v;
    }
}

