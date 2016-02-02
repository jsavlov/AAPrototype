package com.jasonsavlov.aaprototype.parser.id3;

/**
 * Created by jason on 1/21/16.
 */
public final class ID3TagFlags {
    final boolean unsynchronization;
    final boolean extendedHeader;
    final boolean experimental;
    final boolean footerPresent;

    public ID3TagFlags(
            boolean unsynchronization,
            boolean extendedHeader,
            boolean experimental,
            boolean footerPresent)
    {
        this.unsynchronization = unsynchronization;
        this.extendedHeader = extendedHeader;
        this.experimental = experimental;
        this.footerPresent = footerPresent;
    }

    public static ID3TagFlags flagsFromByte(byte flags)
    {
        for (int i = 0; i < 4; i++)
        {
            if ((flags >>> (i+1) & 1) != 0)
            {
                throw new InvalidFlagByteException("Invalid cleared bit at position " + i);
            }
        }

        boolean a, b, c, d;
        a = (flags >>> 7 & 1) == 1;
        b = (flags >>> 6 & 1) == 1;
        c = (flags >>> 5 & 1) == 1;
        d = (flags >>> 4 & 1) == 1;

        return new ID3TagFlags(a, b, c, d);
    }

    public final byte getFlagByte()
    {
        return getFlagBytes(this);
    }

    public static final byte getFlagBytes(ID3TagFlags flags)
    {

        return 0;
    }

    @Override
    public String toString()
    {
        return "ID3TagFlags{" +
                "experimental=" + experimental +
                ", unsynchronization=" + unsynchronization +
                ", extendedHeader=" + extendedHeader +
                ", footerPresent=" + footerPresent +
                '}';
    }

    private static class InvalidFlagByteException extends RuntimeException
    {
        public InvalidFlagByteException(String detailMessage)
        {
            super(detailMessage);
        }
    }

}

