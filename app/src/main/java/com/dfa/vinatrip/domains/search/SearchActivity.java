package com.dfa.vinatrip.domains.search;

import android.os.Build;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.dfa.vinatrip.MainFunction.Province.Province;
import com.dfa.vinatrip.MainFunction.Province.ProvinceDetail.ProvinceDetailActivity_;
import com.dfa.vinatrip.utils.RecyclerItemClickListener;
import com.dfa.vinatrip.R;
import com.dfa.vinatrip.services.DataService;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;

import java.util.List;

@EActivity(R.layout.activity_search)
public class SearchActivity extends AppCompatActivity {

    @Bean
    DataService dataService;

    @ViewById(R.id.my_toolbar)
    Toolbar toolbar;

    @ViewById(R.id.activity_search_rv_result)
    RecyclerView rvResult;

    private List<Province> provinceList;
    private SearchProvinceAdapter searchProvinceAdapter;
    private android.support.v7.app.ActionBar actionBar;
    private SearchView searchView;

    @AfterViews
    void onCreate() {
        setupActionBar();
        changeColorStatusBar();

        provinceList = dataService.getProvinceList();
        searchProvinceAdapter = new SearchProvinceAdapter(this, provinceList);
        rvResult.setAdapter(searchProvinceAdapter);

        LinearLayoutManager manager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        rvResult.setLayoutManager(manager);
        DividerItemDecoration decoration = new DividerItemDecoration(rvResult.getContext(), manager.getOrientation());
        rvResult.addItemDecoration(decoration);

        rvResult.addOnItemTouchListener(
                new RecyclerItemClickListener(this, rvResult, new RecyclerItemClickListener.OnItemClickListener() {
                    @Override
                    public void onItemClick(View view, int position) {
                        // Send Province to ProvinceDetailActivity
                        ProvinceDetailActivity_.intent(SearchActivity.this).province(provinceList.get(position))
                                               .start();
                    }

                    @Override
                    public void onLongItemClick(View view, int position) {

                    }
                }));
    }

    public void setupActionBar() {
        setSupportActionBar(toolbar);
        actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayShowTitleEnabled(false);

            actionBar.setDisplayHomeAsUpEnabled(true);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            super.onBackPressed();
            return true;
        }
        return false;
    }

    public void changeColorStatusBar() {
        if (Build.VERSION.SDK_INT >= 21) {
            getWindow().setStatusBarColor(ContextCompat.getColor(this, R.color.colorStatusBar));
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.search_menu, menu);
        searchView = (SearchView) menu.findItem(R.id.search_menu_menuSearch).getActionView();

        // Expand searchView, if not, it just show icon
        searchView.setIconifiedByDefault(false);

        searchView.setQueryHint("Tìm kiếm...");

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                searchProvinceAdapter.getFilter().filter(newText);
                return true;
            }
        });
        return true;
    }
}
