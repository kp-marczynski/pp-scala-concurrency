package zad4;

import java.util.Random;
import java.util.concurrent.Semaphore;

/**
 * Simple class representing one Philosopher in philosophers dining problem.
 */

public class Philosopher extends Thread {
	private static int event = 0;						// counting events during feast
	public static Semaphore doorman;
	
	// Philosopher private data
	private int ID;						// Unique philosopher ID
	private Semaphore chopstickLeft;    // Own chopstick
	private Semaphore chopstickRight;	// Nieghbour's chopstick
	private int meals = 10;          	// Max meals to eat during banquet
	
	public Philosopher(int ID, Semaphore chopstickLeft, Semaphore chopstickRight) {
		this.ID = ID;
		this.chopstickLeft = chopstickLeft;
		this.chopstickRight = chopstickRight;
	}
	
	/**
	 * Informs about current task executing by philosopher
	 * 
	 * @param str - description of performing action
	 */
	private void updateStatus(String str) {
		System.out.printf("Event no:%3d | Philosopher of ID:%d %s\n", ++event, ID, str);
	}
	
	/**
	 * Pause - waits a bit (random fraction up to one second)
	 */
	private void pause() {
		try {
			sleep(new Random().nextInt(1000));
		}
		catch (InterruptedException ignored) {}
	}
	
	/**
	 * Tell philosopher to meditate - he waits a bit
	 */
	private void meditate() {
		updateStatus("is thinking");
		pause();
	}
	
	/**
	 * Tell philosopher to eat. Tries to acquire resources (chopsticks) Possible
	 */
	private void tryToEat() {
		if (isAllowedToEat()) {
			updateStatus("is hungry and is trying to pick up his chopsticks");
			try {
				// Semaphore - waits on his own chopstick if necessary
				chopstickLeft.acquire();
				
				// He's picked up his own chopstick, now try and grab his neighbor's chopstick
				if (!chopstickRight.tryAcquire()) { // Unsuccessful, guess he's fasting at the moment
					updateStatus(" was not able to get neighbor's chopstick");
				}
				else { 	// Success! begins to eat
					updateStatus("picked up his chopsticks and is eating meal no:" + (10 - --meals));
					pause();
					
					// Now put down the chopsticks
					updateStatus("puts down his chopsticks");
				}
			}
			catch (InterruptedException e) {
				updateStatus("was interrupted while waiting for his chopstick");
			}
			finally { // always puts his own chopstick back down
				chopstickLeft.release();
				chopstickRight.release();
				doorman.release();
			}
		}
	}
	
	private boolean isAllowedToEat() {
		if (doorman.tryAcquire()) {
			updateStatus("has just gone into dinner room");
			return true;
		}
		else {
			updateStatus("can't go into dinner room");
			return false;
		}
	}
	
	/**
	 * Philosophize between all meals are consumed
	 */
	@Override public void run() {
		while (meals > 0) {
			meditate();
			tryToEat();
		}
	}
}