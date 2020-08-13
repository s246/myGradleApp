
/**
 * @author Sebastian Gutierrez
 *
 */

public class Item {
	
	private String name;
	private int stock;
	private int quantity=0;
	private double regularPrice;
	private double rewardsPrice;
	private boolean taxExempt;

	
	//cosntructor
	public Item(String name, int stock, double regularPrice, double rewardsPrice, boolean taxExempt) {
		super();
		this.name = name;
		this.stock = stock;
		this.regularPrice = regularPrice;
		this.rewardsPrice = rewardsPrice;
		this.taxExempt = taxExempt;
	}
	
	//util copy constructor
	public Item(Item copy) {
		this.name = copy.getName();
		this.regularPrice = copy.getRegularPrice();
		this.rewardsPrice = copy.getRewardsPrice();
		this.taxExempt = copy.isTaxExempt();
	}




	//override hascode and equals to compare item in lists (contains)
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Item other = (Item) obj;
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		return true;
	}

	
	
	
	
	//to string to visualize in development phase
	@Override
	public String toString() {
		return "Item [name=" + name + ", Stock=" + stock + ", quantity=" + quantity+ ", regularPrice=" + regularPrice + ", rewardsPrice="
				+ rewardsPrice + ", taxExempt=" + taxExempt + "]";
	}

	
	
	//getters and setters
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getStock() {
		return stock;
	}

	public void setStock(int quantity) {
		this.stock = quantity;
	}

	public boolean isTaxExempt() {
		return taxExempt;
	}

	public void setTaxExempt(boolean taxExempt) {
		this.taxExempt = taxExempt;
	}

	public double getRegularPrice() {
		return regularPrice;
	}

	public void setRegularPrice(double regularPrice) {
		this.regularPrice = regularPrice;
	}

	public double getRewardsPrice() {
		return rewardsPrice;
	}

	public void setRewardsPrice(double rewardsPrice) {
		this.rewardsPrice = rewardsPrice;
	}


	
	public void updateStock(int add) {
		this.stock=this.stock+add;
	}


	public int getQuantity() {
		return quantity;
	}

	public void setQuantity(int quantity) {
		this.quantity = quantity;
	}

	

	

	

}
