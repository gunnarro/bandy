package com.gunnarro.android.bandy.domain.view.list;

/**
 * Class for holding match list items.
 * 
 * @author gunnarro
 * 
 */
public class MatchItem extends Item {

	private String startDate;
	private String teamVsTeam;
	private int numberOfSignedPlayers;

	public MatchItem(Integer id, String value, boolean isEnabled) {
		super(id, value, isEnabled);
	}
}
