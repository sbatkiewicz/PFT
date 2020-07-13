import java.text.DecimalFormat;

public class Purchase {

	private String description;
	private double cost;
	private String category;
	DecimalFormat costFormat = new DecimalFormat("##.##");
	
	public Purchase() {
		description = "Empty";
		cost = 0;
		category = "Empty";
		
	}
		
	public Purchase(String description, double cost, String category) {
		this.description = description;
		this.cost = cost;
		this.category = category;
		
	}
		
	public String getDescription() {
		return description;
	}
	
	public double getCost() {
		return cost;
	}
	
	public String getCategory() {
		return category;
	}
	
	public void setDescription(String description) {
		this.description = description;
	}
	
	public void setCost(double cost) {
		this.cost = cost;
	}
	
	public void setCategory(String category) {
		this.category = category;
	}
	
	public String getFormattedCost() {
		String result = costFormat.format(cost);
		int decimalCount = result.indexOf('.');
		
		String cents = result.substring(decimalCount + 1);
		
		if(cents.length() == 1) {
			result += "0";
		}
		else if(cents.length() == 0) {
			result += "00";
		}	
		
		return result;
	}
	
	public String toString() {
		String result = String.format("%-22s %-6s", description, "$" + cost);
		String modifiedCategory = category;
		while(modifiedCategory.length() < 24) {
			modifiedCategory = " " + modifiedCategory;
		}
		String.format("%14s", modifiedCategory);
		result += modifiedCategory;
		return result;
	}
}
