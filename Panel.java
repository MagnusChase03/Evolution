package evolution;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.util.ArrayList;
import javax.swing.JPanel;

public class Panel extends JPanel implements Runnable {

	private ArrayList<Runner> runners;
	private ArrayList<Runner> deadRunners;
	private ArrayList<Barrier> bars;
	private int counter;
	private int gen;
	private int delay;
	private int bestFit;
	
	Panel() {
		
		gen = 1;
		delay = 100;
		bestFit = 0;
		runners = new ArrayList<>();
		for (int i = 0; i < 20; i++) {
			
			runners.add(new Runner());
			runners.get(i).setX(100 + i * 2);
			
		}
		
		deadRunners = new ArrayList<>();
		bars = new ArrayList<>();
		bars.add(new Barrier());
		
	}
	
	private void crossOver() {
		
		Runner[] newGen = new Runner[20];
		
		for (int i = 0; i < 6; i++) {
			
			Runner best = deadRunners.get(0);
			for (Runner r : deadRunners) {
				
				if (r.getFitness() > best.getFitness())
					best = r;
				
			}
			
			newGen[i] = best;
			newGen[i].setFitness(0);
			deadRunners.remove(best);
			
		}
		
		for (int i = 6; i < 20; i++) {
			
			Runner a = newGen[(int) (Math.random() * 4)];
			Runner b = newGen[(int) (Math.random() * 4)];
			
			int randPoint = (int) (Math.random() * 6 + 1);
			Runner child = null;
			
			switch (randPoint) {
			
				case 1:
					child = new Runner(a.getInputWeights(), b.getHiddenLayerWeights(), b.getHiddenLayerBias(), a.getOutputBias());
					break;
					
				case 2:
					child = new Runner(b.getInputWeights(), a.getHiddenLayerWeights(), a.getHiddenLayerBias(), b.getOutputBias());
					break;
					
				case 3:
					child = new Runner(a.getInputWeights(), b.getHiddenLayerWeights(), a.getHiddenLayerBias(), b.getOutputBias());
					break;
					
				case 4:
					child = new Runner(b.getInputWeights(), a.getHiddenLayerWeights(), b.getHiddenLayerBias(), a.getOutputBias());
					break;
					
				case 5:
					child = new Runner(a.getInputWeights(), a.getHiddenLayerWeights(), b.getHiddenLayerBias(), b.getOutputBias());
					break;
					
				case 6:
					child = new Runner(b.getInputWeights(), b.getHiddenLayerWeights(), a.getHiddenLayerBias(), a.getOutputBias());
			
			
			}
			
			newGen[i] = child;
			
		}
		
		for (Runner r : newGen) {
			
			for (int y = 0; y < r.getInputWeights().length; y++) {
				
				for (int x = 0; x < r.getInputWeights()[y].length; x++) {
					
					if (Math.random() < 0.05)
						r.getInputWeights()[y][x] = Math.random() * 3 - 1;
					
				}
				
			}
			
			for (int y = 0; y < r.getHiddenLayerWeights().length; y++) {
				
				for (int x = 0; x < r.getHiddenLayerWeights()[y].length; x++) {
					
					if (Math.random() < 0.05)
						r.getHiddenLayerWeights()[y][x] = Math.random() * 3 - 1;
					
				}
				
			}
			
			for (int y = 0; y < r.getHiddenLayerBias().length; y++) {
				
				if (Math.random() < 0.05)
					r.getHiddenLayerBias()[y] = Math.random() * 3 - 1;
				
			}
			
			if (Math.random() < 0.05)
				r.setOutputBias(Math.random() * 3 - 1);
			
		}
		
		bars = new ArrayList<>();
		bars.add(new Barrier());
		counter = 0;
		
		deadRunners = new ArrayList<>();
		for (int i = 0; i < newGen.length; i++) {
			
			if (newGen[i] != null) {
			
				newGen[i].setX(100 + i * 2);
				runners.add(newGen[i]);
			
			}
			
		}
		
		gen++;
	}
	
	private int bestFitness() {
			
		Runner best = null;
		for (Runner r : runners) {
			
			if (r.getFitness() >= bestFit)
				best = r;
			
		}
		
		if (best == null)
			return bestFit;

		bestFit = best.getFitness();
		return best.getFitness();
		
	}
	
	private void update() {
		
		Barrier rem = null;
		for (Barrier b : bars) {
			
			b.update();
			if (b.getX() <= 0)
				rem = b;
			
		}
		
		if (rem != null)
			bars.remove(rem);
		
		ArrayList<Runner> remo = new ArrayList<>();
		for (Runner r : runners) {
			
			if (r.isCanJump() && r.decide(bars.get(0).getX() - (r.getX() + 20), (r.getY() + 20) - bars.get(0).getY()) > 0.5) {
			
				r.setMomentum(11.0);
				r.setJumping(true);
				r.setCanJump(false);
				
			}
			
			if(r.isJumping()) {
				

				if(r.getY() >= 270) {
					
					r.setY(270);
					r.setJumping(false);
					r.setCanJump(true);
					
				}
				
				r.setY(r.getY() - (int) r.getMomentum());
				
				double a = 0.5;
				r.setMomentum(r.getMomentum() - a);
				
			}
			
			r.update();
			
			if (r.collide(bars)) {
			
				deadRunners.add(r);
				remo.add(r);
				
			}
			
		}
		
		for (Runner r: remo)
			runners.remove(r);
		
		for (Runner r : runners)
			r.setFitness(r.getFitness() + 1);
		
		if (runners.size() == 0)
			crossOver();
		
	}
	
	public void run() {
		
		counter = 0;
		while (true) {
			
			try {
				
				counter++;
				if (counter >= delay) {
				
					bars.add(new Barrier());
					counter = 0;
					delay = (int) (Math.random() * 75 + 75);
					
				}
				
				Thread.sleep(1000/1000);
				update();
				repaint();
				
			} catch (InterruptedException e) {}
			
		}
		
	}
	
	public void paint(Graphics g) {
		
		g.clearRect(0, 0, 1024, 300);
		
		g.setColor(Color.black);
		g.setFont(new Font("monospace", Font.BOLD, 36));
		g.drawString("" + bestFitness() + " - " + gen, 500, 50);
		
		for (Runner r : runners) {
			
			g.setColor(r.getColor());
			g.fillRect(r.getX(), r.getY(), 20, 20);
			
		}
		
		for (Barrier b : bars) {
			
			g.setColor(Color.BLACK);
			g.fillRect(b.getX(), b.getY(), 20, b.getHeight());
			
		}
		
	}
}
