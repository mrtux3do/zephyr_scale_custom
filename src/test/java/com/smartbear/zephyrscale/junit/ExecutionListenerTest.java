package com.smartbear.zephyrscale.junit;

import com.smartbear.zephyrscale.junit.annotation.TestCase;
import com.smartbear.zephyrscale.junit.builder.CustomFormatContainerBuilder;
import com.smartbear.zephyrscale.junit.decorator.TestDescriptionDecorator;
import com.smartbear.zephyrscale.junit.customformat.CustomFormatExecution;
import com.smartbear.zephyrscale.junit.customformat.CustomFormatContainer;
import com.smartbear.zephyrscale.junit.file.CustomFormatFile;
import com.fasterxml.jackson.databind.ObjectMapper;
import net.serenitybdd.junit.runners.SerenityParameterizedRunner;
import net.thucydides.junit.annotations.UseTestDataFrom;
import org.junit.Test;
import org.junit.runner.Description;
import org.junit.runner.RunWith;
import org.junit.runner.notification.Failure;

import java.io.File;
import java.io.IOException;
import java.lang.annotation.Repeatable;
import java.lang.reflect.Method;

import static com.smartbear.zephyrscale.junit.customformat.CustomFormatConstants.FAILED;
import static com.smartbear.zephyrscale.junit.customformat.CustomFormatConstants.PASSED;
import static com.smartbear.zephyrscale.junit.file.CustomFormatFile.generateCustomFormatFile;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
//@RunWith(SerenityParameterizedRunner.class)
@UseTestDataFrom("data.csv")
public class ExecutionListenerTest {

    public String tcName;
    public String msg;

    class TestCaseAnnotationTest {
        @TestCase(name = "Test zephyr scale #tcName")
        public void shouldHaveTestCaseWithName(){

        }
    }

    @Test
    public void shouldNotSetKeyWhenItIsNotSetInTestCaseAnnotation() throws Exception {
        ExecutionListener executionListener = new ExecutionListener();
        executionListener.testRunStarted(null);

        Method method = TestCaseAnnotationTest.class.getDeclaredMethod("shouldHaveTestCaseWithName");

        Description descriptionJQAT1 = Description.createTestDescription(this.getClass(), method.getName(), method.getAnnotations());
        executionListener.testFinished(descriptionJQAT1);

        executionListener.testRunFinished(null);

        CustomFormatContainer customFormatContainer = getZephyrScaleJUnitResults();

        assertEquals(1, customFormatContainer.getExecutions().size());
        CustomFormatExecution jqat1Result = customFormatContainer.getExecutions().get(0);
        assertNull(jqat1Result.getTestCase().getKey());
    }

    @Test
    public void shouldNotSetKeyWhenItIsNotSetInTestCaseAnnotation123() throws Exception {
        ExecutionListener executionListener = new ExecutionListener();
        executionListener.testRunStarted(null);

        Method method = TestCaseAnnotationTest.class.getDeclaredMethod("shouldHaveTestCaseWithName");

        Description descriptionJQAT1 = Description.createTestDescription(this.getClass(), method.getName(), method.getAnnotations());
        executionListener.testFinished(descriptionJQAT1);

        executionListener.testRunFinished(null);

        CustomFormatContainer customFormatContainer = getZephyrScaleJUnitResults();

        assertEquals(1, customFormatContainer.getExecutions().size());
        CustomFormatExecution jqat1Result = customFormatContainer.getExecutions().get(0);
        assertNull(jqat1Result.getTestCase().getKey());
    }

    private CustomFormatContainer getZephyrScaleJUnitResults() throws IOException {
        File generatedResultFile = new File(CustomFormatFile.DEFAULT_ZEPHYRSCALE_RESULT_FILE_NAME);
        return new ObjectMapper().readValue(generatedResultFile, CustomFormatContainer.class);
    }
}
