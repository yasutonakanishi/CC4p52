package simulation.p5;

public class PendulumnAndBallSystemPApplet extends PendulumPApplet{
	private static int kAppletSize = 300;
		
	private	Ball	ball;
	private BallSystem ballSystem;	
	
	public void setup(){
		super.setup();		
		ball = new Ball(width/2, 0, 30);
		ballSystem = new BallSystem();
	}
		
	public	void setExternalForce(double m, double vx, double vy, double vz){	
		// m1*v1 + m2*v2 = m1*v1' + m2 * v2'
	}
	
	public void draw(){
		super.draw();
		
		//
		ball.move(width, height);
		ball.draw();
		
		ballSystem.move(width, height);
		ballSystem.draw();
	}
	
	private class BallSystem {
		private	Ball[]	balls;
		
		BallSystem(){
			randomSeed(System.currentTimeMillis());
			balls = new Ball[3];
			for(int i=0; i<balls.length; i++)
				balls[i] = new Ball((int)random(width), (int)random(height), 30);
		}

		void move(int width, int height) {		
			for(int i=0; i<balls.length; i++)
				balls[i].move(width, height);	
			//			
		}
		
		void draw(){
			for(int i=0; i<balls.length; i++)
				balls[i].draw();	
		}
	}
	
	private class Ball{
		int x, y;
		int radius;
		double speedx, speedy;
		
		Ball(int x, int y, int r){
			this.x = x;
			this.y = y;
			this.radius = r;
			this.speedx = this.speedy = 0.0;
		}
		void move(int width, int height){
		    // Add gravity to speed
		    speedx = speedx + gravityS * Math.sin(-appletAngle);
		    speedy = speedy + gravityS * Math.cos(-appletAngle);
		    // Add speed to y location
		    x += speedx;
		    y += speedy;
		    // If square reaches the bottom
		    // Reverse speed
		    if (x < 0 + radius){
		    	speedx = speedx * -0.8f;
		    	x = radius;
		    }
		    if (x > width - radius) {
		    	speedx = speedx * -0.8f;
		    	x = width - radius;
		    }
		    if (y < 0 + radius){
		    	speedy = speedy * -0.8f;
		    	y = radius;
		    }
		    if (y > height - radius) {
		    	// Dampening
		    	speedy = speedy * -0.8f;
		    	y = height - radius;
		    }
		}
		void draw(){	
			fill(200, 70);
			stroke(10, 80);
			ellipse(x,y, radius*2, radius*2);
			fill(90, 90, 90);
			ellipse(x,y, radius, radius);
		}		
	}
}
