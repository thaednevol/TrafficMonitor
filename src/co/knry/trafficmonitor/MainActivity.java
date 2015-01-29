package co.knry.trafficmonitor;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import android.app.Activity;
import android.content.Context;
import android.content.res.AssetManager;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

public class MainActivity extends Activity {

	private XmlPullParserFactory xmlFactoryObject; 
    private XmlPullParser myparser;
	private Context mContext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        
        this.mContext = this;
        
        try {
			xmlFactoryObject = XmlPullParserFactory.newInstance();
			myparser = xmlFactoryObject.newPullParser();
			
			AssetManager assetManager = getAssets();
		    InputStream stream = assetManager.open("transmilenio.xml");
			
			myparser.setInput(stream, null);
			
			int event = myparser.getEventType();
			while (event != XmlPullParser.END_DOCUMENT) 
			{
			   String name=myparser.getName();
			   switch (event){
			      case XmlPullParser.START_TAG:
			      break;
			      case XmlPullParser.END_TAG:
			      if(name.equals("temperature")){
			         String temperature = myparser.getAttributeValue(null,"value");
			         
			         
			      }
			      break;
			   }		 
			   event = myparser.next(); 					
			}
		} catch (XmlPullParserException e) {
			Log.d("ERROR", e.toString());
			e.printStackTrace();
		} catch (IOException e) {
			Log.d("ERROR", e.toString());
			e.printStackTrace();
		}
        
        initJson();
        
        Button btnShowLocation = (Button) findViewById(R.id.btnShowLocation);
        
        // show location button click event
        btnShowLocation.setOnClickListener(new View.OnClickListener() {
             

			@Override
            public void onClick(View arg0) { 
				getLocation();                 
            }
        });
        
    }

	protected void getLocation() {
        // create class object
		GPSTracker gps = new GPSTracker(mContext);

        // check if GPS enabled     
        if(gps.canGetLocation()){
             
            double latitude = gps.getLatitude();
            double longitude = gps.getLongitude();
             
            // \n is for new line
            Toast.makeText(getApplicationContext(), "Your Location is - \nLat: " + latitude + "\nLong: " + longitude, Toast.LENGTH_LONG).show();
            
            Log.d("Your Location is ", "Lat: " + latitude + " Long: " + longitude);
        }else{
            // can't get location
            // GPS or Network is not enabled
            // Ask user to enable GPS/network in settings
            gps.showSettingsAlert();
        }
        
        gps.stopUsingGPS();

	}

	private void initJson() {
		List<String> spinnerArray =  new ArrayList<String>();
		
		try {
			JSONObject jo = new JSONObject(loadJSONFromAsset());
			JSONArray ja_features = jo.getJSONArray("features");
			
			for (int i=0; i<ja_features.length(); i++){
				JSONObject jo_feature = ja_features.getJSONObject(i);
				JSONObject jo_properties = jo_feature.getJSONObject("properties");
				String name = jo_properties.getString("Name");
				spinnerArray.add(name);
				Log.i("NOMBRE",name);
			}
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		ArrayAdapter<String> adapter = new ArrayAdapter<String>(
			    this, android.R.layout.simple_spinner_item, spinnerArray);

			adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
			Spinner sp_main_origen = (Spinner) findViewById(R.id.sp_main_origen);
			sp_main_origen.setAdapter(adapter);
			
			Spinner sp_main_destino = (Spinner) findViewById(R.id.sp_main_destino);
			sp_main_destino.setAdapter(adapter);
		
	}
	
	public String loadJSONFromAsset() {
        String json = null;
        try {

            InputStream is = getAssets().open("estaciones.json");

            int size = is.available();

            byte[] buffer = new byte[size];

            is.read(buffer);

            is.close();

            json = new String(buffer, "UTF-8");


        } catch (IOException ex) {
            ex.printStackTrace();
            return null;
        }
        return json;

    }
}
