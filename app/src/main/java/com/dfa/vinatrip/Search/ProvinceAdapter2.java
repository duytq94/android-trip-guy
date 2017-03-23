package com.dfa.vinatrip.Search;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import com.dfa.vinatrip.MainFunction.Province.Province;
import com.dfa.vinatrip.R;

import java.util.ArrayList;
import java.util.List;

public class ProvinceAdapter2 extends RecyclerView.Adapter<ProvinceAdapter2.ProvinceViewHolder> implements Filterable {
    private List<Province> provinceList;
    private LayoutInflater layoutInflater;
    private Context context;

    private ItemFilter mFilter = new ItemFilter();
    private List<Province> provinceFiltered;

    public ProvinceAdapter2(Context context, List<Province> provinceList) {
        this.provinceList = provinceList;
        this.layoutInflater = LayoutInflater.from(context);
        this.context = context;

        this.provinceFiltered = provinceList;
    }

    @Override
    public ProvinceViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = layoutInflater.inflate(R.layout.item_search, parent, false);
        return new ProvinceViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ProvinceAdapter2.ProvinceViewHolder holder, int position) {
        Province province = provinceFiltered.get(position);

        holder.tvTitle.setText(province.getName());
        holder.tvContent.setText(province.getTitle());
    }

    @Override
    public int getItemCount() {
        return provinceFiltered.size();
    }

    @Override
    public Filter getFilter() {
        return mFilter;
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

            final List<Province> list = provinceList;

            int count = list.size();
            final ArrayList<Province> nlist = new ArrayList<Province>(count);

            Province filterableProvince;

            if (TextUtils.isEmpty(filterString)) {
                nlist.addAll(list);
                results.values = nlist;
                results.count = nlist.size();
                return results;
            }

            for (int i = 0; i < count; i++) {
                filterableProvince = list.get(i);
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
            provinceFiltered = (ArrayList<Province>) results.values;
            notifyDataSetChanged();
        }

    }
}
