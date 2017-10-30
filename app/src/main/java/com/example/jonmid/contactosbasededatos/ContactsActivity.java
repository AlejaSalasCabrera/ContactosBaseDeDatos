package com.example.jonmid.contactosbasededatos;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.jonmid.contactosbasededatos.Adapters.ContactAdapter;
import com.example.jonmid.contactosbasededatos.Helpers.SqliteHelper;
import com.example.jonmid.contactosbasededatos.Models.Contact;
import com.example.jonmid.contactosbasededatos.Utilities.Constants;
import com.example.jonmid.contactosbasededatos.Views.RegisterContactActivity;
import com.example.jonmid.contactosbasededatos.Views.SearchContactActivity;

import java.util.ArrayList;
import java.util.List;

public class ContactsActivity extends AppCompatActivity {

    RecyclerView recyclerViewContacts;
    ContactAdapter contactAdapter;
    List<Contact> contactList = new ArrayList<>();
    SqliteHelper sqliteHelper;
    EditText editTextIndex;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contacts);

        recyclerViewContacts = (RecyclerView) findViewById(R.id.id_rv_contacts);
        sqliteHelper = new SqliteHelper(this, "db_contacts", null, 1);
        editTextIndex= (EditText)findViewById(R.id.edt_1);


        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerViewContacts.setLayoutManager(linearLayoutManager);

        //listContacts();
        processData();
    }

    public void onClickShowWindowRegister(View view){
        Intent intent = new Intent(this, RegisterContactActivity.class);
        startActivity(intent);
    }

    public void onClickShowWindowSearch(View view){
        Intent intent = new Intent(this, SearchContactActivity.class);
        startActivity(intent);
    }

    public void listContacts (){
        SQLiteDatabase db =  sqliteHelper.getReadableDatabase();

        Cursor cursor = db.rawQuery("select name,phone,email from users order by id desc",null);
        while (cursor.moveToNext()){
            Contact contact = new Contact();
            contact.setName(cursor.getString(0));
            contact.setPhone(cursor.getString(1));
            contact.setEmail(cursor.getString(2));


            contactList.add(contact);
        }
        cursor.close();
        processData();
    }


    public void listContactsIndex (View view){
        contactList.clear();

        SQLiteDatabase db = sqliteHelper.getReadableDatabase();

        try {

            Cursor cursor = db.rawQuery("select name,phone,email from users where phone ='"+editTextIndex.getText().toString()+ "'",null);
            while (cursor.moveToNext()){
                Contact contact = new Contact();
                contact.setName(cursor.getString(0));
                contact.setPhone(cursor.getString(1));
                contact.setEmail(cursor.getString(2));
                contactList.add(contact);

            }
            cursor.close();
            processData();
        }catch (Exception e){
            Toast.makeText(this, "Nombre del contacto no encontrado", Toast.LENGTH_SHORT).show();
        }
    }


    public void processData(){
        if(contactList.size() != 0){
            contactAdapter = new ContactAdapter(contactList,getApplicationContext());
            recyclerViewContacts.setAdapter(contactAdapter);
        }else {
            Toast.makeText(this, "No tienes contactos", Toast.LENGTH_SHORT).show();
        }

    }


}
