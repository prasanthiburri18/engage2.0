package com.engage.controller;

import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
//import java.sql.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletRequest;
import javax.validation.ConstraintViolation;
import javax.validation.Validator;

import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.client.OAuth2RestTemplate;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.engage.commons.exception.ConstraintViolationException;
import com.engage.commons.exception.DataTamperingException;
import com.engage.commons.exception.InvalidAccessException;
import com.engage.commons.validators.utils.ConstraintValidationUtils;
import com.engage.dao.PatientDao;
import com.engage.dao.PatientPathwayDao;
import com.engage.dto.PatientDto;
import com.engage.dto.PatientListDto;
import com.engage.exception.InvalidDateOfBirthException;
import com.engage.exception.PatientNotFoundException;
import com.engage.model.Patient;
import com.engage.model.PatientPathway;
import com.engage.service.PatientService;
import com.engage.util.JsonMessage;
import com.engage.util.PatientDtoToModelUtils;
import com.engage.util.UtilityFunctions;

@RestController
/**
 * Patient MicroServices for Patient API Operations
 * 
 * @author StartUP Labs
 * @version 1.0
 * 
 */
@RequestMapping(value = "/api/v1")
public class PatientController {
	private static Logger log = LoggerFactory
			.getLogger(PatientController.class);
	@Autowired
	private PatientDao _patientDao;
	@Autowired
	private PatientPathwayDao _patientPathwayDao;
	//@Autowired
//	private PathwayDao _pathwayDao;
	//@Autowired
//	private PathwayEventsDao _pathwayEventsDao;

	// private RestTemplate restTemplate = new RestTemplate();

	@Autowired
	private OAuth2RestTemplate restTemplate;
	@Value("${countryCode}")
	private String countryCode;
	@Value("${scheduleMicroserviceBaseUrl}")
	private String scheduleMicroserviceBaseUrl;

	@Value("${pathwayMicroserviceBaseUrl}")
	private String pathwayMicroserviceBaseUrl;

	@Autowired
	private Validator validator;

	@Autowired
	private PatientService patientService;

	/**
	 * Add Patient Method Patient in method parameter is replaced by PatientDto,
	 * as Encryption is present on setters of Patient Validations are done on
	 * PatientDto
	 * 
	 * @Inputparam Patient JsonObject
	 * @return JsonObject
	 * @throws DataTamperingException
	 */
	@PreAuthorize("#oauth2.hasScope('client_app') and hasAnyAuthority('A','U')")
	@RequestMapping(value = "/addPatient", method = RequestMethod.POST)
	public @ResponseBody JsonMessage create(
			@RequestBody final PatientDto patientDto)
			throws DataTamperingException {
		JsonMessage response = new JsonMessage();
		// Engage2.0
		Set<ConstraintViolation<PatientDto>> errors = validator
				.validate(patientDto);
		if (!errors.isEmpty()) {
			Map<String, String> errorBody = ConstraintValidationUtils
					.getMapOfValidations(errors);
			JSONObject json = new JSONObject(errorBody);
			try {
				throw new com.engage.commons.exception.ConstraintViolationException(
						json.toString());
			} catch (com.engage.commons.exception.ConstraintViolationException ex) {
				response.setData(ex.getMessage());
				response.setMessage("Invalid Patient data");
				response.setStatuscode(422);
				return response;
			}
		}
		if (!checkOrganizationIdFromAuthentication(Long.toString(patientDto
				.getorgId()))) {
			throw new DataTamperingException("Organization Id doesn't match");
		}
		try {

			Patient user = PatientDtoToModelUtils.convertDtoToModel(patientDto);
			// Engage2.0s

			String orgmessage = "";
			String sms = "";
			PatientPathway patentPathway = new PatientPathway();
			user.setStatus("Y");
			long id = _patientDao.save(user);

			if (user.getPathwayId() != 0) {
				/*ArrayList outs = (ArrayList) _patientDao
						.getPathwayFirstMessageforpatient(user.getPathwayId());
				*/
				ArrayList outs = (ArrayList) patientService
						.getPathwayFirstMessageforpatient(user.getPathwayId());
				
				
				String mm = outs.get(0).toString();
				orgmessage = mm.replaceAll("#FN", user.getFirstName());
				for (int i = 0; i < user.getEvents().length; i++) {

					patentPathway.setPatientid(id);
					patentPathway.setEventId(user.getEvents()[i]);
					patentPathway.setTeamId(1);
					patentPathway.setPathwayId(user.getPathwayId());
					_patientPathwayDao.save(patentPathway);

				}

				Map<String, Object> data1 = new HashMap<String, Object>();
				// String results =
				// restTemplate.getForObject("http://35.166.195.23:8080/PathwayMicroservice/api/v1/patientpathwaycron",
				// String.class);
				String patientPathwayCronUrl = pathwayMicroserviceBaseUrl
						+ "/api/v1/patientpathwaycron";
				String results = restTemplate.getForObject(
						patientPathwayCronUrl, String.class);
				// data1.put("toNumber", "+91"+user.getPhone());
				data1.put("toNumber", countryCode + user.getPhone());// for us
				if (orgmessage.equals("")) {
					sms = "Hi "
							+ UtilityFunctions.toTitleCase(user.getFirstName())
							+ ", Congrats on completing your surgery! Your Doctor has set up a new text messaging program to help you with your recovery. Reply with Y to accept. Contact CaringOne anytime to stop this service.";
				} else {
					sms = orgmessage;
				}

				data1.put("smsText", sms);
				// String
				// Str=restTemplate.postForObject("http://35.166.195.23:8080/ScheduleMicroservice/api/v1/sendSms",
				// data1,String.class );
				final String sendSmsUrl = scheduleMicroserviceBaseUrl
						+ "/api/v1/sendSms";
				String Str = restTemplate.postForObject(sendSmsUrl, data1,
						String.class);
			} else {
				// ignore
			}

			response.setMessage("Patient saved successfully");
			response.setStatuscode(200);
			return response;
		}

		catch (Exception ex) {
			response.setMessage(ex.getMessage());
			response.setStatuscode(203);
			return response;
		}
	}

	/**
	 * Edit Patient Method
	 * 
	 * @Inputparam Patient JsonObject
	 * @return JsonObject
	 * @throws DataTamperingException
	 */
	@PreAuthorize("#oauth2.hasScope('client_app') and hasAnyAuthority('A','U')")
	@RequestMapping(value = "/editPatient", method = RequestMethod.PUT)
	public @ResponseBody JsonMessage update(
			@RequestBody final PatientDto patientDto)
			throws DataTamperingException {
		JsonMessage response = new JsonMessage();
		String orgmessage = "";
		String sms = "";
		String newpathway = "no";

		Set<ConstraintViolation<PatientDto>> errors = validator
				.validate(patientDto);
		if (!errors.isEmpty()) {
			Map<String, String> errorBody = ConstraintValidationUtils
					.getMapOfValidations(errors);
			JSONObject json = new JSONObject(errorBody);
			try {
				throw new com.engage.commons.exception.ConstraintViolationException(
						json.toString());
			} catch (ConstraintViolationException ex) {
				response.setData(ex.getMessage());
				response.setMessage("Invalid Patient format");
				response.setStatuscode(422);
				return response;
			}
		}
		if (!checkOrganizationIdFromAuthentication(Long.toString(patientDto
				.getorgId()))) {
			throw new DataTamperingException("Organization Id doesn't match");
		}

		try {

			Patient user = PatientDtoToModelUtils.convertDtoToModel(patientDto);

			PatientPathway patentPathway = new PatientPathway();
			Patient patient = _patientDao.getById(user.getId());

			if (patient.getId() > 0) {
				Long mtime = (long) 0;
				Timestamp timestamp = new Timestamp(System.currentTimeMillis());
				mtime = timestamp.getTime();
				user.setUpdateDate(timestamp);
				_patientDao.update(user);
				ArrayList<Long> patientPathway = _patientPathwayDao
						.getPathwayById(user.getId());
				if (patientPathway.get(0) != null) {
					newpathway = "no";
				} else {
					newpathway = "yes";
				}
				_patientPathwayDao.deletePathEvents(user.getId());
				for (int i = 0; i < user.getEvents().length; i++) {
					patentPathway.setPatientid(user.getId());
					patentPathway.setEventId(user.getEvents()[i]);
					patentPathway.setTeamId(1);
					patentPathway.setPathwayId(user.getPathwayId());
					_patientPathwayDao.save(patentPathway);
				}
				if (user.getEvents().length > 0) {
				}

				if (newpathway.equals("yes")) {
					// RestTemplate restTemplate = new RestTemplate();
					if (user.getPathwayId() != 0) {
						/*ArrayList outs = (ArrayList) _patientDao
						.getPathwayFirstMessageforpatient(user.getPathwayId());
				*/
				ArrayList outs = (ArrayList) patientService
						.getPathwayFirstMessageforpatient(user.getPathwayId());
						String mm = outs.get(0).toString();
						orgmessage = mm.replaceAll("#FN", user.getFirstName());
						Map<String, Object> data1 = new HashMap<String, Object>();
						// String results =
						// restTemplate.getForObject("http://35.166.195.23:8080/PathwayMicroservice/api/v1/patientpathwaycron",
						// String.class);
						String patientPathwayCronUrl = pathwayMicroserviceBaseUrl
								+ "/api/v1/patientpathwaycron";
						String results = restTemplate.getForObject(
								patientPathwayCronUrl, String.class);
						// data1.put("toNumber", "+91"+user.getPhone());
						data1.put("toNumber", countryCode + user.getPhone());// for
																				// us
						if (orgmessage.equals("")) {
							sms = "Hi "
									+ UtilityFunctions.toTitleCase(user
											.getFirstName())
									+ ", Congrats on completing your surgery! Your Doctor has set up a new text messaging program to help you with your recovery. Reply with Y to accept. Contact CaringOne anytime to stop this service.";
						}

						else {
							sms = orgmessage;
						}

						data1.put("smsText", sms);

						// String
						// Str=restTemplate.postForObject("http://35.166.195.23:8080/ScheduleMicroservice/api/v1/sendSms",
						// data1,String.class );
						String sendSmsUrl = scheduleMicroserviceBaseUrl
								+ "/api/v1/sendSms";
						String Str = restTemplate.postForObject(sendSmsUrl,
								data1, String.class);

					}

				}

				response.setMessage("Patient updated successfully");
				response.setStatuscode(200);
				return response;
			} else {
				response.setMessage("Patient doen't exists.");
				response.setStatuscode(204);
				return response;
			}
		} catch (Exception ex) {
			response.setMessage(ex.getMessage());
			response.setStatuscode(203);
			return response;
		}
	}

	/**
	 * View Patient Method
	 * 
	 * @Inputparam JsonObject
	 * @return JsonObject
	 * @throws DataTamperingException
	 */
	@PreAuthorize("#oauth2.hasScope('client_app') and hasAnyAuthority('A','U')")
	@SuppressWarnings("unchecked")
	@RequestMapping(value = "/view_Patient", method = RequestMethod.POST)
	public @ResponseBody JsonMessage viewPatient(
			@RequestBody Map<String, Long> json, HttpServletRequest request)
			throws DataTamperingException {
		JsonMessage response = new JsonMessage();

		if (!checkOrganizationIdFromAuthentication(Long.toString(json
				.get("orgId")))) {
			throw new DataTamperingException("Organization Id doesn't match");
		}

		try {

			Patient patient = _patientDao.getById(json.get("id"));
			// Engage2.0
			final Long orgId = json.get("orgId");
			if (!(orgId == patient.getorgId())) {
				throw new InvalidAccessException(
						"You don't have privileges to view this patient");
			}
			// Engage2.0

			if (patient.getId() > 0) {
				ArrayList<Long> patientPathway = _patientPathwayDao
						.getPathwayById(patient.getId());
				ArrayList<Long> events = _patientPathwayDao
						.getEventsById(patient.getId());
				Map<String, Object> data = new HashMap<String, Object>();
				data.put("patient", patient);
				// RestTemplate restTemplate = new RestTemplate();
				Map<String, Object> data1 = new HashMap<String, Object>();
				data1.put("id", patientPathway);
				data1.put("evetIds", events);
				// Map<String,Object> pathway=(Map<String, Object>)
				// restTemplate.postForObject("http://35.166.195.23:8080/PathwayMicroservice/api/v1/listPathwayEventForPatients",
				// data1,Object.class );
				String listPathwayEventsUrl = pathwayMicroserviceBaseUrl
						+ "/api/v1/listPathwayEventForPatients";
				Map<String, Object> pathway = (Map<String, Object>) restTemplate
						.postForObject(listPathwayEventsUrl, data1,
								Object.class);

				data.put("pathwayInfo", pathway);
				response.setMessage("Patient data");
				response.setData(data);
				response.setStatuscode(200);
				return response;
			} else {
				response.setMessage("Patient doesn't exists.");
				response.setStatuscode(204);
				return response;
			}

		} catch (InvalidAccessException ex) {
			response.setMessage(ex.getMessage());
			response.setStatuscode(203);
			return response;
		} catch (Exception ex) {
			response.setMessage("Patient doesn't exists.");
			response.setStatuscode(203);
			return response;
		}
	}

	/**
	 * List Patient Method
	 * 
	 * @Inputparam JsonObject
	 * @return JsonObject
	 * @throws DataTamperingException
	 */
	@PreAuthorize("#oauth2.hasScope('client_app') and hasAnyAuthority('A','U')")
	@RequestMapping(value = "/list_Patient", method = RequestMethod.POST)
	public @ResponseBody JsonMessage viewPatients(
			@RequestBody Map<String, Long> json) throws DataTamperingException {
		JsonMessage response = new JsonMessage();
		if (!checkOrganizationIdFromAuthentication(Long.toString(json
				.get("orgid")))) {
			throw new DataTamperingException("Organization Id doesn't match");
		}
		try {
			List<Patient> patient = _patientDao.getAll(json.get("orgid"));
			List<Object> patientsData = new ArrayList<Object>();
			for (Patient p : patient) {
				ArrayList<Long> patientPathway = _patientPathwayDao
						.getPathwayById(p.getId());
				ArrayList<Long> events = _patientPathwayDao.getEventsById(p
						.getId());

				Map<String, Object> data = new HashMap<String, Object>();

				data.put("patient", p);
				// RestTemplate restTemplate = new RestTemplate();
				Map<String, Object> data1 = new HashMap<String, Object>();
				data1.put("id", patientPathway);
				data1.put("evetIds", events);
				// Map<String,Object> pathway=(Map<String, Object>)
				// restTemplate.postForObject("http://35.166.195.23:8080/PathwayMicroservice/api/v1/listPathwayEventForPatients",
				// data1,Object.class );
				String listPathwayEventsUrl = pathwayMicroserviceBaseUrl
						+ "/api/v1/listPathwayEventForPatients";
				Map<String, Object> pathway = (Map<String, Object>) restTemplate
						.postForObject(listPathwayEventsUrl, data1,
								Object.class);

				data.put("pathwayInfo", pathway);

				patientsData.add(data);
			}
			response.setMessage("Patient list.");
			response.setData(patientsData);
			response.setStatuscode(200);
			return response;
		} catch (Exception ex) {
			response.setMessage(ex.getMessage());
			response.setStatuscode(203);
			return response;
		}
	}

	/**
	 * Delete Patient Method
	 * 
	 * @Inputparam JsonObject
	 * @return JsonObject
	 * @throws DataTamperingException
	 */
	@PreAuthorize("#oauth2.hasScope('client_app') and hasAuthority('A')")
	@RequestMapping(value = "/delete_Patient", method = RequestMethod.DELETE)
	public @ResponseBody JsonMessage deletePatient(
			@RequestBody Map<String, Long> json) throws DataTamperingException {
		JsonMessage response = new JsonMessage();
		if (_patientDao.verifyId(json.get("id")).size() > 0) {
			Patient patient = _patientDao.getById(json.get("id"));
			if (!checkOrganizationIdFromAuthentication(Long.toString(patient
					.getorgId()))) {
				throw new DataTamperingException(
						"Organization Id doesn't match");
			}
		}
		try {
			if (_patientDao.verifyId(json.get("id")).size() > 0) {
				Patient patient = _patientDao.getById(json.get("id"));

				_patientDao.delete(patient);
				response.setMessage("Patient deleted successfully");
				response.setStatuscode(200);
				return response;
			} else {
				response.setMessage("Patient doen't exists.");
				response.setStatuscode(204);
				return response;
			}
		} catch (Exception ex) {
			response.setMessage(ex.getMessage());
			response.setStatuscode(203);
			return response;
		}
	}

	/**
	 * Patients Count for Pathway
	 * 
	 * @Inputparam JsonObject (PathwayId)
	 * @return JsonObject
	 */
	@RequestMapping(value = "/getPatientsCountByPathwayId", method = RequestMethod.POST)
	public @ResponseBody JsonMessage getPatientsCountByPathwayId(
			@RequestBody Map<String, Long> json) {
		Integer count = 0;
		JsonMessage response = new JsonMessage();
		try {
			count = _patientPathwayDao.getPatientCount(json.get("pathwayId"));
			response.setData(count);
			response.setStatuscode(200);
			return response;
		} catch (Exception ex) {

			response.setData(count);
			response.setStatuscode(200);
			return response;
		}
	}

	/**
	 * Load patient count by pathway and event
	 * 
	 * @Inputparam JsonObject
	 * @return JsonObject
	 */
	@RequestMapping(value = "/getPatientsPathwayIdAndEventId", method = RequestMethod.POST)
	public @ResponseBody Patient getPatientsPathwayIdAndEventId(
			@RequestBody Map<String, Long> json) {
		Patient patient = null;
		Integer count = 0;
		JsonMessage response = new JsonMessage();
		try {

			Long pthid = json.get("pathwayId");
			Long enid = json.get("eventId");

			count = _patientPathwayDao.getPatientInfo(pthid, enid);

			patient = _patientDao.getById(count);
			response.setData(patient);
			response.setStatuscode(200);
			return patient;
		} catch (Exception ex) {

			response.setData(count);
			response.setStatuscode(200);
			return patient;
		}
	}

	/**
	 * Called from UserMicroservice View Patient Info by Phone number Since
	 * phone number is not unique, there can be multiple patients for one phone
	 * number
	 * 
	 * @Inputparam JsonObject
	 * @return JsonObject
	 * @throws PatientNotFoundException
	 */
	@PreAuthorize("#oauth2.hasScope('usermicroservice')")
	@RequestMapping(value = "/patient/phone/{phone}", method = RequestMethod.GET)
	public @ResponseBody List<PatientDto> getPatientByphoneNumber(
			@PathVariable String phone) throws PatientNotFoundException {
		List<PatientDto> patients = null;
		// Passed encrypted phone
		patients = patientService.getPatientsByPhoneNumber(phone);

		return patients;

	}

	/**
	 *
	 * View Patient Info by Phone number
	 * 
	 * @Inputparam JsonObject
	 * @return JsonObject
	 */

	@RequestMapping(value = "/getPatientByphone", method = RequestMethod.POST)
	public @ResponseBody JsonMessage getPatientByphone(
			@RequestBody Map<String, Integer> json) {
		JsonMessage response = new JsonMessage();
		try {
			Integer pid = json.get("patientid");

			String ph = _patientDao.patientPhone(pid);
			response.setData(ph);
			response.setStatuscode(200);
			return response;
		} catch (Exception ex) {
			response.setMessage(ex.getMessage());
			response.setStatuscode(200);
			return response;
		}

	}

	/**
	 * Verify Patient existence by DOB
	 * 
	 * @Inputparam Patient JsonObject
	 * @return JsonObject
	 * @throws ParseException
	 * @throws InvalidDateOfBirthException
	 * @throws PatientNotFoundException
	 */
	@RequestMapping(value = "/patient/dob/{dob}", method = RequestMethod.GET)
	public @ResponseBody List<PatientDto> getPatientByDateOfBirth(
			@PathVariable String dob) throws InvalidDateOfBirthException,
			PatientNotFoundException {
		List<PatientDto> patients = null;
		patients = patientService.getPatientByDob(dob);

		return patients;

	}

	/**
	 * Verify Patient existence by DOB
	 * 
	 * @Inputparam Patient JsonObject
	 * @return JsonObject
	 */
	@RequestMapping(value = "/getPatientBydob", method = RequestMethod.POST)
	public @ResponseBody JsonMessage getPatientBydob(
			@RequestBody Map<String, String> json) {
		JsonMessage response = new JsonMessage();
		try {
			String pdob = json.get("dob");

			int result = _patientDao.verifyPatientInfobydob(pdob);
			response.setData(result);
			response.setStatuscode(200);
			return response;
		} catch (Exception ex) {
			response.setMessage(ex.getMessage());
			response.setStatuscode(200);
			return response;
		}

	}

	/**
	 * Checking PatientPathway Information
	 * 
	 * @Inputparam JsonObject
	 * @return JsonObject
	 */

	@RequestMapping(value = "/getPatientpathwayinfocheck", method = RequestMethod.POST)
	public @ResponseBody JsonMessage getPatientpathwayinfocheck(
			@RequestBody Map<String, String> json) {
		JsonMessage response = new JsonMessage();
		try {
			int pi = 1;
			ArrayList<Long> patientPathway = _patientPathwayDao
					.getPathwayById(pi);
			int pids = Integer.parseInt(patientPathway.get(0).toString());
			response.setData(pids);
			response.setStatuscode(200);
			return response;
		} catch (Exception ex) {
			response.setMessage(ex.getMessage());
			response.setStatuscode(200);
			return response;
		}

	}

	@RequestMapping(value = "/getPatientEventsId", method = RequestMethod.POST)
	public @ResponseBody JsonMessage getPatientEventsId(
			@RequestBody Map<String, String> json) {
		JsonMessage response = new JsonMessage();
		try {
			Long pi = Long.parseLong(json.get("patient_id"));
			ArrayList<Long> patientPathway = _patientPathwayDao
					.getEventsById(pi);

			response.setData(patientPathway);
			response.setStatuscode(200);
			return response;
		} catch (Exception ex) {
			response.setMessage(ex.getMessage());
			response.setStatuscode(200);
			return response;
		}

	}

	/**
	 * API Callback for twilio Reply
	 * 
	 * @Inputparam JsonObject
	 * @return JsonObject
	 */

	@RequestMapping(value = "/twiliorReplayAPI", method = RequestMethod.POST)
	public ResponseEntity<String> receiveBody(
			@RequestParam("From") String From, @RequestParam("Body") String Body) {
		JsonMessage response = new JsonMessage();
		HttpHeaders responseHeaders = new HttpHeaders();
		try {

			String ph = From;
			ph = ph.substring(3);// for India/
			// ph=ph.substring(2);//for US

			Patient patient = _patientDao.getByPhone(ph);

			if (patient.getId() > 0) {
				if (_patientDao.verifyPatientInfo(patient.getId()) > 0) {

					return new ResponseEntity<String>("someResponse", null);
				}
				ArrayList<Long> patientPathway = _patientPathwayDao
						.getPathwayById(patient.getId());
				DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
				// will print like 2014-02-20
				String dd = dateFormat.format(new Date());

				_patientDao.updatePatientInfofprpathway(patient.getId(),
						Integer.parseInt(patientPathway.get(0).toString()),
						"Y", dd, Body.toUpperCase());
				// RestTemplate restTemplate = new RestTemplate();
				Map<String, Object> data1 = new HashMap<String, Object>();
				// String results =
				// restTemplate.getForObject("http://35.166.195.23:8080/PathwayMicroservice/api/v1/patientpathwaycron",
				// String.class);
				String patienPathwayCronUrl = pathwayMicroserviceBaseUrl
						+ "/api/v1/patientpathwaycron";
				String results = restTemplate.getForObject(
						patienPathwayCronUrl, String.class);
				response.setMessage("Thank you for joining. You will start receiving messages");
				response.setStatuscode(200);
				return new ResponseEntity<String>(
						"Thank you for joining. You will start receiving messages.",
						responseHeaders, HttpStatus.OK);
			} else {
				return new ResponseEntity<String>("Thank you for your reply.",
						responseHeaders, HttpStatus.OK);
			}
		} catch (Exception ex) {
			return new ResponseEntity<String>("Error", responseHeaders,
					HttpStatus.OK);
		}

	}

	/**
	 * Patient Reply Method (Authorization API call Only)
	 * 
	 * @Inputparam JsonObject
	 * @return JsonObject
	 */
	@RequestMapping(value = "/patientreply", method = RequestMethod.POST)
	public @ResponseBody JsonMessage patientreply(
			@RequestBody Map<String, String> json) {
		JsonMessage response = new JsonMessage();
		HttpHeaders responseHeaders = new HttpHeaders();
		try {

			String ph = json.get("From");
			String Body = json.get("Body");
			// ph=ph.substring(3);//for India/
			ph = ph.substring(2);// for US
			Patient patient = _patientDao.getByPhone(ph);
			if (patient.getId() > 0) {
				if (_patientDao.verifyPatientInfo(patient.getId()) > 0) {
					log.info("Patient already accepted the pathway");
					response.setMessage("Patient acceptence info already updated");
					response.setStatuscode(200);
					return response;

				}
				ArrayList<Long> patientPathway = _patientPathwayDao
						.getPathwayById(patient.getId());
				DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
				String dd = dateFormat.format(new Date());
				_patientDao.updatePatientInfofprpathway(patient.getId(),
						Integer.parseInt(patientPathway.get(0).toString()),
						"Y", dd, Body.toUpperCase());
				// RestTemplate restTemplate = new RestTemplate();
				Map<String, Object> data1 = new HashMap<String, Object>();
				// String results =
				// restTemplate.getForObject("http://35.166.195.23:8080/PathwayMicroservice/api/v1/patientpathwaycron",
				// String.class);
				String patientPathwayCronUrl = pathwayMicroserviceBaseUrl
						+ "/api/v1/patientpathwaycron";
				String results = restTemplate.getForObject(
						patientPathwayCronUrl, String.class);
				response.setMessage("Thank you for joining. You will start receiving messages.");
				response.setStatuscode(200);
				return response;
			} else {
				response.setMessage("Sorry you are not registered as a patient in our system.Thank you for your reply");
				response.setStatuscode(200);
				return response;
			}
		} catch (Exception ex) {

			response.setMessage(ex.getMessage());
			response.setStatuscode(200);
			return response;
		}

	}

	@PreAuthorize("#oauth2.hasScope('schedulemicroservice')")
	@RequestMapping(value = "/patient", method = RequestMethod.GET)
	public @ResponseBody ResponseEntity<PatientDto> getPatientById(@RequestParam(value="id")Long patientId) throws PatientNotFoundException{
		
		PatientDto patientDto = patientService.getPatientById(patientId);
		
		return new ResponseEntity<PatientDto>(patientDto, HttpStatus.OK);
	}
	@RequestMapping(value = "/patients", method = RequestMethod.GET)
	@PreAuthorize("#oauth2.hasScope('client_app') and hasAnyAuthority('A','U')")
	public @ResponseBody ResponseEntity<List<PatientListDto>> getPatientsList(
			@RequestParam("orgId") Long orgId) throws DataTamperingException, PatientNotFoundException {
		Map<Long,PatientListDto> list = null;
		List<PatientListDto> listOfPatients =null;
		if (checkOrganizationIdFromAuthentication(Long.toString(orgId))) {

			list = patientService.getPatientList(orgId);
			
			if(list!=null){
				listOfPatients =  list.values().stream().collect(Collectors.toList());
			}
		}

		//return new ResponseEntity<Map<Long,PatientListDto>>(list, HttpStatus.OK);
		return new ResponseEntity<List<PatientListDto>>(listOfPatients, HttpStatus.OK);
	}

	/**
	 * Checks if orgId passed in the method matches with orgId of authentication
	 * token
	 * 
	 * @param string
	 * @return
	 * @throws DataTamperingException
	 */

	private boolean checkOrganizationIdFromAuthentication(String string)
			throws DataTamperingException {

		Authentication authentication = SecurityContextHolder.getContext()
				.getAuthentication();

		Set<String> roles = authentication.getAuthorities().stream()
				.map(r -> r.getAuthority()).collect(Collectors.toSet());

		boolean isValidOrganization = roles.contains(string);

		return isValidOrganization;
	}

}