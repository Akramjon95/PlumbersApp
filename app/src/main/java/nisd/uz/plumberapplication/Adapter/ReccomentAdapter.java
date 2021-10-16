package nisd.uz.plumberapplication.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import nisd.uz.plumberapplication.Models.MainModel;
import nisd.uz.plumberapplication.R;
import nisd.uz.plumberapplication.RoomDb.PlumberViewModel;

public class ReccomentAdapter extends RecyclerView.Adapter<ReccomentAdapter.RecommentHolder> {
    Context context;
    List<MainModel>modelList = new ArrayList<>();
    PlumberViewModel viewModel;
    public ReccomentAdapter(Context context, List<MainModel> modelList) {
        this.context = context;
        this.modelList = modelList;
        viewModel = ViewModelProviders.of((FragmentActivity) context).get(PlumberViewModel.class);
    }

    @NonNull
    @Override
    public RecommentHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recomment_list, parent, false);

        return new RecommentHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecommentHolder holder, int position) {
        MainModel mainModel = modelList.get(position);

        holder.tvProductName.setText(mainModel.getTitle());
        holder.tvQuantity.setText(String.valueOf(mainModel.getId()));
    }

    @Override
    public int getItemCount() {
        return modelList.size();
    }

    public void rcSubmitList(List<MainModel>mainModels){
        modelList.clear();
        modelList.addAll(mainModels);
        notifyDataSetChanged();
    }

    public class RecommentHolder extends RecyclerView.ViewHolder{
        private TextView tvProductName, tvQuantity, tvSum;

        public RecommentHolder(@NonNull View itemView) {
            super(itemView);

            tvProductName = (TextView) itemView.findViewById(R.id.rc_product_name);
            tvQuantity = (TextView) itemView.findViewById(R.id.rc_amount);
        }
    }
}
