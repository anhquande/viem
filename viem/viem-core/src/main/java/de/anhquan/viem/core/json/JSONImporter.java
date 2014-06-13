package de.anhquan.viem.core.json;

import java.util.List;

import de.anhquan.viem.core.model.BaseEntity;

public interface JSONImporter<T extends BaseEntity> {
	public List<T> parse(String strJSON) throws ParseJSONException;
}
