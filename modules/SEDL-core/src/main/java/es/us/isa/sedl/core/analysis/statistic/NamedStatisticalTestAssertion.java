/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package es.us.isa.sedl.core.analysis.statistic;

/**
 *
 * @author japarejo
 */
public class NamedStatisticalTestAssertion extends StatisticalTestAssertion{
       private String name;
       private Nhst test;
       private boolean expectedDifference;

    public NamedStatisticalTestAssertion() {
    }    
       
    public NamedStatisticalTestAssertion(String name, Nhst test, boolean expectedDifference) {
        this.name = name;
        this.test = test;
        this.expectedDifference = expectedDifference;
    }

    public String getName() {
        return name;
    }

    public Nhst getTest() {
        return test;
    }

    public boolean isExpectedDifference() {
        return expectedDifference;
    }
       
       
}
