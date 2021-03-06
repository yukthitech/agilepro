package com.agilepro.commons.models.project;

import java.util.List;
import com.yukthi.persistence.repository.annotations.Field;
import com.yukthi.webutils.common.annotations.NonDisplayable;

/**
 * BacklogModel is used for fetching the stories where sprint id will be null.
 * Result object will be mapped with the entity class.
 * 
 * @author Pritam.
 */
public class BacklogStoryModel
{
	/**
	 * Id of the story record.
	 */
	@Field("id")
	private Long id;

	/**
	 * Title of the story.
	 */
	@Field("title")
	private String title;

	/**
	 * The story points.
	 **/
	@Field("storyPoints")
	private Integer storyPoints;

	/**
	 * Parent story id.
	 */
	@Field("parentStory.id")
	private Long parentStoryId;

	/**
	 * Priority of the story.
	 */
	@Field("priority")
	private Integer priority;

	/**
	 * Project id.
	 */
	@Field("project.id")
	private Long projectId;

	/**
	 * Dependencies of the story.
	 */
	private List<StoryDependencyModel> dependencies;

	/**
	 * Is management story, true if it has child stories or else by default
	 * false.
	 */
	@Field("isManagementStory")
	private Boolean isManagementStory;

	/**
	 * Has Childrens story, boolean value used for indicating the story has
	 * childs or not.
	 */
	@NonDisplayable
	private Boolean hasChildrens;

	/**
	 * Indicates false for a bug model.
	 */
	private Boolean isBug = false;

	/**
	 * Gets the story id.
	 * 
	 * @return the story id.
	 */
	public Long getId()
	{
		return id;
	}

	/**
	 * Sets the story id.
	 * 
	 * @param id
	 *            the new story id.
	 */
	public void setId(Long id)
	{
		this.id = id;
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
	 * Sets the title.
	 * 
	 * @param title
	 *            the new title.
	 */
	public void setTitle(String title)
	{
		this.title = title;
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
	 * Gets the parent story id.
	 * 
	 * @return the parent story id.
	 */
	public Long getParentStoryId()
	{
		return parentStoryId;
	}

	/**
	 * Sets the parent story id.
	 * 
	 * @param parentStoryId
	 *            the new parent story id.
	 */
	public void setParentStoryId(Long parentStoryId)
	{
		this.parentStoryId = parentStoryId;
	}

	/**
	 * Gets the priority.
	 * 
	 * @return the priority.
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
	 * Gets the dependencies.
	 * 
	 * @return the dependencies.
	 */
	public List<StoryDependencyModel> getDependencies()
	{
		return dependencies;
	}

	/**
	 * Set the dependencies.
	 * 
	 * @param dependencies
	 *            new dependencies.
	 */
	public void setDependencies(List<StoryDependencyModel> dependencies)
	{
		this.dependencies = dependencies;
	}

	/**
	 * Gets the project id.
	 * 
	 * @return the project id.
	 */
	public Long getProjectId()
	{
		return projectId;
	}

	/**
	 * Sets the project id.
	 * 
	 * @param projectId
	 *            the new project id.
	 */
	public void setProjectId(Long projectId)
	{
		this.projectId = projectId;
	}

	/**
	 * Gets the management story.
	 * 
	 * @return the management story.
	 */
	public Boolean getIsManagementStory()
	{
		return isManagementStory;
	}

	/**
	 * Sets the management story.
	 * 
	 * @param isManagementStory
	 *            new management story.
	 */
	public void setIsManagementStory(Boolean isManagementStory)
	{
		this.isManagementStory = isManagementStory;
	}

	/**
	 * Get has childrens.
	 * 
	 * @return the has childrens.
	 */
	public Boolean getHasChildrens()
	{
		return hasChildrens;
	}

	/**
	 * Set the has childrens.
	 * 
	 * @param hasChildrens
	 *            the new has childrens
	 */
	public void setHasChildrens(Boolean hasChildrens)
	{
		this.hasChildrens = hasChildrens;
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
}
