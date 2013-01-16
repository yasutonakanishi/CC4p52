package simulation.p5;

import processing.core.PApplet;

public class SimpleGridPApplet extends PApplet {
	DrawMode0 d0 = new DrawMode0();
	DrawMode1 d1 = new DrawMode1();
	DrawMode2 d2 = new DrawMode2();
	int counter;
	int sceneLength = 12;

	public void setup() {
		size(400, 300);
		frameRate(30);
		smooth();
		d0.setup();
		d1.setup();
		d2.setup();
	}

	public void draw() {
		if (counter < 30 * sceneLength) {
			d0.draw();
		} 
		else if (counter < 30 * sceneLength * 2) {
			d1.draw();
		}
		else if (counter < 30 * sceneLength * 3) {
			d2.draw();
		}

		counter++;
		if (counter > 30 * sceneLength * 3) {
			counter = 0;
		}
	}

	class DrawMode0 {
		float locY = 0f;
		int gridHeight = 8;
		float speedY = 0.5f;

		public void setup() {
		}

		public void draw() {
			noFill();
			stroke(0, 255, 0);
			background(1);
			locY += speedY;
			for (int i = 0; i < height; i += gridHeight) {
				line(0, i + locY, width, i + locY);
			}
			if (locY > gridHeight) {
				locY = speedY;
			}
		}
	}

	class DrawMode1 {
		int NUM = 800;
		float[] locX = new float[NUM];
		float[] locY = new float[NUM];
		float[] vecX = new float[NUM];
		float[] vecY = new float[NUM];

		public void setup() {
			for (int i = 0; i < NUM; i++) {
				locX[i] = random(0, width);
				locY[i] = random(0, height);
				vecX[i] = random(-1, 1);
				vecY[i] = random(-1, 1);
			}
		}

		public void draw() {
			noFill();
			stroke(0, 255, 0);
			background(1);
			for (int i = 0; i < NUM; i++) {
				locX[i] += vecX[i];
				locY[i] += vecY[i];
				if (locX[i] < 0 || locX[i] > width) {
					vecX[i] *= -1;
				}
				if (locY[i] < 0 || locY[i] > height) {
					vecY[i] *= -1;
				}
				ellipse(locX[i], locY[i], 5, 5);
			}
		}
	}

	class DrawMode2 {
		float[] phase = new float[8];
		float[] phaseSpeed = new float[8];

		public void setup() {
			for(int i = 0; i < phase.length; i++){
				phase[i] = random(0, 1);
				phaseSpeed[i] = random(0.2f, 0.4f);
			}
		}

		public void draw() {
			noStroke();
			fill(1, 200, 1);

			background(1);
			
			for(int i = 0; i < 8; i++){
				if (sin(phase[i]) > 0) {
					rect(width/8.0f*i, 0, width/8f, height);
				}
				phase[i] += phaseSpeed[i];
			}
		}
	}
}

