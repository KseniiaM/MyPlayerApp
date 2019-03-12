package com.elzette.myplayerapp.models;

public class TabPageItemModel {

    private int mPosition;

    private String mTitle;

    private int mViewHolderResourceId;

    public TabPageItemModel(int position, String title, int viewHolderResourceId) {
        mPosition = position;
        mTitle = title;
        mViewHolderResourceId = viewHolderResourceId;
    }

    public int getPosition() {
        return mPosition;
    }

    public String getTitle() {
        return mTitle;
    }

    public int getmViewHolderResourceId() { return mViewHolderResourceId; }
}
