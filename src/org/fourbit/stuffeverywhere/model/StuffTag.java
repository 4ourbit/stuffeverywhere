package org.fourbit.stuffeverywhere.model;

import java.text.ParseException;

import android.text.TextUtils;

public class StuffTag {

    String mLabel;
    String mValue;

    private StuffTag(String label, String value) {
        mLabel = label;
        mValue = value;
    }

    /**
     * Creates a tag from a CharSequence. Malformed input won't fail silently.
     * 
     * @param rawInput
     * @return the tag
     * @throws ParseException occurs when rawInput:<br>
     *             <li>strings between splitter (#) are trimable</li><br>
     *             <li>string before splitter contains whitespaces (for the reason of visually
     *             separating tags)</li>
     */
    public static StuffTag fromText(CharSequence rawInput) throws ParseException {
        // split into label and value
        String label = null;
        String value = null;
        TextUtils.StringSplitter splitter = new TextUtils.SimpleStringSplitter('#');
        splitter.setString(rawInput.toString());

        for (String s : splitter) {

        }

        // TODO normalize UTF before storing and exchanging
        // label = Normalizer.normalize(rawInput, Normalizer.Form.NFC);

        return new StuffTag(label, value);
    }

    @Override
    public String toString() {
        return "" + mLabel + (mValue != null ? "#" + mValue : "");
    }
}
