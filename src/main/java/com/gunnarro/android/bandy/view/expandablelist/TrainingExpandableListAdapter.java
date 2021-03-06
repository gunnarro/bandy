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

public class TrainingExpandableListAdapter extends CommonExpandableListAdapter {

	public TrainingExpandableListAdapter(Activity activity, SparseArray<Group> groups, BandyService bandyService, int minNumberOfSelectedChildren) {
		super(activity, groups, bandyService, minNumberOfSelectedChildren);
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
						bandyService.registrerOnTraining(childItem.getId(), group.getId());
					} else {
						bandyService.unRegistrerTraining(childItem.getId(), group.getId());
					}
					// notifyDataSetChanged();
				} catch (ApplicationException ae) {
					CustomLog.e(this.getClass(), "exception=" + ae.getMessage());
				}
			}
		});
		return convertView;
	}

	@Override
	public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
		if (convertView == null) {
			convertView = inflater.inflate(R.layout.list_row_group_extended, null);
		}
		Group group = (Group) getGroup(groupPosition);
		setGroupInfo(group, convertView);
		if (group.isEnabled()) {
			// parent.setBackgroundColor(Color.YELLOW);
		}
		return convertView;
	}

}
