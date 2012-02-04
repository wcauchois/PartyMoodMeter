package party.mood.meter;

import java.io.IOException;
import java.util.Scanner;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONException;

import android.app.ListActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ArrayAdapter;

public class CheckInActivity extends ListActivity {
  @Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.checkin);
    
    JSONArray places = getPlaces();
    String[] placesArray = new String[places.length()];
    try {
      for(int i = 0; i < places.length(); i++)
        placesArray[i] = places.getString(i);
    } catch(JSONException e) { /* pass */ }
    
    setListAdapter(new ArrayAdapter<String>(this, R.layout.list_item, placesArray));
  }
  
  private static final String PLACES_URL = "http://partymoodmeter.herokuapp.com/places.json";
  
  private JSONArray getPlaces() {
    HttpClient client = new DefaultHttpClient();
    HttpGet get = new HttpGet(PLACES_URL);
    try {
      HttpResponse response = client.execute(get);
      String body = new Scanner(response.getEntity().getContent()).useDelimiter("\\A").next();
      return new JSONArray(body);
    } catch(IOException e) {
      Log.e("CheckInActivity", "IOException thrown " + e);
      return null;
    } catch(JSONException e) {
      Log.e("CheckInActivity", "JSONException thrown " + e);
      return null;
    }
  }
}
