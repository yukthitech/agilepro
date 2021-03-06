package com.yukthi.automation.ui.common;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.function.Supplier;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import com.yukthi.automation.AutomationConfiguration;
import com.yukthi.automation.AutomationContext;
import com.yukthi.automation.State;
import com.yukthi.utils.exceptions.InvalidArgumentException;
import com.yukthi.utils.exceptions.UnsupportedOperationException;

/**
 * Common utils used by automation.
 * 
 * @author akiran
 */
public class AutomationUtils
{
	/**
	 * Five seconds.
	 */
	public static final int FIVE_SECONDS = 5;

	private static Logger logger = LogManager.getLogger(AutomationUtils.class);

	/**
	 * Min wait duration.
	 */
	private static final long DURATION_HUNDRED_MILLIS = 100;

	/**
	 * Millis per second.
	 */
	private static final long MILLIS_PER_SEC = 1000;

	/**
	 * Pattern expected to be used by locator strings.
	 */
	private static Pattern LOCATOR_PATTERN = Pattern.compile("(\\w+)\\s*\\:\\s*(.*)");

	/**
	 * Indicates the default state.
	 */
	private static State NO_STATE = new State();

	/**
	 * The open bracket.
	 **/
	private static String OPENBRACKET = "[";

	/**
	 * The close bracket.
	 **/
	private static String CLOSEBRACKET = "]";

	static
	{
		NO_STATE.setName("");
	}

	/**
	 * Fetches input form field type of specified element.
	 * 
	 * @param element
	 *            Element for which form field type has to be determined.
	 * @return Matching form field type
	 */
	public static FormFieldType getFormFieldType(WebElement element)
	{
		String tagName = element.getTagName().toLowerCase();

		if("textarea".equals(tagName))
		{
			return FormFieldType.MULTI_LINE_TEXT;
		}

		if("select".equals(tagName))
		{
			return FormFieldType.DROP_DOWN;
		}

		if("input".equals(tagName))
		{
			String type = "" + element.getAttribute("type");
			type = type.toLowerCase();

			switch (type)
			{
				case "number":
					return FormFieldType.INT;
				case "password":
					return FormFieldType.PASSWORD;
				case "radio":
					return FormFieldType.RADIO_BUTTON;
				case "checkbox":
					return FormFieldType.CHECK_BOX;
				case "date":
					return FormFieldType.DATE;
				default:
					return FormFieldType.TEXT;
			}
		}

		return null;
	}

	/**
	 * Populates specified field with specified value.
	 * 
	 * @param context
	 *            Automation context
	 * @param parent
	 *            Parent under which target element can be found
	 * @param locator
	 *            Locator of the target field. If locator pattern is not used, this will be assumed as name.
	 * @param value
	 *            Value to be populated
	 * @return True, if population was successful.
	 */
	public static boolean populateField(AutomationContext context, WebElement parent, String locator, String value)
	{
		logger.trace("For field {} under parent {} setting value - {}", locator, parent, value);
		
		if(!LOCATOR_PATTERN.matcher(locator).matches())
		{
			locator = LocatorType.NAME.getKey() + ":" + locator;
		}

		List<WebElement> elements = findElements(context, parent, locator);

		// if no elements found with specified name
		if(elements == null || elements.isEmpty())
		{
			return false;
		}

		if(elements.size() == 1)
		{
			WebElement element = elements.get(0);
			String tagName = element.getTagName().toLowerCase();

			FormFieldType type = getFormFieldType(element);

			if(type != null)
			{
				type.getFieldAccessor().setValue(element, value);
			}
			else
			{
				throw new UnsupportedOperationException("Encountered unsupported input tag '{}' for data population", tagName);
			}
		}

		// find the parent element enclosing input elements
		return true;
	}

	/**
	 * Fetches the element with specified locator.
	 * 
	 * @param context
	 *            Context to be used
	 * @param parent
	 *            Parent under which element need to be searched
	 * @param locator
	 *            Locator to be used for searching
	 * @return Matching element
	 */
	public static WebElement findElement(AutomationContext context, WebElement parent, String locator)
	{
		List<WebElement> elements = findElements(context, parent, locator);

		if(elements == null || elements.size() == 0)
		{
			return null;
		}

		return elements.get(0);
	}

	/**
	 * Fetches the elements with specified locator.
	 * 
	 * @param context
	 *            Context to be used
	 * @param parent
	 *            Parent under which elements need to be searched
	 * @param locator
	 *            Locator to be used for searching
	 * @return Matching elements
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public static List<WebElement> findElements(AutomationContext context, WebElement parent, String locator)
	{
		logger.trace("Trying to find element with location '{}' under parent - {}", locator, parent);

		WebDriver driver = context.getWebDriver();

		Matcher matcher = LOCATOR_PATTERN.matcher(locator);
		LocatorType locatorType = LocatorType.JS;
		String query = null;

		// if the locator string matches required pattern
		if(matcher.matches())
		{
			locatorType = LocatorType.getLocatorType(matcher.group(1));
			query = matcher.group(2);

			// if invalid locator is specified
			if(locatorType == null)
			{
				throw new InvalidArgumentException("Invalid key '{}' encountered in locator - {}", matcher.group(1), locator);
			}
		}
		else
		{
			query = locator;
		}

		By locatorBy = null;

		// find the locator based on prefix
		switch (locatorType)
		{
			case ID:
				locatorBy = By.id(query);
				break;
			case CSS:
				locatorBy = By.cssSelector(query);
				break;
			case CLASS:
				locatorBy = By.className(query);
				break;
			case NAME:
				locatorBy = By.name(query);
				break;
			case TAG:
				locatorBy = By.tagName(query);
				break;
			case XPATH:
				locatorBy = By.xpath(query);
				break;
			default:
				locatorBy = null;
		}

		logger.trace("For locator '{}' using locator-by - {}", locator, locatorBy);

		List<WebElement> result = null;

		// if locator type is not defined (which would be the case for JS
		// locator type)
		if(locatorBy == null)
		{
			Object res = ((JavascriptExecutor) driver).executeScript("return $(" + query + ").get()");

			if(res instanceof Collection)
			{
				result = new ArrayList<WebElement>((Collection) res);
			}
			else
			{
				result = Arrays.asList((WebElement) res);
			}
		}
		// if parent is defined
		else if(parent != null)
		{
			result = parent.findElements(locatorBy);
		}
		else
		{
			result = driver.findElements(locatorBy);
		}

		if(logger.isTraceEnabled())
		{
			logger.trace("For locator '{}' found elements as - {}", locator, toString(context, result));
		}

		return result;
	}

	/**
	 * Computes and returns the current states base on the current url and state
	 * url patterns.
	 * 
	 * @param context
	 *            Context to be used.
	 * @return Currently matching states.
	 */
	public static List<State> getCurrentStates(AutomationContext context)
	{
		WebDriver driver = context.getWebDriver();
		String url = driver.getCurrentUrl();

		// if the browser is not yet open
		if(url == null)
		{
			logger.debug("As no url is found assuming the current state is no state");
			return Arrays.asList(NO_STATE);
		}

		Pattern pattern = null;
		List<State> matchingStates = new ArrayList<State>();

		// loop through all known states
		for(State state : context.getStateConfiguration().getAllStates())
		{
			pattern = state.getUrlPatternObject();

			// if current url matches with current state url pattern
			if(pattern.matcher(url).matches())
			{
				matchingStates.add(state);
			}
		}

		// if no matching state is found
		if(matchingStates.isEmpty())
		{
			logger.debug("As no state is matching with current url assuming no state. Current url - " + url);
			return Arrays.asList(NO_STATE);
		}

		// return states with matching url patterns
		return matchingStates;
	}

	/**
	 * Waits for specified amount of time.
	 * 
	 * @param millis
	 *            Milli seconds to wait.
	 */
	public static void waitFor(long millis)
	{
		try
		{
			Thread.sleep(millis);
		} catch(Exception ex)
		{
			ex.printStackTrace();
		}
	}

	/**
	 * Checks for checkFunction to be true, if not waits for 1 sec and again
	 * tries to check. This process will be repeated for "iterationCount" number
	 * of times. If result is still false, exception "ex" will be thrown.
	 * 
	 * @param checkFunction
	 *            Function to check
	 * @param waitTime
	 *            Time in seconds to wait for
	 * @param waitMessage
	 *            Wait message to be logged during waiting.
	 * @param ex
	 *            Exception to be thrown if all tries fail.
	 */
	public static void validateWithWait(Supplier<Boolean> checkFunction, int waitTime, String waitMessage, RuntimeException ex)
	{
		long iterationCount = (waitTime * MILLIS_PER_SEC) / DURATION_HUNDRED_MILLIS;

		for(int i = 0; i < iterationCount; i++)
		{
			if(checkFunction.get())
			{
				return;
			}

			logger.trace(waitMessage);
			waitFor(DURATION_HUNDRED_MILLIS);
		}

		throw ex;
	}

	/**
	 * Generates html node string from specified elements.
	 * 
	 * @param context
	 *            Automation context
	 * @param elements
	 *            Elements to be converted
	 * @return Converted string.
	 */
	public static String toString(AutomationContext context, Collection<WebElement> elements)
	{
		StringBuilder builder = new StringBuilder(OPENBRACKET);
		boolean first = true;
		JavascriptExecutor jsExecutor = (JavascriptExecutor) context.getWebDriver();

		for(WebElement element : elements)
		{
			if(!first)
			{
				builder.append(",\n");
			}

			builder.append(element).append(OPENBRACKET);

			if(element == null)
			{
				builder.append("null");
				continue;
			}

			builder.append(jsExecutor.executeScript(AutomationConfiguration.getInstance().getScript("elementToString"), element));
			builder.append(CLOSEBRACKET);
			first = false;
		}

		builder.append(CLOSEBRACKET);
		return builder.toString();
	}
}
