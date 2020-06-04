package www.mapscloud.cn.bluetoothservice;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothServerSocket;
import android.bluetooth.BluetoothSocket;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.Toast;

import java.io.InputStream;
import java.util.UUID;

public class MainActivity extends AppCompatActivity {
    private String TAG = this.getClass().getName();
    private BluetoothAdapter mBluetoothAdapter;
    private AcceptThread acceptThread;
//    private final UUID MY_UUID = UUID.fromString("00002902-0000-1000-8000-00805f9b34fb");//和客户端相同的UUID
//    private final UUID MY_UUID = UUID.fromString("00002a1c-0000-1000-8000-00805f9b34fb");//和客户端相同的UUID
//    private final UUID MY_UUID = UUID.fromString("00001c00-d102-11e1-9b23-00025b00a5a5");//和客户端相同的UUID
//    private final UUID MY_UUID = UUID.fromString("00001c01-d103-11e1-9b23-00025b00a5a5");//和客户端相同的UUID
//    private final UUID MY_UUID = UUID.fromString("0000180f-0000-1000-8000-00805f9b34fb");//和客户端相同的UUID
//    private final UUID MY_UUID = UUID.fromString("00002a19-0000-1000-8000-00805f9b34fb");//和客户端相同的UUID
    private final String NAME = "Bluetooth_Socket";
    private BluetoothServerSocket serverSocket;
    private BluetoothSocket socket;
    private InputStream is;//输入流

    public static String BATTERY_LEVEL = "00002a19-0000-1000-8000-00805f9b34fb";
    public static String BATTERY_SERVICE = "0000180f-0000-1000-8000-00805f9b34fb";
    public static String CLIENT_CHARACTERISTIC_CONFIG = "00002902-0000-1000-8000-00805f9b34fb";
    public static String HEART_RATE_MEASUREMENT = "00002a1c-0000-1000-8000-00805f9b34fb";
    public static String MYCJ_BLE = "00001c00-d102-11e1-9b23-00025b00a5a5";
    public static String MYCJ_BLE_READ_WRITE = "00001c01-d103-11e1-9b23-00025b00a5a5";

    private final UUID MY_UUID = UUID.fromString(MYCJ_BLE_READ_WRITE);//和客户端相同的UUID



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        acceptThread = new AcceptThread();
        acceptThread.start();
    }


    private Handler handler = new Handler() {
        public void handleMessage(Message msg) {
            Toast.makeText(getApplicationContext(), String.valueOf(msg.obj),
                    Toast.LENGTH_LONG).show();
            super.handleMessage(msg);
        }
    };

    //服务端监听客户端的线程类
    private class AcceptThread extends Thread {
        public AcceptThread() {
            try {
                serverSocket = mBluetoothAdapter.listenUsingRfcommWithServiceRecord(NAME, MY_UUID);
            } catch (Exception e) {
            }
        }
        public void run() {
            try {
                socket = serverSocket.accept();
                is = socket.getInputStream();
                while(true) {
                    byte[] buffer =new byte[1024];
                    int count = is.read(buffer);
                    Message msg = new Message();
                    msg.obj = new String(buffer, 0, count, "utf-8");
                    Log.e(TAG , new String(buffer, 0, count, "utf-8"));
                    handler.sendMessage(msg);
                }
            }
            catch (Exception e) {
            }
        }
    }
}
