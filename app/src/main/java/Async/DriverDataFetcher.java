package Async;

import android.os.AsyncTask;

import java.io.IOException;

import API.ServiceAPI;

public class DriverDataFetcher extends AsyncTask<Void, Void, String> {
    private final ServiceAPI serviceAPI;
    private final OnDriverFetchedListener listener;

    public DriverDataFetcher(ServiceAPI serviceAPI, OnDriverFetchedListener listener) {
        this.serviceAPI = serviceAPI;
        this.listener = listener;
    }


    @Override
    protected String doInBackground(Void... voids) {
        try {
            return serviceAPI.getRaces();
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