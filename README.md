# Android-Common-Async-Class
paste Network.java or Network.Kotlin in your code according to your code and call it in following way


public class Activity extends AppCompatActivity implements Network.OnAsyncRequestComplete{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity);
         ArrayList<NameValuePair> params = new ArrayList<>();
        params.add(new BasicNameValuePair("username", "Test User"));
        params.add(new BasicNameValuePair("password", "12345"));
        
        Network network = new Network(this, "POST", params);
        network.execute(Utils.getInstance().getRequestedUrl(Utils.LOGIN_URL));//Url to server
     
    }
    @Override
    public void asyncResponse(String response) {
      Toast.makeText(this, response, Toast.LENGTH_LONG)
    }
}

