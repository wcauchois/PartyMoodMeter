package party.mood.meter;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Scanner;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.ListActivity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.SimpleAdapter;

public class PlacesListActivity extends ListActivity {
  private static final String PLACES_URL = "http://partymoodmeter.herokuapp.com/places.json";
  
  @SuppressWarnings("serial")
  @Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    
    final JSONArray places = getPlaces();
    
    String[] from = new String[] { "name", "desc" };
    int[] to = new int[] { R.id.place_name, R.id.place_desc };
    List<HashMap<String, String>> data = new ArrayList<HashMap<String, String>>();
    try {
      for(int i = 0; i < places.length(); i++) {
        final JSONObject placeObj = places.getJSONObject(i);
        data.add(new HashMap<String, String>() {{
          put("name", placeObj.getString("name"));
          put("desc", "" + placeObj.getInt("num_people") + " people are here.");
        }});
      }
    } catch(JSONException e) { /* pass */ }
    
    getListView().setOnItemClickListener(new OnItemClickListener() {
      public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        try {
          int placeId = places.getJSONObject(position).getInt("id");
          Intent checkinIntent = new Intent(PlacesListActivity.this, CheckInActivity.class);
          checkinIntent.setData(Uri.parse("http://partymoodmeter.herokuapp.com/place?id=" + placeId));
          startActivity(checkinIntent);
        } catch(JSONException e) { /* pass */ }
      }
    });
    
    setListAdapter(new SimpleAdapter(this, data, R.layout.list_item, from, to));
  }
  
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
