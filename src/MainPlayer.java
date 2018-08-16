

import javafx.application.Application;
import javafx.stage.*;

public class MainPlayer extends Application {

	public static void main(String[] args) {
		Application.launch(args);
	}

	@Override
	public void start(Stage arg0) throws Exception {
		QueueController controller = new QueueController();
	}
}