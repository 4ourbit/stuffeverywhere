/*******************************************************************************
 *
 * Copyright (c) 2012 Oliver Bley. All rights reserved.
 *
 * This program and the accompanying materials are made
 * available under the terms of the GNU Public License v3
 * which is available at http://www.gnu.org/licenses/
 *
 *******************************************************************************/
package org.fourbit.stuffeverywhere.callbacks;

import android.hardware.Camera;
import android.hardware.Camera.CameraInfo;
import android.util.Log;
import android.view.SurfaceHolder;

public final class SwitchCameraOnOffDuringSurfaceLifecycle implements SurfaceHolder.Callback {

    public interface Callback {
        void onAfterCameraOn(Camera camera, int cameraOrientationDegrees);
        void onBeforeCameraOff(Camera camera, int cameraOrientationDegrees);
    }

    private static final String TAG = SwitchCameraOnOffDuringSurfaceLifecycle.class.getName();

    private Callback mCallback;
    private Class<Camera> mClassCamera;
    private Camera mCamera;
    private int mCameraOrientation;

    public SwitchCameraOnOffDuringSurfaceLifecycle(Class<Camera> class1, Callback callback) {
        this.mCallback = callback;
        this.mClassCamera = class1;
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        try {
            int numberOfCameras = (Integer) mClassCamera.getDeclaredMethod("getNumberOfCameras")
                    .invoke(null, new Object[0]);
            Camera.CameraInfo info = new Camera.CameraInfo();
            for (int i = 0; i < numberOfCameras; i++) {
                mClassCamera.getDeclaredMethod("getCameraInfo", int.class, Camera.CameraInfo.class)
                        .invoke(null, i, info);
                if (info.facing == CameraInfo.CAMERA_FACING_BACK) {
                    mCamera = (Camera) mClassCamera.getDeclaredMethod("open", int.class)
                            .invoke(null, i);
                    mCameraOrientation = info.orientation;

                    mCallback.onAfterCameraOn(mCamera, mCameraOrientation);

                    // Stop when first back-facing camera is found
                    break;
                }
            }
        } catch (Exception e) {
            Log.e(TAG, e.getMessage());
        }
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        mCallback.onBeforeCameraOff(mCamera, mCameraOrientation);
        mCamera.release();
    }
}