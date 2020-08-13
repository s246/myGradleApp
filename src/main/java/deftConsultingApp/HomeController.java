
import java.io.IOException;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Stage;

/**
 * @author Sebastian Gutierrez
 *
 */


public class HomeController {

	// UI elements
	@FXML
	private Button reward_memb_btn;

	@FXML
	private Button regular_memb_btn;

	@FXML
	private Button import_invtry_btn;

	@FXML
	void importInventory(ActionEvent event) {

		Model.readInventory("Inventory.txt");

		// System.out.println(Inventory.inventory);

		regular_memb_btn.setDisable(false);
		reward_memb_btn.setDisable(false);
		import_invtry_btn.setDisable(true);

	}

	@FXML
	void regularMemberSelected(ActionEvent event) {
		// set customer status and create sale
		Model.isActualCustomerRewards = false;
		Model.newSale(Model.isActualCustomerRewards);
		Stage window = (Stage) regular_memb_btn.getScene().getWindow();
		Parent root = null;
		try {
			root = FXMLLoader.load(getClass().getResource("BuyMenu.fxml"));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		Scene scene = new Scene(root);
		window.setScene(scene);
		window.show();

	}

	@FXML
	void rewardsMemberSelected(ActionEvent event) {
		// set customer status and create sale
		Model.isActualCustomerRewards = true;
		Model.newSale(Model.isActualCustomerRewards);
		Stage window = (Stage) regular_memb_btn.getScene().getWindow();
		Parent root = null;
		try {
			root = FXMLLoader.load(getClass().getResource("BuyMenu.fxml"));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		Scene scene = new Scene(root);
		window.setScene(scene);
		window.show();
	}

	@FXML
	void initialize() {
		if (!Model.inventoryLoaded) {
			regular_memb_btn.setDisable(true);
			reward_memb_btn.setDisable(true);
		}

	}

}
