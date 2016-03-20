package pw.arcticwind.expressnow.model.bmob;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import pw.arcticwind.expressnow.R;

//主题
public class TopicAdapter extends RecyclerView.Adapter<TopicAdapter.ViewHolder> {

    private List<Topic> list = new ArrayList<>();
    private OnItemClickListener onItemClickListener;

    @Override
    public TopicAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.cell_topic, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(TopicAdapter.ViewHolder holder, final int position) {
        final Topic topic = list.get(position);
        holder.titleView.setText(topic.getTitle());
        if (onItemClickListener != null) {
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onItemClickListener.onItemClick(position, topic);
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView titleView;

        public ViewHolder(View itemView) {
            super(itemView);
            titleView = (TextView) itemView.findViewById(R.id.cell_topic_title);
        }

    }

    public interface OnItemClickListener {
        void onItemClick(int position, Topic topic);
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    public void addData(List<Topic> list) {
        this.list = list;
        notifyDataSetChanged();
    }

    public void modData(List<Topic> list) {
        this.list.clear();
        addData(list);
    }
}
