package party.mood.meter;

import android.app.Activity;
import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.widget.TextView;

public class MainActivity extends Activity implements SensorEventListener {
  private SensorManager sensorManager;
  private Sensor accelSensor;

  /** Called when the activity is first created. */
  @Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.main);

    sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
    accelSensor = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
  }

  public void onAccuracyChanged(Sensor sensor, int accuracy) {
  }

  public void onSensorChanged(SensorEvent event) {
    float x = event.values[0];
    TextView myText = (TextView)findViewById(R.id.my_text);
    myText.setText("The value is " + x);
  }
  
  @Override
  protected void onResume() {
    super.onResume();
    sensorManager.registerListener(this, accelSensor, SensorManager.SENSOR_DELAY_NORMAL);
  }

  @Override
  protected void onPause() {
    super.onPause();
    sensorManager.unregisterListener(this);
  }
}
