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

import org.fourbit.stuffeverywhere.StuffEverywhereActivity;

import android.content.Context;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.hardware.Camera;
import android.view.Display;
import android.view.MotionEvent;
import android.view.Surface;
import android.view.View;
import android.view.WindowManager;

public final class OnPreviewAvailableMakeViewCameraTrigger implements OnSurfaceCreatedMakeCameraPreviewable.Callback {

    private Camera.PictureCallback mPictureCallback;
    private View mView;

    private boolean isSurfacePressed;

    public OnPreviewAvailableMakeViewCameraTrigger(View view, Camera.PictureCallback callback) {
        this.mPictureCallback = callback;
        this.mView = view;
        this.isSurfacePressed = false;
    }

    @Override
    public void onPreviewAvailable(final Camera camera, final int cameraOrientation) {
        /**
         * Enable long press for the surface
         */
        mView.setOnLongClickListener(new View.OnLongClickListener() {

            @Override
            public boolean onLongClick(View v) {
                isSurfacePressed = true;

                WindowManager wm = (WindowManager) mView.getContext().getSystemService(Context.WINDOW_SERVICE);
                Display display = wm.getDefaultDisplay();

                /** Lock orientation as long as surface is pressed */
                lockScreenOrientation(v.getResources().getConfiguration().orientation, display);

                camera.setDisplayOrientation(getRelativeCameraOrientation(display, cameraOrientation));
                camera.startPreview();
                return true;
            }

            private int getRelativeCameraOrientation(Display display, int currentCameraOrientation) {
                int degrees = 0;
                switch (display.getRotation()) {
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
                return (currentCameraOrientation - degrees + 360) % 360;
            }
        });

        mView.setOnTouchListener(new View.OnTouchListener() {

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (isSurfacePressed) {
                    if (event.getAction() == MotionEvent.ACTION_CANCEL
                            || event.getAction() == MotionEvent.ACTION_UP) {
                        camera.takePicture(null, null, mPictureCallback);
                        isSurfacePressed = false;

                        /** Unlock orientation when surface press is released */
                        unlockScreenOrientation();
                    }
                }
                return false;
            }
        });
    }

    private void lockScreenOrientation(int configurationOrientation, Display display) {
        int screenOrientation;
        int rotation = display.getRotation();
        switch (configurationOrientation) {
            case Configuration.ORIENTATION_PORTRAIT:
                if (rotation == Surface.ROTATION_90 || rotation == Surface.ROTATION_180) {
                    screenOrientation = ActivityInfo.SCREEN_ORIENTATION_REVERSE_PORTRAIT;
                } else {
                    screenOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT;
                }
                break;
            case Configuration.ORIENTATION_LANDSCAPE:
                if (rotation == Surface.ROTATION_0 || rotation == Surface.ROTATION_90) {
                    screenOrientation = ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE;
                } else {
                    screenOrientation = ActivityInfo.SCREEN_ORIENTATION_REVERSE_LANDSCAPE;
                }
                break;
            default:
                screenOrientation = ActivityInfo.SCREEN_ORIENTATION_FULL_SENSOR;
        }
    
        ((StuffEverywhereActivity) mView.getContext())
                .setRequestedOrientation(screenOrientation);
    }

    private void unlockScreenOrientation() {
        ((StuffEverywhereActivity) mView.getContext())
                .setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_FULL_SENSOR);
    }

    @Override
    public void onBeforeCameraOff(Camera camera, int cameraOrientationDegrees) {
    }
}