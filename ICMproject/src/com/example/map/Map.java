package com.example.map;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.BitmapFactory.Options;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.icmproject.R;
import com.example.oursource.picture;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMap.InfoWindowAdapter;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.UiSettings;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

public class Map extends Activity {
	
	private Context mCtx;
	private GoogleMap mMap;
	ArrayList<AreaWithPic> mAreaList;

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
	    super.onCreate(savedInstanceState);
	    setContentView(R.layout.map);
	    
	    
	  
	    /**
	     * Setting a Map
	     */
	    mMap = ((MapFragment) getFragmentManager().findFragmentById(R.id.map)).getMap();
	    mMap.setMyLocationEnabled(true);
//	    mMap.setTrafficEnabled(true);
	    setupMapView();
	    
	    // Finding the photos with GPS and Init AreaWithPic
	    InitArea();
	    
	    mAreaList = AreaWithPic.areaList;
	    int idx = 0;
	    LatLng mLoc;
	    
	    while(idx < mAreaList.size()) {
	    	
	    	mLoc = mAreaList.get(idx).loc;
	    	String memo = mAreaList.get(idx).addr;
	    	
	    	mMap.addMarker(new MarkerOptions()
	    					.position(mLoc)
	    					.title(memo)
	    					);
	    	idx++;
	    }
	    
	    mMap.setInfoWindowAdapter(new InfoWindowAdapter() {

			@Override
			public View getInfoContents(Marker arg0) {
				// TODO Auto-generated method stub
				return null;
			}

			@Override
			public View getInfoWindow(Marker marker) {
				// TODO Auto-generated method stub
				View v = getLayoutInflater().inflate(R.layout.map_info_window, null);

                // Getting reference to the TextView to set title
                TextView memo = (TextView) v.findViewById(R.id.memo);
                ImageView imgV = (ImageView) v.findViewById(R.id.photo);

                memo.setText(marker.getTitle());

                
                /**
                 * marker는 생성되는 순서대로 m0, m1, m2... m 뒤에 붙는 숫자들은 마커의 인덱스이고 이 인덱스는 
                 * areaList의 인덱스와 같으므로 마커의 인덱스를 파싱해서 areaList에 그 인덱스로 접근하면 지역정보를 얻을 수 있다.
                 */
                String id = marker.getId();
                int mIdx = Integer.parseInt( id.substring(1) ); // m 제외하고 스트링파싱 인트파.
 //               LatLng l = marker.getPosition();
                
                String mPath = mAreaList.get(mIdx).pathOfPic.get(0); // 해당 위치에 저장된 많은 사진중 첫번째 사진주
                Bitmap bitm = decodeBitmapFromStringpath(mPath, 100, 100);
                imgV.setImageBitmap(bitm);
//                imgV.setImageURI(Uri.parse(mPath));
                // Returning the view containing InfoWindow contents
                return v;
			}
	    });
	    // TODO Auto-generated method stub
	}
	
	private void setupMapView(){
		 UiSettings settings = mMap.getUiSettings();        
		 mMap.animateCamera(CameraUpdateFactory.newCameraPosition(
		  new CameraPosition(new LatLng(37.0, 126.5), 
		  7.5f, 30f, 112.5f))); // zoom, tilt, bearing
		 mMap.setTrafficEnabled(true);
		 settings.setAllGesturesEnabled(true);
		 settings.setCompassEnabled(true);
		 settings.setMyLocationButtonEnabled(true);
		 settings.setRotateGesturesEnabled(true);
		 settings.setScrollGesturesEnabled(true);
		 settings.setTiltGesturesEnabled(true);
		 settings.setZoomControlsEnabled(true);
		 settings.setZoomGesturesEnabled(true);
	}
	
	public void InitArea() {
		
		ArrayList<picture> pList = picture.picturelist;
	    double _lat, _longi;
	    LatLng _loc;
	    // not sure about this 
	    Geocoder gc = new Geocoder(this);
	    
	    List<Address> _list;
	    Address ad;
	    String local, sublocal;
	    String picPath;
	    
	    for(int i=0; i<pList.size(); i++) {
	    	
	    	if( pList.get(i).LATI != null) {	
	    		_lat = Double.parseDouble(pList.get(i).LATI);
	    		_longi = Double.parseDouble(pList.get(i).LONGI);
	    		_loc = new LatLng(_lat, _longi);
	    		picPath = pList.get(i).DATA;
	    		
	    		try {
					_list = gc.getFromLocation(_lat, _longi, 1);
					ad = _list.get(0);
					
					if( _list.get(0).getSubLocality() != null ) // 마포구, 구 단위 존재하면 구 단위로 분
						local = _list.get(0).getSubLocality();
					else
						local = _list.get(0).getLocality(); //군포시 

					//여기서 지역 중복 검사해서 area객체를 생성하던지 아니면 
					// 이미 기존의 지역구에 주소만 넘기던
					boolean isExisted = false;
					if(!AreaWithPic.areaList.isEmpty()) {
						for(int j=0; j<AreaWithPic.areaList.size(); j++) {
							isExisted = false;
							//같은 지역구가 있으면
							if(local.equals(AreaWithPic.areaList.get(j).local) ) { 
								//pic 주소만 추
								AreaWithPic.areaList.get(j).pathOfPic.add(picPath);
								isExisted = true;
								break;
							}
						}
						if(!isExisted)	//같은 지역구가 없으면 추가
							AreaWithPic.areaList.add(new AreaWithPic(ad, _loc, picPath));
					}
					
					else
						AreaWithPic.areaList.add(new AreaWithPic(ad, _loc, picPath));

				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}	    		
	    	}
	    }
	}
	
	public Bitmap decodeBitmapFromStringpath(String filePath, int reqWidth, int reqHeight){
		//First convert string str to Filepath
//		String filePath = getFilePathfromUri(Uri.parse(str));			
		
		//Second decode with inJustDecodeBounds=true to check dimensions
		BitmapFactory.Options options = new BitmapFactory.Options();
		options.inJustDecodeBounds = true;
		// to get options.outwidth... these are just dimensions of bitmap
		BitmapFactory.decodeFile(filePath, options);
		
		//Calculate inSampleSize
		options.inSampleSize = calculateInSampleSize(options, reqWidth, reqHeight);
		options.inJustDecodeBounds = false; // means return BITMAP!!!			
		return BitmapFactory.decodeFile(filePath, options);
	}	
	
	 private int calculateInSampleSize(Options options, int reqWidth,
				int reqHeight) {
			// TODO Auto-generated method stub
			// to set dimensions with unprocessed bitmaps dimensions
			final int height = options.outHeight;
			final int width = options.outWidth;
			int inSampleSize = 1;
			
			if (height > reqHeight || width > reqWidth){
				if(width > height){ // 작은 크기에 맞춰서 줄이는데 왜? // 큰키게에 맞춰서 줄여야지 여백이 생기더라도 이미지가 잘리지 않을 텐데
					//600,800경우와 300, 80 경우를 생각을 해보면 req100,100일때... 맞는거 같은데 궁금
					//그래서 내 생각대로 바꿈
					inSampleSize = Math.round((float)width / (float)reqWidth);					
				} else{
					inSampleSize = Math.round((float)height / (float)reqHeight);
				}
			}			
			return inSampleSize;
		}
}
