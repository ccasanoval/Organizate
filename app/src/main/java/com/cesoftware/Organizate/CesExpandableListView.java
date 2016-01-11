package com.cesoftware.Organizate;

import android.content.Context;
import android.widget.ExpandableListView;

////////////////////////////////////////////////////////////////////////////////////////////////////
public class CesExpandableListView extends ExpandableListView
{
    private static int ROW_HEIGHT2 = 100;   public static void setRowHeight2(int v){ROW_HEIGHT2=v+1;ROW_HEIGHT3=v+1;}//TODO:tnenr la dimensin 3 antes de mostrar
    private static int ROW_HEIGHT3 = 100;   public static void setRowHeight3(int v){ROW_HEIGHT3=v+1;}
	private int rows2, rows3;

	//______________________________________________________________________________________________
    public CesExpandableListView(Context context){super( context );}

	//______________________________________________________________________________________________
	public void setRows(int[] rows)
	{
		this.rows2 = rows[1];
		this.rows3 = rows[2];
	}

	//______________________________________________________________________________________________
	@Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec)
    {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
	System.err.println("AAA" + "-----------" + rows2 + " ("+ROW_HEIGHT2+")..." + rows3+ " ("+ROW_HEIGHT3+") ------" + (rows2 * ROW_HEIGHT2 + rows3 * ROW_HEIGHT3) + "---" + heightMeasureSpec + "::::" + getBottom());
		setMeasuredDimension(getMeasuredWidth(), rows2 * ROW_HEIGHT2 + rows3*ROW_HEIGHT3);
    }

	//______________________________________________________________________________________________
	@Override
	protected void onLayout(boolean changed, int left, int top, int right, int bottom)
	{
		super.onLayout(changed, left, top, right, bottom);
	}
}
