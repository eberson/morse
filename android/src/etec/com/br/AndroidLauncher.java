package etec.com.br;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.Toast;

import com.badlogic.gdx.backends.android.AndroidApplication;
import com.badlogic.gdx.backends.android.AndroidApplicationConfiguration;
import etec.com.br.MorseGame;

public class AndroidLauncher extends AndroidApplication {

	private static final int CAMERA_REQUEST = 1001;

	@Override
	protected void onCreate (Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		requestAccess();

        AndroidApplicationConfiguration config = new AndroidApplicationConfiguration();


		MorseGame mf = new MorseGame();
		mf.setCameraResolver(new AndroidCameraResolver(getApplicationContext(), mf));

		initialize(mf, config);
	}

	private void requestAccess(){
		ActivityCompat.requestPermissions(this,
				                           new String[] { Manifest.permission.CAMERA },
				                           CAMERA_REQUEST );

		final boolean hasCameraFlash = getPackageManager().hasSystemFeature(PackageManager.FEATURE_CAMERA_FLASH);
		boolean isEnabled = ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED;

		Log.i("FLASHLIGHT", "camera tem flash? " + hasCameraFlash);
		Log.i("FLASHLIGHT", "camera está ativa? " + isEnabled);
	}

	@Override
	public void onRequestPermissionsResult(int requestCode,
										   @NonNull String[] permissions,
										   @NonNull int[] grantResults) {
		switch(requestCode) {
			case CAMERA_REQUEST :
				if (grantResults.length < 1  ||  grantResults[0] != PackageManager.PERMISSION_GRANTED) {
					Toast.makeText(this, "Não vai dar permissão... ok...", Toast.LENGTH_SHORT).show();
					finish();
				}
				break;
		}
	}
}
