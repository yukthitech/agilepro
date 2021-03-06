package com.agilepro.persistence.entity.admin;

import javax.persistence.Column;
import javax.persistence.Table;

import com.yukthi.persistence.annotations.UniqueConstraint;
import com.yukthi.persistence.annotations.UniqueConstraints;
import com.yukthi.webutils.repository.WebutilsEntity;

/**
 * TagEntity has the color code for tagging the project members.
 * Create Read Update Delete.
 * 
 * @author Pritam
 */
@Table(name = "TAG")
@UniqueConstraints({ @UniqueConstraint(name = "SPACE_ID_NAME", fields = { "spaceIdentity", "name" }) })
public class TagEntity extends WebutilsEntity
{
	/**
	 * The name.
	 **/
	@Column(name = "NAME", length = 50, nullable = false)
	@UniqueConstraint(name = "name", message = "Please provide a different name, provided name is already present")
	private String name;

	/**
	 * The description.
	 **/
	@Column(name = "DESCRIPTION")
	private String description;

	/**
	 * The color.
	 **/
	@Column(name = "COLOR", length = 50)
	private String color;

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
	 * Gets the color.
	 *
	 * @return the color
	 */
	public String getColor()
	{
		return color;
	}

	/**
	 * Sets the color.
	 *
	 * @param color
	 *            the new color
	 */
	public void setColor(String color)
	{
		this.color = color;
	}
}
