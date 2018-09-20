package etec.com.br;

import java.util.LinkedList;
import java.util.Queue;

import android.content.Context;
import android.content.pm.PackageManager;
import android.hardware.Camera;
import android.hardware.Camera.Parameters;
import android.hardware.camera2.CameraAccessException;
import android.hardware.camera2.CameraManager;
import android.util.Log;

public class AndroidCameraResolver implements CameraResolver {
    MorseGame _app;
    Context _context;
    double _targetTime = 0;

    Queue<TextToMorse.Letter> queue;

    AndroidCameraResolver(Context context, MorseGame mf) {
        super();
        _app = mf;
        _context = context;

        queue = new LinkedList<TextToMorse.Letter>();
    }

    public void queueLetter(String letter) {
        queue.add(TextToMorse.getLetter(letter));
    }

    @Override
    public void update(double delta) {
        if (_targetTime > 0.0) {
            Log.i("DEMO", "targetTime: " + _targetTime);
            Log.i("DEMO", "delta: " + delta);
        }

        if(_targetTime == 0) { // If we are not processing
            if(!queue.isEmpty()) { // If there is something to process
                TextToMorse.Letter l = queue.element();
                TextToMorse.Item current = l._items.poll();
                if(current._light)
                    turnOnFlash(current._time);
                else {
                    turnOffFlash(current._time);
                    if(l._items.isEmpty()) {
                        _app.colorLetter();
                        queue.poll();
                    }
                }
            }
            else{
                turnOffFlash();
            }
        }
        else if(_targetTime > 0) {
            _targetTime -= delta;
            _targetTime = Math.max(0, _targetTime);
        }
    }

    @Override
    public boolean turnOnFlash() {
        CameraManager cameraManager = (CameraManager) _context.getSystemService(Context.CAMERA_SERVICE);

        try {
            String cameraId = cameraManager.getCameraIdList()[0];
            cameraManager.setTorchMode(cameraId, true);
            return true;
        } catch (CameraAccessException e) {
            Log.e("DEMO", e.getMessage(), e);
            return false;
        }

//        if(_context.getPackageManager().hasSystemFeature(PackageManager.FEATURE_CAMERA_FLASH)) {
//            CameraManager cameraManager = (CameraManager) _context.getSystemService(Context.CAMERA_SERVICE);
//
//            try {
//                String cameraId = cameraManager.getCameraIdList()[0];
//                cameraManager.setTorchMode(cameraId, true);
//            } catch (CameraAccessException e) {
//                e.printStackTrace();
//            }
//
////            _param.setFlashMode(Parameters.FLASH_MODE_TORCH);
////            _camera.setParameters(_param);
////            _camera.startPreview();
//            return true;
//        }
//        return false;
    }

    @Override
    public boolean turnOnFlash(double time) {
        CameraManager cameraManager = (CameraManager) _context.getSystemService(Context.CAMERA_SERVICE);

        try {
            String cameraId = cameraManager.getCameraIdList()[0];
            cameraManager.setTorchMode(cameraId, true);
            _targetTime = time;

            return true;
        } catch (CameraAccessException e) {
            Log.e("DEMO", e.getMessage(), e);
            return false;
        }
//        if(_context.getPackageManager().hasSystemFeature(PackageManager.FEATURE_CAMERA_FLASH)) {
//
//            CameraManager cameraManager = (CameraManager) _context.getSystemService(Context.CAMERA_SERVICE);
//
//            try {
//                String cameraId = cameraManager.getCameraIdList()[0];
//                cameraManager.setTorchMode(cameraId, true);
//            } catch (CameraAccessException e) {
//                e.printStackTrace();
//            }
//
////            _param.setFlashMode(Parameters.FLASH_MODE_TORCH);
////            _camera.setParameters(_param);
////            _camera.startPreview();
//            _targetTime = time;
//
//            return true;
//        }
//        return false;
    }


    @Override
    public boolean turnOffFlash() {
        CameraManager cameraManager = (CameraManager) _context.getSystemService(Context.CAMERA_SERVICE);

        try {
            String cameraId = cameraManager.getCameraIdList()[0];
            cameraManager.setTorchMode(cameraId, false);

            return true;
        } catch (CameraAccessException e) {
            Log.e("DEMO", e.getMessage(), e);
            return false;
        }
//        if(_context.getPackageManager().hasSystemFeature(PackageManager.FEATURE_CAMERA_FLASH)) {
//            _param.setFlashMode(Parameters.FLASH_MODE_OFF);
//            _camera.setParameters(_param);
//            return true;
//        }
//        return false;

    }

    @Override
    public boolean turnOffFlash(double time) {
        CameraManager cameraManager = (CameraManager) _context.getSystemService(Context.CAMERA_SERVICE);

        try {
            String cameraId = cameraManager.getCameraIdList()[0];
            cameraManager.setTorchMode(cameraId, false);
            _targetTime = time;

            return true;
        } catch (CameraAccessException e) {
            Log.e("DEMO", e.getMessage(), e);
            return false;
        }
//        if(_context.getPackageManager().hasSystemFeature(PackageManager.FEATURE_CAMERA_FLASH)) {
//            _param.setFlashMode(Parameters.FLASH_MODE_OFF);
//            _camera.setParameters(_param);
//            _targetTime = time;
//
//            return true;
//        }
//        return false;

    }

    @Override
    public void connectToCamera() {
//        _camera = Camera.open(); // Must deal with null return
//        _param = _camera.getParameters();
    }

    @Override
    public void releaseCamera() {
//        if(_camera != null) {
//            _camera.stopPreview();
//            _camera.release();
//        }
    }

    @Override
    public void dispose() {
        //releaseCamera();
    }

    @Override
    public boolean isProccessing() {
        return !queue.isEmpty();
    }

}
