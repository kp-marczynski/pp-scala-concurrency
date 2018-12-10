package zad4short;

import java.util.Random;
import java.util.concurrent.Semaphore;

public class Philosopher extends Thread {
	public static Semaphore doorman;

	private Semaphore chopstickLeft;
	private Semaphore chopstickRight;
	private int meals = 2;
	
	public Philosopher(Semaphore chopstickLeft, Semaphore chopstickRight) {
		this.chopstickLeft = chopstickLeft;
		this.chopstickRight = chopstickRight;
	}

	private void pause() {
		try {
			sleep(new Random().nextInt(1000));
		}
		catch (InterruptedException ignored) {}
	}

	private void meditate() {
		pause();
	}

	private void tryToEat() {
		if (isAllowedToEat()) {
			try {
				// Semaphore - waits on his own chopstick if necessary
				chopstickLeft.acquire();
				
				// He's picked up his own chopstick, now try and grab his neighbor's chopstick
				if (!chopstickRight.tryAcquire()) { // Unsuccessful, guess he's fasting at the moment
					--meals;
				}
				else { 	// Success! begins to eat
					pause();
				}
			}
			catch (InterruptedException ignored) {
			}
			finally { // always puts his own chopstick back down
				chopstickLeft.release();
				chopstickRight.release();
				doorman.release();
			}
		}
	}
	
	private boolean isAllowedToEat() {
		return doorman.tryAcquire();
	}

	@Override
	public void run() {
		while (meals > 0) {
			meditate();
			tryToEat();
		}
	}
}