package com.hustunique.hackpaper;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;

public class CharSnake {
	private final char[] charSet = "qwertyuiop[]\';lkjhgfdazxcvbnm,./=-`~!@#$%^&*()_+}{POIUYTREWADDFGHJKL:\"?><MNBVCXZ"
			.toCharArray();
	int snakeLength;
	char[] snakeBody;
	int headIndex;
	int tailIndex;

	int speed;
	long bornTime;

	int columnIndex;

	public CharSnake(int length, int speed, int columnIndex) {
		snakeLength = length;
		this.speed = speed;
		this.columnIndex = columnIndex;
		bornTime = System.currentTimeMillis();
		snakeBody = new char[HackPaperService.rows];
		for (int i = 0; i < HackPaperService.rows; i++) {
			snakeBody[i] = randomChar();
		}

		headIndex = 1;
		tailIndex = 0;
	}

	char randomChar() {
		return charSet[(int) (charSet.length * Math.random())];
	}

	void drawSelf(Canvas canvas, Paint paint) {

		float[] subPosi = getSubArray(headIndex * 2, snakeLength * 2,
				HackPaperService.snakePosition[columnIndex]);

		char[] subChar = getSubArray(headIndex, snakeLength, snakeBody);

		canvas.drawPosText(subChar, 0, subChar.length, subPosi, paint);

		paint.setColor(Color.WHITE);
		canvas.drawText(new char[] { snakeBody[headIndex] }, 0, 1,
				HackPaperService.snakePosition[columnIndex][2 * headIndex],
				HackPaperService.snakePosition[columnIndex][headIndex * 2 + 1],
				paint);

		paint.setColor(Color.GREEN);

		headIndex = (int) ((System.currentTimeMillis() - bornTime) * speed
				/ 1000 % HackPaperService.rows);
	}

	char[] getSubArray(int from, int count, char[] array) {
		if (from < 0 || from >= array.length || count >= array.length)
			return null;

		char[] subArray = new char[count];
		for (int i = 0; i < count; i++) {
			subArray[i] = array[from % array.length];
			from++;
		}
		return subArray;
	}

	float[] getSubArray(int from, int count, int[] array) {
		if (from < 0 || from >= array.length || count >= array.length)
			return null;

		float[] subArray = new float[count];
		for (int i = 0; i < count; i++) {
			subArray[i] = array[from % array.length];
			from++;
		}
		return subArray;
	}
}
