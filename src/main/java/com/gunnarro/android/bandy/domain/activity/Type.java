package com.gunnarro.android.bandy.domain.activity;

public class Type {

	public static enum MatchTypesEnum {
		LEAGUE(1, "LEAGUE"), TRAINING(2, "TRAINING"), CUP(3, "CUP"), TOURNAMENT(4, "TOURNAMENT");

		private String name;
		private int id;

		MatchTypesEnum(int id, String name) {
			this.id = id;
			this.name = name;
		}

		public String getName() {
			return name;
		}

		public int getId() {
			return id;
		}
	}

	private int id;
	private String name;
	private String description;

	public Type(String name) {
		this.name = name;
	}

	public Type(int id, String name, String description) {
		this(name);
		this.id = id;
		this.description = description;
	}

	public int getId() {
		return id;
	}

	public String getName() {
		return name;
	}

	public String getDescription() {
		return description;
	}

}
