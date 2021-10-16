package nisd.uz.plumberapplication.Fragments;

import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.utils.ColorTemplate;

import java.util.ArrayList;

import nisd.uz.plumberapplication.R;


public class SettingsFragment extends Fragment {

    BarChart barChart;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_settings, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        barChart = (BarChart) view.findViewById(R.id.barchart);
        ArrayList<BarEntry> months = new ArrayList<>();
        months.add(new BarEntry(8f, 0));
        months.add(new BarEntry(2f, 1));
        months.add(new BarEntry(5f, 2));
        months.add(new BarEntry(20f, 3));
        months.add(new BarEntry(15f, 4));
        months.add(new BarEntry(19f, 5));
        months.add(new BarEntry( 8f, 6));
        months.add(new BarEntry(2f, 7));
        months.add(new BarEntry(5f, 8));
        months.add(new BarEntry(20f, 9));
        months.add(new BarEntry(19f, 10));
        months.add(new BarEntry(15f, 11));

        BarDataSet barDataSet = new BarDataSet(months, "Cells");
        barDataSet.setColors(ColorTemplate.VORDIPLOM_COLORS);
        barDataSet.setValueTextColor(Color.BLACK);
//        barDataSet.setValueTextSize(16f);

        ArrayList<String> labels = new ArrayList<String>();
        labels.add("Jan");
        labels.add("Feb");
        labels.add("Mar");
        labels.add("Apr");
        labels.add("May");
        labels.add("Jun");
        labels.add("Jul");
        labels.add("Aug");
        labels.add("Sep");
        labels.add("Oct");
        labels.add("Nov");
        labels.add("Desc");

        BarData barData = new BarData(labels, barDataSet);
        barChart.setFitsSystemWindows(true);
        barChart.setData(barData);
//        barChart.getDescription().setText("");
        barChart.animateY(2000);
    }
}