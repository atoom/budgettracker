package se.toom.android.budgettracker.activity;

import se.toom.android.budgettracker.adapter.MonthListArrayAdapter;
import se.toom.android.budgettracker.model.BudgetMonth;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;

public class MonthListActivity extends BudgetTrackerListActivity {
	
	private MonthListArrayAdapter monthsArrayAdapter;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		final Context context = this;
		
		// Assert that the current month is available
		budgetTrackerDao.addBudgetMonth(BudgetMonth.getCurrentMonth());
		monthsArrayAdapter = new MonthListArrayAdapter(this, budgetTrackerDao);

		populateArrayAdapter();
		
		setListAdapter(monthsArrayAdapter);
		
		registerSumUpdateNotification(this);
		
		getListView().setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int pos, long id) {
				Intent categoryIntent = new Intent();
				categoryIntent.setClass(context, CategoryListActivity.class);
				
				// Set the selected month in the shared context
				putContextAttribute(CONTEXT_CURRENT_MONTH, parent.getItemAtPosition(pos));
				
				startActivity(categoryIntent);
			}
		});		
	}
	
	@Override
	protected void onSumUpdate() {
		populateArrayAdapter();
	}
	
	protected void populateArrayAdapter() {
		monthsArrayAdapter.clear();
		for(BudgetMonth month : budgetTrackerDao.getBudgetMonths()) {
			monthsArrayAdapter.add(month);
		}
	}
}
