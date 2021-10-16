package nisd.uz.plumberapplication.Fragments;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.core.CompletableObserver;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.disposables.Disposable;
import io.reactivex.rxjava3.schedulers.Schedulers;
import nisd.uz.plumberapplication.Adapter.MainAdapter;
import nisd.uz.plumberapplication.Models.CartModel;
import nisd.uz.plumberapplication.Models.MainModel;
import nisd.uz.plumberapplication.R;
import nisd.uz.plumberapplication.RoomDb.PlumberViewModel;


public class SearchFragment extends Fragment implements MainAdapter.ProductInterface ,SearchView.OnQueryTextListener {

    RecyclerView recyclerView;
    List<MainModel>modelList = new ArrayList<>();
    MainAdapter adapter;
    PlumberViewModel viewModel;
    CompositeDisposable disposable;
    NavController navController;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        setHasOptionsMenu(true);
//        navController.getGraph().setLabel("Search...");
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_search, container, false);

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        navController = Navigation.findNavController(view);
        viewModel = ViewModelProviders.of(this).get(PlumberViewModel.class);
        disposable = new CompositeDisposable();
//        ((AppCompatActivity)getActivity()).getSupportActionBar().setTitle("Search...");

        adapter = new MainAdapter(getContext(), modelList, this);
        recyclerView = (RecyclerView) view.findViewById(R.id.recycler_search_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setHasFixedSize(true);
        recyclerView.setAdapter(adapter);
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        inflater.inflate(R.menu.toolbar_menu,menu);
        super.onCreateOptionsMenu(menu, inflater);

        SearchView searchView = (SearchView) menu.findItem(R.id.search_action).getActionView();
        searchView.requestFocusFromTouch();
        searchView.setIconified(false);
        searchView.setFocusable(true);
        searchView.setBackground(null);

        searchView.setOnQueryTextListener(this);
    }

    public void closeKeyboard(){
        InputMethodManager img = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
        img.hideSoftInputFromWindow(getView().getWindowToken(), 0);
    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        query = "%" + query.toLowerCase().trim() + "%";

        viewModel.searchAllProductsLive(query).observe(getViewLifecycleOwner(), new Observer<List<MainModel>>() {
            @Override
            public void onChanged(List<MainModel> mainModels) {
                adapter = new MainAdapter(getContext(),mainModels, SearchFragment.this);
                recyclerView.setAdapter(adapter);
            }
        });
        return false;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
       if (newText.isEmpty()){
           modelList = new ArrayList<>();
           adapter = new MainAdapter(getContext(), modelList, this);
           recyclerView.setAdapter(adapter);
       }else {
           newText = "%" + newText.toLowerCase().trim() + "%";

           viewModel.searchAllProductsLive(newText).observe(getViewLifecycleOwner(), new Observer<List<MainModel>>() {
               @Override
               public void onChanged(List<MainModel> mainModels) {
                   adapter = new MainAdapter(getContext(), mainModels, SearchFragment.this);
                   recyclerView.setAdapter(adapter);
               }
           });
       }
       return false;
    }


    @Override
    public void OnItemClick(MainModel mainModel) {

    }

    @Override
    public void addItem(MainModel mainModel) {
        insertCartFromSearchFragment(mainModel);
    }

    private void insertCartFromSearchFragment(MainModel mainModel){
        CartModel cartModel = new CartModel();
        cartModel.setId(mainModel.getId());
        cartModel.setUserId(mainModel.getUserId());
        cartModel.setTitle(mainModel.getTitle());
        cartModel.setBody(mainModel.getBody());

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
                        Snackbar.make(requireView(), "mahsulot savatga qo'shildi", Snackbar.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onError(@io.reactivex.rxjava3.annotations.NonNull Throwable e) {
                        Log.e("TAG", "Error: insertCart from SearchFragment: ", e);
                    }
                });
    }
}