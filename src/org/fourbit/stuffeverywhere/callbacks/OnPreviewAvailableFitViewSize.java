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
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.WindowManager;

public class OnPreviewAvailableFitViewSize implements
        OnSurfaceCreatedMakeCameraPreviewable.Callback {

    private View mView;
    private ViewTreeObserver.OnGlobalLayoutListener mListener;

    public OnPreviewAvailableFitViewSize(View view) {
        mView = view;
    }

    @Override
    public void onPreviewAvailable(final Camera camera, int cameraOrientationDegrees) {
        // Camera.Parameters parameters = camera.getParameters();
        // List<Camera.Size> previewSizes = parameters.getSupportedPreviewSizes();
        // parameters.setPreviewSize(480, 320);
        // camera.setParameters(parameters);

        mListener = new ViewTreeObserver.OnGlobalLayoutListener() {

            @Override
            public void onGlobalLayout() {
                WindowManager wm = (WindowManager) mView.getContext().getSystemService(
                        Context.WINDOW_SERVICE);
                int screenWidth = wm.getDefaultDisplay().getWidth();
                float fTargetWidth = screenWidth;

                Camera.Size previewSize = camera.getParameters().getPreviewSize();
                float fHeight = previewSize.height, fWidth = previewSize.width;

                // Set the width of the view to the width of the screen,
                // Set the height of the view to match the aspect ratio of the preview
                ViewGroup.LayoutParams lp = mView.getLayoutParams();
                lp.width = screenWidth;
                lp.height = (int) (fTargetWidth * fHeight / fWidth);
                mView.setLayoutParams(lp);
            }
        };
        mView.getViewTreeObserver().addOnGlobalLayoutListener(mListener);
    }

    @Override
    public void onBeforeCameraOff(Camera camera, int cameraOrientationDegrees) {
        if (mListener != null)
            mView.getViewTreeObserver().removeGlobalOnLayoutListener(mListener);
    }
}