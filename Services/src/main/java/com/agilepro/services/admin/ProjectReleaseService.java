package com.agilepro.services.admin;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.agilepro.commons.models.customer.ProjectModel;
import com.agilepro.commons.models.customer.ProjectReleaseModel;
import com.agilepro.commons.models.project.BasicProjectInfo;
import com.agilepro.controller.response.ProjectReleaseReadResponse;
import com.agilepro.persistence.entity.admin.ProjectReleaseEntity;
import com.agilepro.persistence.repository.admin.IProjectReleaseRepository;
import com.yukthi.persistence.ITransaction;
import com.yukthi.webutils.services.BaseCrudService;

/**
 * The Class ProjectReleaseService.
 * 
 * @author Pritam
 */
@Service
public class ProjectReleaseService extends BaseCrudService<ProjectReleaseEntity, IProjectReleaseRepository>
{
	/**
	 * The project service.
	 **/
	@Autowired
	private ProjectService projectService;

	/**
	 * Instantiates a new project release service.
	 */
	public ProjectReleaseService()
	{
		super(ProjectReleaseEntity.class, IProjectReleaseRepository.class);
	}

	/**
	 * Save.
	 *
	 * @param projectReleasedModel
	 *            the project released model
	 * @return the project release entity
	 */
	public ProjectReleaseEntity saveProjectRelease(ProjectReleaseModel projectReleasedModel)
	{
		try(ITransaction transaction = repository.newOrExistingTransaction())
		{
			ProjectReleaseEntity projectReleaseEntity = null;

			List<Long> projectIds = projectReleasedModel.getProjectIds();

			if(projectIds == null)
			{
				projectReleaseEntity = super.save(projectReleasedModel);
			}
			else
			{
				for(Long prjId : projectIds)
				{
					projectReleaseEntity = super.save(new ProjectReleaseModel(prjId, projectReleasedModel.getReleaseId()));
				}
			}

			transaction.commit();

			return projectReleaseEntity;
		} catch(Exception ex)
		{
			throw new IllegalStateException("An error occurred  while saving  project release - ", ex);
		}
	}

	/**
	 * Fetch all project release.
	 *
	 * @param releaseId
	 *            the release id
	 * @return the list
	 */
	public ProjectReleaseReadResponse fetchAllProjectRelease(Long releaseId)
	{
		List<BasicProjectInfo> basicProjectInfos = repository.fetchProjectsByRelease(releaseId);

		List<ProjectReleaseEntity> projectReleaseEntities = repository.fetchAllProjectRelease();

		Set<Long> projectIds = projectReleaseEntities.stream().map(entity -> entity.getProject().getId()).collect(Collectors.toSet());

		List<ProjectModel> projectModels = projectService.fetchAllProjects();

		List<ProjectModel> filteredModels = projectModels.stream().filter(model -> !projectIds.contains(model.getId())).collect(Collectors.toList());

		return new ProjectReleaseReadResponse(basicProjectInfos, filteredModels);
	}

	/**
	 * Delete by project id.
	 *
	 * @param projectReleasedModel the project released model
	 */
	public void deleteByProjectId(ProjectReleaseModel projectReleasedModel)
	{
		try(ITransaction transaction = repository.newOrExistingTransaction())
		{
			List<Long> projectIds = projectReleasedModel.getProjectIds();
			
			if(projectIds != null)
			{
				for(Long projectId : projectIds)
				{
					if(!repository.deleteByProjectId(projectId))
					{
						throw new IllegalStateException("An error occurred  while deleting project release");
					}
				}
			}
			
			transaction.commit();
		} catch(Exception ex)
		{
			throw new IllegalStateException("An error occurred  while deleting project release - ", ex);
		}
	}
}
