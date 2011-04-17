package se.toom.android.budgettracker.adapter;

import se.toom.android.budgettracker.activity.BudgetTrackerListActivity;
import se.toom.android.budgettracker.dao.BudgetTrackerDao;
import se.toom.android.budgettracker.model.BudgetCategory;
import se.toom.android.budgettracker.model.BudgetMonth;
import android.widget.TextView;

public class CategoryListArrayAdapter extends AbstractItemSumListArrayAdapter<BudgetCategory> {
	
	public CategoryListArrayAdapter(BudgetTrackerListActivity budgetTrackerListActivity, BudgetTrackerDao budgetTrackerDao) {
		super(budgetTrackerListActivity, budgetTrackerDao);
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
			BudgetMonth currentMonth = (BudgetMonth) budgetTrackerListActivity.getContextAttribute(BudgetTrackerListActivity.CONTEXT_CURRENT_MONTH);
			bottomTextView.setVisibility(TextView.VISIBLE);
			bottomTextView.setText(getSumString(budgetTrackerDao.getBudgetItemSum(currentMonth, itemObject)));
		}
	}
}
