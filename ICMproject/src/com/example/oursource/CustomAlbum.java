package com.example.oursource;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.BitmapFactory.Options;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.icmproject.R;

public class CustomAlbum extends Activity {
	
	ArrayList<String> imgList;

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
	    super.onCreate(savedInstanceState);
	    setContentView(R.layout.customalbum);
	    
	    Log.e("d", "dd");
	    
	    imgList = getIntent().getStringArrayListExtra("pathOfPic");
	 
	    GridView gridview = (GridView) findViewById(R.id.gridview);
	    gridview.setAdapter(new ImageAdapter(getApplicationContext()));
	    
	    gridview.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View v, int position,
					long id) {
				// TODO Auto-generated method stub
				Toast.makeText(CustomAlbum.this, "" + position, Toast.LENGTH_SHORT).show();	
			}
	    	
	    });
	
	    // TODO Auto-generated method stub
	}
	
	public class ImageAdapter extends BaseAdapter {
		private Context mContext;
		
		public ImageAdapter(Context c) {
			mContext = c;
		}

		public int getCount() {
			// TODO Auto-generated method stub
			return imgList.size();
		}

		public Object getItem(int position) {
			// TODO Auto-generated method stub
			return null;
		}

		public long getItemId(int position) {
			// TODO Auto-generated method stub
			return 0;
		}

		public View getView(int position, View convertView, ViewGroup parent) {
			// TODO Auto-generated method stub
			ImageView imageView;
			if(convertView == null){
				imageView = new ImageView(mContext);
				imageView.setLayoutParams(new GridView.LayoutParams(85,85));
				imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
				imageView.setPadding(8,  8, 8, 8);
			} else {
				imageView = (ImageView) convertView;
			}
						
		/*	//////////////////////////////////////////// 
			1�� ������ ����� ���´�. 2������ out of memory...*/	
			
			// ��Ʈ���� ����� ���̴� decode�� �ؼ� imageView�� set ���״�. �ɱ� �ȵɱ� �̵� �غ���!
			imageView.setImageBitmap( decodeBitmapFromStringpath(imgList.get(position), 200, 200) );
			// imgList���� ������ ���� string������ uri�ּ��ε� decodeFile������ �� ���� �ȳ�.��Ű �����غ���			
		/*//////////////////////////////////////////// */	
			
			
//			imageView.setImageURI(Uri.parse(imgList.get(position)));
			return imageView;			
		}
		
		public Bitmap decodeBitmapFromStringpath(String path, int reqWidth, int reqHeight){
			//First convert string str to Filepath
			
			//Second decode with inJustDecodeBounds=true to check dimensions
			BitmapFactory.Options options = new BitmapFactory.Options();
			options.inJustDecodeBounds = true;
			// to get options.outwidth... these are just dimensions of bitmap
			BitmapFactory.decodeFile(path, options);
			
			//Calculate inSampleSize
			options.inSampleSize = calculateInSampleSize(options, reqWidth, reqHeight);
			options.inJustDecodeBounds = false; // means return BITMAP!!!			
			return BitmapFactory.decodeFile(path, options);
		}
        
		private int calculateInSampleSize(Options options, int reqWidth,
				int reqHeight) {
			// TODO Auto-generated method stub
			// to set dimensions with unprocessed bitmaps dimensions
			final int height = options.outHeight;
			final int width = options.outWidth;
			int inSampleSize = 1;
			
			if (height > reqHeight || width > reqWidth){
				if(width > height){ // ���� ũ�⿡ ���缭 ���̴µ� ��? // ūŰ�Կ� ���缭 �ٿ����� ������ ������� �̹����� �߸��� ���� �ٵ�
					//600,800���� 300, 80 ��츦 ������ �غ��� req100,100�϶�... �´°� ������ �ñ�
					//�׷��� �� ������� �ٲ�
					inSampleSize = Math.round((float)width / (float)reqWidth);					
				} else{
					inSampleSize = Math.round((float)height / (float)reqHeight);
				}
			}			
			return inSampleSize;
		}
	}
}
