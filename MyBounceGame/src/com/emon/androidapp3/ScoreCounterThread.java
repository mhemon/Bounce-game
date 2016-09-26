package com.emon.androidapp3;

public class ScoreCounterThread extends Thread{

	
	DrawingThread drawingThread;
	boolean ThreadRunningFlag=false;
	
	public ScoreCounterThread(DrawingThread drawingThread) {
		this.drawingThread=drawingThread;
	}
	
	@Override
	public void run() {
		ThreadRunningFlag=true;
		while (ThreadRunningFlag) {
			float tempMax=0;
			
			for (Robot robot:drawingThread.allRobots) {
				if (robot.centerY<tempMax) {
					tempMax=robot.centerY;
				}
			}
			
			drawingThread.Maxscore=(int) (drawingThread.Maxscore>(-tempMax)? drawingThread.Maxscore:(-tempMax));
			
			try {
				sleep(100);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	public void stopThread() {
		ThreadRunningFlag=false;
	}
	
}
