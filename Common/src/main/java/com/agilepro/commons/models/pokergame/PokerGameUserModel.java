package com.agilepro.commons.models.pokergame;

import com.yukthi.validation.annotations.Required;
import com.yukthi.webutils.common.AbstractExtendableModel;
import com.yukthi.webutils.common.annotations.ExtendableModel;
import com.yukthi.webutils.common.annotations.Model;
import com.yukthi.webutils.common.annotations.NonDisplayable;

/**
 * The Class PokerGameUserModel.
 */
@ExtendableModel(name = "PokerGameUser")
@Model(name = "PokerGameUser")
public class PokerGameUserModel extends AbstractExtendableModel
{
	/**
	 * The id.
	 **/
	@NonDisplayable
	private Long id;

	/**
	 * The version.
	 **/
	@NonDisplayable
	private Integer version;

	/**
	 * The members.
	 **/
	private Long projectMemberId;

	/**
	 * The poker game.
	 **/
	@Required
	private Long pokerGameId;

	/**
	 * The Project id for fetching the member id.
	 */
	@Required
	private Long projectId;

	/**
	 * The user id for fetching the member id.
	 */
	@Required
	private Long userId;

	/**
	 * Card value selected by the user.
	 */
	private Float cardValue;

	/**
	 * Card value to display in UI.
	 */
	private String cardValueDisplay;
	
	/**
	 * Project member name.
	 */
	private String projectMemberName;

	/**
	 * Poker Game User no argument constructor.
	 */
	public PokerGameUserModel()
	{
		super();
	}

	/**
	 * Poker Game user.
	 * 
	 * @param pokerGameId
	 *            new poker game id.
	 * @param projectId
	 *            new project id.
	 * @param userId
	 *            new user id.
	 */
	public PokerGameUserModel(Long pokerGameId, Long projectId, Long userId)
	{
		super();
		this.pokerGameId = pokerGameId;
		this.projectId = projectId;
		this.userId = userId;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.yukthi.webutils.common.IExtendableModel#getId()
	 */
	public Long getId()
	{
		return id;
	}

	/**
	 * Sets the id.
	 *
	 * @param id
	 *            the new id
	 */
	public void setId(Long id)
	{
		this.id = id;
	}

	/**
	 * Gets the version.
	 *
	 * @return the version
	 */
	public Integer getVersion()
	{
		return version;
	}

	/**
	 * Sets the version.
	 *
	 * @param version
	 *            the new version
	 */
	public void setVersion(Integer version)
	{
		this.version = version;
	}

	/**
	 * Gets the project member id.
	 * 
	 * @return project member id.
	 */
	public Long getProjectMemberId()
	{
		return projectMemberId;
	}

	/**
	 * Sets the project member id.
	 * 
	 * @param projectMemberId
	 *            the new project member id.
	 */
	public void setProjectMemberId(Long projectMemberId)
	{
		this.projectMemberId = projectMemberId;
	}

	/**
	 * Gets the poker game id.
	 * 
	 * @return the poker game id.
	 */
	public Long getPokerGameId()
	{
		return pokerGameId;
	}

	/**
	 * Sets the poker game id.
	 * 
	 * @param pokerGameId
	 *            the new poker game id.
	 */
	public void setPokerGameId(Long pokerGameId)
	{
		this.pokerGameId = pokerGameId;
	}

	/**
	 * Gets the card value.
	 * 
	 * @return the card value.
	 */
	public Float getCardValue()
	{
		return cardValue;
	}

	/**
	 * Sets the card value.
	 * 
	 * @param cardValue
	 *            the new card value.
	 */
	public void setCardValue(Float cardValue)
	{
		this.cardValue = cardValue;
	}

	/**
	 * Gets the project id.
	 * 
	 * @return the project id for fetching the project member id..
	 */
	public Long getProjectId()
	{
		return projectId;
	}

	/**
	 * Set the project id.
	 * 
	 * @param projectId
	 *            the new project id for fetching the project member id..
	 */
	public void setProjectId(Long projectId)
	{
		this.projectId = projectId;
	}

	/**
	 * Gets the user id.
	 * 
	 * @return the user id for fetching the project member id.
	 */
	public Long getUserId()
	{
		return userId;
	}

	/**
	 * Set the user id.
	 * 
	 * @param userId
	 *            the new user id for fetching the project member id.
	 */
	public void setUserId(Long userId)
	{
		this.userId = userId;
	}

	/**
	 * Gets the card value display.
	 * 
	 * @return the card value display.
	 */
	public String getCardValueDisplay()
	{
		return cardValueDisplay;
	}

	/**
	 * Set the card value display.
	 * 
	 * @param cardValueDisplay
	 *            the new card value display.
	 */
	public void setCardValueDisplay(String cardValueDisplay)
	{
		this.cardValueDisplay = cardValueDisplay;
	}
	
	/**
	 * Gets the project member name.
	 * 
	 * @return the project member name.
	 */
	public String getProjectMemberName()
	{
		return projectMemberName;
	}

	/**
	 * Sets the project member name.
	 * 
	 * @param projectMemberName
	 *            the new project member name.
	 */
	public void setProjectMemberName(String projectMemberName)
	{
		this.projectMemberName = projectMemberName;
	}
}
