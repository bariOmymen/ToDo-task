package com.example.todotasks.adpter;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.todotasks.R;
import com.example.todotasks.model.Task;
import com.example.todotasks.onClickListener;
import com.example.todotasks.ui.AddTasksActivity;
import com.example.todotasks.ui.MainActivity;

import java.util.List;

public class ListAdapter extends RecyclerView.Adapter<ListAdapter.ListViewHolder> implements onClickListener {
    private LayoutInflater layoutInflater;
    private Context context;

    private List<Task> mTask;
    private Task mainTask;

    onClickListener onClickListener2;
    public static String number = "number";
    public static String aTitle = "aTitle";
    public static String aDetails = "aDetails";
    public static String aTaskAddress = "aTask Address";
    public static String aTaskId = "aTask_Id";

    public ListAdapter(Context context, onClickListener onClick){layoutInflater=LayoutInflater.from(context);
        this.context=context;
        this.onClickListener2=onClick;

    }
  View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {


            Log.i("click",String.valueOf(mainTask.getId()));

           /*  Log.i("click",aTask.getTitle());
            Bundle bundle = new Bundle();
            bundle.putString(aTitle,aTask.getTitle());
            bundle.putString(aDetails,aTask.getDetails());
            bundle.putDouble(aLat,aTask.getLat());
            bundle.putDouble(aTitle,aTask.getLon());
            intent.putExtra(aTitle,aTask.getTitle());
            intent.putExtra(aDetails,aTask.getDetails());
            intent.putExtra(aTaskAddress,aTask.getTaskAddress());
            intent.putExtra(aTaskId,aTask.getId());*/

        }
    };


    @NonNull
    @Override
    public ListViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = layoutInflater.inflate(R.layout.item,parent,false);
        //view.setOnClickListener(onClickListener);
        return new ListViewHolder(view);
    }

    public void setList(List<Task> list){
        Log.i("WORK",list.toString());
        this.mTask=list;
        notifyDataSetChanged();
    }

    @Override
    public void onBindViewHolder(@NonNull ListViewHolder holder, int position) {
        Task curr = mTask.get(position);
        holder.textView.setText(curr.getTitle());

    }

    @Override
    public int getItemCount() {
        if(mTask!=null){
        return mTask.size();
    }
        return 0;
    }

    @Override
    public void onClick(Task task, View view) {
        this.mainTask=task;
    }

    public class ListViewHolder extends RecyclerView.ViewHolder{
        TextView textView;

        public ListViewHolder(@NonNull View itemView) {
            super(itemView);
            textView=itemView.findViewById(R.id.todoText);
            textView.setOnClickListener(view -> onClickListener2.onClick(getTaskAtPosition(getAdapterPosition()),view));
        }
    }
   public Task getTaskAtPosition(int position){
        return mTask.get(position);
   }
}


