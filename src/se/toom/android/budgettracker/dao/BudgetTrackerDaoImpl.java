package se.toom.android.budgettracker.dao;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import se.toom.android.budgettracker.model.BudgetCategory;
import se.toom.android.budgettracker.model.BudgetItem;
import se.toom.android.budgettracker.model.BudgetMonth;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class BudgetTrackerDaoImpl extends SQLiteOpenHelper implements BudgetTrackerDao {

	private static final String DATABASE_NAME = "budgettracker";
	private static final int DATABASE_VERSION = 1;
	
	private static final String TABLE_CATEGORY = "category";
	private static final String TABLE_MONTH = "month";
	private static final String TABLE_ITEM = "item";
	
	private static final String DDL_CATEGORY = String.format("create table %s (category TEXT PRIMARY KEY)", TABLE_CATEGORY);
	private static final String DDL_MONTH = String.format("create table %s (month TEXT PRIMARY KEY)", TABLE_MONTH);
	private static final String DDL_ITEM = String.format("create table %s (id INTEGER PRIMARY KEY, value INTEGER, created INTEGER, category TEXT, month TEXT)", TABLE_ITEM);

	public BudgetTrackerDaoImpl(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		db.execSQL(DDL_CATEGORY);
		db.execSQL(DDL_MONTH);
		db.execSQL(DDL_ITEM);
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		// TODO Auto-generated method stub
	}
	
	@Override
	public List<BudgetItem> getBudgetItems(BudgetMonth month) {
		if(month == null) {
			throw new IllegalArgumentException("Month cannot be null");
		}
		
		List<BudgetItem> result = new ArrayList<BudgetItem>();
		SQLiteDatabase db = null;
		
		try {
			db = getReadableDatabase();
			Cursor cursor = db.query(TABLE_ITEM, null, String.format("month = %d%d", month.getYear(), month.getMonth()), null, null, null, null);
			
			if(cursor.moveToFirst()) {
				while(!cursor.isAfterLast()) {
					BudgetItem item = makeBudgetItem(cursor);
					result.add(item);
					cursor.moveToNext();
				}
			}
		} finally {
			if(db != null) {
				db.close();	
			}
		}
		
		return result;
	}

	@Override
	public List<BudgetCategory> getBudgetCategories() {
		List<BudgetCategory> result = new ArrayList<BudgetCategory>();
		SQLiteDatabase db = null;
		
		try {
			db = getReadableDatabase();
			
			Cursor cursor = db.rawQuery(String.format("select * from %s order by category asc", TABLE_CATEGORY), null);
			
			if(cursor.moveToFirst()) {
				while(!cursor.isAfterLast()) {
					BudgetCategory item = makeBudgetCategory(cursor);
					result.add(item);
					cursor.moveToNext();
				}
			}
		} finally {
			if(db != null) {
				db.close();	
			}
		}
		
		return result;

	}
	
	public BudgetCategory getBudgetCategory(String category) {
		BudgetCategory result = null;
		SQLiteDatabase db = null;
		
		try {
			db = getReadableDatabase();
			Cursor cursor = db.query(TABLE_CATEGORY, null, String.format("category = %s", category), null, null, null, null);
			
			if(cursor.moveToFirst()) {
				result = makeBudgetCategory(cursor);
			}
		} finally {
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
		
		if(budgetItem.getMonth() == null) {
			throw new IllegalArgumentException("BudgetItem.Month cannot be null");
		}
		 
		SQLiteDatabase db = null;
		
		try {
			ContentValues values = new ContentValues();
			values.put("value", budgetItem.getValue());
			values.put("created", new Date().getTime());
			values.put("category", budgetItem.getCategory().getCategory());
			values.put("month", String.format("%d%d", budgetItem.getMonth().getYear(), budgetItem.getMonth().getMonth()));
			
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
	public void addBudgetMonth(BudgetMonth budgetMonth) {
		if(budgetMonth == null) {
			throw new IllegalArgumentException("BudgetMonth cannot be null");
		}
		
		SQLiteDatabase db = null;
		
		try {
			ContentValues values = new ContentValues();
			values.put("month", String.format("%d%d", budgetMonth.getYear(), budgetMonth.getMonth()));
			
			db = getWritableDatabase();
			db.insertWithOnConflict(TABLE_MONTH, null, values, SQLiteDatabase.CONFLICT_IGNORE);
		} finally {
			if(db != null) {
				db.close();	
			}
		}
	}

	@Override
	public List<BudgetMonth> getBudgetMonths() {
		List<BudgetMonth> result = new ArrayList<BudgetMonth>();
		SQLiteDatabase db = null;
		
		try {
			db = getReadableDatabase();
			Cursor cursor = db.query(TABLE_MONTH, null, null, null, null, null, null);
			
			if(cursor.moveToFirst()) {
				while(!cursor.isAfterLast()) {
					BudgetMonth item = makeBudgetMonth(cursor);
					result.add(item);
					cursor.moveToNext();
				}
			}
		} finally {
			if(db != null) {
				db.close();	
			}
		}
		
		return result;

	}
	
	public BudgetMonth getBudgetMonth(String month) {
		BudgetMonth result = null;
		SQLiteDatabase db = null;
		
		try {
			db = getReadableDatabase();
			Cursor cursor = db.query(TABLE_MONTH, null, String.format("month = %s", month), null, null, null, null);
			
			if(cursor.moveToFirst()) {
				result = makeBudgetMonth(cursor);
			}
		} finally {
			if(db != null) {
				db.close();	
			}
		}
		
		return result;
	}
	
	@Override
	public int getBudgetItemSum(BudgetMonth budgetMonth, BudgetCategory budgetCategory) {
		int result = 0;
		SQLiteDatabase db = null;

		try {
			String[] values = new String[] { 
				String.format("%d%d", budgetMonth.getYear(), budgetMonth.getMonth()),
				budgetCategory.getCategory() 
			};

			db = getReadableDatabase();
			
			Cursor cursor = db.rawQuery(String.format("select sum(value) from %s where month = ? and category = ?", TABLE_ITEM), values);
			
			if(cursor.moveToFirst()) {
				result = cursor.getInt(0);
			}
		} finally {
			if(db != null) {
				db.close();	
			}
		}
		
		return result;
	}
	
	@Override
	public int getBudgetItemSum(BudgetMonth budgetMonth) {
		int result = 0;
		SQLiteDatabase db = null;

		try {
			String[] values = new String[] { 
				String.format("%d%d", budgetMonth.getYear(), budgetMonth.getMonth())
			};

			db = getReadableDatabase();
			
			Cursor cursor = db.rawQuery(String.format("select sum(value) from %s where month = ?", TABLE_ITEM), values);
			
			if(cursor.moveToFirst()) {
				result = cursor.getInt(0);
			}
		} finally {
			if(db != null) {
				db.close();	
			}
		}
		
		return result;
	}

	protected BudgetMonth makeBudgetMonth(Cursor cursor) {
		return new BudgetMonth(cursor.getString(0));
	}
	
	protected BudgetItem makeBudgetItem(Cursor cursor) {
		return new BudgetItem(cursor.getInt(0), cursor.getInt(1), new Date(cursor.getLong(2)),
				new BudgetCategory(cursor.getString(3)), new BudgetMonth(cursor.getString(4)));
	}
	
	protected BudgetCategory makeBudgetCategory(Cursor cursor) {
		return new BudgetCategory(cursor.getString(0));
	}
}
