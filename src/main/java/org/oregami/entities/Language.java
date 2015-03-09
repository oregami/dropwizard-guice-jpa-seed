package org.oregami.entities;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import org.hibernate.annotations.NamedQueries;
import org.hibernate.annotations.NamedQuery;
import org.hibernate.envers.Audited;
import org.joda.time.LocalDateTime;
import org.oregami.data.CustomLocalDateTimeSerializer;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity
@Audited
@NamedQueries({@NamedQuery(name="Language.GetAll", query = "from Language l")})
@JsonIgnoreProperties(ignoreUnknown = true)
public class Language extends BaseEntityUUID implements TopLevelEntity {

    private static final long serialVersionUID = 1;

    private String name;

	private String description;

    private LocalDateTime changeTime = null;

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

    public LocalDateTime getChangeTime() {
        return changeTime;
    }

    public void setChangeTime(LocalDateTime changeTime) {
        this.changeTime = changeTime;
    }

    @JsonSerialize(using = CustomLocalDateTimeSerializer.class)
    public LocalDateTime getChangeTimeGui() {
        return changeTime;
    }

    @Override
    public Discriminator getDiscriminator() {
        return Discriminator.LANGUAGE;
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
