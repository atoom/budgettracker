package se.toom.android.budgettracker.activity;

import se.toom.android.budgettracker.R;
import se.toom.android.budgettracker.adapter.CategoryListArrayAdapter;
import se.toom.android.budgettracker.model.BudgetCategory;
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

public class CategoryListActivity extends BudgetTrackerListActivity {
	
	private CategoryListArrayAdapter categoriesArrayAdapter;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		final Context context = this;
		final BudgetMonth budgetMonth = (BudgetMonth) getIntent().getSerializableExtra(INTENT_EXTRA_CURRENT_MONTH); 
		categoriesArrayAdapter = new CategoryListArrayAdapter(this, budgetTrackerDao, budgetMonth);
		
		populateArrayAdapter();
		
		setListAdapter(categoriesArrayAdapter);
		
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
				    			populateArrayAdapter();
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
			    					newItem.setMonth(budgetMonth);
			    					newItem.setValue(Integer.parseInt(input.getText().toString()));
				    				budgetTrackerDao.addBudgetItem(newItem);
				    				categoriesArrayAdapter.notifyDataSetChanged();
				    				populateArrayAdapter();
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
	
	protected void populateArrayAdapter() {
		categoriesArrayAdapter.clear();
		categoriesArrayAdapter.add(BudgetCategory.NEW);
		for(BudgetCategory category : budgetTrackerDao.getBudgetCategories()) {
			categoriesArrayAdapter.add(category);
		}
	}
}
