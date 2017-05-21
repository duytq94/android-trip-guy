package com.dfa.vinatrip.domains.main.plan.make_plan;

import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.dfa.vinatrip.R;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;

import java.util.ArrayList;
import java.util.List;

@EActivity(R.layout.activity_choose_background_plan)
public class ChooseBackgroundPlan extends AppCompatActivity {

    @ViewById(R.id.activity_choose_background_plan_rv_item)
    RecyclerView rvItem;

    private List<Integer> listIdBackground;
    private ChooseBackgroundPlanAdapter adapter;

    @AfterViews
    void onCreate() {
        listIdBackground = new ArrayList<>();
        listIdBackground.add(R.drawable.bg_test1);
        listIdBackground.add(R.drawable.bg_test2);
        listIdBackground.add(R.drawable.bg_test3);
        listIdBackground.add(R.drawable.bg_test4);

        adapter = new ChooseBackgroundPlanAdapter(this, listIdBackground);

        rvItem.setAdapter(adapter);
        LinearLayoutManager manager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        rvItem.setLayoutManager(manager);
    }
}
