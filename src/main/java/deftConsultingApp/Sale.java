import java.time.LocalDate;
import java.util.ArrayList;

/**
 * @author Sebastian Gutierrez
 *
 */


public class Sale {

	private ArrayList<Item> customerCart = new ArrayList<Item>();
	private double totalPrice;
	private double totalTax;
	private LocalDate date;
	private double change;
	private double save;
	private boolean rewardsCustomer;

	
	//constructor
	public Sale(double totalPrice, double totalTax, LocalDate date, double change, double save,
			boolean rewardsCustomer) {
		super();
		this.totalPrice = totalPrice;
		this.totalTax = totalTax;
		this.date = date;
		this.change = change;
		this.save = save;
		this.rewardsCustomer = rewardsCustomer;
	}

	
	//sum items sold
	public int totalItemsSold() {
		return customerCart.stream().mapToInt(i -> i.getQuantity()).sum();
	}
	
	

	//calculate total price based on customer status
	public double getTotalPrice() {
		totalPrice = 0;
		if (rewardsCustomer == false) {
			for (Item i : customerCart) {
				totalPrice = totalPrice + (i.getQuantity() * i.getRegularPrice());
			}

			save = 0;
		}

		else {
			double regular = 0.0;
			;
			for (Item i : customerCart) {
				totalPrice = totalPrice + (i.getQuantity() * i.getRewardsPrice());
				regular = regular + (i.getQuantity() * i.getRegularPrice());
			}
			save = regular - totalPrice;
		}
		return totalPrice;
	}

	
	
	//calculate total tax based on taxabel items AND customer status
	public double getTotalTax(double tax) {
		for (Item i : customerCart) {
			if (!i.isTaxExempt() && isRewardsCustomer()) {
				totalTax = totalTax + (((i.getQuantity() * i.getRewardsPrice()) * tax) / 100);
			}

			if (!i.isTaxExempt() && !isRewardsCustomer()) {
				totalTax = totalTax + (((i.getQuantity() * i.getRegularPrice()) * tax) / 100);
			}
		}

		return totalTax;
	}

	
	
	//setters and getters
	public ArrayList<Item> getCustomerCart() {
		return customerCart;
	}
	public LocalDate getDate() {
		return date;
	}

	public void setDate(LocalDate date) {
		this.date = date;
	}

	public double getChange() {
		return change;
	}

	public void setChange(double change) {
		this.change = change;
	}

	public double getSave() {
		return save;
	}

	public void setSave(double save) {
		this.save = save;
	}

	public boolean isRewardsCustomer() {
		return rewardsCustomer;
	}

	public void setRewardsCustomer(boolean rewardsCustomer) {
		this.rewardsCustomer = rewardsCustomer;
	}

	

}
