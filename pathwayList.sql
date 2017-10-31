select * from qc_pathway;

select * from qc_pathway_block;

select * from qc_pathway_event;
select * from qc_patient_pathway_info;

select  * from qc_patient_pathway_info ;

select distinct(ppb.patient_id) /*, ppb.pathway_id , p.Pathway_name,e.event_name */
 from qc_pathway_patient_blocks ppb 
 inner join qc_pathway_event e on ppb.event_id=e.id 
 inner join qc_pathway p on ppb.pathway_id=p.id
/* inner join qc_patient_pathway_info ppi on ppb.patient_id=ppi.patient_id
*/where p.org_id=285;


select p.id as pathwayId,
p.Pathway_name as pathwayName,
e.id as eventId,
e.event_name as eventName,
ppi.patient_id as patientId,
ppi.accepteddate as acceptedDate,
p.org_id as orgId
from qc_pathway p inner join qc_pathway_event e on p.id=e.pathway_id 
left join qc_patient_pathway_info ppi on p.id=ppi.pathway_id
where p.org_id=285 ;

select distinct (id)from patientdb.qc_patient;

select * from patientdb.qc_pathway;
select * from patientdb.qc_pathway_event;
select * from patientdb.qc_pathway_patient_message;

select distinct(pp.patient_id), p.first_name, p.last_name, p.updated_date, pp.pathway_id, pp.event_id,pp.team_id from patientdb.qc_patient_pathway pp
inner join patientdb.qc_patient p on pp.patient_id = p.id