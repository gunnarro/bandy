package com.gunnarro.android.bandy.view.expandablelist;

import android.app.Activity;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.CheckBox;
import android.widget.CheckedTextView;
import android.widget.TextView;

import com.gunnarro.android.bandy.R;
import com.gunnarro.android.bandy.custom.CustomLog;
import com.gunnarro.android.bandy.domain.view.list.Group;
import com.gunnarro.android.bandy.domain.view.list.Item;
import com.gunnarro.android.bandy.service.BandyService;
import com.gunnarro.android.bandy.service.exception.ApplicationException;

public class MyExpandableListAdapter extends BaseExpandableListAdapter {
	private final SparseArray<Group> groups;
	public LayoutInflater inflater;
	public Activity activity;
	private BandyService bandyService;

	public MyExpandableListAdapter(Activity activity, SparseArray<Group> groups, BandyService bandyService) {
		this.activity = activity;
		this.groups = groups;
		this.bandyService = bandyService;
		this.inflater = activity.getLayoutInflater();
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
	public View getChildView(int groupPosition, final int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
		if (convertView == null) {
			convertView = inflater.inflate(R.layout.list_row_details, null);
		}
		final int groupId = groups.get(groupPosition).getId();
		final Item children = (Item) getChild(groupPosition, childPosition);
		if (children == null) {
			throw new ApplicationException("Error getting children: groupPosition=" + groupPosition + ", childPosition=" + childPosition);
		}
		TextView text = (TextView) convertView.findViewById(R.id.rowDetailsTxtId);
		text.setText(children.getValue());
		CheckBox chkBox = (CheckBox) convertView.findViewById(R.id.rowDetailsChkBoxId);
		chkBox.setChecked(children.isEnabled());
		chkBox.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				try {
					CheckBox chkBox = (CheckBox) v.findViewById(R.id.rowDetailsChkBoxId);
					CustomLog.d(this.getClass(), "chkbox cheched=" + chkBox.isChecked() + ", item=" + children.toString() + ", groupId=" + groupId);
					if (chkBox.isChecked()) {
						bandyService.signupForMatch(children.getId(), groupId);
					} else {
						bandyService.unsignForMatch(children.getId(), groupId);
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
				CustomLog.d(this.getClass(), "view selected=" + children.toString());
			}
		});
		return convertView;
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
		if (convertView == null) {
			convertView = inflater.inflate(R.layout.list_row_group, null);
		}
		Group group = (Group) getGroup(groupPosition);
		((CheckedTextView) convertView).setText(group.getName() + " " + group.getInformation());
		((CheckedTextView) convertView).setChecked(isExpanded);
		return convertView;
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
