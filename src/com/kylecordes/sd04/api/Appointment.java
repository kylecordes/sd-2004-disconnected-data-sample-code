/* Example code for SD 2004 talk:
 * Disconnected Data Handling in Mobile / Wireless Applications
 * Copyright 2004 Kyle Cordes
 * Oasis Digital Solutions Inc.
 * http://kylecordes.com
 * http://oasisdigital.com
 */

package com.kylecordes.sd04.api;

import java.io.Serializable;
import java.util.Date;

public class Appointment implements Serializable {
	private int id;
	private String worker;
	private String site;
	private String status;
	private Date apptDate;
	private Date modifiedDate;
	private boolean dirty;
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getSite() {
		return site;
	}
	public void setSite(String site) {
		this.site = site;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
    public Date getApptDate() {
        return apptDate;
    }
    public void setApptDate(Date apptDate) {
        this.apptDate = apptDate;
    }
	public String getWorker() {
		return worker;
	}
	public void setWorker(String worker) {
		this.worker = worker;
	}
    public Date getModifiedDate() {
        return modifiedDate;
    }
    public void setModifiedDate(Date modifiedDate) {
        this.modifiedDate = modifiedDate;
    }
	
	public void makeDirty() {
	    dirty = true;
	}
	
	public void makeClean() {
	    dirty = false;
	}
    public boolean isDirty() {
        return dirty;
    }
}
