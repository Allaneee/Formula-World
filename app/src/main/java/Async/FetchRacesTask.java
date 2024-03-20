package Async;

import android.os.AsyncTask;

import java.io.IOException;

import API.ServiceAPI;

public class FetchRacesTask extends AsyncTask<Void, Void, String> {
    private final ServiceAPI serviceAPI;
    private final OnRacesFetchedListener listener;

    public FetchRacesTask(ServiceAPI serviceAPI, OnRacesFetchedListener listener) {
        this.serviceAPI = serviceAPI;
        this.listener = listener;
    }

    @Override
    protected String doInBackground(Void... voids) {
        try {
            return serviceAPI.getRaceResult();
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    protected void onPostExecute(String result) {
        if (listener != null) {
            listener.onRacesFetched(result);
        }
    }
}
