package com.example.qthjen.sqlitesaveimage;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

public class CustomList extends BaseAdapter {

    Context mContext;
    int mLayout;
    List<Items> mList;

    public CustomList(Context mContext, int mLayout, List<Items> mList) {
        this.mContext = mContext;
        this.mLayout = mLayout;
        this.mList = mList;
    }

    @Override
    public int getCount() {
        return mList.size();
    }

    @Override
    public Object getItem(int i) {
        return null;
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    private class ViewHolder {

        ImageView ivImageList;
        TextView tvItems;
        TextView tvDescription;

    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {

        ViewHolder viewHolder = new ViewHolder();
        if ( view == null) {

            LayoutInflater layoutInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = layoutInflater.inflate(mLayout, null);
            viewHolder.ivImageList = (ImageView) view.findViewById(R.id.ivImageList);
            viewHolder.tvItems = (TextView) view.findViewById(R.id.tvItems);
            viewHolder.tvDescription = (TextView) view.findViewById(R.id.tvDescription);
            view.setTag(viewHolder);

        } else {

            viewHolder = (ViewHolder) view.getTag();
        }

        viewHolder.tvItems.append(mList.get(i).getmName());
        viewHolder.tvDescription.append(mList.get(i).getmDescription());

        /** chuyển byte[] sang bitmap **/
        byte[] hinhAnh = mList.get(i).getmImage();
        Bitmap bitmap = BitmapFactory.decodeByteArray(hinhAnh, 0, hinhAnh.length);
        viewHolder.ivImageList.setImageBitmap(bitmap);

        return view;
    }
}
