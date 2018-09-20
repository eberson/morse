package etec.com.br;

import com.badlogic.gdx.utils.Disposable;

public interface CameraResolver extends Disposable {

    void dispose();

    void update(double delta);

    boolean isProccessing();

    void queueLetter(String letter);
    boolean turnOnFlash();
    boolean turnOnFlash(double time);
    boolean turnOffFlash();
    boolean turnOffFlash(double time);
    void releaseCamera();
    void connectToCamera();
}
