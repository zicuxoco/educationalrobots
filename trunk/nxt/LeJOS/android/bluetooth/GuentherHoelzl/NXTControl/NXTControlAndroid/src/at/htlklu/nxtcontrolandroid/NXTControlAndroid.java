package at.htlklu.nxtcontrolandroid;

/*
  NXTControlAndroid application for remote controlling the NXT brick
  2010 by Guenther Hoelzl
  see http://sites.google.com/site/ghoelzl/

  This file is free software; you can redistribute it and/or
  modify it under the terms of the GNU Lesser General Public
  License as published by the Free Software Foundation; either
  version 3 of the License, or (at your option) any later version.

  This library is distributed in the hope that it will be useful,
  but WITHOUT ANY WARRANTY; without even the implied warranty of
  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
  General Public License for more details.
*/

import android.app.Activity;
import android.app.Dialog;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.content.Intent;
import android.content.res.Resources;
import android.net.Uri;
import android.util.Log;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.DataOutputStream;
import java.net.*;
import java.util.UUID;
import java.util.List;
import java.util.Enumeration;
import java.util.Set;

public class NXTControlAndroid extends Activity
{
    public static final String DEFAULT_NXT_NAME = "NXT"; 
    public static final int MOTOR_A_C_STOP = 0;
    public static final int MOTOR_A_FORWARD = 1;
    public static final int MOTOR_A_BACKWARD = 2;
    public static final int MOTOR_C_FORWARD = 3;
    public static final int MOTOR_C_BACKWARD = 4;
    public static final int TACHOCOUNT_RESET = 8;
    public static final int TACHOCOUNT_READ = 9;
    public static final int ACTION=10;
    public static final int DISCONNECT = 99;   

    public static final int MENU_ABOUT = Menu.FIRST;
    public static final int MENU_QUIT = Menu.FIRST + 1;

    private SensorManager sensorManager;
    private boolean runWithEmulator = false;
    private SeekBar pitchSeekBar;
    private SeekBar rollSeekBar;
    private Button connectButton;
    private Button actionButton;
    private TextView myNXT;
    private BluetoothSocket nxtBTsocket = null;
    private DataOutputStream nxtDos = null;
    private long timeDataSent = 0;

    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        myNXT = (TextView) findViewById(R.id.nxtName);
        myNXT.setText(DEFAULT_NXT_NAME);
        initSeekBars();
        initConnectButton();
    	initActionButton();
    }

    private void initConnectButton() {
        connectButton = (Button) findViewById(R.id.connect_button);
        connectButton.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                if (nxtBTsocket == null) 
                    createNXTConnection();
                else
                    destroyNXTConnection();
                if (nxtBTsocket == null) {
                    connectButton.setText(getResources().getString(R.string.connect));
                    actionButton.setEnabled(false);
			    }
                else {
                    connectButton.setText(getResources().getString(R.string.disconnect));
                    actionButton.setEnabled(true);
                }
            }
        }); 
    }    

    private void initActionButton() {
        actionButton = (Button) findViewById(R.id.action_button);
        actionButton.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                sendNXTcommand(MOTOR_A_C_STOP,0);
                sendNXTcommand(ACTION,0);		
            }
        }); 
    }    


    private void initSeekBars() {
        pitchSeekBar = (SeekBar) findViewById(R.id.seekbar1);
        rollSeekBar = (SeekBar) findViewById(R.id.seekbar2);
        pitchSeekBar.setOnSeekBarChangeListener(new OnSeekBarChangeListener() {

            public void onProgressChanged(SeekBar s, int progress, boolean fromUser) {
                if (runWithEmulator) 
                    updateOrientation((float) 0.0, (float)((progress-50.0)*30.0/50.0), (float)((rollSeekBar.getProgress()-50.0)*30.0/50.0), false);
            }

            public void onStartTrackingTouch(SeekBar s) {
            }

            public void onStopTrackingTouch(SeekBar s) {
            }

        });
        rollSeekBar.setOnSeekBarChangeListener(new OnSeekBarChangeListener() {

            public void onProgressChanged(SeekBar s, int progress, boolean fromUser) {
                if (runWithEmulator) 
                    updateOrientation((float) 0.0, (float)((pitchSeekBar.getProgress()-50.0)*30.0/50.0), (float)((progress-50.0)*30.0/50.0), false);
            }

            public void onStartTrackingTouch(SeekBar s) {
            }

            public void onStopTrackingTouch(SeekBar s) {
            }

        });
    }

    private final SensorEventListener orientationSensorEventListener = new SensorEventListener() {

        public void onSensorChanged(SensorEvent event) {
            updateOrientation(event.values[0],
                              event.values[1],
                              event.values[2],
                              true);
        }

        public void onAccuracyChanged(Sensor sensor, int accuracy) {
        }
    };

    private void updateOrientation(float heading, float pitch, float roll, boolean fromSensor) {

        long currentTime;

        // show position at the seekbars
        if (fromSensor) {
            pitchSeekBar.setProgress((int) (pitch*50.0/30.0 + 50.5));        
            rollSeekBar.setProgress((int) (roll*50.0/30.0 + 50.5));    
        }

        // send values to NXT every 100 msecs
        if (nxtDos != null) {
            currentTime = System.currentTimeMillis();
            if ((currentTime - timeDataSent) > 100) {
                timeDataSent = currentTime;
                // send commands to NXT
                if (Math.abs(pitch) < 10) {
                    sendNXTcommand(MOTOR_A_C_STOP, 0);
                    return;
                }

                // calculate motor values
                int left = (int) Math.round(20.0 * pitch * (1.0 + roll / 90.0));
                int right = (int) Math.round(20.0 * pitch * (1.0 - roll / 90.0));

                int reverseLeft = left < 0 ? 1 : 0;
                left = Math.abs(left);
                if (left > 700) {
                    left = 700;
                }
                int reverseRight = right < 0 ? 1 : 0;
                right = Math.abs(right);
                if (right > 700) {
                    right = 700;
                }
                sendNXTcommand(MOTOR_A_FORWARD + reverseLeft, left);
                sendNXTcommand(MOTOR_C_FORWARD + reverseRight, right);
            }
        }
    }

    @Override
    public void onResume() {
        List<Sensor> sensorList;

        super.onResume();
        connectButton.setText(getResources().getString(R.string.connect));
        actionButton.setEnabled(false);
                
	    // register orientation sensor
	    sensorList = sensorManager.getSensorList(Sensor.TYPE_ORIENTATION);
        runWithEmulator = (sensorList.size() == 0);
        if (!runWithEmulator)
            sensorManager.registerListener(orientationSensorEventListener, sensorList.get(0), SensorManager.SENSOR_DELAY_GAME);
    }

    @Override
    public void onPause() {
        destroyNXTConnection();
        sensorManager.unregisterListener(orientationSensorEventListener);
        super.onStop();
    }

    /* Creates the menu items */
    public boolean onCreateOptionsMenu(Menu menu) {
        menu.add(0, MENU_ABOUT, 0, "About").setIcon(R.drawable.menu_info_icon);
        menu.add(0, MENU_QUIT, 0, "Quit").setIcon(R.drawable.menu_quit_icon);
        return true;
    }

    /* Handles item selections */
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case MENU_ABOUT:
                showAboutDialog();
                return true;         
            case MENU_QUIT:
               finish(); 
               return true;
        }
        return false;
    }

    private void showAboutDialog() 
    {
        final Dialog dialog = new Dialog(this);
        dialog.getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.aboutbox);
        dialog.show();
    }


    // create bluetooth-connection with SerialPortServiceClass_UUID
    // code fragments from http://lejos.sourceforge.net/forum/viewtopic.php?t=1991&highlight=android
    private void createNXTConnection() {
        try {
            BluetoothAdapter btAdapter = BluetoothAdapter.getDefaultAdapter();
            Set<BluetoothDevice> bondedDevices = btAdapter.getBondedDevices();
            BluetoothDevice nxtDevice = null;
         
            for (BluetoothDevice bluetoothDevice : bondedDevices)
            {
                if (bluetoothDevice.getName().equals(myNXT.getText().toString())) {
                    nxtDevice = bluetoothDevice;
                    break;
                }
            } 

            if (nxtDevice == null)
            {
                Toast toast = Toast.makeText(this, "No paired NXT device found", Toast.LENGTH_SHORT);
                toast.show();
                return;
            }             

            nxtBTsocket = nxtDevice.createRfcommSocketToServiceRecord(UUID.fromString("00001101-0000-1000-8000-00805F9B34FB"));
            nxtBTsocket.connect();
            nxtDos = new DataOutputStream(nxtBTsocket.getOutputStream());
        } catch (IOException e) {
            Toast toast = Toast.makeText(this, "Problem at creating a connection", Toast.LENGTH_SHORT);
            toast.show();
        }
    }

    private void destroyNXTConnection() {
        try {
            if (nxtBTsocket != null) {
                // send one close message 
                sendNXTcommand(MOTOR_A_C_STOP,0);
                sendNXTcommand(DISCONNECT,0);
                nxtBTsocket.close();
                nxtBTsocket = null;
            }
            nxtDos = null;            
        } catch (IOException e) {
            Toast toast = Toast.makeText(this, "Problem at closing the connection", Toast.LENGTH_SHORT);
            toast.show();
        }
    }

    public void sendNXTcommand(int command, int value) {
        if (nxtDos == null) {
            return;
        }

        try {
            nxtDos.writeInt(command);
            nxtDos.writeInt(value);
            nxtDos.flush();
        } catch (IOException ioe) { 
            Toast toast = Toast.makeText(this, "Problem at writing command", Toast.LENGTH_SHORT);
            toast.show();            
        }
    }

}
