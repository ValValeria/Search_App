package org.openjfx.hellofx;

import java.lang.reflect.Field;

import org.json.JSONException;
import org.json.JSONObject;

abstract class BaseResult{
	String created_at;
    String description;
    public String url;
}

public class Result extends BaseResult{
 
    public Result(@SuppressWarnings("exports") JSONObject obj) throws JSONException{
    	this.url = obj.getJSONObject("urls").getString("small");
        Field[]fields=this.getClass().getDeclaredFields();
        
        for(Field field:fields) {
        	String propName = field.getName();
        	String propValue = obj.getString(propName);
        	
        	if(obj.has(propName)){
        		switch(propName) {
        	     	case "created_at":
        	     		this.created_at=propValue;
        	     		break;
        	     	case "description":
        	     		this.description=propValue;
        	     		break;
        		}
        	}
        }
    }
}


/*
 * Chapter 22(end)
 * Process and Runtime
 */