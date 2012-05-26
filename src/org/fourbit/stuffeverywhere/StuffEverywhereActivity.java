/*******************************************************************************
 *
 * Copyright (c) 2012 Oliver Bley. All rights reserved.
 *
 * This program and the accompanying materials are made
 * available under the terms of the GNU Public License v3
 * which is available at http://www.gnu.org/licenses/
 *
 *******************************************************************************/
package org.fourbit.stuffeverywhere;

import org.fourbit.stuffeverywhere.callbacks.OnPressedSurfaceToggleCameraPreview;
import org.fourbit.stuffeverywhere.callbacks.SwitchCameraOnOffDuringSurfaceLifecycle;

import android.app.Activity;
import android.hardware.Camera;
import android.os.Bundle;
import android.view.Display;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

public class StuffEverywhereActivity extends Activity {

    SurfaceView mSurfaceView;

    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        mSurfaceView = (SurfaceView) findViewById(R.id.surfaceView1);
        mSurfaceView.getHolder().setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);

        Display defaultDisplay = getWindowManager().getDefaultDisplay();

        mSurfaceView.getHolder().addCallback(
                new SwitchCameraOnOffDuringSurfaceLifecycle(Camera.class,
                        new OnPressedSurfaceToggleCameraPreview(mSurfaceView, defaultDisplay)));
    }
}