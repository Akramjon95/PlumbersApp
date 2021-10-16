package nisd.uz.plumberapplication.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.RecyclerView;

import com.davidodari.simplecounter.SimpleCounter;

import java.util.ArrayList;
import java.util.List;

import kotlin.Unit;
import kotlin.jvm.functions.Function1;
import nisd.uz.plumberapplication.Models.CartModel;
import nisd.uz.plumberapplication.R;
import nisd.uz.plumberapplication.RoomDb.PlumberViewModel;
import nisd.uz.plumberapplication.Utils;

public class CartAdapter extends RecyclerView.Adapter<CartAdapter.CartViewHolder> {
    Context context;
    List<CartModel> cartModelList = new ArrayList<>();
    PlumberViewModel viewModel;
    CartInterface cartInterface;
    private int quantity=1;
    public CartAdapter(Context context, List<CartModel> cartModelList, CartInterface cartInterface) {
        this.context = context;
        this.cartModelList = cartModelList;
        this.cartInterface = cartInterface;
        viewModel = ViewModelProviders.of((FragmentActivity) context).get(PlumberViewModel.class);
    }

    @NonNull
    @Override
    public CartViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.cart_list, parent, false);
        return new CartViewHolder(view);
    }

    public void submitList(List<CartModel> cartModels){
        cartModelList.clear();
        cartModelList.addAll(cartModels);
        notifyDataSetChanged();
    }

    @Override
    public void onBindViewHolder(@NonNull CartViewHolder holder, int position) {

        CartModel cartModel = cartModelList.get(position);
        holder.cart_title.setText(cartModel.getTitle());
        holder.cart_amount.setText(String.valueOf(cartModel.getId()));
        holder.tvPrice.setText(String.valueOf(cartModel.getUserId()+" sum"));
        holder.totalPrice.setText(Utils.moneyToDecimal(cartModel.getUserId()*cartModel.getQuantity()) + " Sum");

        holder.txtQuantity.setText(String.valueOf(cartModel.getQuantity()));


        holder.plusQuantity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                quantity = Integer.parseInt(holder.txtQuantity.getText().toString());
//                quantity = Integer.parseInt(quantity +  holder.txtQuantity.getText().toString());
                quantity++;
                cartModel.setQuantity(quantity);
                cartInterface.onItemCounterClick(cartModel, position);
            }
        });

        holder.minusQuantity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (quantity > 1) {
                    quantity = Integer.parseInt(holder.txtQuantity.getText().toString());
                    quantity--;
                    cartModel.setQuantity(quantity);
                    cartInterface.onItemCounterClick(cartModel, position);
//                    holder.txtQuantity.setText(cartModel.getQuantity());
                }
            }
        });

    }


    @Override
    public int getItemCount() {
        return cartModelList.size();
    }

    public void changeCartCount(CartModel cartModel, int position){
        if (position != -1){
            cartModelList.get(position).setUserId(cartModel.getUserId());
            notifyItemChanged(position);
        }
    }

    public void deleteItem(int position){
       if (position != -1){
          cartModelList.remove(position);
          notifyItemRemoved(position);
       }
    }

    public class CartViewHolder extends RecyclerView.ViewHolder{

        private TextView cart_title, cart_amount;
        private ImageButton plusQuantity, minusQuantity;
        private TextView txtQuantity, tvPrice, totalPrice;
        private ImageView removeItem;
//        private SimpleCounter simpleCounter;


        public CartViewHolder(@NonNull View itemView) {
            super(itemView);
            cart_title = (TextView) itemView.findViewById(R.id.cart_product_name);
            cart_amount = (TextView) itemView.findViewById(R.id.cart_text_qoldiq);
            plusQuantity = (ImageButton) itemView.findViewById(R.id.plus_cart_btn);
            minusQuantity = (ImageButton) itemView.findViewById(R.id.minus_cart_btn);
            txtQuantity = (TextView) itemView.findViewById(R.id.quantity);
            tvPrice = (TextView) itemView.findViewById(R.id.tvPrice);
            totalPrice = (TextView) itemView.findViewById(R.id.cart_total_price);
            removeItem = (ImageView) itemView.findViewById(R.id.remove);
//            simpleCounter = (SimpleCounter) itemView.findViewById(R.id.simpleCounter);

            removeItem.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    cartInterface.deleteItem(cartModelList.get(getAdapterPosition()), getAdapterPosition());
                }
            });
        }

    }

    public interface  CartInterface{
        void onItemCounterClick(CartModel cartModel, int position);
        void deleteItem(CartModel cartModel, int position);
    }

}
