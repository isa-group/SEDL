/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package es.us.isa.sedl.marshaller;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.junit.Test;
import org.stringtemplate.v4.ST;

import es.us.isa.sedl.core.ControlledExperiment;
import es.us.isa.sedl.core.analysis.datasetspecification.DatasetSpecification;
import es.us.isa.sedl.core.analysis.datasetspecification.GroupingProjection;
import es.us.isa.sedl.core.analysis.datasetspecification.Projection;
import es.us.isa.sedl.core.analysis.datasetspecification.ValuationFilter;
import es.us.isa.sedl.core.analysis.statistic.Mean;
import es.us.isa.sedl.core.analysis.statistic.Median;
import es.us.isa.sedl.core.analysis.statistic.StatisticalAnalysisSpec;
import es.us.isa.sedl.core.configuration.CommandExperimentalTask;
import es.us.isa.sedl.core.configuration.ComplexParameter;
import es.us.isa.sedl.core.configuration.ComputationEnvironment;
import es.us.isa.sedl.core.configuration.Configuration;
import es.us.isa.sedl.core.configuration.ExperimentalInputs;
import es.us.isa.sedl.core.configuration.ExperimentalOutputs;
import es.us.isa.sedl.core.configuration.ExperimentalProcedure;
import es.us.isa.sedl.core.configuration.ExperimentalSetting;
import es.us.isa.sedl.core.configuration.File;
import es.us.isa.sedl.core.configuration.InputDataSource;
import es.us.isa.sedl.core.configuration.Library;
import es.us.isa.sedl.core.configuration.OutputDataSource;
import es.us.isa.sedl.core.configuration.OutputDataSourceRole;
import es.us.isa.sedl.core.configuration.Runtime;
import es.us.isa.sedl.core.configuration.SimpleParameter;
import es.us.isa.sedl.core.configuration.SoftwarePlatform;
import es.us.isa.sedl.core.configuration.TaskBasedExperimentalProcedure;
import es.us.isa.sedl.core.context.Context;
import es.us.isa.sedl.core.context.People;
import es.us.isa.sedl.core.context.Person;
import es.us.isa.sedl.core.context.Project;
import es.us.isa.sedl.core.design.AnalysisSpecificationGroup;
import es.us.isa.sedl.core.design.AssignmentMethod;
import es.us.isa.sedl.core.design.ControllableFactor;
import es.us.isa.sedl.core.design.Design;
import es.us.isa.sedl.core.design.ExtensionDomain;
import es.us.isa.sedl.core.design.FullySpecifiedExperimentalDesign;
import es.us.isa.sedl.core.design.FundamentalSet;
import es.us.isa.sedl.core.design.FundamentalSetConstraint;
import es.us.isa.sedl.core.design.Group;
import es.us.isa.sedl.core.design.IntensionDomain;
import es.us.isa.sedl.core.design.IntervalConstraint;
import es.us.isa.sedl.core.design.Level;
import es.us.isa.sedl.core.design.LiteralSizing;
import es.us.isa.sedl.core.design.NonControllableFactor;
import es.us.isa.sedl.core.design.Outcome;
import es.us.isa.sedl.core.design.Population;
import es.us.isa.sedl.core.design.SamplingMethod;
import es.us.isa.sedl.core.design.Variable;
import es.us.isa.sedl.core.design.VariableKind;
import es.us.isa.sedl.core.design.VariableValuation;
import es.us.isa.sedl.core.design.Variables;
import es.us.isa.sedl.core.execution.Execution;
import es.us.isa.sedl.core.execution.ResultsFile;
import es.us.isa.sedl.core.hypothesis.DifferentialHypothesis;

/**
 *
 * @author japarejo
 */
public class SEDL4PeopleMarshallerTest extends AbstractMarshallingTest{     
    
    
    //private static final String NEW_LINE=System.getProperty("line.separator");
    private static final String NEW_LINE="\n";
    private static final String TAB="\t";
    //==================================
    // PREAMBLE TESTS
    //==================================
    
    @Test
    public void testExperimentPreamble1()
    {
        // Create experiment
        ControlledExperiment exp=new ControlledExperiment();
        exp.setId("myExperiment");
        exp.setVersion("1.0");        
        
        String expectedResult="EXPERIMENT: myExperiment version: 1.0";        
        
        /*ST st=getTemplate("preamble",exp,"e");        
        String result=st.render();*/

        SEDL4PeopleMarshaller marshaller=new SEDL4PeopleMarshaller();
        String result=marshaller.writeExperimentPreamble(exp);       

        assertEquals(result.trim(),expectedResult.trim());
    }
    
    @Test
    public void testExperimentPopulation()
    {
        Population p = new Population();
        p.setIndividualDescription("'Run of ETHOM for the parameters specified'");
        p.setAccesiblePopulation("'Any run of ETHOM with a valid tuning for the parameters specified'");
        
        //ST st=getTemplate("population",p,"p");        
        //String result=st.render();
        
        ControlledExperiment e=new ControlledExperiment();
        Design d=new Design();
        e.setDesign(d);
        d.setPopulation(p);
        
        SEDL4PeopleMarshaller marshaller=new SEDL4PeopleMarshaller();
        String result=marshaller.writeExperimentContext(e);       
        
        String expectedResult=
                TAB+"Object:'Run of ETHOM for the parameters specified'"+NEW_LINE+
                TAB+"Population:'Any run of ETHOM with a valid tuning for the parameters specified'";
        
        assertEquals(expectedResult.trim(),result.trim());
        
    }
    
    
    //==================================
    // DESIGN & VARIABLES TESTS
    //==================================
    
    @Test
    public void testExperimentConstants()
    {
        ControlledExperiment e = new ControlledExperiment();
        Design design=new Design();
        SimpleParameter constant = new SimpleParameter();
        constant.setName("C");
        constant.setValue("test");
        design.getDesignParameters().add(constant);
        e.setDesign(design);
        String expectedResult=
                  "Constants:"+NEW_LINE
                + TAB+"C : 'test'";        
       // ST st=getTemplate("constants",e,"e");   
        //st.inspect();
        //String result=st.render();
        SEDL4PeopleMarshaller marshaller=new SEDL4PeopleMarshaller();
        String result=marshaller.writeConstantsBlock(e);       
        assertEquals(expectedResult.trim(),result.trim());                
    }
    
     @Test
    public void testExperimentComplexConstants()
    {
        ControlledExperiment e = new ControlledExperiment();
        Design design=new Design();
        ComplexParameter constant = new ComplexParameter();
        constant.setName("RandomNumberGenerator");
        SimpleParameter s1 = new SimpleParameter();
        s1.setName("desc");
        s1.setValue("'Standard Java RND'");
        SimpleParameter s2 = new SimpleParameter();
        s2.setName("class");
        s2.setValue("'java.util.Random'");
        constant.getParameters().add(s1);
        constant.getParameters().add(s2);
        design.getDesignParameters().add(constant);
        e.setDesign(design);
        String expectedResult=
                  "Constants:"+NEW_LINE
                + TAB + "RandomNumberGenerator : {desc:'Standard Java RND',class:'java.util.Random'}";
        //ST st=getTemplate("constants",e,"e");   
        //st.inspect();
        //String result=st.render();
        SEDL4PeopleMarshaller marshaller=new SEDL4PeopleMarshaller();
        String result=marshaller.writeConstantsBlock(e);       
        assertEquals(expectedResult.trim(),result.trim());                
    }
    
    @Test
    public void testExperimentVariables()
    {
        ControlledExperiment e=new ControlledExperiment();
        Design design=new Design();
        e.setDesign(design);
        Variables vars=new Variables();        
        design.setVariables(vars);
        Variable factor=new ControllableFactor();
        factor.setName("cf1");
        ExtensionDomain d=new ExtensionDomain();
        factor.setDomain(d);
        Level l1=new Level();
        l1.setValue("l1");
        Level l2=new Level();
        l2.setValue("l2");
        d.getLevels().add(l1);
        d.getLevels().add(l2);
        vars.getVariables().add(factor);
        Variable v2=new NonControllableFactor();
        v2.setName("ncf");
        v2.setDomain(d);
        design.getVariables().getVariables().add(v2);
        Outcome outcome=new Outcome();
        outcome.setName("o");
        FundamentalSetConstraint fsc=new FundamentalSetConstraint();
        fsc.setFundamentalSet(FundamentalSet.R);
        IntensionDomain id=new IntensionDomain();
        id.getConstraint().add(fsc);                
        outcome.setDomain(id);
        vars.getVariables().add(outcome);
        design.setVariables(vars);
        String expectedResult=
                  "Variables :"+NEW_LINE
                + TAB+"Factors :"+NEW_LINE
                + TAB+TAB+"cf1 enum 'l1', 'l2'"+NEW_LINE
                + TAB+"NCFactors :"+NEW_LINE
                + TAB+TAB+"ncf enum 'l1', 'l2'"+NEW_LINE
                + TAB+"Outcome :"+NEW_LINE
                + TAB+TAB+"o float "+NEW_LINE;        
        //ST st=getTemplate("variables",design,"design");   
        //st.inspect();
        SEDL4PeopleMarshaller marshaller=new SEDL4PeopleMarshaller();
        String result=marshaller.writeVariablesBlock(e);       
        assertEquals(expectedResult.trim(),result.trim());                
    }
        
    @Test
    public void testFullySpecifiedExperimentalDesign()
    {
        ControlledExperiment e = new ControlledExperiment();
        Design design=new Design();
        design.setVariables(new Variables());
        ControllableFactor f=new ControllableFactor();
        f.setName("Vname");
        design.getVariables().getVariables().add(f);
        FullySpecifiedExperimentalDesign fsed=buildFullySpecifiedExperimentalDesign(buildVariable());
        design.setExperimentalDesign(fsed);
        e.setDesign(design);
        String expectedResult=
                "Design :"+NEW_LINE
                + TAB+"Assignment : Random"+NEW_LINE
                + TAB+"Blocking : Vname"+NEW_LINE
                + TAB+"Groups : "+NEW_LINE
                + TAB+TAB+"by Vname sizing 40"+NEW_LINE
                + TAB+"Protocol :";/*Random"+NEW_LINE   
                + TAB+"Analyses:"+NEW_LINE;               */
        //ST st=getTemplate("FullySpecifiedExperimentalDesign",e,"e");        
        //String result=st.render();
        SEDL4PeopleMarshaller marshaller=new SEDL4PeopleMarshaller();
        String result=marshaller.writeDesign(e);       
        assertEquals(expectedResult.trim(),result.trim());                
    }
    
    @Test
    public void testExperimentalDesign()
    {
        ControlledExperiment e = new ControlledExperiment();
        Design design=new Design();
        Variables vars=new Variables();       
        design.setVariables(vars); 
        Variable var=buildVariable();
        vars.getVariables().add(var);
        FullySpecifiedExperimentalDesign fsed=buildFullySpecifiedExperimentalDesign(var);
        design.setExperimentalDesign(fsed);
        design.getVariables().getVariables().add(var);
        e.setDesign(design);                
        String expectedResult=
                  "Design :"+NEW_LINE
                //+ "        Sampling : Custom"+NEW_LINE
                +TAB+"Assignment : Random"+NEW_LINE
                +TAB+"Blocking : Vname"+NEW_LINE
                +TAB+"Groups : "+NEW_LINE
                +TAB+TAB+ "by Vname sizing 40"+NEW_LINE
                +TAB+"Protocol :";
        SEDL4PeopleMarshaller marshaller=new SEDL4PeopleMarshaller();
        String result=marshaller.writeDesign(e);       
        assertEquals(expectedResult.trim(),result.trim());                
    }
    
    @Test
    public void testExperimentalDesignWithAnalyses()
    {
        ControlledExperiment e = new ControlledExperiment();
        Design design=new Design();
        FullySpecifiedExperimentalDesign fsed=buildFullySpecifiedExperimentalDesign(buildVariable());
       
        Mean mean = new Mean();
        Median median = new Median();
        StatisticalAnalysisSpec a1 = new StatisticalAnalysisSpec();
        a1.setId("A1");
        a1.getStatistic().add(mean);
        StatisticalAnalysisSpec a2 = new StatisticalAnalysisSpec();
        a2.setId("A2");
        a2.getStatistic().add(median);
        
        fsed.getIntendedAnalyses().add(a1);        
        fsed.getIntendedAnalyses().add(a2);        
         design.setExperimentalDesign(fsed);
        e.setDesign(design);
        String expectedResult=
                  TAB+"Analyses :"+NEW_LINE
                + TAB+"A1:"+NEW_LINE
                + TAB+TAB+"Avg()" +NEW_LINE 		
                + TAB+"A2:"+NEW_LINE
                +TAB+TAB+"Median()"+NEW_LINE;               
        //ST st=getTemplate("FullySpecifiedExperimentalDesign",e,"e");        
        //String result=st.render();
        SEDL4PeopleMarshaller marshaller=new SEDL4PeopleMarshaller();
        String result=marshaller.writeAnalyses(e);
        //st.inspect();
        assertEquals(expectedResult.trim(),result.trim());                
    }
    
    @Test
    public void testAnalysis()
    {
        Mean mean = new Mean();
        StatisticalAnalysisSpec a1 = new StatisticalAnalysisSpec();
        a1.setId("A1");
        a1.getStatistic().add(mean);
        String expectedResult=
                  "A1:"+NEW_LINE
                  + TAB + "Avg()" +NEW_LINE;               
        //ST st=getTemplate("FullySpecifiedExperimentalDesign",e,"e");        
        //String result=st.render();
        SEDL4PeopleMarshaller marshaller=new SEDL4PeopleMarshaller();
        String result=marshaller.printAnalysisGroup(a1,0);
        assertEquals(expectedResult.trim(),result.trim());                
    }
    
    protected Variable buildVariable()
    {
        Variable v=new ControllableFactor();
        v.setName("Vname");
        v.setKind(VariableKind.NOMINAL);
        ExtensionDomain ed=new ExtensionDomain();
        Level l1=new Level();
        l1.setValue("l1");
        Level l2=new Level();
        l2.setValue("l2");
        ed.getLevels().add(l1);
        ed.getLevels().add(l2);
        v.setDomain(ed);                
        return v;
    }
    
    protected Design buildDesign()
    {
        Design design=new Design();
        Variables vars=new Variables();        
        design.setVariables(vars);
        Variable factor=new ControllableFactor();
        factor.setName("cf1");
        ExtensionDomain d=new ExtensionDomain();
        factor.setDomain(d);
        Level l1=new Level();
        l1.setValue("l1");
        Level l2=new Level();
        l2.setValue("l2");
        d.getLevels().add(l1);
        d.getLevels().add(l2);
        vars.getVariables().add(factor);
        Variable v2=new NonControllableFactor();
        v2.setName("ncf");
        v2.setDomain(d);
        design.getVariables().getVariables().add(v2);
        Outcome outcome=new Outcome();
        outcome.setName("o");
        FundamentalSetConstraint fsc=new FundamentalSetConstraint();
        fsc.setFundamentalSet(FundamentalSet.R);
        IntensionDomain id=new IntensionDomain();
        id.getConstraint().add(fsc);                
        outcome.setDomain(id);
        vars.getVariables().add(outcome);
        design.setVariables(vars);       
        return design;
    }
    
    protected FullySpecifiedExperimentalDesign buildFullySpecifiedExperimentalDesign(Variable factor)
    {    
        FullySpecifiedExperimentalDesign fsed=new FullySpecifiedExperimentalDesign();
        Group g=new Group();
        g.setName("g1");
        VariableValuation v=new VariableValuation();
        v.setVariable(factor.getName());        
        v.setLevel(null);
        LiteralSizing sizing=new LiteralSizing();
        sizing.setValue(BigInteger.valueOf(40));
        g.setSizing(sizing);
        g.getValuations().add(v);
        fsed.getGroups().add(g);
        SamplingMethod sampling=new SamplingMethod();
        sampling.setRandom(true);
        AssignmentMethod assignment=new AssignmentMethod();
        assignment.setRandom(true);
        assignment.getBlockingVariables().add("Vname");        
        fsed.setAssignmentMethod(assignment);        
        return fsed;
    }
    
    @Test
    public void testexperimentVariable1()
    {
        Variable v=buildVariable();
        ST st=getTemplate("variable",v,"v");        
        String result=st.render();
        String expectedResult="Vname enum \"l1\", \"l2\" ";
        assertEquals(expectedResult,result);
    }
    
    @Test
    public void testexperimentVariableWithUnits()
    {
        Variable v=buildVariableWithUnits();
        ST st=getTemplate("variable",v,"v");
        
        String result=st.render();
        String expectedResult="Vname enum \"l1\", \"l2\" measured in units";
        assertEquals(expectedResult,result);
    }
    
    @Test
    public void testexperimentVariableIntensionDomainInterval()
    {
        Variable v=buildVariableWithInterval();
        ST st=getTemplate("variable",v,"v");
        
        String result=st.render();
        String expectedResult="Vname range [0,100]";
        assertEquals(expectedResult.trim(),result.trim());
    }
    
      @Test
    public void testexperimentVariableIntensionDomainFundamentalSet()
    {
        Variable v=buildVariableWithFundamentalSet();
        ST st=getTemplate("variable",v,"v");
        
        String result=st.render();
        String expectedResult="Vname in N";
        assertEquals(expectedResult.trim(),result.trim());
    }
    
    
    protected Variable buildVariableWithUnits()
    {
        Variable v=new ControllableFactor();
        v.setName("Vname");
        v.setKind(VariableKind.NOMINAL);
        v.setUnits("units");
        ExtensionDomain ed=new ExtensionDomain();
        Level l1=new Level();
        l1.setValue("l1");
        Level l2=new Level();
        l2.setValue("l2");
        ed.getLevels().add(l1);
        ed.getLevels().add(l2);
        v.setDomain(ed);                
        return v;
    }
    
    protected Variable buildVariableWithFundamentalSet()
    {
        Variable v=new ControllableFactor();
        v.setName("Vname");
        v.setKind(VariableKind.SCALAR);
        IntensionDomain id=new IntensionDomain();
        FundamentalSetConstraint c = new FundamentalSetConstraint();
        c.setFundamentalSet(FundamentalSet.N);
        id.getConstraint().add(c);
        v.setDomain(id);
        return v;
    }
    
    protected Variable buildVariableWithInterval()
    {
        Variable v=new ControllableFactor();
        v.setName("Vname");
        v.setKind(VariableKind.SCALAR);
        IntensionDomain id=new IntensionDomain();
        IntervalConstraint c = new IntervalConstraint();
        c.setMin("0");
        c.setMax("100");
        id.getConstraint().add(c);
        v.setDomain(id);
        return v;
    }
    
    
    //----------------------------------
    // Statistics & Data related TESTS
    //----------------------------------
    
    
    
    
    
    //==================================
    // Hypotheses TESTS
    //==================================
    
    
    @Test
    public void testDifferentialHypothesis()
    {
        ControlledExperiment e=new ControlledExperiment();
        DifferentialHypothesis dh=new DifferentialHypothesis();
        dh.setId("H1");
        e.getHypotheses().add(dh);
        
        String expectedResult="Hypothesis: "+TAB+"H1: Differential";
        
        //ST st=getTemplate("hypotheses",e,"e");                
        //st.inspect();
        //String result=st.render();
        
        SEDL4PeopleMarshaller marshaller=new SEDL4PeopleMarshaller();
        String result=marshaller.writeHypothesis(e);       
        assertEquals(expectedResult.trim(), result.trim());                
    }
    
    //==================================
    // Configuration Tests
    //==================================
    
    
    
    
    @Test
    public void testSetting()
    {
        ExperimentalSetting setting=buildExperimentalSetting();
        
        //ST st=getTemplate("setting", setting, "s");        
        //String result=st.render();
        SEDL4PeopleMarshaller marshaller=new SEDL4PeopleMarshaller();
        String result=marshaller.printExperimentalSetting(setting);       
        String expectedResult=
                    TAB +"Setting: "+NEW_LINE
                + TAB + TAB +"Runtimes: java 1.7 "+NEW_LINE
                + TAB + TAB +"Libraries: FOM 0.5"+NEW_LINE;
        assertEquals(expectedResult, result);
        
    }
    
    protected ExperimentalSetting buildExperimentalSetting()
    {
        ExperimentalSetting setting=new ExperimentalSetting();
        ComputationEnvironment compEnv=new ComputationEnvironment();
        SoftwarePlatform sp=new SoftwarePlatform();
        Runtime java=new Runtime();
        java.setName("java");
        java.setVersion("1.7");
        sp.getRuntimes().add(java);
        Library library=new Library();
        library.setName("FOM");
        library.setVersion("0.5");
        sp.getLibraries().add(library);
        compEnv.setSoftwarePlatform(sp);
        setting.setRequirements(compEnv);
        return setting;
    }
    
    @Test
    public void testProcedure()
    {
        ExperimentalProcedure procedure=buildExperimentalProcedure();
        
        //ST st=getTemplate("procedure", procedure, "p");        
        //String result=st.render();
        SEDL4PeopleMarshaller marshaller=new SEDL4PeopleMarshaller();
        String result=marshaller.printExperimentalProcedure(procedure);          
        String expectedResult=
                  "Procedure :"+NEW_LINE
                + TAB+TAB+"Command as Treatment (JaCoPHeuristic, NFeatures, CTC ):"+NEW_LINE
                + TAB+TAB+TAB+"'java -jar ETHOM Results-ETHOM-4.csv'";
        assertEquals(expectedResult.trim(), result.trim());
    }
    
    protected ExperimentalProcedure buildExperimentalProcedure() {
        TaskBasedExperimentalProcedure proc=new TaskBasedExperimentalProcedure();
        CommandExperimentalTask task=new CommandExperimentalTask();
        task.setExperimentalTaskType("Treatment");
        task.getParameters().add("JaCoPHeuristic");
        task.getParameters().add("NFeatures");
        task.getParameters().add("CTC");
        task.setName("java -jar ETHOM Results-ETHOM-4.csv");        
        proc.getTasks().add(task);
        
        return proc;
    }
    
    @Test
    public void testExecution()
    {
        Execution exec=buildExecution();
        //ST st=getTemplate("run",exec, "r");        
        //String result=st.render();
        SEDL4PeopleMarshaller marshaller=new SEDL4PeopleMarshaller();
        String result=marshaller.printExecution(exec,1,0);       
        String expectedResult=
                  "E1: Start:'3915-05-02T00:00:00' End:'3915-05-02T00:00:00'"+NEW_LINE
                    +TAB+"Result:"+NEW_LINE
                    +TAB+TAB+"File: 'myExecResults.csv'";
        assertEquals(expectedResult, result);
    }
    
    protected Execution buildExecution()
    {
        Execution exec=new Execution();
        exec.setId("E1");
        Date d=new Date(2015, 04, 02);
        exec.setStart(d);
        exec.setFinish(d);
        ResultsFile rf=new ResultsFile();
        File f=new File();
        f.setName("myExecResults.csv");
        rf.setFile(f);
        exec.getResults().add(rf);
        return exec;
    }
            
    @Test
    public void testInputs()
    {
        ExperimentalInputs inputs=buildInputs();
        //ST st=getTemplate("inputs",inputs,"is");
        //String result=st.render();
        SEDL4PeopleMarshaller marshaller=new SEDL4PeopleMarshaller();
        String result=marshaller.printInputs(inputs,0);       
        String expectedResult="Inputs: File 'inputs.csv'";
        assertEquals(expectedResult.trim(), result.trim());
    }
    
    public ExperimentalInputs buildInputs()
    {
        ExperimentalInputs result=new ExperimentalInputs();
        InputDataSource ids=new InputDataSource();
        File f=new File();
        f.setName("inputs.csv");
        ids.setFile(f);
        result.getInputDataSources().add(ids);
        return result;
    }
    
    @Test
    public void testOutputs()
    {
        ExperimentalOutputs outputs=buildOutputs();
        //ST st=getTemplate("outputs",outputs,"outs");
        //String result=st.render();
        SEDL4PeopleMarshaller marshaller=new SEDL4PeopleMarshaller();
        String result=marshaller.printOutputs(outputs);       
        String expectedResult="Outputs: File 'results.csv' role:MAIN_RESULT";
        assertEquals(expectedResult.trim(), result.trim());
    }
    
    public ExperimentalOutputs buildOutputs()
    {
        ExperimentalOutputs outputs=new ExperimentalOutputs();
        OutputDataSource ods=new OutputDataSource();
        File f=new File();
        f.setName("results.csv");
        ods.setRole(OutputDataSourceRole.MAIN_RESULT);
        ods.setFileSpecification(null);
        ods.setFile(f);
        outputs.getOutputDataSources().add(ods);
        return outputs;
    }
    
    @Test
    public void testConfiguration()
    {
        Configuration c=buildConfiguration();        
        //ST st=getTemplate("configuration", c, "c");                
        //String result=st.render();
        SEDL4PeopleMarshaller marshaller=new SEDL4PeopleMarshaller();
        String result=marshaller.printConfiguration(c,0);       
        String expectedResult=
                  "C1:"+NEW_LINE
                + TAB +"Inputs: File 'inputs.csv'"+NEW_LINE
                + TAB +"Outputs: File 'results.csv' role:MAIN_RESULT"+NEW_LINE
                + TAB +"Setting: "+NEW_LINE
                + TAB + TAB + "Runtimes: java 1.7 "+NEW_LINE
                + TAB + TAB +"Libraries: FOM 0.5"+NEW_LINE
                + TAB + "Procedure :"+NEW_LINE
                + TAB + TAB +"Command as Treatment (JaCoPHeuristic, NFeatures, CTC ):"+NEW_LINE
                + TAB + TAB + TAB +"'java -jar ETHOM Results-ETHOM-4.csv'";
        assertEquals(expectedResult.trim(), result.trim());
    }
    
    protected Configuration buildConfiguration()
    {
        Configuration c=new Configuration();
        c.setId("C1");
        c.setContext(buildExperimentalContext());
        c.setExperimentalSetting(buildExperimentalSetting());
        c.setExperimentalProcedure(buildExperimentalProcedure());
        c.setExperimentalInputs(buildInputs());
        c.setExperimentalOutputs(buildOutputs());
        return c;
    }            

    private Context buildExperimentalContext() {
        Context result=new Context();
        People p=new People();
        Person japarejo=new Person();
        japarejo.setName("J. A. Parejo");
        japarejo.setEmail("japarejo@us.es");
        japarejo.setId("japarejo");
        japarejo.getOrganization().add("University of Sevilla");
        japarejo.setRole("Main author");
        p.getPerson().add(japarejo);
        Project EXEMPLAR=new Project();
        EXEMPLAR.setName("EXpEriment Management PLAtfoRm");
        EXEMPLAR.setCode("EXEMPLAR");
        EXEMPLAR.setId("EXEMPLAR");
        EXEMPLAR.setWebsite("http://exemplar.us.es");        
        result.getProjects().add(EXEMPLAR);
        result.setPeople(p);
        return result;
    }
    
    //==================================
    // Execution Tests
    //==================================
    
    @Test
    public void testAnalysesWithProjection()
    {
        
        Mean mean = new Mean();
        
        Variable v=new ControllableFactor();
        v.setName("var1");
        v.setKind(VariableKind.NOMINAL); 
        ExtensionDomain ed=new ExtensionDomain();
        Level l1=new Level();
        l1.setValue("l1");
        Level l2=new Level();
        l2.setValue("l2");
        ed.getLevels().add(l1);
        ed.getLevels().add(l2);
        v.setDomain(ed);     

        VariableValuation vv=new VariableValuation();
        vv.setVariable(v.getName());        
        vv.setLevel("l2");
        
        ValuationFilter vf = new ValuationFilter();
        vf.getVariableValuations().add(vv);
        
        GroupingProjection gp =  new GroupingProjection();
        gp.getProjectedVariables().add("var2");
        
        DatasetSpecification ds1 = new DatasetSpecification();
        
        Projection p = new Projection();
        p.getProjectedVariables().add("var3");
        ds1.getGroupings().add(gp);
        ds1.getNonGroupingProjections().add(p);
        ds1.getProjections().add(p);
        ds1.getProjections().add(gp);
        ds1.getFilters().add(vf);
        
        mean.setDatasetSpecification(ds1);
        
        StatisticalAnalysisSpec a1 = new StatisticalAnalysisSpec();
        a1.setId("A1");
        a1.getStatistic().add(mean);        
        
        
        String expectedResult=
                  "A1:"+NEW_LINE
                  +TAB+"Avg( of var3 where var1='l2' by var2)" +NEW_LINE;               
        //ST st=getTemplate("analyses",a1,"a");        
        //String result=st.render();
        SEDL4PeopleMarshaller marshaller=new SEDL4PeopleMarshaller();
        String result=marshaller.printAnalysisGroup(a1,0);
        assertEquals(expectedResult.trim(), result.trim());
    }
    
        
}
