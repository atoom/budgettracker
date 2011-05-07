package se.toom.android.budgettracker.adapter;

import se.toom.android.budgettracker.dao.BudgetTrackerDao;
import se.toom.android.budgettracker.model.BudgetCategory;
import android.content.Context;
import android.widget.TextView;

public abstract class AbstractCategoryListArrayAdapter extends AbstractItemSumListArrayAdapter<BudgetCategory> {
	
	public AbstractCategoryListArrayAdapter(Context context, BudgetTrackerDao budgetTrackerDao) {
		super(context, budgetTrackerDao);
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
			bottomTextView.setText(getSumString(getCategorySum(itemObject)));
		}
	}
	
	protected abstract int getCategorySum(BudgetCategory itemObject);
}
