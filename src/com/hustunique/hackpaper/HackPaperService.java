package com.hustunique.hackpaper;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.FontMetrics;
import android.graphics.Typeface;
import android.os.Handler;
import android.os.SystemClock;
import android.service.wallpaper.WallpaperService;
import android.util.Log;
import android.view.MotionEvent;
import android.view.SurfaceHolder;

public class HackPaperService extends WallpaperService {
	private final Handler mHandler = new Handler();
	public int screenWidth;
	public int screenHeight;

	public int wordHeight;
	public int wordWidth;
	public int wordSize = 13;

	public static int rows;
	public int columns;

	public static int[][] snakePosition;

	@Override
	public void onCreate() {
		super.onCreate();
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
	}

	@Override
	public Engine onCreateEngine() {
		return new HackEngine();
	}

	public class HackEngine extends Engine {

		private final Paint mPaint = new Paint();
		private float mTouchX = -1;
		private float mTouchY = -1;
		private boolean mVisible;
		private CharSnake[] charSnakes;

		private Runnable mDrawWordsRunnable = new Runnable() {

			public void run() {
				System.out.println("run");

				drawFrame();
			}
		};

		public HackEngine() {
			// Create a Paint to draw the lines for our cube

			mPaint.setAntiAlias(true);
			mPaint.setColor(Color.GREEN);
			mPaint.setTextSize(wordSize);
			FontMetrics fontMetrics = mPaint.getFontMetrics();
			wordHeight = (int) (fontMetrics.bottom - fontMetrics.top);
			wordWidth = (int) (fontMetrics.descent - fontMetrics.ascent);
		}

		private void drawFrame() {
			final SurfaceHolder holder = getSurfaceHolder();
			Canvas mCanvas = null;
			try {
				mCanvas = holder.lockCanvas();
				if (mCanvas != null) {
					// draw something
					// drawCube(mCanvas);
					// drawTouchPoint(c);
					mCanvas.drawColor(Color.BLACK);
					// char[] text = new char[100];
					// int textLength = text.length;
					// for (int i = 0; i < textLength; i++) {
					// text[i] = charSet[(int) (charSet.length *
					// Math.random())];
					// }
					//
					// mPaint.setColor(Color.BLUE);
					// mPaint.setTypeface(Typeface.SANS_SERIF);
					// mPaint.setTextSize(20);
					// mCanvas.drawText(String.valueOf(text), 20, 400, mPaint);
					for (int i = 0; i < charSnakes.length; i++) {
						charSnakes[i].drawSelf(mCanvas, mPaint);
					}
				}
			} finally {
				if (mCanvas != null)
					holder.unlockCanvasAndPost(mCanvas);
			}

			// Reschedule the next redraw
			mHandler.removeCallbacks(mDrawWordsRunnable);
			if (mVisible) {
				mHandler.postDelayed(mDrawWordsRunnable, 20);
			}
		}

		@Override
		public void onCreate(SurfaceHolder surfaceHolder) {
			super.onCreate(surfaceHolder);
			setTouchEventsEnabled(true);
		}

		@Override
		public void onDestroy() {
			super.onDestroy();
			mHandler.removeCallbacks(mDrawWordsRunnable);
		}

		@Override
		public void onVisibilityChanged(boolean visible) {
			super.onVisibilityChanged(visible);
			Log.i("hacksurface", "onVisibleChanged()");
			mVisible = visible;
			if (visible) {
				drawFrame();
			} else {
				mHandler.removeCallbacks(mDrawWordsRunnable);
			}
		}

		@Override
		public void onSurfaceCreated(SurfaceHolder holder) {
			super.onSurfaceCreated(holder);
			Log.i("hackpaper", "onsurfaceCreate()");
		}

		@Override
		public void onSurfaceChanged(SurfaceHolder holder, int format,
				int width, int height) {
			super.onSurfaceChanged(holder, format, width, height);
			Log.i("hackpaper", "onsurfaceChanged()");
			screenHeight = height;
			screenWidth = width;
			System.out.println(screenHeight + "" + screenWidth);

			columns = (int) (screenWidth / wordWidth);
			rows = (int) (screenHeight / wordHeight);
			System.out.println("column:" + columns + "rows:" + rows);

			snakePosition = new int[columns][rows * 2];
			for (int i = 0; i < columns; i++) {
				for (int j = 0; j < rows * 2; j++) {
					if ((j % 2) == 0) {
						snakePosition[i][j] = (int) (wordWidth * i);
					} else {
						snakePosition[i][j] = wordHeight * j / 2;
					}
				}
			}

			charSnakes = new CharSnake[columns];
			for (int i = 0; i < charSnakes.length; i++) {
				charSnakes[i] = new CharSnake((int) (rows / 5 + rows * 3 / 5
						* Math.random()), (int) (3 + 15 * Math.random()), i);
			}

			drawFrame();
		}

		@Override
		public void onSurfaceDestroyed(SurfaceHolder holder) {
			super.onSurfaceDestroyed(holder);
			mVisible = false;
			mHandler.removeCallbacks(mDrawWordsRunnable);
		}

		@Override
		public void onOffsetsChanged(float xOffset, float yOffset,
				float xOffsetStep, float yOffsetStep, int xPixelOffset,
				int yPixelOffset) {
			Log.i("hackpaper", "onOffsetsChanged()");
			drawFrame();
		}

		@Override
		public void onTouchEvent(MotionEvent event) {
			if (event.getAction() == MotionEvent.ACTION_MOVE) {
				mTouchX = event.getX();
				mTouchY = event.getY();
			} else {
				mTouchX = -1;
				mTouchY = -1;
			}
			super.onTouchEvent(event);

		}

	}
}
