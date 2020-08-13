
import java.io.IOException;
import java.util.ArrayList;
import javafx.beans.binding.Bindings;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Modality;
import javafx.stage.Stage;

/**
 * @author Sebastian Gutierrez
 *
 */

public class BuyMenuController {

	// Only necesarry UI elements declared
	@FXML
	private TableView<Item> tablaPrincipal;

	@FXML
	private TableColumn<Item, String> nameColumn;

	@FXML
	private TableColumn<Item, String> stockColumn;

	@FXML
	private TableColumn<Item, String> priceColumn;

	@FXML
	private TableColumn<Item, Long> inCartColumn;

	@FXML
	private Button checkOutBtn;

	@FXML
	private Button cancelSaleBtn;

	@FXML
	private Button addToCartBtn;

	@FXML
	private Button removeFromCartBtn;

	
	//boolean to indicate cancelation of transaction
	public static boolean keepBuying = true;

	@FXML
	public void initialize() {
		
		// assign table columns to item properties
		nameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
		stockColumn.setCellValueFactory(new PropertyValueFactory<>("stock"));
		
		
		//assing price based on customer status
		if(Model.isActualCustomerRewards)
			priceColumn.setCellValueFactory(new PropertyValueFactory<>("rewardsPrice"));
		
		else {
			priceColumn.setCellValueFactory(new PropertyValueFactory<>("regularPrice"));
		}
		
		inCartColumn.setCellValueFactory(new PropertyValueFactory<>("quantity"));
		
		
		//disable buttons if no selection is made
		removeFromCartBtn.disableProperty().bind(Bindings.isEmpty(tablaPrincipal.getSelectionModel().getSelectedItems()));
		addToCartBtn.disableProperty().bind(Bindings.isEmpty(tablaPrincipal.getSelectionModel().getSelectedItems()));

		tablaPrincipal.setItems(createObservableList(Inventory.inventory));
		tablaPrincipal.refresh();

	}

	
	
	//add item to customer cart
	@FXML
	public void addToCart(ActionEvent event) {
		
		//get current sale and selected item
		Sale sale = Model.currentSale;
		Item selected = tablaPrincipal.getSelectionModel().getSelectedItem();

		//check stock
		if (selected.getStock() < 1) {
			Alert alerta = new Alert(AlertType.WARNING);
			alerta.setHeaderText("OUT OF STOCK: " + tablaPrincipal.getSelectionModel().getSelectedItem().getName());
			alerta.showAndWait();
		}

		//add new item to cart or increment quantity if already present in cart
		else {
			if (!sale.getCustomerCart().contains(selected)) {
				Item newItem = new Item(selected);
				newItem.setQuantity(1);
				sale.getCustomerCart().add(newItem);
			}

			else {
				int index = sale.getCustomerCart().indexOf(selected);
				Item inCart = sale.getCustomerCart().get(index);
				inCart.setQuantity(inCart.getQuantity() + 1);
			}

			//updated stock and cart representations i table
			selected.setStock(selected.getStock() - 1);
			selected.setQuantity(selected.getQuantity() + 1);
			tablaPrincipal.refresh();
			//System.out.println(sale.getCustomerCart());
		}

	}

	
	//remove from cart
	@FXML
	public void removeFromCart(ActionEvent event) throws IOException {
		//get current sale and item selected
		Sale sale = Model.currentSale;
		Item selected = tablaPrincipal.getSelectionModel().getSelectedItem();

		//check if present in cart
		if (selected.getQuantity() < 1) {
			Alert alerta = new Alert(AlertType.WARNING);
			alerta.setHeaderText("No " + tablaPrincipal.getSelectionModel().getSelectedItem().getName() + " in cart");
			alerta.showAndWait();
		}

		else {
			//remove item if quantity ==0
			if(selected.getQuantity()==1) {
				sale.getCustomerCart().remove(selected);
			}

			//reduce quantity in cart
			else {
			int index = sale.getCustomerCart().indexOf(selected);
			Item inCart = sale.getCustomerCart().get(index);
			inCart.setQuantity(inCart.getQuantity() - 1);
			}

			//update stock and cart represntations
			selected.setStock(selected.getStock() + 1);
			selected.setQuantity(selected.getQuantity() - 1);
		}

		tablaPrincipal.refresh();

		//System.out.println(sale.getCustomerCart());
	}

	@FXML
	public void checkOut(ActionEvent event) throws IOException {

		// show checkout window and freeze table menu
		Parent root = FXMLLoader.load(getClass().getResource("CheckOut.fxml"));
		Scene escena = new Scene(root);
		Stage checkOut = new Stage();
		checkOut.setTitle("DeftConsulting");
		checkOut.setScene(escena);
		checkOut.initModality(Modality.APPLICATION_MODAL);
		checkOut.showAndWait();

		//return form checkout if checkout confirmed
		if (!keepBuying) {
			//clear in memory inventory and read from file updated
			Inventory.inventory.clear();
			Model.readInventory("Inventory.txt");
			Stage window = (Stage) checkOutBtn.getScene().getWindow();
			try {
				root = FXMLLoader.load(getClass().getResource("home.fxml"));
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			Scene scene = new Scene(root);
			window.setScene(scene);
			window.show();
		}
	}


	@FXML
	public void cancelSale(ActionEvent event) {
		//read inventory again and return to home
		Inventory.inventory.clear();
		Model.readInventory("Inventory.txt");
		Stage window = (Stage) checkOutBtn.getScene().getWindow();
		try {
			Parent root = FXMLLoader.load(getClass().getResource("home.fxml"));
			Scene scene = new Scene(root);
			window.setScene(scene);
			window.show();
		} catch (IOException e) {
			e.printStackTrace();
		}
	
	}
	// funcion de cambio a observable list
	public static ObservableList<Item> createObservableList(ArrayList<Item> inventory) {
		ObservableList<Item> listaObservable = FXCollections.observableArrayList();
		if (!inventory.isEmpty()) {
			for (Item temp : inventory) {
				listaObservable.add(temp);
			}
		}
		return listaObservable;
	}

}
