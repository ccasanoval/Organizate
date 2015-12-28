package com.cesoftware.cestodo1;

import android.content.Context;
import android.widget.ExpandableListView;

////////////////////////////////////////////////////////////////////////////////////////////////////
public class CesExpandableListView extends ExpandableListView
{
	//TODO: try this with diferent mobiles/resolutions
	private static final int ROW_HEIGHT1 = 130;//
	private static final int ROW_HEIGHT2 = 130;//
	private static final int ROW_HEIGHT3 = 110;//  x4!!
//	private static final int ROW_HEIGHT1 = 30;
//	private static final int ROW_HEIGHT2 = 36;
//	private static final int ROW_HEIGHT3 = 23;
	private int rows1, rows2, rows3;

	//______________________________________________________________________________________________
    public CesExpandableListView(Context context){super( context );}

	//______________________________________________________________________________________________
	public void setRows(int[] rows)
	{
		this.rows1 = rows[0];
		this.rows2 = rows[1];
		this.rows3 = rows[2];
//System.err.println("BBB"+this+"--------"+rows1+".." + rows2+"..."+rows3+"-------" + (rows1 * ROW_HEIGHT1 + rows2*ROW_HEIGHT2 + rows3*ROW_HEIGHT3));
	}

	//______________________________________________________________________________________________
	@Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec)
    {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    	setMeasuredDimension(getMeasuredWidth(), rows1 * ROW_HEIGHT1 + rows2*ROW_HEIGHT2 + rows3*ROW_HEIGHT3);
		//DisplayMetrics metrics = getResources().getDisplayMetrics();
 		//getWindowManager().getDefaultDisplay().getMetrics(metrics);
//System.err.println("AAA"+this+"-----------------------"+rows1+".." + rows2+"..."+rows3+"-------" + (rows1 * ROW_HEIGHT1 + rows2*ROW_HEIGHT2 + rows3*ROW_HEIGHT3) + "-------" + heightMeasureSpec+"   this="+this);
    }

	//______________________________________________________________________________________________
	@Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom)
    {
        super.onLayout(changed, left, top, right, bottom);
    }
}

