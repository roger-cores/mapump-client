package com.vosaye.mapump;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.GoogleApiClient.ConnectionCallbacks;
import com.google.android.gms.common.api.GoogleApiClient.OnConnectionFailedListener;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMap.OnCameraChangeListener;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.MarkerOptions;
import com.vosaye.mapump.transfer_objects.RequestTO;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.Align;
import android.graphics.Paint.Style;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.widget.Toast;
import android.widget.ToggleButton;

public class MainActivity extends ActionBarActivity
		implements LocationListener, ConnectionCallbacks, OnConnectionFailedListener {
	
	
	
	GoogleMap googleMap;
	Double latitude, longitude, latCenter, lonCenter;
	String url = "http://mapump.whelastic.net:8080/MapumpServerWeb/api/mapump-service/get-stations-near-by";
	Location location;
	AlertDialog dialog;
	GoogleApiClient mGoogleApiClient;
	List<String> filterByCompanies = new ArrayList<String>();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		googleMap = ((MapFragment) getFragmentManager().findFragmentById(R.id.map)).getMap();

		googleMap.setMyLocationEnabled(true);


		
		googleMap.setOnCameraChangeListener(new OnCameraChangeListener() {

			@Override
			public void onCameraChange(CameraPosition cameraPosition) {
				LatLng centerPosition = cameraPosition.target;
				latCenter = centerPosition.latitude;
				lonCenter = centerPosition.longitude;
				new HttpAsyncTask().execute(url, centerPosition.latitude, centerPosition.longitude, 111D, 105D, 5D, filterByCompanies);
				
			}
		});
		
		

		
		Location location = getLastKnownLocation();
		if (location != null) {
			onLocationChanged(location);
		}
		

		filterByCompanies.add("IOCL");
		filterByCompanies.add("RCL");
		filterByCompanies.add("BPCL");
		filterByCompanies.add("HPCL");

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();

		return super.onOptionsItemSelected(item);
	}

	@Override
	public void onLocationChanged(Location location) {
		latitude = location.getLatitude();
		longitude = location.getLongitude();
		latCenter = latitude;
		lonCenter = longitude;
		LatLng latLng = new LatLng(latitude, longitude);
		googleMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
		googleMap.animateCamera(CameraUpdateFactory.zoomTo(15));

		dialog = new AlertDialog.Builder(this).setMessage("Loading").setCancelable(false).create();
		dialog.show();
		new HttpAsyncTask().execute(url, latitude, longitude, 111D, 105D, 5D, filterByCompanies);
		

	}

	@Override
	public void onStatusChanged(String provider, int status, Bundle extras) {

	}

	@Override
	public void onProviderEnabled(String provider) {

	}

	@Override
	public void onProviderDisabled(String provider) {

	}

	public void onCompanyChange(View view) {
		try {
			ToggleButton toggleButton = (ToggleButton) view;
			switch (toggleButton.getId()) {
			case R.id.bpcl:
				if (toggleButton.isChecked() && !filterByCompanies.contains("BPCL"))
					filterByCompanies.add("BPCL");
				else
					filterByCompanies.remove("BPCL");
				break;
			case R.id.iocl:
				if (toggleButton.isChecked() && !filterByCompanies.contains("IOCL"))
					filterByCompanies.add("IOCL");
				else
					filterByCompanies.remove("IOCL");
				break;
			case R.id.rcl:
				if (toggleButton.isChecked() && !filterByCompanies.contains("RCL"))
					filterByCompanies.add("RCL");
				else
					filterByCompanies.remove("RCL");
				break;
			case R.id.hpcl:
				if (toggleButton.isChecked() && !filterByCompanies.contains("HPCL"))
					filterByCompanies.add("HPCL");
				else
					filterByCompanies.remove("HPCL");
			}
			dialog = new AlertDialog.Builder(this).setMessage("Loading").setCancelable(false).create();
			dialog.show();
			if(latCenter != null && lonCenter != null)
			new HttpAsyncTask().execute(url, latCenter, lonCenter, 111D, 105D, 5D, filterByCompanies);
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			Toast.makeText(MainActivity.this, e.getMessage(), Toast.LENGTH_LONG).show();
			Toast.makeText(MainActivity.this, e.getCause().getMessage(), Toast.LENGTH_LONG).show();
			e.printStackTrace();
		}

	}

	public static String GET(String url) {
		InputStream inputStream = null;
		String result = "";
		try {

			// create HttpClient
			HttpClient httpclient = new DefaultHttpClient();

			// make GET request to the given URL
			HttpResponse httpResponse = httpclient.execute(new HttpGet(url));

			// receive response as inputStream
			inputStream = httpResponse.getEntity().getContent();

			// convert inputstream to string
			if (inputStream != null)
				result = convertInputStreamToString(inputStream);
			else
				result = "Did not work!";

		} catch (Exception e) {
			Log.d("InputStream", e.getLocalizedMessage());
		}

		return result;
	}

	public static String PUT(String url, RequestTO request) {
		InputStream inputStream = null;
		String result = "[]";
		com.vosaye.mapump.transfer_objects.Location location = request.getLocation();
		try {

			// create HttpClient
			HttpClient httpclient = new DefaultHttpClient();

			// make GET request to the given URL

			HttpPut putRequest = new HttpPut(url);
			putRequest.addHeader("Content-Type", "application/json");
			putRequest.addHeader("Accept", "application/json");

			JSONObject requestJson = new JSONObject();
			JSONObject locationJson = new JSONObject();
			locationJson.put("latitude", location.getLatitude());
			locationJson.put("longitude", location.getLongitude());

			requestJson.put("location", locationJson);

			requestJson.put("latKm", request.getLatKm());
			requestJson.put("lonKm", request.getLonKm());
			requestJson.put("rangeKm", request.getRangeKm());

			JSONArray jsonArrayFilterCompanies = new JSONArray(request.getFilterByCompanies());

			requestJson.put("filterByCompanies", jsonArrayFilterCompanies);

			putRequest.setEntity(new StringEntity(requestJson.toString()));

			HttpResponse httpResponse = httpclient.execute(putRequest);

			// receive response as inputStream
			inputStream = httpResponse.getEntity().getContent();

			// convert inputstream to string
			if (inputStream != null)
				result = convertInputStreamToString(inputStream);

		} catch (Exception e) {
			Log.d("InputStream", e.getLocalizedMessage());
		}

		return result;
	}
	
	public LatLngBounds getVisibleMapBounds(){
		LatLngBounds.Builder builder = new LatLngBounds.Builder();
		if(googleMap != null)
			return googleMap.getProjection().getVisibleRegion().latLngBounds;
		
		
		return builder.build();
	}

	private static String convertInputStreamToString(InputStream inputStream) throws IOException {
		BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
		String line = "";
		String result = "";
		while ((line = bufferedReader.readLine()) != null)
			result += line;

		inputStream.close();
		return result;

	}

	private class HttpAsyncTask extends AsyncTask<Object, Void, String> {
		@Override
		protected String doInBackground(Object... params) {

			try {
				return PUT(params[0].toString(),
						new RequestTO(
								new com.vosaye.mapump.transfer_objects.Location((Double) params[1],
										(Double) (params[2])),
								(Double) (params[3]), (Double) (params[4]), (Double) (params[5]),
								(List<String>) params[6]));
			} catch (Exception e) {
				e.printStackTrace();
				return "";
			}
		}

		DecimalFormat df = new DecimalFormat("#.0"); 
		// onPostExecute displays the results of the AsyncTask.
		@SuppressWarnings("deprecation")
		@Override
		protected void onPostExecute(String result) {
			googleMap.clear();
			
			
			int count = 0;
			try {

				JSONArray jsonArray = new JSONArray(result);
				if(jsonArray==null) return;
				// Toast.makeText(getBaseContext(), "Received!" +
				// jsonArray.length(), Toast.LENGTH_LONG).show();
//				LatLngBounds.Builder builder = new LatLngBounds.Builder();
				for (int i = 0; i < jsonArray.length(); i++) {
					JSONObject jsonObject = jsonArray.getJSONObject(i);
					try {
						double lat = jsonObject.getDouble("latitude");
						double lng = jsonObject.getDouble("longitude");

						
						Bitmap.Config conf = Bitmap.Config.ARGB_8888; 
						Bitmap bmp = Bitmap.createBitmap(200, 50, conf); 
						Canvas canvas = new Canvas(bmp);

						
						String company = jsonObject.getString("company");

						if(company.equalsIgnoreCase("IOCL")){
							googleMap.addMarker(new MarkerOptions().position(new LatLng(lat,lng)).title(jsonObject.getString("address")).icon(BitmapDescriptorFactory.fromBitmap((writeTextOnDrawable(R.drawable.smlorange, df.format(jsonObject.getDouble("price"))+"")))));
							
						}
						else if(company.equalsIgnoreCase("BPCL")){
							googleMap.addMarker(new MarkerOptions().position(new LatLng(lat,lng)).title(jsonObject.getString("address")).icon(BitmapDescriptorFactory.fromBitmap((writeTextOnDrawable(R.drawable.smlyellow, df.format(jsonObject.getDouble("price"))+"")))));
							
						}
						else if(company.equalsIgnoreCase("HPCL")){
							googleMap.addMarker(new MarkerOptions().position(new LatLng(lat,lng)).title(jsonObject.getString("address")).icon(BitmapDescriptorFactory.fromBitmap((writeTextOnDrawable(R.drawable.smlred, df.format(jsonObject.getDouble("price"))+"")))));
							
						}
						else{
							googleMap.addMarker(new MarkerOptions().position(new LatLng(lat,lng)).title(jsonObject.getString("address")).icon(BitmapDescriptorFactory.fromBitmap((writeTextOnDrawable(R.drawable.smlblue, df.format(jsonObject.getDouble("price"))+"")))));
							
						}
						
						
						

						
						
						
						count++;
//						builder.include(marker.getPosition());

					} catch (Exception e) {
						e.printStackTrace();
					}

				}

//				Cannot animate zoom to include all markers because position isn't static
//				if (count != 0) {
//					builder.include(new LatLng(latitude, longitude));
//					LatLngBounds bounds = builder.build();
//					int padding = 100; // offset from edges of the map in pixels
//					CameraUpdate cu = CameraUpdateFactory.newLatLngBounds(bounds, padding);
//
//					googleMap.animateCamera(cu);
//				}
				
				
			} catch (JSONException e) {
				e.printStackTrace();
				Toast.makeText(getBaseContext(), "Received error", Toast.LENGTH_LONG).show();
				Toast.makeText(getBaseContext(), e.getMessage(), Toast.LENGTH_LONG).show();
				Toast.makeText(getBaseContext(), e.getCause().getMessage(), Toast.LENGTH_LONG).show();
			} catch (Exception e) {
				e.printStackTrace();
				Toast.makeText(getBaseContext(), e.getMessage(), Toast.LENGTH_LONG).show();
				Toast.makeText(getBaseContext(), e.getCause().getMessage(), Toast.LENGTH_LONG).show();
			}

			if(dialog!=null)
			dialog.dismiss();

		}
	}
	
	
	public static Bitmap createDrawableFromView(Context context, View view) {
		DisplayMetrics displayMetrics = new DisplayMetrics();
		((Activity) context).getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
		view.setLayoutParams(new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));
		view.measure(displayMetrics.widthPixels, displayMetrics.heightPixels);
		view.layout(0, 0, displayMetrics.widthPixels, displayMetrics.heightPixels);
		view.buildDrawingCache();
		Bitmap bitmap = Bitmap.createBitmap(view.getMeasuredWidth(), view.getMeasuredHeight(), Bitmap.Config.ARGB_8888);
 
		Canvas canvas = new Canvas(bitmap);
		view.draw(canvas);
 
		return bitmap;
	}
	

	@Override
	public void onConnectionFailed(ConnectionResult arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onConnected(Bundle arg0) {
		// TODO Auto-generated method stub
		if (mGoogleApiClient == null)
			buildGoogleApiClient();
		location = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);
		if (location != null) {
			latitude = location.getLatitude();
			longitude = location.getLongitude();
		}

	}

	@Override
	public void onConnectionSuspended(int arg0) {
		// TODO Auto-generated method stub

	}

	protected synchronized void buildGoogleApiClient() {
		mGoogleApiClient = new GoogleApiClient.Builder(this).addConnectionCallbacks(this)
				.addOnConnectionFailedListener(this).addApi(LocationServices.API).build();
	}
	
	
	LocationManager mLocationManager;

	private Location getLastKnownLocation() {
	    mLocationManager = (LocationManager)getSystemService(LOCATION_SERVICE);
	    List<String> providers = mLocationManager.getProviders(true);
	    Location bestLocation = null;
	    for (String provider : providers) {
	        Location l = mLocationManager.getLastKnownLocation(provider);
	        if (l == null) {
	            continue;
	        }
	        if (bestLocation == null || l.getAccuracy() < bestLocation.getAccuracy()) {
	            // Found best last known location: %s", l);
	            bestLocation = l;
//	    	    mLocationManager.requestLocationUpdates(provider, 20000, 0, this);
	        }
	    }
	    
	    
	    return bestLocation;
	}
	
	
	private Bitmap writeTextOnDrawable(int drawableId, String text) {

	    Bitmap bm = BitmapFactory.decodeResource(getResources(), drawableId)
	            .copy(Bitmap.Config.ARGB_8888, true);

	    Typeface tf = Typeface.create("Helvetica", Typeface.BOLD);

	    Paint paint = new Paint();
	    paint.setStyle(Style.FILL);
	    paint.setColor(Color.WHITE);
	    paint.setFakeBoldText(true);
	    paint.setTypeface(tf);
	    paint.setTextAlign(Align.CENTER);
	    paint.setTextSize(convertToPixels(this, 11));

	    Rect textRect = new Rect();
	    paint.getTextBounds(text, 0, text.length(), textRect);

	    Canvas canvas = new Canvas(bm);

	    //If the text is bigger than the canvas , reduce the font size
	    if(textRect.width() >= (canvas.getWidth() - 4))     //the padding on either sides is considered as 4, so as to appropriately fit in the text
	        paint.setTextSize(convertToPixels(this, 7));        //Scaling needs to be used for different dpi's

	    //Calculate the positions
	    int xPos = (canvas.getWidth() / 2) - 2;     //-2 is for regulating the x position offset

	    //"- ((paint.descent() + paint.ascent()) / 2)" is the distance from the baseline to the center.
	    int yPos = (int) ((canvas.getHeight() / 2) - (canvas.getHeight() / 4) - ((paint.descent() + paint.ascent()) / 2)) ;  

	    canvas.drawText(text, xPos, yPos, paint);

	    return  bm;
	}



	public static int convertToPixels(Context context, int nDP)
	{
	    final float conversionScale = context.getResources().getDisplayMetrics().density;

	    return (int) ((nDP * conversionScale) + 0.5f) ;

	}

}
