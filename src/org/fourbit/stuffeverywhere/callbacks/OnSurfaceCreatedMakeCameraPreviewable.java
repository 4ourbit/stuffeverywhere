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

public final class OnSurfaceCreatedMakeCameraPreviewable implements SurfaceHolder.Callback {

    public interface Callback {
        void onPreviewAvailable(Camera camera, int cameraOrientationDegrees);
        void onBeforeCameraOff(Camera camera, int cameraOrientationDegrees);
    }

    private static final String TAG = OnSurfaceCreatedMakeCameraPreviewable.class.getName();

    private Callback[] mCallbacks;
    private Class<Camera> mClassCamera;
    private Camera mCamera;
    private int mCameraOrientation;

    public OnSurfaceCreatedMakeCameraPreviewable(Class<Camera> class1, Callback... callbacks) {
        this.mCallbacks = callbacks;
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
                    
                    mCamera.setPreviewDisplay(holder);
                    for (Callback cb : mCallbacks)
                        cb.onPreviewAvailable(mCamera, mCameraOrientation);

                    // Stop when first back-facing camera is found
                    break;
                }
            }
        } catch (Exception e) {
            Log.e(TAG, "Error while attaching camera to surface");
            e.printStackTrace();
        }
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        for (Callback cb : mCallbacks)
            cb.onBeforeCameraOff(mCamera, mCameraOrientation);
        // Is this really needed when there is no such callback used?
        mCamera.setPreviewCallback(null);
        // Preview is stopped automatically when taking picture but just to make sure
        mCamera.stopPreview();
        mCamera.release();
    }
}