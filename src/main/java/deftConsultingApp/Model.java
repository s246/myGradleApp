import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.time.LocalDate;
import java.util.List;

/**
 * @author Sebastian Gutierrez
 *
 */

public class Model {

	public static int Transaction = 1;

	public static Sale currentSale = null;

	public static boolean inventoryLoaded = false;

	public static boolean isActualCustomerRewards = false;

	public static void readInventory(String inventoryFile) {

		try {
			
			System.out.println("Working Directory = " + System.getProperty("user.dir"));
			Inventory.inventory.clear();
			readTransactionCount("transactionCount.txt");

			// list of line contents of txt
			List<String> content;

			// read all csv rows
			content = Files.readAllLines(Paths.get(inventoryFile));


			// array to split row conents
			String[] line_contents;

			// For each line in csv file
			for (int lineNum = 0; lineNum < content.size(); lineNum++) {

				// get line
				String line = content.get(lineNum);
				// split ","
				line_contents = line.split(":");

				String itemName = line_contents[0];

				String[] itemData = line_contents[1].split(",");

				int stock = Integer.valueOf(itemData[0].substring(1));
				double regularPrice = Double.valueOf(itemData[1].substring(2));
				double rewardsPrice = Double.valueOf(itemData[2].substring(2));
				boolean taxExempt = Inventory.taxDict.get(itemData[3].substring(1));

				Item item = new Item(itemName, stock, regularPrice, rewardsPrice, taxExempt);

				Inventory.addToInventory(item);

			}

			inventoryLoaded = true;

		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	
	
	public static void readTransactionCount(String transactionFile) {
		try {

			// list of line contents of txt
			List<String> content;

			// read all csv rows
			content = Files.readAllLines(Paths.get(transactionFile));

			Transaction=Integer.valueOf(content.get(0));

		} catch (Exception e) {
			Transaction=1;
			e.printStackTrace();
		}
	}
	
	public static void writeTransactionCount() {
		try {
			Transaction++;
			File file = new File("transactionCount.txt");

			file.createNewFile();
			FileWriter fw = new FileWriter(file.getAbsoluteFile());
			BufferedWriter bw = new BufferedWriter(fw);

			bw.write(String.format("%05d", Transaction));
			bw.close();
		
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	
	
	
	public static void newSale(boolean rewardsCustomer) {
		currentSale = new Sale(0, 0, LocalDate.now(), 0, 0, rewardsCustomer);
	}

	public static void writeReceipt(String content) {

		try {
			File file = new File("transaction_"+String.format("%05d", Transaction)+"_"+currentSale.getDate().toString().replace("-", ""));
			// if file doesnt exists, then create it

			file.createNewFile();
			FileWriter fw = new FileWriter(file.getAbsoluteFile());
			BufferedWriter bw = new BufferedWriter(fw);

			bw.write(content);
			bw.close();

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
	
	
	
	public static void writeInventory() {

		try {
			File file = new File("Inventory.txt");
			// if file doesnt exists, then create it

			file.createNewFile();
			FileWriter fw = new FileWriter(file.getAbsoluteFile());
			BufferedWriter bw = new BufferedWriter(fw);
			NumberFormat formatter = new DecimalFormat("#0.00");     
			
			for(Item i: Inventory.inventory) {
				String line=i.getName()+": "+i.getStock()+", $"+formatter.format(i.getRegularPrice())+", $"+formatter.format(i.getRewardsPrice())+", "+Inventory.taxDict2.get(i.isTaxExempt())+"\n";
				bw.write(line);
			}

			
			bw.close();

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

}
