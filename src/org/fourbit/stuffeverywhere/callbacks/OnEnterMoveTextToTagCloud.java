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
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

public class OnEnterMoveTextToTagCloud implements TextView.OnEditorActionListener {

    ViewGroup mTagCloud;

    public OnEnterMoveTextToTagCloud(EditText textView, ViewGroup tagCloud) {
        mTagCloud = tagCloud;
    }

    @Override
    public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
        mTagCloud.addView(createCloudEntry(mTagCloud.getContext(), v.getText()));
        v.setText("");
        return false;
    }

    private View createCloudEntry(Context context, CharSequence charSequence) {
        // TODO Qualify style, like android.R.layout.select_dialog_item
        TextView entry = new TextView(context);
        entry.setText(charSequence);
        entry.setTextAppearance(context, android.R.style.TextAppearance_Medium);
        entry.setTextColor(context.getResources().getColor(android.R.color.primary_text_dark));
        return entry;
    }
}