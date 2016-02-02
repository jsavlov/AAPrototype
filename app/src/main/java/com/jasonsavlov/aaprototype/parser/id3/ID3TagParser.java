package com.jasonsavlov.aaprototype.parser.id3;

import android.util.Log;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.ObjectOutput;
import java.nio.ByteBuffer;
import java.nio.charset.Charset;

/**
 * Created by jason on 1/18/16.
 */
public class ID3TagParser
{

    private static final String TAG = ID3TagParser.class.getSimpleName();

    /*
     * Instance variables
     */

    private final File songFile;
    private int bytesRead = 0;
    private int totalSize = 0;
    ID3TagFlags currentFlags;


    public ID3TagParser(File file)
    {
        this.songFile = file;
    }

    public ID3Tag parseTags()
    {
        ID3Tag tagToReturn = null;
        DataInputStream inputStream;
        try {
            inputStream = new DataInputStream(new BufferedInputStream(new FileInputStream(songFile)));
            try {
                tagToReturn = new ID3Tag(processHeader(inputStream));
                currentFlags = ID3TagFlags.flagsFromByte(tagToReturn.getHeader().flags);

                if (tagToReturn != null) {
                    bytesRead += 8;
                }

                totalSize = tagToReturn.getHeader().size;

                ID3Frame workingFrame;
                while ((workingFrame = processFrame(inputStream)) != null)
                {
                    tagToReturn.addFrame(workingFrame);
                }
            } finally {
                Log.d(TAG, "parseTags: Closing input stream...");
                inputStream.close();
            }
        } catch (FileNotFoundException ex) {
            Log.e(TAG, "parseTags: File was not found.", ex);
        } catch (IOException ex) {
            Log.e(TAG, "parseTags: There was an IO issue.", ex);
        }

        return tagToReturn;
    }


    private ID3TagHeader processHeader(DataInputStream inputStream) throws IOException
    {
        if (inputStream == null)
            throw new NullPointerException("inputStream was null.");

        byte[] id3byte = new byte[3];
        inputStream.read(id3byte);
        if (!(new String(id3byte, "UTF-8").equals("ID3"))) {
            Log.e(TAG, "processHeader: did not find the ID3 tag at the beginning of file.");
            return null;
        }
        bytesRead += id3byte.length;

        byte maj_ver = inputStream.readByte();
        byte min_ver = inputStream.readByte();
        byte[] ver = {maj_ver, min_ver};
        bytesRead += 2;

        byte flags = inputStream.readByte();
        bytesRead++;

        byte[] sizeBytes = new byte[4];
        inputStream.read(sizeBytes);
        bytesRead += sizeBytes.length;
        //int size = ByteBuffer.wrap(sizeBytes).getInt();
        int size = unsyncsafeIntFromBytes(sizeBytes);

        return new ID3TagHeader(size, ver, flags);
    }

    private ID3Frame processFrame(DataInputStream inputStream) throws IOException
    {
        ID3Frame frameToReturn;
        String fName;
        ID3FrameNames frameValue;
        int bytesReadInFrame = 0;

        byte[] frameBytes = new byte[4];
        inputStream.read(frameBytes);
        bytesRead += frameBytes.length;
        bytesReadInFrame += frameBytes.length;

        fName = new String(frameBytes);

        try {
            frameValue = Enum.valueOf(ID3FrameNames.class, fName);
            Log.d(TAG, "processFrame: processing frame: " + frameValue);
        } catch (IllegalArgumentException ex) {
            Log.i(TAG, "processFrame: frameValue produced IllegalArgumentException. Returning null. fName = " + fName);
            return null;
        }



        byte[] frameSizeBytes = new byte[4];
        inputStream.read(frameSizeBytes);

        int frameSize = unsyncsafeIntFromBytes(frameSizeBytes);
        bytesRead += frameSizeBytes.length;
        bytesReadInFrame += frameSizeBytes.length;

        byte[] flags = new byte[2];
        inputStream.read(flags);
        bytesRead += flags.length;
        bytesReadInFrame += flags.length;

        byte[] data = new byte[frameSize];
        inputStream.read(data);
        bytesRead += frameSize;

        frameToReturn = new ID3Frame(frameValue, data, frameSize, flags);

        return frameToReturn;
    }

    public static final int unsyncsafeIntFromBytes(byte[] b)
    {

        return ((b[0] & 0xFF) << 21) + ((b[1] & 0xFF) << 14) + ((b[2] & 0xFF) << 7) + ((b[3]) & 0xFF);

    }

    public static final byte[] syncSafeIntegerFromInt(int in)
    {
        byte[] buf = new byte[4];

        buf[0] = (byte) ((in & 0x0FE00000) >> 21);
        buf[1] = (byte) ((in & 0x001FC000) >> 14);
        buf[2] = (byte) ((in & 0x00003F80) >> 7);
        buf[3] = (byte) (in & 0x0000007F);

        return buf;
    }
}
