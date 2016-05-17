package com.example.Fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.Toast;

import com.example.Database.Entities.EntityBase;
import com.example.Database.Entities.StoresEntity;
import com.example.Database.Repositories.IRepository;
import com.example.Database.Repositories.RepositoryContainer;
import com.example.Database.Repositories.RepositoryNames;
import com.example.models.StoresViewModel;
import com.example.omur.navigationavm.DatabaseScreen;
import com.example.omur.navigationavm.LocationScreen;
import com.example.omur.navigationavm.R;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.utils.URLEncodedUtils;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;

import android.os.AsyncTask;
import android.app.ProgressDialog;


public class Fragment1 extends Fragment
{
    private final String FILENAME = "destination";

    Button goDatabaseButton, goLocationScreenButton;
    private AutoCompleteTextView autoCompleteTextView;
    private Vector<StoresViewModel> StoreList;
    private RepositoryContainer repositoryContainer;
    private IRepository repository;
    int storeid;
    StoresEntity se ;
    ProgressDialog pDialog;
    String url = "http://omurbyv-001-site1.atempurl.com/MostSearched/Create";
    InputStream veri;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.fragment1,container , false);

        autoCompleteTextView = (AutoCompleteTextView) v.findViewById(R.id.chooseStore);
        repositoryContainer = RepositoryContainer.create(getActivity());
        repository = repositoryContainer.getRepository(RepositoryNames.STORENAMES);


        List<StoresViewModel> StoreList;
        ArrayAdapter<String> adapter = null;
        if((StoreList = (repository.GetAllRecords())) != null)
        {
            List<String> storeStringList = new ArrayList<>();
            for (int counter = 0; counter < StoreList.size(); counter++)
            {
                storeStringList.add(StoreList.get(counter).StoreNames);
            }
            adapter = new ArrayAdapter<String>(v.getContext(), android.R.layout.simple_list_item_1, storeStringList);
        }


        autoCompleteTextView.setAdapter(adapter);

        autoCompleteTextView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                try {
                    writeToFile(String.valueOf(parent.getItemAtPosition(position)));

                    storeid=Integer.parseInt(repository.getIdFromStoreName(String.valueOf(parent.getItemAtPosition(position))));
                    se = (StoresEntity) repository.GetRecord(storeid);
                    new Post().execute();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });

        goDatabaseButton = (Button) v.findViewById(R.id.AddData);
        goDatabaseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), DatabaseScreen.class);
                startActivity(intent);
            }
        });

        goLocationScreenButton = (Button) v.findViewById(R.id.LocationScreen);
        goLocationScreenButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), LocationScreen.class);
                startActivity(intent);
            }
        });


        return v;
    }
    public class Post extends AsyncTask<Void, Void, Void> {

        protected void onPreExecute() { // Post tan önce yapılacak işlemler. Yükleniyor yazısını(ProgressDialog) gösterdik.
            pDialog = new ProgressDialog(getActivity());
            pDialog.setMessage("Yükleniyor...");
            pDialog.setIndeterminate(true);
            pDialog.setCancelable(false); // ProgressDialog u iptal edilemez hale getirdik.
            pDialog.show();
        }

        protected Void doInBackground(Void... unused) { // Arka Planda yapılacaklar. Yani Post işlemi

            List params = new ArrayList<>();

                params.add(new BasicNameValuePair("id", se.getStoreName()));
                params.add(new BasicNameValuePair("Count", String.valueOf(1)));


                httpPost(url, params, 20000);

                Log.d("ukuk", String.valueOf(params)+"/n");
                params.clear();


            return null;
        }

        protected void onPostExecute(Void unused) { //Posttan sonra
            pDialog.dismiss();  //ProgresDialog u kapatıyoruz.
        }
    }

    public Void httpPost(String url,List params, int time) {

        try {

            HttpParams httpParameters = new BasicHttpParams();
            int timeout1 = time;
            int timeout2 = time;
            HttpConnectionParams.setConnectionTimeout(httpParameters, timeout1);
            HttpConnectionParams.setSoTimeout(httpParameters, timeout2);
            DefaultHttpClient httpClient = new DefaultHttpClient(httpParameters);
            HttpPost httpPost = new HttpPost(url);
            httpPost.setEntity(new UrlEncodedFormEntity(params, "UTF-8"));
            HttpResponse httpResponse = httpClient.execute(httpPost);
            HttpEntity httpEntity = httpResponse.getEntity();
            veri = httpEntity.getContent();


        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (ClientProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null ;
    }


    public void writeToFile(String storeName) throws IOException {
        String fpath = "/sdcard/" + FILENAME + ".txt";
        File file = new File(fpath);
        if (!file.exists()) {
            file.createNewFile();
        }
        FileWriter fwriter = new FileWriter(file.getAbsoluteFile());
        BufferedWriter bw = new BufferedWriter(fwriter);
        bw.write(storeName.toString());

        bw.close();
        fwriter.close();
    }

}