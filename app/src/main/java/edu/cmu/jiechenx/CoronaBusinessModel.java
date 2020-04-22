package edu.cmu.jiechenx;

import android.os.AsyncTask;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class CoronaBusinessModel {
    MainActivity ma = null;

    public void ini(String countryCode,MainActivity ma){
        this.ma = ma;
        new AsyncGetInfo().execute(countryCode);
    }

    // Async Class to help do network operation in a helper thread
    private class AsyncGetInfo extends AsyncTask<String, Void, String> {
        protected String doInBackground(String... countryCode) {
            String country = countryCode[0];
            country = country.substring(country.length() - 3, country.length() - 1);
            return sendHTTPRequest(country);
        }

        protected void onPostExecute(String str) {
            ma.callBackReceive(str);
        }


        private String sendHTTPRequest(String countryCode) {
            String u = "https://glacial-reef-17006.herokuapp.com" + "//" + countryCode;
            System.out.println(u);
            URL url = null;
            String response = "";
            try {
                url = new URL(u);
                HttpURLConnection con = (HttpURLConnection) url.openConnection();
                con.setRequestMethod("GET");
                con.setRequestProperty("Accept", "text/plain");

                // wait for response
                int status = con.getResponseCode();

                // If things went poorly, don't try to read any response, just return.
                if (status != 200) {
                    // not using msg
                    String msg = con.getResponseMessage();
                    return msg;
                }

                String output = "";
                // things went well so let's read the response
                BufferedReader br = new BufferedReader(new InputStreamReader(
                        (con.getInputStream())));

                while ((output = br.readLine()) != null) {
                    response += output;

                }
                System.out.println(response);
                con.disconnect();
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }

            return processJson(response,countryCode);
        }


        private String processJson(String js, String countryCode) {
            System.out.println("received following:");
            System.out.println(js);
            String re = "Here is the most updated CoronaVirus data in "+ countryCode + "\n";
            String response = js.split("\\}")[0];
            response = response.split("\\{")[2];
            String[] data = response.split(",");
            for (String s : data){
                re += s+"\n";
            }
            return re;
        }
    }
}
