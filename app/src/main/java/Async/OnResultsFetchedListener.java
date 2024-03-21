package Async;

import java.util.List;

import Classes.GrandPrix.Results;

public interface OnResultsFetchedListener {
    void onResultsFetched(List<Results> results);
}

