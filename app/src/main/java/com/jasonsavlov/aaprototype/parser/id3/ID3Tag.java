package com.jasonsavlov.aaprototype.parser.id3;


import java.nio.charset.Charset;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by jason on 1/18/16.
 */
public class ID3Tag
{

    private static final String TAG = ID3Tag.class.getSimpleName();

    // Instance variables
    private final ConcurrentHashMap<ID3FrameNames, ID3Frame> frameMap = new ConcurrentHashMap<>();
    private final ID3TagHeader header;
    private ID3TagFlags tagFlags;


    public ID3Tag(ID3TagHeader h)
    {
        this.header = h;
        this.tagFlags = ID3TagFlags.flagsFromByte(this.header.flags);
    }

    public ID3TagHeader getHeader()
    {
        return header;
    }

    public void addFrame(ID3Frame frame)
    {
        this.frameMap.put(frame.getFrameName(), frame);
    }

    public ID3Frame getFrameByName(ID3FrameNames frame)
    {
        return this.frameMap.get(frame);
    }

    public static String getStringFromData(byte[] data)
    {
        byte encodingType = data[0];
        Charset charset;
        switch (encodingType) {
            case 0:
                charset = Charset.forName("ISO-8859-1");
                break;
            case 1:
                charset = Charset.forName("UTF-16LE");
                break;
            case 2:
                charset = Charset.forName("UTF-16BE");
                break;
            case 3:
                charset = Charset.forName("UTF-8");
                break;
            default:
                throw new CharacterSetNotSupportedException("The character set byte doesn't match any acceptable character sets...");
        }

        // FIXME: This belongs in each appropriate switch case
        String stringToReturn = new String(data, 3, data.length - 3, charset);

        return stringToReturn;
    }

    @Override
    public String toString()
    {
        return "ID3Tag{" +
                "frameMap=" + frameMap +
                ", header=" + header +
                ", tagFlags=" + tagFlags +
                '}';
    }

    private static class CharacterSetNotSupportedException extends RuntimeException {
        public CharacterSetNotSupportedException(String detailMessage)
        {
            super(detailMessage);
        }
    }
}


