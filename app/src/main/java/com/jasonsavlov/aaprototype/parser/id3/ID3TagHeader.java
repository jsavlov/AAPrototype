package com.jasonsavlov.aaprototype.parser.id3;

import java.util.Arrays;

/**
 * Created by jason on 1/21/16.
 */
public class ID3TagHeader
{
    public final int size;
    public final byte[] version;
    public final byte flags;

    public ID3TagHeader(
            int _size,
            byte[] _version,
            byte _flags)
    {
        this.size = _size;
        this.version = _version;
        this.flags = _flags;
    }

    @Override
    public String toString()
    {
        return "ID3TagHeader{" +
                "flags=" + flags +
                ", size=" + size +
                ", version=" + Arrays.toString(version) +
                '}';
    }
}