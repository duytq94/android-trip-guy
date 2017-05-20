package com.dfa.vinatrip.domains.search;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import com.dfa.vinatrip.R;
import com.dfa.vinatrip.domains.main.province.Province;

import java.util.ArrayList;
import java.util.List;

// This adapter has feature filter list (for search)
public class SearchProvinceAdapter extends RecyclerView.Adapter<SearchProvinceAdapter.ProvinceViewHolder> implements Filterable {
    private List<Province> provinceList;
    private LayoutInflater layoutInflater;

    private ItemFilter itemFilter = new ItemFilter();
    private List<Province> provinceListFiltered;

    public SearchProvinceAdapter(Context context, List<Province> provinceList) {
        this.provinceList = provinceList;
        this.layoutInflater = LayoutInflater.from(context);
        this.provinceListFiltered = provinceList;
    }

    @Override
    public ProvinceViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = layoutInflater.inflate(R.layout.item_search, parent, false);
        return new ProvinceViewHolder(view);
    }

    @Override
    public void onBindViewHolder(SearchProvinceAdapter.ProvinceViewHolder holder, int position) {
        Province province = provinceListFiltered.get(position);

        holder.tvTitle.setText(province.getName());
        holder.tvContent.setText(province.getTitle());
    }

    @Override
    public int getItemCount() {
        return provinceListFiltered.size();
    }

    @Override
    public Filter getFilter() {
        return itemFilter;
    }

    public static class ProvinceViewHolder extends RecyclerView.ViewHolder {
        private TextView tvTitle, tvContent;

        public ProvinceViewHolder(View itemView) {
            super(itemView);
            tvTitle = (TextView) itemView.findViewById(R.id.item_search_tv_title);
            tvContent = (TextView) itemView.findViewById(R.id.item_search_tv_content);
        }
    }

    public class ItemFilter extends Filter {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            String filterString = constraint.toString().toLowerCase();
            FilterResults results = new FilterResults();

            int count = provinceList.size();
            final ArrayList<Province> nlist = new ArrayList<Province>(count);

            Province filterableProvince;

            if (TextUtils.isEmpty(filterString)) {
                nlist.addAll(provinceList);
                results.values = nlist;
                results.count = nlist.size();
                return results;
            }

            for (int i = 0; i < count; i++) {
                filterableProvince = provinceList.get(i);
                if (filterableProvince.getName().toLowerCase().contains(filterString)) {
                    nlist.add(filterableProvince);
                }
            }

            results.values = nlist;
            results.count = nlist.size();
            return results;
        }

        @SuppressWarnings("unchecked")
        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            provinceListFiltered = (ArrayList<Province>) results.values;
            notifyDataSetChanged();
        }

    }
}
