package com.agilepro.persistence.entity.project;

import javax.persistence.Column;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.agilepro.commons.StoryNoteStatus;
import com.agilepro.commons.models.project.StoryNoteModel;
import com.agilepro.persistence.entity.admin.EmployeeEntity;
import com.yukthi.persistence.annotations.DataType;
import com.yukthi.persistence.annotations.DataTypeMapping;
import com.yukthi.persistence.annotations.DeleteWithParent;
import com.yukthi.persistence.annotations.UniqueConstraint;
import com.yukthi.persistence.annotations.UniqueConstraints;
import com.yukthi.utils.annotations.PropertyMapping;
import com.yukthi.webutils.repository.WebutilsEntity;

/**
 * StoryNoteEntity holds the note for the story. Draft and Published notes with unique version title per story.
 * 
 * @author Pritam.
 */
@Table(name = "STORY_NOTE")
@UniqueConstraints({ @UniqueConstraint(name = "SPACE_ID_STORY_VERSION_TITLE", fields = { "spaceIdentity", "story", "versionTitle" }) })
public class StoryNoteEntity extends WebutilsEntity
{
	/**
	 * The story.
	 **/
	@Column(name = "STORY_ID", nullable = false)
	@ManyToOne
	@PropertyMapping(type = StoryNoteModel.class, from = "storyId", subproperty = "id")
	@DeleteWithParent
	private StoryEntity story;

	/**
	 * The note.
	 **/
	@Column(name = "CONTENT", nullable = false)
	@DataTypeMapping(type = DataType.CLOB)
	private String content;

	/**
	 * The story note status.
	 **/
	@Column(name = "NOTE_STATUS", nullable = false)
	@DataTypeMapping(type = DataType.STRING)
	private StoryNoteStatus storyNoteStatus;

	/**
	 * The version title.
	 **/
	@Column(name = "VERSION_TITLE", nullable = false)
	private String versionTitle;

	/**
	 * The owner.
	 */
	@Column(name = "EMPLOYEE_ID", nullable = false)
	@PropertyMapping(type = StoryNoteModel.class, from = "employeeId", subproperty = "id")
	@ManyToOne
	private EmployeeEntity employee;

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
	 * Gets the content.
	 *
	 * @return the content
	 */
	public String getContent()
	{
		return content;
	}

	/**
	 * Sets the content.
	 *
	 * @param content
	 *            the new content
	 */
	public void setContent(String content)
	{
		this.content = content;
	}

	/**
	 * Gets the story note status.
	 *
	 * @return the story note status
	 */
	public StoryNoteStatus getStoryNoteStatus()
	{
		return storyNoteStatus;
	}

	/**
	 * Sets the story note status.
	 *
	 * @param storyNoteStatus
	 *            the new story note status
	 */
	public void setStoryNoteStatus(StoryNoteStatus storyNoteStatus)
	{
		this.storyNoteStatus = storyNoteStatus;
	}

	/**
	 * Gets the version title.
	 *
	 * @return the version title
	 */
	public String getVersionTitle()
	{
		return versionTitle;
	}

	/**
	 * Sets the version title.
	 *
	 * @param versionTitle
	 *            the new version title
	 */
	public void setVersionTitle(String versionTitle)
	{
		this.versionTitle = versionTitle;
	}

	/**
	 * Gets the employee.
	 * 
	 * @return the employee.
	 */
	public EmployeeEntity getEmployee()
	{
		return employee;
	}

	/**
	 * Sets the employee.
	 * 
	 * @param employee
	 *            the new employee.
	 */
	public void setEmployee(EmployeeEntity employee)
	{
		this.employee = employee;
	}
}
