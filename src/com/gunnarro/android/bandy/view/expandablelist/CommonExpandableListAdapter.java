package com.gunnarro.android.bandy.view.expandablelist;

import android.app.Activity;
import android.database.DataSetObserver;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.CheckedTextView;
import android.widget.TextView;

import com.gunnarro.android.bandy.R;
import com.gunnarro.android.bandy.custom.CustomLog;
import com.gunnarro.android.bandy.domain.activity.Activity.ActivityStatusEnum;
import com.gunnarro.android.bandy.domain.view.list.Group;
import com.gunnarro.android.bandy.service.BandyService;

public class CommonExpandableListAdapter extends BaseExpandableListAdapter {
	public LayoutInflater inflater;
	public Activity activity;
	protected final SparseArray<Group> groups;
	protected BandyService bandyService;
	private int minNumberOfSelectedChildren = 0;

	public CommonExpandableListAdapter(Activity activity, SparseArray<Group> groups, BandyService bandyService, int minNumberOfSelectedChildren) {
		this.activity = activity;
		this.groups = groups;
		this.bandyService = bandyService;
		this.inflater = activity.getLayoutInflater();
		this.minNumberOfSelectedChildren = minNumberOfSelectedChildren;
	}

	@Override
	public void registerDataSetObserver(DataSetObserver observer) {
		super.registerDataSetObserver(observer);
	}

	@Override
	public void unregisterDataSetObserver(DataSetObserver observer) {
		super.unregisterDataSetObserver(observer);
	}

	@Override
	public void notifyDataSetChanged() {
		super.notifyDataSetChanged();
		// Refresh the list data here
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Object getChild(int groupPosition, int childPosition) {
		return groups.get(groupPosition).getChildren().get(childPosition);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public long getChildId(int groupPosition, int childPosition) {
		return 0;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public View getChildView(final int groupPosition, final int childPosition, boolean isLastChild, View convertView, final ViewGroup parentView) {
		return convertView;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public int getChildrenCount(int groupPosition) {
		return groups.get(groupPosition).getChildren().size();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Object getGroup(int groupPosition) {
		return groups.get(groupPosition);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public int getGroupCount() {
		return groups.size();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void onGroupCollapsed(int groupPosition) {
		super.onGroupCollapsed(groupPosition);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void onGroupExpanded(int groupPosition) {
		super.onGroupExpanded(groupPosition);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public long getGroupId(int groupPosition) {
		return 0;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
		return convertView;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean hasStableIds() {
		return false;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean isChildSelectable(int groupPosition, int childPosition) {
		return false;
	}

	protected void setGroupInfo(Group group, View convertView) {
		((CheckedTextView) convertView.findViewById(R.id.rowGroupId)).setText(group.getHeader());
		((TextView) convertView.findViewById(R.id.groupSubHeader1TxtId)).setText(group.getSubHeader1());
		((TextView) convertView.findViewById(R.id.groupSubHeader2TxtId)).setText(group.getSubHeader2());
		if (!group.isEnabled()) {
//			((TableLayout) convertView.findViewById(R.id.expandableTableLayout)).setBackgroundColor(Color.DKGRAY);
			((TextView) convertView.findViewById(R.id.activityCompletedId)).setText(ActivityStatusEnum.COMPLETED.toString());
//			((CheckedTextView) convertView.findViewById(R.id.rowGroupId)).setBackgroundResource(R.drawable.icon_bandy);
//			((TextView) convertView.findViewById(R.id.groupSubHeader1TxtId)).setTextColor(color.holo_green_dark);
//			((CheckedTextView) convertView.findViewById(R.id.rowGroupId)).setBackgroundColor(color.holo_blue_light);
//			convertView.setBackgroundColor(color.holo_orange_light);
//			convertView.setEnabled(false);
//			convertView.setActivated(false);
			CustomLog.e(this.getClass(), "disable: " + group.getHeader());
		}
	}

	protected void updateGroupInfo(int groupPosition, int numberOfSelectedChildren) {
		Group group = groups.get(groupPosition);
		group.setNumberOfSelectedChildren(numberOfSelectedChildren);
	}

	/**
	 * View holder
	 */
	class ListViewHolder {
		public TextView textView;

		public ListViewHolder(View base) {
			// textView = (TextView) base.findViewById(R.id.listTV);
		}
	}

}
