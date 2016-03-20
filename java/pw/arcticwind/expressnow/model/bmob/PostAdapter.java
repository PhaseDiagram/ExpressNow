package pw.arcticwind.expressnow.model.bmob;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import pw.arcticwind.expressnow.R;

//帖子
public class PostAdapter extends RecyclerView.Adapter<PostAdapter.ViewHolder> {

    private List<Post> list = new ArrayList<>();

    @Override
    public PostAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.cell_body, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(PostAdapter.ViewHolder holder, final int position) {
        final Post post = list.get(position);
        holder.bodyView.setText(post.getBody());
        holder.userView.setText(post.getUser());
        holder.timeView.setText(post.getTime());

    }

    @Override
    public int getItemCount() {
        return list.size();
    }


    public static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView bodyView;
        public TextView userView;
        public TextView timeView;

        public ViewHolder(View itemView) {
            super(itemView);
            bodyView = (TextView) itemView.findViewById(R.id.cell_body_body);
            userView = (TextView) itemView.findViewById(R.id.cell_body_user);
            timeView = (TextView) itemView.findViewById(R.id.cell_body_time);
        }

    }

    public void addData(List<Post> list) {
        this.list = list;
        notifyDataSetChanged();
    }

    public void modData(List<Post> list) {
        this.list.clear();
        addData(list);
    }

}
