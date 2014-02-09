package com.gunnarro.android.bandy.view.expandablelist;

import android.app.Activity;
import android.util.SparseArray;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CheckedTextView;
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
		super.getChildView(groupPosition, childPosition, isLastChild, convertView, parentView);
		final Group group = groups.get(groupPosition);
		final Item childItem = (Item) getChild(groupPosition, childPosition);
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
						boolean registreredPlayer = bandyService.registrerOnTraining(childItem.getId(), group.getId());
						// updateGroupInfo(groupPosition, registreredPlayer);
					} else {
						boolean registreredPlayer = bandyService.registrerOnTraining(childItem.getId(), group.getId());
						// updateGroupInfo(groupPosition, registreredPlayer);
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

	@Override
	public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
		if (convertView == null) {
			convertView = inflater.inflate(R.layout.list_row_group_extended, null);
		}
		Group group = (Group) getGroup(groupPosition);
		setGroupInfo(group, convertView);
		return convertView;
	}

	private void setGroupInfo(Group group, View convertView) {
		((CheckedTextView) convertView.findViewById(R.id.rowGroupId)).setText(group.getHeader());
		((TextView) convertView.findViewById(R.id.groupSubHeader1TxtId)).setText(group.getSubHeader1());
		((TextView) convertView.findViewById(R.id.groupSubHeader2TxtId)).setText(group.getSubHeader2());
	}
}
