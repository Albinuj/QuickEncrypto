package com.cev.albin.quickencrypto.activity;

/**
 * Created by Albin on 28-Dec-17.
 */

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.cev.albin.quickencrypto.R;
import com.cev.albin.quickencrypto.helper.SQLiteHandler;
import com.cev.albin.quickencrypto.helper.SessionManager;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;


@RequiresApi(api = Build.VERSION_CODES.KITKAT)
public class MainActivity extends AppCompatActivity {
    private List<Items> itemsList = new ArrayList<>();
    private RecyclerView recyclerView;
    private Toolbar toolbar;
    private ItemsAdapter mAdapter;

    String password = "albinjohn";
    byte[] passwordBytes = password.getBytes(StandardCharsets.UTF_8);

    private static final String TAG = MainActivity.class.getSimpleName();
    private static final int ENCRYPT_RESULT_CODE = 1;
    private static final int DECRYPT_RESULT_CODE = 2;
    private SQLiteHandler db;
    private SessionManager session;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // SqLite database handler
        db = new SQLiteHandler(getApplicationContext());

        // session manager
        session = new SessionManager(getApplicationContext());

        if (!session.isLoggedIn()) {
            logoutUser();
        }
        // Creating folder
        File folder = new File(Environment.getExternalStorageDirectory() +
                File.separator + "QuickEncrypto" + File.separator + "Files");
        //String folderPath = folder.getPath();
       // boolean success = true;
        if (!folder.exists()) {
            //success = folder.mkdirs();
            folder.mkdirs();
        } else {
            //Toast.makeText(this, "Folder already created" + folderPath, Toast.LENGTH_SHORT).show();
            // Do something else on failure
        }
        /*if (success) {
            Toast.makeText(this, "Folder created successfully", Toast.LENGTH_SHORT)
                    .show();
            // Do something on success
        } else {

            // Do something else on failure
        }*/

        // Fetching user details from sqlite
        HashMap<String, String> user = db.getUserDetails();

        String name = user.get("name");
        String email = user.get("email");

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        recyclerView = (RecyclerView) findViewById(R.id.recycler_view);

        mAdapter = new ItemsAdapter(itemsList);

        recyclerView.setHasFixedSize(true);

        // vertical RecyclerView
        // keep file_list_row.xmlwidth to `match_parent`
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());

        // horizontal RecyclerView
        // keep file_list_rowxml width to `wrap_content`
        // RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.HORIZONTAL, false);

        recyclerView.setLayoutManager(mLayoutManager);

        // adding inbuilt divider line
        recyclerView.addItemDecoration(new DividerItemDecoration(this, LinearLayoutManager.VERTICAL));

        // adding custom divider line with padding 16dp
        // recyclerView.addItemDecoration(new MyDividerItemDecoration(this, LinearLayoutManager.HORIZONTAL, 16));
        recyclerView.setItemAnimator(new DefaultItemAnimator());

        recyclerView.setAdapter(mAdapter);

        registerForContextMenu(recyclerView);



        // row click listener
        recyclerView.addOnItemTouchListener(new RecyclerTouchListener(getApplicationContext(), recyclerView, new RecyclerTouchListener.ClickListener() {
            @Override
            public void onClick(View view, int position) {
                Items items = itemsList.get(position);
                Toast.makeText(getApplicationContext(), items.getTitle() + " is selected!", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onLongClick(View view, int position) {

            }
        }));

        prepareItemsData();
    }


    /** This will be invoked when a menu item is selected */
    @Override
    public boolean onContextItemSelected(MenuItem item) {

       // AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
        //int clickedItemPosition = item.getOrder();
        int position = -1;
        try {
            position = ((ItemsAdapter)recyclerView.getAdapter()).getPosition();
        } catch (Exception e) {
            Log.d(TAG, e.getLocalizedMessage(), e);
            return super.onContextItemSelected(item);
        }
        switch(item.getItemId()){
            //case R.id.cnt_mnu_edit:
             //   Toast.makeText(this, "Edit : "  , Toast.LENGTH_SHORT).show();
             //   break;
            case R.id.cnt_mnu_delete:
                Items items = itemsList.get(position);
                File file1 = new File(items.getPath());
                boolean deleted = file1.delete();
                if (deleted) {
                    prepareItemsData();
                    Toast.makeText(this, "File Deleted! ", Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.cnt_mnu_share:
//                File file = new File(new File(Environment.getExternalStorageDirectory(), "myfolder"), "file.ext");
//                Items item2 = itemsList.get(position);
//                File file2 = new File(item2.getPath());
//                Uri uri = FileProvider.getUriForFile(context, fileProvider, file2);
//                Intent sendIntent = new Intent();
//                sendIntent.setAction(Intent.ACTION_SEND);
//                sendIntent.putExtra(Intent.EXTRA_STREAM, Uri.fromFile(exportPath));
//                sendIntent.setType("text/plain");
//                startActivity(Intent.createChooser(sendIntent, "Share via"));
//
//                Intent sharingIntent = new Intent(android.content.Intent.ACTION_SEND);
//                sharingIntent.setType("text/plain");
//                String shareBody = "Here is the share content body";
//                sharingIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, "Subject Here");
//                sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, shareBody);
//                startActivity(Intent.createChooser(sharingIntent, "Share via"));
//
                Items item2 = itemsList.get(position);
                File file2 = new File(item2.getPath());
                //File file = new File(Environment.getExternalStorageDirectory().toString() + "/" + "abc.txt");
                Intent sharingIntent = new Intent(Intent.ACTION_SEND);
                sharingIntent.setType("*/*");
                sharingIntent.putExtra(Intent.EXTRA_STREAM, Uri.parse("file://" + file2.getAbsolutePath()));
                startActivity(Intent.createChooser(sharingIntent, "Share file via"));

                //Toast.makeText(this, "Shared " + item2.getPath()  , Toast.LENGTH_SHORT).show();
                break;

        }
        return true;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            // action with ID action_encrypt was selected
            case R.id.action_encrypt:
                Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                //intent.setType("file/*");
                intent.setType("*/*");
                intent.addCategory(Intent.CATEGORY_OPENABLE);
                try {
                    startActivityForResult(Intent.createChooser(intent, "Select a file to Encrypt using"),ENCRYPT_RESULT_CODE);
                } catch (android.content.ActivityNotFoundException ex) {
                    // Potentially direct the user to the Market with a Dialog
                    Toast.makeText(this, "Please install a File Manager.", Toast.LENGTH_LONG).show();
                }
                break;
            case R.id.action_decrypt:
                Intent newintent = new Intent(Intent.ACTION_GET_CONTENT);
                //newintent.setType("file/*");
                newintent.setType("*/*");
                newintent.addCategory(Intent.CATEGORY_OPENABLE);
                try {
                    //startActivityForResult(intent,PICKFILE_RESULT_CODE);
                    startActivityForResult(Intent.createChooser(newintent, "Select a file to Decrypt using"),DECRYPT_RESULT_CODE);
                } catch (android.content.ActivityNotFoundException ex) {
                    // Potentially direct the user to the Market with a Dialog
                    Toast.makeText(this, "Please install a File Manager.", Toast.LENGTH_LONG).show();
                }
                //Toast.makeText(this, "decrypt selected", Toast.LENGTH_SHORT).show();
                break;
            case R.id.action_about: {
                Intent n = new Intent(getApplicationContext(),
                        AboutActivity.class);
                startActivity(n);
            }
                break;
            case R.id.action_logout:
                logoutUser();
                break;
            case R.id.action_settings: {
                Intent i = new Intent(getApplicationContext(),
                        SettingsActivity.class);
                startActivity(i);
            }
                break;
            case R.id.action_shareapp:
                Intent sendIntent = new Intent();
                sendIntent.setAction(Intent.ACTION_SEND);
                sendIntent.putExtra(Intent.EXTRA_TEXT, "Hey, I just installed QuickEncrypto on my Android. " +
                        "With QuickEncrypto you can quickly encrypt your files of any type and send them or store them in cloud securely." +
                        " Get it now from https://www.quickencrypto.com/download/");
                sendIntent.setType("text/plain");
                startActivity(Intent.createChooser(sendIntent, "Share via"));
                break;


            default:
                break;
        }

        return true;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // TODO Auto-generated method stub
        switch(requestCode){
            case ENCRYPT_RESULT_CODE:
                if(resultCode==RESULT_OK){
                    String FilePath = data.getData().getPath();
                    File file = new File(FilePath);
                    String filename = file.getName();
                    String extension = filename.substring(filename.lastIndexOf("."));
                    String file_size = getFileSize(file);
                    if(!(".qe".equals(extension)) ) {
                        String destination = Environment.getExternalStorageDirectory() +
                                File.separator + "QuickEncrypto" + File.separator + "Files" + File.separator + filename + ".qe";
                        AsyncTaskRunner runner = new AsyncTaskRunner();
                        runner.execute(FilePath, destination, passwordBytes);
                        prepareItemsData();
                        Toast.makeText(this, "File Encrypted successfully", Toast.LENGTH_SHORT)
                                .show();
                        // mAdapter.notifyDataSetChanged();
                    }
                    else{
                        Toast.makeText(this, "This file is already Encrypted!!", Toast.LENGTH_SHORT)
                                .show();
                    }
                }
                break;

            case DECRYPT_RESULT_CODE:
                if(resultCode==RESULT_OK) {
                    String FilePath = data.getData().getPath();
                    File file = new File(FilePath);
                    String filename = file.getName();
                    String extension = filename.substring(filename.lastIndexOf("."));
                    String file_size = getFileSize(file);
                    if (".qe".equals(extension)) {
                        String result = filename.substring(0, filename.lastIndexOf("."));
                        String destination = Environment.getExternalStorageDirectory() +
                                File.separator + "QuickEncrypto" + File.separator + "Files" + File.separator + result;
                        AsyncTaskRunner runner = new AsyncTaskRunner();
                        runner.execute(FilePath, destination, passwordBytes);
                        prepareItemsData();
                        Toast.makeText(this, "File Decrypted successfully", Toast.LENGTH_SHORT)
                                .show();

                        // mAdapter.notifyDataSetChanged();
                        //recyclerView.invalidate();
                    } else {

                        Toast.makeText(this, "This file is not Encrypted using QuickEncrypto!!", Toast.LENGTH_SHORT)
                                .show();
                    }
                }
                break;

        }
    }

    private String getFileSize(java.io.File file) {
        final DecimalFormat format = new DecimalFormat("#.##");
        final long MiB = 1024 * 1024;
        final long KiB = 1024;
        final double length = file.length();

        if (length > MiB) {
            return format.format(length / MiB) + " MB";
        }
        if (length > KiB) {
            return format.format(length / KiB) + " KB";
        }
        return format.format(length) + " B";
    }

    /**
     * Prepares data to provide data set to adapter
     */
    private void prepareItemsData() {

        clear();
        String path = Environment.getExternalStorageDirectory()
                + File.separator + "QuickEncrypto"+ File.separator + "Files";

        File directory = new File(path);
        File[] files = directory.listFiles();
        ArrayList<String> Titles = new ArrayList<String>();
        Titles = FetchTitle();
        ArrayList<String> Paths = new ArrayList<String>();
        Paths = FetchPath();
        ArrayList<String> Sizes = new ArrayList<String>();
        Sizes = FetchSize();
        ArrayList<String> Dates = new ArrayList<String>();
        Dates = FetchDate();
        for (int i = 0; i < files.length; i++)
        {
            Items items = new Items( Titles.get(i), Dates.get(i), Sizes.get(i), Paths.get(i));
            itemsList.add(items);

        }


        // notify adapter about data set changes
        // so that it will render the list with new data
        mAdapter.notifyDataSetChanged();

    }

    public void clear() {
        int size = this.itemsList.size();
        if (size > 0) {
            for (int i = 0; i < size; i++) {
                this.itemsList.remove(0);
            }

            mAdapter.notifyItemRangeRemoved(0, size);
        }
    }

    @Override
    protected void onPostResume() {
        prepareItemsData();
        super.onPostResume();
    }

    private ArrayList<String> FetchTitle() {

        ArrayList<String> filenames = new ArrayList<String>();
        String path = Environment.getExternalStorageDirectory()
                + File.separator + "QuickEncrypto"+ File.separator + "Files";

        File directory = new File(path);
        File[] files = directory.listFiles();

        for (int i = 0; i < files.length; i++)
        {

            String file_name = files[i].getName();
            // you can store name to arraylist and use it later
            filenames.add(file_name);
        }
        return filenames;
    }

    private ArrayList<String> FetchPath() {

        ArrayList<String> pathnames = new ArrayList<String>();
        String path = Environment.getExternalStorageDirectory()
                + File.separator + "QuickEncrypto"+ File.separator + "Files";

        File directory = new File(path);
        File[] files = directory.listFiles();

        for (int i = 0; i < files.length; i++)
        {

            String path_name = files[i].getAbsolutePath();
            // you can store name to arraylist and use it later
            pathnames.add(path_name);
        }
        return pathnames;
    }

    private ArrayList<String> FetchSize() {

        ArrayList<String> sizenames = new ArrayList<String>();
        String path = Environment.getExternalStorageDirectory()
                + File.separator + "QuickEncrypto"+ File.separator + "Files";

        File directory = new File(path);
        File[] files = directory.listFiles();

        for (int i = 0; i < files.length; i++)
        {

            String file_size = getFileSize(files[i]);
            // you can store name to arraylist and use it later
            sizenames.add(file_size);
        }
        return sizenames;
    }

    private ArrayList<String> FetchDate() {

        ArrayList<String> datenames = new ArrayList<String>();
        String path = Environment.getExternalStorageDirectory()
                + File.separator + "QuickEncrypto"+ File.separator + "Files";

        File directory = new File(path);
        File[] files = directory.listFiles();

        for (int i = 0; i < files.length; i++)
        {
            Date lastModDate = new Date(files[i].lastModified());
            String file_date = lastModDate.toString();
            // you can store name to arraylist and use it later
            datenames.add(file_date);
        }
        return datenames;
    }
    /**
     * Logging out the user. Will set isLoggedIn flag to false in shared
     * preferences Clears the user data from sqlite users table
     * */
    private void logoutUser() {
        session.setLogin(false);

        db.deleteUsers();

        // Launching the login activity
        Intent intent = new Intent(MainActivity.this, LoginActivity.class);
        startActivity(intent);
        finish();
    }

    private class AsyncTaskRunner extends AsyncTask<Object, String, String> {

        private String resp;
        ProgressDialog progressDialog;

        @Override
        protected String doInBackground(Object... params) {
            publishProgress("Process in progress..."); // Calls onProgressUpdate()
            try {
                String FilePath = (String) params[0];
                String destination = (String) params[1];
                byte[] passwordBytes = (byte[]) params[2];
                xorFile(FilePath,destination,passwordBytes);
            } catch (IOException e) {
                e.printStackTrace();
            }
            return resp;
        }


        @Override
        protected void onPostExecute(String result) {
            // execution of result of Long time consuming operation
            //progressDialog.dismiss();

        }


        @Override
        protected void onPreExecute() {
            // progressDialog = ProgressDialog.show(MainActivity.this, "ProgressDialog", "Wait for "+time.getText().toString()+ " seconds");
        }


        @Override
        protected void onProgressUpdate(String... text) {


        }

        void xorFile(String filename, String dest, byte[] passwordBytes) throws IOException
        {
            FileInputStream is = new FileInputStream(filename);
            FileOutputStream os = new FileOutputStream(dest);

            byte[] data = new byte[1024*4]; //4 KB buffer
            int read = is.read(data), index = 0;
            while( read != -1 ) {
                for( int k=0; k<read; k++ ) {
                    data[k] ^= passwordBytes[index % passwordBytes.length];
                    index++;
                }
                os.write(data,0,read);
                read = is.read(data);
            }

            os.flush();
            os.close();
            is.close();
        }
    }
}
