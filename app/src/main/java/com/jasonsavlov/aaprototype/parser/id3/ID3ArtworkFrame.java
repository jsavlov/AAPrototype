package com.jasonsavlov.aaprototype.parser.id3;

import java.io.ByteArrayInputStream;
import java.io.Serializable;
import java.nio.ByteBuffer;
import java.nio.charset.Charset;

/**
 * Created by jason on 1/24/16.
 */
public final class ID3ArtworkFrame
{

    // constants defined during construction.
    public final int size;
    public final byte textEncoding;
    public final String mimeString;
    public final byte pictureType;
    public final String imageDescriptionString;
    public final byte[] pictureData;


    public ID3ArtworkFrame(String mimeString, int size, byte textEncoding, byte pictureType, String imageDescriptionString, byte[] pictureData)
    {
        this.mimeString = mimeString;
        this.size = size;
        this.textEncoding = textEncoding;
        this.pictureType = pictureType;
        this.imageDescriptionString = imageDescriptionString;
        this.pictureData = pictureData;
    }


    public static final ID3ArtworkFrame generateFromBytes(byte[] data)
    {
        int bytesRead = 0;
        ByteArrayInputStream inputStream = new ByteArrayInputStream(data);
        byte textEncoding = (byte) inputStream.read();
        bytesRead++;
        Charset textCharset = getCharsetForByte(textEncoding);

        inputStream.mark(32);
        int mimeByteLength = 0;
        while (inputStream.read() != 0)
            ++mimeByteLength;

        inputStream.reset();
        byte[] mimeTypeBytes = new byte[mimeByteLength];
        inputStream.read(mimeTypeBytes, 0, mimeByteLength);
        bytesRead += mimeByteLength;
        inputStream.skip(1);
        bytesRead++;

        byte pictureType = (byte) inputStream.read();
        bytesRead++;

        inputStream.mark(1000);
        int descriptionLength = 0;
        while (inputStream.read() != 0)
            descriptionLength++;

        byte[] descriptionBytes = new byte[descriptionLength];
        String description = null;
        inputStream.reset();
        if (descriptionBytes.length != 0) {
            inputStream.read(descriptionBytes, 0, descriptionBytes.length);
            description = new String(descriptionBytes, textCharset);
            bytesRead += descriptionBytes.length;
        }
        inputStream.skip(1);
        bytesRead++;

        byte[] imageData = new byte[data.length - bytesRead];
        inputStream.read(imageData, 0, imageData.length);

        return new ID3ArtworkFrame(new String(mimeTypeBytes, textCharset),
                data.length,
                textEncoding,
                pictureType,
                description,
                imageData);
    }

    private static final Charset getCharsetForByte(byte b)
    {
        switch (b) {
            case 0:
                return Charset.forName("ISO-8859-1");
            case 1:
                return Charset.forName("UTF-16LE");
            case 2:
                return Charset.forName("UTF-16BE");
            case 3:
                return Charset.forName("UTF-8");
            default:
                return null;
        }
    }

}
