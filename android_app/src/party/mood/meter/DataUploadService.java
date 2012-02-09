package party.mood.meter;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.IBinder;
import android.widget.Toast;

public class DataUploadService extends Service {
  private final int UPLOAD_PERIOD = 5;
  private static final String API_ENDPOINT = Utils.SITE_ROOT + "/submit_sensor";
  
  private ScheduledExecutorService mScheduler;
  private SensorManager mSensorManager;
  private Sensor mAccelSensor;
  private UploadSensorData mUploadSensorData;
  
  private static class UploadSensorData implements Runnable, SensorEventListener {
    private URI mApiEndpoint;
    private float[] mCurrentValues = null;
    
    public UploadSensorData(URI apiEndpoint) {
      mApiEndpoint = apiEndpoint;
    }
    
    public void run() {
      if(mCurrentValues == null)
        return;
      JSONArray arrayToSubmit = new JSONArray();
      try {
        // TODO: we should attach some kind of uid to this post
        for(float val : mCurrentValues)
          arrayToSubmit.put(val);
        
        HttpPost post = new HttpPost(mApiEndpoint);
        post.setEntity(new StringEntity(arrayToSubmit.toString()));
        HttpClient client = new DefaultHttpClient();
        client.execute(post);
      } catch(Exception e) {
        /* pass */
      }
    }

    public void onAccuracyChanged(Sensor sensor, int accuracy) { }

    public void onSensorChanged(SensorEvent evt) {
      mCurrentValues = new float[evt.values.length];
      System.arraycopy(evt.values, 0, mCurrentValues, 0, evt.values.length);
    }
  }
  
  @Override
  public void onCreate() {
    mScheduler = Executors.newScheduledThreadPool(1);
    mSensorManager = (SensorManager)getSystemService(Context.SENSOR_SERVICE);
    mAccelSensor = mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
  }
  
  @Override
  public int onStartCommand(Intent intent, int flags, int startId) {
    try {
      mUploadSensorData = new UploadSensorData(new URI(API_ENDPOINT));
    } catch(URISyntaxException e) {
      /* pass */
    }
    mSensorManager.registerListener(mUploadSensorData, mAccelSensor, SensorManager.SENSOR_DELAY_NORMAL);
    mScheduler.scheduleAtFixedRate(mUploadSensorData, 0, UPLOAD_PERIOD, TimeUnit.SECONDS);
    Toast.makeText(this, "service started", Toast.LENGTH_SHORT).show(); // XXX
    return START_STICKY;
  }

  @Override
  public IBinder onBind(Intent intent) {
    // We don't provide binding, so return null.
    return null;
  }
  
  @Override
  public void onDestroy() {
    mSensorManager.unregisterListener(mUploadSensorData);
    mScheduler.shutdown();
    Toast.makeText(this, "service stopped", Toast.LENGTH_SHORT).show(); // XXX
  }
}
