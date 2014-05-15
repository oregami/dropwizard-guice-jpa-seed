/*******************************************************************************
 * Copyright (C) 2012  Oregami.org, Germany http://www.oregami.org
 * 
 * 	This program is free software: you can redistribute it and/or modify
 * 	it under the terms of version 3 or any later version of the
 * 	GNU Affero General Public License as published by the Free Software 
 * 	Foundation.
 * 	
 * 	This program is distributed in the hope that it will be useful,
 * 	but WITHOUT ANY WARRANTY; without even the implied warranty of
 * 	MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * 	GNU Affero General Public License for more details.	
 * 	
 * 	You should have received a copy of the GNU Affero General Public License
 * 	along with this program. If not, see <http://www.gnu.org/licenses/>.
 ******************************************************************************/
package org.oregami.entities;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;
import javax.persistence.Version;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;
import org.hibernate.annotations.GenericGenerator;



@MappedSuperclass
public abstract class BaseEntityUUID implements Serializable
{
    /**
	 * 
	 */
	private static final long serialVersionUID = 8608953068007538072L;

	@Id @GeneratedValue(generator="system-uuid")
	@GenericGenerator(name="system-uuid",
	  strategy = "uuid")
    @Column(name = "id", updatable = false, nullable = false)
    private String id = null;
 
    @Version
    @Column(name = "version")
    private int version = 0;
 
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "veraenderung_zeitpunkt")
    @Transient
    private Date lastUpdate;
 
    protected void copy(final BaseEntityUUID source)
    {
        this.id = source.id;
        this.version = source.version;
        this.lastUpdate = source.lastUpdate;
    }
 
    @Override
    public boolean equals(final Object obj)
    {
        if (this == obj)
        {
            return true;
        }
        if (obj == null)
        {
            return false;
        }
        if (!(obj instanceof BaseEntityUUID))
        {
            return false;
        }
        final BaseEntityUUID other = (BaseEntityUUID) obj;
        if (this.id != null && other.id != null)
        {
            if (this.getClass().equals(other.getClass()) && this.id == other.id)
            {
                return true;
            }
        }
        return false;
    }
 
    public String getId()
    {
        return this.id;
    }
 
    @Deprecated
    public void setId(final String id)
    {
        this.id = id;
    }
 
    public int getVersion()
    {
        return this.version;
    }
 
    @SuppressWarnings("unused")
    private void setVersion(final int version)
    {
        this.version = version;
    }
 
    public Date getLastUpdate()
    {
        return this.lastUpdate;
    }
 
    public void setLastUpdate(final Date lastUpdate)
    {
        this.lastUpdate = lastUpdate;
    }
    
	@Override
	public String toString() {
		return ToStringBuilder.reflectionToString(this, ToStringStyle.MULTI_LINE_STYLE);
	}
}
