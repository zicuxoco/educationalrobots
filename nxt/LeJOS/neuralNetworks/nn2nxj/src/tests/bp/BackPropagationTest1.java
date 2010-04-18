package tests.bp;

import neural.feedforward.FeedforwardLayer;
import neural.feedforward.FeedforwardNetwork;
import neural.feedforward.train.Train;
import neural.feedforward.train.backpropagation.Backpropagation;


public class BackPropagationTest1 {

	public static double Sensors_INPUT[][] = { {0,0,0}, {1,0,0},{0,0,1}, {0,1,0} };
	public static double Actuators_IDEAL[][] = { {1,1}, {1,0}, {0,1}, {0,0} };

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		final FeedforwardNetwork network = new FeedforwardNetwork();
		network.addLayer(new FeedforwardLayer(3));
		network.addLayer(new FeedforwardLayer(3));
		network.addLayer(new FeedforwardLayer(2));
		network.reset();

		// train the neural network
		final Train train = new Backpropagation(network, Sensors_INPUT, Actuators_IDEAL, 0.7, 0.9);

		int epoch = 1; 

		do {
			train.iteration();
			System.out
					.println("Epoch #" + epoch + " Error:" + train.getError());
			epoch++;
		} while ((epoch < 5000) && (train.getError() > 0.001));
		
		// test the neural network
		System.out.println("Neural Network Results:");
		for (int i = 0; i < Sensors_INPUT.length; i++) {
			final double actual[] = network.computeOutputs(Sensors_INPUT[i]);
			System.out.println(Sensors_INPUT[i][0] + "," + Sensors_INPUT[i][1] + "," + Sensors_INPUT[i][2]
					+ ", actual=" + actual[0] + ",ideal=" + Actuators_IDEAL[i][0] + "," +  Actuators_IDEAL[i][1]);
		}
		
	}

}
