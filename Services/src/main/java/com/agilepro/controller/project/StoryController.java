package com.agilepro.controller.project;

import static com.agilepro.commons.IAgileproActions.ACTION_PREFIX_STORY;
import static com.agilepro.commons.IAgileproActions.ACTION_TYPE_DELETE;
import static com.agilepro.commons.IAgileproActions.ACTION_TYPE_DELETE_ALL;
import static com.agilepro.commons.IAgileproActions.ACTION_TYPE_READ;
import static com.agilepro.commons.IAgileproActions.ACTION_TYPE_SAVE_STORIES_IN_BULK;
import static com.agilepro.commons.IAgileproActions.ACTION_TYPE_READ_BACK_LOG_BY_SPRINT_PROJECT_ID;
import static com.agilepro.commons.IAgileproActions.ACTION_TYPE_READ_STORY_BY_PROJECT_IN_PRIORITY_ORDER;
import static com.agilepro.commons.IAgileproActions.ACTION_TYPE_READ_BACK_LOGS_BY_PROJECT_ID;
import static com.agilepro.commons.IAgileproActions.ACTION_TYPE_READ_BY_PROJECT_ID;
import static com.agilepro.commons.IAgileproActions.ACTION_TYPE_SAVE;
import static com.agilepro.commons.IAgileproActions.ACTION_TYPE_UPDATE;
import static com.agilepro.commons.IAgileproActions.ACTION_TYPE_UPDATE_PRIORITY;
import static com.agilepro.commons.IAgileproActions.ACTION_TYPE_SWAP_PRIORITY;
import static com.agilepro.commons.IAgileproActions.ACTION_TYPE_UPDATE_TO_LEAST_PRIORITY;
import static com.agilepro.commons.IAgileproActions.ACTION_TYPE_UPDATE_STORY_STATUS;
import static com.agilepro.commons.IAgileproActions.ACTION_TYPE_UPDATE_STORY_MANAGEMENT;
import static com.agilepro.commons.IAgileproActions.ACTION_TYPE_UPDATE_INPUT_PRIORITY;
import static com.agilepro.commons.IAgileproActions.PARAM_ID;

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

import com.agilepro.commons.StoryResponse;
import com.agilepro.commons.StoryStatus;
import com.agilepro.commons.UserRole;
import com.agilepro.commons.models.project.BacklogStoryModel;
import com.agilepro.commons.models.project.StoriesInBulk;
import com.agilepro.commons.models.project.StoryModel;
import com.agilepro.services.common.Authorization;
import com.agilepro.services.project.StoryService;
import com.yukthi.webutils.annotations.ActionName;
import com.yukthi.webutils.common.models.BaseResponse;
import com.yukthi.webutils.common.models.BasicReadResponse;
import com.yukthi.webutils.common.models.BasicSaveResponse;
import com.yukthi.webutils.controllers.BaseController;

/**
 * The Class StoryController is responsible for receiving the requests from
 * Client. Once received , it directs the request to the service class
 * (StoryService). It also takes care for sending the response back to the
 * client received from service class.
 */
@RestController
@ActionName(ACTION_PREFIX_STORY)
@RequestMapping("/story")
public class StoryController extends BaseController
{
	/**
	 * The story service.
	 **/
	@Autowired
	private StoryService storyService;

	/**
	 * Save new story.
	 * 
	 * @param model for save.
	 * @return save response wrapped with newly saved story id.
	 */
	@ActionName(ACTION_TYPE_SAVE)
	@Authorization(roles = { UserRole.BACKLOG_EDIT, UserRole.EMPLOYEE_VIEW, UserRole.EMPLOYEE_EDIT, UserRole.CUSTOMER_SUPER_USER })
	@RequestMapping(value = "/save", method = RequestMethod.POST)
	@ResponseBody
	public StoryResponse save(@RequestBody @Valid StoryModel model)
	{
		return storyService.saveStory(model);
	}

	/**
	 * Read the Story.
	 *
	 * @param id
	 *            id of StoryModel
	 * 
	 * @return the StoryModel read response
	 */
	@ActionName(ACTION_TYPE_READ)
	@Authorization(entityIdExpression = "parameters[0]", roles = { UserRole.BACKLOG_EDIT, UserRole.EMPLOYEE_VIEW, UserRole.EMPLOYEE_EDIT, UserRole.CUSTOMER_SUPER_USER })
	@RequestMapping(value = "/read/{" + PARAM_ID + "}", method = RequestMethod.GET)
	@ResponseBody
	public BasicReadResponse<StoryModel> read(@PathVariable(PARAM_ID) Long id)
	{
		StoryModel storyModel = storyService.fetchFullModel(id, StoryModel.class);

		return new BasicReadResponse<StoryModel>(storyModel);
	}

	/**
	 * Fetch back logs for the provided project id.
	 * 
	 * @param projectId provided project id.
	 * @return the matching records.
	 */
	@ActionName(ACTION_TYPE_READ_BACK_LOGS_BY_PROJECT_ID)
	@RequestMapping(value = "/fetchBacklogsByProjectId", method = RequestMethod.GET)
	@Authorization(entityIdExpression = "parameters[0]", roles = { UserRole.BACKLOG_EDIT, UserRole.EMPLOYEE_VIEW, UserRole.EMPLOYEE_EDIT, UserRole.CUSTOMER_SUPER_USER })
	@ResponseBody
	public BasicReadResponse<List<BacklogStoryModel>> fetchBacklogs(@RequestParam(value = "projectId") Long projectId)
	{
		return  new BasicReadResponse<List<BacklogStoryModel>>(storyService.fetchBackLogs(projectId));
	}
	
	/**
	 * Fetch story by project id.
	 *
	 * @param projectId
	 *            the project id
	 * @param sprint
	 *            the sprint
	 * @return the basic read response
	 */
	@ActionName(ACTION_TYPE_READ_BACK_LOG_BY_SPRINT_PROJECT_ID)
	@Authorization(entityIdExpression = "parameters[0]", roles = { UserRole.BACKLOG_EDIT, UserRole.EMPLOYEE_VIEW, UserRole.EMPLOYEE_EDIT, UserRole.CUSTOMER_SUPER_USER })
	@RequestMapping(value = "/fetchBacklogBysprintAndProjectId", method = RequestMethod.GET)
	@ResponseBody
	public BasicReadResponse<List<StoryModel>> fetchAllStoryByPrjAndSprint(@RequestParam(value = "projectId", required = true) Long projectId, @RequestParam(value = "sprint", required = true) Long sprint)
	{
		return new BasicReadResponse<List<StoryModel>>(storyService.fetchStoriesForKanban(projectId, sprint));
	}
	
	/**
	 * Fetch story by project id.
	 *
	 * @param projectId
	 *            the project id
	 * @return the basic read response
	 */
	@ActionName(ACTION_TYPE_READ_BY_PROJECT_ID)
	@Authorization(entityIdExpression = "parameters[0]", roles = { UserRole.BACKLOG_EDIT, UserRole.EMPLOYEE_VIEW, UserRole.EMPLOYEE_EDIT, UserRole.CUSTOMER_SUPER_USER })
	@RequestMapping(value = "/readByProjectId", method = RequestMethod.GET)
	@ResponseBody
	public BasicReadResponse<List<StoryModel>> fetchStoryByProjectId(@RequestParam(value = "projectId", required = true) Long projectId)
	{
		return new BasicReadResponse<List<StoryModel>>(storyService.fetchStoriesByProject(projectId));
	}

	/**
	 * Fetch story by priority order for poker game.
	 * 
	 * @param projectId active project id from the drop down.
	 * @return response wrapped with matching results. 
	 */
	@ActionName(ACTION_TYPE_READ_STORY_BY_PROJECT_IN_PRIORITY_ORDER)
	@RequestMapping(value = "/readStoriesByProjectIdInPriorityOrder", method = RequestMethod.GET)
	@Authorization(entityIdExpression = "parameters[0]", roles = { UserRole.BACKLOG_EDIT, UserRole.EMPLOYEE_VIEW, UserRole.EMPLOYEE_EDIT, UserRole.CUSTOMER_SUPER_USER })
	@ResponseBody
	public BasicReadResponse<List<StoryModel>> fetchStoriesInPropertyOrder(@RequestParam(value = "projectId") Long projectId)
	{
		return new BasicReadResponse<List<StoryModel>>(storyService.fetchStoriesByProjectOrderByPriority(projectId));
	}
	
	@ActionName(ACTION_TYPE_UPDATE_PRIORITY)
	@RequestMapping(value = "/updatePriority", method = RequestMethod.GET)
	@Authorization(entityIdExpression = "parameters[0]", roles = { UserRole.BACKLOG_EDIT, UserRole.EMPLOYEE_VIEW, UserRole.EMPLOYEE_EDIT, UserRole.CUSTOMER_SUPER_USER })
	@ResponseBody
	public BaseResponse updatePriority(@RequestParam(value = "id") Long id, @RequestParam(value = "newPriority") Integer newPriority, 
			@RequestParam(value = "projectId") Long projectId)
	{
		storyService.updatePriority(id, newPriority, projectId);
		
		return new BaseResponse();
	}
	
	@ActionName(ACTION_TYPE_UPDATE_INPUT_PRIORITY)
	@RequestMapping(value = "/updateInputPriority", method = RequestMethod.GET)
	@Authorization(entityIdExpression = "parameters[0]", roles = { UserRole.BACKLOG_EDIT, UserRole.EMPLOYEE_VIEW, UserRole.EMPLOYEE_EDIT, UserRole.CUSTOMER_SUPER_USER })
	@ResponseBody
	public BaseResponse updatePriorityAccordingToInput(@RequestParam(value = "id") Long id, @RequestParam(value = "newInputPriority") Integer newInputPriority, 
			@RequestParam(value = "projectId") Long projectId)
	{
		storyService.updatePriorityAccordingToInput(id, newInputPriority, projectId);
		
		return new BaseResponse();
	}
	
	@ActionName(ACTION_TYPE_UPDATE_TO_LEAST_PRIORITY)
	@RequestMapping(value = "/updateToLeastPriority", method = RequestMethod.GET)
	@Authorization(entityIdExpression = "parameters[0]", roles = { UserRole.BACKLOG_EDIT, UserRole.EMPLOYEE_VIEW, UserRole.EMPLOYEE_EDIT, UserRole.CUSTOMER_SUPER_USER })
	@ResponseBody
	public BaseResponse updateToLeastPriority(@RequestParam(value = "id") Long id, @RequestParam(value = "projectId") Long projectId)
	{
		storyService.updateToLeastPriority(id, projectId);
		return new BaseResponse();
	}
	
	@ActionName(ACTION_TYPE_UPDATE_STORY_STATUS)
	@RequestMapping(value = "/updateStoryStatus", method = RequestMethod.GET)
	@Authorization(entityIdExpression = "parameters[0]", roles = { UserRole.BACKLOG_EDIT, UserRole.EMPLOYEE_VIEW, UserRole.EMPLOYEE_EDIT, UserRole.CUSTOMER_SUPER_USER })
	@ResponseBody
	public BaseResponse updateStatus(@RequestParam(value = "id") Long id, @RequestParam(value = "status") StoryStatus status)
	{
		storyService.updateStoryStatus(id, status);
		return new BaseResponse();
	}
	
	@ActionName(ACTION_TYPE_UPDATE_STORY_MANAGEMENT)
	@RequestMapping(value = "/updateStoryManagement", method = RequestMethod.GET)
	@Authorization(entityIdExpression = "parameters[0]", roles = { UserRole.BACKLOG_EDIT, UserRole.EMPLOYEE_VIEW, UserRole.EMPLOYEE_EDIT, UserRole.CUSTOMER_SUPER_USER })
	@ResponseBody
	public BaseResponse updateStoryManagement(@RequestParam(value = "id") Long id, @RequestParam(value = "isManagmentStory") boolean isManagementStory)
	{
		storyService.updateStoryManagement(id, isManagementStory);
		return new BaseResponse();
	}
	
	/**
	 * Swap the priority of stories.
	 * 
	 * @param idToMoveUp provided backlog id to move up.
	 * @param idToMoveDown  provided backlog id to move down.
	 * @return BaseResponse on success update.
	 */
	@ActionName(ACTION_TYPE_SWAP_PRIORITY)
	@RequestMapping(value = "/swapPriority", method = RequestMethod.GET)
	@Authorization(entityIdExpression = "parameters[0]", roles = { UserRole.BACKLOG_EDIT, UserRole.EMPLOYEE_VIEW, UserRole.EMPLOYEE_EDIT, UserRole.CUSTOMER_SUPER_USER })
	@ResponseBody
	public BaseResponse swapPriority(@RequestParam(value = "idToMoveUp")  Long idToMoveUp, @RequestParam(value = "idToMoveDown") Long idToMoveDown)
	{
		storyService.swapPriority(idToMoveUp, idToMoveDown);
		return new BaseResponse();
	}
	
	/**
	 * Update the stories.
	 *
	 * @param model
	 *            id of StoryModel
	 * 
	 * @return the StoryModel response
	 */
	@ActionName(ACTION_TYPE_UPDATE)
	@Authorization(entityIdExpression = "parameters[0].id", roles = { UserRole.BACKLOG_EDIT, UserRole.EMPLOYEE_VIEW, UserRole.EMPLOYEE_EDIT, UserRole.CUSTOMER_SUPER_USER })
	@RequestMapping(value = "/update", method = RequestMethod.POST)
	@ResponseBody
	public BaseResponse update(@RequestBody @Valid StoryModel model)
	{
		storyService.update(model);
		
		return new BaseResponse();
	}

	/**
	 * Save stories in bulk.
	 *
	 * @param model
	 *            the model
	 * @return the basic save response
	 */
	@ActionName(ACTION_TYPE_SAVE_STORIES_IN_BULK)
	@Authorization(roles = { UserRole.BACKLOG_EDIT, UserRole.EMPLOYEE_VIEW, UserRole.EMPLOYEE_EDIT, UserRole.CUSTOMER_SUPER_USER })
	@RequestMapping(value = "/storiesInbulk", method = RequestMethod.POST)
	@ResponseBody
	public BasicSaveResponse saveStoriesInBulk(@RequestBody @Valid StoriesInBulk model)
	{
		storyService.saveListOfStories(model.getStories(), model.getProjectId());

		return new BasicSaveResponse();
	}

	/**
	 * Delete the story.
	 *
	 * @param id
	 *            the id of story
	 * 
	 * @return the story save response
	 */

	@ActionName(ACTION_TYPE_DELETE)
	@Authorization(entityIdExpression = "parameters[0]", roles = { UserRole.BACKLOG_DELETE, UserRole.EMPLOYEE_VIEW, UserRole.CUSTOMER_SUPER_USER })
	@RequestMapping(value = "/delete/{" + PARAM_ID + "}", method = RequestMethod.DELETE)
	@ResponseBody
	public BaseResponse delete(@PathVariable(PARAM_ID) Long id)
	{
		storyService.deleteAndUpdateManagementStory(id);

		return new BaseResponse();
	}

	/**
	 * Delete all story.
	 *
	 * @return the base response
	 */
	@Authorization(roles = { UserRole.TEST_DELETE_ALL, UserRole.CUSTOMER_SUPER_USER })
	@ActionName(ACTION_TYPE_DELETE_ALL)
	@RequestMapping(value = "/deleteAll", method = RequestMethod.DELETE)
	@ResponseBody
	public BaseResponse deleteAll()
	{
		storyService.deleteAll();
		return new BaseResponse();
	}
}
