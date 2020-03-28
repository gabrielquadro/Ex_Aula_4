package com.unisc.ex_aula_4;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.provider.MediaStore;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

public class MainActivity extends Activity {

    ImageView imagemViewFoto;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //verifica se há permissão de camera, caso nã otenha ele pede
        if(ActivityCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(this, new String[] {Manifest.permission.CAMERA},0);
        }

        imagemViewFoto = (ImageView) findViewById(R.id.foto);
        findViewById(R.id.botaoFoto).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cliquePicIt();
            }
        });

    }

    public void cliquePicIt() {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(intent, 1);
    }


    public void cliqueMaps3(View view) {
        String partida = "-25.443195, -49.280977 ";
        String destino = "-25.442207, -43.278403 ";
        String url = "http://maps.google.com/maps?f=d&saddr=" + partida + "&daddr=" + destino + "&hl=pt";
        startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(url)));
    }

    public void cliqueMaps2(View view) {
        String localizacao = "geo:-25.443195,-49.280977";
        startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(localizacao)));
    }

    public void cliqueMaps1(View view) {
        Uri uriGeo = Uri.parse("geo:0,0?q=Augusto+Spengler,Santa+Cruz+do+Sul");
        Intent it = new Intent(Intent.ACTION_VIEW, uriGeo);
        startActivity(it);
    }

    public void cliqueCall(View view) {
        // Uri uri = Uri.parse(("tel: 991208980"));
        // Intent intent = new Intent(Intent.ACTION_DIAL, uri);
        // startActivity(intent);

        Uri uri = Uri.parse(("tel: 991208980"));
        Intent intent = new Intent(Intent.ACTION_CALL, uri);

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        startActivity(intent);
    }

    public void cliqueWeb(View view) {
        Uri uri = Uri.parse(("http://www.unisc.br"));
        Intent intent = new Intent(Intent.ACTION_VIEW,uri);
        startActivity(intent);
    }

    public void cliqueContato(View view) {
        Uri uri = Uri.parse("content://com.android.contacts/contacts/1");
        Intent intent = new Intent(Intent.ACTION_VIEW,uri);
        startActivity(intent);
    }

    private final int contato = 0;

    public void cliqueContatos(View view) {
        Uri uri = Uri.parse("content://com.android.contacts/contacts");
        Intent intent = new Intent(Intent.ACTION_PICK,uri);
        startActivityForResult(intent, contato);
    }

    protected  void onActivityResult(int requestCode, int resultCode, Intent data){
        if(requestCode == contato && resultCode == RESULT_OK) {
            Uri uri = data.getData();

            Cursor c = getContentResolver().query(uri, null, null, null, null);
            c.moveToNext();
            int nameCol = c.getColumnIndexOrThrow(ContactsContract.Contacts.DISPLAY_NAME);
            int idCol = c.getColumnIndexOrThrow(ContactsContract.Contacts._ID);
            String nome = c.getString(nameCol);
            String id = c.getString(idCol);

            c.close();

            Cursor phones = getContentResolver().query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null, ContactsContract.CommonDataKinds.Phone.CONTACT_ID + " = " + id, null, null);
            phones.moveToNext();
            String phoneNumber = phones.getString(phones.getColumnIndexOrThrow(ContactsContract.CommonDataKinds.Phone.NUMBER));

            TextView nomeContato = findViewById(R.id.txtnome);
            nomeContato.setText("Nome: " + nome);
            TextView telContato = findViewById(R.id.txxTelefone);
            telContato.setText("Telefone: " + phoneNumber);
        }

        if(requestCode == 1 && resultCode == RESULT_OK){
            Bundle extras = data.getExtras();
            Bitmap imagem = (Bitmap) extras.get("data");
            imagemViewFoto.setImageBitmap(imagem);
        }
        super.onActivityResult(requestCode,resultCode,data);
    }
}
