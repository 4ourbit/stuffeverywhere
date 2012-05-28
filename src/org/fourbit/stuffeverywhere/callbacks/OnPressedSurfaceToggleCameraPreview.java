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

import java.io.IOException;

import android.hardware.Camera;
import android.util.Log;
import android.view.Display;
import android.view.MotionEvent;
import android.view.Surface;
import android.view.SurfaceView;
import android.view.View;
import android.view.View.OnLongClickListener;
import android.view.View.OnTouchListener;

public final class OnPressedSurfaceToggleCameraPreview implements SwitchCameraOnOffDuringSurfaceLifecycle.Callback {

    private static final String TAG = OnPressedSurfaceToggleCameraPreview.class.getName();
    private final SurfaceView mSurfaceView;
    private Display mDisplay;
    private Camera.PictureCallback mPictureCallback;
    private boolean isSurfacePressed;

    public OnPressedSurfaceToggleCameraPreview(SurfaceView view, Display display,
            Camera.PictureCallback callback) {
        this.mSurfaceView = view;
        this.mDisplay = display;
        this.mPictureCallback = callback;
        this.isSurfacePressed = false;
    }

    @Override
    public void onAfterCameraOn(final Camera camera, final int orientation) {
        try {
            camera.setPreviewDisplay(mSurfaceView.getHolder());
        } catch (IOException e) {
            Log.e(TAG, e.getMessage());
            return; // TODO: Preview unsupported log msg
        }

        /**
         * Enable long press for the surface
         */
        mSurfaceView.setOnLongClickListener(new OnLongClickListener() {

            @Override
            public boolean onLongClick(View v) {
                camera.setDisplayOrientation(getRelativeOrientation(orientation, mDisplay));
                camera.startPreview();
                isSurfacePressed = true;
                return false;
            }

            private int getRelativeOrientation(int currentOrientation, Display display) {
                int rotation = display.getRotation();
                int degrees = 0;
                switch (rotation) {
                    case Surface.ROTATION_0:
                        degrees = 0;
                        break;
                    case Surface.ROTATION_90:
                        degrees = 90;
                        break;
                    case Surface.ROTATION_180:
                        degrees = 180;
                        break;
                    case Surface.ROTATION_270:
                        degrees = 270;
                        break;
                }
                return (currentOrientation - degrees + 360) % 360;
            }
        });

        mSurfaceView.setOnTouchListener(new OnTouchListener() {

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (isSurfacePressed) {
                    if (event.getAction() == MotionEvent.ACTION_UP) {
                        camera.takePicture(null, null, mPictureCallback);
                        // Preview is stopped automatically when taking picture
                        // camera.stopPreview();
                        isSurfacePressed = false;
                    }
                }
                return false;
            }
        });
    }

    @Override
    public void onBeforeCameraOff(final Camera camera, final int orientation) {
    }
}