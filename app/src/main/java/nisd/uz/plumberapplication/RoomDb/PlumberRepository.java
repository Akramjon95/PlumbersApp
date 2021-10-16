package nisd.uz.plumberapplication.RoomDb;

import android.content.Context;
import android.util.Log;

import androidx.lifecycle.LiveData;

import java.util.List;

import io.reactivex.rxjava3.annotations.NonNull;
import io.reactivex.rxjava3.core.Completable;
import io.reactivex.rxjava3.core.Observer;
import io.reactivex.rxjava3.core.Single;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.disposables.Disposable;
import io.reactivex.rxjava3.schedulers.Schedulers;
import nisd.uz.plumberapplication.Models.CartModel;
import nisd.uz.plumberapplication.Models.MainModel;
import nisd.uz.plumberapplication.Network.ApiClient;

public class PlumberRepository {
    private static final String TAG = "PlumberRepository";
    private final PlumberDao dao;
    Context context;
    CompositeDisposable disposable;

    public PlumberRepository(Context context) {
        PlumberDatabase database  = PlumberDatabase.getInstance(context);
        dao = database.plumberDao();
        this.context = context;
        disposable = new CompositeDisposable();
    }

    public void insertProducts(List<MainModel> mainModels){
        disposable.add(
                dao.insertProducts(mainModels)
                .toObservable()
                .subscribeOn(Schedulers.io())
                .subscribe()
        );
    }

    public Completable insertCart(CartModel cartModel){
        return dao.insertCart(cartModel);
    }

    public LiveData<List<MainModel>>getProductsLive(){
        return dao.getProductsLive();
    }

    public LiveData<List<MainModel>>getRecoomentListLive(){
        return dao.getRecoomentListLive();
    }
    public List<MainModel>searchAllProducts(String query){
        return dao.searchAllProducts(query);
    }

    public LiveData<List<MainModel>>searchAllProductsLive(String query){
        return dao.searchAllProductsLive(query);
    }
    //cart
    public LiveData<List<CartModel>>getAllCartProductsLive(){
        return dao.getAllCartProductsLive();
    }
    public List<CartModel> getAllCartProducts(){
        return dao.getAllCartProducts();
    }
    public Single<Integer>getTotalPriceInCart(){
        return dao.getTotalPriceInCart();
    }
    public Completable deleteCart(CartModel cartModel){
        return dao.deleteCart(cartModel);
    }
    public LiveData<List<CartModel>> getCartCount(){
        return dao.getCartCount();
    }


    public void loadProducts(){
        ApiClient.getApiInterface().getAllProducts()
                .toObservable()
                .subscribeOn(Schedulers.io())
                .subscribe(new Observer<List<MainModel>>() {
                    @Override
                    public void onSubscribe(@NonNull Disposable d) {

                    }

                    @Override
                    public void onNext(@NonNull List<MainModel> mainModels) {
                            insertProducts(mainModels);
                        Log.d(TAG, "onNext: product " + mainModels.size());
                    }

                    @Override
                    public void onError(@NonNull Throwable e) {
                        Log.e(TAG, "onError: products ", e);
                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }

    public void updateProducts(MainModel model){
        dao.updateProduct(model);
    }



}
