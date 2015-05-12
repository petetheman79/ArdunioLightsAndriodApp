package com.nextsteps.arduniolights;

import android.app.Activity;
import android.app.ActionBar;
import android.app.Fragment;
import android.os.Bundle;
import android.os.AsyncTask;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.os.Build;

import java.io.ByteArrayInputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.nio.charset.Charset;



public class MainActivity extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        if (savedInstanceState == null) {
            getFragmentManager().beginTransaction()
                    .add(R.id.container, new PlaceholderFragment())
                    .commit();
        }       
        
    }  
    
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    /**
     * A placeholder fragment containing a simple view.
     */
    public static class PlaceholderFragment extends Fragment {

        public PlaceholderFragment() {
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_main, container, false);
            
            Button button = (Button)rootView.findViewById(R.id.btnGreen);
            button.setOnClickListener(buttonCommandListener);
            return rootView;
        }
        
        OnClickListener buttonCommandListener = new OnClickListener() {			
    		@Override
    		public void onClick(View v) {
    			Button b = (Button)v;
    			String command = "";
    			if(b.getText() == "ON"){				
    				command = "b";
    			} else {
    				command = "f";
    			}				
    			
    			Log.v("MainActivity", "Button Clicked");
    			LightClassTask lightTask = new LightClassTask(command);
    			lightTask.execute();	
    			
    			if(b.getText() == "ON"){
    				b.setText("OFF");
    			} else {
    				b.setText("ON");
    			}
    				
    		}
    	};
    	
    	public class LightClassTask extends AsyncTask<Void, Void, Void>{
        	String destAddress = "192.168.0.123";
        	int destPort = 23;
        	String response = "";
        	String command = "";
        	byte[] commandByteArray;
        	
        	LightClassTask(String command){
        		this.command = command;
        		commandByteArray = command.getBytes(Charset.forName("UTF-8"));
        		Log.v("MainActivity", "Command: " + command);
        	}
        	
        	@Override
        	protected Void doInBackground(Void... arg0) {
        		Socket socket = null;    		 
        		
        		try	{
        			Log.v("MainActivity", "Opening Socket");
        			socket = new Socket(destAddress, destPort);        			
        			OutputStream stream = socket.getOutputStream();
            		stream.write(commandByteArray);
            		stream.flush();
            		socket.close();
            		
        		} catch (Exception e) {
        			e.printStackTrace();    			
        		}   
        		return null;    		
        	}
        	
        	@Override
        	protected void onPostExecute(Void result) {
        		super.onPostExecute(result);    		
        	}    	
        }
    }
}



