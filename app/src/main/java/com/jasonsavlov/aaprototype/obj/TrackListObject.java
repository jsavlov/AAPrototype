package com.jasonsavlov.aaprototype.obj;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by jason on 1/4/16.
 */
public interface TrackListObject
{
    void setIsDivider(boolean isDivider);
    void setIsDivider(boolean isDivider, int pos);
    boolean isDivider();
    boolean getIsDivider();
    void setDividerPosition(Integer pos);
    Integer getDividerPosition();
}
