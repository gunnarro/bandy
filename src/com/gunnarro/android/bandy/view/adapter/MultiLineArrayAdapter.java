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
import com.gunnarro.android.bandy.domain.view.list.MultiLineItem;

public class MultiLineArrayAdapter extends ArrayAdapter<MultiLineItem> {
	private final Activity context;
	private final List<MultiLineItem> items;

	/**
	 * Use view holder in order to reduce number of calls to findViewById(),
	 * which is a rather time consuming function.
	 */
	static class ViewHolder {
		public TextView header;
		public TextView subHeader1;
		public TextView subHeader2;
	}

	public MultiLineArrayAdapter(Activity context, List<MultiLineItem> items) {
		super(context, R.layout.list_line_layout, items);
		this.context = context;
		this.items = items;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		View rowView = convertView;
		MultiLineItem item = items.get(position);
		if (rowView == null) {
			LayoutInflater inflater = context.getLayoutInflater();
			rowView = inflater.inflate(R.layout.list_line_layout, null);
			ViewHolder viewHolder = new ViewHolder();
			viewHolder.header = (TextView) rowView.findViewById(R.id.lineHeaderId);
			viewHolder.subHeader1 = (TextView) rowView.findViewById(R.id.lineSubHeader1Id);
			viewHolder.subHeader2 = (TextView) rowView.findViewById(R.id.lineSubHeader2Id);
			if (!item.isEnabled()) {
				viewHolder.header.setTextColor(Color.GRAY);
				viewHolder.subHeader1.setTextColor(Color.GRAY);
				viewHolder.subHeader2.setTextColor(Color.GRAY);
			}
			rowView.setTag(viewHolder);
		}

		ViewHolder holder = (ViewHolder) rowView.getTag();
		holder.header.setText(item.getValue());
		holder.subHeader1.setText(item.getSubHeader1());
		if (!item.isEnabled()) {
			holder.subHeader2.setText(item.getSubHeader2());
		}
		return rowView;
	}
}