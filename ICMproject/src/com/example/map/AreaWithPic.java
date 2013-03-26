package com.example.map;

import java.util.ArrayList;

import android.location.Address;

import com.google.android.gms.maps.model.LatLng;

public class AreaWithPic {
	public static ArrayList<AreaWithPic> areaList = new ArrayList<AreaWithPic>();
	
	public ArrayList<String> pathOfPic;
	
	String addr = null;
	String admAr = null;
	String ctrCode = null;
	String ctrName = null;
	String local = null;
	
	LatLng loc = null;
	
	public AreaWithPic() {
		
	}
	
	public AreaWithPic(Address _ad, LatLng _loc, String _picPath) {
		pathOfPic = new ArrayList<String>();
		pathOfPic.add(_picPath);
		
		loc = _loc;
		
		addr = _ad.getAddressLine(0); // full _addr
		admAr = _ad.getAdminArea(); // ���
		ctrCode = _ad.getCountryCode(); // kr
		ctrName = _ad.getCountryName(); // ���ѹ�
		
		local = _ad.getSubLocality(); // ������, �� ���� �����ϸ� �� ������ ��
		if(local == null)		
			local = _ad.getLocality(); //������
		
/*		
		local = _ad.getLocality(); // ������ 
		if(local == null)
			local = _ad.getSubAdminArea(); //����� 
			*/
	}
}
