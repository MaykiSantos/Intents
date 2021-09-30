package br.com.mayki.intents;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;

import br.com.mayki.intents.databinding.ActivityActionsBinding;

public class ActionsActivity extends AppCompatActivity {

    private ActivityActionsBinding activityActionsBinding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activityActionsBinding = ActivityActionsBinding.inflate(getLayoutInflater());
        setContentView(activityActionsBinding.getRoot());

        activityActionsBinding.mainTb.appTb.setTitle(this.getClass().getSimpleName());
        activityActionsBinding.mainTb.appTb.setSubtitle(getIntent().getAction());
        setSupportActionBar(activityActionsBinding.mainTb.appTb);

        String valorRecebido = getIntent().getStringExtra(Intent.EXTRA_TEXT);

        activityActionsBinding.parameterTv.setText(valorRecebido);
    }
}