package com.six.domain;

public class Score {

	private Integer id;
	private Integer userId;
	private Integer score;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Integer getUserId() {
		return userId;
	}

	public void setUserId(Integer userId) {
		this.userId = userId;
	}

	public Integer getScore() {
		return score;
	}

	public void setScore(Integer score) {
		this.score = score;
	}

	@Override
	public String toString() {
		return "Score [id=" + id + ", userId=" + userId + ", score=" + score
				+ "]";
	}

}
