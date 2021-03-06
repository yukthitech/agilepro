package com.agilepro.persistence.entity.bug;

import javax.persistence.Column;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.agilepro.commons.BugStatus;
import com.agilepro.commons.PriorityStatus;
import com.agilepro.commons.models.bug.BugModel;
import com.agilepro.persistence.entity.admin.EmployeeEntity;
import com.agilepro.persistence.entity.admin.ProjectEntity;
import com.agilepro.persistence.entity.project.SprintEntity;
import com.agilepro.persistence.entity.project.StoryEntity;
import com.yukthi.persistence.annotations.DataType;
import com.yukthi.persistence.annotations.DataTypeMapping;
import com.yukthi.persistence.annotations.UniqueConstraint;
import com.yukthi.persistence.annotations.UniqueConstraints;
import com.yukthi.utils.annotations.PropertyMapping;
import com.yukthi.webutils.annotations.ExtendableEntity;
import com.yukthi.webutils.repository.WebutilsExtendableEntity;

/**
 * The Class BugEntity.
 */

@ExtendableEntity(name = "Bug")
@Table(name = "BUG")
@UniqueConstraints({ @UniqueConstraint(name = "SPACE_PROJECT_TITLE", fields = { "spaceIdentity", "project", "title" }), @UniqueConstraint(name = "SPACE_PROJECT_PRIORITY", fields = { "spaceIdentity", "project", "priority" }) })
public class BugEntity extends WebutilsExtendableEntity
{
	/**
	 * The name.
	 **/
	@Column(name = "TITLE", nullable = false)
	private String title;

	/**
	 * The description.
	 **/
	@Column(name = "DESCRIPTION")
	private String description;

	/**
	 * The reported by.
	 **/
	@ManyToOne
	@PropertyMapping(type = BugModel.class, from = "reportedBy", subproperty = "id")
	@Column(name = "BUG_REPORTED_BY", nullable = false)
	private EmployeeEntity reportedBy;

	/**
	 * The bug status.
	 **/
	@Column(name = "STATUS")
	@DataTypeMapping(type = DataType.STRING)
	private BugStatus status;

	/**
	 * The owner.
	 **/
	@ManyToOne
	@PropertyMapping(type = BugModel.class, from = "owner", subproperty = "id")
	@Column(name = "BUG_OWNER_ID", nullable = false)
	private EmployeeEntity owner;

	/**
	 * priority status.
	 **/
	@DataTypeMapping(type = DataType.STRING)
	private PriorityStatus priorityStatus;

	/**
	 * The project.
	 **/
	@ManyToOne
	@PropertyMapping(type = BugModel.class, from = "projectId", subproperty = "id")
	@Column(name = "PROJECT_ID", nullable = false)
	private ProjectEntity project;

	/**
	 * The story entity.
	 **/
	@Column(name = "STORY_ID", nullable = false)
	@ManyToOne
	@PropertyMapping(type = BugModel.class, from = "storyId", subproperty = "id")
	private StoryEntity story;

	/**
	 * to set sprint target.
	 **/
	@ManyToOne
	@PropertyMapping(type = BugModel.class, from = "sprintId", subproperty = "id")
	@Column(name = "TARGET_SPRINT_ID")
	private SprintEntity targetSprint;

	/**
	 * Indicates the importance of a story.
	 **/
	@Column(name = "STORY_POINTS")
	private Integer storyPoints;

	/**
	 * The priority.
	 */
	@Column(name = "PRIORITY", nullable = false)
	private Integer priority;

	/**
	 * Instantiate bug.
	 */
	public BugEntity()
	{
		super();
	}

	/**
	 * Instantiate bug with provided id.
	 * 
	 * @param id
	 *            provided id.
	 */
	public BugEntity(Long id)
	{
		super(id);
	}

	/**
	 * Gets the title.
	 * 
	 * @return the title.
	 */
	public String getTitle()
	{
		return title;
	}

	/**
	 * Set the title.
	 * 
	 * @param title
	 *            the new title.
	 */
	public void setTitle(String title)
	{
		this.title = title;
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

	public BugStatus getStatus()
	{
		return status;
	}

	public void setStatus(BugStatus status)
	{
		this.status = status;
	}

	/**
	 * Gets the owner.
	 *
	 * @return the owner
	 */
	public EmployeeEntity getOwner()
	{
		return owner;
	}

	/**
	 * Sets the owner.
	 *
	 * @param owner
	 *            the new owner
	 */
	public void setOwner(EmployeeEntity owner)
	{
		this.owner = owner;
	}

	/**
	 * Gets the story.
	 *
	 * @return the story
	 */
	public StoryEntity getStory()
	{
		return story;
	}

	/**
	 * Sets the story.
	 *
	 * @param story
	 *            the new story
	 */
	public void setStory(StoryEntity story)
	{
		this.story = story;
	}

	/**
	 * Gets the target sprint.
	 *
	 * @return the target sprint
	 */
	public SprintEntity getTargetSprint()
	{
		return targetSprint;
	}

	/**
	 * Sets the target sprint.
	 *
	 * @param targetSprint
	 *            the new target sprint
	 */
	public void setTargetSprint(SprintEntity targetSprint)
	{
		this.targetSprint = targetSprint;
	}

	/**
	 * Gets the priority status.
	 *
	 * @return the priority status
	 */
	public PriorityStatus getPriorityStatus()
	{
		return priorityStatus;
	}

	/**
	 * Sets the priority status.
	 *
	 * @param priorityStatus
	 *            the new priority status
	 */
	public void setPriorityStatus(PriorityStatus priorityStatus)
	{
		this.priorityStatus = priorityStatus;
	}

	/**
	 * Gets the project.
	 *
	 * @return the project
	 */
	public ProjectEntity getProject()
	{
		return project;
	}

	/**
	 * Sets the project.
	 *
	 * @param project
	 *            the new project
	 */
	public void setProject(ProjectEntity project)
	{
		this.project = project;
	}

	/**
	 * Gets the reported by.
	 *
	 * @return the reported by
	 */
	public EmployeeEntity getReportedBy()
	{
		return reportedBy;
	}

	/**
	 * Sets the reported by.
	 *
	 * @param reportedBy
	 *            the new reported by
	 */
	public void setReportedBy(EmployeeEntity reportedBy)
	{
		this.reportedBy = reportedBy;
	}

	/**
	 * Gets the story points.
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
}
