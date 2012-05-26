package org.fourbit.stuffeverywhere.callbacks;

import java.io.IOException;

import android.hardware.Camera;
import android.util.Log;
import android.view.MotionEvent;
import android.view.SurfaceView;
import android.view.View;
import android.view.View.OnLongClickListener;
import android.view.View.OnTouchListener;

public final class OnPressedSurfaceToggleCameraPreview implements SwitchCameraOnOffDuringSurfaceLifecycle.Callback {

    private static final String TAG = OnPressedSurfaceToggleCameraPreview.class.getName();
    private final SurfaceView mView;
    private boolean isSurfacePressed;

    public OnPressedSurfaceToggleCameraPreview(SurfaceView view) {
        this.mView = view;
        this.isSurfacePressed = false;
    }

    public void onAfterCameraOn(final Camera camera) {
        try {
            camera.setPreviewDisplay(mView.getHolder());
        } catch (IOException e) {
            Log.e(TAG, e.getMessage());
            return; // TODO: Preview unsupported log msg
        }

        /**
         * Enable long press for the surface
         */
        mView.setOnLongClickListener(new OnLongClickListener() {

            @Override
            public boolean onLongClick(View v) {
                camera.startPreview();
                isSurfacePressed = true;
                return false;
            }
        });

        mView.setOnTouchListener(new OnTouchListener() {

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (isSurfacePressed) {
                    if (event.getAction() == MotionEvent.ACTION_UP) {
                        camera.takePicture(null, null, null);
                        camera.stopPreview();
                        isSurfacePressed = false;
                    }
                }
                return false;
            }
        });
    }

    public void onBeforeCameraOff(final Camera camera) {
    }
}