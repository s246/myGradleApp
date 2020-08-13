import java.net.URL;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert.AlertType;
import javafx.stage.Stage;

/**
 * @author Sebastian Gutierrez
 *
 */

public class CheckOutController {

	@FXML
	private ResourceBundle resources;

	@FXML
	private URL location;

	@FXML
	private Button checkOutBtn;

	@FXML
	private Button cancelBtn;

	@FXML
	private TextField cashTextField;

	@FXML
	private TextArea checkOutTextArea;
	
	
	
	
	
	@FXML
	void initialize() {

		
		//allow only numbers in cash field
		cashTextField.textProperty().addListener((observable, oldValue, newValue) -> {
			if (!newValue.matches("\\d*")) {
				cashTextField.setText(newValue.replaceAll("[^\\d+\\.]", ""));
			}
		});

		
	
		//visualize receipt (format prints)
		checkOutTextArea.appendText(String.valueOf(Model.currentSale.getDate())+"\n"+"\n");
		checkOutTextArea.appendText("TRANSACTION "+String.format("%05d", Model.Transaction)+"\n"+"\n");
		
		String header=String.format("%-12.12s%-12.12s%-12.12s%-12.12s", "ITEM","QUANTITY","UNIT", "PRICE","TOTAL");
		checkOutTextArea.appendText(header+"\n"+"\n");
		NumberFormat formatter = new DecimalFormat("#0.00");     
	

		if (!Model.currentSale.isRewardsCustomer()) {
			for (Item i : Model.currentSale.getCustomerCart()) {
			    
				String s= String.format("%-12.12s%-12.12s%-12.12s%-12.12s",i.getName(),String.valueOf(formatter.format(i.getQuantity())),formatter.format(i.getRegularPrice())+ "$",formatter.format(i.getQuantity() * i.getRegularPrice())+"$");
				
				checkOutTextArea.appendText(s+"\n"+"\n");
			}

		}

		else {
			for (Item i : Model.currentSale.getCustomerCart()) {
				  
				String s= String.format("%-12.12s%-12.12s%-12.12s%-12.12s",i.getName(),String.valueOf(formatter.format(i.getQuantity())),formatter.format(i.getRewardsPrice())+ "$",formatter.format(i.getQuantity() * i.getRewardsPrice())+"$");
				
				checkOutTextArea.appendText(s+"\n"+"\n");
			}
		}

		
		checkOutTextArea.appendText("************************************"+"\n"+"\n");
		checkOutTextArea.appendText("TOTAL NUMBER OF ITEMS SOLD: "+Model.currentSale.totalItemsSold()+"\n");
		checkOutTextArea.appendText("SUB-TOTAL: " + String.valueOf(formatter.format(Model.currentSale.getTotalPrice())) + "$" + "\n");
		checkOutTextArea.appendText("TAX (6.5%): "+String.valueOf(formatter.format(Model.currentSale.getTotalTax(6.5))) + "$" + "\n");
		checkOutTextArea.appendText("TOTAL: " + String.valueOf(formatter.format(Model.currentSale.getTotalPrice()+Model.currentSale.getTotalTax(6.5))) + "$" + "\n");
		

	}

	
	//do nothing
	@FXML
	void cancel(ActionEvent event) {
		Node nodo = (Node) event.getSource();
		Stage stageActual = (Stage) nodo.getScene().getWindow();
		stageActual.close();
		BuyMenuController.keepBuying=true;
	}

	
	//confirm checkout
	@FXML
	void doCheckOut(ActionEvent event) {
		double cash=0.0;
		Sale currentSale=Model.currentSale;
		
		//check cashfield 
		try {
			cash=Double.valueOf(cashTextField.getText());
		} catch (NumberFormatException e) {
			Alert alerta = new Alert(AlertType.ERROR);
			alerta.setHeaderText("INSERT VALID VALUE IN CASH FIELD");
			alerta.showAndWait();
			return;
		}
		
		//check change
		double change=cash-Model.currentSale.getTotalPrice()+Model.currentSale.getTotalTax(6.5);
		//System.out.println(currentSale.getTotalPrice());
		if(change<0) {
			Alert alerta = new Alert(AlertType.ERROR);
			alerta.setHeaderText("NOT ENOUGH CASH");
			alerta.showAndWait();
			return;
		}
		
		
		//set change and complete receipt
		else {
			currentSale.setChange(change);
			NumberFormat formatter = new DecimalFormat("#0.00");     
			checkOutTextArea.appendText("CASH: " + String.valueOf(formatter.format(cash)) + "$" + "\n");
			checkOutTextArea.appendText("CHANGE: " + String.valueOf(formatter.format(currentSale.getChange())) + "$" + "\n");
			checkOutTextArea.appendText("************************************"+"\n"+"\n");
			checkOutTextArea.appendText("YOU SAVED: " + String.valueOf(formatter.format(Model.currentSale.getSave())) + "$!" + "\n");
			
			//write to disk
			Model.writeReceipt(checkOutTextArea.getText());
			Model.writeTransactionCount();
			Model.writeInventory();
			
			//show confirmation
			Alert alerta = new Alert(AlertType.INFORMATION);
			alerta.setHeaderText("CHANGE: " +String.valueOf(formatter.format(currentSale.getChange())) + "$");
			alerta.setContentText("Receipt Created"+"\n"+"Transaction Count File updated"+"\n"+"Inventory File Updated"+"\n");
			alerta.show();
			
			//update buying state for menu controller
			BuyMenuController.keepBuying=false;
			
			Node nodo = (Node) event.getSource();
			Stage stageActual = (Stage) nodo.getScene().getWindow();
			stageActual.close();
		}
	
	}


}
