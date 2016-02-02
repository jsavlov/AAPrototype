package com.jasonsavlov.aaprototype.ui.format;

/**
 * Created by jason on 1/4/16.
 */
final class InvalidTimeStringException extends RuntimeException
{
    public InvalidTimeStringException(String detailMessage)
    {
        super(detailMessage);
    }
}
