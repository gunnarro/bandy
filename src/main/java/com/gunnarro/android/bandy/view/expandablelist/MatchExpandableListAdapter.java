package com.gunnarro.android.bandy.view.expandablelist;

import android.app.Activity;
import android.util.SparseArray;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;

import com.gunnarro.android.bandy.R;
import com.gunnarro.android.bandy.custom.CustomLog;
import com.gunnarro.android.bandy.domain.view.list.Group;
import com.gunnarro.android.bandy.domain.view.list.Item;
import com.gunnarro.android.bandy.service.BandyService;
import com.gunnarro.android.bandy.service.exception.ApplicationException;

public class MatchExpandableListAdapter extends CommonExpandableListAdapter {
	// private final SparseArray<Group> groups;
	// public LayoutInflater inflater;
	// public Activity activity;
	// private BandyService bandyService;
	// private int minNumberOfSelectedChildren = 0;

	public MatchExpandableListAdapter(Activity activity, SparseArray<Group> groups, BandyService bandyService, int minNumberOfSelectedChildren) {
		super(activity, groups, bandyService, minNumberOfSelectedChildren);
		// this.activity = activity;
		// this.groups = groups;
		// this.bandyService = bandyService;
		// this.inflater = activity.getLayoutInflater();
		// this.minNumberOfSelectedChildren = minNumberOfSelectedChildren;
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
		if (convertView == null) {
			convertView = inflater.inflate(R.layout.list_row_details, null);
		}
		final Group group = groups.get(groupPosition);
		if (group.isEnabled()) {
			CustomLog.d(this.getClass(), "Group not active: " + group.toString());
			// return convertView;
		}
		final Item childItem = (Item) getChild(groupPosition, childPosition);
		if (childItem == null) {
			throw new ApplicationException("Error getting children: groupPosition=" + groupPosition + ", childPosition=" + childPosition);
		}
		((TextView) convertView.findViewById(R.id.rowDetailsTxtId)).setText(childItem.getValue());
		CheckBox chkBox = (CheckBox) convertView.findViewById(R.id.rowDetailsChkBoxId);
		chkBox.setChecked(childItem.isEnabled());
		chkBox.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View view) {
				try {
					CheckBox chkBox = (CheckBox) view.findViewById(R.id.rowDetailsChkBoxId);
					CustomLog.d(this.getClass(), "chkbox cheched=" + chkBox.isChecked() + ", item=" + childItem.toString() + ", groupId=" + group.getId());
					if (chkBox.isChecked()) {
						int signedPlayer = bandyService.signupForMatch(childItem.getId(), group.getId());
						updateGroupInfo(groupPosition, signedPlayer);
					} else {
						int signedPlayer = bandyService.unsignForMatch(childItem.getId(), group.getId());
						updateGroupInfo(groupPosition, signedPlayer);
					}
					// notifyDataSetChanged();
				} catch (ApplicationException ae) {
					CustomLog.e(this.getClass(), "exception=" + ae.getMessage());
				}
			}
		});
		convertView.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				CustomLog.d(this.getClass(), "view selected=" + childItem.toString());
			}
		});
		return convertView;
	}

	// private void updateGroupInfo(int groupPosition, int
	// numberOfSelectedChildren) {
	// Group group = groups.get(groupPosition);
	// group.setNumberOfSelectedChildren(numberOfSelectedChildren);
	// }

	// @Override
	// public int getChildrenCount(int groupPosition) {
	// return groups.get(groupPosition).getChildren().size();
	// }
	//
	// @Override
	// public Object getGroup(int groupPosition) {
	// return groups.get(groupPosition);
	// }
	//
	// @Override
	// public int getGroupCount() {
	// return groups.size();
	// }
	//
	// @Override
	// public void onGroupCollapsed(int groupPosition) {
	// super.onGroupCollapsed(groupPosition);
	// }
	//
	// @Override
	// public void onGroupExpanded(int groupPosition) {
	// super.onGroupExpanded(groupPosition);
	// }
	//
	// @Override
	// public long getGroupId(int groupPosition) {
	// return 0;
	// }

	@Override
	public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
		if (convertView == null) {
			convertView = inflater.inflate(R.layout.list_row_group_extended, null);
		}
		Group group = (Group) getGroup(groupPosition);
		setGroupInfo(group, convertView);
		// ((CheckedTextView) convertView).setText(group.getHeader());
		// ((CheckedTextView) convertView).setChecked(isExpanded);
		// ((CheckedTextView) convertView).setActivated(group.isEnabled());
		// ((CheckedTextView) convertView).setEnabled(group.isEnabled());
		// if (group.isEnabled()) {
		// if (group.getNumberOfSelectedChildren() <
		// minNumberOfSelectedChildren) {
		// ((CheckedTextView) convertView).setTextColor(Color.RED);
		// } else {
		// ((CheckedTextView) convertView).setTextColor(Color.WHITE);
		// }
		// }
		return convertView;
	}

	// private void setGroupInfo(Group group, View convertView) {
	// ((CheckedTextView)
	// convertView.findViewById(R.id.rowGroupId)).setText(group.getHeader());
	// ((TextView)
	// convertView.findViewById(R.id.groupSubHeader1TxtId)).setText(group.getSubHeader1());
	// ((TextView)
	// convertView.findViewById(R.id.groupSubHeader2TxtId)).setText(group.getSubHeader2());
	// }
	//
	// @Override
	// public boolean hasStableIds() {
	// return false;
	// }
	//
	// @Override
	// public boolean isChildSelectable(int groupPosition, int childPosition) {
	// return false;
	// }

}
