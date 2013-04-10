package com.xinlan.rotatedemo;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.hardware.SensorEvent;
import android.hardware.SensorManager;

public class Background {
	private MainView context;
	private Bitmap mBitmap;
	private Rect src;
	private Rect dst;
	public int pad = 200;
	private int x, y;
	private Paint paint;
	private float rx, ry, rz;

	public Background(MainView context) {
		this.context = context;
		paint = new Paint();
		paint.setColor(Color.BLACK);
		paint.setTextSize(5.0f);
		mBitmap = BitmapFactory.decodeResource(context.getResources(),
				R.drawable.bg);
		x = mBitmap.getWidth() / 2 - pad;
		y = 0;
		src = new Rect(x, y, x + 2 * pad, y + mBitmap.getHeight());
		dst = new Rect(0, 0, MainView.screenW, MainView.screenH);
	}

	public void draw(Canvas canvas) {
		int dx = (int) (50 * ry);
		src = new Rect(x + dx, y, x + dx + 2 * pad, y + mBitmap.getHeight());
		canvas.drawBitmap(mBitmap, src, dst, null);
		canvas.drawText("x=" + rx + ",y=" + ry + ",z=" + rz, 200, 200, paint);
	}

	public void onSensorChanged(SensorEvent e) {
		// System.out.println("x="+x+",y="+y+",z="+z);
		this.rx = e.values[SensorManager.DATA_X];
		this.ry = e.values[SensorManager.DATA_Y];
		this.rz = e.values[SensorManager.DATA_Z];
	}
}// end class
