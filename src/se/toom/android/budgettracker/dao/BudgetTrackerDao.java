package se.toom.android.budgettracker.dao;

import java.util.List;

import se.toom.android.budgettracker.model.BudgetCategory;
import se.toom.android.budgettracker.model.BudgetItem;
import se.toom.android.budgettracker.model.BudgetMonth;

public interface BudgetTrackerDao {
	
	List<BudgetMonth> getBudgetMonths();
	
	List<BudgetItem> getBudgetItems(BudgetMonth month);
	
	List<BudgetCategory> getBudgetCategories();
	
	void addBudgetCategory(BudgetCategory budgetCategory);
	
	void addBudgetMonth(BudgetMonth budgetMonth);
	
	void addBudgetItem(BudgetItem budgetItem);
	
	int getBudgetItemSum(BudgetMonth budgetMonth, BudgetCategory budgetCategory);
	
	int getBudgetItemSum(BudgetMonth budgetMonth);
}
