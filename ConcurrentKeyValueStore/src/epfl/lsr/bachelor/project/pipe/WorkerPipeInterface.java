package epfl.lsr.bachelor.project.pipe;


/**
 * This interface must be implemented by our worker that access the key value
 * store
 * 
 * @author Gregory Maitre & Patrick Andrade
 * 
 */
public interface WorkerPipeInterface {
	/**
	 * Start the {@link Thread} that perform the request
	 */
	void start();

	/**
	 * Stop the {@link Thread} that perform the request
	 */
	void close();
}
