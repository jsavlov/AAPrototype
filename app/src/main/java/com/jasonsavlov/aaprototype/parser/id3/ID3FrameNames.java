package com.jasonsavlov.aaprototype.parser.id3;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by jason on 1/23/16.
 */
public enum ID3FrameNames
{
    /*
        From the ID3v2.4 site:
        http://id3.org/id3v2.4.0-frames
      */

    /*
     *  This enum is meant to be used as follows:
     *  The names of the frames are in an array of Strings that are also
     *  defined in this enum in the same order as they are in the array.
     *  Use 'ID3FrameNames.[enum_name]' to get the String representation
     *  of the frame using the getFrameNameString([enum_name]).
     */

    AENC, APIC, ASPI, COMM, COMR, TYER,
    ENCR, EQU2, ETCO, GEOB, GRID, LINK,
    MCDI, MLLT, OWNE, PRIV, PCNT, POPM,
    POSS, RBUF, RVA2, RVRB, SEEK, SIGN,
    SYLT, SYTC, TALB, TBPM, TCOM, TCON,
    TCOP, TDEN, TDLY, TDOR, TDRC, TDRL,
    TDTG, TENC, TEXT, TFLT, TIPL, TIT1,
    TIT2, TIT3, TKEY, TLAN, TLEN, TMCL,
    TMED, TMOO, TOAL, TOFN, TOLY, TOPE,
    TOWN, TPE1, TPE2, TPE3, TPE4, TPOS,
    TPRO, TPUB, TRCK, TRSN, TRSO, TSOA,
    TSOP, TSOT, TSRC, TSSE, TSST, UFID,
    USER, USLT, WCOM, WCOP, WOAF, WOAR,
    WOAS, WORS, WPAY, WPUB, TXXX;


    private static final String[] frame_names = {
            AENC.name(), APIC.name(), ASPI.name(), COMM.name(), COMR.name(), TYER.name(),
            ENCR.name(), EQU2.name(), ETCO.name(), GEOB.name(), GRID.name(), LINK.name(),
            MCDI.name(), MLLT.name(), OWNE.name(), PRIV.name(), PCNT.name(), POPM.name(),
            POSS.name(), RBUF.name(), RVA2.name(), RVRB.name(), SEEK.name(), SIGN.name(),
            SYLT.name(), SYTC.name(), TALB.name(), TBPM.name(), TCOM.name(), TCON.name(),
            TCOP.name(), TDEN.name(), TDLY.name(), TDOR.name(), TDRC.name(), TDRL.name(),
            TDTG.name(), TENC.name(), TEXT.name(), TFLT.name(), TIPL.name(), TIT1.name(),
            TIT2.name(), TIT3.name(), TKEY.name(), TLAN.name(), TLEN.name(), TMCL.name(),
            TMED.name(), TMOO.name(), TOAL.name(), TOFN.name(), TOLY.name(), TOPE.name(),
            TOWN.name(), TPE1.name(), TPE2.name(), TPE3.name(), TPE4.name(), TPOS.name(),
            TPRO.name(), TPUB.name(), TRCK.name(), TRSN.name(), TRSO.name(), TSOA.name(),
            TSOP.name(), TSOT.name(), TSRC.name(), TSSE.name(), TSST.name(), UFID.name(),
            USER.name(), USLT.name(), WCOM.name(), WCOP.name(), WOAF.name(), WOAR.name(),
            WOAS.name(), WORS.name(), WPAY.name(), WPUB.name(), TXXX.name()
    };



    public static final List<String> frameNameList;

    static {
        List<String> _list = new ArrayList<>();
        for (String s : frame_names) {
            _list.add(s);
        }
        frameNameList = Collections.unmodifiableList(_list);
    }

    public static final String getFrameNameString(ID3FrameNames frame)
    {
        return frame_names[frame.ordinal()];
    }
}
