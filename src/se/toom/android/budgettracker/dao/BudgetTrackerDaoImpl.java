package se.toom.android.budgettracker.dao;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import se.toom.android.budgettracker.Constants;
import se.toom.android.budgettracker.model.BudgetCategory;
import se.toom.android.budgettracker.model.BudgetDay;
import se.toom.android.budgettracker.model.BudgetItem;
import se.toom.android.budgettracker.model.BudgetMonth;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class BudgetTrackerDaoImpl extends SQLiteOpenHelper implements BudgetTrackerDao {

	private static final SimpleDateFormat dateFormat = new SimpleDateFormat(Constants.DATE_FORMAT);
	private static final SimpleDateFormat dateFormatYearMonth = new SimpleDateFormat(Constants.DATE_FORMAT_YEARMONTH);
	private static final SimpleDateFormat dateTimeFormat = new SimpleDateFormat(Constants.DATETIME_FORMAT);
	
	private static final String DATABASE_NAME = "budgettracker";
	private static final int DATABASE_VERSION = 2;
	
	private static final String TABLE_CATEGORY = "category";
	private static final String TABLE_ITEM = "item";
	
	private static final String DDL_CATEGORY = String.format("create table %s (category TEXT PRIMARY KEY)", TABLE_CATEGORY);
	private static final String DDL_ITEM = String.format("create table %s (id INTEGER PRIMARY KEY, value INTEGER, created TIMESTAMP, category TEXT, day TIMESTAMP)", TABLE_ITEM);

	public BudgetTrackerDaoImpl(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		db.execSQL(DDL_CATEGORY);
		db.execSQL(DDL_ITEM);
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		if(oldVersion == 1 && newVersion == 2) {
			db.execSQL("alter table item add column day TIMESTAMP");
			db.execSQL("update item set day = date((created / 1000), 'unixepoch')");
			db.execSQL("drop table month");
		}
	}
	
	@Override
	public List<BudgetItem> getBudgetItems(BudgetMonth month) {
		if(month == null) {
			throw new IllegalArgumentException("Month cannot be null");
		}
		
		List<BudgetItem> result = new ArrayList<BudgetItem>();
		SQLiteDatabase db = null;
		Cursor cursor = null;
		
		try {
			db = getReadableDatabase();
			
			cursor = db.query(TABLE_ITEM, null, String.format("strftime('%%Y-%%m',day) = %s", month.toString()), null, null, null, null);
			
			if(cursor.moveToFirst()) {
				while(!cursor.isAfterLast()) {
					BudgetItem item = makeBudgetItem(cursor);
					result.add(item);
					cursor.moveToNext();
				}
			}
		} finally {
			if(cursor != null) {
				cursor.close();
			}
			
			if(db != null) {
				db.close();	
			}
		}
		
		return result;
	}
	
	@Override
	public List<BudgetDay> getBudgetDays(BudgetMonth month) {
		if(month == null) {
			throw new IllegalArgumentException("Month cannot be null");
		}
		
		Set<BudgetDay> result = new LinkedHashSet<BudgetDay>();
		SQLiteDatabase db = null;
		Cursor cursor = null;
		
		try {
			db = getReadableDatabase();
			
			String[] values = new String[] {
				month.toString()	
			};
			
			StringBuilder sql = new StringBuilder();
			sql.append("select distinct day");
			sql.append(" from %s where strftime('%%Y-%%m',day) = ?");
			sql.append(" order by day asc");
			
			cursor = db.rawQuery(String.format(sql.toString(), TABLE_ITEM), values);
			
			if(cursor.moveToFirst()) {
				while(!cursor.isAfterLast()) {
					BudgetDay day = makeBudgetDay(cursor);
					result.add(day);
					cursor.moveToNext();
				}
			}
		} finally {
			if(cursor != null) {
				cursor.close();
			}
			
			if(db != null) {
				db.close();	
			}
		}
		
		return new ArrayList<BudgetDay>(result);
	}

	@Override
	public List<BudgetCategory> getBudgetCategories() {
		List<BudgetCategory> result = new ArrayList<BudgetCategory>();
		SQLiteDatabase db = null;
		Cursor cursor = null;
		
		try {
			db = getReadableDatabase();
			
			cursor = db.rawQuery(String.format("select * from %s order by category asc", TABLE_CATEGORY), null);
			
			if(cursor.moveToFirst()) {
				while(!cursor.isAfterLast()) {
					BudgetCategory item = makeBudgetCategory(cursor);
					result.add(item);
					cursor.moveToNext();
				}
			}
		} finally {
			if(cursor != null) {
				cursor.close();
			}
			
			if(db != null) {
				db.close();	
			}
		}
		
		return result;

	}
	
	public BudgetCategory getBudgetCategory(String category) {
		BudgetCategory result = null;
		SQLiteDatabase db = null;
		Cursor cursor = null;
		
		try {
			db = getReadableDatabase();
			
			cursor = db.query(TABLE_CATEGORY, null, String.format("category = %s", category), null, null, null, null);
			
			if(cursor.moveToFirst()) {
				result = makeBudgetCategory(cursor);
			}
		} finally {
			if(cursor != null) {
				cursor.close();
			}
			
			if(db != null) {
				db.close();	
			}
		}
		
		return result;
	}

	@Override
	public void addBudgetCategory(BudgetCategory budgetCategory) {
		if(budgetCategory == null) {
			throw new IllegalArgumentException("BudgetCategory cannot be null");
		}
		
		SQLiteDatabase db = null;
		
		try {
			ContentValues values = new ContentValues();
			values.put("category", budgetCategory.getCategory());
			
			db = getWritableDatabase();
			db.insertWithOnConflict(TABLE_CATEGORY, null, values, SQLiteDatabase.CONFLICT_IGNORE);
		} finally {
			if(db != null) {
				db.close();	
			}
		}
	}

	@Override
	public void addBudgetItem(BudgetItem budgetItem) {
		if(budgetItem == null) {
			throw new IllegalArgumentException("BudgetItem cannot be null");
		}
		
		if(budgetItem.getCategory() == null) {
			throw new IllegalArgumentException("BudgetItem.Category cannot be null");
		}
		
		if(budgetItem.getDay() == null) {
			throw new IllegalArgumentException("BudgetItem.Day cannot be null");
		}
		 
		SQLiteDatabase db = null;
		
		try {
			ContentValues values = new ContentValues();
			values.put("value", budgetItem.getValue());
			values.put("created", dateTimeFormat.format(new Date()));
			values.put("category", budgetItem.getCategory().getCategory());
			values.put("day", dateFormat.format(budgetItem.getDay().getDate()));
			
			db = getWritableDatabase();
			long id = db.insertWithOnConflict(TABLE_ITEM, null, values, SQLiteDatabase.CONFLICT_IGNORE);
			budgetItem.setId(id);
		} finally {
			if(db != null) {
				db.close();	
			}
		}
	}

	@Override
	public List<BudgetMonth> getBudgetMonths() {
		Set<BudgetMonth> result = new LinkedHashSet<BudgetMonth>();
		SQLiteDatabase db = null;
		Cursor cursor = null;
		
		try {
			db = getReadableDatabase();
			
			cursor = db.rawQuery("select strftime('%Y-%m',day) as month from item group by month order by month asc", null);
			
			if(cursor.moveToFirst()) {
				while(!cursor.isAfterLast()) {
					BudgetMonth item = makeBudgetMonth(cursor);
					result.add(item);
					cursor.moveToNext();
				}
			}
		} finally {
			if(cursor != null) {
				cursor.close();
			}
			
			if(db != null) {
				db.close();	
			}
		}
		
		// Make sure that we have the current month in the list
		result.add(new BudgetMonth(new BudgetDay(new Date())));
		
		return new ArrayList<BudgetMonth>(result);

	}

	@Override
	public int getBudgetItemSum(BudgetMonth budgetMonth) {
		return getBudgetItemSum(budgetMonth.toString(), null);
	}
	
	@Override
	public int getBudgetItemSum(BudgetMonth budgetMonth, BudgetCategory budgetCategory) {
		return getBudgetItemSum(budgetMonth.toString(), budgetCategory.getCategory());
	}

	@Override
	public int getBudgetItemSum(BudgetDay budgetDay) {
		return getBudgetItemSum(budgetDay.toString(), null);
	}
	
	@Override
	public int getBudgetItemSum(BudgetDay budgetDay, BudgetCategory budgetCategory) {
		return getBudgetItemSum(budgetDay.toString(), budgetCategory.getCategory());
	}
	
	protected int getBudgetItemSum(String day, String category) {
		int result = 0;
		SQLiteDatabase db = null;
		Cursor cursor = null;

		try {
			List<String> values = new ArrayList<String>();
			
			String dayPattern = day.length() == 7 ? "%Y-%m" : "%Y-%m-%d"; 
			
			StringBuilder sql = new StringBuilder();
			sql.append("select sum(value) from %s");
			sql.append(" where strftime('%s',day) = ?");
			values.add(day);
			
			if(category != null) {
				sql.append(" and category = ?");
				values.add(category);
			}

			db = getReadableDatabase();
			
			cursor = db.rawQuery(String.format(sql.toString(), TABLE_ITEM, dayPattern), values.toArray(new String[values.size()]));
			
			if(cursor.moveToFirst()) {
				result = cursor.getInt(0);
			}
		} finally {
			if(cursor != null) {
				cursor.close();
			}
			
			if(db != null) {
				db.close();	
			}
		}
		
		return result;
		
	}

	protected BudgetMonth makeBudgetMonth(Cursor cursor) {
		try {
			String date = cursor.getString(cursor.getColumnIndex("month"));
			BudgetDay day = new BudgetDay(dateFormatYearMonth.parse(date));
			return new BudgetMonth(day);
		} catch(Exception e) {
			throw new RuntimeException("Unable to create BudgetMonth", e);
		}
	}
	
	protected BudgetItem makeBudgetItem(Cursor cursor) {
		try {
			int id = cursor.getInt(cursor.getColumnIndex("id"));
			int value = cursor.getInt(cursor.getColumnIndex("value"));
			Date created = dateTimeFormat.parse(cursor.getString(cursor.getColumnIndex("created")));
			Date day = dateFormat.parse(cursor.getString(cursor.getColumnIndex("day")));
			String category = cursor.getString(cursor.getColumnIndex("category"));
			
			return new BudgetItem(id, value, created, new BudgetCategory(category), new BudgetDay(day));
		} catch(Exception e) {
			throw new RuntimeException("Unable to create BudgetItem", e);
		}
	}
	
	protected BudgetCategory makeBudgetCategory(Cursor cursor) {
		return new BudgetCategory(cursor.getString(0));
	}
	
	protected BudgetDay makeBudgetDay(Cursor cursor) {
		try {
			String date = cursor.getString(cursor.getColumnIndex("day"));
			return new BudgetDay(dateFormat.parse(date));
		} catch(Exception e) {
			throw new RuntimeException("Unable to create BudgetDay", e);
		}
	}
}
