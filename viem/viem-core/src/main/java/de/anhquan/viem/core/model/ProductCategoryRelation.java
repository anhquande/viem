package de.anhquan.viem.core.model;

import org.json.simple.JSONObject;

import com.googlecode.objectify.Ref;
import com.googlecode.objectify.annotation.Cache;
import com.googlecode.objectify.annotation.Entity;
import com.googlecode.objectify.annotation.Index;
import com.googlecode.objectify.annotation.Load;

import de.anhquan.viem.core.model.BaseEntity;

@Cache
@Entity
public class ProductCategoryRelation extends BaseEntity {

	private static final long serialVersionUID = 1L;
	
	@Index
	@Load
	private Ref<Product> product;
	
	@Index
	@Load
	private Ref<Category> category;
	
	public Product getProduct() {
		if (product==null)
			return null;
		return product.get();
	}

	public void setProduct(Product entity) {
		if (entity==null)
			this.product = null;
		else
			this.product = Ref.create(entity);
	}

	public Category getCategory() {
		if (category == null)
			return null;
		else
			return category.get();
	}

	public void setCategory(Category entity) {
		if (entity==null)
			this.category = null;
		else
			this.category = Ref.create(entity);
	}

	@Override
	public String toJSON() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public JSONObject toJSONObject() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void fromJSON(JSONObject item) {
		// TODO Auto-generated method stub
		
	}

}
