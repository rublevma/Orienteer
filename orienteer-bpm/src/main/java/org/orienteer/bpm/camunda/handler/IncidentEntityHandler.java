package org.orienteer.bpm.camunda.handler;

import com.orientechnologies.orient.core.metadata.schema.OType;
import org.camunda.bpm.engine.impl.db.ListQueryParameterObject;
import org.camunda.bpm.engine.impl.persistence.entity.IncidentEntity;
import org.camunda.bpm.engine.runtime.IncidentQuery;
import org.orienteer.bpm.camunda.OPersistenceSession;
import org.orienteer.core.util.OSchemaHelper;

import java.util.List;
import java.util.Map;

/**
 * Created by kir on 08.07.16.
 */
public class IncidentEntityHandler extends AbstractEntityHandler<IncidentEntity> {

    public IncidentEntityHandler() {
        super("BPMIncident");
    }

    @Override
    public void applySchema(OSchemaHelper helper) {
        super.applySchema(helper);

        helper.oProperty("id", OType.STRING, 10)
                .oProperty("incidentTimestamp", OType.DATETIME, 20)
                .oProperty("incidentMessage", OType.STRING, 30)
                .oProperty("incidentType", OType.STRING, 40)
                .oProperty("executionId", OType.STRING, 50)
                .oProperty("activityId", OType.STRING, 60)
                .oProperty("processInstanceId", OType.STRING, 70)
                .oProperty("processDefinitionId", OType.STRING, 80)
                .oProperty("causeIncidentId", OType.STRING, 90)
                .oProperty("rootCauseIncidentId", OType.STRING, 100)
                .oProperty("configuration", OType.STRING, 110)
                .oProperty("tenantId", OType.STRING, 120)
                .oProperty("jobDefinitionId", OType.STRING, 130);
    }

    @Statement
    public List<IncidentEntity> selectIncidentsByExecutionId(OPersistenceSession session, final ListQueryParameterObject parameter) {
        return queryList(session, "select from " + getSchemaClass() + " where executionId=?", parameter.getParameter());
    }

    @Statement
    public List<IncidentEntity> selectIncidentsByProcessInstanceId(OPersistenceSession session, final ListQueryParameterObject parameter) {
        return queryList(session, "select from " + getSchemaClass() + " where processInstanceId=?", parameter.getParameter());
    }

    @Statement
    public List<IncidentEntity> selectIncidentsByConfiguration(OPersistenceSession session, final ListQueryParameterObject parameter) {
        Map<String, String> map = (Map<String, String>) parameter.getParameter();
        String query = "select from " + getSchemaClass() + " where configuration=?";
        if (map.containsKey("incidentType") && map.get("incidentType") != null) {
            query += " and incidentType=?";
            return queryList(session, query, map.get("configuration"), map.get("incidentType"));
        }
        return queryList(session, query, map.get("configuration"));
    }

    @Statement
    public List<IncidentEntity> selectIncidentsByQueryCriteria(OPersistenceSession session, IncidentQuery query) {
        return query(session, query);
    }

}
