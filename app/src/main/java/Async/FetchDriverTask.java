package Async;

import android.os.AsyncTask;

import java.io.IOException;

import API.ServiceAPI;

public class FetchDriverTask extends AsyncTask<Void, Void, String> {
    private final ServiceAPI serviceAPI;
    private final OnDriverFetchedListener listener;

    public FetchDriverTask(ServiceAPI serviceAPI, OnRacesFetchedListener listener) {
        this.serviceAPI = serviceAPI;
        this.listener = (OnDriverFetchedListener) listener;
    }

    @Override
    protected String doInBackground(Void... voids) {
        try {
            return serviceAPI.getDrivers();
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    protected void onPostExecute(String result) {
        if (listener != null) {
            listener.onDriverFetched(result);
        }
    }
}