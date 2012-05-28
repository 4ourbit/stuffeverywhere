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

import android.hardware.Camera;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.ViewGroup;

public class StuffEverywhereActivity extends FragmentActivity {

    public static class TagsFragment extends Fragment {

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                Bundle savedInstanceState) {
            View view = inflater.inflate(R.layout.tags_fragment, container, false);
            return view;
        }
    }

    SurfaceView mSurfaceView;

    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        mSurfaceView = (SurfaceView) findViewById(R.id.surfaceView1);

        final Display defaultDisplay = getWindowManager().getDefaultDisplay();
        final Camera.PictureCallback cameraPictureCallback = new Camera.PictureCallback() {

            @Override
            public void onPictureTaken(byte[] imageData, Camera camera) {
                FragmentManager fragmentManager = getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction()
                        .replace(R.id.bottom_frame, new TagsFragment())
                        .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
                        .addToBackStack(null);
                fragmentTransaction.commit();
            }
        };

        mSurfaceView.getHolder().setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
        mSurfaceView.getHolder().addCallback(
                new SwitchCameraOnOffDuringSurfaceLifecycle(Camera.class,
                        new OnPressedSurfaceToggleCameraPreview(mSurfaceView, defaultDisplay,
                                cameraPictureCallback)));
    }
}