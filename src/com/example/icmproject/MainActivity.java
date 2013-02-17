package com.example.icmproject;

import com.example.oursource.picture;
import com.example.oursource.Ourdb;

import android.os.Bundle;
import android.provider.MediaStore;
import android.app.Activity;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.view.Menu;
import android.view.View;

public class MainActivity extends Activity {

	Ourdb mHelper;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		initialization();	
		}

		public void monclick(View v) {
		/*
		 ����ٰ� ����Ʈ �Ἥ ������ �ѱ�� ���� ���� �Һκ� �����غ����� ����.
		 
		  ���� �����
		 import com.example.oursource.picture; �̰� ����Ʈ�ϰ� 
		  
		 for (int h = 0; h < picture.picturelist.size(); h++) {
			if (picture.picturelist.get(h).BUCKET_NAME=="3��8��") {
				
			}
		}
		�̷�������
			 */
	}

	public void initialization(){
			String[] proj = { MediaStore.Images.Media.LATITUDE,
					MediaStore.Images.Media.LONGITUDE,
					MediaStore.Images.Media._ID,
					MediaStore.Images.Media.BUCKET_DISPLAY_NAME,
					MediaStore.Images.Media.DATE_TAKEN,
					MediaStore.Images.Media.DATA };

			Cursor imageCursor = getContentResolver().query(
					MediaStore.Images.Media.EXTERNAL_CONTENT_URI, proj, null,
					null, null);

			if (imageCursor != null && imageCursor.moveToFirst()) {
				String bucketname;
				String idname;
				String latiname;
				String longiname;
				String dataname;
				String datename;

				int thumbsBucketCol = imageCursor
						.getColumnIndex(MediaStore.Images.Media.BUCKET_DISPLAY_NAME);
				int thumbsDateCol = imageCursor
						.getColumnIndex(MediaStore.Images.Media.DATE_TAKEN);
				int thumbsIdCol = imageCursor
						.getColumnIndex(MediaStore.Images.Media._ID);
				int thumbsDataCol = imageCursor
						.getColumnIndex(MediaStore.Images.Media.DATA);
				int thumbsLatiCol = imageCursor
						.getColumnIndex(MediaStore.Images.Media.LATITUDE);
				int thumbsLongiCol = imageCursor
						.getColumnIndex(MediaStore.Images.Media.LONGITUDE);
				
				picture.picturelist.clear();
				do {
					bucketname = imageCursor.getString(thumbsBucketCol);
					idname = imageCursor.getString(thumbsIdCol);
					dataname = imageCursor.getString(thumbsDataCol);
					datename = imageCursor.getString(thumbsDateCol);
					latiname = imageCursor.getString(thumbsLatiCol);
					longiname = imageCursor.getString(thumbsLongiCol);

					if (idname != null) {
						picture.picturelist.add(new picture(idname, datename,
								latiname, longiname, dataname, bucketname));

						}
				} while (imageCursor.moveToNext());

			}

			imageCursor.close();
			
			mHelper = new Ourdb(this);
			SQLiteDatabase db = mHelper.getWritableDatabase();
			Cursor cursor;
			cursor = db
					.rawQuery("SELECT pictureid,diary,lati,longi FROM db1", null);
			while (cursor.moveToNext()) {
				for (int e = 0; e < picture.picturelist.size(); e++) {
					if (picture.picturelist.get(e)._ID.equals(cursor.getString(0))) {
						picture.picturelist.get(e).DIARY = cursor.getString(1);
						picture.picturelist.get(e).LATI = cursor.getString(2);
						picture.picturelist.get(e).LONGI = cursor.getString(3);

						break;
					}
				}
			}
			cursor.close();
			mHelper.close();
			
			return;
		}
		
			
		
		@Override
		public boolean onCreateOptionsMenu(Menu menu) {
			// Inflate the menu; this adds items to the action bar if it is present.
			getMenuInflater().inflate(R.menu.activity_main, menu);
			return true;
		}

	}