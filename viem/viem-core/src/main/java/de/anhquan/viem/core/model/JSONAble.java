package de.anhquan.viem.core.model;

import org.json.simple.JSONObject;

public interface JSONAble {
	public String toJSON();
	public JSONObject toJSONObject();
	public void fromJSON(JSONObject item);
}
