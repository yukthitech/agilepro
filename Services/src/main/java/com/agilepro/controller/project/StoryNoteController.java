package com.agilepro.controller.project;

import static com.agilepro.commons.IAgileproActions.ACTION_PREFIX_STORY_NOTE;
import static com.agilepro.commons.IAgileproActions.ACTION_TYPE_LATEST_STORY_NOTE_BY_STORY_ID;
import static com.agilepro.commons.IAgileproActions.ACTION_TYPE_SAVE_OR_UPDATE;
import static com.agilepro.commons.IAgileproActions.PARAM_ID;
import static com.agilepro.commons.IAgileproActions.ACTION_TYPE_READ_ALL_STORY_NOTE_BY_STORY_ID;
import static com.agilepro.commons.IAgileproActions.ACTION_TYPE_DELETE;

import java.util.Date;
import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.agilepro.commons.UserRole;
import com.agilepro.commons.models.project.StoryNoteModel;
import com.agilepro.services.common.Authorization;
import com.agilepro.services.project.StoryNoteService;
import com.yukthi.webutils.annotations.ActionName;
import com.yukthi.webutils.common.models.BaseResponse;
import com.yukthi.webutils.common.models.BasicReadResponse;
import com.yukthi.webutils.common.models.BasicSaveResponse;
import com.yukthi.webutils.controllers.BaseController;

/**
 * The Class StoryNoteController.
 * 
 * @author Pritam
 */
@RestController
@ActionName(ACTION_PREFIX_STORY_NOTE)
@RequestMapping("/storyNote")
public class StoryNoteController extends BaseController
{
	/**
	 * The story note service.
	 **/
	@Autowired
	private StoryNoteService storyNoteService;

	/**
	 * Save or update the story note model.
	 * Save for new published story note.
	 * Update for the existing draft.
	 *
	 * @param storyNoteModel
	 *            the story note model
	 * @return the basic save response
	 */
	@ActionName(ACTION_TYPE_SAVE_OR_UPDATE)
	@Authorization(roles = { UserRole.EMPLOYEE_VIEW, UserRole.STORY_NOTE_EDIT, UserRole.CUSTOMER_SUPER_USER })
	@RequestMapping(value = "/saveOrUpdate", method = RequestMethod.POST)
	@ResponseBody
	public BasicSaveResponse save(@RequestBody @Valid StoryNoteModel storyNoteModel)
	{
		storyNoteModel.setUpdatedOn(new Date());
		
		return new BasicSaveResponse(storyNoteService.saveOrUpdate(storyNoteModel).getId());
	}
	
	@ActionName(ACTION_TYPE_READ_ALL_STORY_NOTE_BY_STORY_ID)
	@Authorization(roles = { UserRole.EMPLOYEE_VIEW, UserRole.STORY_NOTE_EDIT, UserRole.CUSTOMER_SUPER_USER })
	@RequestMapping(value = "/readAllNoteByStoryId", method = RequestMethod.GET)
	@ResponseBody
	public BasicReadResponse<List<StoryNoteModel>> fetchAllStoryNotes(@RequestParam(value = "storyId") Long storyId)
	{
		return new BasicReadResponse<List<StoryNoteModel>>(storyNoteService.fetchAllStoryNotes(storyId));
	}
	
	@ActionName(ACTION_TYPE_LATEST_STORY_NOTE_BY_STORY_ID)
	@Authorization(entityIdExpression = "parameters[0]", roles = { UserRole.EMPLOYEE_VIEW, UserRole.STORY_NOTE_VIEW, UserRole.CUSTOMER_SUPER_USER })
	@RequestMapping(value = "/readLatestStoryNoteByStoryId", method = RequestMethod.GET)
	@ResponseBody
	public BasicReadResponse<StoryNoteModel> fetchActiveStoryNote(@RequestParam(value = "storyId") Long storyId)
	{
		return new BasicReadResponse<StoryNoteModel>(storyNoteService.fetchLatestStoryNoteByStoryId(storyId)); 
	}
	
	@ActionName(ACTION_TYPE_DELETE)
	@Authorization(entityIdExpression = "parameters[0]", roles = { UserRole.EMPLOYEE_VIEW, UserRole.STORY_NOTE_VIEW, UserRole.CUSTOMER_SUPER_USER })
	@RequestMapping(value = "/delete/{" + PARAM_ID + "}", method = RequestMethod.DELETE)
	@ResponseBody
	public BaseResponse delete(@PathVariable(PARAM_ID) Long id)
	{
		storyNoteService.deleteById(id);
		
		return new BaseResponse();
	}
}
