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

import android.content.Context;
import android.hardware.Camera;
import android.view.MotionEvent;
import android.view.Surface;
import android.view.View;
import android.view.View.OnLongClickListener;
import android.view.View.OnTouchListener;
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
    public void onPreviewAvailable(final Camera camera, final int orientation) {
        /**
         * Enable long press for the surface
         */
        mView.setOnLongClickListener(new OnLongClickListener() {

            @Override
            public boolean onLongClick(View v) {
                camera.startPreview();

                camera.setDisplayOrientation(getRelativeOrientation(orientation));
                isSurfacePressed = true;
                return false;
            }

            private int getRelativeOrientation(int currentOrientation) {
                WindowManager wm = (WindowManager) mView.getContext().getSystemService(
                        Context.WINDOW_SERVICE);
                int rotation = wm.getDefaultDisplay().getRotation();
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

        mView.setOnTouchListener(new OnTouchListener() {

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (isSurfacePressed) {
                    if (event.getAction() == MotionEvent.ACTION_UP) {
                        camera.takePicture(null, null, mPictureCallback);
                        isSurfacePressed = false;
                    }
                }
                return false;
            }
        });
    }

    @Override
    public void onBeforeCameraOff(Camera camera, int cameraOrientationDegrees) {
    }
}