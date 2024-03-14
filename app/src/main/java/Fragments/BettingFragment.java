package Fragments;

// BettingFragment.java
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import Adapter.GrandPrixAdapter;
import Async.FetchRacesTask;
import Classes.GrandPrix;
import API.ServiceAPI;
import Async.OnRacesFetchedListener;
import com.example.formula_world.R;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;

import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

public class BettingFragment extends Fragment implements OnRacesFetchedListener {

    private ServiceAPI serviceAPI;
    private RecyclerView recyclerView;
    private GrandPrixAdapter grandPrixAdapter;

    public BettingFragment() {
        // Required empty public constructor
    }

    public static BettingFragment newInstance(String param1, String param2) {
        BettingFragment fragment = new BettingFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        serviceAPI = new ServiceAPI();
        fetchGrandPrix(); // Chargez la liste des Grands Prix (paris) depuis l'API
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_betting, container, false);

        recyclerView = view.findViewById(R.id.recyclerViewBetting);
        recyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));

        return view;
    }

    @Override
    public void onRacesFetched(String grandPrixXml) {
        // Parsez le XML et créez la liste des GrandPrix
        List<GrandPrix> grandPrixList = parseGrandPrixXml(grandPrixXml);

        // Initialisez et attachez l'adaptateur au RecyclerView
        grandPrixAdapter = new GrandPrixAdapter(grandPrixList);
        recyclerView.setAdapter(grandPrixAdapter);
    }

    private List<GrandPrix> parseGrandPrixXml(String grandPrixXml) {
        List<GrandPrix> grandPrixList = new ArrayList<>();
        Log.d("test",grandPrixXml);
        try {
            // Créez un objet DocumentBuilderFactory pour obtenir un objet DocumentBuilder
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();

            // Analysez la chaîne XML en un objet Document
            Document doc = builder.parse(new InputSource(new StringReader(grandPrixXml)));

            // Obtenez la liste des éléments 'Race' dans le document XML
            NodeList raceNodes = doc.getElementsByTagName("Race");
            for (int i = 0; i < raceNodes.getLength(); i++) {
                Element raceElement = (Element) raceNodes.item(i);
                GrandPrix grandPrix = new GrandPrix(
                        getElementTextContent(raceElement, "RaceName"),
                        getElementTextContent(raceElement, "CircuitName"),
                        getElementTextContent(raceElement, "Locality"),
                        getElementTextContent(raceElement, "Contry"),
                        getElementTextContent(raceElement, "Date"),
                        getElementTextContent(raceElement, "Time")
                );
                grandPrix.setRaceName(getElementTextContent(raceElement, "RaceName"));
                grandPrix.setCircuitName(getElementTextContent(raceElement, "CircuitName"));
                grandPrix.setDate(getElementTextContent(raceElement, "Date"));
                // Ajoutez l'objet GrandPrix à la liste
                grandPrixList.add(grandPrix);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return grandPrixList;
    }

    // Méthode utilitaire pour obtenir le contenu textuel d'un élément XML
    private String getElementTextContent(Element parentElement, String tagName) {
        NodeList nodeList = parentElement.getElementsByTagName(tagName);
        if (nodeList != null && nodeList.getLength() > 0) {
            return nodeList.item(0).getTextContent();
        }
        return "";
    }
    private List<GrandPrix> parseGrandPrixJson(String grandPrixJson) {
        List<GrandPrix> grandPrixList = new ArrayList<>();
        Log.d("test",grandPrixJson);
        return grandPrixList;
    }
    private void fetchGrandPrix() {
        // Exécutez la tâche asynchrone pour récupérer les données des Grands Prix depuis l'API
        new FetchRacesTask(serviceAPI, this).execute();
    }
}
