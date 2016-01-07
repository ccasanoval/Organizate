package com.cesoftware.cestodo1;

import android.content.Context;
import android.widget.ExpandableListView;

////////////////////////////////////////////////////////////////////////////////////////////////////
public class CesExpandableListView extends ExpandableListView
{
	//TODO: try this with diferent mobiles/resolutions
	private static final int ROW_HEIGHT1 = 120;//
	private static final int ROW_HEIGHT2 = 120;//
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
		//this.getChildVisibleRect();
//+this.getClipBounds()
		System.err.println("AAA" + this + "--------------------" + rows1 + ".." + rows2 + ".." + rows3 + "------" + (rows1 * ROW_HEIGHT1 + rows2 * ROW_HEIGHT2 + rows3 * ROW_HEIGHT3) + "-------" + heightMeasureSpec + "::::" + getBottom());
		System.err.println("BBB" + (getBottom()>0 ? getBottom() : rows1*ROW_HEIGHT1)+"---"+rows2*ROW_HEIGHT2);
		setMeasuredDimension(getMeasuredWidth(), rows1 * ROW_HEIGHT1 + rows2 * ROW_HEIGHT2 + rows3*ROW_HEIGHT3);
		//DisplayMetrics metrics = getResources().getDisplayMetrics();
 		//getWindowManager().getDefaultDisplay().getMetrics(metrics);
    }

	//______________________________________________________________________________________________
	@Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom)
    {
        super.onLayout(changed, left, top, right, bottom);
    }
}

/*
TextView textView = (TextView)findViewById(R.id.my_textview);
ViewTreeObserver observer = textView.getViewTreeObserver();
observer.addOnGlobalLayoutListener(new OnGlobalLayoutListener() {
    @Override
    public void onGlobalLayout() {
        //in here, place the code that requires you to know the dimensions.
        //this will be called as the layout is finished, prior to displaying.
    }
}
*
*
*
final TextView tv = (TextView)findViewById(R.id.image_test);
ViewTreeObserver vto = tv.getViewTreeObserver();
vto.addOnGlobalLayoutListener(new OnGlobalLayoutListener() {

    @Override
    public void onGlobalLayout() {
        LayerDrawable ld = (LayerDrawable)tv.getBackground();
        ld.setLayerInset(1, 0, tv.getHeight() / 2, 0, 0);
        ViewTreeObserver obs = tv.getViewTreeObserver();

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            obs.removeOnGlobalLayoutListener(this);
        } else {
            obs.removeGlobalOnLayoutListener(this);
        }
    }

});*
* */
