package com.emon.androidapp3;

import android.R.integer;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Point;

public class Dock {
	Bitmap dockBitmap;
	int dockwidth,dockheight;
	int leftmostpoint,rightmostpoint;
	
	Point topleftPoint=new Point(0,0),bottomcenterPoint;
	DrawingThread drawingThread;
	
	boolean movingLeftFlag=false;
	boolean movingRightFlag=false;
	
	public Dock(DrawingThread drawingThread,int BitmapId) {
		this.drawingThread=drawingThread;
		Bitmap tempBitmap=BitmapFactory.decodeResource(drawingThread.context.getResources(),BitmapId);
		tempBitmap=Bitmap.createScaledBitmap(tempBitmap, drawingThread.displayX,drawingThread.displayX*tempBitmap.getHeight()/tempBitmap.getWidth(), true);
		
		
		dockBitmap=tempBitmap;
		dockheight=dockBitmap.getHeight();
		dockwidth=dockBitmap.getWidth();
	
		
		
		bottomcenterPoint=new Point((int)drawingThread.displayX/2,(int)drawingThread.displayY);
	    topleftPoint.y=bottomcenterPoint.y-dockheight;
	    UpdateInfo();
	}

	private void UpdateInfo() {
		leftmostpoint=bottomcenterPoint.x-dockwidth/2;
		rightmostpoint=bottomcenterPoint.x+dockwidth/2;
		
		
		topleftPoint.x=leftmostpoint;
		
	}
	public void moveDockToLeft() {
		bottomcenterPoint.x-=4;
		UpdateInfo();
	}
	public void moveDockToRight() {
		bottomcenterPoint.x+=4;
		UpdateInfo();
	}
	
	public void startMovingLeft() {
		Thread thread=new Thread(){
			@Override
			public void run() {
				movingLeftFlag=true;
				
				while (movingLeftFlag) {
					
			        moveDockToLeft();
					
					try {
						sleep(10);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			}
			
			
		};
		
		thread.start();
	}
	
	public void stopMovingLeft() {
		movingLeftFlag=false;
	}
	
	public void startMovingRight() {
		Thread thread=new Thread(){
			@Override
			public void run() {
				movingRightFlag=true;
				
				while (movingRightFlag) {
					
			        moveDockToRight();
					
					try {
						sleep(10);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			}
			
			
		};
		
		thread.start();
	}
	
	public void stopMovingRight() {
		movingRightFlag=false;
	}
}
