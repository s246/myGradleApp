import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * @author Sebastian Gutierrez
 *
 */

public class Inventory {
	
	//inventory in memory
	public static final ArrayList<Item> inventory = new ArrayList<Item>();
	
	//utils dictionaries
	public static final Map <String, Boolean> taxDict = new HashMap<String, Boolean>(){{
        put("Tax-Exempt", true);
        put("Taxable", false);
    }};
    
	public static final Map <Boolean,String> taxDict2 = new HashMap<Boolean,String>(){{
        put(true,"Tax-Exempt");
        put(false,"Taxable");
    }};

    
    // add (contains works because hashcode and equals overriden in ITEM object)
    public static void addToInventory(Item i) {
    	if (inventory.contains(i)) {
    		Item item=inventory.get(inventory.indexOf(i));
    		item.setStock(item.getStock()+i.getStock());
    	}
    	
    	else {
    		inventory.add(i);
    	}
    	
    }

}
