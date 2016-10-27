package com.example.rm71058.am;

import android.app.ProgressDialog;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.ProgressBar;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private ListView videosListView;
    private VideoDAO dao;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        videosListView = (ListView) findViewById(R.id.videosListView);
        dao = new VideoDAO(this);
    }

    public void search(View v){
        SearchTask task = new SearchTask();
        task.execute();
    }

    private class SearchTask extends AsyncTask<String, Void, String> {

        private ProgressDialog progressDialog;
        SharedPreferences sp = getPreferences(MODE_PRIVATE);
        int countSearch = sp.getInt("count_search", 0);
        int lastCode = dao.lastCode();

        @Override
        protected String doInBackground(String... params) {
            HttpURLConnection connection = null;
            try {
                if(lastCode == 0 || countSearch > 10) {
                    URL url = new URL("http://ws.qoala.com.br/ITIssues/itflix");
                    connection = (HttpURLConnection) url.openConnection();

                    connection.setRequestMethod("GET");
                    connection.setRequestProperty("context-type", "text/json");

                    if(connection.getResponseCode() == 200) {
                        BufferedReader stream = new BufferedReader(
                                new InputStreamReader(connection.getInputStream())
                        );

                        String line = "";
                        StringBuilder response = new StringBuilder();
                        while((line = stream.readLine()) != null) {
                            response.append(line);
                        }

                        incrementCountSearch();

                        return response.toString();
                    }
                }
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                if(connection != null)
                    connection.disconnect();
            }
            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            progressDialog.dismiss();
            if(s != null) {
                try {
                    JSONArray jsonArray = new JSONArray(s);

                    List<Video> videos = new ArrayList<>();

                    for(int i = 0; i < jsonArray.length(); i++){
                        JSONObject video = jsonArray.getJSONObject(i);
                        int code = (int) video.getLong("codigo");
                        int time = (int) video.getLong("tempo");
                        String description = video.getString("descricao");
                        if(code > lastCode)
                            dao.insert(code, time, description);
                        videos.add(new Video(code, time, description));
                    }

                    setListAdapter(videos);

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            } else {
                incrementCountSearch();
                setListAdapter(dao.all());
            }
        }
        private void incrementCountSearch() {
            SharedPreferences.Editor e = sp.edit();
            e.putInt("count_search", (countSearch > 10) ? 1 : countSearch + 1);
            e.commit();
        }

        private void setListAdapter(List<Video> videos) {
            ListAdapter adapter = new ArrayAdapter<>(MainActivity.this, android.R.layout.simple_list_item_1, videos);
            videosListView.setAdapter(adapter);
        }

        @Override
        protected void onPreExecute() {
            progressDialog = ProgressDialog.show(MainActivity.this, "Aguarde..", "Buscando dados do servidor");
        }
    }
}
