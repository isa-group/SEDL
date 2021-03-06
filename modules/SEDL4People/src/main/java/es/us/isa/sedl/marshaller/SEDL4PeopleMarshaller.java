package es.us.isa.sedl.marshaller;

import es.us.isa.sedl.core.ControlledExperiment;
import es.us.isa.sedl.core.EmpiricalStudy;
import es.us.isa.sedl.core.SedlBase;
import es.us.isa.sedl.core.analysis.AnalysisResult;
import es.us.isa.sedl.core.analysis.datasetspecification.DatasetSpecification;
import es.us.isa.sedl.core.analysis.datasetspecification.Filter;
import es.us.isa.sedl.core.analysis.datasetspecification.GroupingProjection;
import es.us.isa.sedl.core.analysis.datasetspecification.Projection;
import es.us.isa.sedl.core.analysis.datasetspecification.ValuationFilter;
import es.us.isa.sedl.core.analysis.statistic.AssociationalAnalysis;
import es.us.isa.sedl.core.analysis.statistic.ConfidenceInterval;
import es.us.isa.sedl.core.analysis.statistic.CorrelationCoeficient;
import es.us.isa.sedl.core.analysis.statistic.DescriptiveStatistic;
import es.us.isa.sedl.core.analysis.statistic.DescriptiveStatisticValue;
import es.us.isa.sedl.core.analysis.statistic.InferentialStatistic;
import es.us.isa.sedl.core.analysis.statistic.InterquartileRange;
import es.us.isa.sedl.core.analysis.statistic.Mean;
import es.us.isa.sedl.core.analysis.statistic.Median;
import es.us.isa.sedl.core.analysis.statistic.Mode;
import es.us.isa.sedl.core.analysis.statistic.Nhst;
import es.us.isa.sedl.core.analysis.statistic.PValue;
import es.us.isa.sedl.core.analysis.statistic.Range;
import es.us.isa.sedl.core.analysis.statistic.Ranking;
import es.us.isa.sedl.core.analysis.statistic.StandardDeviation;
import es.us.isa.sedl.core.analysis.statistic.Statistic;
import es.us.isa.sedl.core.analysis.statistic.StatisticalAnalysisResult;
import es.us.isa.sedl.core.configuration.CommandExperimentalTask;
import es.us.isa.sedl.core.configuration.ComplexParameter;
import es.us.isa.sedl.core.configuration.ComputationEnvironment;
import es.us.isa.sedl.core.configuration.Configuration;
import es.us.isa.sedl.core.configuration.ExperimentalEnvironment;
import es.us.isa.sedl.core.configuration.ExperimentalInputs;
import es.us.isa.sedl.core.configuration.ExperimentalOutputs;
import es.us.isa.sedl.core.configuration.ExperimentalProcedure;
import es.us.isa.sedl.core.configuration.ExperimentalSetting;
import es.us.isa.sedl.core.configuration.ExperimentalTask;
import es.us.isa.sedl.core.configuration.InputDataSource;
import es.us.isa.sedl.core.configuration.Library;
import es.us.isa.sedl.core.configuration.OutputDataSource;
import es.us.isa.sedl.core.configuration.Parameter;
import es.us.isa.sedl.core.configuration.Runtime;
import es.us.isa.sedl.core.configuration.SimpleParameter;
import es.us.isa.sedl.core.context.ClassificationTerm;
import es.us.isa.sedl.core.context.Person;
import es.us.isa.sedl.core.design.Treatment;
import es.us.isa.sedl.core.design.AnalysisSpecificationGroup;
import es.us.isa.sedl.core.design.Constraint;
import es.us.isa.sedl.core.design.ControllableFactor;
import es.us.isa.sedl.core.design.ExperimentalProtocolStep;
import es.us.isa.sedl.core.design.ExtensionDomain;
import es.us.isa.sedl.core.design.FullySpecifiedExperimentalDesign;
import es.us.isa.sedl.core.design.FundamentalSetConstraint;
import es.us.isa.sedl.core.design.Group;
import es.us.isa.sedl.core.design.IntensionDomain;
import es.us.isa.sedl.core.design.IntervalConstraint;
import es.us.isa.sedl.core.design.Level;
import es.us.isa.sedl.core.design.Measurement;
import es.us.isa.sedl.core.design.NonControllableFactor;
import es.us.isa.sedl.core.design.Outcome;
import es.us.isa.sedl.core.design.ProtocolScheme;
import es.us.isa.sedl.core.analysis.statistic.StatisticalAnalysisSpec;
import es.us.isa.sedl.core.configuration.TaskBasedExperimentalProcedure;
import es.us.isa.sedl.core.design.LiteralSizing;
import es.us.isa.sedl.core.design.Variable;
import es.us.isa.sedl.core.design.VariableKind;
import es.us.isa.sedl.core.design.VariableValuation;
import es.us.isa.sedl.core.execution.Execution;
import es.us.isa.sedl.core.execution.ExperimentalResult;
import es.us.isa.sedl.core.execution.ResultsFile;
import es.us.isa.sedl.core.execution.SimpleLog;
import es.us.isa.sedl.core.hypothesis.AssociationalHypothesis;
import es.us.isa.sedl.core.hypothesis.DescriptiveHypothesis;
import es.us.isa.sedl.core.hypothesis.DifferentialHypothesis;
import es.us.isa.sedl.core.hypothesis.Hypothesis;
import es.us.isa.sedl.grammar.SEDL4PeopleLexer;
import es.us.isa.sedl.core.util.SEDLMarshaller;
import java.io.IOException;
import java.io.OutputStream;
import java.security.spec.DSAGenParameterSpec;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import org.apache.commons.lang3.StringUtils;
import static es.us.isa.sedl.grammar.SEDL4PeopleLexer.*;

public class SEDL4PeopleMarshaller implements SEDLMarshaller {

    private StringBuilder sedlCode;
    private EmpiricalStudy experiment;

    private final String TAB = "\t";
    private final String RET = "\n";
    private final String ESP = " ";

    public List<String> write(EmpiricalStudy exp, OutputStream os)
            throws IOException {
        return null;
    }

    @Override
    public String asString(EmpiricalStudy exp) {
        experiment = exp;
        sedlCode = new StringBuilder();
        sedlCode.append(writeExperimentPreamble(exp));
        sedlCode.append(writeExperimentContext(exp));
        sedlCode.append(writeConstantsBlock(exp));
        sedlCode.append(writeVariablesBlock(exp));
        sedlCode.append(writeHypothesis(exp));
        sedlCode.append(writeDesign(exp));
        sedlCode.append(writeAnalyses(exp));
        sedlCode.append(writeConfigurations(exp));
        return sedlCode.toString();
    }

    public String writeExperimentPreamble(EmpiricalStudy exp) {

        StringBuilder sb = new StringBuilder();
        for (String moduleImport : exp.getAnnotations()) {
            sb.append(getTokenName(IMPORT)).append(ESP).append(moduleImport).append(RET);
        }
        sb.append(writePreComments("", exp, FormalLinguisticPattern.StructuredAbstract));
        sb.append(getTokenName(
                EXPERIMENT));
        sb.append(getTokenName(
                COLON
        )).append(
                ESP
        ).append(
                exp.getId()
        ).append(
                ESP
        );
        if (exp.getVersion() != null) {
            sb.append(getTokenName(
                    VERSION
            )).append(getTokenName(
                    COLON
            )).append(
                    ESP
            ).append(
                    exp.getVersion()
            ).append(
                    ESP
            );
        }
        if (exp.getMetaid() != null) {
            sb.append(getTokenName(
                    REP
            )).append(getTokenName(
                    COLON
            )).append(
                    ESP
            ).append(
                    exp.getMetaid()
            );
        }
        sb.append(RET);
        return sb.toString();
    }

    private String writePreComments(String indentation, SedlBase entity) {
        return writePreComments(indentation, entity, null);
    }

    private String writePreComments(String indentation, SedlBase entity, FormalLinguisticPattern pattern) {
        StringBuilder result = new StringBuilder();
        List<String> comments = new ArrayList<String>(entity.getNotes());
        if (pattern != null && entity.getNotes().size() > 1) {
            comments = pattern.apply(comments);
        }
        for (String comment : comments) {
            if (comment.contains("\n")) {
                result.append(indentation + "/*").append(comment).append("*/").append(RET);
            } else {
                result.append(indentation + "//").append(comment).append(RET);
            }
        }
        return result.toString();
    }

    /*private String writePostComments(){
            StringBuilder result=new StringBuilder();
            for(String note:entity.getNotes()){
                if(note.startsWith("Pre-//") || note.startsWith("Pre-/*")){
                    result.append(note.substring(4)).append(RET);
                }
            }
            return result.toString();
        }*/
    public String writeExperimentContext(EmpiricalStudy exp) {
        StringBuilder sb = new StringBuilder();
        ControlledExperiment e = (ControlledExperiment) exp;
        if (e.getContext() != null) {
            sb.append(writePreComments(TAB, e.getContext(), FormalLinguisticPattern.GQM));
        }
        /*boolean firstNote = true;
        if (!experiment.getNotes().isEmpty()) {

            for (String note : experiment.getNotes()) {
                if (!note.startsWith("//") && !note.startsWith("/*")) {
                    if (firstNote) {
                        sb.append(TAB)
                                .append(getTokenName(NOTES))
                                .append(getTokenName(COLON))
                                .append(RET);
                        firstNote = false;
                    }
                    sb.append(TAB)
                            .append(TAB)
                            .append(note)
                            .append(RET);
                }
            }
        }*/
 /*
        if (!experiment.getAnnotations().isEmpty()) {
            sb.append(TAB)
                    .append(getTokenName(ANNOTATIONS))
                    .append(getTokenName(COLON))
                    .append(RET);
            for (String annotation : experiment.getAnnotations()) {
                sb.append(TAB)
                        .append(TAB)
                        .append(annotation)
                        .append(RET);
            }
        }*/
        sb.append(writeExperimentClassification(exp));
        sb.append(writeExperimentKeywords(exp));
        if (e.getContext() != null && e.getContext().getPeople() != null && !e.getContext().getPeople().getPerson().isEmpty()) {
            sb.append(getTokenName(SUBJECTS)).append(getTokenName(COLON)).append(RET);
            for (Person p : e.getContext().getPeople().getPerson()) {
                sb.append(writePreComments(TAB, p));
                sb.append(TAB).append(p.getName());
                if (p.getEmail() != null && !"".equals(p.getEmail())) {
                    sb.append(ESP)
                            .append(getTokenName(OPEN_PAR))
                            .append(p.getEmail())
                            .append(getTokenName(CLOSE_PAR));
                    if (p.getOrganization() != null && !"".equals(p.getOrganization())) {
                        sb.append(ESP)
                                .append(getTokenName(FROM))
                                .append(ESP);
                        if (!p.getOrganization().isEmpty()) {
                            sb.append(printValue(p.getOrganization().get(0)));
                        }
                    }
                    if (p.getRole() != null && !"".equals(p.getRole())) {
                        sb.append(ESP)
                                .append(getTokenName(AS))
                                .append(ESP)
                                .append(p.getRole());
                    }
                    sb.append(RET);
                }
            }
        }

        // Object: 
        if (e.getDesign().getPopulation().getIndividualDescription() != null && !e.getDesign().getPopulation().getIndividualDescription().equals("")) {
            sb.append(TAB)
                    .append(getTokenName(OBJECT))
                    .append(getTokenName(COLON))
                    .append(e.getDesign().getPopulation().getIndividualDescription())
                    .append(RET);
        }
        // Population: 
        if (e.getDesign().getPopulation().getAccesiblePopulation() != null && !e.getDesign().getPopulation().getAccesiblePopulation().equals("")) {
            sb.append(TAB)
                    .append(getTokenName(POPULATION))
                    .append(getTokenName(COLON))
                    .append(e.getDesign().getPopulation().getAccesiblePopulation())
                    .append(RET);
        }
        return sb.toString();
    }

    private String writeExperimentClassification(EmpiricalStudy exp) {
        String result = "";
        ControlledExperiment e = (ControlledExperiment) exp;
        if (e.getContext() != null && !e.getContext().getClassificationTerms().isEmpty()) {
            StringBuilder sb = new StringBuilder();
            sb.append(TAB)
                    .append(getTokenName(CLASSIFIED_AS))
                    .append(getTokenName(COLON)).append(RET);
            for (ClassificationTerm ce : e.getContext().getClassificationTerms()) {
                sb.append(TAB).append(TAB)
                        .append(ce.getClassificationSystem())
                        .append(getTokenName(COLON))
                        .append(ce.getCode())
                        .append(ESP)
                        .append(ce.getName())
                        .append(RET);
            }
            result = sb.toString();
        }
        return result;
    }

    private String writeExperimentKeywords(EmpiricalStudy exp) {
        String result = "";
        ControlledExperiment e = (ControlledExperiment) exp;
        ClassificationTerm ce = null;
        if (e.getContext() != null && !e.getContext().getKeywords().isEmpty()) {
            StringBuilder sb = new StringBuilder();
            sb.append(TAB)
                    .append(getTokenName(KEYWORDS))
                    .append(getTokenName(COLON)).append(ESP);
            for (int i = 0; i < e.getContext().getKeywords().size(); i++) {
                sb.append(e.getContext().getKeywords().get(i));
                if (i != e.getContext().getKeywords().size() - 1) {
                    sb.append(getTokenName(COMMA))
                            .append(ESP);
                } else {
                    sb.append(RET);
                }
            }
            result = sb.toString();
        }
        return result;
    }

    public String writeConstantsBlock(EmpiricalStudy experiment) {
        ControlledExperiment exp = null;
        StringBuilder sb = new StringBuilder();
        if (experiment instanceof ControlledExperiment) {
            exp = (ControlledExperiment) experiment;
        }
        if (exp != null && !exp.getDesign().getDesignParameters().isEmpty()) {
            sb.append(getTokenName(CONSTANTS))
                    .append(getTokenName(COLON))
                    .append(RET);
            for (Parameter p : exp.getDesign().getDesignParameters()) {
                sb.append(writePreComments(TAB, p));
                if (p instanceof SimpleParameter) {
                    SimpleParameter param = (SimpleParameter) p;
                    sb.append(TAB)
                            .append(
                                    param.getName())
                            .append(ESP)
                            .append(getTokenName(
                                    COLON))
                            .append(ESP)
                            .append(
                                    printValue(param.getValue()));
                } else if (p instanceof ComplexParameter) {
                    ComplexParameter param = (ComplexParameter) p;
                    sb.append(TAB)
                            .append(
                                    param.getName())
                            .append(ESP)
                            .append(
                                    getTokenName(COLON))
                            .append(ESP)
                            .append(
                                    getTokenName(OPEN_BRA));
                    for (int i = 0; i < param.getParameters().size(); i++) {
                        Parameter sp = param.getParameters().get(i);
                        if (sp instanceof SimpleParameter) {
                            if (i != 0) {
                                sb.append(getTokenName(COMMA));
                            }
                            sb.append(sp.getName())
                                    .append(getTokenName(COLON))
                                    .append(((SimpleParameter) sp).getValue());
                        }
                    }
                    sb.append(getTokenName(CLOSE_BRA));

                } else {
                    System.out.println("Can not find parameter " + p.getName() + " type.");
                }
                if (p.getUnits() != null && !"".equals(p.getUnits())) {
                    sb.append(ESP)
                            .append(getTokenName(MEASURED))
                            .append(ESP)
                            .append(getTokenName(IN))
                            .append(ESP)
                            .append(p.getUnits());
                }
                sb.append(RET);
            }
        }
        return sb.toString();
    }

    public String writeVariablesBlock(EmpiricalStudy experiment) {

        ControlledExperiment exp = null;
        if (experiment instanceof ControlledExperiment) {
            exp = (ControlledExperiment) experiment;
        }
        StringBuilder sb = new StringBuilder(
                getTokenName(VARIABLES));
        sb.append(
                ESP
        ).append(
                getTokenName(COLON)
        ).append(RET
        ).append(
                writeFactorsBlock(exp)
        ).append(
                writeNCFactorsBlock(exp)
        ).append(
                writeOutcomeBlock(exp)
        );
        return sb.toString();
    }

    private String writeFactorsBlock(ControlledExperiment exp) {
        StringBuilder sb = new StringBuilder();
        if (exp != null) {
            sb.append(
                    TAB
            ).append(
                    getTokenName(FACTORS)
            ).append(
                    ESP
            ).append(
                    getTokenName(COLON)
            ).append(RET);
            for (Variable v : exp.getDesign().getVariables().getVariables()) {
                if (v instanceof ControllableFactor) {
                    sb.append(printVariable(v));
                }
            }
        }
        return sb.toString();
    }

    private String printVariable(Variable v) {
        StringBuilder sb = new StringBuilder();
        sb.append(writePreComments(TAB + TAB, v));
        sb.append(
                TAB
        ).append(
                TAB
        ).append(
                v.getName()
        ).append(
                ESP
        ).append(
                printDomain(v)
        );
        if (v.getUnits() != null && !"".equals(v.getUnits())) {
            sb.append(ESP)
                    .append(getTokenName(MEASURED))
                    .append(ESP)
                    .append(getTokenName(IN))
                    .append(ESP)
                    .append(v.getUnits());
        }
        sb.append(RET);
        return sb.toString();
    }

    private String printDomain(Variable v) {
        String result = "";
        if (v.getDomain() instanceof ExtensionDomain) {
            if (v.getKind() == VariableKind.ORDINAL) {
                result = getTokenName(ORDERED) + ESP;
            }
            result += printExtensionDomain((ExtensionDomain) v.getDomain());
        } else if (v.getDomain() instanceof IntensionDomain) {
            result = printIntensionDomain((IntensionDomain) v.getDomain());
        } else {
            System.out.println("Error, variableKind don't found: " + v.toString());
        }
        return result;
    }

    private String printExtensionDomain(ExtensionDomain domain) {
        StringBuilder sb = new StringBuilder();
        sb.append(
                getTokenName(ENUM));
        for (int i = 0; i < domain.getLevels().size(); i++) {
            Level l = domain.getLevels().get(i);
            if (!l.getNotes().isEmpty()) {
                sb.append(RET);
            }
            sb.append(writePreComments(TAB + TAB + TAB, l));
            if (!l.getNotes().isEmpty()) {
                sb.append(TAB + TAB + TAB);
            }
            sb.append(
                    ESP
            ).append(
                    printValue(l.getValue())
            );
            if (domain.getLevels().size() > 1 && i != domain.getLevels().size() - 1) {
                sb.append(
                        getTokenName(COMMA));
            }

        }
        return sb.toString();
    }

    private String printValue(String value) {
        String result = value;
        if(value==null)
            return "";
        if (!StringUtils.isNumeric(value) && !isBoolean(value) && !isFunctionalExpression(value)) {
            if (value.startsWith("'") || value.startsWith("\"")) {
                result = value;
            } else {
                result = "'" + value + "'";
            }
        }
        return result;
    }

    private boolean isFunctionalExpression(String value) {
            if(value!=null)
                return value.contains("(");
            else 
                return false;
    }

    private boolean isBoolean(String value) {
        return "true".equalsIgnoreCase(value) || "false".equalsIgnoreCase(value);
    }

    private String printIntensionDomain(IntensionDomain dom) {
        StringBuilder sb = new StringBuilder();
        for (Constraint c : dom.getConstraint()) {
            if (c instanceof FundamentalSetConstraint) {
                sb.append(printFundamentalSetConstraint((FundamentalSetConstraint) c));
            } else if (c instanceof IntervalConstraint) {
                sb.append(printIntervalConstraint((IntervalConstraint) c));
            }
            sb.append(ESP);
        }

        return sb.toString();
    }

    private String printFundamentalSetConstraint(FundamentalSetConstraint c) {
        String result = null;

        switch (c.getFundamentalSet()) {
            case R:
                result = getTokenName(FLOAT);
                break;
            case B:
                result = getTokenName(BOOL);
                break;
            case N:
                result = getTokenName(INTEGER);
                break;
            case Z:
                result = getTokenName(INTEGER);
                break;
        }

        return result;
    }

    private String printIntervalConstraint(IntervalConstraint c) {
        StringBuilder sb = new StringBuilder();
        sb.append(
                getTokenName(RANGE_MIN_MAX)
        ).append(ESP
        ).append(getTokenName(OPEN_SQU_BRA)
        ).append(c.getMin()
        ).append(","
        ).append(c.getMax()
        ).append(getTokenName(CLOSE_SQU_BRA));
        return sb.toString();
    }

    private String writeNCFactorsBlock(ControlledExperiment exp) {
        StringBuilder sb = new StringBuilder();
        boolean ncFactorsExists = false;
        boolean titleWroten = false;
        if (exp != null) {
            for (Variable v : exp.getDesign().getVariables().getVariables()) {
                if (v instanceof NonControllableFactor) {
                    if (!ncFactorsExists) {
                        sb.append(
                                TAB
                        ).append(
                                getTokenName(NCFACTORS)
                        ).append(
                                ESP
                        ).append(
                                getTokenName(COLON)
                        ).append(RET);
                        ncFactorsExists = true;
                    }

                    sb.append(printVariable(v));
                }
            }
        }
        return sb.toString();
    }

    private String writeOutcomeBlock(ControlledExperiment exp) {
        StringBuilder sb = new StringBuilder();
        boolean outcomeExists = false;
        boolean titleWroten = false;
        if (exp != null) {
            sb.append(
                    TAB
            ).append(
                    getTokenName(OUTCOME)
            ).append(
                    ESP
            ).append(
                    getTokenName(COLON)
            ).append(RET);

            for (Variable v : exp.getDesign().getVariables().getVariables()) {
                if (v instanceof Outcome) {
                    sb.append(printVariable(v));
                }
            }
        }
        return sb.toString();

    }

    public String writeHypothesis(EmpiricalStudy experiment) {
        ControlledExperiment exp = null;
        StringBuilder sb = new StringBuilder();
        if (experiment instanceof ControlledExperiment) {
            exp = (ControlledExperiment) experiment;
        }
        if (exp != null) {
            sb.append(
                    getTokenName(HYPOTHESIS)
            ).append(
                    getTokenName(COLON)
            ).append(
                    ESP);
            if (exp.getHypotheses().size() > 1) {
                sb.append(RET);
            }
            for (Hypothesis h : exp.getHypotheses()) {
                sb.append(writePreComments(TAB, h));
                sb.append(TAB)
                        .append(h.getId())
                        .append(getTokenName(COLON))
                        .append(ESP);
                if (h instanceof AssociationalHypothesis) {
                    sb.append(printAssociationalHypthesis((AssociationalHypothesis) h));
                } else if (h instanceof DescriptiveHypothesis) {
                    sb.append(printDescriptiveHypothesis((DescriptiveHypothesis) h));
                } else if (h instanceof DifferentialHypothesis) {
                    sb.append(printDifferentialHypothesis((DifferentialHypothesis) h));
                } else {
                    System.out.println("Unknown hypothesis type : " + h.toString());
                }
                sb.append(RET);
            }
        }
        return sb.toString();
    }

    public String writeDesign(EmpiricalStudy experiment) {
        StringBuilder sb = new StringBuilder();
        if (experiment instanceof ControlledExperiment) {
            ControlledExperiment exp = (ControlledExperiment) experiment;
            sb.append(writePreComments("", exp.getDesign()));
        }
        sb.append(
                getTokenName(DESIGN)
        ).append(
                ESP
        ).append(
                getTokenName(COLON)
        ).append(
                RET);
        try {
            sb.append(
                    writeSampling(experiment)
            ).append(
                    writeAssignment(experiment)
            ).append(
                    writeBlockingVariables(experiment)
            ).append(
                    writeDetailedDesign(experiment)
            ).append(
                    writeGroups(experiment)
            ).append(
                    writeProtocol(experiment));
        } catch (Exception e) {
            System.out.println("!! Error marshalling. !! ");
            e.printStackTrace();
        }
        return sb.toString();
    }

    private String writeSampling(EmpiricalStudy experiment) {
        ControlledExperiment exp = null;
        if (experiment instanceof ControlledExperiment) {
            exp = (ControlledExperiment) experiment;
        }
        StringBuilder sb = new StringBuilder();
        //System.out.println("BEFORE SAMPLING: " + String.valueOf(exp != null) + "/" + String.valueOf(exp.getDesign() != null) + "//" + String.valueOf(exp.getDesign().getSamplingMethod() != null));
        if (exp != null && exp.getDesign() != null && exp.getDesign().getSamplingMethod() != null) {
            //System.out.println(">>>>>>>>> WRITE SAMPLING 2");
            //System.out.println("Writing: " + TAB + getTokenName(SAMPLING) + ESP + getTokenName(COLON) + ESP);
            sb.append(
                    TAB
            ).append(
                    getTokenName(SAMPLING)
            ).append(
                    ESP
            ).append(
                    getTokenName(COLON)
            ).append(
                    ESP
            );
            if (exp.getDesign().getSamplingMethod().isRandom()) {
                //System.out.println("RANDOM!");
                sb.append(
                        getTokenName(RANDOM)
                ).append(
                        RET);
            } else {
                //System.out.println("NO RANDOM!");
                sb.append(
                        exp.getDesign().getSamplingMethod().getDescription()
                ).append(
                        RET);
            }
        }
        return sb.toString();
    }

    private String writeDetailedDesign(EmpiricalStudy experiment) {
        /*ControlledExperiment exp = null;
                StringBuilder sb=new StringBuilder();
		if ( experiment instanceof ControlledExperiment ) {
			exp = (ControlledExperiment)experiment;
		}
		if ( exp != null ) {
			// Cambiar el custom. Qué posibles valores hay?
			sb.append(
                                TAB
                        ).append(
                                getTokenName(GROUPS)
                        ).append(getTokenName(COLON)
                        ).append(
                                ESP
                        ).append(
                                "Custom"
                        ).append(
                                RET   );
		}
                return sb.toString();*/
        return "";
    }

    private String writeAssignment(EmpiricalStudy experiment) {
        ControlledExperiment exp = null;
        if (experiment instanceof ControlledExperiment) {
            exp = (ControlledExperiment) experiment;
        }
        StringBuilder sb = new StringBuilder();
        if (exp != null) {
            if (exp.getDesign().getExperimentalDesign() instanceof FullySpecifiedExperimentalDesign) {
                FullySpecifiedExperimentalDesign fd = (FullySpecifiedExperimentalDesign) exp.getDesign().getExperimentalDesign();
                System.out.println("ASSIGNMENT: ?? " + fd.getAssignmentMethod());
                if (fd.getAssignmentMethod() != null) {
                    sb.append(
                            TAB
                    ).append(
                            getTokenName(ASSIGNMENT)
                    ).append(
                            ESP
                    ).append(
                            getTokenName(COLON)
                    ).append(
                            ESP);
                    if (fd.getAssignmentMethod().isRandom()) {
                        sb.append(getTokenName(RANDOM));
                    } else {
                        sb.append(fd.getAssignmentMethod().getDescription());
                    }
                    sb.append(RET);
                }
            }
        }
        return sb.toString();
    }

    private String writeBlockingVariables(EmpiricalStudy experiment) {
        ControlledExperiment exp = null;
        if (experiment instanceof ControlledExperiment) {
            exp = (ControlledExperiment) experiment;
        }
        StringBuilder sb = new StringBuilder();
        if (exp != null) {
            if (exp.getDesign().getExperimentalDesign() instanceof FullySpecifiedExperimentalDesign) {
                FullySpecifiedExperimentalDesign fd = (FullySpecifiedExperimentalDesign) exp.getDesign().getExperimentalDesign();
                if (!fd.getAssignmentMethod().getBlockingVariables().isEmpty()) {
                    sb.append(
                            TAB
                    ).append(
                            getTokenName(BLOCKING)
                    ).append(ESP)
                            .append(
                                    getTokenName(COLON));
                    for (int i = 0; i < fd.getAssignmentMethod().getBlockingVariables().size(); i++) {
                        Variable var = exp.getDesign().getVariables().getVariableByName(fd.getAssignmentMethod().getBlockingVariables().get(i));
                        if (var != null) {
                            if (i != 0) {
                                sb.append(
                                        getTokenName(COMMA));
                            }
                            sb.append(ESP).append(
                                    var.getName());
                        }
                    }
                    sb.append(RET);
                }
            }
        }
        return sb.toString();
    }

    private String writeGroups(EmpiricalStudy experiment) {
        ControlledExperiment exp = null;
        if (experiment instanceof ControlledExperiment) {
            exp = (ControlledExperiment) experiment;
        }
        StringBuilder sb = new StringBuilder();
        if (exp != null) {
            if (exp.getDesign().getExperimentalDesign() instanceof FullySpecifiedExperimentalDesign) {
                FullySpecifiedExperimentalDesign fd = (FullySpecifiedExperimentalDesign) exp.getDesign().getExperimentalDesign();
                if (!fd.getGroups().isEmpty()) {
                    sb.append(
                            TAB
                    ).append(getTokenName(
                            GROUPS)
                    ).append(
                            ESP
                    ).append(getTokenName(
                            COLON)
                    ).append(
                            ESP);
                    if(fd.getGroups().size()==1 && areAllValuationsEmpty(fd.getGroups().get(0).getValuations()))
                    {
                        LiteralSizing l = (LiteralSizing) fd.getGroups().get(0).getSizing();
                        sb.append(RET).append(TAB).append(TAB)
                            .append(getTokenName(BY))
                            .append(ESP)
                            .append(printVariableValuations(fd.getGroups().get(0).getValuations(),getTokenName(COLON),false))
                            .append(
                                ESP
                            ).append(
                                getTokenName(SIZING)
                            ).append(
                                ESP
                            ).append(
                                l.getValue().intValue()
                            );
                    }else {
                        for (int i = 0; i < fd.getGroups().size(); i++) {                                                
                            Group g = fd.getGroups().get(i);
                            if (i != 0) {
                                sb.append(getTokenName(COMMA));
                            }
                            LiteralSizing l = (LiteralSizing) g.getSizing();
                            sb.append(
                                g.getName()
                            ).append(
                                getTokenName(OPEN_PAR)                                
                            ).append(
                                printVariableValuations(g.getValuations(),getTokenName(COLON))
                            ).append(
                                getTokenName(CLOSE_PAR)
                            ).append(
                                ESP
                            ).append(
                                getTokenName(SIZING)
                            ).append(
                                ESP
                            ).append(
                                l.getValue().intValue()
                            );
                        }
                    }
                    sb.append(RET);
                }
            }
        }
        return sb.toString();
    }

    private boolean areAllValuationsEmpty(List<VariableValuation> valuations){        
        for(VariableValuation val:valuations){
            if(val.getLevel()!=null)
                return false;
        }
        return true;
    }

    private String writeProtocol(EmpiricalStudy experiment) {
        ControlledExperiment exp = null;
        if (experiment instanceof ControlledExperiment) {
            exp = (ControlledExperiment) experiment;
        }
        StringBuilder sb = new StringBuilder();
        if (exp != null) {
            if (exp.getDesign().getExperimentalDesign() instanceof FullySpecifiedExperimentalDesign) {
                FullySpecifiedExperimentalDesign fd = (FullySpecifiedExperimentalDesign) exp.getDesign().getExperimentalDesign();
                sb.append(
                        TAB
                ).append(getTokenName(
                        PROTOCOL)
                ).append(
                        ESP
                ).append(getTokenName(
                        COLON)
                ).append(
                        ESP);
                if (fd.getExperimentalProtocol() != null) {
                    if (fd.getExperimentalProtocol().getScheme() == null || fd.getExperimentalProtocol().getScheme().equals(ProtocolScheme.RANDOM)) {
                        sb.append(getTokenName(RANDOM));
                    } else if (fd.getExperimentalProtocol().getScheme().equals(ProtocolScheme.SEQUENTIAL)) {
                        // Añadir token SEQUENTIAL a la gramática
                        if (fd.getExperimentalProtocol().getSteps().isEmpty()) {
                            sb.append("Custom").append(RET);
                        } else {
                            sb.append(RET);
                            for (ExperimentalProtocolStep step : fd.getExperimentalProtocol().getSteps()) {
                                sb.append(TAB + TAB + printExperimentalProtocolStep(step) + RET);
                            }
                        }
                    } else {
                        System.out.println("Protocol scheme not found.");
                    }
                }
                sb.append(RET);
            }
        }
        return sb.toString();
    }

    private String printExperimentalProtocolStep(ExperimentalProtocolStep m) {
        String result = null;
        if (m instanceof Measurement) {
            result = printMeasurement((Measurement) m);
        } else {
            result = printTreatment((Treatment) m);
        }
        return result;
    }

    private String printMeasurement(Measurement m) {
        StringBuilder sb = new StringBuilder();
        sb.append(getTokenName(MEASUREMENT));
        if (!m.getVariable().isEmpty()) {
            sb.append(ESP)
                    .append(getTokenName(OF))
                    .append(ESP);
            if (!m.getVariable().isEmpty()) {
                sb.append(m.getVariable().get(0));
                for (int i = 1; i < m.getVariable().size(); i++) {
                    sb.append(getTokenName(COMMA))
                            .append(ESP)
                            .append(m.getVariable().get(i));
                }

            }
            sb.append(ESP)
                    .append(getTokenName(ON));
        }
        sb.append(ESP)
                .append(m.getGroup());
        sb.append(getTokenName(OPEN_PAR));
        printVariableValuations(m.getVariablevaluations(), getTokenName(COLON));
        sb.append(getTokenName(CLOSE_PAR));
        return sb.toString();
    }

    private String printTreatment(Treatment m) {
        StringBuilder sb = new StringBuilder();
        sb.append(getTokenName(TREATMENT));
        sb.append(ESP)
                .append(m.getGroup());
        sb.append(getTokenName(OPEN_PAR));
        printVariableValuations(m.getVariableValuation(), getTokenName(COLON));
        sb.append(getTokenName(CLOSE_PAR));
        return sb.toString();
    }

    public String writeAnalyses(EmpiricalStudy experiment) {
        ControlledExperiment exp = null;
        if (experiment instanceof ControlledExperiment) {
            exp = (ControlledExperiment) experiment;
        }
        StringBuilder sb = new StringBuilder();
        if (exp != null) {
            if (exp.getDesign().getExperimentalDesign() instanceof FullySpecifiedExperimentalDesign) {
                sb.append(
                        TAB
                ).append(getTokenName(
                        ANALYSES)
                ).append(
                        ESP
                ).append(getTokenName(
                        COLON)
                ).append(RET);
                FullySpecifiedExperimentalDesign fd = (FullySpecifiedExperimentalDesign) exp.getDesign().getExperimentalDesign();
                int cont = 1;
                for (AnalysisSpecificationGroup analyses : fd.getIntendedAnalyses()) {
                    sb.append(printAnalysisGroup(analyses, cont));
                }
            }
        }
        return sb.toString();
    }

    public String printAnalysisGroup(AnalysisSpecificationGroup analysisSpecGroup, int cont) {
        StringBuilder sb = new StringBuilder();
        for(int i=0;i<cont;i++)
            sb.append(TAB);
        sb.append(analysisSpecGroup.getId())
                .append(getTokenName(COLON))
                .append(RET);
        if (analysisSpecGroup instanceof StatisticalAnalysisSpec) {
            StatisticalAnalysisSpec ana = (StatisticalAnalysisSpec) analysisSpecGroup;
            if (!ana.getStatistic().isEmpty()) {
                for(int i=0;i<cont;i++)
                    sb.append(TAB);
                for (Statistic analysisSpec : ana.getStatistic()) {
                    sb.append(TAB);
                    String ending = "";
                    sb.append(printStatistic(analysisSpec));
                }
                sb.append(RET);
            }
        } else {
            sb.append(analysisSpecGroup.toString());
        }

        return sb.toString();
    }

    public String printStatistic(Statistic stat) {
        String result = null;
        if (stat instanceof DescriptiveStatistic) {
            result = printDescriptiveStatistic((DescriptiveStatistic) stat);
        } else if (stat instanceof Ranking) {
            result = printRanking((Ranking) stat);
        } else if (stat instanceof InferentialStatistic) {
            result = printInferentialStatistic((InferentialStatistic) stat);
        } else {
            result = stat.toString();
        }
        return result;
    }

    private String printDescriptiveStatistic(DescriptiveStatistic descriptiveStatistic) {
        String result = null;
        if (descriptiveStatistic instanceof Mean) {
            result = getTokenName(MEAN);
        } else if (descriptiveStatistic instanceof Median) {
            result = getTokenName(MEDIAN);
        } else if (descriptiveStatistic instanceof Mode) {
            result = getTokenName(MODE);
        } else if (descriptiveStatistic instanceof StandardDeviation) {
            result = getTokenName(STDDEV);
        } else if (descriptiveStatistic instanceof InterquartileRange) {
            result = getTokenName(IQR);
        } else if (descriptiveStatistic instanceof Range) {
            result = getTokenName(RANGE);
        }
        result += getTokenName(OPEN_PAR);
        result += printDatasetSpecification(descriptiveStatistic.getDatasetSpecification());
        result += getTokenName(CLOSE_PAR);
        return result;
    }

    private String printRanking(Ranking ranking) {
        return getTokenName(RANKING)
                + getTokenName(OPEN_PAR)
                + printDatasetSpecification(ranking.getDatasetSpecification())
                + getTokenName(CLOSE_PAR);
    }

    private String printInferentialStatistic(InferentialStatistic inferentialStatistic) {
        String result = null;
        if (inferentialStatistic instanceof Nhst) {
            result = printNHST((Nhst) inferentialStatistic);
        } else if (inferentialStatistic instanceof ConfidenceInterval) {
            result = getTokenName(CONFIDENCE_INTERVAL)
                    + getTokenName(OPEN_PAR)
                    + printDatasetSpecification(inferentialStatistic.getDatasetSpecification());
            if (((ConfidenceInterval) inferentialStatistic).getConfidenceLevel() != 0.0) {
                result += getTokenName(COMMA) + ((ConfidenceInterval) inferentialStatistic).getConfidenceLevel();
            }
            result += getTokenName(CLOSE_PAR);
        } else if (inferentialStatistic instanceof AssociationalAnalysis) {
            result = printAssociationalAnalysis((AssociationalAnalysis) inferentialStatistic);
        }
        return result;
    }

    private String printAssociationalAnalysis(AssociationalAnalysis associationalAnalysis) {
        String result = null;
        CorrelationCoeficient cor = (CorrelationCoeficient) associationalAnalysis;
        result = cor.getName()
                + getTokenName(OPEN_PAR)
                + printDatasetSpecification(cor.getDatasetSpecification())
                + getTokenName(CLOSE_PAR);
        return result;
    }

    private String printNHST(Nhst nhst) {
        StringBuilder sb = new StringBuilder();
        sb.append(
                nhst.getName()
        ).append(
                getTokenName(OPEN_PAR)
        ).append(
                printDatasetSpecification(nhst.getDatasetSpecification())
        ).append(
                getTokenName(COMMA)
        ).append(
                ESP
        ).append(
                nhst.getAlpha()
        ).append(
                getTokenName(CLOSE_PAR)
        );
        return sb.toString();

    }

    private String printDatasetSpecification(DatasetSpecification dataSpec) {
        StringBuilder sb = new StringBuilder();
        if (dataSpec != null) {
            for (int i = 0; i < dataSpec.getProjections().size(); i++) {
                Projection p = dataSpec.getProjections().get(i);
                if (!(p instanceof GroupingProjection)) {
                    sb.append(printProjection(p));
                }
            }

            for (int i = 0; i < dataSpec.getFilters().size(); i++) {
                Filter f = dataSpec.getFilters().get(i);
                sb.append(printFilter(f));
                sb.append(ESP);
            }

            for (int i = 0; i < dataSpec.getProjections().size(); i++) {
                Projection p = dataSpec.getProjections().get(i);
                if (p instanceof GroupingProjection) {
                    sb.append(printProjection(p));
                }
            }
        }
        return sb.toString();
    }

    private String printProjection(Projection p) {
        String result = null;
        if (p instanceof Projection && !(p instanceof GroupingProjection)) {
            result = printSimpleProjection(p);
        }
        if (p instanceof GroupingProjection) {
            result = printGroupingProjection((GroupingProjection) p);
        }
        return result;
    }

    private String printSimpleProjection(Projection p) {
        StringBuilder sb = new StringBuilder();
        sb.append(ESP)
                .append(
                        getTokenName(OF)
                ).append(ESP);
        for (int j = 0; j < p.getProjectedVariables().size(); j++) {
            if (j != 0) {
                sb.append(getTokenName(
                        COMMA));
            }
            sb.append(p.getProjectedVariables().get(j));

        }
        sb.append(ESP);
        return sb.toString();
    }

    private String printGroupingProjection(GroupingProjection gp) {
        StringBuilder sb = new StringBuilder();
        sb.append(getTokenName(BY)).append(ESP);
        for (int j = 0; j < gp.getProjectedVariables().size(); j++) {
            if (j != 0) {
                sb.append(getTokenName(
                        COMMA));
            }
            sb.append(gp.getProjectedVariables().get(j));
        }
        return sb.toString();
    }

    private String printFilter(Filter f) {
        String result = null;
        if (f instanceof ValuationFilter) {
            result = printValuationFilter((ValuationFilter) f);
        }
        return result;
    }

    private String printValuationFilter(ValuationFilter vf) {
        StringBuilder sb = new StringBuilder();
        sb.append(getTokenName(
                WHERE)
        ).append(ESP);
        for (int j = 0; j < vf.getVariableValuations().size(); j++) {
            if (j != 0) {
                sb.append(getTokenName(
                        COMMA
                ));
            }
            VariableValuation var = vf.getVariableValuations().get(j);
            if (var != null) {
                sb.append(
                        var.getVariable())
                        .append("=")
                        .append(printValue(var.getLevel()));
            }
        }
        return sb.toString();
    }

    private String writeConfigurations(EmpiricalStudy experiment) {
        StringBuilder sb = new StringBuilder();
        ControlledExperiment exp = null;
        if (experiment instanceof ControlledExperiment) {
            exp = (ControlledExperiment) experiment;
        }

        if (exp != null) {

            if (!exp.getConfigurations().isEmpty()) {
                sb.append(getTokenName(
                        CONFIGURATION
                )).append(getTokenName(
                        COLON
                )).append(
                        RET
                );
            }

            for (int i = 0; i < exp.getConfigurations().size(); i++) {
                sb.append(TAB).append(printConfiguration(exp.getConfigurations().get(i), i));
            }
        }
        return sb.toString();
    }

    public String printConfiguration(Configuration conf, int i) {
        StringBuilder sb = new StringBuilder();
        String id = conf.getId();
        if (id == null || id.equals("")) {
            id = "C" + i;
        }
        sb.append(id).append(
                getTokenName(COLON)
        ).append(
                RET
        ).append(
                printInputs(conf.getExperimentalInputs(),i+1)        
        ).append(
                RET
        ).append(
                printOutputs(conf.getExperimentalOutputs())
        ).append(
                printExperimentalSetting(conf.getExperimentalSetting())
        ).append(
                printExperimentalProcedure(conf.getExperimentalProcedure())
        ).append(
                printExperimentalExecutions(conf.getExecutions())
        ).append(RET);
        return sb.toString();
    }

    private String printExperimentalExecutions(List<Execution> executions) {
        StringBuilder sb = new StringBuilder();
        if (!executions.isEmpty()) {
            sb.append(TAB).append(TAB)
                    .append(getTokenName(RUNS))
                    .append(getTokenName(COLON))
                    .append(RET);
            int i = 1;
            for (Execution execution : executions) {
                sb.append(printExecution(execution, i,2));
                i++;
            }
        }
        return sb.toString();
    }

    public String printExecution(Execution execution, int resultIndex, int tabs) {
        StringBuilder sb = new StringBuilder();
        String id = execution.getId();
        if (id == null || id.equals("")) {
            id = "R" + resultIndex;
        }
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale.ENGLISH);
        for(int i=0;i<tabs;i++)
            sb.append(TAB);
        sb.append(id)
            .append(getTokenName(COLON));
        if (execution.getStart() != null) {
            sb.append(ESP)
                    .append(getTokenName(START))
                    .append(getTokenName(COLON))
                    .append("'" + df.format(execution.getStart()) + "'");
        }
        if (execution.getFinish() != null) {
            sb.append(ESP)
                    .append(getTokenName(END))
                    .append(getTokenName(COLON))
                    .append("'" + df.format(execution.getFinish()) + "'");
        }
        sb.append(RET);
        if (execution.getLog() != null && execution.getLog() instanceof SimpleLog) {
            SimpleLog slog = (SimpleLog) execution.getLog();
            if (slog.getDescription() != null && !"".equals(slog.getDescription())) {
                for(int i=0;i<tabs+1;i++)
                    sb.append(TAB);
                sb.append(getTokenName(LOG))
                    .append(getTokenName(COLON))
                    .append("'" + slog.getDescription() + "'")
                    .append(RET);
            }
        }
        for(int i=0;i<tabs+1;i++)
                    sb.append(TAB);
        sb.append(getTokenName(RESULT))
            .append(getTokenName(COLON))
            .append(RET);

        if (!execution.getResults().isEmpty()) {
            for (ExperimentalResult expresult : execution.getResults()) {
                sb.append(printExperimentalResult(expresult,tabs+2));
            }
        }
        return sb.toString();
    }

    private String printAnalysisResult(AnalysisResult analysisResult) {
        return "";
    }

    private String printExperimentalResult(ExperimentalResult experimentalResult,int tabs) {
        StringBuilder sb = new StringBuilder();
        ResultsFile result = (ResultsFile) experimentalResult;
        for(int i=0;i<tabs;i++)
            sb.append(TAB);
        sb.append(getTokenName(FILE))
            .append(getTokenName(COLON))
            .append(ESP)
            .append("'").append(result.getFile().getName()).append("'");
        return sb.toString();
    }

    public String printOutputs(ExperimentalOutputs outputs) {
        StringBuilder sb = new StringBuilder();
        if (!outputs.getOutputDataSources().isEmpty()) {
            sb.append(
                    TAB
            ).append(getTokenName(
                    OUTPUTS
            )).append(getTokenName(
                    COLON
            )).append(
                    ESP);
            for (OutputDataSource out : outputs.getOutputDataSources()) {
                if (out != null) {
                    sb.append(getTokenName(
                            FILE
                    )).append(
                            ESP
                    ).append(
                            out.getFile() != null ? "'"+out.getFile().getName()+"'" : "''"
                    ).append(
                            ESP);
                    if (out.getRole() != null) {
                        sb.append(getTokenName(
                                ROLE
                        )).append(getTokenName(
                                COLON
                        )).append(
                                out.getRole().name()
                        );
                    }

                    if (out.getFileSpecification() != null) {
                        sb.append(getTokenName(
                                FORMAT
                        )).append(getTokenName(
                                COLON
                        )).append(
                                out.getFileSpecification().getNamePattern()
                        );
                    }
                    // Falta el mapping
                }
            }
            sb.append(RET);
        }

        return sb.toString();
    }

    public String printExperimentalSetting(ExperimentalSetting setting) {
        StringBuilder sb = new StringBuilder();
        // SETTINGS
        if (setting != null) {
            sb.append(
                    TAB
            ).append(getTokenName(
                    SETTING
            )).append(getTokenName(
                    COLON
            )).append(
                    ESP);
            if (setting.getRequirements() != null) {
                sb.append(
                        printExperimentalEnvironment(setting.getRequirements())
                );

                // HardwarePlatform?
                sb.append(RET);
            }
        }
        return sb.toString();
    }

    private String printExperimentalEnvironment(ExperimentalEnvironment req) {
        String s = "";
        if (req instanceof ComputationEnvironment) {
            s=printComputationEnvironment((ComputationEnvironment) req);
        }
        return s;
    }

    private String printComputationEnvironment(ComputationEnvironment env) {
        StringBuilder sb = new StringBuilder();
        if (env.getSoftwarePlatform() != null) {
            if (!env.getSoftwarePlatform().getRuntimes().isEmpty()) {
                sb.append(RET).append(TAB).append(TAB)
                  .append(getTokenName(
                        RUNTIMES
                )).append(getTokenName(
                        COLON
                )).append(
                        ESP);
                for (int j = 0; j < env.getSoftwarePlatform().getRuntimes().size(); j++) {
                    if (j != 0) {
                        sb.append(getTokenName(
                                COMMA
                        )).append(
                                ESP);
                    }
                    Runtime r = env.getSoftwarePlatform().getRuntimes().get(j);
                    sb.append(
                            r.getName()
                    ).append(
                            ESP
                    ).append(
                            r.getVersion()
                    ).append(
                            ESP);
                }                
            }

            if (!env.getSoftwarePlatform().getLibraries().isEmpty()) {
                sb.append(RET).append(TAB).append(TAB)
                .append(getTokenName(
                        LIBRARIES
                )).append(getTokenName(
                        COLON
                )).append(
                        ESP);
                for (int j = 0; j < env.getSoftwarePlatform().getLibraries().size(); j++) {
                    if (j != 0) {
                        sb.append(getTokenName(
                                COMMA
                        )).append(
                                ESP);
                    }
                    Library l = env.getSoftwarePlatform().getLibraries().get(j);
                    sb.append(
                            l.getName()
                    ).append(
                            ESP
                    ).append(
                            l.getVersion()
                    );
                }
            }
        }
        return sb.toString();
    }

    public String printExperimentalProcedure(ExperimentalProcedure procedure) {
        StringBuilder sb = new StringBuilder();
        if (procedure != null && procedure instanceof TaskBasedExperimentalProcedure) {
            sb.append(
                    TAB
            ).append(getTokenName(
                    PROCEDURE
            )).append(
                    ESP
            ).append(getTokenName(
                    COLON
            )).append(
                    RET
            );
            TaskBasedExperimentalProcedure myprocedure = (TaskBasedExperimentalProcedure) procedure;
            if (!myprocedure.getTasks().isEmpty()) {
                for (ExperimentalTask expTask : myprocedure.getTasks()) {
                    CommandExperimentalTask task = (CommandExperimentalTask) expTask;
                    sb.append(
                            TAB
                    ).append(
                            TAB
                    ).append(getTokenName(
                            COMMAND
                    )).append(
                            ESP
                    ).append(getTokenName(
                            AS
                    )).append(
                            ESP
                    ).append(getTokenName(
                            TREATMENT
                    )).append(
                            ESP
                    ).append(getTokenName(
                            OPEN_PAR
                    ));

                    for (int j = 0; j < task.getParameters().size(); j++) {
                        if (j != 0) {
                            sb.append(getTokenName(
                                    COMMA
                            )).append(
                                    ESP);
                        }
                        sb.append(task.getParameters().get(j));
                    }

                    sb.append(
                            ESP
                    ).append(getTokenName(
                            CLOSE_PAR
                    )).append(getTokenName(
                            COLON
                    ))
                    .append(RET)
                    .append(TAB)
                    .append(TAB)
                    .append(TAB)
                    .append("'")
                    .append(task.getName())
                    .append("'");
                }
            }
        }
        return sb.toString();
    }

    private String getTokenName(int token) {
        String tk = tokenNames[token];
        return tk.substring(1, tk.length() - 1).trim();
    }

    private String printAssociationalHypthesis(AssociationalHypothesis associationalHypothesis) {
        StringBuilder sb = new StringBuilder();

        sb.append(associationalHypothesis.getDependentVariable());

        sb.append(getTokenName(IS))
                .append(ESP);
        if (associationalHypothesis.getRelation() != null) {
            sb.append(associationalHypothesis.getRelation().toString())
                    .append(ESP);
        }

        sb.append(getTokenName(CORRELATED))
                .append(ESP)
                .append(getTokenName(WITH))
                .append(ESP);

        sb.append(associationalHypothesis.getIndependentVariables().get(0));

        for (int i = 1; i < associationalHypothesis.getIndependentVariables().size(); i++) {
            sb.append(getTokenName(COMMA));
            sb.append(ESP);
            sb.append(associationalHypothesis.getIndependentVariables().get(i));
        }

        return sb.toString();
    }

    private String printDescriptiveHypothesis(DescriptiveHypothesis descriptiveHypothesis) {
        StringBuilder sb = new StringBuilder();
        sb.append(getTokenName(DESCRIPTIVE));
        return sb.toString();
    }

    private String printDifferentialHypothesis(DifferentialHypothesis differentialHypothesis) {
        StringBuilder sb = new StringBuilder();
        if(differentialHypothesis.getIndependentVariables().size()>1){        
            sb.append(differentialHypothesis.getIndependentVariables().get(0));
            for (int i = 1; i < differentialHypothesis.getIndependentVariables().size(); i++) {
                sb.append(getTokenName(COMMA));
                sb.append(ESP);
                sb.append(differentialHypothesis.getIndependentVariables().get(i));
            }
        } else {
            sb.append(getTokenName(DIFFERENTIAL));
            return sb.toString();
        }
        sb.append(ESP);

        sb.append(getTokenName(IMPACTS))
                .append(ESP)
                .append(getTokenName(SIGNIFICANTLY))
                .append(ESP)
                .append(getTokenName(ON))
                .append(ESP);

        sb.append(differentialHypothesis.getDependentVariable());
        return sb.toString();
    }
    private String printVariableValuations(List<VariableValuation> valuations, String separator)
    {
        return printVariableValuations(valuations,separator,true);
    }

    private String printVariableValuations(List<VariableValuation> valuations, String separator,boolean ignoreNull) {
        StringBuilder sb=new StringBuilder();
        if (!valuations.isEmpty() ) {
            if(valuations.get(0).getLevel()!=null){
                sb.append(valuations.get(0).getVariable())
                    .append(separator)
                    .append(printValue(valuations.get(0).getLevel()) + ESP);
            }else if(ignoreNull){
                sb.append(RET);
            } else {
                sb.append(valuations.get(0).getVariable());
            }
            for (int i = 1; i < valuations.size(); i++) {
                if(valuations.get(i).getLevel()!=null){
                    sb.append(getTokenName(COMMA))
                        .append(valuations.get(i).getVariable())
                        .append(separator)
                        .append(printValue(valuations.get(i).getLevel()) + ESP);
                }else if(ignoreNull){
                    sb.append(RET);
                } else {
                    sb.append(getTokenName(COMMA))
                        .append(valuations.get(i).getVariable());
                }
            }
        }
        return sb.toString();
    }

	public String printInputs(ExperimentalInputs inputs, int i) {
        StringBuilder sb=new StringBuilder();
        if(inputs!=null){
            for(int j=0;j<i;j++)
                sb.append(TAB);
            sb.append(getTokenName(INPUTS))
                .append(getTokenName(COLON));
            if(inputs.getInputDataSources().size()>1){
                sb.append(RET);
                i++;
                for(int j=0;j<i;j++)
                    sb.append(TAB);
            }else{
                sb.append(ESP);
            }
            for(InputDataSource input:inputs.getInputDataSources()){                
                sb.append(getTokenName(FILE))
                    .append(ESP)
                    .append("'");
                if(input.getFile().getPath()!=null)
                    sb.append(input.getFile().getPath()).append("/");
                sb.append(input.getFile().getName());
                sb.append("'");
                if(inputs.getInputDataSources().size()>1)
                    sb.append(RET);
            }
            
        }
        return sb.toString();
    }
    
    public String printAnalysisExecutionResult(AnalysisResult ar,int tabs){
        String result=null;
        if(ar instanceof StatisticalAnalysisResult){
            result=printStatisicalAnalysisResult((StatisticalAnalysisResult)ar,tabs);
        }
        return result;
    }

    private String printStatisicalAnalysisResult(StatisticalAnalysisResult ar, int tabs) {
        StringBuilder sb=new StringBuilder();
        for(int i=0;i<tabs;i++)
            sb.append(TAB);
        if(ar.getId()!=null && !ar.getId().equals("")){
            sb.append(ar.getId())
                .append(getTokenName(COLON));
            sb.append(TAB);
        }
                    
        if(ar instanceof DescriptiveStatisticValue){
            DescriptiveStatisticValue ds=((DescriptiveStatisticValue)ar);
            sb.append(ds.getDescriptiveStatistic())
                .append(getTokenName(OPEN_PAR))
                .append(printDatasetSpecification(ar.getDatasetSpecification()))
                .append(getTokenName(CLOSE_PAR))
                .append(getTokenName(COLON))
                .append(" ")
                .append(ds.getValue());
        }else if(ar instanceof PValue){
            PValue pvalue=(PValue)ar;
            sb.append(pvalue.getNhst())
                .append(getTokenName(OPEN_PAR))
                .append(printDatasetSpecification(ar.getDatasetSpecification()))
                .append(getTokenName(COMMA))
                .append(pvalue.getSignificanceThreshold())
                .append(getTokenName(CLOSE_PAR))
                .append(getTokenName(COLON))
                .append(" ")
                .append(pvalue.getValue());
        }        
        return sb.toString();
    }

}
