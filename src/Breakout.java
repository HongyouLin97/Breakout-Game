/*
 * File: Breakout.java
 * -------------------
 * Name: Hongyou Lin
 * 
 * This file will eventually implement the game of Breakout.
 */

import java.awt.Color;

import java.awt.event.MouseEvent;

import acm.program.GraphicsProgram;
import acm.util.RandomGenerator;
import acm.graphics.*;

public class Breakout extends GraphicsProgram {

	/** Width and height of application window in pixels */
	public static final int APPLICATION_WIDTH = 400;
	public static final int APPLICATION_HEIGHT = 600;

	/** Dimensions of game board (usually the same) */
	private static final int WIDTH = APPLICATION_WIDTH;
	private static final int HEIGHT = APPLICATION_HEIGHT;

	/** Dimensions of the paddle */
	private static final int PADDLE_WIDTH = 60;
	private static final int PADDLE_HEIGHT = 10;

	/** Offset of the paddle up from the bottom */
	private static final int PADDLE_Y_OFFSET = 30;

	/** Number of bricks per row */
	private static final int NBRICKS_PER_ROW = 10; 

	/** Number of rows of bricks */
	private static final int NBRICK_ROWS = 10;

	/** Separation between bricks */
	private static final int BRICK_SEP = 4;

	/** Width of a brick */
	private static final int BRICK_WIDTH =
	  (WIDTH - (NBRICKS_PER_ROW - 1) * BRICK_SEP) / NBRICKS_PER_ROW;

	/** Height of a brick */
	private static final int BRICK_HEIGHT = 8;

	/** Radius of the ball in pixels */
	private static final int BALL_RADIUS = 10;

	/** Offset of the top brick row from the top */
	private static final int BRICK_Y_OFFSET = 70;

	/** Number of turns */
	private static final int NTURNS = 3;
	
	private static final int TIME_DELAY_FOR_BALL = 10;
	
	private GRect drawBrick;
	
	private GRect paddle;
	
	private GOval ball;
	
	private int turns = NTURNS;
	
	private int countBricks =  NBRICKS_PER_ROW*NBRICK_ROWS;
	
	private double vx,vy;

	public void init() {   
		resize(WIDTH,HEIGHT); 
	}
	
	/*
	 * Decomposition according to the functionality of methods;
	 */
	public void run() {   
		setUp();
		startGame();
	}
	
	private void setUp() {   // have further decompositions in terms of categories after the general decomposition of "public void run";
		createBricks();
		createPaddle();
		createBall();
	}
	
	private void createBricks(){
		double x = getWidth()/2-NBRICKS_PER_ROW/2*BRICK_WIDTH-(NBRICKS_PER_ROW-1)*BRICK_SEP/2;  
		double y = BRICK_Y_OFFSET;
			for (int brick = 0; brick < NBRICKS_PER_ROW; brick++){  // let the drawing proceeds to the right until the tenth brick is drawn;
				for (int row = 0; row < NBRICK_ROWS; row++){   // let the drawing proceeds downwards ten times to reach the end; 
					drawBrick = new GRect(x+brick*(BRICK_WIDTH+BRICK_SEP),y+row*(BRICK_HEIGHT+BRICK_SEP),BRICK_WIDTH,BRICK_HEIGHT);  // each time the position for the bricks is changing its coordinate's values, which are directly proportional to the variables; on x direction, the position will increase one unit of brick width and brick separation in one count; on y direction, the position will increase one unit of brick height and brick separation in each count.
					if (row <2){
					drawBrick.setColor(Color.RED);
					}
					if (row<4 && row>=2){
						drawBrick.setColor(Color.ORANGE);
					}
					if (row<6 && row>=4){
						drawBrick.setColor(Color.YELLOW);
					}
					if (row<8 && row>=6){
						drawBrick.setColor(Color.GREEN);
					}
					if (row<10 && row>=8){
						drawBrick.setColor(Color.CYAN);
					}         
					drawBrick.setFilled(true);          // by using the variables "row" and control statement, assign different colors to different bricks in different rows;
					add (drawBrick);
				}	
			}
	}
	
	private void createPaddle(){
		paddle = new GRect (getWidth()/2-PADDLE_WIDTH/2,getHeight()-PADDLE_Y_OFFSET-PADDLE_HEIGHT,PADDLE_WIDTH,PADDLE_HEIGHT);
		paddle.setColor(Color.BLACK);
		paddle.setFilled(true);
		add(paddle);
		addMouseListeners();
	}
	
	/*
	 * "mouseMoved" method serves for the "createPaddle" method;
	 */
	public void mouseMoved(MouseEvent e) {      
		double mouseX = e.getX();
		if ((e.getX() < getWidth() - PADDLE_WIDTH/2)) {
				if((e.getX() > PADDLE_WIDTH/2)) {     // make sure that the paddle doesn't go off the screen's boundary;
					paddle.setLocation(mouseX - PADDLE_WIDTH/2, getHeight() - PADDLE_Y_OFFSET - PADDLE_HEIGHT);  // allow the paddle to go with the mouse and stay at the original horizon;
				}
		}
	}
	
	private void createBall() {
		ball = new GOval (getWidth()/2-BALL_RADIUS/2,getHeight()/2-BALL_RADIUS/2,BALL_RADIUS,BALL_RADIUS);
		ball.setColor(Color.BLACK);
		ball.setFilled(true);
		add(ball);	
	}
	
	// set the velocity only at the beginning of the game; served for the "createBall" function;
	private void setVelocity() {
		RandomGenerator rgen = RandomGenerator.getInstance();  // declare variable "rgen" for random events;
		vx = rgen.nextDouble(0.0,10.0);		
		if (rgen.nextBoolean(0.5)) {
			vx = -vx;
		}
		vy = 4.0;
	}
		
	// only makes the ball react to the collisions to the walls on two sides, the paddle and the bricks; 
	private void reactToCollision() { 
		if ((ball.getX() + 2* BALL_RADIUS) >= getWidth() || ball.getX() <= 0) { 
				vx = -vx;
		}
		if (ball.getY()<=0) {
			vy = -vy;
		}
		GObject collider = getCollidingObject(); //assigns the result value of "getCollidingObject" to GRect "collider" in order to determine whether the ball is collide with a paddle, a wall or a brick;
		if (collider == paddle) {
			if (belowThePaddleOrNot(ball) && vy > 0) {   // solve the bugs that the ball will bounce "inside" the paddle;
				vy = -vy;
			}
		}
		else if (collider != null) {  
			remove(collider);
			countBricks--;
			vy = -vy;
		}
		ball.move(vx,vy); 
		pause(TIME_DELAY_FOR_BALL);     // help to show the animation;
	}
	
	// by using "getElementAT" methods to check whether or not there is another "GObject" on the four points on the frame of the ball;
	private GObject getCollidingObject() {     
		if (getElementAt(ball.getX(), ball.getY()) != null) {
			return getElementAt(ball.getX(), ball.getY());
		}
		if (getElementAt(ball.getX()+2*BALL_RADIUS, ball.getY()) != null) {
			return getElementAt(ball.getX()+2*BALL_RADIUS, ball.getY());
		}
		if (getElementAt(ball.getX(), ball.getY()+2*BALL_RADIUS) != null) {
			return getElementAt(ball.getX(), ball.getY()+2*BALL_RADIUS);
		}
		if (getElementAt(ball.getX()+2*BALL_RADIUS, ball.getY()+2*BALL_RADIUS) != null) {
			return getElementAt(ball.getX()+2*BALL_RADIUS, ball.getY()+2*BALL_RADIUS);
		} 
		return null;
	}	
	
	// solving bugs that the ball might fluctuate "inside" the paddle;
	private boolean belowThePaddleOrNot(GOval ball) {
		double BallYPosition = ball.getY() + 2*BALL_RADIUS;
		if (BallYPosition > (getHeight()-PADDLE_Y_OFFSET-PADDLE_HEIGHT)) {
			return true;
		} else {
			return false;
		}
	}
	
	private void failure() {
		GLabel fail = new GLabel ("You fail!");
		fail.setFont("SansSerif-bold-36");
		fail.setColor(Color.RED);
		add(fail,getWidth()/2-fail.getWidth()/2,getHeight()/2);
	}
	
	private void win() {
		GLabel win = new GLabel ("You win!");
		win.setFont("SansSerif-bold-36");
		win.setColor(Color.RED);
		add(win,getWidth()/2-win.getWidth()/2,getHeight()/2);
	}
	
	private void countForTurns() {  // give the player 3 chances to win the game;
		turns--;
		remove(ball);
		if (turns>0) {
			createBall();
			startGame();
		} else {
			failure();
		}
	}
	
	private void startGame() {
		setVelocity();  
		waitForClick(); 
			while (true) {
				if (ball.getY() <= getHeight()) {
					reactToCollision();
				} else {
					countForTurns();
					}
				if (countBricks == 0) {
					remove(ball);
					win();
					break;
				}
			}
	}
	
}


