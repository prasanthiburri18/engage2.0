package com.engage.controller;




import com.engage.dao.BlocksDao;
import com.engage.model.Blocks;
import com.engage.util.JsonMessage;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

/**
* Pathway MicroServices for Blocks API Operations
* 
* @author  StartUP Labs
* @version 1.0
* 
*/
@RestController
@RequestMapping(value="/api/v1")
public class BlocksController {
	  private static final int ArraysList = 0;
	private static Logger log = LoggerFactory.getLogger(BlocksController.class);
  @Autowired
  private BlocksDao _blocksDao;



  /**
   * Adding Pathway Block by getting Block Object
   * @Inputparam pathwayBlock JsonObject
   * @return  JsonObject
   */
  
  
  @RequestMapping(value="/addBlock",method = RequestMethod.POST)
  public @ResponseBody JsonMessage addBlock(@RequestBody final Blocks pathwayBlock) 
  {
	  JsonMessage response=new JsonMessage();
	 try 
	  {
		 Boolean isPathway= _blocksDao.getByBlockName(pathwayBlock.getBlockName(),pathwayBlock.getEventId());
		 if(isPathway==true)
		 {
			 response.setMessage("This block already exists");
				response.setStatuscode(208);
				return response;
		 }else
		 {
				Long id= _blocksDao.save(pathwayBlock);
				response.setMessage("Block has been created successfully.");
				response.setData(id);
				response.setStatuscode(200);
				return response;
		 }
	
	  
	  }catch(Exception ex)
	 {
		  response.setMessage("Block not registered.");
		  response.setStatuscode(204);
		  return response;
	 }
  }

  /**
   * Delete Pathway Block by Block Id
   * @Inputparam PathwayBlockjsonObject
   * @return JsonObject
   */

  @RequestMapping(value="/deleteBlock",method = RequestMethod.POST)
  public @ResponseBody JsonMessage deleteBlock(@RequestBody Map<String, String> json) 
  {
	  JsonMessage response=new JsonMessage();
	 try
	 {
		 Blocks blocks=_blocksDao.getById(Long.parseLong(json.get("id")));
		 if(blocks.getId()>0)
		  {
			 _blocksDao.delete(blocks);
			  response.setMessage("Block deleted successfully.");
			  response.setStatuscode(200);
			  return response;
		  }else
		  {
			  response.setMessage("Block not registered.");
			  response.setStatuscode(204);
			  return response;
		  }
	 }catch (Exception e) {
		 response.setMessage("Block not registered.");
		  response.setStatuscode(204);
		  return response;
	}
	 	 
  }
  /**
   * Update Pathway Block by PathwayId
   * @Inputparam Block JsonObject
   * @return JsonObject
   */
  @RequestMapping(value="/updateBlock",method = RequestMethod.POST)
  public @ResponseBody JsonMessage updateBlock(@RequestBody final Blocks pathway) 
  {
	  JsonMessage response=new JsonMessage();
		 try 
		  {
		  
			 _blocksDao.update(pathway);
			response.setMessage("Block updated successfully");
			response.setStatuscode(200);
			return response;
		  
		  }catch(Exception ex)
		 {
			  response.setMessage("Block not registered");
			  response.setStatuscode(204);
			  return response;
		 }
	 	 
  }
  /**
   * Loading Pathway Block List By PathwayId
   *  @Inputparam Block JsonObject
   * @return Blocklist Json Object
   */
  
  @RequestMapping(value="/listBlocks",method = RequestMethod.POST)
  public @ResponseBody JsonMessage listBlocks(@RequestBody Map<String, String> json) 
  {
	  JsonMessage response=new JsonMessage();
	    try 
	    { 
	    
	    		List<Blocks> blocks=_blocksDao.verifyId(Long.parseLong(json.get("pathwayId")));
	    		
	    		response.setMessage("Blocks list.");
	    		response.setData(blocks);
	    		response.setStatuscode(200);
	    		return response;
	    }
	    catch(Exception ex) 
	    {
	    	  response.setMessage(ex.getMessage());
	    	  response.setStatuscode(203);
	    	  return response;
	    }
	 	 
  }
  
  /**
   * Patient Child Block Update Operation
   * @Inputparam BlockjsonObject
   * @return JsonObject
   */
  @RequestMapping(value="/updateparentchildblocks",method = RequestMethod.POST)
  public @ResponseBody JsonMessage updateparentchildblocks(@RequestBody Map<String, String> json) 
  {
	  JsonMessage response=new JsonMessage();
	    try 
	    { 
	    	
	    	
	    	Long rid=Long.parseLong(json.get("rid"));
	    	Long parentbid=Long.parseLong(json.get("parentblockid"));
	    	Long brow=Long.parseLong(json.get("brow"));
	    	Long bcol=Long.parseLong(json.get("bcol"));
	    	
	    	String msentat=(json.get("msentat"));
	    	String aptdate=(json.get("aptdate"));
	    	
	    		Integer results=_blocksDao.updatePatientappointmentblock(rid,parentbid,brow,bcol,msentat,aptdate);
	    		
	    		response.setMessage("Child Blocks list.");
	    		response.setData(results);
	    		response.setStatuscode(200);
	    		return response;
	    }
	    catch(Exception ex) 
	    {
	    	  response.setMessage(ex.getMessage());
	    	  response.setStatuscode(203);
	    	  return response;
	    }
	 	 
  }
  /**
   * Listing Child Block by Mater Parent Id
   * @Inputparam jsonObject
   * @return JsonObject
   */
  @RequestMapping(value="/listBlocksByParent",method = RequestMethod.POST)
  public @ResponseBody JsonMessage listBlocksByParent(@RequestBody Map<String, String> json) 
  {
	  JsonMessage response=new JsonMessage();
	    try 
	    { 

	    	Long parentbid=Long.parseLong(json.get("parentblockid"));
	    		List<Object> blocks=_blocksDao.getChildBlockdata(parentbid);
	    		
	    		response.setMessage("Child Blocks list.");
	    		response.setData(blocks);
	    		response.setStatuscode(200);
	    		return response;
	    }
	    catch(Exception ex) 
	    {
	    	  response.setMessage(ex.getMessage());
	    	  response.setStatuscode(203);
	    	  return response;
	    }
	 	 
  }
  
  
  
  
  
}