package nisd.uz.plumberapplication;

import android.os.Bundle;
import android.widget.AutoCompleteTextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.ViewModelProviders;
import androidx.navigation.NavController;
import androidx.navigation.NavDestination;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.textfield.TextInputLayout;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.rxjava3.disposables.CompositeDisposable;
import nisd.uz.plumberapplication.Adapter.MainAdapter;
import nisd.uz.plumberapplication.Models.MainModel;
import nisd.uz.plumberapplication.RoomDb.PlumberDatabase;
import nisd.uz.plumberapplication.RoomDb.PlumberViewModel;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";
    NavController navController;
    Toolbar toolbar;
    private AppBarConfiguration mAppBarConfiguration;
    private MainAdapter adapter;

    private TextInputLayout catDropdownMenu;
    private AutoCompleteTextView catTextview;
    private RecyclerView recyclerView;
    private List<MainModel> mainModelList = new ArrayList<>();
    PlumberDatabase database;
    PlumberViewModel viewModel;
    CompositeDisposable disposable;
    BottomNavigationView bottomNav;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        disposable = new CompositeDisposable();
//        database = PlumberDatabase.getInstance(this);
        viewModel = ViewModelProviders.of(this).get(PlumberViewModel.class);


//        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
//        recyclerView.setHasFixedSize(true);
//        recyclerView.setLayoutManager(new LinearLayoutManager(this));
//
//        adapter = new MainAdapter(this, mainModelList);
//        recyclerView.setAdapter(adapter);
//
//        viewModel.getProducts().observe(this, new Observer<List<MainModel>>() {
//            @Override
//            public void onChanged(List<MainModel> mainModels) {
//                adapter.submitList(mainModels);
//                Log.d(TAG, "onChanged: " + mainModels.size());
//            }
//        });

//        DrawerLayout drawer = findViewById(R.id.drawer_layout);
//        NavigationView navigationView = findViewById(R.id.nav_view);
        bottomNav = (BottomNavigationView) findViewById(R.id.bottomNav);
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.

        mAppBarConfiguration = new AppBarConfiguration.Builder(
                R.id.homeFragment, R.id.cartFragment, R.id.searchFragment, R.id.settingsFragment).build();
        navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        NavigationUI.setupWithNavController(bottomNav, navController);
        NavigationUI.setupWithNavController(toolbar, navController);
        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);

        navController.addOnDestinationChangedListener(new NavController.OnDestinationChangedListener() {
            @Override
            public void onDestinationChanged(@NonNull NavController controller, @NonNull NavDestination destination, @Nullable Bundle arguments) {
                int id = destination.getId();

                if (id == R.id.homeFragment){
                    navController.getGraph().setLabel("Products");
                }
                else if (id == R.id.cartFragment){
                    navController.getGraph().setLabel("Cart");
                }
                else if (id == R.id.searchFragment){
                    navController.getGraph().setLabel("Search...");
                }

            }
        });

    }

    @Override
    protected void onResume() {
        super.onResume();
        viewModel.loadProducts();
    }

//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        // Inflate the menu; this adds items to the action bar if it is present.
//        getMenuInflater().inflate(R.menu.toolbar_menu, menu);
//        MenuItem menuItem = menu.findItem(R.id.search_action);
//        View actionView = menuItem.getActionView();
//
//        actionView.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                navController.navigate(R.id.action_homeFragment_to_searchFragment);
//            }
//        });
////        SearchView searchView = (SearchView) findViewById(R.id.searchView);
////        searchView
//        return true;
//    }

    @Override
    public boolean onSupportNavigateUp() {
        return NavigationUI.navigateUp(navController, mAppBarConfiguration)
                || super.onSupportNavigateUp();
    }
}