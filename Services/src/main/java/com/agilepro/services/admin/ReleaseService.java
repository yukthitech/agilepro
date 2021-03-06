package com.agilepro.services.admin;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import org.springframework.stereotype.Service;
import com.agilepro.commons.models.customer.ReleaseModel;
import com.agilepro.persistence.entity.admin.ReleaseEntity;
import com.agilepro.persistence.repository.admin.IReleaseRepository;
import com.yukthi.webutils.services.BaseCrudService;

/**
 * The Class ReleaseService.
 */
@Service
public class ReleaseService extends BaseCrudService<ReleaseEntity, IReleaseRepository>
{
	/**
	 * Instantiates a new release service.
	 */
	public ReleaseService()
	{
		super(ReleaseEntity.class, IReleaseRepository.class);
	}

	/**
	 * Fetch all release.
	 *
	 * @return the list
	 */
	public List<ReleaseModel> fetchAllRelease()
	{
		List<ReleaseEntity> releaseEntities = repository.fetchAllReleaseByDate(new Date());

		List<ReleaseModel> releaseModels = new ArrayList<ReleaseModel>(releaseEntities.size());

		releaseEntities.forEach(entity -> releaseModels.add(super.toModel(entity, ReleaseModel.class)));

		return releaseModels;
	}

	/**
	 * Deletes all release.
	 */
	public void deleteAll()
	{
		repository.deleteAll();
	}
}
