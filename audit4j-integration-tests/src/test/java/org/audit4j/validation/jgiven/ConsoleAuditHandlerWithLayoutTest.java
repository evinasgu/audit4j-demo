package org.audit4j.validation.jgiven;


import org.audit4j.core.layout.CustomizableLayout;
import org.audit4j.core.layout.Layout;
import org.audit4j.validation.jgiven.enumeration.Version;
import org.audit4j.validation.jgiven.stage.GivenSomeState;
import org.audit4j.validation.jgiven.stage.ThenSomeOutcome;
import org.audit4j.validation.jgiven.stage.WhenSomeAction;
import org.junit.Rule;
import org.junit.Test;
import org.junit.contrib.java.lang.system.SystemOutRule;

import com.tngtech.jgiven.junit.ScenarioTest;



public class ConsoleAuditHandlerWithLayoutTest
        extends ScenarioTest<GivenSomeState, WhenSomeAction, ThenSomeOutcome> {

    @Rule
    public final SystemOutRule systemOutRule = new SystemOutRule().enableLog();
    
    
    private Layout getLayout() {
    	CustomizableLayout layout = new CustomizableLayout();
    	layout.setTemplate("${eventDate}|gg|${uuid}|${actor}|${action}|${origin} => ${foreach fields field}[${field.name}:${field.value}]${end}");
    	
    	return layout;
    }
    
	
    @Test
    public void console_audit_handler_with_programming_configuration_receiving_a_message_with_event_builder() {
        given().audit4j_starting_with_a_programming_configuration_using_ConsoleAuditHandler_with_layout(getLayout());
        when().a_message_is_sent_to_the_audit_manager_with_event_builder()
        .and().audit_manager_is_stopped();
        then().the_console_log_contains_the_audit4j_logo_with_version_$(systemOutRule, Version.V250);
        //.and().the_console_log_contains_the_expected_output_for_event_builder(systemOutRule);
    }
/*    
    @Test
    public void console_audit_handler_with_programming_configuration_receiving_a_message_with_audit_event() {
        given().audit4j_starting_with_a_programming_configuration_using_ConsoleAuditHandler();
        when().a_message_is_sent_to_the_audit_manager_with_audit_event()
        .and().audit_manager_is_stopped();
        then().the_console_log_contains_the_audit4j_logo_with_version_$(systemOutRule, Version.V250)
        .and().the_console_log_contains_the_expected_output_for_audit_event(systemOutRule);
    }
    
    @Test
    public void console_audit_handler_with_programming_configuration_receiving_a_message_with_event_builder_during_disable_state() {
    	
        given().audit4j_starting_with_a_programming_configuration_using_ConsoleAuditHandler();
        when().audit_manager_is_disabled()
        .and().a_message_is_sent_to_the_audit_manager_with_event_builder()
        .and().audit_manager_is_stopped();
        then().the_console_log_contains_the_audit4j_logo_with_version_$(systemOutRule, Version.V250)         
        .but().the_console_log_does_not_contains_messages_for_event_builder(systemOutRule);
    }
    */


}
