package nisd.uz.plumberapplication;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;

import java.util.ArrayList;
import java.util.List;

import nisd.uz.plumberapplication.Models.MainModel;

public class CartActivity extends AppCompatActivity {

    List<MainModel>modelList = new ArrayList<>();
    String title;
    String amount;

    private RecyclerView recyclerView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);

        title = getIntent().getStringExtra("product_title");
        amount = getIntent().getStringExtra("product_amount");

//        modelList.add(title, amount, );

        recyclerView = (RecyclerView)findViewById(R.id.cart_recyclerView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
    }
}