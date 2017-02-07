package com.agilepro.services.bug;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.agilepro.commons.models.bug.BacklogBugModel;
import com.agilepro.commons.models.bug.BugModel;
import com.agilepro.persistence.entity.bug.BugEntity;
import com.agilepro.persistence.entity.project.SprintEntity;
import com.agilepro.persistence.repository.bug.IBugRepository;
import com.agilepro.services.project.SprintService;
import com.yukthi.persistence.ITransaction;
import com.yukthi.utils.exceptions.InvalidStateException;
import com.yukthi.webutils.services.BaseCrudService;

/**
 * The Class BugService.
 * 
 * @author Pritam.
 */
@Service
public class BugService extends BaseCrudService<BugEntity, IBugRepository>
{
	/**
	 * SprintService.
	 */
	@Autowired
	private SprintService sprintService;
	
	/**
	 * Instantiates a new bug service.
	 */
	public BugService()
	{
		super(BugEntity.class, IBugRepository.class);
	}
	
	/**
	 * Adding the priority and saving the bug model.
	 * 
	 * @param bugModel provided bug model for save.
	 * @return newly saved bug.
	 */
	public BugEntity saveBug(BugModel bugModel)
	{
		try(ITransaction transaction = repository.newOrExistingTransaction())
		{
			bugModel.setPriority(repository.getMaxOrder(bugModel.getProjectId()) + 1);
			
			BugEntity bugEntity = super.save(bugModel);
			
			transaction.commit();
			
			return bugEntity;
		} catch(RuntimeException ex)
		{
			throw ex;
		} catch(Exception ex)
		{
			throw new InvalidStateException(ex, "An error occurred while saving bug model - {}", bugModel);
		}
	}

	public void updateBugSprint(Long[] ids, Long sprintId)
	{
		try(ITransaction transaction = repository.newOrExistingTransaction())
		{
			SprintEntity sprint = null;
			
			// check for null for the case where story to backlogs
			if(sprintId != null)
			{
				sprint = sprintService.fetch(sprintId);
			}

			for(Long id : ids)
			{
				repository.updateSprint(id, sprint);
			}

			transaction.commit();
		} catch(RuntimeException ex)
		{
			throw ex;
		} catch(Exception ex)
		{
			throw new InvalidStateException(ex, "An error occurred while updating bug sprint");
		}
	}
	
	/**
	 * Fetch backlogs bugs.
	 * 
	 * @param projectId
	 *            provided project id for fetching the backlog.
	 * @return matching record.
	 */
	public List<BacklogBugModel> fetchBacklogBugs(Long projectId)
	{
		return repository.fetchBacklogBugs(projectId);
	}
}
