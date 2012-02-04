package party.mood.meter;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

public class DataUploadService extends Service {
  @Override
  public int onStartCommand(Intent intent, int flags, int startId) {
    return START_STICKY;
  }

  @Override
  public IBinder onBind(Intent intent) {
    // We don't provide binding, so return null.
    return null;
  }
  
  @Override
  public void onDestroy() { }
}
