package k.field.rice.forest.btetheringon;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothProfile;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class ScrollingActivity extends AppCompatActivity {

    private final int PAN = 5;

    private BluetoothProfile mProxy = null;

    private TextView textView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scrolling);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setBluetoothTethering(!isTetheringOn());
            }
        });

        textView = (TextView)findViewById(R.id.text);

        if(isTetheringOn()) {
            textView.setText("Bluetooth Tethering is ON");
        } else {
            textView.setText("Bluetooth Tethering is OFF");
        }

        BluetoothAdapter adapter = BluetoothAdapter.getDefaultAdapter();
        if (adapter != null) {
            adapter.getProfileProxy(this.getApplicationContext(), mProfileServiceListener,
                    PAN);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_scrolling, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }


    private BluetoothProfile.ServiceListener mProfileServiceListener =
            new BluetoothProfile.ServiceListener() {
                public void onServiceConnected(int profile, BluetoothProfile proxy) {
                        mProxy = proxy;
                }
                public void onServiceDisconnected(int profile) {
                    mProxy = null;
                }
            };

    private void  setBluetoothTethering(boolean value ) {
        try {
            Class<?> clazz = Class.forName("android.bluetooth.BluetoothPan");
            Method method = clazz.getMethod("setBluetoothTethering", new Class[]{boolean.class});
            method.invoke(mProxy, new Object[]{new Boolean(value)});

            if(value) {
                textView.setText("Bluetooth Tethering is ON");
            } else {
                textView.setText("Bluetooth Tethering is OFF");
            }
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
    }

    private boolean isTetheringOn() {
        boolean ret = false;
        try {
            Class<?> clazz = Class.forName("android.bluetooth.BluetoothPan");
            Method method = clazz.getMethod("isTetheringOn");
        ret = (boolean)method.invoke(mProxy);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }

        return ret;
    }
}
