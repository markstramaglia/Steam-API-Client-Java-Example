package com.example.steamapiclient;

import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.ArrayList;
import java.util.List;

public class MainController {

    private List<Achievement> achievementList;

    @FXML
    private TextField textFieldAppId;
    @FXML
    private TextArea responseTextArea;
    @FXML
    private TableView<Achievement> achievementsTable;
    @FXML
    private TableColumn<Achievement, String> achievementName;
    @FXML
    private TableColumn<Achievement, Double> achievementPercent;

    @FXML
    public void initialize() {
        achievementList = new ArrayList<>();
        achievementName.setCellValueFactory(new PropertyValueFactory<Achievement,String>("name"));
        achievementPercent.setCellValueFactory(new PropertyValueFactory<Achievement,Double>("percent"));
    }

    @FXML
    public void refresh() {
        Task<List<Achievement>> task = new Task<List<Achievement>>() {
            @Override
            protected List<Achievement> call() throws Exception {
                return fetchData();
            }

            @Override
            protected void succeeded() {
                achievementsTable.getItems().clear();
                achievementsTable.getItems().addAll( getValue() );
            }
        };

        new Thread(task).start();
    }

    private List<Achievement> fetchData() {
        return achievementList;
    }


    @FXML
    protected void onGetAchievementsForAppButtonClick() {

        String appId = textFieldAppId.getText();
        getAchievementPercentagesForApp(appId);

    }

    private void getAchievementPercentagesForApp(String appId) {

        // Set the URL for the Steam Web Service, binding in the appId
        String url = "http://api.steampowered.com/ISteamUserStats/GetGlobalAchievementPercentagesForApp/v0002/?gameid=" + appId + "&format=json";

        // Invoke the Steam Web Service using the method below, and get back the response object
        HttpResponse<String> response = invokeWebService(url);

        // Check if the Steam API returned a valid response
        if(response.statusCode() == 200) {

            // Display the raw JSON response body in the text area
            responseTextArea.setText(response.body());

            // Reset the List
            achievementList = new ArrayList<>();

            // Parse the JSON Response data
            JSONObject jsonResponseBody = new JSONObject(response.body());
            // There should be a single "achievementpercentages" object
            JSONObject jsonAchievementPercentages = jsonResponseBody.getJSONObject("achievementpercentages");
            // There should be an array of "achievements" objects
            JSONArray jsonAchievements = jsonAchievementPercentages.getJSONArray("achievements");
            // Iterate through the array
            for(int i = 0; i < jsonAchievements.length(); i++) {
                JSONObject jsonAchievement = (JSONObject) jsonAchievements.get(i);
                // Add each new Achievement row to the List object
                achievementList.add( new Achievement(jsonAchievement.getString("name"), jsonAchievement.getDouble("percent")));
            }

            // Refresh the JavaFX TableView, reloading rows from list
            refresh();
        }
        else {
            responseTextArea.setText("ERROR: '" + appId + "' is not a valid Steam App ID, or App data is not public.");
        }
    }

    private HttpResponse<String> invokeWebService(String url) {

        HttpRequest request = null;
        HttpResponse<String> response = null;
        try {
            request = HttpRequest.newBuilder()
                    .uri(new URI(url))
                    .GET()
                    .build();

            response = HttpClient.newHttpClient()
                    .send(request, HttpResponse.BodyHandlers.ofString());

        } catch (URISyntaxException | IOException | InterruptedException e) {
            throw new RuntimeException(e);
        }

        return response;

    }
}