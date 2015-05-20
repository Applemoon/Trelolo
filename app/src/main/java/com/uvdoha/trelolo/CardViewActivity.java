package com.uvdoha.trelolo;

import android.app.Activity;
import android.os.Bundle;
import android.widget.TextView;

/**
 * Created by a.halaidzhy on 20.05.15.
 */
public class CardViewActivity extends Activity {
    TextView cardName;
    TextView cardId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.card_view);

        cardName = (TextView) findViewById(R.id.card_title);
        cardId = (TextView) findViewById(R.id.card_id);

        if (getIntent() != null) {
            cardId.setText(getIntent().getStringExtra("card_id"));
            cardName.setText(getIntent().getStringExtra("card_title"));
        }
    }
}
