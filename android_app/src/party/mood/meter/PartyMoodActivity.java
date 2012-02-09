package party.mood.meter;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

public class PartyMoodActivity extends Activity {
  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.party_mood);
    
    final Intent uploadServiceIntent = new Intent(this, DataUploadService.class);
    startService(uploadServiceIntent);
    
    ((Button)findViewById(R.id.stop_button)).setOnClickListener(new OnClickListener() {
      public void onClick(View v) {
        stopService(uploadServiceIntent);
      }
    });
  }
}
