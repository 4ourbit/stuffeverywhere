package org.fourbit.stuffeverywhere.callbacks;

import android.hardware.Camera;
import android.view.SurfaceHolder;

public final class SwitchCameraOnOffDuringSurfaceLifecycle implements SurfaceHolder.Callback {

    public interface Callback {
        void onAfterCameraOn(Camera camera);
        void onBeforeCameraOff(Camera camera);
    }

    private Callback mCallback;
    private Class<Camera> mClassCamera;
    private Camera mCamera;

    public SwitchCameraOnOffDuringSurfaceLifecycle(Class<Camera> class1, Callback callback) {
        this.mCallback = callback;
        this.mClassCamera = class1;
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        try {
            mCamera = (Camera) mClassCamera.getDeclaredMethod("open").invoke(null, new Object[0]);
        } catch (Exception ignored) {
            ignored.printStackTrace();
        }
        mCallback.onAfterCameraOn(mCamera);
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        mCallback.onBeforeCameraOff(mCamera);
        mCamera.release();
    }
}