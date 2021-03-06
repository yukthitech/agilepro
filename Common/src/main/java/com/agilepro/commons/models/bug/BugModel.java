package com.agilepro.commons.models.bug;

import java.util.List;

import com.agilepro.commons.BugStatus;
import com.agilepro.commons.PriorityStatus;
import com.yukthi.validation.annotations.Required;
import com.yukthi.webutils.common.AbstractExtendableModel;
import com.yukthi.webutils.common.FileInfo;
import com.yukthi.webutils.common.annotations.ExtendableModel;
import com.yukthi.webutils.common.annotations.IgnoreField;
import com.yukthi.webutils.common.annotations.LOV;
import com.yukthi.webutils.common.annotations.Label;
import com.yukthi.webutils.common.annotations.Model;
import com.yukthi.webutils.common.annotations.MultilineText;
import com.yukthi.webutils.common.annotations.NonDisplayable;
import com.yukthi.webutils.common.annotations.ReadOnly;

/**
 * The Class BugModel.
 */
@ExtendableModel(name = "Bug")
@Model
public class BugModel extends AbstractExtendableModel
{
	/**
	 * The id.
	 **/
	@NonDisplayable
	private Long id;

	/**
	 * The version.
	 **/
	@NonDisplayable
	private Integer version;

	/**
	 * The name.
	 **/
	@Required
	private String title;

	/**
	 * The reported by.
	 **/

	@LOV(name = "projectMembers")
	@Required
	private Long reportedBy;

	/**
	 * The owner.
	 **/
	@ReadOnly
	@LOV(name = "projectMembers")
	@Required
	private Long owner;

	/**
	 * The project id.
	 **/
	@LOV(name = "projectsLov")
	@Required
	@Label("Project")
	private Long projectId;

	/**
	 * The story.
	 **/
	@LOV(name = "storiesLov")
	@Required
	@Label("Story")
	private Long storyId;

	/**
	 * The sprint.
	 **/
	@LOV(name = "sprintLov")
	@Label("Sprint")
	private Long sprintId;

	/**
	 * The bug status.
	 **/
	private BugStatus status;

	/**
	 * The priority status.
	 **/
	@ReadOnly
	private PriorityStatus priorityStatus;

	/**
	 * Priority.
	 */
	private Integer priority;

	/**
	 * Story points for the bug.
	 */
	private Integer storyPoints;

	/**
	 * The description.
	 **/
	@MultilineText
	private String description;

	/**
	 * The file.
	 **/
	private List<FileInfo> file;
	
	/**
	 * By default true for a bug.
	 */
	@NonDisplayable
	private Boolean isBug = true;
	
	/**
	 * The tasks.
	 */
	@NonDisplayable
	@IgnoreField
	private List<Long> tasks;

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.yukthi.webutils.common.IExtendableModel#getId()
	 */
	public Long getId()
	{
		return id;
	}

	/**
	 * Sets the id.
	 *
	 * @param id
	 *            the new id
	 */
	public void setId(Long id)
	{
		this.id = id;
	}

	/**
	 * Gets the version.
	 *
	 * @return the version
	 */
	public Integer getVersion()
	{
		return version;
	}

	/**
	 * Sets the version.
	 *
	 * @param version
	 *            the new version
	 */
	public void setVersion(Integer version)
	{
		this.version = version;
	}

	/**
	 * Gets the description.
	 *
	 * @return the description
	 */
	public String getDescription()
	{
		return description;
	}

	/**
	 * Sets the description.
	 *
	 * @param description
	 *            the new description
	 */
	public void setDescription(String description)
	{
		this.description = description;
	}

	/**
	 * Gets the reported by.
	 *
	 * @return the reported by
	 */
	public Long getReportedBy()
	{
		return reportedBy;
	}

	/**
	 * Sets the reported by.
	 *
	 * @param reportedBy
	 *            the new reported by
	 */
	public void setReportedBy(Long reportedBy)
	{
		this.reportedBy = reportedBy;
	}

	public BugStatus getStatus()
	{
		return status;
	}

	public void setStatus(BugStatus status)
	{
		this.status = status;
	}

	/**
	 * Gets the title.
	 *
	 * @return the title
	 */
	public String getTitle()
	{
		return title;
	}

	/**
	 * Sets the title.
	 *
	 * @param title
	 *            the new title
	 */
	public void setTitle(String title)
	{
		this.title = title;
	}

	/**
	 * Gets the owner.
	 *
	 * @return the owner
	 */
	public Long getOwner()
	{
		return owner;
	}

	/**
	 * Sets the owner.
	 *
	 * @param owner
	 *            the new owner
	 */
	public void setOwner(Long owner)
	{
		this.owner = owner;
	}

	/**
	 * Gets the project id.
	 *
	 * @return the project id
	 */
	public Long getProjectId()
	{
		return projectId;
	}

	/**
	 * Sets the project id.
	 *
	 * @param projectId
	 *            the new project id
	 */
	public void setProjectId(Long projectId)
	{
		this.projectId = projectId;
	}

	/**
	 * Gets the sprint id.
	 *
	 * @return the sprint id
	 */
	public Long getSprintId()
	{
		return sprintId;
	}

	/**
	 * Sets the sprint id.
	 *
	 * @param sprintId
	 *            the new sprint id
	 */
	public void setSprintId(Long sprintId)
	{
		this.sprintId = sprintId;
	}

	/**
	 * Gets the story id.
	 *
	 * @return the story id
	 */
	public Long getStoryId()
	{
		return storyId;
	}

	/**
	 * Sets the story id.
	 *
	 * @param storyId
	 *            the new story id
	 */
	public void setStoryId(Long storyId)
	{
		this.storyId = storyId;
	}

	/**
	 * Gets the file.
	 *
	 * @return the file
	 */
	public List<FileInfo> getFile()
	{
		return file;
	}

	/**
	 * Sets the file.
	 *
	 * @param file
	 *            the new file
	 */
	public void setFile(List<FileInfo> file)
	{
		this.file = file;
	}

	/**
	 * Get the story points.
	 * 
	 * @return the story points.
	 */
	public Integer getStoryPoints()
	{
		return storyPoints;
	}

	/**
	 * Set the story points.
	 * 
	 * @param storyPoints
	 *            the new story points.
	 */
	public void setStoryPoints(Integer storyPoints)
	{
		this.storyPoints = storyPoints;
	}

	/**
	 * Gets the priority.
	 * 
	 * @return the new priority.
	 */
	public Integer getPriority()
	{
		return priority;
	}

	/**
	 * Sets the priority.
	 * 
	 * @param priority
	 *            the new priority.
	 */
	public void setPriority(Integer priority)
	{
		this.priority = priority;
	}

	/**
	 * Get the priority status.
	 * 
	 * @return the priority status.
	 */
	public PriorityStatus getPriorityStatus()
	{
		return priorityStatus;
	}

	/**
	 * Set the priority status.
	 * 
	 * @param priorityStatus
	 *            the new priority status.
	 */
	public void setPriorityStatus(PriorityStatus priorityStatus)
	{
		this.priorityStatus = priorityStatus;
	}
	
	/**
	 * Gets isBug.
	 * 
	 * @return isBug.
	 */
	public Boolean getIsBug()
	{
		return isBug;
	}

	/**
	 * Set isBug.
	 * 
	 * @param isBug
	 *            the new isBug.
	 */
	public void setIsBug(Boolean isBug)
	{
		this.isBug = isBug;
	}
	
	/**
	 * Gets the tasks.
	 *
	 * @return the tasks
	 */
	public List<Long> getTasks()
	{
		return tasks;
	}

	/**
	 * Sets the tasks.
	 *
	 * @param tasks
	 *            the new tasks
	 */
	public void setTasks(List<Long> tasks)
	{
		this.tasks = tasks;
	}
}
