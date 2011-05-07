package se.toom.android.budgettracker.adapter;

import se.toom.android.budgettracker.activity.BudgetTrackerListActivity;
import se.toom.android.budgettracker.dao.BudgetTrackerDao;
import se.toom.android.budgettracker.model.BudgetDay;
import android.widget.TextView;

public class DayListArrayAdapter extends AbstractItemSumListArrayAdapter<BudgetDay> {
	
	public DayListArrayAdapter(BudgetTrackerListActivity<BudgetDay> budgetTrackerListActivity, BudgetTrackerDao budgetTrackerDao) {
		super(budgetTrackerListActivity, budgetTrackerDao);
	}

	@Override
	protected void setTopText(TextView topTextView, BudgetDay itemObject) {
		topTextView.setText(itemObject.toString());
	}

	@Override
	protected void setBottomText(TextView bottomTextView, BudgetDay itemObject) {
		bottomTextView.setText(getSumString(budgetTrackerDao.getBudgetItemSum(itemObject)));
	}
}
