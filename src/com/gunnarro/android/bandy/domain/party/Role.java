package com.gunnarro.android.bandy.domain.party;

public class Role {

	public enum RoleTypesEnum {
		DEFAULT(1), PARENT(2), TEAMLEAD(3), COACH(4), CHAIRMAN(5), DEPUTY_CHAIRMAN(6), BOARD_MEMBER(7);
		int id;

		RoleTypesEnum(int id) {
			this.id = id;
		}

		public int getId() {
			return id;
		}
	}

	private int id;
	private int conctactId;
	private String role;

	public Role(int id, int conctactId, String role) {
		this.id = id;
		this.conctactId = conctactId;
		this.role = role;
	}

	public int getId() {
		return id;
	}

	public int getConctactId() {
		return conctactId;
	}

	public String getRole() {
		return role;
	}

	@Override
	public String toString() {
		return "Role [id=" + id + ", conctactId=" + conctactId + ", role=" + role + "]";
	}

}
