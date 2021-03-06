package es.us.isa.sedl.module;


import es.us.isa.sedl.core.EmpiricalStudy;
import es.us.isa.sedl.runtime.analysis.AnalysisOperation;
import es.us.isa.sedl.runtime.lifecycle.ExperimentalLifecyclePhase;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 *
 * @author japarejo
 */
public class ExperimentTypeAnalysisOperationSpecification {
    private Class<? extends EmpiricalStudy> experimentType;
    Set<AnalysisOperation> analysisOperations;
    Map<ExperimentalLifecyclePhase,Set<AnalysisOperation>> analysisOperationsByPhase;

    public ExperimentTypeAnalysisOperationSpecification(Map<ExperimentalLifecyclePhase, Set<AnalysisOperation>> analysisOperationsByPhase) {
        this(null,analysisOperationsByPhase);
    }
    
    public ExperimentTypeAnalysisOperationSpecification(Class<? extends EmpiricalStudy> experimentType,  Map<ExperimentalLifecyclePhase, Set<AnalysisOperation>> analysisOperationsByPhase) {
        this.experimentType = experimentType;        
        this.analysisOperationsByPhase = analysisOperationsByPhase;
        this.analysisOperations=new HashSet<AnalysisOperation>();
        for(ExperimentalLifecyclePhase phase:analysisOperationsByPhase.keySet())
            analysisOperations.addAll(analysisOperationsByPhase.get(phase));
    }

    public Set<AnalysisOperation> getAnalysisOperations() {
        return analysisOperations;
    }

    public Map<ExperimentalLifecyclePhase, Set<AnalysisOperation>> getAnalysisOperationsByPhase() {
        return analysisOperationsByPhase;
    }

    public Class<? extends EmpiricalStudy> getExperimentType() {
        return experimentType;
    }

    public void setExperimentType(Class<? extends EmpiricalStudy> experimentType) {
        this.experimentType = experimentType;
    }
    
    
    
    
}
