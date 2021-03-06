package com.agilepro.services.admin;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.agilepro.commons.UserRole;
import com.agilepro.commons.models.admin.EmployeeModel;
import com.agilepro.controller.AgileProUserDetails;
import com.agilepro.persistence.entity.admin.CustomerEntity;
import com.agilepro.persistence.entity.admin.DesignationEntity;
import com.agilepro.persistence.entity.admin.EmployeeEntity;
import com.agilepro.persistence.repository.admin.IEmployeeRepository;
import com.yukthi.persistence.ITransaction;
import com.yukthi.utils.exceptions.InvalidStateException;
import com.yukthi.utils.exceptions.NullValueException;
import com.yukthi.webutils.repository.UserEntity;
import com.yukthi.webutils.repository.UserRoleEntity;
import com.yukthi.webutils.services.BaseCrudService;
import com.yukthi.webutils.services.CurrentUserService;
import com.yukthi.webutils.services.UserRoleService;
import com.yukthi.webutils.services.UserService;
import com.yukthi.webutils.utils.WebUtils;

/**
 * employee.
 *
 * @author Bhavana
 */
@Service
public class EmployeeService extends BaseCrudService<EmployeeEntity, IEmployeeRepository>
{
	private static Logger logger = LogManager.getLogger(EmployeeService.class);

	/**
	 * UserService for UserEntity table.
	 */
	@Autowired
	private UserService userService;

	/**
	 * UserRoleService for saving the role.
	 */
	@Autowired
	private UserRoleService userRoleService;

	/**
	 * Used to fetch current user info.
	 */
	@Autowired
	private CurrentUserService currentUserService;

	/**
	 * used to fetch the designations.
	 */
	@Autowired
	private DesignationService designationService;

	/**
	 * Instantiates a new employee service.
	 */
	public EmployeeService()
	{
		super(EmployeeEntity.class, IEmployeeRepository.class);
	}

	/**
	 * Save.
	 *
	 * @param model
	 *            the model
	 * @return the employee entity
	 */
	public EmployeeEntity saveEmployee(EmployeeModel model)
	{
		try(ITransaction transaction = repository.newOrExistingTransaction())
		{
			AgileProUserDetails cbiller = (AgileProUserDetails) currentUserService.getCurrentUserDetails();

			Long customerId = cbiller.getCustomerId();

			// saving employee
			EmployeeEntity employeeEntity = super.save(model);

			// saving user
			saveUser(model, customerId, employeeEntity);

			transaction.commit();

			return employeeEntity;
		} catch(RuntimeException ex)
		{
			throw ex;
		} catch(Exception ex)
		{
			throw new InvalidStateException(ex, "An error occurred while saving model - {}", model);
		}
	}

	// This method is for testing purpose which will be removed latter
	@Override
	public EmployeeEntity save(Object model)
	{
		// use transaction

		AgileProUserDetails cbiller = (AgileProUserDetails) currentUserService.getCurrentUserDetails();
		Long customerId = cbiller.getCustomerId();

		// convert to entity
		EmployeeEntity entity = WebUtils.convertBean(model, EmployeeEntity.class);
		EmployeeModel empModel = null;

		List<DesignationEntity> designations = new ArrayList<DesignationEntity>();

		if(model instanceof EmployeeModel)
		{
			empModel = (EmployeeModel) model;
		}

		for(Long desigNationId : empModel.getDesignationId())
		{
			designations.add(designationService.fetch(desigNationId));
		}

		entity.setDesignations(designations);

		super.save(entity, model);

		// saving user
		saveUser(empModel, customerId, entity);

		return entity;
	}

	// This method is for testing which will be removed latter
	public EmployeeEntity saveEmp(EmployeeModel model)
	{
		return this.save(model);
	}

	/**
	 * Update.
	 *
	 * @param model
	 *            the model
	 * @return the employee entity
	 */
	public EmployeeEntity update(EmployeeModel model)
	{
		if(model == null)
		{
			throw new NullValueException("EmployeeModel Object is null");
		}

		try(ITransaction transaction = repository.newOrExistingTransaction())
		{
			// updating employee
			EmployeeEntity customerEmployee = super.update(model);

			// updating user
			updateUser(model, customerEmployee);

			transaction.commit();
			return customerEmployee;
		} catch(RuntimeException ex)
		{
			throw ex;
		} catch(Exception ex)
		{
			throw new InvalidStateException(ex, "An error occurred while updating model - {}", model);
		}
	}

	/**
	 * Save user.
	 *
	 * @param model
	 *            the model
	 * @param customerUser
	 *            the employee user
	 */
	private void saveUser(EmployeeModel model, Long customerId, EmployeeEntity employeeEntity)
	{
		UserEntity userEntity = new UserEntity();

		// model is having the password
		userEntity.setPassword(model.getPassword());

		userEntity.setUserName(employeeEntity.getMailId());
		userEntity.setDisplayName(employeeEntity.getName());
		userEntity.setBaseEntityId(employeeEntity.getId());
		userEntity.setBaseEntityType(EmployeeEntity.class.getName());

		UserRoleEntity roleEntity = new UserRoleEntity();
		roleEntity.setOwnerType(CustomerEntity.class.getName());
		roleEntity.setOwnerId(customerId);
		roleEntity.setRole(UserRole.EMPLOYEE_VIEW);
		roleEntity.setUser(userEntity);

		userService.save(userEntity, null);
		userRoleService.save(roleEntity, null);
		logger.debug("Added new employee with user-name - " + userEntity.getUserName());
	}

	/**
	 * Update user.
	 *
	 * @param model
	 *            the model
	 * @param employee
	 *            the employee user
	 */
	private void updateUser(EmployeeModel model, EmployeeEntity employee)
	{
		UserEntity userEntity = userService.fetchUserByBaseEntity(employee.getClass().getName(), employee.getId());

		if(userEntity == null)
		{
			throw new InvalidStateException("No user record found with base type details [Type: {}, Id: {}]", employee.getClass().getName(), employee.getId());
		}

		userEntity.setPassword(model.getPassword());
		userEntity.setUserName(model.getMailId());
		userEntity.setDisplayName(model.getName());
		userEntity.setVersion(userEntity.getVersion());

		userService.update(userEntity, null);
		logger.debug("Updated employee user with user-name - " + userEntity.getUserName());
	}

	/**
	 * deleteById.
	 * 
	 * @return employee
	 */
	@Override
	public boolean deleteById(long id)
	{
		try(ITransaction transaction = repository.newOrExistingTransaction())
		{
			userService.deleteByBaseEntity(EmployeeEntity.class.getName(), id);

			boolean res = super.deleteById(id);

			transaction.commit();
			return res;
		} catch(RuntimeException ex)
		{
			throw ex;
		} catch(Exception ex)
		{
			throw new InvalidStateException(ex, "An error occurred while deleting employee with id - {}", id);
		}
	}

	/**
	 * Fetch employees.
	 *
	 * @param employeeName
	 *            the employee name
	 * @return the list
	 */
	public List<EmployeeModel> fetchEmployees(String employeeName)
	{
		List<EmployeeModel> employeeModels = new ArrayList<EmployeeModel>();

		if(employeeName != null)
		{
			employeeName = employeeName.replace('*', '%');
		}

		List<EmployeeEntity> employeeEntities = repository.fetchEmployees(employeeName);

		if(employeeEntities != null)
		{
			for(EmployeeEntity entity : employeeEntities)
			{
				employeeModels.add(super.toModel(entity, EmployeeModel.class));
			}
		}

		return employeeModels;
	}

	/**
	 * Fetch with no space.
	 *
	 * @param employeeId
	 *            the employee id
	 * @return the employee entity
	 */
	public EmployeeEntity fetchWithNoSpace(Long employeeId)
	{
		return repository.fetchWithNoSpace(employeeId);
	}

	/**
	 * Fetches specified employee roles based on employee designations.
	 * 
	 * @param employeeId
	 *            Employee id for whom roles needs to be fetched
	 * @return Matching roles
	 */
	public Set<UserRole> fetchEmployeeRoles(Long employeeId)
	{
		List<Long> designationIds = repository.fetchDesignationIds(employeeId);

		Set<UserRole> roles = new HashSet<UserRole>();
		DesignationEntity designation = null;

		if(designationIds != null)
		{
			for(Long designationId : designationIds)
			{
				designation = designationService.fetch(designationId);

				for(UserRole role : designation.getRoles())
				{
					roles.add(role);
				}
			}
		}

		return roles;
	}

	/**
	 * Fetch employee name for the provided employee id.
	 * 
	 * @param id
	 *            provided employee id for fetching the record.
	 * @return the matching record employee name.
	 */
	public String fetchEmployeeName(Long id)
	{
		return repository.fetchEmployeeName(id);
	}

	/**
	 * Deletes all records.
	 */
	public void deleteAll()
	{
		repository.deleteAll();
	}
}
