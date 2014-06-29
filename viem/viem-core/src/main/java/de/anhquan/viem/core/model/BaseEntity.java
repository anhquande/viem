package de.anhquan.viem.core.model;

import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import com.googlecode.objectify.Ref;
import com.googlecode.objectify.annotation.Id;

import de.anhquan.viem.core.annotation.IgnoreJson;
import de.anhquan.viem.core.dto.Dto;
import de.anhquan.viem.core.util.Parser;

@SuppressWarnings("serial")
public abstract class BaseEntity implements Dto, JSONAble {
	
	@IgnoreJson
	@Id
    protected Long id;

    protected BaseEntity() {
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((id == null) ? 0 : id.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }

        if (obj == null) {
            return false;
        }

        if (getClass() != obj.getClass()) {
            return false;
        }

        BaseEntity other = (BaseEntity) obj;

        if (id == null) {
            if (other.id != null) {
                return false;
            }
        } else if (!id.equals(other.id)) {
            return false;
        }

        return true;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public boolean isSaved() {
        return (id != null);
    }
    
    /**
     * Change value of an object property
     * @param fieldName
     * @param value
     * @return Return true if success, otherwise false
     * 
     */
    public boolean setValue(String fieldName, Object value){
    	Class<?> myCls = this.getClass();
		List<Field> fields = getFields(myCls);
		for (Field field : fields) {
			if (field.getName().compareTo(fieldName)!=0)
				continue;
			if (Modifier.isStatic(field.getModifiers()))
				continue;
			try {
				field.setAccessible(true);
				field.set(this, value);
				return true;
			} catch (IllegalAccessException e) {
				e.printStackTrace();
			}
		}
		
		return false;
    }
    
    public boolean fieldExists(String fieldName) {
    	Class<?> myCls = this.getClass();
		List<Field> fields = getFields(myCls);		
		for (Field field : fields) {
			if (field.getName().compareTo(fieldName)==0)
				return true;
		}
		
		return false;
    }
    
    /**
     * Get value of an object property
     * 
     * @param fieldName
     * @return
     * @throws IllegalArgumentException
     */
    public Object getValue(String fieldName) {
    	Class<?> myCls = this.getClass();
		List<Field> fields = getFields(myCls);
		for (Field field : fields) {
			if (field.getName().compareTo(fieldName)!=0)
				continue;
			if (Modifier.isStatic(field.getModifiers()))
				continue;
			
			try {
				field.setAccessible(true);
				return field.get(this);
			} catch (IllegalAccessException e) {
				e.printStackTrace();
			}
		}
		
		return null;
    }
    
    @Override
    public void fromJSON(JSONObject json) {
    	if (json==null)
    		return;
    	
		Class<?> myCls = this.getClass();
		List<Field> fields = getFields(myCls);
		for (Field field : fields) {
			if (field.getAnnotation(IgnoreJson.class)!=null)
				continue;
			
			if (Modifier.isStatic(field.getModifiers()))
				continue;
			
			field.setAccessible(true);
			try {
				String fieldName = field.getName();
				Class<?> fieldType = field.getType();
				Object value = json.get(fieldName);
				if (value!=null) {
					if (fieldType == String.class) {
						field.set(this, Parser.parseString(value));
					}
					else if ((fieldType == Integer.class) || (fieldType == int.class)) {
						field.set(this, Parser.parseInt(value));
					} 
					else if ((fieldType == Long.class) || (fieldType == long.class)) {
						field.set(this, Parser.parseLong(value));
					} 
					else if ((fieldType == Boolean.class) || (fieldType == boolean.class)) {
						field.set(this, Parser.parseBoolean(value));
					}
					else if ((fieldType == List.class) || (fieldType == Array.class)){
						//ignore 
					}
					else
						field.set(this, value);
				}
			} catch (IllegalArgumentException e) {
				e.printStackTrace();
			} catch (IllegalAccessException e) {
				e.printStackTrace();
			}
		} // end for
    }
    
    @Override
	public JSONObject toJSONObject(){
    	JSONObject json = new JSONObject();
		Class<?> myCls = this.getClass();
		List<Field> fields = getFields(myCls);
		for (Field field : fields) {
			if ((field.getAnnotation(IgnoreJson.class)!=null) || Modifier.isStatic(field.getModifiers())){
				continue;
			}
			
			field.setAccessible(true);
			try {
				Object value = field.get(this);
				Class<?> fieldType = field.getType();
				String fieldName = field.getName();

				if (fieldType.isAssignableFrom(JSONAble.class)) {
					if (value == null)
						json.put(fieldName, null);
					else
						json.put(fieldName, ((JSONAble)value).toJSONObject());
				}
				else if (fieldType.isAssignableFrom(Ref.class)) {
					if (value == null)
						json.put(fieldName, null);
					else{
						Object obj = ((Ref)value).get();
						if (obj==null)
							json.put(fieldName, null );
						else{
							if (obj instanceof BaseEntity){
								BaseEntity entity = (BaseEntity)obj;
								json.put(fieldName, entity.toJSONObject());
							}
							else
								json.put(fieldName, obj );
						}
							
					}
						
				}
				else if (fieldType == List.class) {
					List list = (List)value;
					JSONArray jsonList = new JSONArray();
					if (list!=null){
						for (Object object : list) {
							if (object!=null){
								if (object instanceof JSONAble){
									jsonList.add(((JSONAble)object).toJSONObject());
								}
								else
									jsonList.add(object);
							}
						}
					}
				}
				else
					json.put(fieldName, value);
			} catch (IllegalArgumentException e) {
				e.printStackTrace();
			} catch (IllegalAccessException e) {
				e.printStackTrace();
			}
		}
		return json;
	}

    public void copyFrom(BaseEntity other) {
    	if (other==null)
    		return;
    	
		Class<?> myCls = this.getClass();
		List<Field> fields = getFields(myCls);
		for (Field field : fields) {
			if (!Modifier.isStatic(field.getModifiers())){
				field.setAccessible(true);
				try {
					if ("id".compareTo(field.getName())!=0){
						Object value = field.get(other);
						field.set(this, value);
					}
				} catch (IllegalArgumentException e) {
					e.printStackTrace();
				} catch (IllegalAccessException e) {
					e.printStackTrace();
				}
			}
		}
    }
    
    public void copyFrom(HttpServletRequest request) {
		Class<?> myCls = this.getClass();
		List<Field> fields = getFields(myCls);
		for (Field field : fields) {
			if (!Modifier.isStatic(field.getModifiers())){
				field.setAccessible(true);
				try {
					Class<?> fieldType = field.getType();
					Object value = request.getParameter(field.getName());
					if (value!=null) {
						if (fieldType == String.class) {
							field.set(this, Parser.parseString(value));
						}
						else if ((fieldType == Integer.class) || (fieldType == int.class)) {
							field.set(this, Parser.parseInt(value));
						} 
						else if ((fieldType == Long.class) || (fieldType == long.class)) {
							field.set(this, Parser.parseLong(value));
						} 
						else if ((fieldType == Boolean.class) || (fieldType == boolean.class)) {
							field.set(this, Parser.parseBoolean(value));
						}
						else if ((fieldType == List.class) || (fieldType == Array.class)){
							//ignore. skip it
						}
						else
							field.set(this, value);
					}
				} catch (IllegalArgumentException e) {
					e.printStackTrace();
				} catch (IllegalAccessException e) {
					e.printStackTrace();
				}
			}
		}
    }
  
    @Override
	public String toJSON(){
		return toJSONObject().toJSONString();
	}
	
	public String toString(){
		return ToStringBuilder.reflectionToString(this, ToStringStyle.MULTI_LINE_STYLE);
	}
	
	private List<Field> getFields(Class<?> clazz){
		if (clazz == null)
			return new ArrayList<Field>();
		
		List<Field> fields = new ArrayList<Field>();		
		fields.addAll(Arrays.asList(clazz.getDeclaredFields()));
		fields.addAll(getFields(clazz.getSuperclass()));
		return fields;
	}
    
}
