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
		
		monthsArrayAdapter = new MonthListArrayAdapter(this, budgetTrackerDao);
		
		setListAdapter(monthsArrayAdapter);

		final Context context = this;
		getListView().setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int pos, long id) {
				Intent categoryIntent = new Intent();
				categoryIntent.setClass(context, CategoryListActivity.class);
				categoryIntent.putExtra(INTENT_EXTRA_CURRENT_MONTH, monthsArrayAdapter.getItem(pos));
				startActivity(categoryIntent);
			}
		});		
	}
	
	@Override
	protected void onResume() {
		super.onResume();
		
		// Assert that the current month is available
		budgetTrackerDao.addBudgetMonth(BudgetMonth.getCurrentMonth());
		
		populateArrayAdapter();
	}
	
	protected void populateArrayAdapter() {
		monthsArrayAdapter.clear();
		for(BudgetMonth month : budgetTrackerDao.getBudgetMonths()) {
			monthsArrayAdapter.add(month);
		}
	}
}
