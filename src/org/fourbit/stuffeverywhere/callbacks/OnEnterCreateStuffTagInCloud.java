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

import java.text.ParseException;
import java.util.ArrayList;

import org.fourbit.stuffeverywhere.model.StuffTag;

import android.view.KeyEvent;
import android.widget.TextView;

public class OnEnterCreateStuffTagInCloud implements TextView.OnEditorActionListener {

    interface Callback {
        public void onStuffTagAdded();
    }

    ArrayList<StuffTag> mStuffTagCloud;
    Callback mCallback;

    public OnEnterCreateStuffTagInCloud(ArrayList<StuffTag> stuffTagCloud, Callback callback) {
        mStuffTagCloud = stuffTagCloud;
        mCallback = callback;
    }

    @Override
    public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
        StuffTag stuffTag;
        try {
            stuffTag = StuffTag.fromText(v.getText());
            mStuffTagCloud.add(stuffTag);

            /** reset input box on enter */
            v.setText("");
            
            mCallback.onStuffTagAdded();
        } catch (ParseException e) {
            // TODO e.g. return toast message: invalid tag
            e.printStackTrace();
        }

        return false;
    }
}