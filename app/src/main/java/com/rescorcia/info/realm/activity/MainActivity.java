package com.rescorcia.info.realm.activity;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;

import com.google.android.material.bottomappbar.BottomAppBar;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.appcompat.widget.Toolbar;

import android.os.Environment;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import com.rescorcia.info.realm.app.ObtenerInfo;
import com.rescorcia.info.realm.app.Prefs;
import com.rescorcia.info.realm.R;
import com.rescorcia.info.realm.adapters.BooksAdapter;
import com.rescorcia.info.realm.adapters.RealmBooksAdapter;
import com.rescorcia.info.realm.model.Book;
import com.rescorcia.info.realm.realmm.RealmController;
import io.realm.Realm;
import io.realm.RealmConfiguration;
import io.realm.RealmResults;

public class MainActivity extends AppCompatActivity {

    private BooksAdapter adapter;
    private Realm realm;
    private LayoutInflater inflater;
    private FloatingActionButton fab;
    private RecyclerView recycler;
    private BottomAppBar appBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        setUpBottomAppBar();

        RealmConfiguration realmConfiguration = new RealmConfiguration.Builder(this)
                .name("sample.realm")
                .schemaVersion(1)
                .build();
        Realm.setDefaultConfiguration(realmConfiguration);



        fab = (FloatingActionButton) findViewById(R.id.fab);
        recycler = (RecyclerView) findViewById(R.id.recycler);

        //get realm instance
        realm = RealmController.with(this).getRealm();


        setupRecycler();

        if (!Prefs.with(this).getPreLoad()) {
            setRealmData();
        }

        // refresh the realm instance
        RealmController.with(this).refresh();
        // get all persisted objects
        // create the helper adapter and notify data set changes
        // changes will be reflected automatically
        setRealmAdapter(RealmController.with(this).getBooks());

        Toast.makeText(this, "Presiona para editar, manten presionado para eliminar", Toast.LENGTH_LONG).show();

        //add new item
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                inflater = MainActivity.this.getLayoutInflater();
                View content = inflater.inflate(R.layout.edit_item, null);
                final EditText editTitle = (EditText) content.findViewById(R.id.title);
                final EditText editAuthor = (EditText) content.findViewById(R.id.author);
                final EditText editThumbnail = (EditText) content.findViewById(R.id.thumbnail);

                AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                builder.setView(content)
                        .setTitle("Agregar Libro")
                        .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                                Book book = new Book();
                                //book.setId(RealmController.getInstance().getBooks().size() + 1);
                                book.setId(RealmController.getInstance().getBooks().size() + System.currentTimeMillis());
                                book.setTitle(editTitle.getText().toString());
                                book.setAuthor(editAuthor.getText().toString());
                                book.setImageUrl(editThumbnail.getText().toString());

                                if (editTitle.getText() == null || editTitle.getText().toString().equals("") || editTitle.getText().toString().equals(" ")) {
                                    Toast.makeText(MainActivity.this, "Ingresar datos", Toast.LENGTH_SHORT).show();
                                } else {
                                    // Persist your data easily
                                    realm.beginTransaction();
                                    realm.copyToRealm(book);
                                    realm.commitTransaction();

                                    adapter.notifyDataSetChanged();

                                    // scroll the recycler view to bottom
                                    recycler.scrollToPosition(RealmController.getInstance().getBooks().size() - 1);
                                }
                            }
                        })
                        .setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {

                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        });
                AlertDialog dialog = builder.create();
                dialog.show();
            }
        });
    }

    public void setRealmAdapter(RealmResults<Book> books) {

        RealmBooksAdapter realmAdapter = new RealmBooksAdapter(this.getApplicationContext(), books, true);
        // Set the data and tell the RecyclerView to draw
        adapter.setRealmAdapter(realmAdapter);
        adapter.notifyDataSetChanged();
    }

    @SuppressLint("WrongConstant")
    private void setupRecycler() {
        // use this setting to improve performance if you know that changes
        // in content do not change the layout size of the RecyclerView
        recycler.setHasFixedSize(true);

        // use a linear layout manager since the cards are vertically scrollable
        final LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recycler.setLayoutManager(layoutManager);

        // create an empty adapter and add it to the recycler view
        adapter = new BooksAdapter(this);
        recycler.setAdapter(adapter);
    }

    private void setRealmData() {

        ArrayList<Book> books = new ArrayList<>();

        Book book = new Book();
        book.setId(1 + System.currentTimeMillis());
        book.setAuthor("Reto Meier");
        book.setTitle("Android Application Development");
        book.setImageUrl("http://192.168.1.17:8012/imagenes/code.png");
        books.add(book);

        book = new Book();
        book.setId(2 + System.currentTimeMillis());
        book.setAuthor("Itzik");
        book.setTitle("T-SQL Fundamentals");
        book.setImageUrl("http://192.168.1.17:8012/imagenes/librotres.png");
        books.add(book);

        book = new Book();
        book.setId(3 + System.currentTimeMillis());
        book.setAuthor("Hetland");
        book.setTitle("Python: From Novice To Professional");
        book.setImageUrl("http://192.168.1.17:8012/imagenes/librodos.png");
        books.add(book);

        book = new Book();
        book.setId(4 + System.currentTimeMillis());
        book.setAuthor("Fowler");
        book.setTitle("The Passionate Programmer");
        book.setImageUrl("http://192.168.1.17:8012/imagenes/librouno.png");
        books.add(book);

        book = new Book();
        book.setId(5 + System.currentTimeMillis());
        book.setAuthor("Kanetkar");
        book.setTitle("Questions In C Programming");
        book.setImageUrl("http://192.168.1.17:8012/imagenes/libroC.png");
        books.add(book);


        for (Book b : books) {
            // Persist your data easily
            realm.beginTransaction();
            realm.copyToRealm(b);
            realm.commitTransaction();
        }

        Prefs.with(this).setPreLoad(true);

    }




    /**
     * set up Bottom Bar
     */
    private void setUpBottomAppBar() {
        //find id
        appBar = findViewById(R.id.bar);

        //set bottom bar to Action bar as it is similar like Toolbar
        setSupportActionBar(appBar);

        //click event over Bottom bar menu item
        appBar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.acerca:
                        PackageInfo packageInfo;

                        try {
                            packageInfo = getPackageManager().getPackageInfo(getPackageName(), 0);
                            AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                            builder.setPositiveButton("Aceptar", null)
                                    .setIcon(R.drawable.iconbook)
                                    .setTitle("Información")
                                    .setMessage(
                                            "Versión: " + packageInfo.versionName ).show();

                        } catch (PackageManager.NameNotFoundException e) {
                            e.printStackTrace();
                        }
                        break;
                }
                return false;
            }
        });

        //click event over navigation menu like back arrow or hamburger icon
        appBar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //open bottom sheet

            }
        });
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.acerca_menu, menu);
        return true;
    }

}