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
import android.text.Editable;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnKeyListener;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

public class OnEnterMoveTextToTagCloud implements OnKeyListener {

    EditText mEditText;
    ViewGroup mTagCloud;

    public OnEnterMoveTextToTagCloud(EditText textView, ViewGroup tagCloud) {
        mEditText = textView;
        mTagCloud = tagCloud;
    }

    public boolean onKey(View v, int keyCode, KeyEvent event) {
        if ((event.getAction() == KeyEvent.ACTION_DOWN) && (keyCode == KeyEvent.KEYCODE_ENTER)) {
            TextView entry = createCloudEntry(mTagCloud.getContext(), mEditText.getText());

            mTagCloud.addView(entry);
            mEditText.setText("");
            return false;
        }
        return false;
    }

    private TextView createCloudEntry(Context context, Editable text) {
        TextView entry = new TextView(context);
        entry.setText(text);
        entry.setTextAppearance(context, android.R.style.TextAppearance_Medium);
        return entry;
    }
}
