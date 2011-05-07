package se.toom.android.budgettracker.adapter;

import se.toom.android.budgettracker.dao.BudgetTrackerDao;
import se.toom.android.budgettracker.model.BudgetCategory;
import se.toom.android.budgettracker.model.BudgetDay;
import android.content.Context;

public class DayCategoryListArrayAdapter extends AbstractCategoryListArrayAdapter {
	
	protected BudgetDay budgetDay;
	
	public DayCategoryListArrayAdapter(Context context, BudgetTrackerDao budgetTrackerDao, BudgetDay budgetDay) {
		super(context, budgetTrackerDao);
		this.budgetDay = budgetDay;
	}

	@Override
	protected int getCategorySum(BudgetCategory itemObject) {
		return budgetTrackerDao.getBudgetItemSum(budgetDay, itemObject);
	}
}
