package com.xinlan.rotatedemo;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceHolder.Callback;
import android.view.SurfaceView;

public class MainView extends SurfaceView implements Callback, Runnable {
	private SurfaceHolder sfh;
	private Paint paint;
	private Thread th;
	private boolean flag;
	private Canvas canvas;
	private Context context;
	public static int screenW, screenH;
	private Resources res = this.getResources();

	public static int GAME_STATE = 1;

	public Background bg;
	public SensorEventListener lsn;
	public SensorManager sensorMgr;
	public Sensor sensor;
	public float x,y,z;

	public MainView(Context context) {
		super(context);
		this.context = context;
		sfh = this.getHolder();
		sfh.addCallback(this);
		paint = new Paint();
		paint.setColor(Color.WHITE);
		paint.setAntiAlias(true);
		setFocusable(true);
		setFocusableInTouchMode(true);
		this.setKeepScreenOn(true);
	}

	public void surfaceCreated(SurfaceHolder holder) {
		screenW = this.getWidth();
		screenH = this.getHeight();
		init();
		flag = true;
		th = new Thread(this);
		th.start();
	}

	/**
	 */
	public void init() {
		GAME_STATE = 1;
		bg = new Background(this);
		// 得到当前手机传感器管理对象

		sensorMgr= (SensorManager) context
				.getSystemService(Context.SENSOR_SERVICE);

		// 加速重力感应对象
		sensor = sensorMgr.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
		// 实例化一个监听器
		lsn = new SensorEventListener() {
			// 实现接口的方法
			public void onSensorChanged(SensorEvent e) {
				bg.onSensorChanged(e);
			}
			public void onAccuracyChanged(Sensor s, int accuracy) {
			}
		};

		// 注册listener，第三个参数是检测的精确度
		sensorMgr
				.registerListener(lsn, sensor, SensorManager.SENSOR_DELAY_GAME);
	}

	public void draw() {
		try {
			canvas = sfh.lockCanvas();
			if (canvas != null) {
				switch (GAME_STATE) {
				case 1:
					canvas.drawColor(Color.WHITE);
					bg.draw(canvas);
					//System.out.println("x="+x+",y="+y+",z="+z);
					break;
				}
			}// end if
		} catch (Exception e) {
		} finally {
			if (canvas != null) {
				sfh.unlockCanvasAndPost(canvas);
			}
		}
	}

	public void logic() {
		// player.logic();
		// teacher.logic();
	}

	public void run() {
		while (flag) {
			long start = System.currentTimeMillis();
			logic();
			draw();
			long end = System.currentTimeMillis();
			// System.out.println(end - start);
			try {
				if (end - start < 1) {
					Thread.sleep(1 - (end - start));
				}
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}// end while
	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		return true;
	}

	public void surfaceDestroyed(SurfaceHolder holder) {
		flag = false;
		sensorMgr.unregisterListener(lsn, sensor);
	}

	public void surfaceChanged(SurfaceHolder holder, int format, int width,
			int height) {
	}
}// end class
