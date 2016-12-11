$.application.controller('storyController', ["$scope", "crudController", "utils","modelDefService", 
                                             "validator","$state","actionHelper",
       function($scope, crudController, utils, modelDefService, validator, $state, actionHelper) {
	
	 crudController.extend($scope, {
		"name": "Story",
		"modelName": "StoryModel",
		
		"nameColumn" : "title",
		
		"modelDailogId": "storyDialog",
		
		"saveAction": "story.save",
		"readAction": "story.read",
		"updateAction": "story.update",
		"deleteAction": "story.delete",
		
		"onHide" : function(){
		
			$scope.stopInterval();
		},
		
		"onDisplay" : function(model){
			
			$scope.initTinyMce();
			
			if(!(model.id))
			{
				$scope.conversationTab = false;
			}
			else
			{
				$scope.message = "";
				$scope.conversationTab = true;
				$scope.attachmentTab = true;
				
				// logic for adjust height as extension value can be added
				var modelFormElem = angular.element('#modelFormId'); 
				
				var panelBodyElem = angular.element('#panelBodyId');
				
				var conversationHeight = modelFormElem.height();

				panelBodyElem.css('height', conversationHeight + 'px');
				
				$scope.storyId = model.id;

				$scope.getAllProjectMembers();
				
				$scope.selectedTitle = {};
				$scope.titles = [];
				
				$scope.getAllTitle();
				
				$scope.getAllAttachment();
			}
			
			// Broad cast 
	    	$scope.$broadcast("fetchAllStoryNotes");
		}
		
	});
	
	 
	 $scope.storyViewTab = {active: true, color: "blueBackGround"};
	 $scope.dependencyViewTab = {active: false, color: "greyBackGround"};
	 $scope.priorityViewTab = {active: false, color: "greyBackGround"};
	 

	 	/**
		 * Set the active tab.
		 */
		$scope.setActiveTab = function(tabName){
			
			switch(tabName)
			{
				case "STORY_VIEW":
				{
					$scope.storyViewTab = {active: true, color: "blueBackGround"};
					$scope.dependencyViewTab = {active: false, color: "greyBackGround"};
					$scope.priorityViewTab = {active: false, color: "greyBackGround"};
					break;
				}
				case "DEPENDENCY_VIEW":
				{
					$scope.storyViewTab = {active: false, color: "greyBackGround"};
					$scope.dependencyViewTab = {active: true, color: "blueBackGround"};
					$scope.priorityViewTab = {active: false, color: "greyBackGround"};
					break;
				}
				case "PRIORITY_VIEW":
				{
					$scope.storyViewTab = {active: false, color: "greyBackGround"};
					$scope.dependencyViewTab = {active: false, color: "greyBackGround"};
					$scope.priorityViewTab = {active: true, color: "blueBackGround"};
				}
			}
			
		};
		
	 
	 /**
	  * Fetch back logs according to the project id. 
	  */
	 $scope.initStory = function(){
		 
		console.log("init story is called");
		
		$scope.idToBaclokgItem = {};
		$scope.hierarchyList = [];
		$scope.epicStoryList = [];
		$scope.childStories = [];
		$scope.parentIdChildListMap = {};
		$scope.indentPosition = 0;
		  
		$scope.finalResult = [];
		
		var activeProjectId = $scope.getActiveProjectId()
		 
		if(activeProjectId != -1)
		{
			 actionHelper.invokeAction("story.fetchBacklogsByProjectId", null, {"projectId" : activeProjectId},
					 function(readResponse, respConfig)
					 {
				 		$scope.backLogs = readResponse.model;
				 		console.log("working");
				 		
				 		if($scope.backLogs)
				 		{
				 			$scope.loadBacklogItems($scope.backLogs);
				 		}
				 			
				 		try
						{
							$scope.$apply();
						}catch(ex)
						{}
				 
					 },{"hideInProgress" : true}
			 );
		}
	 };
	 
	 $scope.loadBacklogItems = function(backlogItems) {
		  
		  var backlog;
		  for(var index = 0; index < backlogItems.length; index++)
			{
			  backlog = backlogItems[index];
			  
			  $scope.idToBaclokgItem[backlog.id] = backlog;
			  
			  if(!backlog.parentStoryId)
			  {
				  $scope.epicStoryList.push(backlog);
			  }else
			  {
				  $scope.childStories.push(backlog);
			  }
			}
		  
		  // sorting
		  if($scope.epicStoryList.length > 0)
			{
			  $scope.epicStoryList.sort(function(a, b){return a.id-b.id});
			}
		  
		  // get the map of child stories
		  var childBackLog;
		  for(var index = 0; index < $scope.childStories.length; index++)
			{
			  	childBackLog = $scope.childStories[index];
			  	
			  	var childArr = $scope.parentIdChildListMap[childBackLog.parentStoryId];
			  	
			  	if(childArr && (childArr.indexOf(childBackLog) == -1))
				  	{
			  			childArr.push(childBackLog);
			  		
				  	}else
				  	{
				  		childArr = [childBackLog];
				  	}
			}
		  
		  $scope.addBackLogsAccordingToChild($scope.epicStoryList, 0);
		  
		  console.log($scope.finalResult);
	};
	 
	/**
	 * Recursion wise adding the stories.
	 */
	$scope.addBackLogsAccordingToChild = function(backLogArr, indentValue){
		
		 var backLog;
		 for(var i = 0; i < backLogArr.length; i++)
		  {
			  backLog = backLogArr[i];
			  backLog["indent"] = indentValue;
			  
			  $scope.finalResult.push(backLog);
			  
			  if($scope.parentIdChildListMap[backLog.id])
				{
				  $scope.addBackLogsAccordingToChild($scope.parentIdChildListMap[backLog.id], indentValue + 1);
				}
		  }
	};
	
	
	/**
	 * Added new backlog after save. 
	 */
	$scope.addBacklog = function(backlogModel){
		
		$scope.finalResult.push(backlogModel);
		
		$scope.epicStoryList.push(backlogModel);
	};
	
	/**
	 * Displays bulk story dialog.
	 */
	$scope.openBulkStories = function() {
		utils.openModal("bulkStoryDialog", {});
		
	};
	
	// Listener for broadcast
	$scope.$on("activeProjectSelectionChanged", function(event, args) {
		
		$scope.initStory();
		//$scope.$broadcast("rowsModified");
	});
	
}]);


