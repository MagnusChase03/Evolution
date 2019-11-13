package evolution;

import java.awt.Color;
import java.util.ArrayList;

public class Runner {
	
	private int x;
	private int y;
	private Color color;
	private boolean canJump = true;
	private int fitness;
	
	private double[][] inputWeights;
	private double[][] hiddenLayerWeights;
	private double[] hiddenLayerBias;
	private double outputBias;
	
	private double momentum = 3.0;
	private boolean isJumping = false;
	
	Runner() {
		
		x = 100;
		y = 270;
		color = new Color((int) (Math.random() * 255),(int) (Math.random() * 255),(int) (Math.random() * 255));
		fitness = 0;
		
		inputWeights = new double[2][3];
		hiddenLayerWeights = new double[3][1];
		hiddenLayerBias = new double[3];
		
		for (int y = 0; y < inputWeights.length; y++) {
			
			for (int x = 0; x < inputWeights[y].length; x++) {
				
				inputWeights[y][x] = Math.random() * 3 - 1;
				
			}
			
		}
		
		for (int y = 0; y < hiddenLayerWeights.length; y++) {
			
			for (int x = 0; x < hiddenLayerWeights[y].length; x++) {
				
				hiddenLayerWeights[y][x] = Math.random() * 3 - 1;
				
			}
			
		}
		
		for (int y = 0; y < hiddenLayerBias.length; y++) {
			
			hiddenLayerBias[y] = Math.random() * 3 - 1;
			
		}
		
		outputBias = Math.random() * 3 - 1;
		
	}
	
	Runner(double[][] iw, double[][] hw, double[] hb, double ob) {
		
		x = 100;
		y = 270;
		color = new Color((int) (Math.random() * 255),(int) (Math.random() * 255),(int) (Math.random() * 255));
		fitness = 0;
		
		inputWeights = iw;
		hiddenLayerWeights = hw;
		hiddenLayerBias = hb;
		outputBias = ob;
		
	}
	
	private double sigmoid(double x) {
		
		return 1 / (1 + Math.exp(-x));
		
	}
	
	double decide(int diffX, int diffY) {
		
		double[] hiddenLayer = new double[3];
		for (int y = 0; y < inputWeights[0].length; y++) {
			
			hiddenLayer[y] = (inputWeights[0][y] * diffX) + (inputWeights[1][y] * diffY) + hiddenLayerBias[y];
			hiddenLayer[y] = sigmoid(hiddenLayer[y]);
			
		}
		
		double output = 0.0;
		for (int y = 0; y < hiddenLayerWeights[0].length; y++) {
			
			output += hiddenLayer[y] * hiddenLayerWeights[y][0];
			
		}
		
		output += outputBias;
		output = sigmoid(output);
		
		return output;
		
	}
	
	boolean collide(ArrayList<Barrier> bars) {
		
		for (Barrier b : bars) {
			
			if (x < b.getX() && x + 20 > b.getX() && (y > 260 && y < 280 || y < b.getY() && y + 20 > b.getY() ||
					y > b.getY() && y < b.getY() + 20)) {
				
				return true;
				
			}
			
		}
		return false;
		
	}
	
	void update() {
		
		if (y < 270)
			y += 2;
		else
			canJump = true;
		
	}

	int getX() {
		return x;
	}

	void setX(int x) {
		this.x = x;
	}

	int getY() {
		return y;
	}

	void setY(int y) {
		this.y = y;
	}

	double[][] getInputWeights() {
		return inputWeights;
	}

	void setInputWeights(double[][] inputWeights) {
		this.inputWeights = inputWeights;
	}

	double[][] getHiddenLayerWeights() {
		return hiddenLayerWeights;
	}

	void setHiddenLayerWeights(double[][] hiddenLayerWeights) {
		this.hiddenLayerWeights = hiddenLayerWeights;
	}

	double[] getHiddenLayerBias() {
		return hiddenLayerBias;
	}

	void setHiddenLayerBias(double[] hiddenLayerBias) {
		this.hiddenLayerBias = hiddenLayerBias;
	}

	double getOutputBias() {
		return outputBias;
	}

	void setOutputBias(double outputBias) {
		this.outputBias = outputBias;
	}

	boolean isCanJump() {
		return canJump;
	}

	void setCanJump(boolean canJump) {
		this.canJump = canJump;
	}

	Color getColor() {
		return color;
	}

	void setColor(Color color) {
		this.color = color;
	}

	int getFitness() {
		return fitness;
	}

	void setFitness(int fitness) {
		this.fitness = fitness;
	}

	public double getMomentum() {
		return momentum;
	}

	public void setMomentum(double momentum) {
		if(momentum < -7) {
			
			momentum = -7.0;
			
		}
		this.momentum = momentum;
	}

	public boolean isJumping() {
		return isJumping;
	}

	public void setJumping(boolean isJumping) {
		this.isJumping = isJumping;
	}

	
}
