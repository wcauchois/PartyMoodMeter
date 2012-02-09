package party.mood.meter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.os.Bundle;

public class Utils {
  public static final String SITE_ROOT = "http://partymoodmeter.herokuapp.com";
  
  public static Bundle convertJSONObjectToBundle(JSONObject obj) throws JSONException {
    Bundle result = new Bundle();
    JSONArray names = obj.names();
    for(int i = 0; i < names.length(); i++) {
      String name = names.getString(i);
      Object value = obj.get(name);
      if(value instanceof String)
        result.putString(name, (String)value);
      else if(value instanceof Integer)
        result.putInt(name, (Integer)value);
      else if(value instanceof Boolean)
        result.putBoolean(name, (Boolean)value);
      else {
        /* ignore */
      }
    }
    return result;
  }
}

