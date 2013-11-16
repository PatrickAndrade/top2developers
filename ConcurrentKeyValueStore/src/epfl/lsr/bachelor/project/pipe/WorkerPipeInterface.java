package epfl.lsr.bachelor.project.pipe;

/**
 * This interface must be implemented by our worker that access the key value
 * store
 * 
 * @author Gregory Maitre & Patrick Andrade
 * 
 */
public interface WorkerPipeInterface {
	void start();

	void close();

}
