package com.leon.sqlite_simple_demo;

import com.sqlcrypt.database.AssetsDatabaseManager;
import com.sqlcrypt.database.Cursor;
import com.sqlcrypt.database.sqlite.SQLiteDatabase;

import android.os.Bundle;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends Activity implements OnClickListener{
	
	private TextView textview;
	private EditText edittext;
	private Dialog dialog;
	
	private SQLiteDatabase mDB;
	private int type;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		findViewById(R.id.open).setOnClickListener(this);
		findViewById(R.id.resetpasswd).setOnClickListener(this);
		
		textview = (TextView)findViewById(R.id.result);
		AssetsDatabaseManager.initManager(this);
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		AssetsDatabaseManager.closeAllDatabase();
	}

	@Override
	public void onClick(View v) {
		int id = v.getId();
		switch(id) {
		case R.id.open:
			open();
			break;
		case R.id.resetpasswd:
			resetPassword();
			break;
		}
	}
	
	private void open() {
		type = 0;
		showDialog();
	}
	
	private void showData(String passwd) {
		AssetsDatabaseManager manager = AssetsDatabaseManager.getManager();
		try {
			if(mDB != null && mDB.isOpen()) {
				mDB.close();
			}
			mDB = manager.getDatabase("sample.sqlite", passwd);
		} catch(Exception e) { 
			e.printStackTrace();
			Toast.makeText(this, R.string.error_tip2, Toast.LENGTH_SHORT).show();
			return;
		}
		Cursor cursor = mDB.query("GeoCoordinate", null, null, null, null, null, null);
		StringBuilder builder = new StringBuilder();
		int size = cursor.getCount();
		builder.append("Size：" + size).append("\n");
		cursor.moveToFirst();
		while(!cursor.isAfterLast()) {
			String c1 = cursor.getColumnName(1);
			String c2 = cursor.getColumnName(2);
			String c3 = cursor.getColumnName(3);
			String c4 = cursor.getColumnName(4);
			String c5 = cursor.getColumnName(5);
			String c6 = cursor.getColumnName(6);
			
			String r1 = cursor.getString(cursor.getColumnIndex(c1));
			String r2 = cursor.getString(cursor.getColumnIndex(c2));
			String r3 = cursor.getString(cursor.getColumnIndex(c3));
			String r4 = cursor.getString(cursor.getColumnIndex(c4));
			String r5 = cursor.getString(cursor.getColumnIndex(c5));
			String r6 = cursor.getString(cursor.getColumnIndex(c6));
			builder.append(r1 + "|" + r2 + "|" + r3 + "|" + r4 + "|" + r5 + "|" + r6);
			cursor.moveToNext();
		}
		cursor.close();
		textview.setText(builder.toString());
	}
	
	private void resetPassword() {
		if(mDB == null || !mDB.isOpen()) {
			Toast.makeText(this, R.string.error_tip1, Toast.LENGTH_SHORT).show();
			return;
		}
		type = 1;
		showDialog();
	}
	
	private void showDialog() {
		if(dialog == null) {
			AlertDialog.Builder builder = new AlertDialog.Builder(this);
			View view = LayoutInflater.from(this).inflate(R.layout.dialog, null);
			edittext = (EditText) view.findViewById(R.id.edit_text);
			builder.setView(view);
			builder.setTitle(R.string.enter_password);
			builder.setPositiveButton(R.string.confirm, mListener);
			dialog = builder.create();
		}
		dialog.show();
	}
	
	DialogInterface.OnClickListener mListener = new DialogInterface.OnClickListener() {
		@Override
		public void onClick(DialogInterface dialog, int which) {
			String passwd = edittext.getText().toString();
			if(type == 0 ) {
				showData(passwd);
			} else if(type == 1) {
				mDB.resetPassword(passwd);
			}
		}
	};
}
