package com.agilepro.commons.models.notification;

import java.util.Map;

import com.yukthi.validation.annotations.Required;
import com.yukthi.webutils.common.annotations.IgnoreField;
import com.yukthi.webutils.common.annotations.Model;

/**
 * The Class MailTemplateDefinitionModel.
 */
@Model
public class MailTemplateDefinitionModel
{

	/**
	 * The id.
	 **/
	private Long id;

	/**
	 * The name.
	 **/
	private String name;

	/**
	 * The description.
	 **/
	private String description;

	/**
	 * The context attribute.
	 **/
	@IgnoreField
	private Map<String, String> contextAttribute;

	/** 
	 * The customer id.
	 **/
	@Required
	private Long customerId;

	/**
	 * Instantiates a new mail template definition model.
	 */
	public MailTemplateDefinitionModel()
	{
	}

	/**
	 * Instantiates a new mail template definition model.
	 *
	 * @param name the name
	 * @param description the description
	 * @param customerId the customer id
	 */
	public MailTemplateDefinitionModel(String name, String description, Long customerId)
	{
		this.name = name;
		this.description = description;
		this.customerId = customerId;
	}

	/**
	 * Gets the id.
	 *
	 * @return the id
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
	 * Gets the context attribute.
	 *
	 * @return the context attribute
	 */
	public Map<String, String> getContextAttribute()
	{
		return contextAttribute;
	}

	/**
	 * Sets the context attribute.
	 *
	 * @param contextAttribute
	 *            the context attribute
	 */
	public void setContextAttribute(Map<String, String> contextAttribute)
	{
		this.contextAttribute = contextAttribute;
	}

	/**
	 * Gets the customer id.
	 *
	 * @return the customer id
	 */
	public Long getCustomerId()
	{
		return customerId;
	}

	/**
	 * Sets the customer id.
	 *
	 * @param customerId the new customer id
	 */
	public void setCustomerId(Long customerId)
	{
		this.customerId = customerId;
	}
}
