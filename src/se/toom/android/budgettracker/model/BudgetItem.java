package se.toom.android.budgettracker.model;

import java.util.Date;

public class BudgetItem {
	private long id;
	private int value;
	private Date created;
	private BudgetCategory category;
	private BudgetMonth month;
	
	public BudgetItem() {}

	public BudgetItem(long id, int value, Date created, BudgetCategory category,
			BudgetMonth month) {
		this.id = id;
		this.value = value;
		this.created = created;
		this.category = category;
		this.month = month;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public int getValue() {
		return value;
	}

	public void setValue(int value) {
		this.value = value;
	}
	
	public Date getCreated() {
		return created;
	}

	public void setCreated(Date created) {
		this.created = created;
	}

	public BudgetCategory getCategory() {
		return category;
	}

	public void setCategory(BudgetCategory category) {
		this.category = category;
	}

	public BudgetMonth getMonth() {
		return month;
	}

	public void setMonth(BudgetMonth month) {
		this.month = month;
	}
}
