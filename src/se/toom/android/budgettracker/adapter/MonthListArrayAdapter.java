package se.toom.android.budgettracker.adapter;

import se.toom.android.budgettracker.activity.BudgetTrackerListActivity;
import se.toom.android.budgettracker.dao.BudgetTrackerDao;
import se.toom.android.budgettracker.model.BudgetMonth;
import android.widget.TextView;

public class MonthListArrayAdapter extends AbstractItemSumListArrayAdapter<BudgetMonth> {
	
	public MonthListArrayAdapter(BudgetTrackerListActivity budgetTrackerListActivity, BudgetTrackerDao budgetTrackerDao) {
		super(budgetTrackerListActivity, budgetTrackerDao);
	}

	@Override
	protected void setTopText(TextView topTextView, BudgetMonth itemObject) {
		topTextView.setText(itemObject.toString());
	}

	@Override
	protected void setBottomText(TextView bottomTextView, BudgetMonth itemObject) {
		bottomTextView.setText(getSumString(budgetTrackerDao.getBudgetItemSum(itemObject)));
	}
}
