package se.toom.android.budgettracker.activity;

import java.util.List;

import se.toom.android.budgettracker.adapter.AbstractItemSumListArrayAdapter;
import se.toom.android.budgettracker.dao.BudgetTrackerDao;
import se.toom.android.budgettracker.dao.BudgetTrackerDaoImpl;
import android.app.ListActivity;
import android.os.Bundle;

public abstract class BudgetTrackerListActivity<T> extends ListActivity {
	
	public static final String INTENT_EXTRA_CURRENT_MONTH = "intentExtraCurrentMonth";
	public static final String INTENT_EXTRA_CURRENT_DAY = "intentExtraCurrentDay";
	
	protected BudgetTrackerDao budgetTrackerDao;
	
	protected AbstractItemSumListArrayAdapter<T> itemSumListArrayAdapter;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		budgetTrackerDao = new BudgetTrackerDaoImpl(this);
		
		itemSumListArrayAdapter = createItemSumListArrayAdapter();
		
		populateItemSumListArrayAdapter();
		
		setListAdapter(itemSumListArrayAdapter);
	}
	
	@Override
	protected void onResume() {
		super.onResume();
		populateItemSumListArrayAdapter();
	}
	
	protected void populateItemSumListArrayAdapter() {
		itemSumListArrayAdapter.clear();
		for(T item : getItemsForArrayAdapter()) {
			itemSumListArrayAdapter.add(item);
		}
	}
	
	protected abstract AbstractItemSumListArrayAdapter<T> createItemSumListArrayAdapter();
	
	protected abstract List<T> getItemsForArrayAdapter();
}