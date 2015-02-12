package de.anhquan.viem.core.json;

import de.anhquan.viem.core.model.ConfigEntity;

public class ConfigImporter extends BaseJSONImporter<ConfigEntity>{

	protected ConfigImporter(){
		super(ConfigEntity.class);
	}
}
