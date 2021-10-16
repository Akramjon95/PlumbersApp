package nisd.uz.plumberapplication.Fragments;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.core.CompletableObserver;
import io.reactivex.rxjava3.core.SingleObserver;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.disposables.Disposable;
import io.reactivex.rxjava3.schedulers.Schedulers;
import nisd.uz.plumberapplication.Adapter.CartAdapter;
import nisd.uz.plumberapplication.Models.CartModel;
import nisd.uz.plumberapplication.R;
import nisd.uz.plumberapplication.RoomDb.PlumberDatabase;
import nisd.uz.plumberapplication.RoomDb.PlumberViewModel;
import nisd.uz.plumberapplication.Utils;


public class CartFragment extends Fragment implements CartAdapter.CartInterface {
    RecyclerView recyclerView;
    List<CartModel> cartModelList = new ArrayList<>();
    PlumberDatabase database;
    PlumberViewModel viewModel;
    CompositeDisposable disposable;
    CartAdapter adapter;
    NavController navController;

    private TextView TvtotalPrice;
    RelativeLayout relativeLayout;
    int totalPrice = 0;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        setHasOptionsMenu(true);
//        ((AppCompatActivity)getActivity()).getSupportActionBar().setTitle("Shopping Cart");
//        navController.getGraph().setLabel("Shopping...");
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_cart, container, false);

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        navController = Navigation.findNavController(view);
        database = PlumberDatabase.getInstance(getContext());
        disposable = new CompositeDisposable();
        viewModel = ViewModelProviders.of(this).get(PlumberViewModel.class);

        TvtotalPrice = (TextView)view.findViewById(R.id.textSum);
        relativeLayout = (RelativeLayout) view.findViewById(R.id.total_linear);
        recyclerView = (RecyclerView) view.findViewById(R.id.recyclerView_cart);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        adapter = new CartAdapter(getContext(), cartModelList, CartFragment.this);
        recyclerView.setAdapter(adapter);

        viewModel.getAllCartProducts().observe(getViewLifecycleOwner(), new Observer<List<CartModel>>() {
            @Override
            public void onChanged(List<CartModel> cartModels) {
                adapter.submitList(cartModels);
                Log.d("TAG", "onChanged: cartList: " + cartModelList.size());
            }
        });

        updateTotalPrice();

    }


    @Override
    public void onItemCounterClick(CartModel cartModel, int position) {
            insert(cartModel, position);
    }

    @Override
    public void deleteItem(CartModel cartModel, int position) {
        viewModel.deleteCart(cartModel)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new CompletableObserver() {
                    @Override
                    public void onSubscribe(@io.reactivex.rxjava3.annotations.NonNull Disposable d) {
                        disposable.add(d);
                    }

                    @Override
                    public void onComplete() {
                        adapter.deleteItem(position);
                        updateTotalPrice();
                    }

                    @Override
                    public void onError(@io.reactivex.rxjava3.annotations.NonNull Throwable e) {

                    }
                });
    }

    private void insert(CartModel cartModel, int position){
        viewModel.insertCart(cartModel)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new CompletableObserver() {
                    @Override
                    public void onSubscribe(@io.reactivex.rxjava3.annotations.NonNull Disposable d) {
                        disposable.add(d);
                    }

                    @Override
                    public void onComplete() {
                        adapter.changeCartCount(cartModel, position);
                        updateTotalPrice();
                    }

                    @Override
                    public void onError(@io.reactivex.rxjava3.annotations.NonNull Throwable e) {

                    }
                });
    }

    private void updateTotalPrice(){
        viewModel.getTotalPriceInCart()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new SingleObserver<Integer>() {
                    @Override
                    public void onSubscribe(@io.reactivex.rxjava3.annotations.NonNull Disposable d) {
                        disposable.add(d);
                    }

                    @Override
                    public void onSuccess(@io.reactivex.rxjava3.annotations.NonNull Integer integer) {
                        TvtotalPrice.setText(Utils.moneyToDecimal(integer) + " So'm");
                        totalPrice = integer;
                        if (integer == 0){
                            relativeLayout.setVisibility(View.GONE);
                        }else {
                            relativeLayout.setVisibility(View.VISIBLE);
                        }

                    }

                    @Override
                    public void onError(@io.reactivex.rxjava3.annotations.NonNull Throwable e) {
                        TvtotalPrice.setText(Utils.moneyToDecimal(0)+" So'm");
                        totalPrice = 0;
                    }
                });
    }
}