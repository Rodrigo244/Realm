package com.rescorcia.info.realm.adapters;

import android.content.Context;

import com.rescorcia.info.realm.model.Book;
import io.realm.RealmResults;

public class RealmBooksAdapter extends RealmModelAdapter<Book> {

    public RealmBooksAdapter(Context context, RealmResults<Book> realmResults, boolean automaticUpdate) {

        super(context, realmResults, automaticUpdate);
    }
}