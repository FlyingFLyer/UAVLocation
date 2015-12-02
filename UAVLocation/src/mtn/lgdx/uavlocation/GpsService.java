package mtn.lgdx.uavlocation;

import org.apache.http.Header;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.RequestParams;
import com.loopj.android.http.TextHttpResponseHandler;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.IBinder;
import android.widget.Toast;

public class GpsService extends Service {
	
    private LocationManager locationManager;
    private Location location = null;
    private final Criteria criteria = new Criteria();
    private static int minUpdateTime = 1000;		//��С����ʱ��  ms
    private static int minUpdateDistance = 1; 		//��С���¾���  m
    private double latitude 	= 0.0;				//γ��
    private double longitude 	= 0.0;				//����
    private double speed 		= 0.0;				//�ٶ�
    private double altitude 	= 0.0;				//�߶�
    private double bearing 		= 0.0;				//����
    private final String URL = "http://uavuav.oicp.net/gps/get_gps.php";	//�������Ͻ���GPS��Ϣ��PHP�ű�
    
    private String from_server = "";

	@Override
	public IBinder onBind(Intent intent) {
		return null;
	}
	
	@Override
	public void onCreate() {
		super.onCreate();
		
        //��ö�Location Manager������
        locationManager = (LocationManager)getSystemService(Context.LOCATION_SERVICE);
        //ָ��Location Provider������
        criteria.setAccuracy(Criteria.ACCURACY_FINE);				//�߾���
        criteria.setPowerRequirement(Criteria.POWER_LOW);			//�ͺĵ���
        criteria.setAltitudeRequired(true);							//��Ҫ���θ߶�
        criteria.setBearingRequired(true);							//��Ҫ����
        criteria.setSpeedRequired(true);							//��Ҫ�ٶ�
        criteria.setCostAllowed(true);								//��������ṩ����ȡ����
        criteria.setHorizontalAccuracy(Criteria.ACCURACY_HIGH);		//ˮƽ����(��γ��)Ҫ��߾���
        criteria.setVerticalAccuracy(Criteria.ACCURACY_HIGH);		//��ֱ����(����)Ҫ��߾���
        criteria.setBearingAccuracy(Criteria.ACCURACY_HIGH);		//����Ҫ��߾���
        criteria.setSpeedAccuracy(Criteria.ACCURACY_HIGH);			//�ٶ�Ҫ��߾���
        
        String provider = locationManager.getBestProvider(criteria, true);
        location = locationManager.getLastKnownLocation(provider);
        if (provider != null) {
        	locationManager.requestLocationUpdates(provider, minUpdateTime, minUpdateDistance, locationListener);
		}
	}
	
	private LocationListener locationListener = new LocationListener() {
		
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
	};
	
	private void sendGPS(Location location){
		 
		if (location != null) {
			latitude 	= location.getLatitude();
			longitude 	= location.getLongitude();
			altitude 	= location.getAltitude();
			speed 		= location.getSpeed();
			bearing 	= location.getBearing();
					
			AsyncHttpClient client = new AsyncHttpClient();
			RequestParams params = new RequestParams();
			params.put("longitude", ""+longitude);
			params.put("latitude", ""+latitude);
			params.put("altitude", ""+altitude);
			params.put("speed", ""+speed);
			params.put("bearing", ""+bearing);
				
			client.get(URL, params, new TextHttpResponseHandler() {
				@Override
				public void onSuccess(int arg0, Header[] arg1, String arg2) {
					from_server = arg2;
				}
				@Override
				public void onFailure(int arg0, Header[] arg1, String arg2, Throwable arg3) {
					from_server = "�����������GPS����ʧ�ܣ�";	//���ʧ���������ʾ��Ϣ
				}
			});	
			
			//�����̷߳���gps���ݣ�����UI�ϸ���gps��ʾ����
			Intent broadcastIntent = new Intent("mtn.lgdx.httpgps.GpsService");
			broadcastIntent.putExtra("longitude", longitude+"\n");
			broadcastIntent.putExtra("latitude", latitude+"\n");
			broadcastIntent.putExtra("altitude", altitude+"\n");
			broadcastIntent.putExtra("speed", speed+"\n");
			broadcastIntent.putExtra("bearing", bearing+"\n");
			broadcastIntent.putExtra("from_server", from_server+"\n");	//�ѷ��������ص����ݷ��͸����߳�
			sendBroadcast(broadcastIntent);	
			
		}		
	}

}
