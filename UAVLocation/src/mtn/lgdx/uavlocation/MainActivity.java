package mtn.lgdx.uavlocation;

import java.io.UnsupportedEncodingException;
import java.util.List;

import mtn.lgdx.httpgps.R;

import org.apache.http.Header;
import org.apache.http.entity.StringEntity;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.loopj.android.http.TextHttpResponseHandler;

import android.R.string;
import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends Activity implements OnClickListener {
	
	//private EditText url;
	//private Button send;
	private TextView show_gps,from_server;
	private final String ACTION = "mtn.lgdx.httpgps.GpsService";
	
    private LocationManager locationManager;
    private Location location = null;
    private final Criteria criteria = new Criteria();
    private static int minUpdateTime = 1000;		//最小更新时间  ms
    private static int minUpdateDistance = 1; 		//最小更新距离  m
    private double latitude 	= 0.0;				//纬度
    private double longitude 	= 0.0;				//经度
    private double speed 		= 0.0;				//速度
    private double altitude 	= 0.0;				//高度
    private double bearing 		= 0.0;				//方向
    
    private Intent gpsServiceIntent;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		//url = (EditText) findViewById(R.id.url);
		//send = (Button) findViewById(R.id.send_gps);
		show_gps = (TextView) findViewById(R.id.show_gps);
		from_server = (TextView) findViewById(R.id.from_server);
		
		gpsServiceIntent = new Intent(this,GpsService.class);
		startService(gpsServiceIntent);		//启动GPS服务
		
		registerReceiver(gpsBroadcastReceiver, new IntentFilter(ACTION));
		
		from_server.append("服务器返回的数据:\n");
		//send.setOnClickListener(this);
		/*
        //获得对Location Manager的引用
        locationManager = (LocationManager)getSystemService(Context.LOCATION_SERVICE);
        //指定Location Provider的条件
        criteria.setAccuracy(Criteria.ACCURACY_FINE);				//高精度
        criteria.setPowerRequirement(Criteria.POWER_LOW);			//低耗电量
        criteria.setAltitudeRequired(true);							//需要海拔高度
        criteria.setBearingRequired(true);							//需要方向
        criteria.setSpeedRequired(true);							//需要速度
        criteria.setCostAllowed(true);								//允许服务提供商收取费用
        criteria.setHorizontalAccuracy(Criteria.ACCURACY_HIGH);		//水平方向(经纬度)要求高精度
        criteria.setVerticalAccuracy(Criteria.ACCURACY_HIGH);		//垂直方向(海拔)要求高精度
        criteria.setBearingAccuracy(Criteria.ACCURACY_HIGH);		//方向要求高精度
        criteria.setSpeedAccuracy(Criteria.ACCURACY_HIGH);			//速度要求高精度
        
        String provider = locationManager.getBestProvider(criteria, true);
        location = locationManager.getLastKnownLocation(provider);
        if (provider != null) {
        	locationManager.requestLocationUpdates(provider, minUpdateTime, minUpdateDistance, new LocationListener() {
				
				public void onStatusChanged(String provider, int status, Bundle extras) {
					// TODO Auto-generated method stub
					
				}
				
				public void onProviderEnabled(String provider) {
					// TODO Auto-generated method stub
					sendGPS(locationManager.getLastKnownLocation(provider));
				}
				
				public void onProviderDisabled(String provider) {
					// TODO Auto-generated method stub
					
				}
				
				public void onLocationChanged(Location location) {
					// TODO Auto-generated method stub
					sendGPS(location);

				}
			});
		}else {
			Toast.makeText(getApplicationContext(), "No Location Providers available", Toast.LENGTH_SHORT).show();
		}
		*/
	}
	
	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
	}
	
	@Override
	protected void onStop() {
		// TODO Auto-generated method stub
		super.onStop();
	}
	
	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		unregisterReceiver(gpsBroadcastReceiver);
		stopService(gpsServiceIntent);
	}
	
	/*
	private void sendGPS(Location location){
		if (location != null) {
			latitude 	= location.getLatitude();
			longitude 	= location.getLongitude();
			altitude 	= location.getAltitude();
			speed 		= location.getSpeed();
			bearing 	= location.getBearing();
			
			StringBuilder stringBuilder = new StringBuilder();
			stringBuilder.append("实时位置信息：\n");
			stringBuilder.append("经度： ");
			stringBuilder.append(longitude+"\n");
			stringBuilder.append("纬度： ");
			stringBuilder.append(latitude+"\n");
			stringBuilder.append("海拔： ");
			stringBuilder.append(altitude+"\n");
			stringBuilder.append("速度： ");
			stringBuilder.append(speed+"\n");
			stringBuilder.append("方向： ");
			stringBuilder.append(bearing+"\n");
			show_gps.setText(stringBuilder.toString());
			
				AsyncHttpClient client = new AsyncHttpClient();
				RequestParams params = new RequestParams();

				params.put("longitude", ""+longitude);
				params.put("latitude", ""+latitude);
				params.put("altitude", ""+altitude);
				params.put("speed", ""+speed);
				params.put("bearing", ""+bearing);
				
				client.get(url.getText().toString(), params, new TextHttpResponseHandler() {
					
					@Override
					public void onSuccess(int arg0, Header[] arg1, String arg2) {
						// TODO Auto-generated method stub
						from_server.append(arg2+"\n");
					}
					
					@Override
					public void onFailure(int arg0, Header[] arg1, String arg2, Throwable arg3) {
						// TODO Auto-generated method stub
						
					}
				});					
				
		}else {
			Toast.makeText(MainActivity.this, "Invalid location", Toast.LENGTH_SHORT).show();
			show_gps.setText("");
		}
		
	}
*/
	public void onClick(View v) {
		//sendGPS(location);
	}
	
	private final BroadcastReceiver gpsBroadcastReceiver = new BroadcastReceiver() {
		
		@Override
		public void onReceive(Context context, Intent intent) {
			//if (intent.getAction().equals(ACTION)) {
				StringBuilder stringBuilder = new StringBuilder();
				stringBuilder.append("实时位置信息：\n");
				stringBuilder.append("经度： "+intent.getStringExtra("longitude"));
				stringBuilder.append("纬度： "+intent.getStringExtra("latitude"));
				stringBuilder.append("海拔： "+intent.getStringExtra("altitude"));
				stringBuilder.append("速度： "+intent.getStringExtra("speed"));
				stringBuilder.append("方向： "+intent.getStringExtra("bearing"));
				
				show_gps.setText(stringBuilder.toString());
				
				from_server.append(intent.getStringExtra("from_server"));
			//}else {
			//	Toast.makeText(MainActivity.this, "Action is not equal", Toast.LENGTH_SHORT).show();
			//}
		}
	};
}



