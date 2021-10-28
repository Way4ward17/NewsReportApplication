package com.theway4wardacademy.report.Adapter;
import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.theway4wardacademy.report.Pojo.Post;
import com.theway4wardacademy.report.R;
import java.util.List;


public class PostAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    public static  final int IMAGE = 1;
    public static  final int VIDEO = 2;
    public static  final int AUDIO = 3;

    Context mContext;
    List<Post> mPost;



    public PostAdapter(Context mContext, List<Post> mPost){
        this.mPost = mPost;
        this.mContext = mContext;

    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
       View view;
        if (viewType == IMAGE) {
            view = LayoutInflater.from(mContext).inflate(R.layout.item_post_image, parent, false);
            return new Image(view);
        }else if(viewType == VIDEO){
            view = LayoutInflater.from(mContext).inflate(R.layout.item_post_video, parent, false);
            return new Video(view);
        }else{
            view = LayoutInflater.from(mContext).inflate(R.layout.item_post_audio, parent, false);
            return new Audio(view);
        }
    }



    @Override
    public int getItemViewType(int position) {
        if (TextUtils.equals(mPost.get(position).getType(), "image"))
            return IMAGE;
        else  if (TextUtils.equals(mPost.get(position).getType(), "video"))
            return VIDEO;
        else
            return AUDIO;
    }

    @Override
    public void onBindViewHolder(@NonNull final RecyclerView.ViewHolder holder, final int position) {
        final Post post = mPost.get(position);

        if (getItemViewType(position) == IMAGE) {


        } else if (getItemViewType(position) == VIDEO) {


        } else if (getItemViewType(position) == AUDIO) {


        }

    }
   @Override
    public int getItemCount() {
        return mPost.size();
    }


    class Image extends RecyclerView.ViewHolder{

        Image (View itemview){
            super(itemview);


        }


    }



    class Video extends RecyclerView.ViewHolder{

        Video (View itemview){
            super(itemview);


        }


    }

    class Audio extends RecyclerView.ViewHolder{

        Audio (View itemview){
            super(itemview);


        }


    }



}



