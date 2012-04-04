package party.mood.meter;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.Arrays;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONObject;
import org.openintents.sensorsimulator.hardware.Sensor;
import org.openintents.sensorsimulator.hardware.SensorEvent;
import org.openintents.sensorsimulator.hardware.SensorEventListener;
import org.openintents.sensorsimulator.hardware.SensorManagerSimulator;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;
import android.widget.Toast;

public class DataUploadService extends Service {
  private static final int UPLOAD_PERIOD = 5;
  private static final String API_ENDPOINT = Utils.SITE_ROOT + "/submit_mood";

  private ScheduledExecutorService mScheduler;
  private SensorManagerSimulator mSensorManager;
  private Sensor mAccelSensor;
  private UploadSensorData mUploadSensorData;

  private static class UploadSensorData implements Runnable,
      SensorEventListener {
    private URI mApiEndpoint;
    private float[] mCurrentValues = null;
    private double[] xValues = new double[5];
    private double[] yValues = new double[5];
    private double[] zValues = new double[5];
    private int clock = 0;
    private float[] mValueDeltas;

    public UploadSensorData(URI apiEndpoint) {
      mApiEndpoint = apiEndpoint;
    }

    public void run() {
    	/*
      if(mValueDeltas == null)
        return;
        */
      try {
    	  if (clock < 5) {
    		  xValues[clock] = (double) mCurrentValues[0];
    		  yValues[clock] = (double) mCurrentValues[1];
    		  zValues[clock] = (double) mCurrentValues[2];
    		  clock++;
    		  return;
    	  } else {
    		  double xDeriv = Utils.differentiateFive(1, xValues);
    		  double yDeriv = Utils.differentiateFive(1, yValues);
    		  double zDeriv = Utils.differentiateFive(1, zValues);
    		  
          JSONObject objectToSubmit = new JSONObject();
          objectToSubmit.put("xDeriv", xDeriv);
          objectToSubmit.put("yDeriv", yDeriv);
          objectToSubmit.put("zDeriv", zDeriv);
          
          Log.d("DataUploadService", objectToSubmit.toString());
    		  
    		  clock = 0;
    	  }
        // TODO: we should attach some kind of uid to this post
    	  /*
        float timeAveragedAverageDelta = 0.0f;
        for(int i = 0; i < mValueDeltas.length; i++) {
          timeAveragedAverageDelta +=
              (mValueDeltas[i] / (UPLOAD_PERIOD * 1000.0f * (float)mValueDeltas.length));
        }
        mValueDeltas = null;
        */

        /*
        HttpPost post = new HttpPost(mApiEndpoint);
        post.setEntity(new StringEntity(objectToSubmit.toString()));
        HttpClient client = new DefaultHttpClient();
        client.execute(post);
        */
      } catch(Exception e) {
        /* pass */
      }
    }

    public void onAccuracyChanged(Sensor sensor, int accuracy) {
    }

    public void onSensorChanged(SensorEvent evt) {
      mCurrentValues = evt.values;
      /*
       * if(mCurrentValues != null) { if(mValueDeltas == null) { mValueDeltas =
       * new float[evt.values.length]; Arrays.fill(mValueDeltas, 0.0f); }
       * for(int i = 0; i < evt.values.length; i++) mValueDeltas[i] +=
       * Math.abs(evt.values[i] - mCurrentValues[i]); } mCurrentValues = new
       * float[evt.values.length]; System.arraycopy(evt.values, 0,
       * mCurrentValues, 0, evt.values.length);
       */
    }
  }

  @Override
  public void onCreate() {
    mScheduler = Executors.newScheduledThreadPool(1);
    // mSensorManager = (SensorManager)getSystemService(Context.SENSOR_SERVICE);
    mSensorManager = SensorManagerSimulator.getSystemService(this,
        SENSOR_SERVICE);
    mSensorManager.connectSimulator();
    mAccelSensor = mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
  }

  @Override
  public int onStartCommand(Intent intent, int flags, int startId) {
    try {
      mUploadSensorData = new UploadSensorData(new URI(API_ENDPOINT));
    } catch(URISyntaxException e) {
      /* pass */
    }
    mSensorManager.registerListener(mUploadSensorData, mAccelSensor,
        SensorManagerSimulator.SENSOR_DELAY_NORMAL);
    mScheduler.scheduleAtFixedRate(mUploadSensorData, 0, 1, TimeUnit.SECONDS);
    Toast.makeText(this, "service started", Toast.LENGTH_SHORT).show(); // XXX
    Log.d("DataUploadService", "service started!!");
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
