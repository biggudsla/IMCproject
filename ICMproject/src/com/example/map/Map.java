package com.example.map;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.location.Address;
import android.location.Geocoder;
import android.net.Uri;
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
	    	
	    	mMap.addMarker(new MarkerOptions()
	    					.position(mLoc)
	    					.title("This will be ok")
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

                memo.setText("please");

                
                /**
                 * marker�� �����Ǵ� ������� m0, m1, m2... m �ڿ� �ٴ� ���ڵ��� ��Ŀ�� �ε����̰� �� �ε����� 
                 * areaList�� �ε����� �����Ƿ� ��Ŀ�� �ε����� �Ľ��ؼ� areaList�� �� �ε����� �����ϸ� ���������� ���� �� �ִ�.
                 */
                String id = marker.getId();
                int mIdx = Integer.parseInt( id.substring(1) ); // m �����ϰ� ��Ʈ���Ľ� ��Ʈ��.
 //               LatLng l = marker.getPosition();
                
                String mPath = mAreaList.get(mIdx).pathOfPic.get(0); // �ش� ��ġ�� ����� ���� ������ ù��° ������
                imgV.setImageURI(Uri.parse(mPath));
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
	    	
	    	if( !pList.get(i).LATI.isEmpty()) {	
	    		_lat = Double.parseDouble(pList.get(i).LATI);
	    		_longi = Double.parseDouble(pList.get(i).LONGI);
	    		_loc = new LatLng(_lat, _longi);
	    		picPath = pList.get(i).DATA;
	    		
	    		try {
					_list = gc.getFromLocation(_lat, _longi, 1);
					ad = _list.get(0);
					
					if( _list.get(0).getSubLocality() != null ) // ������, �� ���� �����ϸ� �� ������ ��
						local = _list.get(0).getSubLocality();
					else
						local = _list.get(0).getLocality(); //������ 

					//���⼭ ���� �ߺ� �˻��ؼ� area��ü�� �����ϴ��� �ƴϸ� 
					// �̹� ������ �������� �ּҸ� �ѱ��
					boolean isExisted = false;
					if(!AreaWithPic.areaList.isEmpty()) {
						for(int j=0; j<AreaWithPic.areaList.size(); j++) {
							isExisted = false;
							//���� �������� ������
							if(local.equals(AreaWithPic.areaList.get(j).local) ) { 
								//pic �ּҸ� ��
								AreaWithPic.areaList.get(j).pathOfPic.add(picPath);
								isExisted = true;
								break;
							}
						}
						if(!isExisted)	//���� �������� ������ �߰�
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

}
