package com.jasonsavlov.aaprototype.ui.format;

import com.jasonsavlov.aaprototype.obj.AASong;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by jason on 1/4/16.
 */
public final class TrackNumberFormatter
{

    private TrackNumberFormatter() {}

    public static ArrayList<AASong> generateListWithDividers(ArrayList<AASong> listOfSongs)
    {
        ArrayList<AASong> listToReturn = new ArrayList<>();

        AASong firstSong = listOfSongs.get(0);

        if (firstSong.getTrackNumString().length() != 4)
            return listOfSongs;

        int i = 0;
        int currentDiscNumber = 0;
        while (i < listOfSongs.size())
        {
            AASong workingSong = listOfSongs.get(i);
            String songTrackStr = workingSong.getTrackNumString();

            int discTrackNumber = Character.getNumericValue(songTrackStr.charAt(0));

            if (discTrackNumber == (currentDiscNumber + 1))
            {
                currentDiscNumber = discTrackNumber;
                //listToReturn.add(new DiscDivider(discTrackNumber));
                workingSong.setIsStartingTrack(true);
            }

            workingSong.setDiscNumber(discTrackNumber);

            String truncatedTrackNumber = songTrackStr.substring(1, songTrackStr.length());
            int trackFromTruncating = Integer.parseInt(truncatedTrackNumber);
            workingSong.setTrackNumString(String.valueOf(trackFromTruncating));

            listToReturn.add(workingSong);
            ++i;
        }

        return listToReturn;
    }

    public static boolean processDiscNumbers(List<AASong> listOfSongs)
    {
        AASong firstSong = listOfSongs.get(0);

        if (firstSong.getTrackNumString().length() != 4)
            return false;

        int i = 0;
        int currentDiscNumber = 0;
        while (i < listOfSongs.size())
        {
            AASong workingSong = listOfSongs.get(i);
            String songTrackStr = workingSong.getTrackNumString();

            int discTrackNumber = Character.getNumericValue(songTrackStr.charAt(0));

            if (discTrackNumber == (currentDiscNumber + 1))
            {
                currentDiscNumber = discTrackNumber;
                //listToReturn.add(new DiscDivider(discTrackNumber));
                workingSong.setIsStartingTrack(true);
            }

            workingSong.setDiscNumber(discTrackNumber);

            String truncatedTrackNumber = songTrackStr.substring(1, songTrackStr.length());
            int trackFromTruncating = Integer.parseInt(truncatedTrackNumber);
            workingSong.setTrackNumString(String.valueOf(trackFromTruncating));

            ++i;
        }
        return true;
    }



}
