package com.example.meghana.helloworld;

import android.annotation.SuppressLint;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.concurrent.ExecutionException;

public class MainActivity extends AppCompatActivity {
    TextView tv;
    StringBuilder builder;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        builder = new StringBuilder();

        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                try {
                    new Description().execute().get();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } catch (ExecutionException e) {
                    e.printStackTrace();
                }
                tv = (TextView)findViewById(R.id.text1);
                Log.d("MAin Activity","&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&");
                tv.setText(builder);
            }
        });

    }


    private class Description extends AsyncTask<Void, Void, Void> {
        String desc;

        @Override
        protected void onPreExecute() {

            //tv.setText("Loading Results...");

        }

        @Override
        protected Void doInBackground(Void... params) {
            Document doc = null, main_page;
            String google_url = "https://www.google.co.in/search?q=things+to+do+in+london";
            try {
                doc = Jsoup.connect(google_url).get();
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            String url = doc.select("a[href^=https://www.tripadvisor.in/Attractions] ").attr("href");
            System.out.println(url);
            getListings(url);

            try {
                main_page = Jsoup.connect(url).get();
                Elements pages = main_page.select("div.pageNumbers>a");


                for(int i =0; i < pages.size()-1;++i)
                {
                    Element page = pages.get(i);
                    //System.out.println(page.text());
                    //System.out.println("Yes");
                    //System.out.println(page.attr("href"));
                    url = "https://www.tripadvisor.in" + page.attr("href");
                    //System.out.println(url);
                    getListings(url);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }


            return null;
        }


        void getListings(String url)
        {

            try {
                Document  main_page = null;
                main_page = Jsoup.connect(url).get();
                Log.d("Connected",url);
                if (main_page == null)
                    Log.d("mainpage","null");
                Elements links = main_page.select("div.attraction_clarity_cell");

                for( Element link  : links)
                {
                    //System.out.println(link);

                    Element listing = link.select("div.listing_title > a").first();
                    String title = listing.text();
                    String listing_link = "https://www.tripadvisor.in" + listing.attr("href");

                /*Element e1 = link.select("div.listing").first();
                Element e2 = e1.select("div.listing_details").first();
                Element e3 = e2.select("div.listing_info").first();
                Element e4 = e3.select("div.listing_rating").first();
               	Element e5 = e4.select("div.wrap >div.rs rating >span").first();
               	//Element e6 = e5.select("div.rs rating >span").first();
               	System.out.println(e5.attr("alt"));
               	//if(e6 == null)
               		//System.out.println("yes");*/



                    String img_src = link.select("img[src]").first().attr("src");

                    Element d1 = link.select("div.listing").first();
                    Element d2 = d1.select("div.listing_details").first();
                    Element d3 = d2.select("div.listing_info").first();
                    Element d4 = d3.select("div.tag_line").first();
                    String description = d4.text();

                    String r = title +"\n"+description+"\n"+listing_link+"\n"+img_src+"\n\n";

                    Log.d("data",r);






                    builder.append("\n----------------------------").append(title +"\n"+description+"\n"+listing_link+"\n"+img_src+"\n").append("\n-----------------------------------\n");

                }
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }


    }


    }


}
