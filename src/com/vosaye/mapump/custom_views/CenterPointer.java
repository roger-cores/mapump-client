package com.vosaye.mapump.custom_views;

import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Build;
import android.util.AttributeSet;
import android.view.View;

public class CenterPointer extends View {

	Paint myPaint;
	
	float centerX, centerY;

	
	public CenterPointer(Context context) {
		super(context);
		init();
	}

	public CenterPointer(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
		super(context, attrs, defStyleAttr);
		init();
	}

	public CenterPointer(Context context, AttributeSet attrs, int defStyleAttr) {
		super(context, attrs, defStyleAttr);
		init();
	}

	public CenterPointer(Context context, AttributeSet attrs) {
		super(context, attrs);
		init();
	}

	


	private void init() {
		myPaint = new Paint();
		myPaint.setColor(Color.BLACK);
		myPaint.setStyle(Paint.Style.STROKE);
		myPaint.setStrokeWidth(6);
	}

	@Override
	protected void onDraw(Canvas canvas) {
		int w = getWidth();
		int h = getHeight();

		
		
		centerX = (float) w / 2;
		centerY = (float) h / 2;
		
		canvas.drawLine(centerX - 30, centerY, centerX + 30, centerY, myPaint);
		canvas.drawLine(centerX, centerY - 30, centerX, centerY + 30, myPaint);
		

	}

	

}
