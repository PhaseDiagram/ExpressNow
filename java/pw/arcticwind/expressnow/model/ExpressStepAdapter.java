package pw.arcticwind.expressnow.model;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import pw.arcticwind.expressnow.R;

//快递步骤.
//有header
public class ExpressStepAdapter extends RecyclerView.Adapter<ExpressStepAdapter.ViewHolder> {

    private List<ExpressStep> list = new ArrayList<>();

    private View headerView;
    public static final int HEADER = 1;
    public static final int NORMAL = 2;

    private OnItemClickListener onItemClickListener;

    public interface OnItemClickListener {
        void onItemClick(int position, FavorCell favorCell);
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public TextView timeView;
        public TextView contextView;

        public ViewHolder(View itemView) {
            super(itemView);
            if (itemView == headerView) {
                return;
            }
            timeView = (TextView) itemView.findViewById(R.id.time_cell);
            contextView = (TextView) itemView.findViewById(R.id.context_cell);
        }
    }

    @Override
    public int getItemCount() {
        if (headerView == null) {
            return list.size();
        } else {
            return list.size() - 1;
        }
    }

    @Override
    public ExpressStepAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (headerView != null && viewType == HEADER) {
            return new ViewHolder(headerView);
        }
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.cell_result_list, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        if (getItemViewType(position) == HEADER) {
            return;
        }
        position = holder.getLayoutPosition();
        if (headerView != null) {
            position--;
        }
        holder.contextView.setText(list.get(position).getStepContext());
        holder.timeView.setText(list.get(position).getStepTime());
    }

    @Override
    public int getItemViewType(int position) {
        if (headerView == null) {
            return NORMAL;
        }
        if (position == 0) {
            return HEADER;
        }
        return NORMAL;
    }

    public void setHeaderView(View headerView) {
        this.headerView = headerView;
        notifyItemInserted(0);
    }

    public View getHeaderView() {
        return headerView;
    }

    public void addData(List<ExpressStep> list) {
        this.list.addAll(list);
        notifyDataSetChanged();
    }

    public void modData(List<ExpressStep> list) {
        this.list.clear();
        addData(list);
    }

}
