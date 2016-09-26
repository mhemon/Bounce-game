package com.emon.androidapp3;

public class AnimationThread extends Thread {
	private boolean flag=false;
	float gravityX,gravityY;
	float timeconstant=0.3f;
	float redartationratio=-0.5f;
	int width,height;
	int left,right,top,bottom;
	
	DrawingThread drawingThread;
	
	public AnimationThread(DrawingThread drawingThread) {
		super();
		this.drawingThread = drawingThread;
		updateDimenstion();
	}

	private void updateDimenstion() {
		width=drawingThread.allPossibleRobots.get(0).getWidth();
		height=drawingThread.allPossibleRobots.get(0).getHeight();
		left=width/2;
		top=height/2;
		right=drawingThread.displayX-(width/2);
		bottom=drawingThread.displayY-(height/2);
	}

	@Override
	public void run() {
		flag=true;
		while (flag) {
			updateallPosition();
			
			try {
				sleep(10);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	
	private void updateallPosition() {
		gravityX=GameActivity.getgX();
		gravityY=GameActivity.getgY();
		
		if (drawingThread.touchFlag) {
			for (int i = 0; i <drawingThread.allRobots.size()-1; i++) {
				updateallrobotposition(drawingThread.allRobots.get(i),i);
			}
		}
			else {
				for (int i = 0; i <drawingThread.allRobots.size(); i++) {
					updateallrobotposition(drawingThread.allRobots.get(i),i);
				}
			}
		}

	

	private void updateallrobotposition(Robot robot,int position) {
		//for x axis......
		robot.centerX+=(robot.velocityX*timeconstant+0.5*gravityX*timeconstant*timeconstant);
		robot.velocityX+=gravityX*timeconstant;
		
		//for y axis.....
		robot.centerY+=(robot.velocityY*timeconstant+0.5*gravityY*timeconstant*timeconstant);
		robot.velocityY+=gravityY*timeconstant;
	
		constrainPosition(robot,position);
	}

 private void constrainPosition(Robot robot,int position) {
	 // for x axis........
		if (robot.centerX<left) {
			robot.centerX=left;
			robot.velocityX*=redartationratio;
		}else if (robot.centerX>right) {
			robot.centerX=right;
			robot.velocityX*=redartationratio;
		}
		
		//for y axis.............
//		if (robot.centerY<top) {
//			robot.centerY=top;
//			robot.velocityY*=redartationratio;
//		}else 
			
			if (robot.centerY>bottom) {
			   if (isRobotOutsideDock(robot)) {
				robot.robotFelldown=true;
				if (robot.centerY>bottom+height) {
					drawingThread.allRobots.remove(position);
				}
				
			}
			   
			   if (robot.robotFelldown==false) {
				   robot.centerY=bottom;
				   robot.velocityY*=redartationratio;
			}
			
		}
		
	}

public void stopThread() {
		flag=false;
	}
	private boolean isRobotOutsideDock(Robot robot) {
		if (robot.centerX+(width/2)<drawingThread.dock.leftmostpoint||robot.centerX-(width/2)>drawingThread.dock.rightmostpoint) {
			return true;
		}
		
		return false;
	}

}
