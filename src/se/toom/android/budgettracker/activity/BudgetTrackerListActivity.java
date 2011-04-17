package se.toom.android.budgettracker.activity;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import se.toom.android.budgettracker.dao.BudgetTrackerDao;
import se.toom.android.budgettracker.dao.BudgetTrackerDaoImpl;
import android.app.ListActivity;
import android.os.Bundle;

public abstract class BudgetTrackerListActivity extends ListActivity {
	
	public static final String CONTEXT_CURRENT_MONTH = "contextCurrentMonth";
	public static final String CONTEXT_SUM_UPDATE_NOTIFICATION = "contextSumUpdateNotification";
	
	private static Map<String, Object> budgetContext = new HashMap<String, Object>();
	
	static {
		budgetContext.put(CONTEXT_SUM_UPDATE_NOTIFICATION, new HashSet<BudgetTrackerListActivity>());
	}
	
	protected BudgetTrackerDao budgetTrackerDao;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		budgetTrackerDao = new BudgetTrackerDaoImpl(this);
	}
	
	public Object getContextAttribute(String key) {
		return budgetContext.get(key);
	}

	protected void putContextAttribute(String key, Object obj) {
		budgetContext.put(key, obj);
	}
	
	@SuppressWarnings("unchecked")
	protected void registerSumUpdateNotification(BudgetTrackerListActivity activity) {
		Set<BudgetTrackerListActivity> activites = (Set<BudgetTrackerListActivity>) budgetContext.get(CONTEXT_SUM_UPDATE_NOTIFICATION);
		activites.add(activity);
	}
	
	@SuppressWarnings("unchecked")
	protected void notifySumUpdate() {
		Set<BudgetTrackerListActivity> activites = (Set<BudgetTrackerListActivity>) budgetContext.get(CONTEXT_SUM_UPDATE_NOTIFICATION);
		for(BudgetTrackerListActivity activity : activites) {
			activity.onSumUpdate();
		}
	}
	
	protected void onSumUpdate() {}
}