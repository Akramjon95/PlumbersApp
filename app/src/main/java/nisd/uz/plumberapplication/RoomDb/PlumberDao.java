package nisd.uz.plumberapplication.RoomDb;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

import io.reactivex.rxjava3.core.Completable;
import io.reactivex.rxjava3.core.Single;
import nisd.uz.plumberapplication.Models.CartModel;
import nisd.uz.plumberapplication.Models.MainModel;

@Dao
public interface PlumberDao {

    //product
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    Completable insertProducts(List<MainModel> mainModel);

//    @Insert
//    void insertItemByHand();

    @Query("Select * from mainmodel order by id desc")
    List<MainModel> getAllProducts();

    @Query("select * from mainmodel")
    LiveData<List<MainModel>> getProductsLive();

    @Query("select *from mainmodel order by id asc")
    LiveData<List<MainModel>> getRecoomentListLive();

    @Update
    void updateProduct(MainModel model);

    //search products
    @Query("select * from mainmodel where title like :query")
    List<MainModel>searchAllProducts(String query);

    @Query("select * from mainmodel where title like :query")
    LiveData<List<MainModel>>searchAllProductsLive(String query);

    //cart
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    Completable insertCart(CartModel cartModel);
    @Query("select * from cart_table order by id desc")
    List<CartModel>getAllCartProducts();
    @Query("select * from cart_table")
    LiveData<List<CartModel>>getAllCartProductsLive();

    @Query("select sum(userId*quantity) from cart_table")
    Single<Integer> getTotalPriceInCart();

    @Delete
    Completable deleteCart(CartModel cartModel);

    @Query("select * from cart_table")
    LiveData<List<CartModel>> getCartCount();
}
