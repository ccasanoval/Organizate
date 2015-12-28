package com.cesoftware.cestodo1;

import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.view.ViewGroup;
import android.view.LayoutInflater;
import android.widget.ExpandableListView;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.BaseExpandableListAdapter;

import com.cesoftware.cestodo1.models.Objeto;

import java.util.List;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

////////////////////////////////////////////////////////////////////////////////////////////////////
public class NivelUnoListAdapter extends BaseExpandableListAdapter
{
	private Context _context;
	private ArrayList<Objeto> _lista;
	private LayoutInflater _inflater;
	private ExpandableListView _topExpList;
	private CesExpandableListView _listViewCache[];

	private static final String NIVEL2 = "NIVEL2";
	private static final String NIVEL3 = "NIVEL3";

	//______________________________________________________________________________________________
    public NivelUnoListAdapter(Context context, ExpandableListView topExpList, ArrayList<Objeto> lista)
    {
        _context = context;
        _topExpList = topExpList;
		_inflater = LayoutInflater.from(context);

		ArrayList<Objeto> nivel1 = Objeto.filtroN(lista, Objeto.NIVEL1);
        _lista = nivel1;
		_listViewCache = new CesExpandableListView[nivel1.size()];
		NivelDosListAdapter.setLista(nivel1);
    }

	//______________________________________________________________________________________________
	@Override
    public boolean hasStableIds(){return true;}
	//______________________________________________________________________________________________
	@Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {return true;}
	//______________________________________________________________________________________________
	@Override
    public void onGroupCollapsed(int groupPosition){}
	//______________________________________________________________________________________________
	@Override
    public void onGroupExpanded(int groupPosition){}

	//______________________________________________________________________________________________
	@Override
    public Object getGroup(int groupPosition)
	{
		return _lista.get(groupPosition).getNombre();
	}
	//______________________________________________________________________________________________
	@Override
    public Object getChild(int groupPosition, int childPosition)
    {
        return _lista.get(groupPosition).getHijos()[childPosition];
    }

	//______________________________________________________________________________________________
	@Override
    public int getGroupCount() {return _lista.size();}
	//______________________________________________________________________________________________
	@Override
    public int getChildrenCount(int groupPosition) {return 1;}

	//______________________________________________________________________________________________
	@Override
    public long getGroupId(int groupPosition)
	{
        return (long)( groupPosition*1024 );  // To be consistent with getChildId
    }
	//______________________________________________________________________________________________
	@Override
    public long getChildId(int groupPosition, int childPosition)
    {
        return (long)( groupPosition*1024+childPosition );  // Max 1024 children per group
    }


	//______________________________________________________________________________________________
	@Override
    public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent)
    {
//System.err.println("getChildView--------------------groupPosition="+groupPosition+" / childPosition="+childPosition+" / isLastChild="+isLastChild);
		CesExpandableListView dev = new CesExpandableListView(_context);
		dev.setRows(calculateRowCount(groupPosition, null));
		dev.setAdapter(new NivelDosListAdapter(
				_context,
				createGroupList(groupPosition),		// groupData describes the first-level entries
				R.layout.nivel2,				// Layout for the first-level entries
				new String[]{NIVEL2},				// Key in the groupData maps to display
				new int[]{R.id.childname},			// Data under "colorName" key goes into this TextView
				createChildList(groupPosition),		// childData describes second-level entries
				R.layout.nivel3,				// Layout for second-level entries
				new String[]{NIVEL3},				// Keys in childData maps to display
				new int[]{R.id.childname},			// Data under the keys above go into these TextViews
				groupPosition
			));
		dev.setOnGroupClickListener(new Level2GroupExpandListener(groupPosition));
		_listViewCache[groupPosition] = dev;

		/* NADIE LO LLAMA ¿?
		dev.setOnChildClickListener(new ExpandableListView.OnChildClickListener()
		{
			@Override
			public boolean onChildClick(ExpandableListView parent, View v, int groupPosition, int childPosition, long id)
			{
				System.err.println("*******************************---------------------------------***" + _lista[groupPosition].getHijos()[childPosition].toString());
				return true;
			}
		});*/

        return dev;
    }
	//______________________________________________________________________________________________
	private List createGroupList(int seccion)
	{
        List<Map> result = new ArrayList<>();
	    for(Objeto o: _lista.get(seccion).getHijos())
	    {
			HashMap<String, String> m = new HashMap<>();
	        m.put(NIVEL2, o.getNombre());
	    	result.add(m);
	    }
	    return result;
    }
	//______________________________________________________________________________________________
	private List createChildList(int seccion)
	{
		List<List> result = new ArrayList<>();
		for(Objeto o : _lista.get(seccion).getHijos())
		{
			List<HashMap> secList = new ArrayList<>();
			for(Objeto o2 : o.getHijos())
			{
				HashMap<String, String> child = new HashMap<>();
		        child.put(NIVEL3, o2.getNombre());
				secList.add(child);
			}
			result.add(secList);
		}
		return result;
	}
	////////////////////////////////////////////////////////////////////////////////////////////////
	class Level2GroupExpandListener implements ExpandableListView.OnGroupClickListener
	{
		public Level2GroupExpandListener(int level1GroupPosition)
		{
			this.level1GroupPosition = level1GroupPosition;
		}
		@Override
		public boolean onGroupClick(ExpandableListView parent, View v, int groupPosition, long id)
		{
			if(parent.isGroupExpanded(groupPosition))
				parent.collapseGroup(groupPosition);
			else
				parent.expandGroup(groupPosition);
			if(parent instanceof CesExpandableListView)
			{
				CesExpandableListView dev = (CesExpandableListView)parent;
				dev.setRows(calculateRowCount(level1GroupPosition, parent));
			}
			_topExpList.requestLayout();
			return true;
		}

		private int level1GroupPosition;
	}

	//______________________________________________________________________________________________
	@Override
    public View getGroupView(final int groupPosition, boolean isExpanded, View convertView, ViewGroup parent)
	{
        View v = null;
        if( convertView != null)
            v = convertView;
        else
			v = _inflater.inflate(R.layout.nivel1, parent, false);
        String gt = (String)getGroup(groupPosition).toString();
		TextView colorGroup = (TextView)v.findViewById( R.id.groupname );
		if( gt != null )
			colorGroup.setText( gt );

		/// NIVEL 1 --------------------------------------------------------------------------------
		ImageButton btnEditar = (ImageButton)v.findViewById(R.id.btn_editar);
		btnEditar.setOnClickListener(new View.OnClickListener()
		{
			@Override
			public void onClick(View view)
			{
				Intent intent = new Intent(_context, ActEdit.class);
				intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
				intent.putExtra("objeto", _lista.get(groupPosition));
    			_context.startActivity(intent);
			}
		});
		btnEditar.setFocusable(false);//NO HACE CASO EN LAYOUT XML

        return v;
    }

	//______________________________________________________________________________________________
	// Calculates the rows counts
    private int[] calculateRowCount(int level1, ExpandableListView level2view)
    {
		int[] rowCtr = {0,0,0};
		if(level2view == null)
		{
			rowCtr[1] += _lista.get(level1).getHijos().length;
		}
		else
		{
			++rowCtr[0];
			Objeto[] ao = _lista.get(level1).getHijos();
			for(int j=0; j < ao.length; j++)
			{
				++rowCtr[1];
				if(level2view.isGroupExpanded(j))
					rowCtr[2] += ao[j].getHijos().length;
			}
		}
System.err.println("calculateRowCount---------------------" + level1 + " / " + level2view + "------------" + rowCtr[0]+":"+rowCtr[1]+":"+rowCtr[2] + "::::" + (level2view != null ? level2view.getCount() : 0));
		return rowCtr;
    }
}
