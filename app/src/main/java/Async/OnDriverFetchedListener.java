package Async;

import java.util.List;

import Classes.Driver;

public interface OnDriverFetchedListener {
    List<Driver> onDriverFetched(String racesJson);
}