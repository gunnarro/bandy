package com.gunnarro.android.bandy.view.expandablelist;

import android.app.Activity;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.CheckedTextView;
import android.widget.TextView;

import com.gunnarro.android.bandy.R;
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
	public Object getChild(int groupPosition, int childPosition) {
		return groups.get(groupPosition).getChildren().get(childPosition);
	}

	@Override
	public long getChildId(int groupPosition, int childPosition) {
		return 0;
	}

	@Override
	public View getChildView(final int groupPosition, final int childPosition, boolean isLastChild, View convertView, final ViewGroup parentView) {
		return convertView;
	}

	protected void updateGroupInfo(int groupPosition, int numberOfSelectedChildren) {
		Group group = groups.get(groupPosition);
		group.setNumberOfSelectedChildren(numberOfSelectedChildren);
	}

	@Override
	public int getChildrenCount(int groupPosition) {
		return groups.get(groupPosition).getChildren().size();
	}

	@Override
	public Object getGroup(int groupPosition) {
		return groups.get(groupPosition);
	}

	@Override
	public int getGroupCount() {
		return groups.size();
	}

	@Override
	public void onGroupCollapsed(int groupPosition) {
		super.onGroupCollapsed(groupPosition);
	}

	@Override
	public void onGroupExpanded(int groupPosition) {
		super.onGroupExpanded(groupPosition);
	}

	@Override
	public long getGroupId(int groupPosition) {
		return 0;
	}

	@Override
	public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
		return convertView;
	}

	protected void setGroupInfo(Group group, View convertView) {
		((CheckedTextView) convertView.findViewById(R.id.rowGroupId)).setText(group.getHeader());
		((TextView) convertView.findViewById(R.id.groupSubHeader1TxtId)).setText(group.getSubHeader1());
		((TextView) convertView.findViewById(R.id.groupSubHeader2TxtId)).setText(group.getSubHeader2());
	}

	@Override
	public boolean hasStableIds() {
		return false;
	}

	@Override
	public boolean isChildSelectable(int groupPosition, int childPosition) {
		return false;
	}

}
