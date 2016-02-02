package com.jasonsavlov.aaprototype.parser.id3;

import org.junit.BeforeClass;
import org.junit.Test;


import java.io.File;
import java.util.Arrays;

import org.apache.commons.io.FileUtils;

import static org.junit.Assert.*;

/**
 * Created by jason on 1/23/16.
 */
public class ID3TagParserTest
{
    private static File fileToTest; // The file to test
    private static File albumArtworkImageFile;
    private static final String fileDirectory = "/Users/jason/Music/iTunes/iTunes Music/Soundgarden/Badmotorfinger/01 Rusty Cage.mp3"; // The directory of the file to test.
    private static final String artworkDirectory = "/Users/jason/Documents/71HlQFwSF2L._SL1042_.jpg";
    private static ID3Tag tagTested = null;
    private static ID3TagParser parser = null;
    private static final String TEXT_ENCODING = "UTF-8";

    // Constants used for testing
    private static final String correctArtistName = "Soundgarden";
    private static final String correctAlbumName = "Badmotorfinger";
    private static final String correctSongName = "Rusty Cage";


    @BeforeClass
    public static void setupBeforeClass()
    {
        // Open the file
        fileToTest = new File(fileDirectory);
        albumArtworkImageFile = new File(artworkDirectory);


        // Set up the parser
        parser = new ID3TagParser(fileToTest);
        tagTested = parser.parseTags();
    }

    // Test to make sure the artist is correct

    @Test
    public void testArtistName() throws Exception
    {
        byte[] artistNameData = tagTested.getFrameByName(ID3FrameNames.TPE1).getData();
        String artistNameString = ID3Tag.getStringFromData(artistNameData);
        assertTrue(artistNameString.equals(correctArtistName));
    }

    @Test
    public void testAlbumName() throws Exception
    {
        byte[] albumNameData = tagTested.getFrameByName(ID3FrameNames.TALB).getData();
        String albumNameString = ID3Tag.getStringFromData(albumNameData);
        assertTrue(albumNameString.equals(correctAlbumName));
    }

    @Test
    public void testSongName() throws Exception
    {
        byte[] songNameData = tagTested.getFrameByName(ID3FrameNames.TIT2).getData();
        String songNameString = ID3Tag.getStringFromData(songNameData);
        assertTrue(songNameString.equals(correctSongName));
    }

    @Test
    public void testAlbumArtFrame() throws Exception
    {
        ID3Frame frame = tagTested.getFrameByName(ID3FrameNames.APIC);
        ID3ArtworkFrame artworkFrame = ID3ArtworkFrame.generateFromBytes(frame.getData());

        byte[] knownArtworkFileData = FileUtils.readFileToByteArray(albumArtworkImageFile);
        byte[] artworkFrameImageData = artworkFrame.pictureData;

        assertTrue(Arrays.equals(knownArtworkFileData, artworkFrameImageData));
    }

    @Test
    public void testSyncsafeConversion() throws Exception
    {
        final byte[] nonsyncsafe = {0x1D, 0x65, (byte) 0xD6, (byte) 0xD7};
        final int nonsyncsafe_hex = 0x1D65D6D7; // Decimal: 493213399

        final byte[] syncsafe = {0x75, 0x4B, 0x2C, 0x57};
        final int syncsafe_hex = 0x754B2C57; // Decimal: 1967860823

        int unsync_conversion = ID3TagParser.unsyncsafeIntFromBytes(syncsafe);
        System.out.println("nonsyncsafe_hex = " + nonsyncsafe_hex + ", unsync_conversion = " + unsync_conversion);
        //assertEquals(nonsyncsafe_hex, unsync_conversion);

        byte[] sync_conversion = ID3TagParser.syncSafeIntegerFromInt(nonsyncsafe_hex);
        System.out.println("syncsafe_hex = " + syncsafe.toString() + ", sync_conversion = " + sync_conversion.toString());
        assertArrayEquals(syncsafe, sync_conversion);
    }

}