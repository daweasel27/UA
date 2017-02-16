package enemy.phobia.client;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.webkit.WebView;
import android.widget.Toast;
import com.dropbox.client2.DropboxAPI;
import com.dropbox.client2.exception.DropboxException;

import enemy.phobia.client.JSON.Send.SendValues;

public class UploadFileToDropbox extends AsyncTask<Void, Void, Boolean> {

    private DropboxAPI<?> dropbox;
    private String path;
    private Context context;
    private File mediaFile;
    private int id;

    public UploadFileToDropbox(Context context, DropboxAPI<?> dropbox,String path, File mediaFile,int sessionId) {
        this.context = context.getApplicationContext();
        this.dropbox = dropbox;
        this.path = path;
        this.mediaFile = mediaFile;
        this.id =sessionId;
    }

    @Override
    protected Boolean doInBackground(Void... params) {
        try {
            Log.e("UPLOADING","VIDEO");
            FileInputStream fileInputStream = new FileInputStream(mediaFile);
            dropbox.putFile(path + mediaFile.getName(), fileInputStream, mediaFile.length(), null, null);
            Log.e("AAAAAAAAA",mediaFile.getName());
            DropboxAPI.DropboxLink link = DropboxActivity.dropbox.share(path + mediaFile.getName());
            URL url = new URL(link.url);
            HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setInstanceFollowRedirects(false);
            URL secondURL = new URL(urlConnection.getHeaderField("location"));
            Log.e("ls",secondURL.toString().replace("dl=0","raw=1"));
            SendValues.sendURL(id, secondURL.toString().replace("dl=0", "raw=1"), context);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    @Override
    protected void onPostExecute(Boolean result) {
        if (result){
            Toast.makeText(context, "File Uploaded Sucesfully!", Toast.LENGTH_LONG).show();
        } else {
            Toast.makeText(context, "Failed to upload file", Toast.LENGTH_LONG).show();
        }
    }
}
