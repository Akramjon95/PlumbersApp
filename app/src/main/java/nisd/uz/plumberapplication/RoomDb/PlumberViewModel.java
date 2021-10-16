package nisd.uz.plumberapplication.RoomDb;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import java.util.List;

import io.reactivex.rxjava3.core.Completable;
import io.reactivex.rxjava3.core.Single;
import nisd.uz.plumberapplication.Models.CartModel;
import nisd.uz.plumberapplication.Models.MainModel;

public class PlumberViewModel extends AndroidViewModel {
    PlumberRepository repository;

    public PlumberViewModel(@NonNull Application application) {
        super(application);
        repository = new PlumberRepository(application);
    }

    //product
    public void loadProducts(){
        repository.loadProducts();
    }

    public LiveData<List<MainModel>>getProducts(){
        return repository.getProductsLive();
    }

    public LiveData<List<MainModel>>getRecoomentListLive(){
        return repository.getRecoomentListLive();
    }

    //search
    public List<MainModel>searchAllProducts(String query){
        return repository.searchAllProducts(query);
    }
    public LiveData<List<MainModel>>searchAllProductsLive(String query){
        return repository.searchAllProductsLive(query);
    }

    //cart
    public Completable insertCart(CartModel cartModels){
       return repository.insertCart(cartModels);
    }
    public LiveData<List<CartModel>>getAllCartProducts(){
        return repository.getAllCartProductsLive();
    }

    public Single<Integer>getTotalPriceInCart(){
        return repository.getTotalPriceInCart();
    }
    public Completable deleteCart(CartModel cartModel){
        return repository.deleteCart(cartModel);
    }
    public LiveData<List<CartModel>>getCartCount(){
        return repository.getCartCount();
    }
}
