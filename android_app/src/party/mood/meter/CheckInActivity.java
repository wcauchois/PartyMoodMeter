package party.mood.meter;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

public class CheckInActivity extends Activity {
  @Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.checkin);
    
    Bundle placeBundle = getIntent().getExtras().getBundle("place");
    ((TextView)findViewById(R.id.place_name)).setText(placeBundle.getString("name"));
    ((TextView)findViewById(R.id.place_desc)).setText(
        "" + placeBundle.getInt("num_people") + " people are here.");
    
    ((Button)findViewById(R.id.checkin_button)).setOnClickListener(new OnClickListener() {
      public void onClick(View v) {
        Intent partyMoodIntent = new Intent(getBaseContext(), PartyMoodActivity.class);
        partyMoodIntent.putExtra("place_id", getIntent().getExtras().getInt("id"));
        startActivity(partyMoodIntent);
      }
    });
  }
}
