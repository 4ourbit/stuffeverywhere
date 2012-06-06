package org.fourbit.stuffeverywhere.model;

public class MalformedStuffTagException extends Exception {

    private static final long serialVersionUID = 8834572795380789409L;

    public MalformedStuffTagException(String detailMessage) {
        super(detailMessage);
    }
}