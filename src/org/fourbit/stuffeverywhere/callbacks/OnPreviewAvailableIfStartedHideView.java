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
import android.view.View;

public class OnPreviewAvailableIfStartedHideView implements OnSurfaceCreatedMakeCameraPreviewable.Callback {

    private View mView;

    public OnPreviewAvailableIfStartedHideView(View view) {
        mView = view;
    }

    @Override
    public void onPreviewAvailable(Camera camera, int cameraOrientationDegrees) {
        camera.setOneShotPreviewCallback(new Camera.PreviewCallback() {

            @Override
            public void onPreviewFrame(byte[] data, Camera camera) {
                mView.setVisibility(View.GONE);
            }
        });
    }

    @Override
    public void onBeforeCameraOff(Camera camera, int cameraOrientationDegrees) {
    }
}
