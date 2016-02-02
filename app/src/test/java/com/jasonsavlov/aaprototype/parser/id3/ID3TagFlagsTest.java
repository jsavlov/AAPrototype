package com.jasonsavlov.aaprototype.parser.id3;

import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Created by jason on 1/23/16.
 */
public class ID3TagFlagsTest
{


    @Test
    public void testFlagsFromByte() throws Exception
    {
        byte byteToTest = (byte) 0b11000000;

        ID3TagFlags flags = ID3TagFlags.flagsFromByte(byteToTest);
        assertTrue(flags.unsynchronization == true);
        assertTrue(flags.extendedHeader == true);
    }
}