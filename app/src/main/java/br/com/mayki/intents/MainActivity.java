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
import android.os.Environment;
import android.os.PersistableBundle;
import android.view.Menu;
import android.view.MenuItem;

import java.util.regex.Pattern;

import br.com.mayki.intents.databinding.ActivityMainBinding;


public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding activityMainBinding;
    private ActivityResultLauncher<String> requisicaoPermissoesActivityResultLauncher;
    private ActivityResultLauncher<Intent> selecionarImagemActivityResultLauncher;
    private ActivityResultLauncher<Intent> escolherAplicativoActivityResultyLauncher;

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

        selecionarImagemActivityResultLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), resultado->{
            if(resultado.getResultCode() == RESULT_OK){
                Uri referenciaImagemUri = resultado.getData().getData();
                activityMainBinding.imagemLocal.setImageURI(referenciaImagemUri); //carrega imagem dentro da aplicação
                startActivity(new Intent(Intent.ACTION_VIEW, referenciaImagemUri));
            }
        });

        escolherAplicativoActivityResultyLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), resultado->{
            if(resultado.getResultCode() == RESULT_OK){
                Uri referenciaImagemUri = resultado.getData().getData();
                startActivity(new Intent(Intent.ACTION_VIEW, referenciaImagemUri));
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
                selecionarImagemActivityResultLauncher.launch(preparaPegarImagemIntent());
                return true;
            case R.id.chooserMi:
                Intent escolherAplicativoIntent = new Intent(Intent.ACTION_CHOOSER);
                escolherAplicativoIntent.putExtra(Intent.EXTRA_TITLE, "Escolha um aplicativo para imagens");
                escolherAplicativoIntent.putExtra(Intent.EXTRA_INTENT, preparaPegarImagemIntent());
                escolherAplicativoActivityResultyLauncher.launch(escolherAplicativoIntent);

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

    private Intent preparaPegarImagemIntent() {
        Intent pegarImagemIntent = new Intent(Intent.ACTION_PICK);
        return pegarImagemIntent.setDataAndType(Uri.parse(this.getExternalFilesDir(Environment.DIRECTORY_PICTURES).getPath()), "image/*");
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