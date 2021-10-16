package nisd.uz.plumberapplication.Adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import nisd.uz.plumberapplication.CartActivity;
import nisd.uz.plumberapplication.Models.MainModel;
import nisd.uz.plumberapplication.R;
import nisd.uz.plumberapplication.RoomDb.PlumberViewModel;

public class MainAdapter extends RecyclerView.Adapter<MainAdapter.MainHolder> implements Filterable {

    private Context context;
    private List<MainModel> mainModelList = new ArrayList<>();
    private List<MainModel>modelListFull;
    PlumberViewModel viewModel;
    ProductInterface productInterface;

    public MainAdapter(Context context, List<MainModel> mainModelList,ProductInterface productInterface) {
        this.context = context;
        this.mainModelList = mainModelList;
        this.productInterface = productInterface;
        modelListFull = new ArrayList<>(mainModelList);
        viewModel = ViewModelProviders.of((FragmentActivity) context).get(PlumberViewModel.class);
    }

    @NonNull
    @Override
    public MainHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.main_product_list, parent, false);

        return new MainHolder(view);
    }

    public void submitList(List<MainModel> mainModels){
        mainModelList.clear();
        mainModelList.addAll(mainModels);
        notifyDataSetChanged();
    }

    @Override
    public void onBindViewHolder(@NonNull MainHolder holder, int position) {
        MainModel model = mainModelList.get(position);
        holder.product_name.setText(model.getTitle());
        holder.product_qoldiq.setText(String.valueOf(model.getId()));

    }

    @Override
    public int getItemCount() {
        return mainModelList.size();
    }

//    @Override
//    public void onClick(View v) {
//        onClick(v);
//    }

    @Override
    public Filter getFilter() {
        return productFilter;
    }

    private final Filter productFilter = new Filter() {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            List<MainModel>filterAllProductList = new ArrayList<>();
            if (constraint == null || constraint.length() == 0){

                filterAllProductList.addAll(modelListFull);

            }else {
                String filterPattern = constraint.toString().toLowerCase().trim();
            }
            FilterResults results = new FilterResults();
            results.values = filterAllProductList;
            return results;
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            mainModelList.clear();
            mainModelList.addAll((List) results.values);
            notifyDataSetChanged();
        }
    };

    public class MainHolder extends RecyclerView.ViewHolder {
        private TextView product_name, product_qoldiq;
        private ImageView addToCart;

        public MainHolder(@NonNull View itemView) {
            super(itemView);

            product_name = (TextView)itemView.findViewById(R.id.product_name);
            product_qoldiq = (TextView) itemView.findViewById(R.id.text_qoldiq);
            addToCart = (ImageView) itemView.findViewById(R.id.add_cart_btn);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    productInterface.OnItemClick(mainModelList.get(getAdapterPosition()));
                }
            });

            addToCart.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    MainModel mainModel = mainModelList.get(getAdapterPosition());
                    productInterface.addItem(mainModel);
                    notifyItemChanged(getAdapterPosition());
                }
            });

        }

    }

    public interface ProductInterface{
        void OnItemClick(MainModel mainModel);
        void addItem(MainModel mainModel);
    }
}
