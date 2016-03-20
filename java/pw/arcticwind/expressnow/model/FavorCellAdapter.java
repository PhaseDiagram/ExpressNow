package pw.arcticwind.expressnow.model;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import pw.arcticwind.expressnow.R;

//快递最新进展
//用于收藏页, 历史页等
public class FavorCellAdapter extends RecyclerView.Adapter<FavorCellAdapter.ViewHolder> {
    private List<FavorCell> list = new ArrayList<>();

    private OnItemClickListener onItemClickListener;

    public interface OnItemClickListener {
        void onItemClick(int position, FavorCell favorCell);
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView comView;
        public TextView numView;
        public TextView timeView;
        public TextView infoView;

        public ViewHolder(View itemView) {
            super(itemView);
            comView = (TextView) itemView.findViewById(R.id.favor_cell_com);
            numView = (TextView) itemView.findViewById(R.id.favor_cell_num);
            timeView = (TextView) itemView.findViewById(R.id.favor_cell_time);
            infoView = (TextView) itemView.findViewById(R.id.favor_cell_info);
        }

    }

    @Override
    public FavorCellAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.cell_favor, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(FavorCellAdapter.ViewHolder holder, final int position) {
        final FavorCell favorCell = list.get(position);
        holder.comView.setText(favorCell.getCom());
        holder.numView.setText(favorCell.getNum());
        holder.timeView.setText(favorCell.getTime());
        holder.infoView.setText(favorCell.getInfo());
        if (onItemClickListener != null) {
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onItemClickListener.onItemClick(position, favorCell);
                }
            });
        }

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public void addData(List<FavorCell> list) {
        this.list.addAll(list);
        notifyDataSetChanged();
    }

    public void modData(List<FavorCell> list) {
        this.list.clear();
        addData(list);
    }

}
