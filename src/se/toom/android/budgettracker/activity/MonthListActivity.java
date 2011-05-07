package se.toom.android.budgettracker.activity;

import java.util.List;

import se.toom.android.budgettracker.adapter.AbstractItemSumListArrayAdapter;
import se.toom.android.budgettracker.adapter.MonthListArrayAdapter;
import se.toom.android.budgettracker.model.BudgetMonth;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;

public class MonthListActivity extends BudgetTrackerListActivity<BudgetMonth> {
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		final Context context = this;
		getListView().setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int pos, long id) {
				Intent categoryIntent = new Intent();
				categoryIntent.setClass(context, CategoryListActivity.class);
				categoryIntent.putExtra(INTENT_EXTRA_CURRENT_MONTH, itemSumListArrayAdapter.getItem(pos));
				startActivity(categoryIntent);
			}
		});
		
		getListView().setOnItemLongClickListener(new OnItemLongClickListener() {
			@Override
			public boolean onItemLongClick(AdapterView<?> parent, View view, int pos, long id) {
				Intent dayIntent = new Intent();
				dayIntent.setClass(context, DayListActivity.class);
				dayIntent.putExtra(INTENT_EXTRA_CURRENT_MONTH, itemSumListArrayAdapter.getItem(pos));
				startActivity(dayIntent);
				return true;
			}
		});
	}
	
	@Override
	protected AbstractItemSumListArrayAdapter<BudgetMonth> createItemSumListArrayAdapter() {
		return new MonthListArrayAdapter(this, budgetTrackerDao);
	}

	@Override
	protected List<BudgetMonth> getItemsForArrayAdapter() {
		return budgetTrackerDao.getBudgetMonths();
	}
}
