package evolution;

import java.awt.Color;
import java.awt.Graphics;
import java.util.ArrayList;
import javax.swing.JPanel;

public class Panel extends JPanel implements Runnable {

	private ArrayList<Runner> runners;
	private ArrayList<Runner> deadRunners;
	private ArrayList<Barrier> bars;
	private int counter;
	
	Panel() {
		
		runners = new ArrayList<>();
		for (int i = 0; i < 10; i++) {
			
			runners.add(new Runner());
			runners.get(i).setX(100 + i * 5);
			
		}
		
		deadRunners = new ArrayList<>();
		bars = new ArrayList<>();
		bars.add(new Barrier());
		
	}
	
	private void crossOver() {
		
		Runner[] newGen = new Runner[10];
		
		for (int i = 0; i < 4; i++) {
			
			Runner best = deadRunners.get(0);
			for (Runner r : deadRunners) {
				
				if (r.getFitness() > best.getFitness())
					best = r;
				
			}
			
			newGen[i] = best;
			deadRunners.remove(best);
			
		}
		
		for (int i = 4; i < 10; i++) {
			
			Runner a = newGen[(int) (Math.random() * 4)];
			Runner b = newGen[(int) (Math.random() * 4)];
			Runner child = new Runner(a.getInputWeights(), b.getHiddenLayerWeights(), b.getHiddenLayerBias(), a.getOutputBias());
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
			
				newGen[i].setX(100 + i * 5);
				runners.add(newGen[i]);
			
			}
			
		}
		
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
			
				r.setY(r.getY() - 100);
				r.setCanJump(false);
				
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
				if (counter >= 100) {
				
					bars.add(new Barrier());
					counter = 0;
					
				}
				
				Thread.sleep(1000/60);
				update();
				repaint();
				
			} catch (InterruptedException e) {}
			
		}
		
	}
	
	public void paint(Graphics g) {
		
		g.clearRect(0, 0, 1024, 300);
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
