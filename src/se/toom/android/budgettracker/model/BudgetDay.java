package se.toom.android.budgettracker.model;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;

import se.toom.android.budgettracker.Constants;

public class BudgetDay implements Serializable {
	
	private static final long serialVersionUID = -5388764359179758017L;

	private static final SimpleDateFormat dateFormat = new SimpleDateFormat(Constants.DATE_FORMAT);
	
	protected Date date;
	protected BudgetMonth month;
	
	public BudgetDay(Date date) {
		this.date = date;
		this.month = new BudgetMonth(this);
	}
	
	public Date getDate() {
		return date;
	}
	
	public BudgetMonth getMonth() {
		return month;
	}
	
	@Override
	public String toString() {
		return dateFormat.format(date);
	}
	
	@Override
	public boolean equals(Object o) {
		if(o instanceof BudgetDay) {
			return toString().equals(o.toString());
		}
		
		return super.equals(o);
	}
	
	@Override
	public int hashCode() {
		return toString().hashCode();
	}
}
