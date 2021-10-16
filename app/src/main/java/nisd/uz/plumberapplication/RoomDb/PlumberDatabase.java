package nisd.uz.plumberapplication.RoomDb;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import nisd.uz.plumberapplication.Models.CartModel;
import nisd.uz.plumberapplication.Models.MainModel;

@Database(entities = {MainModel.class, CartModel.class}, version = 1)
public abstract class PlumberDatabase extends RoomDatabase {
    private static PlumberDatabase instance;
    public abstract PlumberDao plumberDao();

    public static synchronized PlumberDatabase getInstance(Context context){
        if (instance == null){
            instance = Room.databaseBuilder(context.getApplicationContext(), PlumberDatabase.class, "plumber_db")
                    .fallbackToDestructiveMigration()
                    .allowMainThreadQueries()
                    .build();
        }
        return instance;
    }
}
