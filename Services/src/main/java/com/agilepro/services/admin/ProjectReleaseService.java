package com.agilepro.services.admin;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.agilepro.commons.models.customer.ProjectModel;
import com.agilepro.commons.models.project.BasicProjectInfo;
import com.agilepro.controller.response.ProjectReleaseReadResponse;
import com.agilepro.persistence.entity.admin.ProjectReleaseEntity;
import com.agilepro.persistence.repository.admin.IProjectReleaseRepository;
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
	 * The iproject release repository.
	 **/
	private IProjectReleaseRepository iprojectReleaseRepository;
	
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
	 * Inits the.
	 */
	@PostConstruct
	private void init()
	{
		iprojectReleaseRepository = repositoryFactory.getRepository(IProjectReleaseRepository.class);
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
		List<BasicProjectInfo> basicProjectInfos = iprojectReleaseRepository.fetchProjectsByRelease(releaseId);
		
		List<ProjectReleaseEntity>  projectReleaseEntities = iprojectReleaseRepository.fetchAllProjectRelease();
		
		List<ProjectModel> projectModels = projectService.fetchProjects();
		
		if(basicProjectInfos.isEmpty())
		{
			return new ProjectReleaseReadResponse(basicProjectInfos, projectModels);
		}
		
		Set<Long> projectIds =  projectReleaseEntities.stream().map(entity -> entity.getProject().getId()).collect(Collectors.toSet());
		
		List<ProjectModel> filteredModels = projectModels.stream().filter(model -> !projectIds.contains(model.getId())).collect(Collectors.toList());
		
		return new ProjectReleaseReadResponse(basicProjectInfos, filteredModels);
	}
}