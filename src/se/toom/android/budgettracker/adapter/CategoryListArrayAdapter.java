package se.toom.android.budgettracker.adapter;

import se.toom.android.budgettracker.dao.BudgetTrackerDao;
import se.toom.android.budgettracker.model.BudgetCategory;
import se.toom.android.budgettracker.model.BudgetMonth;
import android.content.Context;
import android.widget.TextView;

public class CategoryListArrayAdapter extends AbstractItemSumListArrayAdapter<BudgetCategory> {
	
	protected BudgetMonth budgetMonth;
	
	public CategoryListArrayAdapter(Context context, BudgetTrackerDao budgetTrackerDao, BudgetMonth budgetMonth) {
		super(context, budgetTrackerDao);
		this.budgetMonth = budgetMonth;
	}

	@Override
	protected void setTopText(TextView topTextView, BudgetCategory itemObject) {
		topTextView.setText(itemObject.getCategory());
	}

	@Override
	protected void setBottomText(TextView bottomTextView, BudgetCategory itemObject) {
		if (BudgetCategory.NEW.equals(itemObject)) {
			bottomTextView.setVisibility(TextView.GONE);
		} else {
			bottomTextView.setVisibility(TextView.VISIBLE);
			bottomTextView.setText(getSumString(budgetTrackerDao.getBudgetItemSum(budgetMonth, itemObject)));
		}
	}
	
	public BudgetMonth getBudgetMonth() {
		return budgetMonth;
	}
}
