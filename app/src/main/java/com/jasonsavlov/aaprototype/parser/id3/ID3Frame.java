package com.jasonsavlov.aaprototype.parser.id3;

import java.util.Arrays;

/**
 * Created by jason on 1/21/16.
 */
public class ID3Frame
{
    // Instance variables
    private final ID3FrameNames frameName;
    private final byte[] data;
    private final int size;
    private final byte[] flags;

    public ID3Frame(
        ID3FrameNames _name,
        byte[] _data,
        int _size,
        byte[] _flags)
    {
        this.frameName = _name;
        this.data = _data;
        this.size = _size;
        this.flags = _flags;
    }

    public byte[] getData()
    {
        return data;
    }

    public ID3FrameNames getFrameName()
    {
        return frameName;
    }

    public int getSize()
    {
        return size;
    }

    public byte[] getFlags()
    {
        return flags;
    }

    @Override
    public String toString()
    {
        return "ID3Frame{" +
                "data=" + Arrays.toString(data) +
                ", frameName=" + frameName +
                ", size=" + size +
                ", flags=" + Arrays.toString(flags) +
                '}';
    }
}
