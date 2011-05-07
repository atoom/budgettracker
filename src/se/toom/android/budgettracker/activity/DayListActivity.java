package se.toom.android.budgettracker.activity;

import java.util.List;

import se.toom.android.budgettracker.adapter.AbstractItemSumListArrayAdapter;
import se.toom.android.budgettracker.adapter.DayListArrayAdapter;
import se.toom.android.budgettracker.model.BudgetDay;
import se.toom.android.budgettracker.model.BudgetMonth;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;

public class DayListActivity extends BudgetTrackerListActivity<BudgetDay> {
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		final Context context = this;
		getListView().setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int pos, long id) {
				Intent categoryIntent = new Intent();
				categoryIntent.setClass(context, CategoryListActivity.class);
				categoryIntent.putExtra(INTENT_EXTRA_CURRENT_DAY, itemSumListArrayAdapter.getItem(pos));
				startActivity(categoryIntent);
			}
		});		
	}

	@Override
	protected AbstractItemSumListArrayAdapter<BudgetDay> createItemSumListArrayAdapter() {
		return new DayListArrayAdapter(this, budgetTrackerDao);
	}

	@Override
	protected List<BudgetDay> getItemsForArrayAdapter() {
		return budgetTrackerDao.getBudgetDays(
				(BudgetMonth) getIntent().getSerializableExtra(INTENT_EXTRA_CURRENT_MONTH));
	}
}
