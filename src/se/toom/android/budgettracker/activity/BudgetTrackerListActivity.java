package se.toom.android.budgettracker.activity;

import se.toom.android.budgettracker.dao.BudgetTrackerDao;
import se.toom.android.budgettracker.dao.BudgetTrackerDaoImpl;
import android.app.ListActivity;
import android.os.Bundle;

public abstract class BudgetTrackerListActivity extends ListActivity {
	
	public static final String INTENT_EXTRA_CURRENT_MONTH = "intentExtraCurrentMonth";
	
	protected BudgetTrackerDao budgetTrackerDao;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		budgetTrackerDao = new BudgetTrackerDaoImpl(this);
	}
}