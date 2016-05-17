package com.example.Fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Iterator;
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
import android.util.Log;
import android.widget.Button;
import android.widget.Toast;
import com.example.Database.Entities.StoresEntity;

import com.example.Database.Repositories.IRepository;
import com.example.Database.Repositories.RepositoryContainer;
import com.example.Database.Repositories.RepositoryNames;
import com.example.models.StoresViewModel;
import com.example.omur.navigationavm.R;


/**
 * Created by omur on 12.03.2016.
 */
public class Fragment3 extends Fragment {

    private RepositoryContainer repositoryContainer;
    private IRepository repository;
    private Vector<StoresViewModel> StoreList;
    Button buton;
    int strid ;
    Integer strcount ;
    ProgressDialog pDialog;
    String url2 = "http://omurbyv-001-site1.atempurl.com/MostSearched";
    String veri_string;
    InputStream veri;
    List params ;
    Post post = new Post();  //Post Class dan post adında nesne olusturduk.Post classın içindeki methodu kullanabilmek için

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.fragment3, container, false);

        repositoryContainer = RepositoryContainer.create(getActivity());
        repository = repositoryContainer.getRepository(RepositoryNames.STORENAMES);


        buton = (Button) v.findViewById(R.id.send);
        buton.setOnClickListener(new View.OnClickListener() { //buton a click listener ekledik

            public void onClick(View v) {
                new Post().execute(); //Asynctask Classı Çağırıyoruz
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

                httpGet(url2);

            return null;
        }

        protected void onPostExecute(Void unused) { //Posttan sonra
            pDialog.dismiss();  //ProgresDialog u kapatıyoruz.
        }
    }


   public  String httpGet(String url) {
        try {

                DefaultHttpClient httpClient = new DefaultHttpClient();
                HttpGet httpGet = new HttpGet(url);

                HttpResponse httpResponse = httpClient.execute(httpGet);
                HttpEntity httpEntity = httpResponse.getEntity();
                veri =  httpEntity.getContent();

        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (ClientProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            BufferedReader reader = new BufferedReader(new InputStreamReader(
                    veri, "iso-8859-1"), 8);
            StringBuilder sb = new StringBuilder();
            String line = null;
            while ((line = reader.readLine()) != null) {
                sb.append(line + "\n");
            }
            veri.close();
            veri_string = sb.toString();
            Pattern p = Pattern.compile("<td>([^<]*)</td>", Pattern.MULTILINE | Pattern.DOTALL);

             params = new ArrayList<>();
            for (Matcher m = p.matcher(veri_string); m.find(); ) {

                params.add(m.group(1));

            }


            //access via Iterator
            Iterator iterator = params.iterator();
            while(iterator.hasNext()){
                String name = (String) iterator.next();
                String cnt = (String) iterator.next();

                String strname=name.trim() ;
                String strcnt=cnt.replaceAll("\\s","");


               Log.d("say",strname) ;
                Log.d("says",strcnt) ;


                strcount = Integer.parseInt(strcnt);
                String id=repository.getIdFromStoreName(String.valueOf(strname)) ;

                if(!id.equals(null)) {
                    strid = Integer.parseInt(id);
                    repository.Update(new StoresEntity(strid, null, null, strcount));
                }
                else
                    repository.Update(new StoresEntity(0, null, null, 0));

            }


        } catch (Exception e) {
            Log.e("Buffer Error", "Hata " + e.toString());
        }
        return veri_string;


    }

}