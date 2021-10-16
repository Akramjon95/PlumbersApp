package nisd.uz.plumberapplication.Network;

import java.util.List;

import io.reactivex.rxjava3.core.Flowable;
import nisd.uz.plumberapplication.Models.MainModel;
import retrofit2.http.GET;

public interface ApiInterface {

    //products
    @GET("posts")
    Flowable<List<MainModel>>getAllProducts();
}
