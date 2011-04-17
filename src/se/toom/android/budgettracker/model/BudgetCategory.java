package se.toom.android.budgettracker.model;

public class BudgetCategory {
	private String category;

	public static final BudgetCategory NEW = new BudgetCategory("<NEW CATEGORY>");
	
	public BudgetCategory(String category) {
		this.category = category;
	}
	
	public String getCategory() {
		return category;
	}
	
	@Override
	public String toString() {
		return category;
	}
	
	@Override
	public boolean equals(Object o) {
		if(o instanceof BudgetCategory) {
			return category.equals(o.toString());
		} else {
			return super.equals(o);
		}
	}
}
