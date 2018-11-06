import android.app.Activity
import android.os.AsyncTask
import android.os.Parcel
import android.os.Parcelable
import org.apache.http.NameValuePair
import java.net.HttpURLConnection
import java.net.URL
import java.io.*
import java.net.URLEncoder
import java.util.ArrayList


class Network() : AsyncTask<String, String, String>(), Parcelable {

    lateinit var requestType:String
    lateinit var activity: Activity
    lateinit var params:HashMap<String, String>
    lateinit var postData:StringBuilder
    lateinit var postDataBytes:ByteArray
    lateinit var onAsyncRequestComplete:OnAsyncRequestComplete

    constructor(parcel: Parcel) : this() {
        requestType = parcel.readString()
    }

    interface OnAsyncRequestComplete {
        fun asyncResponse(response:String)
    }

    constructor(activity:Activity, requestType:String, params: HashMap<String, String>) : this() {

        onAsyncRequestComplete = activity as OnAsyncRequestComplete;
        this.requestType = requestType
        this.activity = activity
        this.params = params
    }


    override fun onPreExecute() {
        // Before doInBackground

        postData = StringBuilder()
        for (param in params.entries) {
            if (postData.length != 0) postData.append('&')
            postData.append(URLEncoder.encode(param.key, "UTF-8"))
            postData.append('=')
            postData.append(URLEncoder.encode(param.value.toString(), "UTF-8"))
        }

        postDataBytes = postData.toString().toByteArray()
    }

    override fun doInBackground(vararg urls: String?): String {
        var urlConnection: HttpURLConnection? = null

        try {
            val url = URL(urls[0])

            urlConnection = url.openConnection() as HttpURLConnection
            urlConnection.setDoInput(true)
            if(requestType.equals("POST"))
                urlConnection.setDoOutput(true)
            urlConnection.requestMethod = requestType
            if(requestType.equals("POST"))
                urlConnection.getOutputStream().write(postDataBytes);

            var inString = streamToString(urlConnection.inputStream)

            publishProgress(inString)
        } catch (ex: Exception) {

        } finally {
            if (urlConnection != null) {
                urlConnection.disconnect()
            }
        }

        return " "
    }

    override fun onProgressUpdate(vararg values: String?) {
        onAsyncRequestComplete.asyncResponse(values[0].toString());
    }

    override fun onPostExecute(result: String?) {
        // Done
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(requestType)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<Network> {
        override fun createFromParcel(parcel: Parcel): Network {
            return Network(parcel)
        }

        override fun newArray(size: Int): Array<Network?> {
            return arrayOfNulls(size)
        }
    }
}

fun streamToString(inputStream: InputStream): String {

    val bufferReader = BufferedReader(InputStreamReader(inputStream))
    var line: String
    var result = ""

    try {
        do {
            line = bufferReader.readLine()
            if (line != null) {
                result += line
            }
        } while (line != null)
        inputStream.close()
    } catch (ex: Exception) {

    }

    return result
}
