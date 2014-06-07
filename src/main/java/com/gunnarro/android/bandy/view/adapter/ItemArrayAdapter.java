package com.gunnarro.android.bandy.view.adapter;

import java.util.List;

import android.app.Activity;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.gunnarro.android.bandy.R;
import com.gunnarro.android.bandy.domain.view.list.Item;

public class ItemArrayAdapter extends ArrayAdapter<Item> {
	private final Activity context;
	private final List<Item> items;

	/**
	 * Use view holder in order to reduce number of calls to findViewById(),
	 * which is a rather time consuming function.
	 */
	static class ViewHolder {
		public TextView id;
		public TextView value;
		public TextView isEnabled;
	}

	public ItemArrayAdapter(Activity context, List<Item> items) {
		super(context, R.layout.list_multi_line_layout, items);
		this.context = context;
		this.items = items;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		View rowView = convertView;
		if (items != null && items.size() > 0) {
			Item item = items.get(position);
			if (rowView == null) {
				LayoutInflater inflater = context.getLayoutInflater();
				rowView = inflater.inflate(R.layout.list_single_line_layout, null);
				ViewHolder viewHolder = new ViewHolder();
				viewHolder.id = (TextView) rowView.findViewById(R.id.lineHeaderId);
				if (!item.isEnabled()) {
					viewHolder.id.setTextColor(Color.GRAY);
					viewHolder.value.setTextColor(Color.GRAY);
					viewHolder.isEnabled.setTextColor(Color.GRAY);
				}
				rowView.setTag(viewHolder);
			}

			ViewHolder holder = (ViewHolder) rowView.getTag();
			holder.id.setText(item.getId());
			holder.value.setText(item.getValue());
			holder.isEnabled.setText(item.isEnabled().toString());
		}
		return rowView;
	}
}