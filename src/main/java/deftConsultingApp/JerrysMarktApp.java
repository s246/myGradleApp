import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

/**
 * @author Sebastian Gutierrez
 *
 */

public class JerrysMarktApp extends Application {
		//static reference to home
		public static HomeController homeController;

		public static void main(String[] args) {
			Application.launch(args);

		}


		@Override
		public void start(Stage primaryStage) throws Exception {
			// cargar archivo fxml principal
			FXMLLoader loader = new FXMLLoader();
			loader.setLocation(getClass().getResource("home.fxml"));
			Parent root = loader.load();

			// referenciar el controlador  cargado para usar en otros controladores
			homeController = loader.getController();

			// cargar escena y stage
			Scene escena = new Scene(root);
			primaryStage.setTitle("DeftConsulting");
			primaryStage.setScene(escena);
			primaryStage.show();

		}

}
