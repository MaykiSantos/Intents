package br.com.mayki.intents;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContract;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.view.Menu;
import android.view.MenuItem;

import java.util.regex.Pattern;

import br.com.mayki.intents.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding activityMainBinding;
    private ActivityResultLauncher<String> requisicaoPermissoesActivityResultLauncher;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activityMainBinding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(activityMainBinding.getRoot());

        //define os valores dos itens
        activityMainBinding.mainTb.appTb.setTitle("Tratando Intents");
        activityMainBinding.mainTb.appTb.setSubtitle("Principais tipos");

        //Usando toobar com ActionBar da Activity
        setSupportActionBar(activityMainBinding.mainTb.appTb);

        requisicaoPermissoesActivityResultLauncher = registerForActivityResult(new ActivityResultContracts.RequestPermission(), concedida -> {
            if(concedida){
                fazerLigacao();
            }else{
                requisitaPermissaoLigacao();
            }
        });


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.viewMi:
                String url = activityMainBinding.parameterEt.getText().toString();
                String urlValidada = (url.matches("http"))? url : "https://"+url;

                Intent siteIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(urlValidada));
                startActivity(siteIntent);
                return true;
            case R.id.callMi:
                if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
                    if(checkSelfPermission(Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED){
                        //requisita permição
                        requisitaPermissaoLigacao();
                    }else{
                        //execura a discagem
                        fazerLigacao();
                    }
                }else{
                    //execura a discagem
                    fazerLigacao();
                }

                return true;
            case R.id.dialMi:
                Intent discagemIntent = new Intent(Intent.ACTION_DIAL);
                discagemIntent.setData(Uri.parse("tel: " + activityMainBinding.parameterEt.getText().toString()));
                startActivity(discagemIntent);
                return true;
            case R.id.pickMi:
                return true;
            case R.id.chooserMi:
                return true;
            case R.id.exitMi:
                finish();
                return true;
            case R.id.actionMi:
                //abrido outra activity usando uma intent Action
                Intent actionIntent = new Intent("OPEN_ACTION_ACTYVITY").putExtra(
                        Intent.EXTRA_TEXT,
                        activityMainBinding.parameterEt.getText().toString()
                );

                startActivity(actionIntent);
                return true;
            default:
                return false;
        }
    }


    private void fazerLigacao(){
        Intent ligacaoIntent = new Intent(Intent.ACTION_CALL);
        ligacaoIntent.setData(Uri.parse("tel: " + activityMainBinding.parameterEt.getText().toString()));
        startActivity(ligacaoIntent);
    }

    private void requisitaPermissaoLigacao(){
        requisicaoPermissoesActivityResultLauncher.launch(Manifest.permission.CALL_PHONE);
    }

}