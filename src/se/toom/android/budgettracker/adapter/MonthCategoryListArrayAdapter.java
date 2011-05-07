package se.toom.android.budgettracker.adapter;

import se.toom.android.budgettracker.dao.BudgetTrackerDao;
import se.toom.android.budgettracker.model.BudgetCategory;
import se.toom.android.budgettracker.model.BudgetMonth;
import android.content.Context;

public class MonthCategoryListArrayAdapter extends AbstractCategoryListArrayAdapter {
	
	protected BudgetMonth budgetMonth;
	
	public MonthCategoryListArrayAdapter(Context context, BudgetTrackerDao budgetTrackerDao, BudgetMonth budgetMonth) {
		super(context, budgetTrackerDao);
		this.budgetMonth = budgetMonth;
	}

	@Override
	protected int getCategorySum(BudgetCategory itemObject) {
		return budgetTrackerDao.getBudgetItemSum(budgetMonth, itemObject);
	}
}
