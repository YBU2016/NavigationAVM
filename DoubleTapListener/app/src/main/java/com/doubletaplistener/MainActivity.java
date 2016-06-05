package com.doubletaplistener;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    TextView textView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        textView = (TextView) findViewById(R.id.textView);
        RelativeLayout rv = (RelativeLayout) findViewById(R.id.relativeLayout);

        if (rv != null) {
            rv.setOnClickListener(new DoubleClickListener() {
                @Override
                public void onSingleClick(View v) {
                    textView.setText("Dev");
                }

                @Override
                public void onDoubleClick(View v) {
                    textView.setText("Alp");
                }
            });
        }

    }
}
