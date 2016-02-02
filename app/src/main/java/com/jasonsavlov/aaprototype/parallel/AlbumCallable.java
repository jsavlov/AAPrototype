package com.jasonsavlov.aaprototype.parallel;

import com.jasonsavlov.aaprototype.obj.AAAlbum;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;

/**
 * Created by jason on 12/24/15.
 */
public class AlbumCallable implements Callable<ArrayList<AAAlbum>>
{
    // Instance variables
    private int start_point;
    private int step_factor;
    private List<AAAlbum> original_list;

    // Main c-tor
    public AlbumCallable(int _start,
                         int _steps,
                         List<AAAlbum> _list)
    {
        start_point = _start;
        step_factor = _steps;
        original_list = _list;
    }

    // This is where the action happens
    @Override
    public ArrayList<AAAlbum> call() throws Exception
    {
        // The list that will be returned when execution finishes.
        ArrayList<AAAlbum> listToReturn = new ArrayList<>();

        for (int i = 0; i < step_factor; i++)
        {
            listToReturn.add(original_list.get(i + start_point));
        }

        return listToReturn;
    }
}
