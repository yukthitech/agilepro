package com.yukthi.automation.ui.steps;

import java.util.Map;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebElement;

import com.yukthi.automation.AutomationContext;
import com.yukthi.automation.Executable;
import com.yukthi.automation.IExecutionLogger;
import com.yukthi.automation.IStep;
import com.yukthi.automation.ui.common.AutomationUtils;
import com.yukthi.ccg.xml.DynamicBean;
import com.yukthi.utils.exceptions.InvalidStateException;

/**
 * Fill form with action fills the form and then goes for the provided action.
 * 
 * @author Pritam.
 */
@Executable("fillFormWithAction")
public class FillFormWithActionStep implements IStep
{
	/**
	 * The logger.
	 */
	private static Logger logger = LogManager.getLogger(FillFormWithActionStep.class);

	/**
	 * The error message.
	 **/
	private static String DEBUG_MESSAGE = "Populating field {} with value - {}";

	/**
	 * The error message.
	 **/
	private static String ERROR_MESSAGE = "Failed to fill element '{}' under parent '{}' with value - {}";

	/**
	 * Html locator of the form or container (like DIV) enclosing the input
	 * elements.
	 */
	private String locator;

	/**
	 * Data to be filled. All the fields matching with the property names of
	 * specified bean will be searched and populated with corresponding data.
	 */
	private Object data;

	/**
	 * PressEnterAtEnd if true then for the provided action or else ignore.
	 */
	private boolean pressEnterAtEnd;

	/**
	 * Fills the form using dynamic bean.
	 * 
	 * @param context
	 *            Automation context
	 * @param exeLogger
	 *            logger
	 */
	private void fillWithDynamicBean(AutomationContext context, IExecutionLogger exeLogger)
	{
		DynamicBean dynamicBean = (DynamicBean) data;
		Map<String, Object> properties = dynamicBean.getProperties();
		Object value = null;

		for(String name : properties.keySet())
		{
			value = properties.get(name);

			// ignore java core properties like - getClass()
			if(properties.get(name) == null)
			{
				continue;
			}

			exeLogger.debug(DEBUG_MESSAGE, name, value);

			if(!AutomationUtils.populateField(context, null, name, "" + value))
			{
				exeLogger.error(ERROR_MESSAGE, name, value);
				throw new InvalidStateException(ERROR_MESSAGE, name, locator, value);
			}
		}
	}

	/**
	 * Press Enter sets the key value as enter for the web element.
	 * 
	 * @param context
	 *            current Automation context.
	 * @param exeLogger
	 *            logger.
	 */
	private void pressEnter(AutomationContext context, IExecutionLogger exeLogger)
	{
		WebElement webElement = AutomationUtils.findElement(context, null, locator);
		webElement.sendKeys(Keys.ENTER);

		logger.debug("Successfully enter key is pressed");
	}

	/**
	 * Loops throw the properties specified data bean and populates the fields
	 * with matching names.
	 * 
	 * @param context
	 *            Current automation context
	 */
	@Override
	public void execute(AutomationContext context, IExecutionLogger logger)
	{
		if(data instanceof DynamicBean)
		{
			fillWithDynamicBean(context, logger);
		}

		if(pressEnterAtEnd)
		{
			logger.debug("User has provided enter key to be pressed");

			pressEnter(context, logger);
		}
	}

	/**
	 * Gets the html locator of the form or container (like DIV) enclosing the
	 * input elements.
	 *
	 * @return the html locator of the form or container (like DIV) enclosing
	 *         the input elements
	 */
	public String getLocator()
	{
		return locator;
	}

	/**
	 * Sets the html locator of the form or container (like DIV) enclosing the
	 * input elements.
	 *
	 * @param locator
	 *            the new html locator of the form or container (like DIV)
	 *            enclosing the input elements
	 */
	public void setLocator(String locator)
	{
		this.locator = locator;
	}

	/**
	 * Gets the data to be filled. All the fields matching with the property
	 * names of specified bean will be searched and populated with corresponding
	 * data.
	 *
	 * @return the data to be filled
	 */
	public Object getData()
	{
		return data;
	}

	/**
	 * Sets the data to be filled. All the fields matching with the property
	 * names of specified bean will be searched and populated with corresponding
	 * data.
	 *
	 * @param data
	 *            the new data to be filled
	 */
	public void setData(Object data)
	{
		this.data = data;
	}

	/**
	 * Gets press enter at end.
	 * 
	 * @return boolean value of pressEnterAtTheEnd.
	 */
	public boolean getPressEnterAtEnd()
	{
		return pressEnterAtEnd;
	}

	/**
	 * Sets value for press enter at the end.
	 * 
	 * @param pressEnterAtEnd
	 *            the new press enter at the end.
	 */
	public void setPressEnterAtEnd(boolean pressEnterAtEnd)
	{
		this.pressEnterAtEnd = pressEnterAtEnd;
	}
}