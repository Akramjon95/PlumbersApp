package nisd.uz.plumberapplication.Fragments;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.NavigationUI;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.core.CompletableObserver;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.disposables.Disposable;
import io.reactivex.rxjava3.schedulers.Schedulers;
import nisd.uz.plumberapplication.Adapter.MainAdapter;
import nisd.uz.plumberapplication.Adapter.ReccomentAdapter;
import nisd.uz.plumberapplication.Models.CartModel;
import nisd.uz.plumberapplication.Models.MainModel;
import nisd.uz.plumberapplication.R;
import nisd.uz.plumberapplication.RoomDb.PlumberDatabase;
import nisd.uz.plumberapplication.RoomDb.PlumberViewModel;


public class HomeFragment extends Fragment implements MainAdapter.ProductInterface{
    private static final String TAG = "HomeFragment";
    private RecyclerView recyclerView, recomentRecyclerView;
    private List<MainModel> mainModelList = new ArrayList<>();
    PlumberDatabase database;
    PlumberViewModel viewModel;
    CompositeDisposable disposable;
    BottomNavigationView bottomNav;
    private MainAdapter adapter;
    NavController navController;
    int cartQuantity = 0;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        setHasOptionsMenu(true);

//        ((AppCompatActivity)getActivity()).getSupportActionBar().setTitle("Products ");
//        navController.getGraph().setLabel("Plumber shop");
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_home2, container, false);

    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        disposable = new CompositeDisposable();
        database = PlumberDatabase.getInstance(getContext());
        navController = Navigation.findNavController(view);
        viewModel = ViewModelProviders.of(this).get(PlumberViewModel.class);

//        navController.getGraph().setLabel("Search...");
        recomentRecyclerView = (RecyclerView)view.findViewById(R.id.recyclerView_recomment);

        recyclerView = (RecyclerView) view.findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        adapter = new MainAdapter(getContext(), mainModelList, this);
        recyclerView.setAdapter(adapter);

        viewModel.getProducts().observe(getViewLifecycleOwner(), new Observer<List<MainModel>>() {
            @Override
            public void onChanged(List<MainModel> mainModels) {
                adapter.submitList(mainModels);
                Log.d(TAG, "onChanged: " + mainModels.size());
            }
        });

        recomentRecyclerView.setHasFixedSize(true);
        recomentRecyclerView.setLayoutManager(new LinearLayoutManager(getContext(),LinearLayoutManager.HORIZONTAL, false));

        ReccomentAdapter reccomentAdapter = new ReccomentAdapter(getContext(), mainModelList);
        recomentRecyclerView.setAdapter(reccomentAdapter);
        viewModel.getRecoomentListLive().observe(getViewLifecycleOwner(), new Observer<List<MainModel>>() {
            @Override
            public void onChanged(List<MainModel> mainModels) {
                reccomentAdapter.rcSubmitList(mainModels);
            }
        });
        closeKeyboard();

        //get cart quantity
        viewModel.getCartCount().observe(getViewLifecycleOwner(), new Observer<List<CartModel>>() {
            @Override
            public void onChanged(List<CartModel> cartModels) {
                cartQuantity = cartModels.size();
                getActivity().invalidateOptionsMenu();
            }
        });

    }

    public void closeKeyboard(){
        InputMethodManager img = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
        img.hideSoftInputFromWindow(getView().getWindowToken(), 0);
    }
    @Override
    public void onResume() {
        super.onResume();
      viewModel.loadProducts();
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        inflater.inflate(R.menu.toolbar_menu, menu);
        super.onCreateOptionsMenu(menu, inflater);

        SearchView searchView = (SearchView) menu.findItem(R.id.search_action).getActionView();
        searchView.setOnSearchClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Navigation.findNavController(getView())
                        .navigate(HomeFragmentDirections.actionHomeFragmentToSearchFragment());
            }
        });

        MenuItem menuItem = menu.findItem(R.id.cartFragment);
        View actionView = menuItem.getActionView();

        TextView cartCount = actionView.findViewById(R.id.cart_badge_text_view);

        cartCount.setText(String.valueOf(cartQuantity));

        cartCount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                onOptionsItemSelected(menuItem);
                navController.navigate(R.id.action_homeFragment_to_cartFragment);
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        return NavigationUI.onNavDestinationSelected(item, navController) ||
                super.onOptionsItemSelected(item);
    }

    @Override
    public void OnItemClick(MainModel mainModel) {

    }

    @Override
    public void addItem(MainModel mainModel) {
        insertCartItem(mainModel);
    }

    private void insertCartItem(MainModel mainModel){
        CartModel cartModel = new CartModel();
        cartModel.setId(mainModel.getId());
        cartModel.setUserId(mainModel.getUserId());
        cartModel.setTitle(mainModel.getTitle());
        cartModel.setBody(mainModel.getBody());
        cartModel.setQuantity(1);

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
                        Snackbar.make(requireView(), "Mahsulot savatga qo'shildi", Snackbar.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onError(@io.reactivex.rxjava3.annotations.NonNull Throwable e) {
                        Log.e("TAG", "Error: insertCart: ", e);
                    }
                });
    }

}