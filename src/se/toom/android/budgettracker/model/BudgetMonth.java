package se.toom.android.budgettracker.model;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import se.toom.android.budgettracker.Constants;

public class BudgetMonth implements Serializable {
	
	private static final long serialVersionUID = 8067358722677663148L;
	
	private static final SimpleDateFormat dateFormat = new SimpleDateFormat(Constants.DATE_FORMAT_YEARMONTH);
	
	protected BudgetDay day;
	
	public BudgetMonth(BudgetDay budgetDay) {
		if(budgetDay == null) {
			throw new IllegalArgumentException("BudgetDay cannot be null");
		}
		
		this.day = budgetDay;
	}
	
	@Override
	public String toString() {
		return dateFormat.format(day.getDate());
	}
	
	@Override
	public boolean equals(Object o) {
		if(o instanceof BudgetMonth) {
			return toString().equals(o.toString());
		}

		return super.equals(o);
	}
	
	@Override
	public int hashCode() {
		return toString().hashCode();
	}
	
	public static BudgetDay getLastDayOfMonth(BudgetMonth month) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(month.day.getDate());
		cal.set(Calendar.DAY_OF_MONTH, 1);
		cal.add(Calendar.MONTH, 1);
		cal.add(Calendar.DAY_OF_MONTH, -1);
		return new BudgetDay(cal.getTime());
	}
}
