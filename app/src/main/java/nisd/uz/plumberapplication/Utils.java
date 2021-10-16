package nisd.uz.plumberapplication;

import android.content.Context;
import android.content.SharedPreferences;

import androidx.multidex.MultiDexApplication;

import java.text.NumberFormat;

public class Utils extends MultiDexApplication {

    private static String SHARED_PREF = "Shared_Prefs";

    private static SharedPreferences sharedPreferences;
    private static SharedPreferences.Editor editor;

    @Override
    public void onCreate() {
        super.onCreate();
        sharedPreferences = getSharedPreferences(SHARED_PREF, Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();
    }

    public static String moneyToDecimal(int money){
        NumberFormat numberFormat = NumberFormat.getNumberInstance();
        return numberFormat.format(money);
    }
}
