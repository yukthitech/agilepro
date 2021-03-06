package com.yukthi.automation.ui.validations;

import java.util.ArrayList;
import java.util.List;

import org.openqa.selenium.WebElement;

import com.yukthi.automation.AbstractValidation;
import com.yukthi.automation.AutomationContext;
import com.yukthi.automation.Executable;
import com.yukthi.automation.IExecutionLogger;
import com.yukthi.automation.ui.common.AutomationUtils;
import com.yukthi.automation.ui.common.FieldOption;
import com.yukthi.automation.ui.common.FormFieldType;

/**
 * Validates specified form has specified fields with specified field details.
 */
@Executable("validateFormFields")
public class ValidateFormFields extends AbstractValidation
{
	/**
	 * Represents field details to validate.
	 * 
	 * @author akiran
	 */
	public static class FormField
	{
		/**
		 * locator of the field.
		 */
		private String locator;

		/**
		 * Indicates if multiple fields are expected with same name.
		 */
		private boolean multiple;

		/**
		 * Expected form field type.
		 */
		private FormFieldType type;

		/**
		 * List of field options to be validated.
		 */
		private List<FieldOption> fieldOptions;

		/**
		 * Expected value of the field.
		 */
		private String value;

		/**
		 * Gets the locator of the field.
		 *
		 * @return the locator of the field
		 */
		public String getLocator()
		{
			return locator;
		}

		/**
		 * Sets the locator of the field.
		 *
		 * @param locator
		 *            the new locator of the field
		 */
		public void setLocator(String locator)
		{
			this.locator = locator;
		}

		/**
		 * Checks if is indicates if multiple fields are expected with same
		 * name.
		 *
		 * @return the indicates if multiple fields are expected with same name
		 */
		public boolean isMultiple()
		{
			return multiple;
		}

		/**
		 * Sets the indicates if multiple fields are expected with same name.
		 *
		 * @param multiple
		 *            the new indicates if multiple fields are expected with
		 *            same name
		 */
		public void setMultiple(boolean multiple)
		{
			this.multiple = multiple;
		}

		/**
		 * Gets the expected form field type.
		 *
		 * @return the expected form field type
		 */
		public FormFieldType getType()
		{
			return type;
		}

		/**
		 * Sets the expected form field type.
		 *
		 * @param type
		 *            the new expected form field type
		 */
		public void setType(FormFieldType type)
		{
			this.type = type;
		}

		/**
		 * Gets the list of field options to be validated.
		 *
		 * @return the list of field options to be validated
		 */
		public List<FieldOption> getFieldOptions()
		{
			return fieldOptions;
		}

		/**
		 * Sets the list of field options to be validated.
		 *
		 * @param fieldOptions
		 *            the new list of field options to be validated
		 */
		public void setFieldOptions(List<FieldOption> fieldOptions)
		{
			this.fieldOptions = fieldOptions;
		}

		/**
		 * Adds value to {@link #fieldOptions fieldOptions}.
		 *
		 * @param fieldOption
		 *            fieldOption to be added
		 */
		public void addFieldOption(FieldOption fieldOption)
		{
			if(fieldOptions == null)
			{
				fieldOptions = new ArrayList<FieldOption>();
			}

			fieldOptions.add(fieldOption);
		}

		/**
		 * Gets the expected value of the field.
		 *
		 * @return the expected value of the field
		 */
		public String getValue()
		{
			return value;
		}

		/**
		 * Sets the expected value of the field.
		 *
		 * @param value
		 *            the new expected value of the field
		 */
		public void setValue(String value)
		{
			this.value = value;
		}
	}

	/**
	 * Locator for the form.
	 */
	private String locator;

	/**
	 * Field details to validate.
	 */
	private List<FormField> fields;

	/**
	 * Sets the locator for the form.
	 *
	 * @param locator
	 *            the new locator for the form
	 */
	public void setLocator(String locator)
	{
		this.locator = locator;
	}

	/**
	 * Adds value to {@link #fields fields}.
	 *
	 * @param field
	 *            field to be added
	 */
	public void addField(FormField field)
	{
		if(fields == null)
		{
			fields = new ArrayList<FormField>();
		}

		fields.add(field);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.yukthi.ui.automation.IValidation#execute(com.yukthi.ui.automation.
	 * AutomationContext, com.yukthi.ui.automation.IExecutionLogger)
	 */
	@Override
	public boolean validate(AutomationContext context, IExecutionLogger exeLogger)
	{
		WebElement formElement = AutomationUtils.findElement(context, null, locator);
		List<WebElement> fieldElements = null;
		FormFieldType fieldType = null;

		for(FormField field : this.fields)
		{
			fieldElements = AutomationUtils.findElements(context, formElement, field.locator);

			if(fieldElements == null || fieldElements.isEmpty())
			{
				exeLogger.error("No field found with locator: " + field.getLocator());
				return false;
			}

			if(fieldElements.size() > 1 && !field.isMultiple())
			{
				exeLogger.error("Multiple fields found when single field is expected for locator: " + field.getLocator());
				return false;
			}

			if(field.getType() != null)
			{
				for(WebElement element : fieldElements)
				{
					fieldType = AutomationUtils.getFormFieldType(element);

					if(fieldType == null)
					{
						exeLogger.error("Failed to find field type of field locator: " + field.getLocator());
						return false;
					}

					if(field.getType() != fieldType)
					{
						exeLogger.error(String.format("Expected field type '%s' is not matching with actual field type '%s' for locator - %s", field.getType(), fieldType, field.locator));
						return false;
					}
				}
			}

			if(field.getFieldOptions() != null)
			{
				List<FieldOption> expectedOptions = field.getFieldOptions();
				List<FieldOption> actualOptions = fieldType.getFieldAccessor().getOptions(fieldElements.get(0));
				FieldOption expectedOption = null, actualOption = null;

				for(int i = 0; i < expectedOptions.size(); i++)
				{
					expectedOption = expectedOptions.get(i);
					actualOption = actualOptions.get(i);

					if(expectedOption.getLabel() != null && !expectedOption.getLabel().equals(actualOption.getLabel()))
					{
						exeLogger.error("At index {} expected field option label '{}' " + "is not matching with actual field option label '{}' for locator - {}", i, expectedOption.getLabel(), actualOption.getLabel(), field.locator);
						return false;
					}

					if(expectedOption.getValue() != null && !expectedOption.getValue().equals(actualOption.getValue()))
					{
						exeLogger.error("At index {} expected field option value '{}' " + "is not matching with actual field option value '{}' for locator - {}", i, expectedOption.getValue(), actualOption.getValue(), field.locator);
						return false;
					}
				}
			}

			if(field.getValue() != null)
			{
				String actValue = fieldType.getFieldAccessor().getValue(fieldElements.get(0));

				if(field.getValue().equals(actValue))
				{
					exeLogger.error("Expected field value '{}' is not matching with " + "actual field value'{}' for locator - {}", field.getValue(), actValue, field.locator);
					return false;
				}
			}
		}

		return true;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString()
	{
		StringBuilder builder = new StringBuilder(super.toString());
		builder.append("[");

		builder.append("Locator: ").append(locator);

		builder.append("]");
		return builder.toString();
	}
}
