package se.toom.android.budgettracker.activity;

import java.util.Date;
import java.util.List;

import se.toom.android.budgettracker.R;
import se.toom.android.budgettracker.adapter.AbstractCategoryListArrayAdapter;
import se.toom.android.budgettracker.adapter.AbstractItemSumListArrayAdapter;
import se.toom.android.budgettracker.adapter.DayCategoryListArrayAdapter;
import se.toom.android.budgettracker.adapter.MonthCategoryListArrayAdapter;
import se.toom.android.budgettracker.model.BudgetCategory;
import se.toom.android.budgettracker.model.BudgetDay;
import se.toom.android.budgettracker.model.BudgetItem;
import se.toom.android.budgettracker.model.BudgetMonth;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.EditText;

public class CategoryListActivity extends BudgetTrackerListActivity<BudgetCategory> {
	
	protected AbstractCategoryListArrayAdapter categoriesArrayAdapter;
	protected BudgetDay budgetDay;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		final Context context = this;
		
		if(getIntent().hasExtra(INTENT_EXTRA_CURRENT_MONTH)) {
			BudgetMonth budgetMonth = (BudgetMonth) getIntent().getSerializableExtra(INTENT_EXTRA_CURRENT_MONTH); 
			budgetDay = getDayForMonth(budgetMonth);
		} else if(getIntent().hasExtra(INTENT_EXTRA_CURRENT_DAY)) {
			budgetDay = (BudgetDay) getIntent().getSerializableExtra(INTENT_EXTRA_CURRENT_DAY);
		} else {
			throw new IllegalArgumentException("Cannot start activity without extra parameters");
		}
		
		getListView().setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int pos, long id) {
				final BudgetCategory selectedItem = (BudgetCategory) parent.getItemAtPosition(pos);
				
				if(BudgetCategory.NEW.equals(selectedItem)) {
					final EditText input = new EditText(context);
					input.setInputType(InputType.TYPE_CLASS_TEXT);
					new AlertDialog.Builder(context)
				    	.setTitle(R.string.addbudgetcategory)
				    	.setView(input)
				    	.setPositiveButton(R.string.buttonOk, new DialogInterface.OnClickListener() {
				    		public void onClick(DialogInterface dialog, int whichButton) {
				    			BudgetCategory newCategory = new BudgetCategory(input.getText().toString());
				    			budgetTrackerDao.addBudgetCategory(newCategory);
				    			populateItemSumListArrayAdapter();
				    		}
				    	}).setNegativeButton(R.string.buttonCancel, new DialogInterface.OnClickListener() {
				    		public void onClick(DialogInterface dialog, int whichButton) {
				            // Do nothing.
				        }
				    }).show();
				} else {
					final EditText input = new EditText(context);
					input.setInputType(InputType.TYPE_CLASS_NUMBER);
					new AlertDialog.Builder(context)
			    		.setTitle(R.string.addbudgetitem)
			    		.setView(input)
			    		.setPositiveButton(R.string.buttonOk, new DialogInterface.OnClickListener() {
			    			public void onClick(DialogInterface dialog, int whichButton) {
			    				try {
			    					BudgetItem newItem = new BudgetItem();
			    					newItem.setCategory(selectedItem);
			    					newItem.setDay(budgetDay);
			    					newItem.setValue(Integer.parseInt(input.getText().toString()));
				    				budgetTrackerDao.addBudgetItem(newItem);
				    				populateItemSumListArrayAdapter();
			    				} catch(NumberFormatException e) {}
			    			}
			    		}).setNegativeButton(R.string.buttonCancel, new DialogInterface.OnClickListener() {
			    			public void onClick(DialogInterface dialog, int whichButton) {
			    			// 	Do nothing.
			    		}
			    	}).show();
				}
			}
		});
	}
	
	@Override
	protected AbstractItemSumListArrayAdapter<BudgetCategory> createItemSumListArrayAdapter() {
		if(getIntent().hasExtra(INTENT_EXTRA_CURRENT_MONTH)) {
			return new MonthCategoryListArrayAdapter(this, budgetTrackerDao, 
					(BudgetMonth) getIntent().getSerializableExtra(INTENT_EXTRA_CURRENT_MONTH));
		} else if(getIntent().hasExtra(INTENT_EXTRA_CURRENT_DAY)) {
			return new DayCategoryListArrayAdapter(this, budgetTrackerDao, 
					(BudgetDay) getIntent().getSerializableExtra(INTENT_EXTRA_CURRENT_DAY));
		} else {
			throw new IllegalArgumentException("Cannot start activity without extra parameters");
		}
	}
	
	@Override
	protected List<BudgetCategory> getItemsForArrayAdapter() {
		List<BudgetCategory> result = budgetTrackerDao.getBudgetCategories();
		result.add(0, BudgetCategory.NEW);
		return result;
	}

	/**
	 * Get the day for the supplied month. If the supplied month
	 * match the current month the current date will be returned. In
	 * other cases the last day of the supplied month will be returned.
	 * 
	 * @param month
	 * @return
	 */
	protected BudgetDay getDayForMonth(BudgetMonth month) {
		BudgetDay currentDay = new BudgetDay(new Date());
		BudgetMonth currentMonth = new BudgetMonth(currentDay);
		if(currentMonth.equals(month)) {
			return currentDay;
		} else {
			return BudgetMonth.getLastDayOfMonth(month);
		}
	}
}
