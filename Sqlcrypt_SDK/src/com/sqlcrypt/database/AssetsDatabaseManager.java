package com.sqlcrypt.database;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import com.sqlcrypt.database.sqlite.SQLiteDatabase;
import com.sqlcrypt.database.sqlite.SQLiteDebug;


import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.AssetManager;
import android.util.Log;

public class AssetsDatabaseManager {
	private static final String TAG = "AssetsDatabase";
	private final String DATABASE_DIR = "data";
	private final String databasepath;
	
	
	// A mapping from assets database file to SQLiteDatabase object
	private Map<String, SQLiteDatabase> databases = new HashMap<String, SQLiteDatabase>();
	
	// Context of application
	private Context context = null;
	
	// Singleton Pattern
	private static AssetsDatabaseManager mInstance = null;
	
	/**
	 * Initialize AssetsDatabaseManager
	 * @param context, context of application
	 */
	public static void initManager(Context context){
		if(mInstance == null){
			mInstance = new AssetsDatabaseManager(context);
		}
	}
	
	/**
	 * Get a AssetsDatabaseManager object
	 * @return, if success return a AssetsDatabaseManager object, else return null
	 */
	public static AssetsDatabaseManager getManager(){
		if(mInstance == null) {
			throw new NullPointerException("Please first setup initManager(Context)");
		}
		return mInstance;
	}
	
	private AssetsDatabaseManager(Context context){
		this.context = context;
		File datafile = new File(context.getFilesDir(), DATABASE_DIR);
		databasepath = datafile.getAbsolutePath();
	}
	
	/**
	 * Get a assets database, if this database is opened this method is only return a copy of the opened database
	 * @param dbfile db文件路径(在assets中的文件路径)
	 * @param passwd 密码
	 * @return, if success it return a SQLiteDatabase object else return null
	 */
	public SQLiteDatabase getDatabase(String dbfile, String passwd) {
		SQLiteDatabase db = databases.get(dbfile);
		if(db != null && db.isOpen()){
			if(SQLiteDebug.isDebug) {
				Log.i(TAG, String.format("Return a database copy of %s", dbfile));
			}
			return (SQLiteDatabase) databases.get(dbfile);
		}
		if(context==null)
			return null;
		if(SQLiteDebug.isDebug) {
			Log.i(TAG, String.format("Create database %s", dbfile));
		}
		String spath = databasepath;
		String sfile = getDatabaseFile(dbfile);
		
		File file = new File(sfile);
		SharedPreferences dbs = context.getSharedPreferences(
				AssetsDatabaseManager.class.toString(), 0);
		// Get Database file flag, if true means this database file was copied and valid
		boolean flag = dbs.getBoolean(dbfile, false);
		if(!flag || !file.exists()){
			file = new File(spath);
			if(!file.exists() && !file.mkdirs()){
				if(SQLiteDebug.isDebug) {
					Log.i(TAG, "Create \""+spath+"\" fail!");
				}
				return null;
			}
			if(!copyAssetsToFilesystem(dbfile, sfile)){
				if(SQLiteDebug.isDebug) {
					Log.i(TAG, String.format("Copy %s to %s fail!", dbfile, sfile));
				}
				return null;
			}
			
			dbs.edit().putBoolean(dbfile, true).commit();
		}
		
		db = SQLiteDatabase.openDatabase(sfile, passwd, null, 
				SQLiteDatabase.NO_LOCALIZED_COLLATORS);
		if(db != null){
			databases.put(dbfile, db);
		}
		return db;
	}
	
	/**
	 * 获取缓存中的数据库
	 * @param dbfile assets里的数据库路径
	 * */
	public SQLiteDatabase getDatabase(String dbfile) {
		SQLiteDatabase db = databases.get(dbfile);
		if(db != null && db.isOpen()){
			if(SQLiteDebug.isDebug) {
				Log.i(TAG, String.format("Return a database copy of %s", dbfile));
			}
			return (SQLiteDatabase) databases.get(dbfile);
		}
		return null;
	}
	
	private String getDatabaseFile(String dbfile){
		return databasepath + "/" + dbfile;
	}
	
	private boolean copyAssetsToFilesystem(String assetsSrc, String des){
		if(SQLiteDebug.isDebug) {
			Log.i(TAG, "Copy "+assetsSrc+" to "+des);
		}
		InputStream istream = null;
		OutputStream ostream = null;
		try{
			AssetManager am = context.getAssets();
			istream = am.open(assetsSrc);
			ostream = new FileOutputStream(des);
			byte[] buffer = new byte[1024 * 64];
	    	int length;
	    	while ((length = istream.read(buffer)) > 0){
	    		ostream.write(buffer, 0, length);
	    	}
	    	istream.close();
	    	ostream.close();
		}
		catch(Exception e){
			e.printStackTrace();
			try{
				if(istream!=null)
			    	istream.close();
				if(ostream!=null)
			    	ostream.close();
			}
			catch(Exception ee){
				ee.printStackTrace();
			}
			return false;
		}
		return true;
	}
	
	/**
	 * Close assets database
	 * @param dbfile, the assets file which will be closed soon
	 * @return, the status of this operating
	 */
	public boolean closeDatabase(String dbfile){
		if(databases.get(dbfile) != null){
			SQLiteDatabase db = (SQLiteDatabase) databases.get(dbfile);
			db.close();
			databases.remove(dbfile);
			return true;
		}
		return false;
	}
	
	/**
	 * Close all assets database
	 */
	static public void closeAllDatabase(){
		if(SQLiteDebug.isDebug) {
			Log.i(TAG, "closeAllDatabase");
		}
		if(mInstance != null){
			Set<String> set = mInstance.databases.keySet();
			Iterator<String> iterator = set.iterator();
			while(iterator.hasNext()) {
				String key = iterator.next();
				SQLiteDatabase db = mInstance.databases.get(key);
				if(db.isOpen()) {
					db.close();
				}
				iterator.remove();
			}
		}
	}
}
