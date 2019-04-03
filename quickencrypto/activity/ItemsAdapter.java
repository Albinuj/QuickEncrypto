package com.cev.albin.quickencrypto.activity;

import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.RecyclerView.ViewHolder;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.cev.albin.quickencrypto.R;

import java.util.List;

public class ItemsAdapter extends RecyclerView.Adapter<ItemsAdapter.MyViewHolder> {
    private int position;
    private List<Items> itemsList;

    public void setPosition(int position) {
        this.position = position;
    }
    public int getPosition() {
        return position;
    }

    public class MyViewHolder extends ViewHolder implements View.OnCreateContextMenuListener {
        public TextView title, size, date;

        public MyViewHolder(View view) {
            super(view);
            title = (TextView) view.findViewById(R.id.title);
            date = (TextView) view.findViewById(R.id.date);
            size = (TextView) view.findViewById(R.id.size);

            view.setOnCreateContextMenuListener(this);
        }

        @Override
        public void onCreateContextMenu(ContextMenu contextMenu, View view, ContextMenu.ContextMenuInfo contextMenuInfo) {

            contextMenu.setHeaderTitle("Select the action");

            contextMenu.add(0, R.id.cnt_mnu_delete,
                    0,R.string.str_cnt_mnu_delete);
            contextMenu.add(Menu.NONE, R.id.cnt_mnu_share,
                    Menu.NONE, R.string.str_cnt_mnu_share);
            //contextMenu.add(Menu.NONE, R.id.cnt_mnu_edit,
             //       Menu.NONE, R.string.str_cnt_mnu_edit);
        }
    }


    public ItemsAdapter(List<Items> itemsList) {
        this.itemsList = itemsList;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.file_list_row, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {
        Items items = itemsList.get(position);
        holder.title.setText(items.getTitle());
        holder.date.setText(items.getDate());
        holder.size.setText(items.getSize());

       holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
           @Override
           public boolean onLongClick(View view) {
               setPosition(position);
               return false;
           }
       });


    }

    @Override
    public int getItemCount() {
        return itemsList.size();
    }

    @Override
    public void onViewRecycled(MyViewHolder holder) {

        holder.itemView.setOnLongClickListener(null);
        super.onViewRecycled(holder);
    }


}
