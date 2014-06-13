package de.anhquan.viem.core.model;

import de.anhquan.viem.core.ApplicationError;

public class EntityNotFoundException extends ApplicationException{

	private static final long serialVersionUID = 1L;
	
	public EntityNotFoundException(){
		super(ApplicationError.ENTITY_NOT_FOUND);
	}
		
}
