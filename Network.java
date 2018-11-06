public class Network extends AsyncTask<String, Integer, String> {

    OnAsyncRequestComplete caller;
    Context context;
    String method = "GET";
    List<NameValuePair> parameters = null;
    boolean handelResponse = true;

    // Three Constructors
    public Network(Activity a, String m, List<NameValuePair> p) {
        caller = (OnAsyncRequestComplete) a;
        context = a;
        method = m;
        parameters = p;
    }

    public Network(Activity a, String m, List<NameValuePair> p, boolean handled_reponse) {
        caller = (OnAsyncRequestComplete) a;
        context = a;
        method = m;
        parameters = p;
        handelResponse=  handled_reponse;
    }


    public Network(Activity a, String m) {
        caller = (OnAsyncRequestComplete) a;
        context = a;
        method = m;
    }

    public Network(Activity a) {
        caller = (OnAsyncRequestComplete) a;
        context = a;
    }

    // Interface to be implemented by calling activity
    public interface OnAsyncRequestComplete {
        public void asyncResponse(String response);
    }

    public String doInBackground(String... urls) {
        // get url pointing to entry point of API
        String address = urls[0].toString();
        if (method == "POST") {
            return post(address);
        }

        if (method == "GET") {
            return get(address);
        }

        return null;
    }

    public void onPreExecute() {

    }

    public void onProgressUpdate(Integer... progress) {
    }

    public void onPostExecute(String response) {
        if(handelResponse) {
            caller.asyncResponse(response);
        }else{
            Log.d("NETWORK_NHR_POST", response);
        }
    }

    protected void onCancelled(String response) {
        if(handelResponse) {
            caller.asyncResponse(response);
        }else{
            Log.d("NETWORK_NHR_CANCL", response);
        }
    }

    @SuppressWarnings("deprecation")
    private String get(String address) {
        try {

            if (parameters != null) {

                String query = "";
                String EQ = "="; String AMP = "&";
                for (NameValuePair param : parameters) {
                    query += param.getName() + EQ + URLEncoder.encode(param.getValue()) + AMP;
                }

                if (query != "") {
                    address += "?" + query;
                }
            }

            HttpClient client = new DefaultHttpClient();
            HttpGet get= new HttpGet(address);
            get.setHeader(new BasicHeader("Authorization", Utils.getInstance().getAccessToken()));


            HttpResponse response = client.execute(get);
            return stringifyResponse(response);

        } catch (ClientProtocolException e) {
            // TODO Auto-generated catch block
        } catch (IOException e) {
            // TODO Auto-generated catch block
        }

        return null;
    }

    private String post(String address) {
        try {

            HttpClient client = new DefaultHttpClient();
            HttpPost post = new HttpPost(address);
            post.setHeader(new BasicHeader("Authorization", Utils.getInstance().getAccessToken()));

            if (parameters != null) {
                post.setEntity(new UrlEncodedFormEntity(parameters));
            }

            HttpResponse response = client.execute(post);
            return stringifyResponse(response);

        } catch (ClientProtocolException e) {
            // TODO Auto-generated catch block
        } catch (IOException e) {
            // TODO Auto-generated catch block
        }

        return null;
    }

    private String stringifyResponse(HttpResponse response) {
        BufferedReader in;
        try {
            in = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));

            StringBuffer sb = new StringBuffer("");
            String line = "";
            while ((line = in.readLine()) != null) {
                sb.append(line);
            }
            in.close();

            return sb.toString();
        } catch (IllegalStateException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        return null;
    }
}
