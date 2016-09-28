package com.agilepro.commons.models.customer;

import com.yukthi.validation.annotations.Required;
import com.yukthi.webutils.common.annotations.Model;
import com.yukthi.webutils.common.annotations.NonDisplayable;

@Model(name = "ProjectRelease")
public class ProjectReleaseModel
{
	/**
	 * The id.
	 **/
	@NonDisplayable
	private Long id;

	private Long projectId;

	@Required
	private Long releaseId;

	/**
	 * Version used for update.
	 **/
	@NonDisplayable
	private Integer version;

	public Long getId()
	{
		return id;
	}

	public void setId(Long id)
	{
		this.id = id;
	}

	public Long getProjectId()
	{
		return projectId;
	}

	public void setProjectId(Long projectId)
	{
		this.projectId = projectId;
	}

	public Integer getVersion()
	{
		return version;
	}

	public void setVersion(Integer version)
	{
		this.version = version;
	}

	public Long getReleaseId()
	{
		return releaseId;
	}

	public void setReleaseId(Long releaseId)
	{
		this.releaseId = releaseId;
	}
}
