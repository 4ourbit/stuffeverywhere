package org.fourbit.stuffeverywhere.model;


public class StuffTag {

    CharSequence mLabel;
    CharSequence mValue;

    private StuffTag(CharSequence label, CharSequence value) {
        // FIXME java.lang.NoClassDefFoundError: android.test.MoreAsserts on device!?
        // MoreAsserts.assertNotEqual(label.toString(), "#");
        // Assert.assertEquals(label.toString(), label.toString().trim());
        // Assert.assertEquals(value.toString(), value.toString().trim());
        // MoreAsserts.assertNotContainsRegex("\\s+", label.toString());

        mLabel = label;
        mValue = value;
    }

    public static StuffTag fromText(CharSequence rawInput) throws MalformedStuffTagException {
        return fromText(rawInput, '#');
    }

    /**
     * Creates a tag with first occurrence of splitter treated as tag/value separator. Malformed
     * input fails with {@link MalformedStuffTagException}.
     * 
     * @param rawInput
     * @return the tag
     * @throws MalformedStuffTagException occurs when rawInput:<br>
     *             <li>splitter is the first char in rawInput</li><br>
     *             <li>tag or value are trimable</li><br>
     *             <li>tag is empty</li><br>
     *             <li>tag contains whitespaces</li><br>
     */
    public static StuffTag fromText(CharSequence rawInput, char splitter) throws MalformedStuffTagException {
        // split into label and value
        CharSequence label = "", value = "";

        int firstSplit = rawInput.toString().indexOf(splitter);
        if (firstSplit == 0)
            throw new MalformedStuffTagException("" + splitter + " is not allowed as beginning of a tag");

        if (firstSplit < 0) {
            label = rawInput;
        } else {
            label = rawInput.subSequence(0, firstSplit);
            value = rawInput.subSequence(firstSplit + 1, rawInput.length());
        }

        if (!label.toString().equals(label.toString().trim()))
            throw new MalformedStuffTagException("The tag is not allowed to be trimable");
        if (!value.toString().equals(value.toString().trim()))
            throw new MalformedStuffTagException("The value is not allowed to be trimable");
        if ("".equals(label))
            throw new MalformedStuffTagException("The tag is not allowed to be empty");
        if (java.util.regex.Pattern.matches("\\s+", label))
            throw new MalformedStuffTagException("The tag is not allowed to have whitespaces");
        // COMMENT for the reason of visually separating tags
        
        // TODO normalize UTF before storing and exchanging
        // label = Normalizer.normalize(rawInput, Normalizer.Form.NFC);

        return new StuffTag(label, value);
    }

    @Override
    public String toString() {
        return "" + mLabel + (!mValue.equals("") ? "#" + mValue : "");
    }
}