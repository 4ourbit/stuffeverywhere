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

import android.view.View;

public class OnTagAddedUpdateView implements OnEnterMoveTextToTagCloud.Callback {

    View mView;

    public OnTagAddedUpdateView(View view) {
        mView = view;
    }

    @Override
    public void onStuffTagAdded() {
        mView.requestLayout();
    }
}
