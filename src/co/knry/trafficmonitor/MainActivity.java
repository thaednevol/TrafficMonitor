package co.knry.trafficmonitor;

import java.io.IOException;
import java.io.InputStream;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import android.app.Activity;
import android.content.res.AssetManager;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

public class MainActivity extends Activity {

	private XmlPullParserFactory xmlFactoryObject; 
    private XmlPullParser myparser;
	
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        
        try {
			xmlFactoryObject = XmlPullParserFactory.newInstance();
			myparser = xmlFactoryObject.newPullParser();
			
			AssetManager assetManager = getAssets();
		    InputStream stream = assetManager.open("helloworld.txt");
			
			myparser.setInput(stream, null);
		} catch (XmlPullParserException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        
        
    }
}
