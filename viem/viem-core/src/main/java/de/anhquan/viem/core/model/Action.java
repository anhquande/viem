package de.anhquan.viem.core.model;

public enum Action {

	INVALID_ACTION("INVALID_ACTION"),
	
	// Command for Entity
	SHOW_ALL ("showAll"),
	CLEAR_ALL ("clearAll"),
	SORT("sort"),
	DELETE("delete", true),
	UPDATE("update", true),
	DUPLICATE("duplicate", true),
	TOGGLE_VISIBILITY("toggleVisibility", true), 
	VIEW("view",true),
	EXPORT2JSON("export2json"), 
	CREATE("create"),
	
	// Command for Option
	ADD_OPTION("addOption",true),
	DELETE_OPTION("deleteOption",true),
	UPDATE_OPTION("updateOption",true),
	SORT_OPTION("sortOption"),
	DUPLICATE_OPTION("duplicateOption",true),
	
	// Command for OptionItem
	ADD_OPTION_ITEM("addOptionItem",true),
	DUPLICATE_OPTION_ITEM("duplicateOptionItem",true),
	UPDATE_OPTION_ITEM("updateOptionItem",true),
	DELETE_OPTION_ITEM("deleteOptionItem",true),
	SORT_OPTION_ITEM("sortOptionItem"),
	
	// Command for Settings
	SET_KEY ("setKey", true),
	GET_KEY ("getKey", true),
	RESTORE_KEY ("restoreKey", true),
	RESET_ALL_KEYS ("resetAllKeys"),

	//Command for category
	SAVE_SELECTED_PRODUCTS ("saveSelectedProducts", true),
	
	//INIT
	INIT("init"),
	RESET("reset"), 
	RESET_ALL("resetAll");
	
	private String value;
	private boolean requireId;
	
	private Action(String value){
		this(value, false);
	}

	private Action(String value, boolean requireId){
		this.value = value;
		this.requireId = requireId;
	}

	public boolean requireId(){
		return requireId;
	}
	
	@Override
    public String toString() {
		return this.value;
	}
	
	public String getValue() {
		return this.value;
	}
	
	public static Action parse(String str){
		if ((str==null) || str.isEmpty())
			return INVALID_ACTION;
		
		Action[] allActions = Action.class.getEnumConstants();
		for (Action action : allActions) {
			if (str.compareToIgnoreCase(action.getValue())==0)
				return action;
		}
		
		return INVALID_ACTION;
	}
}
