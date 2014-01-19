package com.gunnarro.android.bandy.view.development;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.gunnarro.android.bandy.R;
import com.gunnarro.android.bandy.service.impl.BandyServiceImpl;
import com.gunnarro.android.bandy.view.development.CommonListFragment.ListTypeEnum;

public class BlackListFragment extends CommonListFragment {

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		super.setType(ListTypeEnum.PLAYERS.name());
		super.setAddBtnId(R.id.black_list_add_btn);
		super.setDelBtnId(R.id.black_list_del_btn);
		super.setRefreshBtnId(R.id.black_list_refresh_btn);
		super.setInputFieldId(R.id.black_list_input_field);
		super.setBandyService(new BandyServiceImpl(getActivity()));
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		super.onCreateView(inflater, container, savedInstanceState);
		// Inflate the layout for this fragment
		if (container == null) {
			return null;
		}
		View view = inflater.inflate(R.layout.black_list_layout, container, false);
		super.setupEventHandlers(view);
		return view;
	}
}
