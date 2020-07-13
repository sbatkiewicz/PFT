import java.awt.EventQueue;

public class Main {
		/*
		 *  Create the Login frame and original database reference
		 */
	public static void main(String[] args) {
		

		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					Database db = new Database();
					LoginFrame window = new LoginFrame(db);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
		
	}

}
