package com.example.oursource;

import java.util.ArrayList;

public class picture {
	public static ArrayList<picture> picturelist = new ArrayList<picture>();

	public String _ID = "";
	public String DATE = "";
	public String LATI = "";
	public String LONGI = "";
	public String DATA = "";
	public String BUCKET_NAME = "";
	public String DIARY = "";

	public picture(String id, String date, String lati, String longi,
			String data, String bucket) {
		_ID = id;
		DATE = date;
		LATI = lati;
		LONGI = longi;
		DATA = data;
		BUCKET_NAME = bucket;
	}

	public static ArrayList<picture> getlist() {
		return picturelist;
	}

}
