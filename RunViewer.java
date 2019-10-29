import javax.swing.*;
/**
 * @author Jacob Zaidi
 */
public class RunViewer implements Runnable {
	public void run() {
		@SuppressWarnings("unused")
		Viewer program = new Viewer();
	}
	public static void main(String[] args) {
		SwingUtilities.invokeLater(new RunViewer());
	}
}
