package se.toom.android.budgettracker.model;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Calendar;

public class BudgetMonth implements Serializable {
	
	private static final long serialVersionUID = 8067358722677663148L;
	
	private int year;
	private int month;
	
	private static final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM");

	public BudgetMonth(String budgetMonth) {
		this.year = Integer.parseInt(budgetMonth.substring(0, 4));
		this.month = Integer.parseInt(budgetMonth.substring(4));
	}
	
	public BudgetMonth(int year, int month) {
		this.year = year;
		this.month = month;
	}

	public int getYear() {
		return year;
	}

	public int getMonth() {
		return month;
	}
	
	@Override
	public String toString() {
		Calendar calendar = Calendar.getInstance();
		calendar.set(Calendar.YEAR, year);
		calendar.set(Calendar.MONTH, month);
		return dateFormat.format(calendar.getTime());
	}
	
	public static BudgetMonth getCurrentMonth() {
		Calendar calendar = Calendar.getInstance();
		return new BudgetMonth(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH));
	}
}
