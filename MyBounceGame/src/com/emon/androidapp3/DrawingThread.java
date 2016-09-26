package com.emon.androidapp3;

import java.util.ArrayList;

import android.R.color;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.Paint.Align;
import android.view.Display;
import android.view.WindowManager;

public class DrawingThread extends Thread{
  private Canvas canvas;
  GameView gameView;
  Context context;
  
  boolean ThreadFlag=false;
  boolean touchFlag=false;
  boolean pauseFlag=false;
  
  
  Bitmap backgroundBitmap;
  int displayX,displayY;
  int Maxscore=0;
	
   ArrayList<Robot>allRobots;
   ArrayList<Bitmap>allPossibleRobots;
   
   
   AnimationThread animationThread;
   ScoreCounterThread scoreCounterThread;
   GameActivity gameActivity;
   
    Dock dock;
    
    Paint scorePaint;
  
	public DrawingThread(GameView gameView, Context context) {
	super();
    this.gameView = gameView;
	this.context = context;
	initilizeAll();
}



	private void initilizeAll() {
		WindowManager windowManager=(WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
		Display defaultDisplay=windowManager.getDefaultDisplay();
		
		Point displayDimenstion=new Point();
		defaultDisplay.getSize(displayDimenstion);
		
		displayX=displayDimenstion.x;
		displayY=displayDimenstion.y;
		
		backgroundBitmap=BitmapFactory.decodeResource(context.getResources(),R.drawable.background);
		backgroundBitmap=Bitmap.createScaledBitmap(backgroundBitmap, displayX, displayY, true);
		
		initializeAllPossibleRobots();
		scoreCounterThread=new ScoreCounterThread(this);
		dock=new Dock(this,R.drawable.dock);
	    scorePaint=new Paint();
		scorePaint.setColor(Color.WHITE);
		scorePaint.setTextAlign(Align.CENTER);
		scorePaint.setTextSize(displayX/10);
	}



	private void initializeAllPossibleRobots() {
		allRobots=new ArrayList<Robot>();
		allPossibleRobots=new ArrayList<Bitmap>();
		
		allPossibleRobots.add(giveResizedRobotBitmap(R.drawable.robot1));
		allPossibleRobots.add(giveResizedRobotBitmap(R.drawable.robot2));
		allPossibleRobots.add(giveResizedRobotBitmap(R.drawable.robot3));
		allPossibleRobots.add(giveResizedRobotBitmap(R.drawable.robot4));
		allPossibleRobots.add(giveResizedRobotBitmap(R.drawable.robot5));
		
		
	}
	
	
	private Bitmap giveResizedRobotBitmap(int resourceID) {
		Bitmap tempBitmap=BitmapFactory.decodeResource(context.getResources(),resourceID);
		tempBitmap=Bitmap.createScaledBitmap(tempBitmap,displayX/5,(tempBitmap.getHeight()/tempBitmap.getWidth())*(displayX/5), true);
		return tempBitmap;
	}



	@Override
	public void run() {
		ThreadFlag=true;
		animationThread=new AnimationThread(this);
		
		animationThread.start();
		scoreCounterThread.start();
		
		while (ThreadFlag) {
			canvas=gameView.surfaceHolder.lockCanvas();
			
			
			try {
				synchronized (gameView.surfaceHolder) {//display update blog
					
					updateDisplay();
					
				}
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}finally{
				if (canvas!=null) {
					gameView.surfaceHolder.unlockCanvasAndPost(canvas);
				}
			}
			
			
			try {
				Thread.sleep(10);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		}
		
		animationThread.stopThread();
		scoreCounterThread.stopThread();
	}
	
	public void stopThread() {
		ThreadFlag=false;
	}
	
	private void updateDisplay() {
		canvas.drawBitmap(backgroundBitmap, 0, 0, null);
		drawDock();
		
		
		for (int i = 0; i < allRobots.size(); i++) {
			Robot tempRobot=allRobots.get(i);
			canvas.drawBitmap(tempRobot.robotBitmap,tempRobot.centerX-(tempRobot.width/2),tempRobot.centerY-(tempRobot.height/2),tempRobot.robotPaint);
		}
		
		//drawSensorsData();
		
		if (pauseFlag) {
			pausestateDraw();
		}
		drawScore();
	}


	 
	
	private void pausestateDraw() {
//		gameActivity.stopUsingSensors();
		Paint paint=new Paint();
		paint.setColor(Color.WHITE);
		paint.setTextSize(100);
		paint.setTextAlign(Align.CENTER);
		canvas.drawARGB(170, 0, 0, 0);
		canvas.drawText("PAUSED",displayX/2,displayY/2,paint);		
		drawScore();
	}



	private void drawSensorsData() {
		Paint paint=new Paint();
		paint.setColor(color.holo_green_dark);
		paint.setTextSize(displayX/10);
		canvas.drawText("X-axis :"+GameActivity.getgX(), 0, displayY/3, paint);
		canvas.drawText("Y-axis :"+GameActivity.getgY(), 0, displayY/3+(displayX/5), paint);
	}
	
	private void drawDock() {
		canvas.drawBitmap(dock.dockBitmap,dock.topleftPoint.x,dock.topleftPoint.y,null);
	}
	
	private void drawScore() {
		if (Maxscore>1000) {
			scorePaint.setColor(Color.GREEN);
			
			if (Maxscore>10000) {
				scorePaint.setColor(Color.CYAN);
				
				if (Maxscore>100000) {
					scorePaint.setColor(Color.RED);
				}
			}
		}
		
		
		canvas.drawText("Score : "+Maxscore,displayX/2,displayY/7, scorePaint);
	}
	
}
