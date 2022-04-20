package br.edu.ifpb.dac.ssp;

import org.junit.platform.suite.api.SelectPackages;
import org.junit.platform.suite.api.Suite;
import org.junit.platform.suite.api.SuiteDisplayName;

@SelectPackages({"br.edu.ifpb.dac.ssp.controller", "br.edu.ifpb.dac.ssp.service"
	, "br.edu.ifpb.dac.ssp.model.dto"})
@Suite
@SuiteDisplayName("Tests for functionalities implemented in Sprint 1")
class SchedulingSportPracticesApplicationTests {
	
}
