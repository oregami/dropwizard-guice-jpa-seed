package org.oregami.entities;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.hibernate.annotations.NamedQueries;
import org.hibernate.annotations.NamedQuery;
import org.hibernate.envers.Audited;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity
@Audited
@NamedQueries({@NamedQuery(name="Language.GetAll", query = "from Language l")})
public class Language extends BaseEntityUUID {

    private static final long serialVersionUID = 1;

    private String name;

	private String description;

	public Language(String name) {
		this.setName(name);
	}

	public Language() {
	}
	
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}


    public static final String GERMAN = "GERMAN";
    public static final String MANDARIN = "MANDARIN";
    public static final String CHINESE = "CHINESE";
    public static final String SPANISH = "SPANISH";
    public static final String ENGLISH = "ENGLISH";
    public static final String HINDI = "HINDI";
    public static final String ARABIC = "ARABIC";
    public static final String PORTUGUESE = "PORTUGUESE";
    public static final String BENGALI = "BENGALI";
    public static final String RUSSIAN = "RUSSIAN";
    public static final String JAPANESE = "JAPANESE";
    public static final String PUNJABI = "PUNJABI";
    public static final String KOREAN = "KOREAN";
    public static final String FRENCH = "FRENCH";
    public static final String PERSIAN = "PERSIAN";
    public static final String TURKISH = "TURKISH";
    public static final String ITALIAN = "ITALIAN";
    public static final String CANTONESE = "CANTONESE";
    public static final String POLISH = "POLISH";
    public static final String DUTCH = "DUTCH";
    public static final String GREEK = "GREEK";
}
