package com.gunnarro.android.bandy.domain.party;

public class Role {

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
