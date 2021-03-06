package com.agilepro.commons.models.sprint;

import java.util.Date;

import com.yukthi.persistence.repository.annotations.Condition;
import com.yukthi.persistence.repository.annotations.Operator;
import com.yukthi.validation.annotations.Required;
import com.yukthi.webutils.common.annotations.Model;
import com.yukthi.webutils.common.annotations.NonDisplayable;

/**
 * The Class SprintSearchQuery.
 */
@Model
public class SprintSearchQuery
{
	/**
	 * The name.
	 **/
	@Condition(value = "name", op = Operator.LIKE, ignoreCase = true)
	private String name;

	/**
	 * The description.
	 **/
	@Condition(value = "description", op = Operator.LIKE, ignoreCase = true)
	private String description;

	/**
	 * The start date.
	 **/
	@Condition(value = "startDate", op = Operator.GE)
	private Date startDate;

	/**
	 * The end date.
	 **/
	@Condition(value = "endDate", op = Operator.LE)
	private Date endDate;
	
	/**
	 * Project id is mandatory for sprint search.
	 */
	@Required
	@NonDisplayable
	@Condition(value = "project.id", op = Operator.EQ)
	private Long projectId;

	/**
	 * Gets the name.
	 *
	 * @return the name
	 */
	public String getName()
	{
		return name;
	}

	/**
	 * Sets the name.
	 *
	 * @param name
	 *            the new name
	 */
	public void setName(String name)
	{
		this.name = name;
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
	 * Gets the start date.
	 *
	 * @return the start date
	 */
	public Date getStartDate()
	{
		return startDate;
	}

	/**
	 * Sets the start date.
	 *
	 * @param startDate
	 *            the new start date
	 */
	public void setStartDate(Date startDate)
	{
		this.startDate = startDate;
	}

	/**
	 * Gets the end date.
	 *
	 * @return the end date
	 */
	public Date getEndDate()
	{
		return endDate;
	}

	/**
	 * Sets the end date.
	 *
	 * @param endDate
	 *            the new end date
	 */
	public void setEndDate(Date endDate)
	{
		this.endDate = endDate;
	}

	public Long getProjectId()
	{
		return projectId;
	}

	public void setProjectId(Long projectId)
	{
		this.projectId = projectId;
	}
}
