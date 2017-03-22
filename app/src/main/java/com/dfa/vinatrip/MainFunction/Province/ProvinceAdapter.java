package com.dfa.vinatrip.MainFunction.Province;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;

import com.dfa.vinatrip.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

// Adapter RecyclerView for Province in ProvinceFragment
public class ProvinceAdapter extends RecyclerView.Adapter<ProvinceAdapter.ProvinceViewHolder> implements Filterable {
    private List<Province> provinceList;
    private LayoutInflater layoutInflater;
    private Context context;

    private ItemFilter mFilter = new ItemFilter();
    private List<Province> provinceFiltered;

    public ProvinceAdapter(Context context, List<Province> provinceList) {
        this.provinceList = provinceList;
        this.layoutInflater = LayoutInflater.from(context);
        this.context = context;

        this.provinceFiltered = provinceList;
    }

    @Override
    public ProvinceViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = layoutInflater.inflate(R.layout.item_province, parent, false);
        return new ProvinceViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ProvinceAdapter.ProvinceViewHolder holder, int position) {
        Province province = provinceFiltered.get(position);

        //bind data to viewholder
        holder.tvName.setText(province.getName());
        holder.tvTitle.setText(province.getTitle());
        // all photo will the same scale
        holder.ivAvatar.setScaleType(ImageView.ScaleType.FIT_XY);
        Picasso.with(context).load(province.getAvatar())
                .placeholder(R.drawable.ic_loading)
                .error(R.drawable.photo_not_available)
                .into(holder.ivAvatar);
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
        private TextView tvName, tvTitle;
        private ImageView ivAvatar;

        public ProvinceViewHolder(View itemView) {
            super(itemView);
            tvName = (TextView) itemView.findViewById(R.id.item_province_tv_name);
            tvTitle = (TextView) itemView.findViewById(R.id.item_province_tv_title);
            ivAvatar = (ImageView) itemView.findViewById(R.id.item_province_iv_avatar);
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
