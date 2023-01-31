package com.example.cs310_hw2;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;


public class NewsRepository {

    public void getNewsCategories(ExecutorService srv, Handler uiHandler){

        srv.execute(()->{

            try {
                URL url = new URL("http://10.3.0.14:8080/newsapp/getallnewscategories");


                HttpURLConnection conn = (HttpURLConnection) url.openConnection();

                BufferedReader reader = new BufferedReader(
                        new InputStreamReader(conn.getInputStream())
                );

                StringBuilder buffer = new StringBuilder();
                String line ="";

                while((line = reader.readLine())!=null){
                    buffer.append(line);

                }


                JSONObject data = new JSONObject(buffer.toString());
                JSONArray CategoryArr = data.getJSONArray("items");



                List<String> categoryTitles = new ArrayList<String>(3);
                // First we need to add dummy elements and then we update them
                for (int i=0; i < CategoryArr.length(); i++) categoryTitles.add("");

                for (int i=0; i<CategoryArr.length();i++){
                    JSONObject obj = CategoryArr.getJSONObject(i);
                    // ADDING ACCORDING TO THEIR ID'S (ORDERING)
                    categoryTitles.set(obj.getInt("id")-1, obj.getString("name"));
                }



                Message msg = new Message();
                msg.obj = categoryTitles;
                uiHandler.sendMessage(msg);

                conn.disconnect();


            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
            }

        });
    }

    public void getNewsDataByCategory(ExecutorService srv, Handler uiHandler, int categoryId){

        srv.execute(()->{

            try {
                URL url = new URL("http://10.3.0.14:8080/newsapp/getbycategoryid/" + categoryId);

                HttpURLConnection conn = (HttpURLConnection) url.openConnection();


                BufferedReader reader = new BufferedReader(
                        new InputStreamReader(conn.getInputStream())
                );

                StringBuilder buffer = new StringBuilder();
                String line ="";

                while((line = reader.readLine())!=null){
                    buffer.append(line);

                }


                JSONObject data = new JSONObject(buffer.toString());
                JSONArray NewsArr = data.getJSONArray("items");


                List<JSONObject> NewsData = new ArrayList<>();
                for (int i=0; i<NewsArr.length();i++){
                    JSONObject obj = NewsArr.getJSONObject(i);

                    // We get rid of the last characters of the date info
                    obj.put("date", obj.getString("date").substring(0, 10));

                    NewsData.add(obj);
                }


                Message msg = new Message();
                msg.obj = NewsData;
                uiHandler.sendMessage(msg);

                conn.disconnect();


            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }


        });
    }

    public void downloadImage(ExecutorService srv, Handler uiHandler, String path){

        srv.execute(()->{

            try {
                URL url = new URL(path);
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();

                Bitmap bitmapImage = BitmapFactory.decodeStream(conn.getInputStream());

                Message msg = new Message();
                msg.obj = bitmapImage;
                uiHandler.sendMessage(msg);

            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }

        });

    }



    public void getCommentsWithID(ExecutorService srv, Handler uiHandler, String id){

        srv.execute(()->{

            try {
                URL url = new URL("http://10.3.0.14:8080/newsapp/getcommentsbynewsid/" + id);

                HttpURLConnection conn = (HttpURLConnection) url.openConnection();


                BufferedReader reader = new BufferedReader(
                        new InputStreamReader(conn.getInputStream())
                );

                StringBuilder buffer = new StringBuilder();
                String line ="";

                while((line = reader.readLine())!=null){
                    buffer.append(line);

                }


                JSONObject data = new JSONObject(buffer.toString());
                JSONArray CommentsArr = data.getJSONArray("items");

                List<JSONObject> CommentsData = new ArrayList<>();
                for (int i=0; i<CommentsArr.length();i++){
                    JSONObject obj = CommentsArr.getJSONObject(i);

                    CommentsData.add(obj);
                }



                Message msg = new Message();
                msg.obj = CommentsData;
                uiHandler.sendMessage(msg);


            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
            }


        });

    }


    public void PostComment(ExecutorService srv, Handler uiHandler, String name, String text, String id){

        srv.execute(()->{

            try {
                URL url = new URL("http://10.3.0.14:8080/newsapp/savecomment");
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();

                conn.setDoInput(true);
                conn.setDoOutput(true);

                conn.setRequestMethod("POST");
                conn.setRequestProperty("Content-Type","application/JSON");

                JSONObject JSONtoSend = new JSONObject();
                JSONtoSend.put("name", name);
                JSONtoSend.put("text", text);
                JSONtoSend.put("news_id", id);


                // Now we send the data
                BufferedOutputStream writer = new BufferedOutputStream(conn.getOutputStream());
                writer.write(JSONtoSend.toString().getBytes(StandardCharsets.UTF_8));
                writer.flush();



                //Getting the response from the server
                BufferedReader reader = new BufferedReader(
                        new InputStreamReader(conn.getInputStream())
                );

                StringBuilder buffer = new StringBuilder();
                String line ="";

                while((line = reader.readLine())!=null){
                    buffer.append(line);

                }
                JSONObject response = new JSONObject(buffer.toString());

                conn.disconnect();

                Message msg = new Message();
                msg.obj = response.getString("serviceMessageCode");
                uiHandler.sendMessage(msg);


            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
            }

        });

    }
}
