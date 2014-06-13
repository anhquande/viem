package de.anhquan.viem.core.model;

import java.util.Date;

import org.json.simple.JSONObject;

import com.googlecode.objectify.annotation.Cache;
import com.googlecode.objectify.annotation.Entity;
import com.googlecode.objectify.annotation.Index;

import de.anhquan.viem.core.util.Parser;

@Entity
@Cache
public class AppUser extends NameBasedEntity{

	private static final long serialVersionUID = 8247569115470286287L;

	private String firstName;
	
	private String lastName;
	
	private Date lastLogin;
	
	private Date firstLogin;
	
	private String nickName;
	
	private String userId;
	
	@Index
	private String facebookUserName;
		
	public Date getLastLogin() {
		return lastLogin;
	}

	public void setLastLogin(Date lastLogin) {
		this.lastLogin = lastLogin;
	}

	public Date getFirstLogin() {
		return firstLogin;
	}

	public void setFirstLogin(Date firstLogin) {
		this.firstLogin = firstLogin;
	}

	public String getFacebookUserName() {
		return facebookUserName;
	}

	public void setFacebookUserName(String facebookUserName) {
		this.facebookUserName = facebookUserName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getNickName() {
		return nickName;
	}

	public void setNickName(String nickName) {
		this.nickName = nickName;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}
		
	@Override
	public void fromJSON(JSONObject json) {
		if (json == null)
			return;
		this.name = Parser.parseString( json.get("email"));
		this.firstName = Parser.parseString( json.get("firstName"));
		this.lastName = Parser.parseString( json.get("lastName"));
		this.firstLogin = Parser.parseDate( json.get("firstLogin"));
		this.lastLogin = Parser.parseDate( json.get("lastLogin"));
		this.facebookUserName = Parser.parseString( json.get("facebookUserName"));
		this.nickName = Parser.parseString( json.get("nickName"));
		this.userId = Parser.parseString( json.get("userId"));

	}
	
	@SuppressWarnings("unchecked")
	@Override
	public JSONObject toJSONObject(){
		JSONObject json = new JSONObject();
		json.put("email", this.name);
		json.put("firstName", this.firstName);
		json.put("lastName", this.lastName);
		json.put("firstLogin", this.firstLogin);
		json.put("lastLogin", this.lastLogin);
		json.put("facebookUserName", this.facebookUserName);
		json.put("nickName", this.nickName);
		json.put("userId", this.userId);

		return json;
		
	}

}
