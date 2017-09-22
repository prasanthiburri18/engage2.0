package com.engage.controller;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.validation.ConstraintViolation;
import javax.validation.Validator;

import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.engage.commons.exception.ConstraintViolationException;
import com.engage.commons.util.HtmlEscapeUtil;
import com.engage.commons.validators.utils.ConstraintValidationUtils;
import com.engage.dao.BlocksDao;
import com.engage.model.Blocks;
import com.engage.util.JsonMessage;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;

/**
 * Pathway MicroServices for Blocks API Operations
 * 
 * @author StartUP Labs
 * @version 1.0
 * 
 */
@RestController
@RequestMapping(value = "/api/v1")
public class BlocksController {
	private static final int ArraysList = 0;
	private static Logger log = LoggerFactory.getLogger(BlocksController.class);
	@Autowired
	private BlocksDao _blocksDao;

	@Autowired
	private Validator validator;

	/**
	 * Adding Pathway Block by getting Block Object
	 * 
	 * @Inputparam pathwayBlock JsonObject
	 * @return JsonObject
	 */

	@RequestMapping(value = "/addBlock", method = RequestMethod.POST)
	public @ResponseBody JsonMessage addBlock(@RequestBody Blocks pathwayBlock) {
		JsonMessage response = new JsonMessage();
		try {
			// Engage2.0 start

			Set<ConstraintViolation<Blocks>> violations = validator.validate(pathwayBlock);
			if (!violations.isEmpty()) {
				Map<String, String> errormessages = ConstraintValidationUtils.getMapOfValidations(violations);
				JSONObject json = new JSONObject(errormessages);
				throw new ConstraintViolationException(json.toString());
			}
			// Engage2.0 end

			Boolean isPathway = _blocksDao.getByBlockName(pathwayBlock.getBlockName(), pathwayBlock.getEventId());
			if (isPathway == true) {
				response.setMessage("This block already exists");
				response.setStatuscode(208);
				return response;
			} else {
				// Engage2.0
				//Not verifying for null check as it already passed above validations
				pathwayBlock = sanitizeMessagesOfBlock(pathwayBlock);
				// Engage2.0
				Long id = _blocksDao.save(pathwayBlock);
				response.setMessage("Block has been created successfully.");
				response.setData(id);
				response.setStatuscode(200);
				return response;
			}

		} catch (ConstraintViolationException ex) {
			response.setMessage(ex.getMessage());
			response.setStatuscode(204);
			return response;
		} catch (Exception ex) {
			response.setMessage("Block not registered.");
			response.setStatuscode(204);
			return response;
		}
	}

	/**
	 * Delete Pathway Block by Block Id
	 * 
	 * @Inputparam PathwayBlockjsonObject
	 * @return JsonObject
	 */

	@RequestMapping(value = "/deleteBlock", method = RequestMethod.DELETE)
	public @ResponseBody JsonMessage deleteBlock(@RequestBody Map<String, String> json) {
		JsonMessage response = new JsonMessage();
		try {
			Blocks blocks = _blocksDao.getById(Long.parseLong(json.get("id")));
			if (blocks.getId() > 0) {
				_blocksDao.delete(blocks);
				response.setMessage("Block deleted successfully.");
				response.setStatuscode(200);
				return response;
			} else {
				response.setMessage("Block not registered.");
				response.setStatuscode(204);
				return response;
			}
		} catch (Exception e) {
			response.setMessage("Block not registered.");
			response.setStatuscode(204);
			return response;
		}

	}

	/**
	 * Update Pathway Block by PathwayId
	 * 
	 * @Inputparam Block JsonObject
	 * @return JsonObject
	 */
	@RequestMapping(value = "/updateBlock", method = RequestMethod.PUT)
	public @ResponseBody JsonMessage updateBlock(@RequestBody Blocks pathway) {
		JsonMessage response = new JsonMessage();
		try { // Engage2.0 start

			Set<ConstraintViolation<Blocks>> violations = validator.validate(pathway);
			if (!violations.isEmpty()) {
				Map<String, String> errormessages = ConstraintValidationUtils.getMapOfValidations(violations);
				JSONObject json = new JSONObject(errormessages);
				throw new ConstraintViolationException(json.toString());
			}
			
			pathway = sanitizeMessagesOfBlock(pathway);
			// Engage2.0 end

			_blocksDao.update(pathway);
			response.setMessage("Block updated successfully");
			response.setStatuscode(200);
			return response;

		}
		// Added to publish json error message to client
		catch (ConstraintViolationException ex) {
			response.setMessage(ex.getMessage());
			response.setStatuscode(204);
			return response;
		} catch (Exception ex) {
			response.setMessage("Block not registered");
			response.setStatuscode(204);
			return response;
		}

	}

	/**
	 * Loading Pathway Block List By PathwayId
	 * 
	 * @Inputparam Block JsonObject
	 * @return Blocklist Json Object
	 */

	@RequestMapping(value = "/listBlocks", method = RequestMethod.POST)
	public @ResponseBody JsonMessage listBlocks(@RequestBody Map<String, String> json) {
		JsonMessage response = new JsonMessage();
		try {

			List<Blocks> blocks = _blocksDao.verifyId(Long.parseLong(json.get("pathwayId")));
			if(blocks!=null && blocks.size()>0){
				for(Blocks block: blocks){
					desanitizeMessagesOfBlock(block);
				}
			}
			response.setMessage("Blocks list.");
			response.setData(blocks);
			response.setStatuscode(200);
			return response;
		} catch (Exception ex) {
			response.setMessage(ex.getMessage());
			response.setStatuscode(203);
			return response;
		}

	}

	/**
	 * Patient Child Block Update Operation
	 * 
	 * @Inputparam BlockjsonObject
	 * @return JsonObject
	 */
	@RequestMapping(value = "/updateparentchildblocks", method = RequestMethod.POST)
	public @ResponseBody JsonMessage updateparentchildblocks(@RequestBody Map<String, String> json) {
		JsonMessage response = new JsonMessage();
		try {

			Long rid = Long.parseLong(json.get("rid"));
			Long parentbid = Long.parseLong(json.get("parentblockid"));
			Long brow = Long.parseLong(json.get("brow"));
			Long bcol = Long.parseLong(json.get("bcol"));

			String msentat = (json.get("msentat"));
			String aptdate = (json.get("aptdate"));

			Integer results = _blocksDao.updatePatientappointmentblock(rid, parentbid, brow, bcol, msentat, aptdate);

			response.setMessage("Child Blocks list.");
			response.setData(results);
			response.setStatuscode(200);
			return response;
		} catch (Exception ex) {
			response.setMessage(ex.getMessage());
			response.setStatuscode(203);
			return response;
		}

	}

	/**
	 * Listing Child Block by Mater Parent Id
	 * 
	 * @Inputparam jsonObject
	 * @return JsonObject
	 */
	@RequestMapping(value = "/listBlocksByParent", method = RequestMethod.POST)
	public @ResponseBody JsonMessage listBlocksByParent(@RequestBody Map<String, String> json) {
		JsonMessage response = new JsonMessage();
		try {

			Long parentbid = Long.parseLong(json.get("parentblockid"));
			List<Object> blocks = _blocksDao.getChildBlockdata(parentbid);
//Desantization done at dao layer
			response.setMessage("Child Blocks list.");
			response.setData(blocks);
			response.setStatuscode(200);
			return response;
		} catch (Exception ex) {
			response.setMessage(ex.getMessage());
			response.setStatuscode(203);
			return response;
		}

	}

	/**
	 * This method is used for escaping html characters in Blocks - body,
	 * subject, follow up and reminder
	 * 
	 * @param pathwayBlock
	 * @return
	 * @throws JsonParseException
	 * @throws JsonMappingException
	 * @throws IOException
	 */
	private Blocks sanitizeMessagesOfBlock(Blocks pathwayBlock)
			throws JsonParseException, JsonMappingException, IOException {
		if (pathwayBlock != null) {
			pathwayBlock.setBodyOfMessage(HtmlEscapeUtil.escapeHtml(pathwayBlock.getBodyOfMessage()));
			pathwayBlock.setSubjectOfMessage(HtmlEscapeUtil.escapeHtml(pathwayBlock.getSubjectOfMessage()));
			pathwayBlock.setRemainderOfMessage(HtmlEscapeUtil.escapeHtml(pathwayBlock.getRemainderOfMessage()));
			pathwayBlock.setfollowupOfMessage(HtmlEscapeUtil.escapeHtml(pathwayBlock.getfollowupOfMessage()));
		}
		return pathwayBlock;

	}

	private Blocks desanitizeMessagesOfBlock(Blocks pathwayBlock) throws JsonParseException, JsonMappingException, IOException
			 {
		if (pathwayBlock != null) {
			pathwayBlock.setBodyOfMessage(HtmlEscapeUtil.unescapeHtml(pathwayBlock.getBodyOfMessage()));
			pathwayBlock.setSubjectOfMessage(HtmlEscapeUtil.unescapeHtml(pathwayBlock.getSubjectOfMessage()));
			pathwayBlock.setRemainderOfMessage(HtmlEscapeUtil.unescapeHtml(pathwayBlock.getRemainderOfMessage()));
			pathwayBlock.setfollowupOfMessage(HtmlEscapeUtil.unescapeHtml(pathwayBlock.getfollowupOfMessage()));
		}
		return pathwayBlock;

	}
}