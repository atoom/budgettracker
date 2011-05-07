package se.toom.android.budgettracker.adapter;

import java.util.ArrayList;

import se.toom.android.budgettracker.R;
import se.toom.android.budgettracker.activity.BudgetTrackerListActivity;
import se.toom.android.budgettracker.dao.BudgetTrackerDao;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

public abstract class AbstractItemSumListArrayAdapter<T> extends ArrayAdapter<T> {
	
	protected BudgetTrackerListActivity<T> budgetTrackerListActivity;
	protected BudgetTrackerDao budgetTrackerDao;
	
	public AbstractItemSumListArrayAdapter(Context context, BudgetTrackerDao budgetTrackerDao) {
		super(context, R.layout.item_list_item_with_sum, new ArrayList<T>());
		this.budgetTrackerDao = budgetTrackerDao;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {

        View v = convertView;
        if (v == null) {
        	LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            v = inflater.inflate(R.layout.item_list_item_with_sum, null);
        }
		
		T t = getItem(position);
		if (t != null) {
			TextView topTextView = (TextView) v.findViewById(R.id.toptext);
			TextView bottomTextView = (TextView) v.findViewById(R.id.bottomtext);
			
			if (topTextView != null) {
				setTopText(topTextView, t);
			}
			
			if(bottomTextView != null) {
				setBottomText(bottomTextView, t);
			}
		}
		
		return v;
	}
	
	protected String getSumString(int sum) {
		return String.format("Sum: %d", sum);
	}
	
	protected abstract void setTopText(TextView topTextView, T itemObject);
	
	protected abstract void setBottomText(TextView bottomTextView, T itemObject);
}
