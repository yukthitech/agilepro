$.application.controller('taskController', ["$scope", "crudController", "utils", "actionHelper",
                                            function($scope, crudController, utils, actionHelper) {
	crudController.extend($scope, {
		"name": "Task",
		"modelName": "TaskModel",
		
		"nameColumn" : "title",
		
		"modelDailogId": "taskDialog",
		
		"saveAction": "task.save",
		"readAction": "task.read",
		"updateAction": "task.update",
		"deleteAction": "task.delete",
	});

	/**
	 * Gets invoked by the angular js filter.
	 * 
	 * Default story filter by story title.
	 */
	$scope.storyBugFilter = function(){
		
		var retFunc = function(item){
				
			if(!$scope.storySearch)
			{
				return true;
			}
			
			var searchString = $scope.storySearch.toLowerCase();

			return item.title.toLowerCase().includes(searchString);
		};
		
		if($scope.oldSearchStory == $scope.storySearch)
		{
			return retFunc;
		}
				
		$scope.oldSearchStory = $scope.storySearch;

		return retFunc;
	};
	
	/**
	 * Filter story by status.
	 */
	$scope.commonFilterStory = function(){
		
		var storyStatusName = $scope.getSelectedStoryStatus();
		var ownerObj = $scope.getSelectedOwner();
		
		for(var i = 0 ; i < $scope.itemsFortask.length ; i++)
		{
			var storyObj = $scope.itemsFortask[i];
			
			if(ownerObj)
			{
				if((storyObj.ownerId == ownerObj.id) || (ownerObj.id == 0 && storyObj.ownerId))
				{
					storyObj.display = true;
				}else
				{
					storyObj.display = false;
					continue;
				}
			}
		
			if(storyStatusName)
			{
				if((storyStatusName == "All" || !storyStatusName)|| (storyStatusName == "Completed" && storyObj.status == "COMPLETED")|| (storyStatusName == "Not Completed" && storyObj.status != "COMPLETED"))
				{
					storyObj.display = true;
				}else
				{
					storyObj.display = false;
				}
			}
			
		}
	};
	
	/**
	 * Fetch stories by sprint
	 */
	$scope.fetchStoriesAndBugBySprint = function(){
		
		$scope.itemsFortask = [];
		$scope.idToTask = {};
		
		if(!$scope.getSelectedSprint())
		{
			return;
		}
		
		actionHelper.invokeAction("storyAndBug.readStoriesAndBugBySprint", null, {"sprintId" : $scope.getSelectedSprint().id},
			function(readResponse, respConfig)
			{
				$scope.storyModels = readResponse.model.storyModels;
				$scope.bugModels  = readResponse.model.bugModels;
				
				$scope.itemsFortask = $scope.storyModels.concat($scope.bugModels);
				
				var idToStory = {};
				var idToBug = {};
				var storyIdsInSprint = [];
				
				if($scope.itemsFortask)
				{
					for(var i = 0 ; i < $scope.itemsFortask.length ; i++)
					{
						var obj = $scope.itemsFortask[i];
						obj.display = true;
						obj.childrens = [];
						
						if(obj.isBug)
						{
							idToBug[obj.id] = obj;
						}else
						{
							idToStory[obj.id] = obj;
							storyIdsInSprint.push(obj.id);
						}
					}
				}
				
				$scope.addFetchedStoryItemsToParent(idToBug, idToStory, storyIdsInSprint);
				
				try
				{
					$scope.$apply();
				}catch(ex)
				{}
				
			}, {"hideInProgress" : true});	
	};
	
	/**
	 * Fetch the story info and open the modal.
	 */
	$scope.openStoryNoteModal = function(storyId){
		
		$scope.storyForUpdate = $scope.idToStory[storyId];
		
		actionHelper.invokeAction("storyNote.readLatestStoryNoteByStoryId", null, {"storyId" : storyId}, 
				function(readResponse, respConfig)
				{
					if(readResponse.code == 0)
					{
						$("#storyNoteModal").modal("show");
						$scope.storyNote = readResponse.model;
						
						if(!$scope.storyNote)
						{
							$scope.message = "Currently there is no note for " + $scope.storyForUpdate.title;
						}else
						{
							$scope.message = "";
						}
						
						try
						{
							$scope.$apply();
						}catch(ex)
						{}
					}
					
				}, {"hideInProgress" : true});
	};
	
	/**
	 * Open story edit modal.
	 */
	$scope.openStoryEditModal = function(storyId){
		
		$scope.$broadcast("editStory",storyId);
	};

	// TASK
	
	/**
	 * On click plus.
	 */
	$scope.onClickPlus = function(indexInTask){
		
		var obj = $scope.itemsFortask[indexInTask];

		//debugger;
		
		if(!$scope.previousClickedIndex || $scope.previousClickedIndex == indexInTask)
		{
			obj.expanded = !obj.expanded;
			console.log($scope.previousClickedIndex);
			console.log(obj.title + " = " + obj.expanded);
		}
		else if($scope.previousClickedIndex && ($scope.previousClickedIndex != indexInTask))
		{
			var previousObj = $scope.itemsFortask[$scope.previousClickedIndex];
			
			previousObj.expanded = false;
			console.log(previousObj.title + " is closed");
			obj.expanded = true;
		}
		
		$scope.previousClickedIndex = indexInTask;
		
		if(obj.expanded)
		{
			if(obj.isBug)
			{
				
			}else
			{
				//$scope.fetchTaskByStory(obj);
			}
		}
	};
	
	/**
	 * Fetch task by stories.
	 */
	$scope.fetchTaskByStory = function(storyObj){
		
		actionHelper.invokeAction("task.readByStoryId", null, {"storyId" : storyObj.id}, 
				function(readResponse, respConfig)
				{
					if(readResponse.code == 0)
					{
						storyObj.tasks = readResponse.model;
						
						$scope.expandedStoryId = storyObj.id;
						
						for(var i = 0 ; i < storyObj.tasks.length ; i++)
						{
							var taskObj = storyObj.tasks[i];
							
							$scope.idToTask[taskObj.id] = taskObj;
						}
						
						$scope.taskChanges = {};
						$scope.onTypeTargets = [];
						$scope.taskError = {"show" : false, "message" : ""};
						
						try
						{
							$scope.$apply();
						}catch(ex)
						{}
					}
					
				}, {"hideInProgress" : true});
	};
	
	/**
	 * Gets invoked on click of add button. 
	 */
	$scope.addNewTask = function(taskTitle, estimateTime, storyId){

		if(!taskTitle)
		{
			$scope.taskError.show = true;
			$scope.taskError.message = "Please provide a title";
			return;
		}

		if(!estimateTime || (estimateTime.trim()).length == 0)
		{
			$scope.taskError.show = true;
			$scope.taskError.message = "Please provide the estimated time";
			return;
		}
		
		//estimateTime = toInt(estimateTime);
		
		if (estimateTime <= 0)
		{
			console.log(estimateTime);
			
			$scope.taskError.show = true;
			$scope.taskError.message = "Please provide estimated time greater than 0";
			return;
		}
		
		$scope.taskError.show = false;
		
		if(taskTitle && estimateTime)
		{
			$scope.saveNewTask(taskTitle, estimateTime, storyId);
		}
	};
	
	/**
	 * Save new task uses action helper to call the controller.
	 */
	$scope.saveNewTask = function(taskTitle, estimateTime, storyId){
		
		var model = {"title" : taskTitle, 
					 "estimateTime" : estimateTime, 
					 "storyId" : $scope.expandedStoryId, 
					 "projectId" : $scope.getActiveProjectId(),
					 "status" : "NOT_STARTED",
					 "actualTimeTaken" : 0};
		
		actionHelper.invokeAction("storyTask.save", model, null, 
				function(saveResponse, respConfig)
				{
					if(saveResponse.code == 0)
					{
						model.id = saveResponse.id;
						
						$scope.idToStory[storyId].tasks.push(model);
						
						$scope.idToTask[model.id] = model;
						
						$("#newTaskTitle_" + storyId).focus();
						$("#newTaskTitle_" + storyId).val("");
						$("#estimateTime_" + storyId).val("");

						try
						{
							$scope.$apply();
						}catch(ex)
						{}
					}
				}, {"hideInProgress" : true});
	};
	
	$scope.updateBugStatus(id, status);

	/**
	 * Update story status, calls the controller.
	 */
	$scope.updateStoryStatus = function(storyId, status){
		
		actionHelper.invokeAction("story.updateStoryStatus", null, {"id" : storyId, "status" : status},
				function(updateResponse, respConfig)
				{
					if(updateResponse.code == 0)
					{
						$scope.idToStory[storyId].status = status;
					}
					
					if(status == "COMPLETED")
					{
						var taskArr = $scope.idToStory[storyId].tasks;
						
						for(index in taskArr)
						{
							taskArr[index].status = status;
						}
					}
					
					try
					{
						$scope.$apply();
					}catch(ex)
					{}
					
				}, {"hideInProgress" : true});
	};

	
	/**
	 * Update story status, calls the controller.
	 */
	$scope.updateStoryStatus = function(storyId, status){
		
		actionHelper.invokeAction("story.updateStoryStatus", null, {"id" : storyId, "status" : status},
				function(updateResponse, respConfig)
				{
					if(updateResponse.code == 0)
					{
						$scope.idToStory[storyId].status = status;
					}
					
					if(status == "COMPLETED")
					{
						var taskArr = $scope.idToStory[storyId].tasks;
						
						for(index in taskArr)
						{
							taskArr[index].status = status;
						}
					}
					
					try
					{
						$scope.$apply();
					}catch(ex)
					{}
					
				}, {"hideInProgress" : true});
	};
	
	/**
	 * On change of story status.
	 */
	$scope.onStatusChange = function(id, updateItemIsBug, status){
		
		console.log("$scope.onStatusChange");
		
		if(updateItemIsBug)
		{
			$scope.updateBugStatus(id, status);
		}else
		{
			$scope.updateStoryStatus(id, status);
		}
	};
	
	// DRAG AND DROP METHODS.
	
	/**
	 * Drag backlogs
	 */
	$scope.dragStoryBugFromSprint = function(event){
		
		event.originalEvent.dataTransfer.setData('text/plain', 'text');
		
		var arrElem = (event.target.id).split('_');
		
		var draggingItemIsBug = (arrElem[1] == "true");
		var draggingId = Number(arrElem[2]);
		
		$scope.onDragOfItemFromSprintToBacklog(draggingItemIsBug, draggingId);
		
		/*
		if($scope.multipleBacklogIds.indexOf($scope.draggingId) == -1)
		{
			$scope.multipleBacklogIds.push($scope.draggingId);
		}
		
		$('#dropStoryForBacklogId').css("border", "3px solid #66c2ff");
		$('#searchBacklogInputId').css("border-bottom", "3px solid #66c2ff");
		$('#dropStoryForBacklogId').css('box-shadow', "5px 5px 5px #888888");
		
		$scope.allowedFromStoryToBacklog = true;
		$scope.allowedFromBacklogToStory = false;*/
	};
	
	/**
	 * Recursive method for adding the child stories.
	 */
	$scope.addChildIdsForDrag = function(childArr){
		
		var multipleCheckedStoryIds = $scope.getMultipleCheckedStoryIds();
		var storyIdsInBacklog = $scope.getStoryIdsInBacklog();
		
		for(var i = 0 ;i < childArr.length ; i++)
		{
			var childObj = childArr[i];
			
			if( (multipleCheckedStoryIds.indexOf(childObj.id) == -1) && (storyIdsInBacklog.indexOf(childObj.id) != -1) )
			{
				$scope.childIdsFromBacklog.push(childObj.id);
			}
			
			var childrens = $scope.idToBacklogStory[childObj.id].childrens;
			
			if(childrens.length > 0)
			{
				$scope.addChildIdsForDrag(childrens);
			}
		}
		
	};
	
	/**
	 * On drop of backlog.
	 */
	$scope.onDropOfBacklog = function(event){
		
		event.preventDefault();
	
		if($scope.getAllowedFromBacklogToStory())
		{
			$scope.childIdsFromBacklog = [];
			
			var multipleCheckedStoryIds = $scope.getMultipleCheckedStoryIds();
			
			// check for childs in case of dragging single item
			if((multipleCheckedStoryIds.length == 1))
			{
				var parent = $scope.getBacklogStory(multipleCheckedStoryIds[0]);
				
				if(parent)
				{
					var childArr = parent.childrens;
					
					if(childArr.length > 0)
					{
						$scope.addChildIdsForDrag(childArr);
					}
				}
			}
			
			var proceed = true;
			
			if($scope.childIdsFromBacklog.length > 0)
			{
				proceed = false;
				
				var deleteOp = $.proxy(function(confirmed) {
					
					if(!confirmed)
					{
						return;
					}else
					{
						$scope.forSuccessDropOfBacklog();
					}
					
				}, {"$scope": $scope, "proceed": proceed});

				
				utils.confirm(["Are you sure you want drag all the childrens of - '{}'", $scope.idToBacklogStory[$scope.draggingId].title], deleteOp);
			}
			
			if(proceed)
			{
				$scope.forSuccessDropOfBacklog();
			}
			
		}
	};

	
	/**
	 * Gets invoked for success drop of backlog
	 */
	$scope.forSuccessDropOfBacklog = function(){
		
		var sprintObj = $scope.getSelectedSprint();
		
		var multipleCheckedBugIds = $scope.getMultipleCheckedBugIds();
		var multipleCheckedStoryIds = $scope.getMultipleCheckedStoryIds();
		var draggingId = $scope.getDraggingId();
		var draggingItemIsBug = $scope.getDraggingItem();
		
		if((multipleCheckedBugIds.length > 0) || (multipleCheckedStoryIds.length > 0) && sprintObj) 
		{
			for(var i = 0 ; i <  $scope.childIdsFromBacklog.length ; i++)
			{
				var childId = $scope.childIdsFromBacklog[i];
				
				if(multipleCheckedStoryIds.indexOf(childId) == -1)
				{
					multipleCheckedStoryIds.push(childId);
				}
			}
			
			$scope.updateStorySprint(sprintObj.id, multipleCheckedBugIds, multipleCheckedStoryIds);
		}
		else if(draggingId && sprintObj)
		{
			if(draggingItemIsBug)
			{
				multipleCheckedBugIds = [draggingId];
			}else
			{
				multipleCheckedStoryIds = [draggingId];
			}
			
			$scope.updateStorySprint(sprintObj.id, multipleCheckedBugIds, multipleCheckedStoryIds);
		}
	};
	
	/**
	 * Add items for task.
	 */
	$scope.addToItemsFortask = function(multipleBugIds, multipleStoryIds, sprintId){
		
		var storyIdsInSprint = $scope.getStoryIdsInSprint();
		
		for(var i = 0 ;i < multipleBugIds.length ; i++)
		{
			var obj = $scope.getBacklogBug(multipleBugIds[i]);
			
			if(obj)
			{
				obj.display = true;
				obj.sprintId = sprintId;
				
				$scope.setSprintBug(obj.id, obj);
				storyIdsInSprint.push(obj.id);
				
				$scope.itemsFortask.push(obj);
			}
		}
		
		for(var i = 0 ;i < multipleStoryIds.length ; i++)
		{
			var obj = $scope.getBacklogStory(multipleStoryIds[i]);
			
			if(obj)
			{
				obj.display = true;
				obj.sprintId = sprintId;
				
				$scope.setSprintStory(obj.id, obj);
				storyIdsInSprint.push(obj.id);
				
				$scope.itemsFortask.push(obj);
			}
		}
		
		try
		{
			$scope.$apply();
		}catch(ex)
		{}
	};
	
	/**
	 * Common method for updating the sprint.
	 */
	$scope.updateStorySprint = function(sprintId, multipleBugIds, multipleStoryIds){
		
		actionHelper.invokeAction("storyAndBug.updateStorySprintBugSprint", 
					{"multipleBugIds" : multipleBugIds, "multipleStoryIds" : multipleStoryIds, "sprintId" : sprintId}, null,
				function(updateResponse, respConfig)
				{
					if(updateResponse.code == 0)
					{
						$scope.addToItemsFortask(multipleBugIds, multipleStoryIds, sprintId);
						
						$scope.reArrangeTheBacklogItems(multipleBugIds, multipleStoryIds, sprintId);
						
					}else
					{
						utils.alert("Error in dragging");
					}

				}, {"hideInProgress" : true});
	};
	
	// Listener for broadcast
	$scope.$on("reArrangeSprintItems", function(event, args) {
		
		var draggingItemIsBug = args.draggingItemIsBug;
		var draggingId = args.draggingId;
		
		if(!draggingItemIsBug)
		{
			var storyIdsInSprint = $scope.getStoryIdsInSprint();
			
			storyIdsInSprint.splice(storyIdsInSprint.indexOf(draggingId), 1);
		}
		
		var indexForRemove = -1;
		
		for(var i = 0 ; i < $scope.itemsFortask.length ; i++)
		{
			var obj = $scope.itemsFortask[i];

			if(draggingItemIsBug && obj.id == draggingId && obj.isBug)
			{
				indexForRemove = i;
				break;
			}
			else if(!draggingItemIsBug && obj.id == draggingId && !obj.isBug)
			{
				indexForRemove = i;
				break;
			}
		}
		
		if(indexForRemove >= 0)
		{
			$scope.itemsFortask.splice(indexForRemove, 1);
		}
		
		try
		{
			$scope.$apply();
		}catch(ex)
		{}
		
	});
	
	// Listener for broadcast
	$scope.$on("activeSprintSelectionChanged", function(event, args) {
		
		$scope.fetchStoriesAndBugBySprint();
	});
	
	// Listener for broadcast
	$scope.$on("activeOwnerSelectionChanged", function(event, args) {
		
		$scope.commonFilterStory();
	});
	
	// Listener for broadcast
	$scope.$on("activeStoryStatusSelectionChanged", function(event, args) {
		
		$scope.commonFilterStory();
	});
	
}]);